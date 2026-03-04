using Microsoft.EntityFrameworkCore;
using GestionHospitalaria.Models;

namespace GestionHospitalaria.Data
{
    public class HospitalDbContext : DbContext
    {
        public HospitalDbContext(DbContextOptions<HospitalDbContext> options) : base(options) { }

        public DbSet<Rol> Roles { get; set; }
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<Medico> Medicos { get; set; }
        public DbSet<Paciente> Pacientes { get; set; }
        public DbSet<Expediente> Expedientes { get; set; }
        public DbSet<Consulta> Consultas { get; set; }
        public DbSet<Cita> Citas { get; set; }
        public DbSet<Receta> Recetas { get; set; }
        public DbSet<AreaLaboratorio> AreasLaboratorio { get; set; }
        public DbSet<OrdenLaboratorio> OrdenesLaboratorio { get; set; }
        public DbSet<Producto> Productos { get; set; }
        public DbSet<Inventario> Inventarios { get; set; }
        public DbSet<MovimientoInventario> MovimientosInventario { get; set; }
        public DbSet<Proveedor> Proveedores { get; set; }
        public DbSet<Cama> Camas { get; set; }
        public DbSet<Ocupacion> Ocupaciones { get; set; }
        public DbSet<RecetaProducto> RecetaProductos { get; set; }
        public DbSet<OrdenProducto> OrdenProductos { get; set; }
        public DbSet<SignosVitales>
            SignosVitales
        { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<RecetaProducto>()
                .HasKey(rp => new { rp.IdReceta, rp.IdProducto });

            modelBuilder.Entity<OrdenProducto>()
                .HasKey(op => new { op.Id, op.IdProducto });

            modelBuilder.Entity<AreaLaboratorio>(entity =>
            {
                entity.ToTable("area_laboratorio");
                entity.Property(e => e.Id).HasColumnName("id_area");

                entity.Property(e => e.Nombre).HasColumnName("nombre_area");
            });

            modelBuilder.Entity<OrdenLaboratorio>(entity =>
            {
                entity.Property(e => e.Id).HasColumnName("id_orden");
                entity.Property(e => e.Tipo_Examen).HasColumnName("tipo_examen");
                entity.Property(e => e.Fecha_solicitud).HasColumnName("fecha_solicitud");
            });

            base.OnModelCreating(modelBuilder);
        }
    }
}