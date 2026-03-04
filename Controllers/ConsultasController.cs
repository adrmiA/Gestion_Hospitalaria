using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ConsultasController : ControllerBase
    {
        private readonly ConsultasService _consultaService;

        public ConsultasController(ConsultasService consultaService)
        {
            _consultaService = consultaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ConsultaReadDTO>>> GetAll()
        {
            var consultas = await _consultaService.GetAllConsultasReporteAsync();
            return Ok(consultas);
        }

        [HttpPost]
        public async Task<ActionResult<ConsultaReadDTO>> Create(ConsultaCreateDTO consultaDto)
        {
            try
            {
                var nuevaConsulta = await _consultaService.CreateConsultaAsync(consultaDto);
                return CreatedAtAction(nameof(GetById), new { id = nuevaConsulta.Id }, nuevaConsulta);
            }
            catch (Exception ex)
            {
                return BadRequest($"Error al crear la consulta: {ex.Message}");
            }
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ConsultaReadDTO>> GetById(int id)
        {
            var consulta = await _consultaService.GetConsultaByIdAsync(id);
            if (consulta == null) return NotFound();
            return Ok(consulta);
        }

        [HttpGet("expediente/{idExpediente}")]
        public async Task<ActionResult<IEnumerable<ConsultaReadDTO>>> GetByExpediente(int idExpediente)
        {
            var historial = await _consultaService.GetHistorialByExpedienteAsync(idExpediente);
            return Ok(historial);
        }

    }
}