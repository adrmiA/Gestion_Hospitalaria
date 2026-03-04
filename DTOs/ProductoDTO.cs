namespace GestionHospitalaria.DTOs
{
    public class ProductoDTO
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string TipoProducto { get; set; }
        public string UnidadMedida { get; set; }
        public decimal Precio { get; set; }
        public DateOnly FechaVencimiento { get; set; }
        public string Estado { get; set; }
        public int StockActual { get; set; }
    }
 

    public class MovimientoInventarioDTO
    {
        public int IdProducto { get; set; }
        public int Cantidad { get; set; }
        public string TipoMovimiento { get; set; } 
        public string Motivo { get; set; }
        public int? IdProveedor { get; set; }
    }

    public class InventarioStatusDTO
    {
        public int IdInventario { get; set; } 
        public int IdProducto { get; set; } 
        public string NombreProducto { get; set; }
        public int StockActual { get; set; }
        public int StockMinimo { get; set; } 
        public int StockMaximo { get; set; } 
        public string Ubicacion { get; set; }
        public string EstadoStock { get; set; }
    }
}