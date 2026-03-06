package sv.hospital.gestionhospitalaria.controller.recepcion;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.service.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class RecepcionCitasController implements Initializable {

    @FXML private TableView<CitaRow> tableCitas;
    @FXML private TableColumn<CitaRow,Integer> colCitId;
    @FXML private TableColumn<CitaRow,String> colCitPac, colCitMed, colCitFec, colCitHor, colCitEst;
    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private VBox panelAsignar;
    @FXML private ComboBox<MedicoItem> cmbMedicos;
    @FXML private TextField txtFecha, txtHora;
    @FXML private Label lblCitStatus;

    private final ObservableList<CitaRow> allData = FXCollections.observableArrayList();
    private CitaRow selectedCita;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCitId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCitPac.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colCitMed.setCellValueFactory(new PropertyValueFactory<>("medico"));
        colCitFec.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCitHor.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colCitEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cmbFiltroEstado.setItems(FXCollections.observableArrayList("Todos","Pendiente","Confirmada","Cancelada"));
        cmbFiltroEstado.setValue("Todos");
        loadCitas();
        loadMedicos();
    }

    @FXML public void loadCitas() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Citas");
                List<CitaRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject c = el.getAsJsonObject();
                    String pac = c.has("paciente") && !c.get("paciente").isJsonNull()
                            ? c.getAsJsonObject("paciente").get("nombre").getAsString() + " "
                            + c.getAsJsonObject("paciente").get("apellido").getAsString() : "N/A";
                    String med = c.has("medico") && !c.get("medico").isJsonNull()
                            ? c.getAsJsonObject("medico").getAsJsonObject("usuario").get("nombre").getAsString() : "Sin asignar";
                    rows.add(new CitaRow(c.get("idCita").getAsInt(), pac, med,
                            c.get("fecha").getAsString(), c.get("hora").getAsString(),
                            c.get("estado").getAsString(),
                            c.get("idPaciente").getAsInt(),
                            c.has("idMedico") && !c.get("idMedico").isJsonNull() ? c.get("idMedico").getAsInt() : 0));
                }
                Platform.runLater(() -> { allData.setAll(rows); tableCitas.setItems(allData); });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void loadMedicos() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Medicos");
                List<MedicoItem> items = new ArrayList<>();
                for (var el : arr) {
                    JsonObject m = el.getAsJsonObject();
                    String nombre = m.getAsJsonObject("usuario").get("nombre").getAsString();
                    String esp = m.has("especialidad") && !m.get("especialidad").isJsonNull()
                            ? m.get("especialidad").getAsString() : "";
                    items.add(new MedicoItem(m.get("idMedico").getAsInt(), nombre + " (" + esp + ")"));
                }
                Platform.runLater(() -> cmbMedicos.setItems(FXCollections.observableArrayList(items)));
            } catch (Exception ignored) {}
        }).start();
    }

    @FXML public void filterCitas() {
        String f = cmbFiltroEstado.getValue();
        if ("Todos".equals(f)) { tableCitas.setItems(allData); return; }
        tableCitas.setItems(allData.filtered(r -> r.getEstado().equalsIgnoreCase(f)));
    }

    @FXML public void aprobarCita() {
        selectedCita = tableCitas.getSelectionModel().getSelectedItem();
        if (selectedCita == null) { alert("Seleccione una cita."); return; }
        panelAsignar.setVisible(true); panelAsignar.setManaged(true);
    }

    @FXML public void cancelarCita() {
        CitaRow sel = tableCitas.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Seleccione una cita."); return; }
        cambiarEstado(sel.getId(), "Cancelada");
    }

    @FXML public void confirmarAsignacion() {
        MedicoItem med = cmbMedicos.getValue();
        if (med == null || txtFecha.getText().isBlank() || txtHora.getText().isBlank()) {
            lblCitStatus.setText("Complete todos los campos."); return;
        }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("idMedico", med.id());
                body.addProperty("fecha", txtFecha.getText().trim());
                body.addProperty("hora", txtHora.getText().trim());
                body.addProperty("estado", "Confirmada");
                body.addProperty("idPaciente", selectedCita.getIdPaciente());
                ApiService.put("/Citas/" + selectedCita.getId(), body);
                Platform.runLater(() -> {
                    lblCitStatus.setText("✅ Cita confirmada y asignada a " + med.nombre());
                    cancelarAsignacion(); loadCitas();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblCitStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void cancelarAsignacion() { panelAsignar.setVisible(false); panelAsignar.setManaged(false); }

    private void cambiarEstado(int id, String estado) {
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("estado", estado);
                ApiService.put("/Citas/" + id + "/estado", body);
                Platform.runLater(this::loadCitas);
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void alert(String msg) { new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait(); }

    public record MedicoItem(int id, String nombre) { @Override public String toString() { return nombre; } }

    public static class CitaRow {
        private final int id, idPaciente, idMedico;
        private final String paciente, medico, fecha, hora;
        private String estado;
        public CitaRow(int id, String paciente, String medico, String fecha, String hora, String estado, int idPaciente, int idMedico) {
            this.id=id; this.paciente=paciente; this.medico=medico; this.fecha=fecha;
            this.hora=hora; this.estado=estado; this.idPaciente=idPaciente; this.idMedico=idMedico;
        }
        public int getId()          { return id; }
        public int getIdPaciente()  { return idPaciente; }
        public int getIdMedico()    { return idMedico; }
        public String getPaciente() { return paciente; }
        public String getMedico()   { return medico; }
        public String getFecha()    { return fecha; }
        public String getHora()     { return hora; }
        public String getEstado()   { return estado; }
        public void setEstado(String e) { this.estado = e; }
    }
}