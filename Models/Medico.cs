using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("medico")]
    public class Medico
    {
        [Key, Column("id_medico")]
        public int Id { get; set; }
        [Column("especialidad")]
        public string Especialidad { get; set; }

        [Column("id_usuario")]
        public int IdUsuario { get; set; }
        [ForeignKey("IdUsuario")]
        public Usuario Usuario { get; set; }
    }
}
