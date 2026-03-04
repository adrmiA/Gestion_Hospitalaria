using Microsoft.AspNetCore.Mvc;
using GestionHospitalaria.DTOs;
using GestionHospitalaria.Services;

namespace GestionHospitalaria.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class InventarioController : ControllerBase
    {
        private readonly InventarioService _inventarioService;

        public InventarioController(InventarioService inventarioService)
        {
            _inventarioService = inventarioService;
        }

        [HttpGet("productos")]
        public async Task<ActionResult<IEnumerable<ProductoDTO>>> GetProductos()
        {
            return Ok(await _inventarioService.GetAllProductosAsync());
        }

        [HttpGet("status")]
        public async Task<ActionResult<IEnumerable<InventarioStatusDTO>>> GetStockStatus()
        {
            return Ok(await _inventarioService.GetInventarioStatusAsync());
        }

        [HttpPost("movimiento")]
        public async Task<IActionResult> RegistrarMovimiento(MovimientoInventarioDTO movimientoDto)
        {
            var resultado = await _inventarioService.RegisterMovimientoAsync(movimientoDto);
            if (!resultado) 
                return BadRequest("Error al registrar movimiento. Verifique el stock disponible o los datos.");
            
            return Ok(new { mensaje = "Movimiento registrado correctamente" });
        }

        [HttpGet("bajo-stock")]
        public async Task<ActionResult<IEnumerable<InventarioStatusDTO>>> GetBajoStock()
        {
            return Ok(await _inventarioService.GetProductosBajoStockAsync());
        }
    }
}