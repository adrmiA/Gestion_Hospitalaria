namespace GestionHospitalaria.DTOs
{
    public class CitaCreateDTO
    {
        public DateOnly Fecha { get; set; }
        public TimeOnly Hora { get; set; }
        public string Estado { get; set; }
        public int IdPaciente { get; set; }
        public int IdMedico { get; set; }
    }

    public class CitaReadDTO
    {
        public int Id { get; set; }
        public DateOnly Fecha { get; set; }
        public TimeOnly Hora { get; set; }
        public string Estado { get; set; }
        public string NombrePaciente { get; set; }
        public string NombreMedico { get; set; }
        public string EspecialidadMedico { get; set; }
    }
}