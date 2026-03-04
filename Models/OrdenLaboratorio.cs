using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("orden_laboratorio")]
    public class OrdenLaboratorio
    {
        public int Id { get; set; }
        public string Tipo_Examen { get; set; }
        public DateOnly Fecha_solicitud { get; set; }
        public string Resultado { get; set; }
        public string estado { get; set; }
        public int IdConsulta { get; set; }
        [ForeignKey("IdConsulta")]
        public Consulta Consulta { get; set; }

        public int IdArea { get; set; }
        [ForeignKey("IdArea")]
        public AreaLaboratorio AreaLaboratorio { get; set; }
    }
}
