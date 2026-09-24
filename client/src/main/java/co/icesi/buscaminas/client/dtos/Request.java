package co.icesi.buscaminas.client.dtos;

import java.util.Map;

public class Request {
    public String action;
    public Map<String, String> data;

    public Request() {
    }

    public Request(String action, Map<String, String> data) {
        this.action = action;
        this.data = data;
    }
}