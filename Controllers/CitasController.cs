using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;
using Microsoft.AspNetCore.Mvc;
using static Microsoft.EntityFrameworkCore.DbLoggerCategory;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CitasController : ControllerBase
    {
        private readonly CitasService _citaService;

        public CitasController(CitasService citaService)
        {
            _citaService = citaService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CitaReadDTO>>> GetAll()
        {
            return Ok(await _citaService.GetAllCitasAsync());
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<CitaReadDTO>> GetById(int id)
        {
            var cita = await _citaService.GetCitaByIdAsync(id);
            if (cita == null) return NotFound();
            return Ok(cita);
        }

        [HttpGet("hoy")]
        public async Task<ActionResult<IEnumerable<CitaReadDTO>>> GetCitasHoy()
        {
            var fechaHoy = DateOnly.FromDateTime(DateTime.Now);
            return Ok(await _citaService.GetCitasByFechaAsync(fechaHoy));
        }

        [HttpPost]
        public async Task<ActionResult<CitaReadDTO>> Create(CitaCreateDTO citaDto)
        {
            var nuevaCita = await _citaService.CreateCitaAsync(citaDto);
            return CreatedAtAction(nameof(GetById), new { id = nuevaCita.Id }, nuevaCita);
        }

        [HttpPatch("{id}/estado")]
        public async Task<IActionResult> ChangeStatus(int id, [FromBody] string nuevoEstado)
        {
            var actualizado = await _citaService.UpdateCitaEstadoAsync(id, nuevoEstado);

            if (!actualizado) return NotFound();

            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> CancelarCita(int id)
        {
            var cancelada = await _citaService.UpdateCitaEstadoAsync(id, "Cancelada");
            if (!cancelada) return NotFound();
            return NoContent();
        }
    }
}