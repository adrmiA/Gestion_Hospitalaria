using GestionHospitalaria.Data;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace GestionHospitalaria.Services
{
    public class UsuariosService
    {
        private readonly HospitalDbContext _context;
        private readonly IConfiguration _config;

        public UsuariosService(HospitalDbContext context, IConfiguration config)
        {
            _context = context;
            _config = config;
        }

        public async Task<string> Authenticate(UsuarioLoginDTO loginDto)
        {
            var usuario = await _context.Usuarios
                .Include(u => u.Rol)
                .FirstOrDefaultAsync(u => u.NombreUsuario == loginDto.Usuario && u.Password == loginDto.Contraseña);

            if (usuario == null) return null;

            // Generación de Token JWT
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_config["Jwt:Key"]);
            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[] {
                    new Claim(ClaimTypes.Name, usuario.NombreUsuario),
                    new Claim(ClaimTypes.Role, usuario.Rol.Nombre)
                }),
                Expires = DateTime.UtcNow.AddDays(1),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };
            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }

        public async Task<List<UsuarioReadDTO>> GetUsuariosAsync()
        {
            return await _context.Usuarios
                .Include(u => u.Rol)
                .Select(u => new UsuarioReadDTO
                {
                    Id = u.IdUsuario,
                    Nombre = u.Nombre,
                    Usuario = u.NombreUsuario,
                    NombreRol = u.Rol.Nombre
                })
                .ToListAsync();
        }

        public async Task<UsuarioReadDTO> GetUsuarioByIdAsync(int id)
        {
            var usuario = await _context.Usuarios
                .Include(u => u.Rol)
                .FirstOrDefaultAsync(u => u.IdUsuario == id);

            if (usuario == null) return null;

            return new UsuarioReadDTO
            {
                Id = usuario.IdUsuario,
                Nombre = usuario.Nombre,
                Usuario = usuario.NombreUsuario,
                NombreRol = usuario.Rol.Nombre
            };
        }

        public async Task<UsuarioReadDTO> CreateUsuarioAsync(UsuarioCreateDTO usuarioDto)
        {
            var nuevoUsuario = new Usuario
            {
                Nombre = usuarioDto.Nombre,
                NombreUsuario = usuarioDto.Usuario,
                Password = usuarioDto.Contraseña,
                IdRol = usuarioDto.IdRol
            };

            _context.Usuarios.Add(nuevoUsuario);
            await _context.SaveChangesAsync();

            await _context.Entry(nuevoUsuario).Reference(u => u.Rol).LoadAsync();

            return new UsuarioReadDTO
            {
                Id = nuevoUsuario.IdUsuario,
                Nombre = nuevoUsuario.Nombre,
                Usuario = nuevoUsuario.NombreUsuario,
                NombreRol = nuevoUsuario.Rol.Nombre
            };
        }
        public async Task<bool> UpdateUsuarioAsync(int id, UsuarioCreateDTO dto)
        {
            var usuario = await _context.Usuarios.FindAsync(id);
            if (usuario == null) return false;

            usuario.Nombre = dto.Nombre;
            usuario.NombreUsuario = dto.Usuario;
            usuario.IdRol = dto.IdRol;

            if (!string.IsNullOrWhiteSpace(dto.Contraseña))
            {
                usuario.Password = dto.Contraseña;
            }

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteUsuarioAsync(int id)
        {
            var usuario = await _context.Usuarios.FindAsync(id);
            if (usuario == null) return false;

            _context.Usuarios.Remove(usuario);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}