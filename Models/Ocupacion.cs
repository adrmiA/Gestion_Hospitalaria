using GestionHospitalaria.Models;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

[Table("ocupacion")]
public class Ocupacion
{
    [Key]
    [Column("id_ocupacion")] 
    public int Id { get; set; }

    [Column("fecha_ingreso")]
    public DateTime Fecha_ingreso { get; set; }

    [Column("fecha_salida")]
    public DateTime? Fecha_salida { get; set; }

    [Column("id_paciente")]
    public int IdPaciente { get; set; }
    [ForeignKey("IdPaciente")]
    public Paciente Paciente { get; set; }

    [Column("id_cama")]
    public int IdCama { get; set; }
    [ForeignKey("IdCama")]
    public Cama Cama { get; set; }
}