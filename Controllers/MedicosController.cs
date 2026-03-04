using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class MedicosController : ControllerBase
    {
        private readonly MedicosService _medicoService;

        public MedicosController(MedicosService medicoService)
        {
            _medicoService = medicoService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<MedicoReadDTO>>> GetAll()
        {
            return Ok(await _medicoService.GetAllMedicosAsync());
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<MedicoReadDTO>> GetById(int id)
        {
            var medico = await _medicoService.GetMedicoByIdAsync(id);
            if (medico == null) return NotFound();
            return Ok(medico);
        }

        [HttpPost]
        public async Task<ActionResult<MedicoReadDTO>> Create(MedicoCreateDTO medicoDto)
        {
            var medico = await _medicoService.CreateMedicoAsync(medicoDto);
            return CreatedAtAction(nameof(GetById), new { id = medico.Id }, medico);
        }

        [HttpGet("especialidad/{especialidad}")]
        public async Task<ActionResult<IEnumerable<MedicoReadDTO>>> GetByEspecialidad(string especialidad)
        {
            return Ok(await _medicoService.GetMedicosByEspecialidadAsync(especialidad));
        }
    }
}