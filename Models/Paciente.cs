using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("paciente")]
    public class Paciente
    {
        [Key]
        [Column("id_paciente")]
        public int Id { get; set; }
        [Column("nombre")]
        public string Nombre { get; set; }
        [Column("apellido")]
        public string Apellido { get; set; }
        [Column("dui")]
        public string DUI { get; set; }
        [Column("telefono")]
        public string Telefono { get; set; }
        [Column("fecha_nacimiento")]
        public DateOnly FechaNacimiento { get; set; } 
        [Column("estado")]
        public string Estado { get; set; }
    }
}
