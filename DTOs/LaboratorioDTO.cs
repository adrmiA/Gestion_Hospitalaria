namespace GestionHospitalaria.DTOs
{
    public class OrdenLaboratorioCreateDTO
    {
        public int IdConsulta { get; set; }
        public int IdArea { get; set; }
        public string TipoExamen { get; set; }
        public DateOnly FechaSolicitud { get; set; }
        public string Estado { get; set; }
    }

    public class OrdenLaboratorioReadDTO
    {
        public int Id { get; set; }
        public string NombreArea { get; set; }
        public string TipoExamen { get; set; }
        public string Resultado { get; set; }
        public string Estado { get; set; }
        public DateOnly FechaSolicitud { get; set; }
    }
}