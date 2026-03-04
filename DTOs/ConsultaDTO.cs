namespace GestionHospitalaria.DTOs
{
    public class ConsultaCreateDTO
    {
        public DateTime Fecha { get; set; }
        public string Diagnostico { get; set; }
        public int IdExpediente { get; set; }
        public int IdMedico { get; set; }
        public RecetaCreateDTO? Receta { get; set; }
    }

    public class RecetaCreateDTO
    {
        public string Duracion { get; set; }
        public string Frecuencia { get; set; }
        public List<RecetaProductoDTO> Productos { get; set; }
    }

    public class RecetaProductoDTO
    {
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }
        public string Dosis { get; set; }
    }

    public class ConsultaReadDTO
    {
        public int Id { get; set; }
        public DateTime Fecha { get; set; }
        public string Diagnostico { get; set; }
        public string NombrePaciente { get; set; }
        public string NombreMedico { get; set; }
        public decimal Costo { get; set; } = 25.00m; 
    }

}