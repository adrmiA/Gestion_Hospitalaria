using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("expediente")]
    public class Expediente
    {
        [Key]
        [Column("id_expediente")]
        public int Id { get; set; }

        public DateOnly Fecha_apertura { get; set; }

        [Column("id_paciente")]
        public int IdPaciente { get; set; }
        [ForeignKey("IdPaciente")]
        public Paciente Paciente { get; set; }
    }
}
