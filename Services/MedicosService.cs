using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class MedicosService
    {
        private readonly HospitalDbContext _context;

        public MedicosService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<MedicoReadDTO>> GetAllMedicosAsync()
        {
            return await _context.Medicos
                .Include(m => m.Usuario) 
                .Select(m => new MedicoReadDTO
                {
                    Id = m.Id,
                    NombreMedico = m.Usuario.Nombre,
                    Especialidad = m.Especialidad
                })
                .ToListAsync();
        }

        public async Task<MedicoReadDTO?> GetMedicoByIdAsync(int id)
        {
            var m = await _context.Medicos
                .Include(m => m.Usuario)
                .FirstOrDefaultAsync(m => m.Id == id);

            if (m == null) return null;

            return new MedicoReadDTO
            {
                Id = m.Id,
                NombreMedico = m.Usuario.Nombre,
                Especialidad = m.Especialidad
            };
        }

        public async Task<MedicoReadDTO> CreateMedicoAsync(MedicoCreateDTO dto)
        {
            var nuevoMedico = new Medico
            {
                Especialidad = dto.Especialidad,
                IdUsuario = dto.IdUsuario
            };

            _context.Medicos.Add(nuevoMedico);
            await _context.SaveChangesAsync();

            await _context.Entry(nuevoMedico).Reference(m => m.Usuario).LoadAsync();

            return new MedicoReadDTO
            {
                Id = nuevoMedico.Id,
                NombreMedico = nuevoMedico.Usuario.Nombre,
                Especialidad = nuevoMedico.Especialidad
            };
        }

        public async Task<List<MedicoReadDTO>> GetMedicosByEspecialidadAsync(string especialidad)
        {
            return await _context.Medicos
                .Include(m => m.Usuario)
                .Where(m => m.Especialidad.Contains(especialidad))
                .Select(m => new MedicoReadDTO
                {
                    Id = m.Id,
                    NombreMedico = m.Usuario.Nombre,
                    Especialidad = m.Especialidad
                })
                .ToListAsync();
        }
    }
}