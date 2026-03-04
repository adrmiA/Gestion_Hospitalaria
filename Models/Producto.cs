using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("producto")]
    public class Producto
    {
        [Key]
        [Column("id_producto")] 
        public int Id { get; set; }

        [Column("nombre")]
        public string Nombre { get; set; }

        [Column("tipo_producto")]
        public string Tipo_producto { get; set; }

        [Column("descripcion")]
        public string Descripcion { get; set; }

        [Column("unidad_medida")]
        public string unidad_medida { get; set; }

        [Column("precio_unitario")]
        public decimal Precio { get; set; }

        [Column("fecha_vencimiento")]
        public DateOnly Fecha_vencimiento { get; set; }

        [Column("estado")]
        public string estado { get; set; }
    }
}