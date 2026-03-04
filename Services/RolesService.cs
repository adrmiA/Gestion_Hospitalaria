using GestionHospitalaria.Data;
using GestionHospitalaria.Models;
using Microsoft.EntityFrameworkCore;
using GestionHospitalaria.DTOs;

namespace GestionHospitalaria.Services
{
    public class RolesService
    {
        private readonly HospitalDbContext _context;

        public RolesService(HospitalDbContext context)
        {
            _context = context;
        }

        public async Task<List<RolDTO>> GetAllRolesAsync()
        {
            return await _context.Roles
                .Select(r => new RolDTO
                {
                    Id = r.Id,
                    Nombre = r.Nombre
                })
                .ToListAsync();
        }
    }
}