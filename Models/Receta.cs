using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("receta")]
    public class Receta
    {
        public int Id { get; set; }
        public string Duracion { get; set; }
        public string Frecuencia { get; set; }
        public int IdConsulta { get; set; }
        [ForeignKey("IdConsulta")]
        public Consulta Consulta { get; set; }
    }
}
