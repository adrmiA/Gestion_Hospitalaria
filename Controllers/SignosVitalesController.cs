using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SignosVitalesController : ControllerBase
    {
        private readonly SignosVitalesService _service;

        public SignosVitalesController(SignosVitalesService service)
        {
            _service = service;
        }

        [HttpGet("expediente/{idExpediente}")]
        public async Task<ActionResult<IEnumerable<SignosVitalesDTO>>> GetByExpediente(int idExpediente)
        {
            var lista = await _service.GetByExpedienteAsync(idExpediente);
            return Ok(lista);
        }

        [HttpPost]
        public async Task<IActionResult> Create(SignosVitalesDTO dto)
        {
            var resultado = await _service.CreateAsync(dto);
            if (!resultado) return BadRequest("Error al registrar signos vitales.");
            return Ok(new { mensaje = "Signos vitales registrados correctamente." });
        }
    }
}