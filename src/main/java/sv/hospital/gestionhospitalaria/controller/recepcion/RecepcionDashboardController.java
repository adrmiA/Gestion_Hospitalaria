package sv.hospital.gestionhospitalaria.controller.recepcion;

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

public class RecepcionDashboardController implements Initializable {

    @FXML private Label lblNombre;
    @FXML private StackPane contentArea;
    @FXML private Button btnPacientes, btnCitas, btnHosp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNombre.setText(SessionManager.getInstance().getUserName());
        loadContent("/hospital/fxml/recepcion/RecepcionPacientes.fxml", btnPacientes);
    }

    @FXML void showPacientes()      { loadContent("/hospital/fxml/recepcion/RecepcionPacientes.fxml",       btnPacientes); }
    @FXML void showCitas()          { loadContent("/hospital/fxml/recepcion/RecepcionCitas.fxml",           btnCitas); }
    @FXML void showHospitalizacion(){ loadContent("/hospital/fxml/recepcion/RecepcionHospitalizacion.fxml", btnHosp); }

    @FXML void handleLogout() {
        SessionManager.getInstance().logout();
        MainApp.loadScene("/hospital/fxml/Login.fxml", "Hospital - Iniciar Sesión", 520, 400);
    }

    private void loadContent(String fxml, Button active) {
        for (Button b : new Button[]{btnPacientes, btnCitas, btnHosp})
            b.getStyleClass().removeAll("menu-btn-active");
        active.getStyleClass().add("menu-btn-active");
        try {
            Node n = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(n);
        } catch (Exception e) { e.printStackTrace(); }
    }
}