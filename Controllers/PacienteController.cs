using GestionHospitalariaWEB.Models;
using Microsoft.AspNetCore.Mvc;
using System.Text;
using System.Text.Json;
using System.Diagnostics;
using System.Net.Http;

namespace GestionHospitalariaWEB.Controllers
{
    public class PacienteController : Controller
    {
        private readonly HttpClient _httpClient;
        private readonly string _apiUrl = "http://localhost:5221/api";

        private readonly JsonSerializerOptions _jsonOptions = new JsonSerializerOptions
        {
            PropertyNameCaseInsensitive = true
        };

        public PacienteController()
        {
            _httpClient = new HttpClient();
        }

        public IActionResult Index() => View();

        public async Task<IActionResult> SolicitarCita()
        {
            var model = new SolicitarCitaVM();
            model.Medicos = await ObtenerMedicosAPI();
            return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> SolicitarCita(SolicitarCitaVM model)
        {
            try
            {
                var resPac = await _httpClient.GetAsync($"{_apiUrl}/pacientes/dui/{model.Dui}");
                if (!resPac.IsSuccessStatusCode)
                {
                    model.MensajeError = "DUI no encontrado. Debe registrarse en el hospital primero.";
                    model.Medicos = await ObtenerMedicosAPI();
                    return View(model);
                }

                var pacJson = await resPac.Content.ReadAsStringAsync();

                using var doc = JsonDocument.Parse(pacJson);
                int idPacienteReal = doc.RootElement.GetProperty("id").GetInt32();

                var citaObj = new
                {
                    idPaciente = idPacienteReal,
                    idMedico = model.IdMedico,
                    fecha = model.Fecha,
                    hora = model.Hora + ":00", 
                    estado = "Pendiente"
                };

                var jsonBody = JsonSerializer.Serialize(citaObj);
                var content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

                var resCita = await _httpClient.PostAsync($"{_apiUrl}/citas", content);

                if (resCita.IsSuccessStatusCode)
                {
                    model.MensajeExito = "Solicitud enviada correctamente. Recepción la procesará pronto.";
                }
                else
                {
                    model.MensajeError = "El servidor rechazó la solicitud. Verifique los datos.";
                }
            }
            catch (Exception ex)
            {
                model.MensajeError = "Error de conexión: " + ex.Message;
            }

            model.Medicos = await ObtenerMedicosAPI();
            return View(model);
        }

        public IActionResult InformeMedico() => View(new InformeMedicoVM());

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> InformeMedico(InformeMedicoVM model)
        {
            if (string.IsNullOrEmpty(model.Dui)) return View(model);

            try
            {
                var response = await _httpClient.GetAsync($"{_apiUrl}/expedientes/buscar-dui/{model.Dui}");
                if (response.IsSuccessStatusCode)
                {
                    var json = await response.Content.ReadAsStringAsync();
                    model.Expediente = JsonSerializer.Deserialize<ExpedienteDTO>(json, _jsonOptions);
                }
                else
                {
                    model.MensajeError = "No se encontró un historial clínico para el DUI ingresado.";
                }
            }
            catch (Exception ex)
            {
                model.MensajeError = "Error al conectar con el servidor: " + ex.Message;
            }

            return View(model);
        }

        private async Task<List<MedicoDTO>> ObtenerMedicosAPI()
        {
            try
            {
                var res = await _httpClient.GetAsync($"{_apiUrl}/medicos");
                if (res.IsSuccessStatusCode)
                {
                    var json = await res.Content.ReadAsStringAsync();
                    return JsonSerializer.Deserialize<List<MedicoDTO>>(json, _jsonOptions) ?? new List<MedicoDTO>();
                }
                else
                {
                    Debug.WriteLine($"[API Error]: El servidor respondió con status {res.StatusCode}");
                }
            }
            catch (HttpRequestException ex)
            {
                Debug.WriteLine($"[Network Error]: No se pudo conectar a la API. ¿Está encendida? Detalle: {ex.Message}");
            }
            catch (JsonException ex)
            {
                Debug.WriteLine($"[JSON Error]: Error al procesar los datos de los médicos: {ex.Message}");
            }
            catch (Exception ex)
            {
                Debug.WriteLine($"[Unknown Error]: {ex.Message}");
            }
            return new List<MedicoDTO>();
        }
    }
}