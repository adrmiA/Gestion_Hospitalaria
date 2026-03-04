namespace GestionHospitalaria.DTOs
{
    public class OrdenLaboratorioResultDTO
    {
        public int IdOrden { get; set; }
        public string Resultado { get; set; }
        public string Estado { get; set; } 
    }

    public class AreaLaboratorioDTO
    {
        public int Id { get; set; }
        public string NombreArea { get; set; }
    }
}