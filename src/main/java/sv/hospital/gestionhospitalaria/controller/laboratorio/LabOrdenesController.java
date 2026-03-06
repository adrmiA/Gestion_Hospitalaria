package sv.hospital.gestionhospitalaria.controller.laboratorio;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.service.ApiService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class LabOrdenesController implements Initializable {

    @FXML private TableView<OrdRow> tableOrdenes;
    @FXML private TableColumn<OrdRow,Integer> colId;
    @FXML private TableColumn<OrdRow,String> colArea, colTipo, colFecha, colEstado, colResult;
    @FXML private ComboBox<String> cmbEstadoFiltro;
    @FXML private TextField txtOrdenId;
    @FXML private TextArea txtResultado;
    @FXML private Label lblOrdenInfo, lblLabStatus;

    private final ObservableList<OrdRow> allData = FXCollections.observableArrayList();
    private int currentOrdenId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colResult.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        cmbEstadoFiltro.setItems(FXCollections.observableArrayList("Todos","Pendiente","En proceso","Completada"));
        cmbEstadoFiltro.setValue("Todos");
        loadOrdenes();
    }

    @FXML public void loadOrdenes() {
        new Thread(() -> {
            try {
                JsonArray arr = ApiService.getArray("/Laboratorio");
                List<OrdRow> rows = new ArrayList<>();
                for (var el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    String area = o.has("areaLaboratorio") && !o.get("areaLaboratorio").isJsonNull()
                            ? o.getAsJsonObject("areaLaboratorio").get("nombreArea").getAsString() : "—";
                    rows.add(new OrdRow(o.get("idOrden").getAsInt(), area,
                            o.get("tipoExamen").getAsString(),
                            o.get("fechaSolicitud").getAsString(),
                            o.get("estado").getAsString(),
                            o.has("resultado") && !o.get("resultado").isJsonNull()
                                    ? o.get("resultado").getAsString() : "—"));
                }
                Platform.runLater(() -> { allData.setAll(rows); tableOrdenes.setItems(allData); });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    @FXML public void filterOrdenes() {
        String f = cmbEstadoFiltro.getValue();
        if ("Todos".equals(f)) { tableOrdenes.setItems(allData); return; }
        tableOrdenes.setItems(allData.filtered(r -> r.getEstado().equalsIgnoreCase(f)));
    }

    @FXML public void cargarOrden() {
        String id = txtOrdenId.getText().trim();
        if (id.isEmpty()) return;
        new Thread(() -> {
            try {
                JsonObject o = ApiService.getObject("/Laboratorio/" + id);
                currentOrdenId = o.get("idOrden").getAsInt();
                String area = o.has("areaLaboratorio") && !o.get("areaLaboratorio").isJsonNull()
                        ? o.getAsJsonObject("areaLaboratorio").get("nombreArea").getAsString() : "—";
                String info = "Orden #" + currentOrdenId + " | " + area + " | " + o.get("tipoExamen").getAsString()
                        + " | Estado: " + o.get("estado").getAsString();
                Platform.runLater(() -> lblOrdenInfo.setText(info));
            } catch (Exception e) {
                Platform.runLater(() -> lblOrdenInfo.setText("Orden no encontrada."));
            }
        }).start();
    }

    @FXML public void guardarResultado() {
        if (currentOrdenId < 0) { lblLabStatus.setText("Carga una orden primero."); return; }
        String resultado = txtResultado.getText().trim();
        if (resultado.isEmpty()) { lblLabStatus.setText("Ingresa el resultado."); return; }
        new Thread(() -> {
            try {
                JsonObject body = new JsonObject();
                body.addProperty("resultado", resultado);
                body.addProperty("estado", "Completada");
                ApiService.put("/Laboratorio/" + currentOrdenId, body);
                Platform.runLater(() -> {
                    lblLabStatus.setText("✅ Resultado guardado correctamente.");
                    txtResultado.clear(); lblOrdenInfo.setText(""); currentOrdenId = -1; loadOrdenes();
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblLabStatus.setText("Error: " + e.getMessage()));
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
        public int getId()           { return id; }
        public String getArea()      { return area; }
        public String getTipo()      { return tipo; }
        public String getFecha()     { return fecha; }
        public String getEstado()    { return estado; }
        public String getResultado() { return resultado; }
    }
}