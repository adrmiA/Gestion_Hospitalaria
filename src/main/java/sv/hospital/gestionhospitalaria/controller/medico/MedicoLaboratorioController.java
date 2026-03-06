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

public class MedicoLaboratorioController implements Initializable {

    @FXML private TableView<OrdRow> tableOrdenes;
    @FXML private TableColumn<OrdRow,Integer> colOrdId;
    @FXML private TableColumn<OrdRow,String> colOrdArea, colOrdTipo, colOrdFecha, colOrdEstado, colOrdResult;
    @FXML private TextField txtNumOrden;
    @FXML private Label lblLabStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colOrdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrdArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colOrdTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colOrdFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colOrdEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colOrdResult.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        loadOrdenes();
    }

    @FXML public void loadOrdenes() {
        Integer medicoId = SessionManager.getInstance().getMedicoId();
        if (medicoId == null) return;
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Laboratorio/medico/" + medicoId);
                List<OrdRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    String area = o.has("areaLaboratorio") && !o.get("areaLaboratorio").isJsonNull()
                            ? o.getAsJsonObject("areaLaboratorio").get("nombreArea").getAsString() : "-";
                    rows.add(new OrdRow(
                            o.get("idOrden").getAsInt(), area,
                            o.get("tipoExamen").getAsString(),
                            o.get("fechaSolicitud").getAsString(),
                            o.get("estado").getAsString(),
                            o.has("resultado") && !o.get("resultado").isJsonNull()
                                    ? o.get("resultado").getAsString() : "—"
                    ));
                }
                Platform.runLater(() -> { tableOrdenes.setItems(FXCollections.observableArrayList(rows));
                    lblLabStatus.setText(rows.size() + " órdenes"); });
            } catch (Exception e) {
                Platform.runLater(() -> lblLabStatus.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML public void buscarOrden() {
        String id = txtNumOrden.getText().trim();
        if (id.isEmpty()) return;
        new Thread(() -> {
            try {
                JsonObject o = ApiService.getObject("/Laboratorio/" + id);
                String area = o.has("areaLaboratorio") && !o.get("areaLaboratorio").isJsonNull()
                        ? o.getAsJsonObject("areaLaboratorio").get("nombreArea").getAsString() : "-";
                OrdRow row = new OrdRow(o.get("idOrden").getAsInt(), area,
                        o.get("tipoExamen").getAsString(),
                        o.get("fechaSolicitud").getAsString(),
                        o.get("estado").getAsString(),
                        o.has("resultado") && !o.get("resultado").isJsonNull()
                                ? o.get("resultado").getAsString() : "—");
                Platform.runLater(() -> tableOrdenes.setItems(FXCollections.observableArrayList(row)));
            } catch (Exception e) {
                Platform.runLater(() -> lblLabStatus.setText("Orden no encontrada."));
            }
        }).start();
    }

    public static class OrdRow {
        private final int id;
        private final String area, tipo, fecha, estado, resultado;
        public OrdRow(int id, String area, String tipo, String fecha, String estado, String resultado) {
            this.id=id; this.area=area; this.tipo=tipo; this.fecha=fecha;
            this.estado=estado; this.resultado=resultado;
        }
        public int getId()        { return id; }
        public String getArea()   { return area; }
        public String getTipo()   { return tipo; }
        public String getFecha()  { return fecha; }
        public String getEstado() { return estado; }
        public String getResultado() { return resultado; }
    }
}