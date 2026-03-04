namespace GestionHospitalaria.DTOs
{
    public class PacienteCreateDTO
    {
        public string Nombre { get; set; }
        public string Apellido { get; set; }
        public string DUI { get; set; }
        public string Telefono { get; set; }
        public DateOnly FechaNacimiento { get; set; }
        public string Estado { get; set; }
    }

    public class PacienteReadDTO
    {
        public int Id { get; set; }
        public string NombreCompleto { get; set; } 
        public string DUI { get; set; }
        public string Telefono { get; set; }
        public int Edad { get; set; }
        public string Estado { get; set; }
    }
}