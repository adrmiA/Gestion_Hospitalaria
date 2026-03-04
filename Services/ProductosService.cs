using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs; 
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class ProductosService
    {
        private readonly HospitalDbContext _context;

        public ProductosService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<ProductoDTO>> GetAllAsync()
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

        public async Task<ProductoDTO?> GetByIdAsync(int id)
        {
            var p = await _context.Productos.FindAsync(id);
            if (p == null) return null;

            return new ProductoDTO
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
            };
        }

        public async Task<ProductoDTO> CreateAsync(ProductoDTO dto)
        {
            var nuevo = new Producto
            {
                Nombre = dto.Nombre,
                Tipo_producto = dto.TipoProducto,
                Precio = dto.Precio,
                Fecha_vencimiento = dto.FechaVencimiento,
                estado = dto.Estado,
                Descripcion = "",
                unidad_medida = dto.UnidadMedida ?? "Unidad"
            };
            _context.Productos.Add(nuevo);
            await _context.SaveChangesAsync();
            dto.Id = nuevo.Id;
            return dto;
        }

        public async Task<bool> UpdateAsync(int id, ProductoDTO dto)
        {
            var p = await _context.Productos.FindAsync(id);
            if (p == null) return false;
            p.Nombre = dto.Nombre;
            p.Tipo_producto = dto.TipoProducto;
            p.Precio = dto.Precio;
            p.Fecha_vencimiento = dto.FechaVencimiento;
            p.estado = dto.Estado;
            p.unidad_medida = dto.UnidadMedida;
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(int id)
        {
            var p = await _context.Productos.FindAsync(id);
            if (p == null) return false;
            _context.Productos.Remove(p);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}