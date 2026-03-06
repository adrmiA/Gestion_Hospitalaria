package sv.hospital.gestionhospitalaria.controller.medico;

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

public class MedicoDashboardController implements Initializable {

    @FXML private Label lblNombre;
    @FXML private StackPane contentArea;
    @FXML private Button btnInicio, btnCitas, btnConsultas, btnLaboratorio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNombre.setText("Dr. " + SessionManager.getInstance().getUserName());
        loadContent("/hospital/fxml/medico/MedicoCitas.fxml", btnCitas);
    }

    @FXML void showDashboard()    { loadContent("/hospital/fxml/medico/MedicoCitas.fxml",       btnInicio); }
    @FXML void showCitas()        { loadContent("/hospital/fxml/medico/MedicoCitas.fxml",        btnCitas); }
    @FXML void showConsultas()    { loadContent("/hospital/fxml/medico/MedicoConsultas.fxml",    btnConsultas); }
    @FXML void showLaboratorio()  { loadContent("/hospital/fxml/medico/MedicoLaboratorio.fxml",  btnLaboratorio); }

    @FXML void handleLogout() {
        SessionManager.getInstance().logout();
        MainApp.loadScene("/hospital/fxml/Login.fxml", "Hospital - Iniciar Sesión", 520, 400);
    }

    private void loadContent(String fxml, Button active) {
        for (Button b : new Button[]{btnInicio, btnCitas, btnConsultas, btnLaboratorio})
            b.getStyleClass().removeAll("menu-btn-active");
        active.getStyleClass().add("menu-btn-active");
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) { e.printStackTrace(); }
    }
}