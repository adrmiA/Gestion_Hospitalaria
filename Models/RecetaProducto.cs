using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("receta_producto")]
    public class RecetaProducto
    {
        [Column("id_receta")]
        public int IdReceta { get; set; }
        [ForeignKey("IdReceta")]
        public Receta Receta { get; set; }

        [Column("id_producto")]
        public int IdProducto { get; set; }
        [ForeignKey("IdProducto")]
        public Producto Producto { get; set; }

        [Column("cantidad")]
        public int Cantidad { get; set; }
        [Column("dosis")]
        public string Dosis { get; set; }
    }
}
