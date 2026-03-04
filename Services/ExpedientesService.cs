using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class ExpedientesService
    {
        private readonly HospitalDbContext _context;

        public ExpedientesService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<ExpedienteReadDTO?> GetExpedienteFullByIdAsync(int id)
        {
            var expediente = await _context.Expedientes
                .Include(e => e.Paciente)
                .FirstOrDefaultAsync(e => e.Id == id);

            if (expediente == null) return null;

            return await MapToFullDTO(expediente);
        }

        public async Task<ExpedienteReadDTO?> GetExpedienteByPacienteIdAsync(int idPaciente)
        {
            var expediente = await _context.Expedientes
                .Include(e => e.Paciente)
                .FirstOrDefaultAsync(e => e.IdPaciente == idPaciente);

            if (expediente == null) return null;

            return await MapToFullDTO(expediente);
        }

        public async Task<ExpedienteReadDTO?> GetExpedienteByDuiAsync(string dui)
        {
            var expediente = await _context.Expedientes
                .Include(e => e.Paciente)
                .FirstOrDefaultAsync(e => e.Paciente.DUI == dui);

            if (expediente == null) return null;

            return await MapToFullDTO(expediente);
        }

        private async Task<ExpedienteReadDTO> MapToFullDTO(Expediente e)
        {
            var consultas = await _context.Consultas
                .Include(c => c.Medico)
                    .ThenInclude(m => m.Usuario)
                .Where(c => c.IdExpediente == e.Id) 
                .OrderByDescending(c => c.Fecha)
                .Select(c => new ConsultaResumenDTO
                {
                    IdConsulta = c.Id,
                    Fecha = c.Fecha,
                    Diagnostico = c.Diagnostico,
                    Medico = c.Medico.Usuario.Nombre
                })
                .ToListAsync();

            return new ExpedienteReadDTO
            {
                Id = e.Id,
                FechaApertura = e.Fecha_apertura.ToDateTime(TimeOnly.MinValue),
                NombrePaciente = $"{e.Paciente.Nombre} {e.Paciente.Apellido}",
                DUI = e.Paciente.DUI,
                HistorialConsultas = consultas
            };
        }
    }
}