using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class LaboratorioService
    {
        private readonly HospitalDbContext _context;

        public LaboratorioService(HospitalDbContext context)
        {
            _context = context;
        }
        
        public async Task<List<AreaLaboratorioDTO>> GetAreasAsync()
        {
            return await _context.AreasLaboratorio
                .Select(a => new AreaLaboratorioDTO
                {
                    Id = a.Id,
                    NombreArea = a.Nombre
                })
                .ToListAsync();
        }

        public async Task<OrdenLaboratorioReadDTO> CreateOrdenAsync(OrdenLaboratorioCreateDTO dto)
        {
            var nuevaOrden = new OrdenLaboratorio
            {
                IdConsulta = dto.IdConsulta,
                IdArea = dto.IdArea,
                Tipo_Examen = dto.TipoExamen,
                Fecha_solicitud = dto.FechaSolicitud,
                estado = dto.Estado,
                Resultado = "Pendiente de procesar" 
            };

            _context.OrdenesLaboratorio.Add(nuevaOrden);
            await _context.SaveChangesAsync();

            await _context.Entry(nuevaOrden).Reference(o => o.AreaLaboratorio).LoadAsync();

            return MapToReadDTO(nuevaOrden);
        }

        public async Task<List<OrdenLaboratorioReadDTO>> GetOrdenesPendientesAsync()
        {
            var ordenes = await _context.OrdenesLaboratorio
                .Include(o => o.AreaLaboratorio)
                .Where(o => o.estado == "Pendiente")
                .ToListAsync();

            return ordenes.Select(o => MapToReadDTO(o)).ToList();
        }

        public async Task<bool> UpdateResultadoAsync(OrdenLaboratorioResultDTO dto)
        {
            var orden = await _context.OrdenesLaboratorio.FindAsync(dto.IdOrden);
            if (orden == null) return false;

            orden.Resultado = dto.Resultado;
            orden.estado = dto.Estado;

            await _context.SaveChangesAsync();
            return true;
        }

        private OrdenLaboratorioReadDTO MapToReadDTO(OrdenLaboratorio o)
        {
            return new OrdenLaboratorioReadDTO
            {
                Id = o.Id,
                NombreArea = o.AreaLaboratorio?.Nombre ?? "N/A",
                TipoExamen = o.Tipo_Examen,
                Resultado = o.Resultado,
                Estado = o.estado,
                FechaSolicitud = o.Fecha_solicitud
            };
        }
    }
}