using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class PacientesController : ControllerBase
    {
        private readonly PacientesService _pacienteService;

        public PacientesController(PacientesService pacienteService)
        {
            _pacienteService = pacienteService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<PacienteReadDTO>>> GetAll()
        {
            return Ok(await _pacienteService.GetAllPacientesAsync());
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<PacienteReadDTO>> GetById(int id)
        {
            var paciente = await _pacienteService.GetPacienteByIdAsync(id);
            if (paciente == null) return NotFound();
            return Ok(paciente);
        }

        [HttpGet("dui/{dui}")]
        public async Task<ActionResult<PacienteReadDTO>> GetByDui(string dui)
        {
            var paciente = await _pacienteService.GetPacienteByDuiAsync(dui);
            if (paciente == null) return NotFound();
            return Ok(paciente);
        }

        [HttpPost]
        public async Task<ActionResult<PacienteReadDTO>> Create(PacienteCreateDTO pacienteDto)
        {
            var nuevoPaciente = await _pacienteService.CreatePacienteAsync(pacienteDto);
            return CreatedAtAction(nameof(GetById), new { id = nuevoPaciente.Id }, nuevoPaciente);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, PacienteCreateDTO pacienteDto)
        {
            var actualizado = await _pacienteService.UpdatePacienteAsync(id, pacienteDto);
            if (!actualizado) return NotFound();
            return NoContent();
        }
    }
}