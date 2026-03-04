using GestionHospitalaria.Data;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class DashboardService
    {
        private readonly HospitalDbContext _context;

        public DashboardService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<DashboardStatsDTO> GetGlobalStatsAsync()
        {
            var hoy = DateOnly.FromDateTime(DateTime.Today);

            var pacientesTotales = await _context.Pacientes.CountAsync();

            var citasHoy = await _context.Citas.CountAsync(c => c.Fecha == hoy);

            var camasDisponibles = await _context.Camas.CountAsync(c => c.Estado == "Disponible");

            var stockBajo = await _context.Inventarios.CountAsync(i => i.Stock_actual <= i.Stock_minimo);

            return new DashboardStatsDTO
            {
                PacientesTotales = pacientesTotales,
                CitasHoy = citasHoy,
                CamasDisponibles = camasDisponibles,
                ProductosStockBajo = stockBajo
            };
        }
    }
}