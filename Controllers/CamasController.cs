using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CamasController : ControllerBase
    {
        private readonly CamasService _camaService;

        public CamasController(CamasService camaService)
        {
            _camaService = camaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CamaDTO>>> GetAll()
        {
            return Ok(await _camaService.GetAllCamasAsync());
        }

        [HttpGet("disponibles")]
        public async Task<ActionResult<IEnumerable<CamaDTO>>> GetDisponibles()
        {
            return Ok(await _camaService.GetCamasDisponiblesAsync());
        }

        [HttpPost("ocupar")]
        public async Task<ActionResult<OcupacionReadDTO>> OcuparCama(OcupacionCreateDTO ocupacionDto)
        {
            var resultado = await _camaService.OcuparCamaAsync(ocupacionDto);
            if (resultado == null)
                return BadRequest("No se pudo ocupar la cama. Verifique si existe y está disponible.");

            return Ok(resultado);
        }

        [HttpPost("liberar/{idCama}")]
        public async Task<IActionResult> LiberarCama(int idCama)
        {
            var liberada = await _camaService.LiberarCamaAsync(idCama);
            if (!liberada)
                return NotFound("No se encontró la cama o ya se encuentra libre.");

            return NoContent();
        }
    }
}