package com.hospital.swing.api;

import com.google.gson.*;
import com.hospital.swing.session.SessionManager;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:5221/api";
    private static final int TIMEOUT = 10000;
    
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    public static Gson getGson() {
        return gson;
    }

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

    private static JsonElement execute(String method, String endpoint, JsonObject body) throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(BASE_URL + endpoint);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);

            String token = SessionManager.getInstance().getToken();
            if (token != null && !token.trim().isEmpty()) {
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
            throw new RuntimeException("El servidor tarda demasiado en responder.");
        } catch (ConnectException e) {
            throw new RuntimeException("No se pudo conectar al Backend. ¿Está encendido?");
        } catch (IOException e) {
            throw new RuntimeException("Error de red: " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
    }

    private static JsonElement readResponse(HttpURLConnection con) throws Exception {
        int status = con.getResponseCode();

        InputStream is = (status >= 200 && status < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        String responseBody = "";
        if (is != null) {
            try (Scanner s = new Scanner(is, StandardCharsets.UTF_8)) {
                s.useDelimiter("\\A");
                responseBody = s.hasNext() ? s.next() : "";
            }
        }

        if (status == 204 || responseBody.isEmpty()) {
            return JsonNull.INSTANCE;
        }

        if (status < 200 || status >= 300) {
            throw new RuntimeException("Error " + status + ": " + responseBody);
        }

        return JsonParser.parseString(responseBody);
    }
}