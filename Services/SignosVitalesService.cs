using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class SignosVitalesService
    {
        private readonly HospitalDbContext _context;

        public SignosVitalesService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<SignosVitalesDTO>> GetByExpedienteAsync(int idExpediente)
        {
            return await _context.SignosVitales
                .Where(s => s.IdExpediente == idExpediente)
                .OrderByDescending(s => s.FechaRegistro)
                .Select(s => new SignosVitalesDTO
                {
                    IdExpediente = s.IdExpediente,
                    FrecuenciaCardiaca = s.FrecuenciaCardiaca,
                    FrecuenciaRespiratoria = s.FrecuenciaRespiratoria,
                    Temperatura = s.Temperatura,
                    PresionArterial = s.PresionArterial,
                    SaturacionOxigeno = s.SaturacionOxigeno,
                    Peso = s.Peso,
                    Talla = s.Talla,
                    FechaRegistro = s.FechaRegistro
                })
                .ToListAsync();
        }

        public async Task<bool> CreateAsync(SignosVitalesDTO dto)
        {
            try
            {
                var nuevo = new SignosVitales
                {
                    IdExpediente = dto.IdExpediente,
                    FrecuenciaCardiaca = dto.FrecuenciaCardiaca,
                    FrecuenciaRespiratoria = dto.FrecuenciaRespiratoria,
                    Temperatura = dto.Temperatura,
                    PresionArterial = dto.PresionArterial,
                    SaturacionOxigeno = dto.SaturacionOxigeno,
                    Peso = dto.Peso,
                    Talla = dto.Talla,
                    FechaRegistro = DateTime.Now
                };

                _context.SignosVitales.Add(nuevo);
                await _context.SaveChangesAsync();
                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error en SignosVitalesService: " + ex.Message);
                return false;
            }
        }
    }
}