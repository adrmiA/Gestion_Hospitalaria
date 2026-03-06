package sv.hospital.gestionhospitalaria.controller.medico;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import sv.hospital.gestionhospitalaria.service.ApiService;
import sv.hospital.gestionhospitalaria.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MedicoCitasController implements Initializable {

    @FXML private TableView<CitaRow> tableCitas;
    @FXML private TableColumn<CitaRow,Integer> colId;
    @FXML private TableColumn<CitaRow,String>  colPaciente, colFecha, colHora, colEstado;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Label lblStatus;

    private final ObservableList<CitaRow> allData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cmbEstado.setItems(FXCollections.observableArrayList("Todos","Pendiente","Confirmada","Cancelada"));
        cmbEstado.setValue("Todos");
        loadCitas();
    }

    @FXML public void loadCitas() {
        lblStatus.setText("Cargando...");
        Integer medicoId = SessionManager.getInstance().getMedicoId();
        if (medicoId == null) { lblStatus.setText("No se encontró ID de médico."); return; }
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Citas/medico/" + medicoId);
                List<CitaRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject c = el.getAsJsonObject();
                    String pac = c.has("paciente") && !c.get("paciente").isJsonNull()
                            ? c.getAsJsonObject("paciente").get("nombre").getAsString() + " "
                            + c.getAsJsonObject("paciente").get("apellido").getAsString()
                            : "N/A";
                    rows.add(new CitaRow(
                            c.get("idCita").getAsInt(), pac,
                            c.get("fecha").getAsString(),
                            c.get("hora").getAsString(),
                            c.get("estado").getAsString()
                    ));
                }
                Platform.runLater(() -> {
                    allData.setAll(rows);
                    tableCitas.setItems(allData);
                    lblStatus.setText("Total: " + rows.size() + " citas");
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void filterCitas() {
        String f = cmbEstado.getValue();
        if ("Todos".equals(f)) { tableCitas.setItems(allData); return; }
        ObservableList<CitaRow> filtered = FXCollections.observableArrayList(
                allData.filtered(r -> r.getEstado().equalsIgnoreCase(f)));
        tableCitas.setItems(filtered);
    }

    @FXML public void confirmarCita() { cambiarEstado("Confirmada"); }
    @FXML public void cancelarCita()  { cambiarEstado("Cancelada"); }

    private void cambiarEstado(String nuevoEstado) {
        CitaRow sel = tableCitas.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Seleccione una cita."); return; }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("estado", nuevoEstado);
                ApiService.put("/Citas/" + sel.getId() + "/estado", body);
                Platform.runLater(() -> { sel.setEstado(nuevoEstado); loadCitas(); });
            } catch (Exception e) {
                Platform.runLater(() -> alert("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void verExpediente() {
        CitaRow sel = tableCitas.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Seleccione una cita."); return; }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hospital/fxml/medico/MedicoConsultas.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 900, 600));
            stage.setTitle("Expediente - " + sel.getPaciente());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) { alert("Error al abrir expediente."); }
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    // ── Row model ──
    public static class CitaRow {
        private int id;
        private String paciente, fecha, hora, estado;
        public CitaRow(int id, String paciente, String fecha, String hora, String estado) {
            this.id = id; this.paciente = paciente; this.fecha = fecha;
            this.hora = hora; this.estado = estado;
        }
        public int getId() { return id; }
        public String getPaciente() { return paciente; }
        public String getFecha()    { return fecha; }
        public String getHora()     { return hora; }
        public String getEstado()   { return estado; }
        public void setEstado(String e) { this.estado = e; }
    }
}