package sv.hospital.gestionhospitalaria.controller.laboratorio;

import sv.hospital.gestionhospitalaria.MainApp;
import sv.hospital.gestionhospitalaria.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LabDashboardController implements Initializable {

    @FXML private Label lblNombre;
    @FXML private StackPane contentArea;
    @FXML private Button btnOrdenes, btnResultados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNombre.setText(SessionManager.getInstance().getUserName());
        loadContent("/hospital/fxml/laboratorio/LabOrdenes.fxml", btnOrdenes);
    }

    @FXML void showOrdenes()    { loadContent("/hospital/fxml/laboratorio/LabOrdenes.fxml",    btnOrdenes); }
    @FXML void showResultados() { loadContent("/hospital/fxml/laboratorio/LabResultados.fxml", btnResultados); }

    @FXML void handleLogout() {
        SessionManager.getInstance().logout();
        MainApp.loadScene("/hospital/fxml/Login.fxml", "Hospital - Iniciar Sesión", 520, 400);
    }

    private void loadContent(String fxml, Button active) {
        for (Button b : new Button[]{btnOrdenes, btnResultados})
            b.getStyleClass().removeAll("menu-btn-active");
        active.getStyleClass().add("menu-btn-active");
        try {
            Node n = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(n);
        } catch (Exception e) { e.printStackTrace(); }
    }
}