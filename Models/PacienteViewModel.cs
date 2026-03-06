namespace GestionHospitalariaWEB.Models 
{
    public class SolicitarCitaVM
    {
        public string Dui { get; set; }
        public string Fecha { get; set; }
        public string Hora { get; set; }
        public int IdMedico { get; set; }
        public List<MedicoDTO> Medicos { get; set; } = new();
        public string? MensajeError { get; set; }
        public string? MensajeExito { get; set; }
    }

    public class InformeMedicoVM
    {
        public string Dui { get; set; }
        public ExpedienteDTO? Expediente { get; set; }
        public string? MensajeError { get; set; }
    }

    public class MedicoDTO
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string Especialidad { get; set; }
    }

    public class ExpedienteDTO
    {
        public int Id { get; set; }
        public string DUI { get; set; }
        public string NombrePaciente { get; set; }
        public string FechaApertura { get; set; }
        public List<ConsultaDTO> HistorialConsultas { get; set; } = new();
    }

    public class ConsultaDTO
    {
        public string Fecha { get; set; }
        public string Medico { get; set; }
        public string Diagnostico { get; set; }
    }
}