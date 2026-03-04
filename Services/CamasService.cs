using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class CamasService
    {
        private readonly HospitalDbContext _context;

        public CamasService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<CamaDTO>> GetAllCamasAsync()
        {
            return await _context.Camas
                .Select(c => new CamaDTO
                {
                    Id = c.Id,
                    Numero = c.Numero,
                    Sala = c.Sala,
                    Estado = c.Estado
                }).ToListAsync();
        }

        public async Task<List<CamaDTO>> GetCamasDisponiblesAsync()
        {
            return await _context.Camas
                .Where(c => c.Estado == "Disponible")
                .Select(c => new CamaDTO
                {
                    Id = c.Id,
                    Numero = c.Numero,
                    Sala = c.Sala,
                    Estado = c.Estado
                }).ToListAsync();
        }

        public async Task<OcupacionReadDTO?> OcuparCamaAsync(OcupacionCreateDTO dto)
        {
            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var cama = await _context.Camas.FindAsync(dto.IdCama);

                if (cama == null || cama.Estado != "Disponible") return null;

                cama.Estado = "Ocupada";
            
                var nuevaOcupacion = new Ocupacion
                {
                    IdPaciente = dto.IdPaciente,
                    IdCama = dto.IdCama,
                    Fecha_ingreso = dto.FechaIngreso
                };

                _context.Ocupaciones.Add(nuevaOcupacion);
                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                await _context.Entry(nuevaOcupacion).Reference(o => o.Paciente).LoadAsync();
                await _context.Entry(nuevaOcupacion).Reference(o => o.Cama).LoadAsync();

                return new OcupacionReadDTO
                {
                    Id = nuevaOcupacion.Id,
                    NombrePaciente = $"{nuevaOcupacion.Paciente.Nombre} {nuevaOcupacion.Paciente.Apellido}",
                    NumeroCama = nuevaOcupacion.Cama.Numero,
                    Sala = nuevaOcupacion.Cama.Sala,
                    FechaIngreso = nuevaOcupacion.Fecha_ingreso
                };
            }
            catch
            {
                await transaction.RollbackAsync();
                return null;
            }
        }

        public async Task<bool> LiberarCamaAsync(int idCama)
        {
            using var transaction = await _context.Database.BeginTransactionAsync();
            try
            {
                var cama = await _context.Camas.FindAsync(idCama);

                if (cama == null || !cama.Estado.Trim().Equals("Ocupada", StringComparison.OrdinalIgnoreCase))
                    return false;

                var ocupacion = await _context.Ocupaciones
                    .Where(o => o.IdCama == idCama && o.Fecha_salida == null)
                    .OrderByDescending(o => o.Fecha_ingreso)
                    .FirstOrDefaultAsync();

                if (ocupacion != null)
                {
                    ocupacion.Fecha_salida = DateTime.Now;
                }

                cama.Estado = "Disponible";

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();
                return true;
            }
            catch (Exception ex)
            {
                await transaction.RollbackAsync();
                Console.WriteLine("ERROR: " + ex.Message);
                return false;
            }
        }
    }
}