using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class PacientesService
    {
        private readonly HospitalDbContext _context;

        public PacientesService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<PacienteReadDTO>> GetAllPacientesAsync()
        {
            var pacientes = await _context.Pacientes.ToListAsync();
            return pacientes.Select(p => MapToReadDTO(p)).ToList();
        }

        public async Task<PacienteReadDTO?> GetPacienteByIdAsync(int id)
        {
            var paciente = await _context.Pacientes.FindAsync(id);
            return paciente != null ? MapToReadDTO(paciente) : null;
        }

        public async Task<PacienteReadDTO?> GetPacienteByDuiAsync(string dui)
        {
            var paciente = await _context.Pacientes
                .FirstOrDefaultAsync(p => p.DUI == dui);
            return paciente != null ? MapToReadDTO(paciente) : null;
        }

        public async Task<PacienteReadDTO> CreatePacienteAsync(PacienteCreateDTO dto)
        {
            var nuevoPaciente = new Paciente
            {
                Nombre = dto.Nombre,
                Apellido = dto.Apellido,
                DUI = dto.DUI,
                Telefono = dto.Telefono,
                FechaNacimiento = dto.FechaNacimiento,
                Estado = dto.Estado
            };

            _context.Pacientes.Add(nuevoPaciente);
            await _context.SaveChangesAsync();

            return MapToReadDTO(nuevoPaciente);
        }

        public async Task<bool> UpdatePacienteAsync(int id, PacienteCreateDTO dto)
        {
            var paciente = await _context.Pacientes.FindAsync(id);
            if (paciente == null) return false;

            paciente.Nombre = dto.Nombre;
            paciente.Apellido = dto.Apellido;
            paciente.DUI = dto.DUI;
            paciente.Telefono = dto.Telefono;
            paciente.FechaNacimiento = dto.FechaNacimiento;
            paciente.Estado = dto.Estado;

            await _context.SaveChangesAsync();
            return true;
        }

        private PacienteReadDTO MapToReadDTO(Paciente p)
        {
            var hoy = DateOnly.FromDateTime(DateTime.Today);
            int edad = hoy.Year - p.FechaNacimiento.Year;
            if (p.FechaNacimiento > hoy.AddYears(-edad)) edad--;

            return new PacienteReadDTO
            {
                Id = p.Id,
                NombreCompleto = $"{p.Nombre} {p.Apellido}",
                DUI = p.DUI,
                Telefono = p.Telefono,
                Edad = edad,
                Estado = p.Estado
            };
        }
    }
}