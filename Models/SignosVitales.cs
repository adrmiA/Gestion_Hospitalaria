using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GestionHospitalaria.Models
{
    [Table("signos_vitales")]
    public class SignosVitales
    {
        [Key]
        [Column("id_signos")] 
        public int Id { get; set; }

        [Column("id_expediente")]
        public int IdExpediente { get; set; }

        [ForeignKey("IdExpediente")]
        public Expediente Expediente { get; set; }

        [Column("frecuencia_cardiaca")]
        public string FrecuenciaCardiaca { get; set; }

        [Column("frecuencia_respiratoria")]
        public string FrecuenciaRespiratoria { get; set; }

        [Column("temperatura")]
        public string Temperatura { get; set; }

        [Column("presion_arterial")]
        public string PresionArterial { get; set; }

        [Column("saturacion_oxigeno")]
        public string SaturacionOxigeno { get; set; }

        [Column("peso")]
        public string Peso { get; set; }

        [Column("talla")]
        public string Talla { get; set; }

        [Column("fecha_registro")]
        public DateTime FechaRegistro { get; set; }
    }
}