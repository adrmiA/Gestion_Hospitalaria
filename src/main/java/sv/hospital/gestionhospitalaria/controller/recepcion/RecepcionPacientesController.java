package sv.hospital.gestionhospitalaria.controller.recepcion;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.service.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class RecepcionPacientesController implements Initializable {

    @FXML private TableView<PacRow> tablePacientes;
    @FXML private TableColumn<PacRow,Integer> colPacId;
    @FXML private TableColumn<PacRow,String> colPacNom, colPacApe, colPacDui, colPacTel, colPacEst;
    @FXML private VBox formNuevo;
    @FXML private TextField txtNombre, txtApellido, txtDui, txtTelefono, txtFechaNac;
    @FXML private Label lblPacStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPacId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPacNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPacApe.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colPacDui.setCellValueFactory(new PropertyValueFactory<>("dui"));
        colPacTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colPacEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
        loadPacientes();
    }

    @FXML public void loadPacientes() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Pacientes");
                List<PacRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject p = el.getAsJsonObject();
                    rows.add(new PacRow(
                            p.get("idPaciente").getAsInt(),
                            p.get("nombre").getAsString(),
                            p.get("apellido").getAsString(),
                            p.has("dui") && !p.get("dui").isJsonNull() ? p.get("dui").getAsString() : "",
                            p.has("telefono") && !p.get("telefono").isJsonNull() ? p.get("telefono").getAsString() : "",
                            p.has("estado") && !p.get("estado").isJsonNull() ? p.get("estado").getAsString() : "Activo"
                    ));
                }
                Platform.runLater(() -> tablePacientes.setItems(FXCollections.observableArrayList(rows)));
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    @FXML public void showFormNuevo() { formNuevo.setVisible(true); formNuevo.setManaged(true); }
    @FXML public void cancelarForm()  { formNuevo.setVisible(false); formNuevo.setManaged(false); }

    @FXML public void guardarPaciente() {
        if (txtNombre.getText().trim().isEmpty()) { lblPacStatus.setText("El nombre es requerido."); return; }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("nombre", txtNombre.getText().trim());
                body.addProperty("apellido", txtApellido.getText().trim());
                body.addProperty("dui", txtDui.getText().trim());
                body.addProperty("telefono", txtTelefono.getText().trim());
                body.addProperty("fechaNacimiento", txtFechaNac.getText().trim());
                body.addProperty("estado", "Activo");
                ApiService.post("/Pacientes", body);
                Platform.runLater(() -> {
                    lblPacStatus.setText("✅ Paciente registrado.");
                    cancelarForm(); loadPacientes();
                    txtNombre.clear(); txtApellido.clear(); txtDui.clear();
                    txtTelefono.clear(); txtFechaNac.clear();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblPacStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    public static class PacRow {
        private final int id;
        private final String nombre, apellido, dui, telefono, estado;
        public PacRow(int id, String n, String a, String d, String t, String e) {
            this.id=id; nombre=n; apellido=a; dui=d; telefono=t; estado=e; }
        public int getId()        { return id; }
        public String getNombre()   { return nombre; }
        public String getApellido() { return apellido; }
        public String getDui()      { return dui; }
        public String getTelefono() { return telefono; }
        public String getEstado()   { return estado; }
    }
}