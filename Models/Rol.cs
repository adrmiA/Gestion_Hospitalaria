using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("rol")]
    public class Rol
    {
        [Key, Column("id_rol")]
        public int Id { get; set; }
        [Column("nombre")]
        public string Nombre { get; set; }
    }
}
