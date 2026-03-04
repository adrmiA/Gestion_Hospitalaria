using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("area_laboratorio")]
    public class AreaLaboratorio
    {
        [Key]
        [Column("id_area")]
        public int Id { get; set; }

        [Required]
        public string Nombre { get; set; }
    }
}
