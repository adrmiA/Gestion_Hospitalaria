using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;

namespace GestionHospitalaria.Models
{
    [Table("movimiento_inventario")]
    public class MovimientoInventario
    {
        [Key]
        [Column("id_movimiento")]
        public int Id { get; set; }

        [Column("id_inventario")]
        public int IdInventario { get; set; }
        [ForeignKey("IdInventario")]
        public Inventario Inventario { get; set; }

        [Column("id_proveedor")]
        public int? IdProveedor { get; set; }
        [ForeignKey("IdProveedor")]
        public Proveedor Proveedor { get; set; }

        [Column("tipo_movimiento")]
        public string Tipo { get; set; }

        [Column("cantidad")]
        public int Cantidad { get; set; }
        [Column("motivo")]
        public string Motivo { get; set; }
        [Column("fecha")]
        public DateTime Fecha { get; set; }
    }
}
