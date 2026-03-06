package sv.hospital.gestionhospitalaria.controller.recepcion;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.service.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class RecepcionHospitalizacionController implements Initializable {

    @FXML private TableView<OcRow> tableOcupaciones;
    @FXML private TableColumn<OcRow,Integer> colOcId;
    @FXML private TableColumn<OcRow,String> colOcPac, colOcCama, colOcSala, colOcIng;
    @FXML private TextField txtPacId, txtCamaId, txtFechaIng;
    @FXML private Label lblHospStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colOcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOcPac.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colOcCama.setCellValueFactory(new PropertyValueFactory<>("cama"));
        colOcSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        colOcIng.setCellValueFactory(new PropertyValueFactory<>("ingreso"));
        loadOcupaciones();
    }

    @FXML public void loadOcupaciones() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Camas/ocupaciones");
                List<OcRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    if (!o.get("fechaSalida").isJsonNull()) continue; // solo activos
                    String pac = o.has("paciente") && !o.get("paciente").isJsonNull()
                            ? o.getAsJsonObject("paciente").get("nombre").getAsString() + " "
                            + o.getAsJsonObject("paciente").get("apellido").getAsString() : "N/A";
                    String cama = o.has("cama") && !o.get("cama").isJsonNull()
                            ? o.getAsJsonObject("cama").get("numero").getAsString() : "?";
                    String sala = o.has("cama") && !o.get("cama").isJsonNull()
                            ? o.getAsJsonObject("cama").get("sala").getAsString() : "?";
                    rows.add(new OcRow(o.get("idOcupacion").getAsInt(), pac, cama, sala,
                            o.get("fechaIngreso").getAsString(),
                            o.get("idOcupacion").getAsInt()));
                }
                Platform.runLater(() -> tableOcupaciones.setItems(FXCollections.observableArrayList(rows)));
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    @FXML public void registrarIngreso() {
        String pacId = txtPacId.getText().trim();
        String camaId = txtCamaId.getText().trim();
        String fecha = txtFechaIng.getText().trim();
        if (pacId.isEmpty() || camaId.isEmpty() || fecha.isEmpty()) {
            lblHospStatus.setText("Complete todos los campos."); return;
        }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("idPaciente", Integer.parseInt(pacId));
                body.addProperty("idCama", Integer.parseInt(camaId));
                body.addProperty("fechaIngreso", fecha);
                ApiService.post("/Camas/ocupar", body);
                Platform.runLater(() -> { lblHospStatus.setText("✅ Ingreso registrado.");
                    txtPacId.clear(); txtCamaId.clear(); txtFechaIng.clear(); loadOcupaciones(); });
            } catch (Exception e) {
                Platform.runLater(() -> lblHospStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void darDeAlta() {
        OcRow sel = tableOcupaciones.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Seleccione un registro."); return; }
        new Thread(() -> {
            try {
                ApiService.put("/Camas/alta/" + sel.getRawId(), new JsonObject());
                Platform.runLater(() -> { lblHospStatus.setText("✅ Paciente dado de alta. Cama liberada."); loadOcupaciones(); });
            } catch (Exception e) {
                Platform.runLater(() -> lblHospStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void alert(String m) { new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK).showAndWait(); }

    public static class OcRow {
        private final int id, rawId;
        private final String paciente, cama, sala, ingreso;
        public OcRow(int id, String pac, String cama, String sala, String ing, int rawId) {
            this.id=id; paciente=pac; this.cama=cama; this.sala=sala; ingreso=ing; this.rawId=rawId; }
        public int getId()          { return id; }
        public int getRawId()       { return rawId; }
        public String getPaciente() { return paciente; }
        public String getCama()     { return cama; }
        public String getSala()     { return sala; }
        public String getIngreso()  { return ingreso; }
    }
}