package com.hospital.swing.session;

public class SessionManager {
    private static SessionManager instance;
    private String token, rolNombre, nombreUsuario;
    private int idUsuario;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void iniciarSesion(String token, String rol, int idUsuario, String nombre) {
        this.token = token;
        this.rolNombre = rol;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombre;
    }

    public void cerrarSesion() {
        token = null; rolNombre = null; idUsuario = 0; nombreUsuario = null;
    }

    public String getToken()         { return token; }
    public String getRolNombre()     { return rolNombre; }
    public int    getIdUsuario()     { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public boolean estaAutenticado() { return token != null; }
}