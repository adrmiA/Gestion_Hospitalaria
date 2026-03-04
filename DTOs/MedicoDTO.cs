namespace GestionHospitalaria.DTOs
{
    public class MedicoCreateDTO
    {
        public string Especialidad { get; set; }
        public int IdUsuario { get; set; }
    }

    public class MedicoReadDTO
    {
        public int Id { get; set; }
        public string NombreMedico { get; set; } 
        public string Especialidad { get; set; }
    }
}