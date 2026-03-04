namespace GestionHospitalaria.DTOs
{
    public class UsuarioLoginDTO
    {
        public string Usuario { get; set; }
        public string Contraseña { get; set; }
    }

    public class UsuarioCreateDTO
    {
        public string Nombre { get; set; }
        public string Usuario { get; set; }
        public string Contraseña { get; set; }
        public int IdRol { get; set; }
    }

    public class UsuarioReadDTO
    {
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string Usuario { get; set; }
        public string NombreRol { get; set; }
    }
}