using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("cita")]
    public class Cita
    {
        [Key, Column("id_cita")]
        public int Id { get; set; }
        [Column("fecha")]
        public DateOnly Fecha { get; set; }
        [Column("hora")]
        public TimeOnly Hora { get; set; }
        [Column("estado")]
        public string Estado { get; set; }

        [Column("id_paciente")]
        public int IdPaciente { get; set; }
        [ForeignKey("IdPaciente")]
        public Paciente Paciente { get; set; }

        [Column("id_medico")]
        public int IdMedico { get; set; }
        [ForeignKey("IdMedico")]
        public Medico Medico { get; set; }
    }
}
