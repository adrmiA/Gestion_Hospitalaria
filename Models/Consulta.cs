using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("consulta")] 
    public class Consulta
    {
        [Key]
        [Column("id_consulta")]
        public int Id { get; set; }

        [Column("fecha")]
        public DateTime Fecha { get; set; }

        [Column("diagnostico")]
        public string Diagnostico { get; set; }

        [Column("id_expediente")]
        public int IdExpediente { get; set; }

        [ForeignKey("IdExpediente")]
        public Expediente Expediente { get; set; }

        [Column("id_medico")]
        public int IdMedico { get; set; }

        [ForeignKey("IdMedico")]
        public Medico Medico { get; set; }
    }
}