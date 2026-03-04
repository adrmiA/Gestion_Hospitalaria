using GestionHospitalaria.Data;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Models;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class InventarioService
    {
        private readonly HospitalDbContext _context;

        public InventarioService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<ProductoDTO>> GetAllProductosAsync()
        {
            return await _context.Productos
                .Select(p => new ProductoDTO
                {
                    Id = p.Id,
                    Nombre = p.Nombre,
                    TipoProducto = p.Tipo_producto,
                    UnidadMedida = p.unidad_medida, 
                    Precio = p.Precio,
                    FechaVencimiento = p.Fecha_vencimiento,
                    Estado = p.estado,
                    StockActual = _context.Inventarios
                        .Where(i => i.IdProducto == p.Id)
                        .Select(i => i.Stock_actual)
                        .FirstOrDefault()
                }).ToListAsync();
        }

        public async Task<List<InventarioStatusDTO>> GetInventarioStatusAsync()
        {
            var inventarios = await _context.Inventarios
                .Include(i => i.Producto)
                .ToListAsync();

            return inventarios.Select(i => MapToStatusDTO(i)).ToList();
        }

        public async Task<bool> RegisterMovimientoAsync(MovimientoInventarioDTO dto)
        {
            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var inventario = await _context.Inventarios
                    .FirstOrDefaultAsync(i => i.IdProducto == dto.IdProducto);

                if (inventario == null)
                {
                    inventario = new Inventario
                    {
                        IdProducto = dto.IdProducto,
                        Stock_actual = 0,
                        Stock_minimo = 10,
                        Stock_maximo = 100,
                        Ubicacion = "Por asignar",
                        Fecha_actualizacion = DateOnly.FromDateTime(DateTime.Now)
                    };
                    _context.Inventarios.Add(inventario);
                    await _context.SaveChangesAsync();
                }

                if (dto.TipoMovimiento.ToUpper() == "ENTRADA")
                {
                    inventario.Stock_actual += dto.Cantidad;
                }
                else if (dto.TipoMovimiento.ToUpper() == "SALIDA")
                {
                    if (inventario.Stock_actual < dto.Cantidad) return false;
                    inventario.Stock_actual -= dto.Cantidad;
                }
                else return false;

                inventario.Fecha_actualizacion = DateOnly.FromDateTime(DateTime.Now);

                var movimiento = new MovimientoInventario
                {
                    IdInventario = inventario.Id,
                    Tipo = dto.TipoMovimiento,
                    Cantidad = dto.Cantidad,
                    Motivo = dto.Motivo,
                    Fecha = DateTime.Now
                };

                _context.MovimientosInventario.Add(movimiento);
                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                return true;
            }
            catch (Exception)
            {
                await transaction.RollbackAsync();
                return false;
            }
        }

        public async Task<List<InventarioStatusDTO>> GetProductosBajoStockAsync()
        {
            var bajos = await _context.Inventarios
                .Include(i => i.Producto)
                .Where(i => i.Stock_actual <= i.Stock_minimo)
                .ToListAsync();

            return bajos.Select(i => MapToStatusDTO(i)).ToList();
        }

        private InventarioStatusDTO MapToStatusDTO(Inventario i)
        {
            string estado;
            if (i.Stock_actual <= i.Stock_minimo) estado = "Bajo";
            else if (i.Stock_actual >= i.Stock_maximo) estado = "Exceso";
            else estado = "Normal";

            return new InventarioStatusDTO
            {
                IdInventario = i.Id, 
                IdProducto = i.IdProducto,
                NombreProducto = i.Producto.Nombre,
                StockActual = i.Stock_actual,
                StockMinimo = i.Stock_minimo, 
                StockMaximo = i.Stock_maximo, 
                Ubicacion = i.Ubicacion,
                EstadoStock = estado
            };
        }

    }
}