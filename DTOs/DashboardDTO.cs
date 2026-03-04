namespace GestionHospitalaria.DTOs
{
    public class DashboardStatsDTO
    {
        public int PacientesTotales { get; set; }
        public int CitasHoy { get; set; }
        public int CamasDisponibles { get; set; }
        public int ProductosStockBajo { get; set; }
    }
}