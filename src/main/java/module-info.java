module sv.hospital.gestionhospitalaria {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens sv.hospital.gestionhospitalaria to javafx.fxml;
    opens sv.hospital.gestionhospitalaria.controller.laboratorio to javafx.fxml;
    opens sv.hospital.gestionhospitalaria.controller.medico to javafx.fxml;
    opens sv.hospital.gestionhospitalaria.controller.recepcion to javafx.fxml;
    opens sv.hospital.gestionhospitalaria.controller to javafx.fxml;
    opens sv.hospital.gestionhospitalaria.model to com.google.gson, javafx.base;

    // ✅ AGREGAR ESTAS DOS LÍNEAS:
    opens sv.hospital.gestionhospitalaria.service to com.google.gson;
    opens sv.hospital.gestionhospitalaria.session to com.google.gson, javafx.base;

    exports sv.hospital.gestionhospitalaria;
}