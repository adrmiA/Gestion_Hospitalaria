using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("inventario")] 
    public class Inventario
    {
        [Key]
        [Column("id_inventario")] 
        public int Id { get; set; }

        [Column("stock_actual")]
        public int Stock_actual { get; set; }

        [Column("stock_minimo")]
        public int Stock_minimo { get; set; }

        [Column("stock_maximo")]
        public int Stock_maximo { get; set; }

        [Column("ubicacion")]
        public string Ubicacion { get; set; }

        [Column("fecha_actualizacion")]
        public DateOnly Fecha_actualizacion { get; set; }

        [Column("id_producto")]
        public int IdProducto { get; set; }

        [ForeignKey("IdProducto")]
        public Producto Producto { get; set; }
    }
}