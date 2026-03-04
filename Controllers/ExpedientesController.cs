using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ExpedientesController : ControllerBase
    {
        private readonly ExpedientesService _expedienteService;

        public ExpedientesController(ExpedientesService expedienteService)
        {
            _expedienteService = expedienteService;
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ExpedienteReadDTO>> GetById(int id)
        {
            var expediente = await _expedienteService.GetExpedienteFullByIdAsync(id);
            if (expediente == null) return NotFound("Expediente no encontrado.");
            return Ok(expediente);
        }

        [HttpGet("paciente/{idPaciente}")]
        public async Task<ActionResult<ExpedienteReadDTO>> GetByPaciente(int idPaciente)
        {
            var expediente = await _expedienteService.GetExpedienteByPacienteIdAsync(idPaciente);
            if (expediente == null) return NotFound("El paciente no cuenta con un expediente activo.");
            return Ok(expediente);
        }

        [HttpGet("buscar-dui/{dui}")]
        public async Task<ActionResult<ExpedienteReadDTO>> GetByDui(string dui)
        {
            var expediente = await _expedienteService.GetExpedienteByDuiAsync(dui);
            if (expediente == null) return NotFound("No se encontró un expediente con ese DUI.");
            return Ok(expediente);
        }
    }
}