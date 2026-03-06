package sv.hospital.gestionhospitalaria.service;

import com.google.gson.*;
import sv.hospital.gestionhospitalaria.session.SessionManager;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;

public class ApiService {

    private static final String BASE_URL = "https://localhost:7229/api"; // ← asegúrate que sea https

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .sslContext(trustAllSslContext())   // ← AGREGAR ESTO
            .build();

    // ── Acepta cualquier certificado (solo para desarrollo) ──────────────────
    private static SSLContext trustAllSslContext() {
        try {
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] c, String a) {}
                        public void checkServerTrusted(X509Certificate[] c, String a) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new SecureRandom());
            return sc;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear SSLContext", e);
        }
    }
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ─── AUTH ────────────────────────────────────────────────────────────────
    public static JsonObject login(String usuario, String contrasena) throws Exception {
        String body = gson.toJson(new LoginRequest(usuario, contrasena));
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Usuarios/login"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() == 200) return JsonParser.parseString(res.body()).getAsJsonObject();
        throw new Exception("Credenciales inválidas (código " + res.statusCode() + ")");
    }

    // ─── GET ─────────────────────────────────────────────────────────────────
    public static JsonArray getArray(String endpoint) throws Exception {
        HttpRequest req = buildGet(endpoint);
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        System.out.println("STATUS: " + res.statusCode());
        System.out.println("BODY: " + res.body());

        if (res.statusCode() >= 400) {
            throw new Exception("Error API GET: " + res.statusCode() + " - " + res.body());
        }

        return JsonParser.parseString(res.body()).getAsJsonArray();
    }

    public static JsonObject getObject(String endpoint) throws Exception {
        HttpRequest req = buildGet(endpoint);
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(res.body()).getAsJsonObject();
    }

    // ─── POST ────────────────────────────────────────────────────────────────
    public static JsonElement post(String endpoint, JsonObject body) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .POST(BodyPublishers.ofString(body.toString()))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() >= 400) throw new Exception("Error API: " + res.statusCode() + " - " + res.body());
        String rb = res.body();
        if (rb == null || rb.isBlank()) return JsonNull.INSTANCE;
        return JsonParser.parseString(rb);
    }

    // ─── PUT ─────────────────────────────────────────────────────────────────
    public static void put(String endpoint, JsonObject body) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .PUT(BodyPublishers.ofString(body.toString()))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() >= 400) throw new Exception("Error API PUT: " + res.statusCode() + " - " + res.body());
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────
    public static void delete(String endpoint) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .DELETE()
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() >= 400) throw new Exception("Error API DELETE: " + res.statusCode());
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────
    private static HttpRequest buildGet(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getToken())
                .GET()
                .build();
    }

    private static class LoginRequest {
        @com.google.gson.annotations.SerializedName("Usuario")
        private final String usuario;

        @com.google.gson.annotations.SerializedName("Contraseña")
        private final String contrasena;

        LoginRequest(String usuario, String contrasena) {
            this.usuario = usuario;
            this.contrasena = contrasena;
        }
    }
}