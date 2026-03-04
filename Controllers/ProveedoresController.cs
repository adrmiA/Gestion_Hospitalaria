using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ProveedoresController : ControllerBase
    {
        private readonly ProveedoresService _proveedorService;

        public ProveedoresController(ProveedoresService proveedorService)
        {
            _proveedorService = proveedorService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ProveedorDTO>>> GetAll()
        {
            return Ok(await _proveedorService.GetAllAsync());
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ProveedorDTO>> GetById(int id)
        {
            var proveedor = await _proveedorService.GetByIdAsync(id);
            if (proveedor == null) return NotFound();
            return Ok(proveedor);
        }

        [HttpPost]
        public async Task<ActionResult<ProveedorDTO>> Create(ProveedorDTO proveedorDto)
        {
            var nuevo = await _proveedorService.CreateAsync(proveedorDto);
            return CreatedAtAction(nameof(GetById), new { id = nuevo.Id }, nuevo);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, ProveedorDTO proveedorDto)
        {
            var actualizado = await _proveedorService.UpdateAsync(id, proveedorDto);
            if (!actualizado) return NotFound();
            return NoContent();
        }
    }
}