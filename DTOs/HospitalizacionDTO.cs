namespace GestionHospitalaria.DTOs
{
    public class CamaDTO
    {
        public int Id { get; set; }
        public string Numero { get; set; }
        public string Sala { get; set; }
        public string Estado { get; set; }
    }

    public class OcupacionCreateDTO
    {
        public int IdPaciente { get; set; }
        public int IdCama { get; set; }
        public DateTime FechaIngreso { get; set; }
    }

    public class OcupacionReadDTO
    {
        public int Id { get; set; }
        public string NombrePaciente { get; set; }
        public string NumeroCama { get; set; }
        public string Sala { get; set; }
        public DateTime FechaIngreso { get; set; }
    }
}