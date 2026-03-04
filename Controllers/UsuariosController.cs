using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.Models;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.EntityFrameworkCore; 

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UsuariosController : ControllerBase
    {
        private readonly UsuariosService _usuarioService;

        public UsuariosController(UsuariosService usuarioService)
        {
            _usuarioService = usuarioService;
        }

        [HttpPost("login")]
        public async Task<ActionResult> Login(UsuarioLoginDTO loginDto)
        {
            var token = await _usuarioService.Authenticate(loginDto);

            if (token == null)
                return Unauthorized(new { mensaje = "Usuario o contraseña incorrectos." });

            var usuarios = await _usuarioService.GetUsuariosAsync();
            var usuarioInfo = usuarios.FirstOrDefault(u => u.Usuario.Equals(loginDto.Usuario, StringComparison.OrdinalIgnoreCase));

            if (usuarioInfo == null)
                return Unauthorized(new { mensaje = "Error al recuperar datos del usuario." });

            int? medicoId = null;

            return Ok(new
            {
                token = token,
                usuario = usuarioInfo, 
                medicoId = medicoId    
            });
        }

        [Authorize]
        [HttpGet]
        public async Task<ActionResult<IEnumerable<UsuarioReadDTO>>> GetAll()
        {
            return Ok(await _usuarioService.GetUsuariosAsync());
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<UsuarioReadDTO>> GetById(int id)
        {
            var usuario = await _usuarioService.GetUsuarioByIdAsync(id);
            if (usuario == null) return NotFound();
            return Ok(usuario);
        }

        [AllowAnonymous]
        [HttpPost("register")]
        public async Task<ActionResult<UsuarioReadDTO>> Register(UsuarioCreateDTO usuarioDto)
        {
            var nuevoUsuario = await _usuarioService.CreateUsuarioAsync(usuarioDto);
            return CreatedAtAction(nameof(GetById), new { id = nuevoUsuario.Id }, nuevoUsuario);
        }

        [Authorize]
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, UsuarioCreateDTO usuarioDto)
        {
            var actualizado = await _usuarioService.UpdateUsuarioAsync(id, usuarioDto);

            if (!actualizado)
                return NotFound();

            return NoContent();
        }

        [Authorize]
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var eliminado = await _usuarioService.DeleteUsuarioAsync(id);

            if (!eliminado)
                return NotFound();

            return NoContent();
        }
    }
}