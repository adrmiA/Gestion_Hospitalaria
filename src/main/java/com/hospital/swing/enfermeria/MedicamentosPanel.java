package com.hospital.swing.enfermeria;

import com.google.gson.*;
import com.hospital.swing.api.ApiClient;
import com.hospital.swing.session.SessionManager;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MedicamentosPanel extends javax.swing.JPanel {

    private DefaultTableModel modelo;
    private int idExpediente = -1;
    private Map<String, Integer> mapaMedicamentos;

    public MedicamentosPanel() {
        mapaMedicamentos = new HashMap<>(); 
        initComponents();
        configurarTabla();
        cargarMedicamentos();
        btnRegistrarAdmin.setEnabled(false); 
    }

    private void configurarTabla() {
        modelo = new DefaultTableModel(
            new String[]{"Fecha", "Medicamento", "Dosis", "Cantidad", "Enfermero"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMedicamentosDados.setModel(modelo);
        tblMedicamentosDados.setRowHeight(25);
        tblMedicamentosDados.getTableHeader().setBackground(new Color(30, 80, 150));
        tblMedicamentosDados.getTableHeader().setForeground(Color.WHITE);
    }

    private void cargarMedicamentos() {
        new SwingWorker<JsonArray, Void>() {
            @Override
            protected JsonArray doInBackground() throws Exception {
                JsonElement r = ApiClient.get("/productos");
                return r.isJsonArray() ? r.getAsJsonArray() : new JsonArray();
            }

            @Override
            protected void done() {
                try {
                    cmbListaMedicamentos.removeAllItems();
                    mapaMedicamentos.clear();
                    for (JsonElement el : get()) {
                        JsonObject p = el.getAsJsonObject();
                        int id = p.has("id") ? p.get("id").getAsInt() : p.get("id_producto").getAsInt();
                        String nombre = gs(p, "nombre");
                        cmbListaMedicamentos.addItem(nombre);
                        mapaMedicamentos.put(nombre, id);
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        }.execute();
    }

    private void buscarPaciente() {
        String dui = txtDuiFiltro.getText().trim();
        if (dui.isEmpty() || dui.equals("DUI")) return;

        btnBuscar.setEnabled(false);
        new SwingWorker<JsonObject, Void>() {
            String err;

            @Override protected JsonObject doInBackground() {
                try {
                    JsonElement rPac = ApiClient.get("/pacientes/dui/" + dui);
                    if (!rPac.isJsonObject()) { err = "Paciente no encontrado."; return null; }
                    JsonObject pac = rPac.getAsJsonObject();

                    if ("Inactivo".equalsIgnoreCase(gs(pac, "estado"))) {
                        err = "PACIENTE INACTIVO."; return null;
                    }

                    int idPac = pac.has("id") ? pac.get("id").getAsInt() : pac.get("idPaciente").getAsInt();

                    JsonElement rExp = ApiClient.get("/expedientes/paciente/" + idPac);
                    JsonObject exp = rExp.isJsonArray() ? rExp.getAsJsonArray().get(0).getAsJsonObject() : rExp.getAsJsonObject();
                    
                    idExpediente = exp.has("id") ? exp.get("id").getAsInt() : exp.get("idExpediente").getAsInt();

                    return pac;
                } catch (Exception ex) { err = "Error: " + ex.getMessage(); return null; }
            }

            @Override protected void done() {
                btnBuscar.setEnabled(true);
                try {
                    JsonObject pac = get();
                    if (pac != null) {
                        lblNombrePaciente.setText("Paciente: " + gs(pac, "nombre") + " " + gs(pac, "apellido"));
                        lblNombrePaciente.setForeground(new Color(34, 139, 34));
                        btnRegistrarAdmin.setEnabled(true);
                        cargarHistorialMedicamentos(); 
                    } else {
                        lblNombrePaciente.setText(err);
                        lblNombrePaciente.setForeground(Color.RED);
                        btnRegistrarAdmin.setEnabled(false);
                        idExpediente = -1;
                        modelo.setRowCount(0);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void cargarHistorialMedicamentos() {
        if (idExpediente == -1) return;
        new SwingWorker<JsonArray, Void>() {
            @Override protected JsonArray doInBackground() throws Exception {
                JsonElement rMed = ApiClient.get("/administracion-medicamentos/expediente/" + idExpediente);
                return rMed.isJsonArray() ? rMed.getAsJsonArray() : new JsonArray();
            }

            @Override protected void done() {
                try {
                    modelo.setRowCount(0);
                    JsonArray lista = get();
                    for (JsonElement el : lista) {
                        JsonObject m = el.getAsJsonObject();
                        modelo.addRow(new Object[]{
                            gs(m, "fechaAdministracion"), 
                            gs(m, "medicamento"), 
                            gs(m, "dosis"), 
                            gs(m, "cantidad"), 
                            gs(m, "enfermero")
                        });
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void registrarAdministracion() {
        if (idExpediente < 0 || cmbListaMedicamentos.getSelectedItem() == null) return;
        
        String dosis = txtDosisSubmit.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();

        if (dosis.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete dosis y cantidad.");
            return;
        }

        btnRegistrarAdmin.setEnabled(false);
        int idProducto = mapaMedicamentos.get(cmbListaMedicamentos.getSelectedItem().toString());

        new SwingWorker<Boolean, Void>() {
            String error;
            @Override protected Boolean doInBackground() {
                try {
                    JsonObject m = new JsonObject();
                    m.addProperty("idExpediente", idExpediente);
                    m.addProperty("idProducto",   idProducto);
                    m.addProperty("idUsuario",    SessionManager.getInstance().getIdUsuario());
                    m.addProperty("dosis",        dosis);
                    m.addProperty("cantidad",     Integer.parseInt(cantidadStr));
                    
                    ApiClient.post("/administracion-medicamentos", m);
                    return true;
                } catch (Exception ex) { error = ex.getMessage(); return false; }
            }
            @Override protected void done() {
                btnRegistrarAdmin.setEnabled(true);
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(null, "Registrado.");
                        txtDosisSubmit.setText(""); txtCantidad.setText("");
                        cargarHistorialMedicamentos(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: " + error);
                    }
                } catch (Exception e) {}
            }
        }.execute();
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

        txtDuiFiltro = new javax.swing.JTextField();
        cmbListaMedicamentos = new javax.swing.JComboBox<>();
        txtDosisSubmit = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnRegistrarAdmin = new javax.swing.JButton();
        lblNombrePaciente = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMedicamentosDados = new javax.swing.JTable();

        txtDuiFiltro.setText("DUI");
        txtDuiFiltro.addActionListener(this::txtDuiFiltroActionPerformed);

        cmbListaMedicamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbListaMedicamentos.addActionListener(this::cmbListaMedicamentosActionPerformed);

        txtDosisSubmit.setText("Dosis");
        txtDosisSubmit.addActionListener(this::txtDosisSubmitActionPerformed);

        txtCantidad.setText("Cantidad");
        txtCantidad.addActionListener(this::txtCantidadActionPerformed);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnRegistrarAdmin.setText("Registrar");
        btnRegistrarAdmin.addActionListener(this::btnRegistrarAdminActionPerformed);

        lblNombrePaciente.setText("Paciente: (No seleccionado)");

        jLabel1.setText("Fármacos:");

        jLabel2.setText("Dosis:");

        jLabel3.setText("Cantidad:");

        tblMedicamentosDados.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblMedicamentosDados);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtDuiFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnBuscar))
                    .addComponent(lblNombrePaciente)
                    .addComponent(btnRegistrarAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbListaMedicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDosisSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDuiFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombrePaciente)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbListaMedicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDosisSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnRegistrarAdmin)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDuiFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDuiFiltroActionPerformed
        // TODO add your handling code here:
        btnBuscarActionPerformed(evt);
    }//GEN-LAST:event_txtDuiFiltroActionPerformed

    private void cmbListaMedicamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbListaMedicamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbListaMedicamentosActionPerformed

    private void txtDosisSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDosisSubmitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDosisSubmitActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:                                     
        String dui = txtDuiFiltro.getText().trim();
        if (dui.isEmpty() || dui.equalsIgnoreCase("DUI")) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un DUI válido.");
            return;
        }

        btnBuscar.setEnabled(false);
        lblNombrePaciente.setText("Buscando...");

        new SwingWorker<JsonObject, Void>() {
            String err;
            JsonArray historial = new JsonArray();

            @Override protected JsonObject doInBackground() {
                try {
                    JsonElement rPac = ApiClient.get("/pacientes/dui/" + dui);
                    if (!rPac.isJsonObject()) { err = "Paciente no encontrado."; return null; }
                    JsonObject pac = rPac.getAsJsonObject();

                    String estado = (pac.has("estado")) ? pac.get("estado").getAsString() : "";
                    if ("Inactivo".equalsIgnoreCase(estado)) {
                        err = "EL PACIENTE ESTÁ INACTIVO. No se puede administrar medicación.";
                        return null;
                    }

                    int idPac = -1;
                    if (pac.has("id")) idPac = pac.get("id").getAsInt();
                    else if (pac.has("idPaciente")) idPac = pac.get("idPaciente").getAsInt();

                    JsonElement rExp = ApiClient.get("/expedientes/paciente/" + idPac);
                    JsonObject exp = rExp.isJsonArray() ? rExp.getAsJsonArray().get(0).getAsJsonObject() : rExp.getAsJsonObject();

                    idExpediente = exp.has("id") ? exp.get("id").getAsInt() : exp.get("idExpediente").getAsInt();

                    JsonElement rMed = ApiClient.get("/administracion-medicamentos/expediente/" + idExpediente);
                    if (rMed.isJsonArray()) historial = rMed.getAsJsonArray();

                    return pac;
                } catch (Exception ex) {
                    err = "Error de conexión: " + ex.getMessage();
                    return null;
                }
            }

            @Override protected void done() {
                btnBuscar.setEnabled(true);
                try {
                    JsonObject pac = get();
                    if (pac != null) {
                        String nombre = gs(pac, "nombre") + " " + gs(pac, "apellido");
                        lblNombrePaciente.setText("Paciente: " + nombre);
                        lblNombrePaciente.setForeground(new Color(34, 139, 34)); // Verde
                        btnRegistrarAdmin.setEnabled(true);

                        modelo.setRowCount(0);
                        for (JsonElement el : historial) {
                            JsonObject m = el.getAsJsonObject();
                            modelo.addRow(new Object[]{
                                gs(m, "fechaAdministracion"), 
                                gs(m, "nombreMedicamento"), 
                                gs(m, "dosis"), 
                                gs(m, "cantidad"), 
                                gs(m, "nombreEnfermero")
                            });
                        }
                    } else {
                        lblNombrePaciente.setText(err);
                        lblNombrePaciente.setForeground(Color.RED);
                        idExpediente = -1;
                        btnRegistrarAdmin.setEnabled(false);
                        modelo.setRowCount(0);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRegistrarAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarAdminActionPerformed
        // TODO add your handling code here:
        if (idExpediente < 0 || cmbListaMedicamentos.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un paciente y un medicamento.");
        return;
    }

    String dosis = txtDosisSubmit.getText().trim();
    String cantidad = txtCantidad.getText().trim();

    if (dosis.isEmpty() || cantidad.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar Dosis y Cantidad.");
        return;
    }

    btnRegistrarAdmin.setEnabled(false);
    String nombreMed = (String) cmbListaMedicamentos.getSelectedItem();
    Integer idProducto = mapaMedicamentos.get(nombreMed);

    new SwingWorker<Boolean, Void>() {
        String errorMsg;

        @Override protected Boolean doInBackground() {
            try {
                JsonObject m = new JsonObject();
                m.addProperty("idExpediente", idExpediente);
                m.addProperty("idProducto",   idProducto);
                m.addProperty("dosis",        dosis);
                m.addProperty("cantidad",     cantidad);
                m.addProperty("idUsuario",    SessionManager.getInstance().getIdUsuario());
                
                ApiClient.post("/administracion-medicamentos", m);
                return true;
            } catch (Exception ex) {
                errorMsg = ex.getMessage();
                return false;
            }
        }

        @Override protected void done() {
            btnRegistrarAdmin.setEnabled(true);
            try {
                if (get()) {
                    JOptionPane.showMessageDialog(MedicamentosPanel.this, "Medicamento registrado con éxito.");
                    txtDosisSubmit.setText(""); 
                    txtCantidad.setText("");
                    btnBuscarActionPerformed(null); 
                } else {
                    JOptionPane.showMessageDialog(MedicamentosPanel.this, "Error al registrar: " + errorMsg);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }.execute();
    }//GEN-LAST:event_btnRegistrarAdminActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegistrarAdmin;
    private javax.swing.JComboBox<String> cmbListaMedicamentos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNombrePaciente;
    private javax.swing.JTable tblMedicamentosDados;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDosisSubmit;
    private javax.swing.JTextField txtDuiFiltro;
    // End of variables declaration//GEN-END:variables
}
