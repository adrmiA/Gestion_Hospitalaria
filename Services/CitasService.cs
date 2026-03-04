using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class CitasService
    {
        private readonly HospitalDbContext _context;

        public CitasService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<CitaReadDTO>> GetAllCitasAsync()
        {
            return await _context.Citas
                .Include(c => c.Paciente)
                .Include(c => c.Medico)
                    .ThenInclude(m => m.Usuario)
                .Select(c => MapToReadDTO(c))
                .ToListAsync();
        }

        public async Task<CitaReadDTO?> GetCitaByIdAsync(int id)
        {
            var cita = await _context.Citas
                .Include(c => c.Paciente)
                .Include(c => c.Medico)
                    .ThenInclude(m => m.Usuario)
                .FirstOrDefaultAsync(c => c.Id == id);

            return cita != null ? MapToReadDTO(cita) : null;
        }

        public async Task<List<CitaReadDTO>> GetCitasByFechaAsync(DateOnly fecha)
        {
            return await _context.Citas
                .Include(c => c.Paciente)
                .Include(c => c.Medico)
                    .ThenInclude(m => m.Usuario)
                .Where(c => c.Fecha == fecha)
                .Select(c => MapToReadDTO(c))
                .ToListAsync();
        }

        public async Task<CitaReadDTO> CreateCitaAsync(CitaCreateDTO dto)
        {
            var nuevaCita = new Cita
            {
                Fecha = dto.Fecha,
                Hora = dto.Hora,
                Estado = dto.Estado,
                IdPaciente = dto.IdPaciente,
                IdMedico = dto.IdMedico
            };

            _context.Citas.Add(nuevaCita);
            await _context.SaveChangesAsync();

            return await GetCitaByIdAsync(nuevaCita.Id);
        }

        public async Task<bool> UpdateCitaEstadoAsync(int id, string nuevoEstado)
        {
            var cita = await _context.Citas.FindAsync(id);
            if (cita == null) return false;

            cita.Estado = nuevoEstado;
            await _context.SaveChangesAsync();
            return true;
        }
        
        private static CitaReadDTO MapToReadDTO(Cita c)
        {
            return new CitaReadDTO
            {
                Id = c.Id,
                Fecha = c.Fecha,
                Hora = c.Hora,
                Estado = c.Estado,
                NombrePaciente = $"{c.Paciente.Nombre} {c.Paciente.Apellido}",
                NombreMedico = c.Medico.Usuario.Nombre,
                EspecialidadMedico = c.Medico.Especialidad
            };
        }
    }
}