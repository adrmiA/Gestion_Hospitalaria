namespace GestionHospitalaria.DTOs
{
    public class ExpedienteReadDTO
    {
        public int Id { get; set; }
        public DateTime FechaApertura { get; set; }
        public string NombrePaciente { get; set; }
        public string DUI { get; set; }
        public List<ConsultaResumenDTO> HistorialConsultas { get; set; }
    }

    public class ConsultaResumenDTO
    {
        public int IdConsulta { get; set; }
        public DateTime Fecha { get; set; }
        public string Diagnostico { get; set; }
        public string Medico { get; set; }
    }
}