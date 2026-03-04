using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class ConsultasService
    {
        private readonly HospitalDbContext _context;

        public ConsultasService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<ConsultaReadDTO> CreateConsultaAsync(ConsultaCreateDTO dto)
        {
            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var nuevaConsulta = new Consulta
                {
                    Fecha = dto.Fecha,
                    Diagnostico = dto.Diagnostico,
                    IdExpediente = dto.IdExpediente,
                    IdMedico = dto.IdMedico
                };

                _context.Consultas.Add(nuevaConsulta);
                await _context.SaveChangesAsync(); 

                if (dto.Receta != null)
                {
                    var nuevaReceta = new Receta
                    {
                        IdConsulta = nuevaConsulta.Id,
                    };

                    _context.Recetas.Add(nuevaReceta);
                    await _context.SaveChangesAsync(); 

                    foreach (var prodDto in dto.Receta.Productos)
                    {
                        var recetaProducto = new RecetaProducto
                        {
                            IdReceta = nuevaReceta.Id,
                            IdProducto = prodDto.IdProducto,
                            Cantidad = prodDto.Cantidad,
                            Dosis = prodDto.Dosis
                        };
                        _context.RecetaProductos.Add(recetaProducto);
                    }
                    await _context.SaveChangesAsync();
                }

                await transaction.CommitAsync();

                return await GetConsultaByIdAsync(nuevaConsulta.Id);
            }
            catch (Exception)
            {
                await transaction.RollbackAsync();
                throw;
            }
        }

        public async Task<ConsultaReadDTO?> GetConsultaByIdAsync(int id)
        {
            var c = await _context.Consultas
                .Include(c => c.Medico).ThenInclude(m => m.Usuario)
                .Include(c => c.Expediente).ThenInclude(e => e.Paciente)
                .FirstOrDefaultAsync(c => c.Id == id);

            if (c == null) return null;

            return new ConsultaReadDTO
            {
                Id = c.Id,
                Fecha = c.Fecha,
                Diagnostico = c.Diagnostico,
                NombreMedico = c.Medico.Usuario.Nombre,
                NombrePaciente = $"{c.Expediente.Paciente.Nombre} {c.Expediente.Paciente.Apellido}"
            };
        }

        public async Task<List<ConsultaReadDTO>> GetHistorialByExpedienteAsync(int idExpediente)
        {
            return await _context.Consultas
                .Include(c => c.Medico).ThenInclude(m => m.Usuario)
                .Include(c => c.Expediente).ThenInclude(e => e.Paciente)
                .Where(c => c.IdExpediente == idExpediente)
                .OrderByDescending(c => c.Fecha)
                .Select(c => new ConsultaReadDTO
                {
                    Id = c.Id,
                    Fecha = c.Fecha,
                    Diagnostico = c.Diagnostico,
                    NombreMedico = c.Medico.Usuario.Nombre,
                    NombrePaciente = $"{c.Expediente.Paciente.Nombre} {c.Expediente.Paciente.Apellido}"
                })
                .ToListAsync();
        }
        public async Task<List<ConsultaReadDTO>> GetAllConsultasAsync()
        {
            return await _context.Consultas
                .Include(c => c.Medico).ThenInclude(m => m.Usuario)
                .Include(c => c.Expediente).ThenInclude(e => e.Paciente)
                .Select(c => new ConsultaReadDTO
                {
                    Id = c.Id,
                    Fecha = c.Fecha,
                    Diagnostico = c.Diagnostico,
                    NombreMedico = c.Medico.Usuario.Nombre,
                    NombrePaciente = c.Expediente.Paciente.Nombre + " " + c.Expediente.Paciente.Apellido,
                    Costo = 25.00m 
                })
                .ToListAsync();
        }
        public async Task<List<ConsultaReadDTO>> GetAllConsultasReporteAsync()
        {
            return await _context.Consultas
                .Include(c => c.Medico).ThenInclude(m => m.Usuario)
                .Include(c => c.Expediente).ThenInclude(e => e.Paciente)
                .Select(c => new ConsultaReadDTO
                {
                    Id = c.Id,
                    Fecha = c.Fecha,
                    Diagnostico = c.Diagnostico,
                    NombreMedico = c.Medico.Usuario.Nombre,
                    NombrePaciente = c.Expediente.Paciente.Nombre + " " + c.Expediente.Paciente.Apellido,
                    Costo = 25.00m 
                }).ToListAsync();
        }
    }
}