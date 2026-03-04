namespace GestionHospitalaria.DTOs
{
    public class SignosVitalesDTO
    {
        public int IdExpediente { get; set; }
        public string FrecuenciaCardiaca { get; set; }
        public string FrecuenciaRespiratoria { get; set; }
        public string Temperatura { get; set; }
        public string PresionArterial { get; set; }
        public string SaturacionOxigeno { get; set; }
        public string Peso { get; set; }
        public string Talla { get; set; }
        public DateTime? FechaRegistro { get; set; }
    }
}