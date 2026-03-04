package com.hospital.swing.api;

import com.google.gson.*;
import com.hospital.swing.session.SessionManager;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:5221/api";
    private static final int TIMEOUT = 10000;

    public static JsonElement get(String endpoint) throws Exception {
        return execute("GET", endpoint, null);
    }

    public static JsonElement post(String endpoint, JsonObject body) throws Exception {
        return execute("POST", endpoint, body);
    }

    public static JsonElement put(String endpoint, JsonObject body) throws Exception {
        return execute("PUT", endpoint, body);
    }

    public static JsonElement delete(String endpoint) throws Exception {
        return execute("DELETE", endpoint, null);
    }

    // Reemplaza tu método execute en ApiClient.java con este:
    private static JsonElement execute(String method, String endpoint, JsonObject body) throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            String token = SessionManager.getInstance().getToken();
            if (token != null && !token.isBlank()) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }

            if (body != null) {
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }
            }

            return readResponse(con);

        } catch (SocketTimeoutException e) {
            throw new RuntimeException("Tiempo de espera agotado con el servidor.");
        } catch (ConnectException e) {
            throw new RuntimeException("No se puede conectar al servidor. ¿Está encendido el Backend?");
        } catch (IOException e) {
            throw new RuntimeException("Error de comunicación: " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
        // IMPORTANTE: No pongas "return null;" aquí afuera. 
        // Los catch ya lanzan excepciones, por lo que el compilador no lo pedirá.
    }

    private static JsonElement readResponse(HttpURLConnection con) throws Exception {

        int status = con.getResponseCode();

        InputStream is = (status >= 200 && status < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        if (is == null) {
            if (status >= 200 && status < 300)
                return JsonNull.INSTANCE;
            else
                throw new RuntimeException("HTTP " + status + " sin respuesta.");
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String response = sb.toString().trim();

            if (status < 200 || status >= 300) {
                throw new RuntimeException("HTTP " + status + ": " + response);
            }

            if (response.isEmpty()) {
                return JsonNull.INSTANCE;
            }

            return JsonParser.parseString(response);
        }
    }
}