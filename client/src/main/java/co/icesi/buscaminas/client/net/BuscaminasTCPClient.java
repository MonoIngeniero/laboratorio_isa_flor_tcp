package co.icesi.buscaminas.client.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.icesi.buscaminas.client.dtos.Request;
import co.icesi.buscaminas.client.dtos.Response;
import co.icesi.buscaminas.client.model.Cell;

public class BuscaminasTCPClient {

    private final Gson gson;

    public BuscaminasTCPClient() {
        this.gson = new GsonBuilder().create();
    }

    public Response sendRequest(String host, int port, Request request) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String jsonOut = gson.toJson(request);
            writer.write(jsonOut);
            writer.newLine();
            writer.flush();

            String jsonIn = reader.readLine();
            if (jsonIn == null) {
                throw new IOException("El servidor cerro la conexion sin enviar respuesta.");
            }
            return gson.fromJson(jsonIn, Response.class);
        }
    }

    public Cell[][] extractBoard(Response response) {
        if (response == null || response.data == null || response.data.get("board") == null) {
            return null;
        }
        Object rawBoard = response.data.get("board");
        String json = gson.toJson(rawBoard);
        return gson.fromJson(json, Cell[][].class);
    }

    public Boolean extractBoolean(Response response, String key) {
        if (response == null || response.data == null || response.data.get(key) == null) {
            return null;
        }
        Object raw = response.data.get(key);
        if (raw instanceof Boolean) {
            return (Boolean) raw;
        }
        return Boolean.parseBoolean(raw.toString());
    }

    public String extractMessage(Response response) {
        if (response == null || response.data == null) {
            return null;
        }
        Object raw = response.data.get("message");
        return raw == null ? null : raw.toString();
    }
}