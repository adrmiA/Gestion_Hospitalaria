package com.hospital.swing;

import com.hospital.swing.login.LoginFrame;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch(Exception e){}
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
