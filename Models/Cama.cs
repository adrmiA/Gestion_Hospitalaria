using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("Cama")]
    public class Cama
    {
        [Key]
        [Column("id_cama")] 
        public int Id { get; set; }

        [Required]
        [Column("numero")] 
        public string Numero { get; set; }

        [Column("sala")]
        public string Sala { get; set; }

        [Column("estado")]
        public string Estado { get; set; }
    }
}