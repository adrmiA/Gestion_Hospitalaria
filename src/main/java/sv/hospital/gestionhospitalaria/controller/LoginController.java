package sv.hospital.gestionhospitalaria.controller;
import com.google.gson.JsonObject;
import sv.hospital.gestionhospitalaria.MainApp;
import sv.hospital.gestionhospitalaria.service.ApiService;
import sv.hospital.gestionhospitalaria.session.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    private void handleLogin() {
        lblError.setText("");
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            lblError.setText("Complete todos los campos.");
            return;
        }
        new Thread(() -> {
            try {
                JsonObject resp = ApiService.login(user, pass);
                String token = resp.get("token").getAsString();
                JsonObject userData = resp.getAsJsonObject("usuario");
                int userId = userData.get("idUsuario").getAsInt();
                String nombre = userData.get("nombre").getAsString();
                JsonObject rolObj = userData.getAsJsonObject("rol");
                String rolNombre = rolObj.get("nombre").getAsString();

                // Fetch medicoId if role is Medico
                Integer medicoId = null;
                SessionManager.getInstance().login(userId, nombre, rolNombre, token, null);
                if ("Medico".equalsIgnoreCase(rolNombre)) {
                    try {
                        var medicos = ApiService.getArray("/Medicos");
                        for (var el : medicos) {
                            var m = el.getAsJsonObject();
                            if (m.get("idUsuario").getAsInt() == userId) {
                                medicoId = m.get("idMedico").getAsInt();
                                break;
                            }
                        }
                    } catch (Exception ignored) {}
                }
                SessionManager.getInstance().login(userId, nombre, rolNombre, token, medicoId);

                final String rol = rolNombre;
                Platform.runLater(() -> navigateByRole(rol));
            } catch (Exception e) {
                Platform.runLater(() -> lblError.setText(e.getMessage()));
            }
        }).start();
    }

    private void navigateByRole(String rol) {
        switch (rol.toLowerCase()) {
            case "medico" -> MainApp.loadScene("/sv/hospital/gestionhospitalaria/medico/MedicoDashboard.fxml",
                    "Médico - Panel Principal", 1000, 680);
            case "recepcion" -> MainApp.loadScene("/sv/hospital/gestionhospitalaria/recepcion/RecepcionDashboard.fxml",
                    "Recepción - Panel Principal", 1000, 680);
            case "laboratorio" -> MainApp.loadScene("/sv/hospital/gestionhospitalaria/laboratorio/LabDashboard.fxml",
                    "Laboratorio - Panel Principal", 1000, 680);
            default -> lblError.setText("Rol no soportado en este cliente: " + rol);
        }
    }
}