using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("usuario")]
    public class Usuario
    {
        [Key, Column("id_usuario")]
        public int IdUsuario { get; set; }
        [Column("nombre")]
        public string Nombre { get; set; }
        [Column("usuario")]
        public string NombreUsuario { get; set; }
        [Column("contraseña")]
        public string Password { get; set; }

        [Column("id_rol")]
        public int IdRol { get; set; }
        [ForeignKey("IdRol")]
        public Rol Rol { get; set; }
    }
}
