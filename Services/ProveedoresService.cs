using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using Microsoft.EntityFrameworkCore;

namespace GestionHospitalaria.Services
{
    public class ProveedoresService
    {
        private readonly HospitalDbContext _context;

        public ProveedoresService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<ProveedorDTO>> GetAllAsync()
        {
            return await _context.Proveedores
                .Select(p => new ProveedorDTO
                {
                    Id = p.Id,
                    Nombre = p.Nombre,
                    Telefono = p.Telefono,
                    Correo = p.Correo,
                    Estado = p.estado 
                })
                .ToListAsync();
        }

        public async Task<ProveedorDTO> GetByIdAsync(int id)
        {
            var p = await _context.Proveedores.FindAsync(id);
            if (p == null) return null;

            return new ProveedorDTO
            {
                Id = p.Id,
                Nombre = p.Nombre,
                Telefono = p.Telefono,
                Correo = p.Correo,
                Estado = p.estado
            };
        }

        public async Task<ProveedorDTO> CreateAsync(ProveedorDTO proveedorDto)
        {
            var nuevoProveedor = new Proveedor
            {
                Nombre = proveedorDto.Nombre,
                Telefono = proveedorDto.Telefono,
                Correo = proveedorDto.Correo,
                estado = proveedorDto.Estado,
                Direccion = "" 
            };

            _context.Proveedores.Add(nuevoProveedor);
            await _context.SaveChangesAsync();

            proveedorDto.Id = nuevoProveedor.Id;
            return proveedorDto;
        }

        public async Task<bool> UpdateAsync(int id, ProveedorDTO proveedorDto)
        {
            var proveedor = await _context.Proveedores.FindAsync(id);
            if (proveedor == null) return false;

            proveedor.Nombre = proveedorDto.Nombre;
            proveedor.Telefono = proveedorDto.Telefono;
            proveedor.Correo = proveedorDto.Correo;
            proveedor.estado = proveedorDto.Estado;

            await _context.SaveChangesAsync();
            return true;
        }
    }
}