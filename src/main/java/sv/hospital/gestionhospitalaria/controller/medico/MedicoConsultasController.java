package sv.hospital.gestionhospitalaria.controller.medico;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.service.ApiService;
import sv.hospital.gestionhospitalaria.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class MedicoConsultasController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private Label lblPaciente, lblConsStatus;
    @FXML private TableView<ConsRow> tableConsultas;
    @FXML private TableColumn<ConsRow,Integer> colConsId;
    @FXML private TableColumn<ConsRow,String>  colConsDate, colConsDiag;
    @FXML private TextArea txtDiagnostico, txtNotas;
    @FXML private ComboBox<AreaItem> cmbArea;
    @FXML private TextField txtTipoExamen;

    private int currentPacienteId = -1;
    private int currentExpedienteId = -1;
    private int lastConsultaId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colConsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colConsDate.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colConsDiag.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        loadAreas();
    }

    private void loadAreas() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Laboratorio/areas");
                List<AreaItem> items = new ArrayList<>();
                for (var e : arr) {
                    JsonObject a = e.getAsJsonObject();
                    items.add(new AreaItem(a.get("idArea").getAsInt(), a.get("nombreArea").getAsString()));
                }
                Platform.runLater(() -> cmbArea.setItems(FXCollections.observableArrayList(items)));
            } catch (Exception ignored) {}
        }).start();
    }

    @FXML public void buscarPaciente() {
        String q = txtBuscar.getText().trim();
        if (q.isEmpty()) return;
        new Thread(() -> {
            try {
                // Try by DUI
                JsonArray pacientes = ApiService.getArray("/Pacientes");
                JsonObject found = null;
                for (var el : pacientes) {
                    JsonObject p = el.getAsJsonObject();
                    String dui = p.has("dui") && !p.get("dui").isJsonNull() ? p.get("dui").getAsString() : "";
                    String id  = String.valueOf(p.get("idPaciente").getAsInt());
                    if (dui.equalsIgnoreCase(q) || id.equals(q)) { found = p; break; }
                }
                if (found == null) { Platform.runLater(() -> lblPaciente.setText("Paciente no encontrado.")); return; }
                final JsonObject pat = found;
                currentPacienteId = pat.get("idPaciente").getAsInt();
                // Expediente
                JsonObject exp = ApiService.getObject("/Expedientes/paciente/" + currentPacienteId);
                currentExpedienteId = exp.get("idExpediente").getAsInt();
                // Consultas
                JsonArray cons = ApiService.getArray("/Consultas/expediente/" + currentExpedienteId);
                List<ConsRow> rows = new ArrayList<>();
                for (var ce : cons) {
                    JsonObject c = ce.getAsJsonObject();
                    rows.add(new ConsRow(c.get("idConsulta").getAsInt(),
                            c.get("fecha").getAsString(),
                            c.get("diagnostico").getAsString()));
                }
                String nombre = pat.get("nombre").getAsString() + " " + pat.get("apellido").getAsString();
                Platform.runLater(() -> {
                    lblPaciente.setText("Paciente: " + nombre + " | Exp: " + currentExpedienteId);
                    tableConsultas.setItems(FXCollections.observableArrayList(rows));
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblPaciente.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void registrarConsulta() {
        if (currentExpedienteId < 0) { lblConsStatus.setText("Busca un paciente primero."); return; }
        String diag = txtDiagnostico.getText().trim();
        if (diag.isEmpty()) { lblConsStatus.setText("Ingresa un diagnóstico."); return; }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("diagnostico", diag);
                body.addProperty("idExpediente", currentExpedienteId);
                body.addProperty("idMedico", SessionManager.getInstance().getMedicoId());
                JsonElement resp = ApiService.post("/Consultas", body);
                if (resp.isJsonObject()) lastConsultaId = resp.getAsJsonObject().get("idConsulta").getAsInt();
                Platform.runLater(() -> {
                    lblConsStatus.setText("✅ Consulta registrada. ID: " + lastConsultaId);
                    txtDiagnostico.clear(); txtNotas.clear();
                    buscarPaciente();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblConsStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void crearOrdenLab() {
        AreaItem area = cmbArea.getValue();
        String tipo = txtTipoExamen.getText().trim();
        if (area == null || tipo.isEmpty()) { lblConsStatus.setText("Selecciona área y tipo de examen."); return; }
        if (lastConsultaId < 0) { lblConsStatus.setText("Registra una consulta primero."); return; }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("idConsulta", lastConsultaId);
                body.addProperty("idArea", area.id());
                body.addProperty("tipoExamen", tipo);
                ApiService.post("/Laboratorio", body);
                Platform.runLater(() -> { lblConsStatus.setText("✅ Orden de lab. creada."); txtTipoExamen.clear(); });
            } catch (Exception e) {
                Platform.runLater(() -> lblConsStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    // Inner models
    public record AreaItem(int id, String nombre) {
        @Override public String toString() { return nombre; }
    }
    public static class ConsRow {
        private final int id; private final String fecha, diagnostico;
        public ConsRow(int id, String f, String d) { this.id=id; fecha=f; diagnostico=d; }
        public int getId() { return id; }
        public String getFecha() { return fecha; }
        public String getDiagnostico() { return diagnostico; }
    }
}