package com.hospital.swing.enfermeria;

import com.google.gson.*;
import com.hospital.swing.api.ApiClient;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class SignosVitalesPanel extends javax.swing.JPanel {

    private DefaultTableModel modelo;
    private int idExpediente = -1;
    private String nombrePacienteActual = "";
    private JsonArray historialActual = new JsonArray();

    public SignosVitalesPanel() {
        initComponents();
        configurarTabla();
        lblNombrePaciente.setText("Seleccione un paciente");
        btnVerEvolucion.setEnabled(false);
    }

    private void configurarTabla() {
        modelo = new DefaultTableModel(
            new String[]{"Fecha", "FC", "FR", "Temp", "PA", "O2", "Peso", "Talla"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHistorialSignos.setModel(modelo);
        tblHistorialSignos.setRowHeight(25);
        tblHistorialSignos.getTableHeader().setBackground(new Color(30, 80, 150));
        tblHistorialSignos.getTableHeader().setForeground(Color.WHITE);
    }

    private void buscarPaciente() {
        String dui = txtBusquedaDui.getText().trim();
        if (dui.isEmpty() || dui.equals("DUI")) {
            JOptionPane.showMessageDialog(this, "Ingrese un DUI válido.");
            return;
        }

        btnBuscarPaciente.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            String errorMsg = null;

            @Override protected Boolean doInBackground() throws Exception {
                try {
                    JsonElement resE = ApiClient.get("/expedientes/buscar-dui/" + dui);
                    if (resE.isJsonObject()) {
                        JsonObject exp = resE.getAsJsonObject();
                        idExpediente = exp.has("id") ? exp.get("id").getAsInt() : exp.get("idExpediente").getAsInt();
                        nombrePacienteActual = exp.get("nombrePaciente").getAsString();

                        JsonElement resS = ApiClient.get("/signosvitales/expediente/" + idExpediente);
                        historialActual = resS.isJsonArray() ? resS.getAsJsonArray() : new JsonArray();
                        return true;
                    }
                } catch (Exception e) { errorMsg = e.getMessage(); }
                return false;
            }

            @Override protected void done() {
                btnBuscarPaciente.setEnabled(true);
                try {
                    if (get()) {
                        lblNombrePaciente.setText(nombrePacienteActual);
                        lblNombrePaciente.setForeground(new Color(0, 153, 51));
                        btnVerEvolucion.setEnabled(true);
                        actualizarTabla();
                    } else {
                        lblNombrePaciente.setText("No encontrado.");
                        lblNombrePaciente.setForeground(Color.RED);
                        btnVerEvolucion.setEnabled(false);
                        idExpediente = -1;
                        modelo.setRowCount(0);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (JsonElement el : historialActual) {
            JsonObject s = el.getAsJsonObject();
            modelo.addRow(new Object[]{
                gs(s, "fechaRegistro"), gs(s, "frecuenciaCardiaca"), 
                gs(s, "frecuenciaRespiratoria"), gs(s, "temperatura"), 
                gs(s, "presionArterial"), gs(s, "saturacionOxigeno"), 
                gs(s, "peso"), gs(s, "talla")
            });
        }
    }

    private void registrarSignos() {
        if (idExpediente == -1) return;

        JsonObject sv = new JsonObject();
        sv.addProperty("IdExpediente", idExpediente);
        sv.addProperty("FrecuenciaCardiaca", txtFrecuenciaCardiaca.getText().trim());
        sv.addProperty("FrecuenciaRespiratoria", txtFR.getText().trim());
        sv.addProperty("Temperatura", txtTemperatura.getText().trim());
        sv.addProperty("PresionArterial", txtPresionArterial.getText().trim());
        sv.addProperty("SaturacionOxigeno", txtSaturacionOxigeno.getText().trim());
        sv.addProperty("Peso", txtPeso.getText().trim());
        sv.addProperty("Talla", txtTalla.getText().trim());

        btnGuardarSignos.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                ApiClient.post("/signosvitales", sv);
                return true;
            }
            @Override protected void done() {
                btnGuardarSignos.setEnabled(true);
                JOptionPane.showMessageDialog(SignosVitalesPanel.this, "Signos Vitales registrados.");
                limpiarCampos();
                buscarPaciente(); 
            }
        }.execute();
    }

    private void limpiarCampos() {
        txtFrecuenciaCardiaca.setText(""); txtFR.setText("");
        txtTemperatura.setText(""); txtPresionArterial.setText("");
        txtSaturacionOxigeno.setText(""); txtPeso.setText(""); txtTalla.setText("");
    }

    private String gs(JsonObject o, String k) {
        return (o.has(k) && !o.get(k).isJsonNull()) ? o.get(k).getAsString() : "";
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtBusquedaDui = new javax.swing.JTextField();
        btnBuscarPaciente = new javax.swing.JButton();
        txtFrecuenciaCardiaca = new javax.swing.JTextField();
        txtFR = new javax.swing.JTextField();
        txtTemperatura = new javax.swing.JTextField();
        txtPresionArterial = new javax.swing.JTextField();
        txtSaturacionOxigeno = new javax.swing.JTextField();
        txtPeso = new javax.swing.JTextField();
        txtTalla = new javax.swing.JTextField();
        btnGuardarSignos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorialSignos = new javax.swing.JTable();
        lblNombrePaciente = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnVerEvolucion = new javax.swing.JButton();

        txtBusquedaDui.setColumns(12);
        txtBusquedaDui.setText("DUI");
        txtBusquedaDui.addActionListener(this::txtBusquedaDuiActionPerformed);

        btnBuscarPaciente.setText("Buscar");
        btnBuscarPaciente.addActionListener(this::btnBuscarPacienteActionPerformed);

        txtFrecuenciaCardiaca.setText("FC");
        txtFrecuenciaCardiaca.addActionListener(this::txtFrecuenciaCardiacaActionPerformed);

        txtFR.setText("FR");
        txtFR.addActionListener(this::txtFRActionPerformed);

        txtTemperatura.setText("Temperatura");
        txtTemperatura.addActionListener(this::txtTemperaturaActionPerformed);

        txtPresionArterial.setText("PA");
        txtPresionArterial.addActionListener(this::txtPresionArterialActionPerformed);

        txtSaturacionOxigeno.setText("Sat");
        txtSaturacionOxigeno.addActionListener(this::txtSaturacionOxigenoActionPerformed);

        txtPeso.setText("Peso");
        txtPeso.addActionListener(this::txtPesoActionPerformed);

        txtTalla.setText("Talla");
        txtTalla.addActionListener(this::txtTallaActionPerformed);

        btnGuardarSignos.setText("Registrar");
        btnGuardarSignos.addActionListener(this::btnGuardarSignosActionPerformed);

        tblHistorialSignos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblHistorialSignos);

        lblNombrePaciente.setText("Paciente: (No seleccionado)");

        jLabel1.setText("F. Cardíaca:");

        jLabel2.setText("Temperatura:");

        jLabel3.setText("P. Arterial:");

        jLabel4.setText("Saturación Oxigeno:");

        jLabel5.setText("Peso:");

        jLabel6.setText("Talla:");

        jLabel7.setText("F. Respiratoria:");

        btnVerEvolucion.setText("Gráfico");
        btnVerEvolucion.addActionListener(this::btnVerEvolucionActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardarSignos, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnVerEvolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtBusquedaDui, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscarPaciente))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtTemperatura)
                                                .addComponent(txtTalla, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(17, 17, 17)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addComponent(txtPresionArterial, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(18, 18, 18)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7)
                                                        .addComponent(txtFR, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addComponent(jLabel6)))
                                .addComponent(lblNombrePaciente)
                                .addComponent(jLabel1))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(txtSaturacionOxigeno, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBusquedaDui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPaciente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombrePaciente)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFrecuenciaCardiaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPresionArterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSaturacionOxigeno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarSignos)
                    .addComponent(btnVerEvolucion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusquedaDuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaDuiActionPerformed
        // TODO add your handling code here:
         btnBuscarPacienteActionPerformed(evt);
    }//GEN-LAST:event_txtBusquedaDuiActionPerformed

    private void btnBuscarPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPacienteActionPerformed
        // TODO add your handling code here:
         String dui = txtBusquedaDui.getText().trim();
        if (dui.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DUI para buscar.");
            return;
        }

        btnBuscarPaciente.setEnabled(false);
        new SwingWorker<Void, Void>() {
            String errorMsg = null;

            @Override protected Void doInBackground() throws Exception {
                try {
                    JsonElement resE = ApiClient.get("/expedientes/buscar-dui/" + dui);
                    if (resE.isJsonObject()) {
                        JsonObject exp = resE.getAsJsonObject();
                        idExpediente = exp.get("id").getAsInt();
                        nombrePacienteActual = exp.get("nombrePaciente").getAsString();

                        JsonElement resS = ApiClient.get("/signosvitales/expediente/" + idExpediente);
                        historialActual = resS.isJsonArray() ? resS.getAsJsonArray() : new JsonArray();
                    } else {
                        idExpediente = -1;
                    }
                } catch (Exception e) { errorMsg = e.getMessage(); }
                return null;
            }

            @Override protected void done() {
                btnBuscarPaciente.setEnabled(true);
                if (idExpediente != -1) {
                    lblNombrePaciente.setText(nombrePacienteActual);
                    lblNombrePaciente.setForeground(new Color(0, 153, 51));
                    btnVerEvolucion.setEnabled(true);
                    actualizarTabla();
                } else {
                    lblNombrePaciente.setText("No encontrado.");
                    lblNombrePaciente.setForeground(Color.RED);
                    btnVerEvolucion.setEnabled(false);
                    modelo.setRowCount(0);
                }
            }
        }.execute();
    
    }//GEN-LAST:event_btnBuscarPacienteActionPerformed

    private void txtFrecuenciaCardiacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFrecuenciaCardiacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFrecuenciaCardiacaActionPerformed

    private void txtFRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFRActionPerformed

    private void txtTemperaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTemperaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTemperaturaActionPerformed

    private void txtPresionArterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPresionArterialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPresionArterialActionPerformed

    private void txtSaturacionOxigenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaturacionOxigenoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaturacionOxigenoActionPerformed

    private void txtPesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesoActionPerformed

    private void txtTallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTallaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTallaActionPerformed

    private void btnGuardarSignosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarSignosActionPerformed
        // TODO add your handling code here:
        if (idExpediente == -1) {
            JOptionPane.showMessageDialog(this, "Busque un paciente primero.");
            return;
        }

        JsonObject sv = new JsonObject();
        sv.addProperty("IdExpediente", idExpediente);
        sv.addProperty("FrecuenciaCardiaca", txtFrecuenciaCardiaca.getText().trim());
        sv.addProperty("FrecuenciaRespiratoria", txtFR.getText().trim());
        sv.addProperty("Temperatura", txtTemperatura.getText().trim());
        sv.addProperty("PresionArterial", txtPresionArterial.getText().trim());
        sv.addProperty("SaturacionOxigeno", txtSaturacionOxigeno.getText().trim());
        sv.addProperty("Peso", txtPeso.getText().trim());
        sv.addProperty("Talla", txtTalla.getText().trim());

        btnGuardarSignos.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                ApiClient.post("/signosvitales", sv);
                return true;
            }
            @Override protected void done() {
                btnGuardarSignos.setEnabled(true);
                JOptionPane.showMessageDialog(SignosVitalesPanel.this, "Registro guardado.");
                limpiarCampos();
                btnBuscarPacienteActionPerformed(null); 
            }
        }.execute();
    }//GEN-LAST:event_btnGuardarSignosActionPerformed

    private void btnVerEvolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerEvolucionActionPerformed
        // TODO add your handling code here:
        if (idExpediente == -1 || historialActual.size() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para graficar.");
            return;
        }
        Window parent = SwingUtilities.getWindowAncestor(this);
        SignosChartDialog dialog = new SignosChartDialog((Frame)parent, nombrePacienteActual, historialActual);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnVerEvolucionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPaciente;
    private javax.swing.JButton btnGuardarSignos;
    private javax.swing.JButton btnVerEvolucion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNombrePaciente;
    private javax.swing.JTable tblHistorialSignos;
    private javax.swing.JTextField txtBusquedaDui;
    private javax.swing.JTextField txtFR;
    private javax.swing.JTextField txtFrecuenciaCardiaca;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtPresionArterial;
    private javax.swing.JTextField txtSaturacionOxigeno;
    private javax.swing.JTextField txtTalla;
    private javax.swing.JTextField txtTemperatura;
    // End of variables declaration//GEN-END:variables
}
