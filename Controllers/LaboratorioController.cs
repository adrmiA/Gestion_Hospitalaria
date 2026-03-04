using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class LaboratorioController : ControllerBase
    {
        private readonly LaboratorioService _labService;

        public LaboratorioController(LaboratorioService labService)
        {
            _labService = labService;
        }

        [HttpGet("areas")]
        public async Task<ActionResult<IEnumerable<AreaLaboratorioDTO>>> GetAreas()
        {
            return Ok(await _labService.GetAreasAsync());
        }

        [HttpPost("orden")]
        public async Task<ActionResult<OrdenLaboratorioReadDTO>> CreateOrden(OrdenLaboratorioCreateDTO ordenDto)
        {
            var nuevaOrden = await _labService.CreateOrdenAsync(ordenDto);
            return Ok(nuevaOrden);
        }

        [HttpGet("pendientes")]
        public async Task<ActionResult<IEnumerable<OrdenLaboratorioReadDTO>>> GetPendientes()
        {
            return Ok(await _labService.GetOrdenesPendientesAsync());
        }

        [HttpPatch("resultado")]
        public async Task<IActionResult> UpdateResultado(OrdenLaboratorioResultDTO resultDto)
        {
            var actualizado = await _labService.UpdateResultadoAsync(resultDto);
            if (!actualizado) return NotFound("Orden no encontrada");
            return NoContent();
        }
    }
}