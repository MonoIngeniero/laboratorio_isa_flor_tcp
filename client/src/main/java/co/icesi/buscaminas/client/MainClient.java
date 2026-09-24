package co.icesi.buscaminas.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import co.icesi.buscaminas.client.dtos.Request;
import co.icesi.buscaminas.client.dtos.Response;
import co.icesi.buscaminas.client.model.Cell;
import co.icesi.buscaminas.client.net.BuscaminasTCPClient;
import co.icesi.buscaminas.client.ui.BoardRenderer;

public class MainClient {

    private static final BuscaminasTCPClient client = new BuscaminasTCPClient();
    private static final BoardRenderer renderer = new BoardRenderer();
    private static final Scanner scanner = new Scanner(System.in);

    private static String host;
    private static int port;
    private static boolean gameEnded = false;

    public static void main(String[] args) {
        System.out.println("--------------------");
        System.out.println("BUSCAMINAS - CLIENTE TCP");
        System.out.println("--------------------");

        host = readHost(args);
        port = readPort(args);

        System.out.println("Conectando a " + host + ":" + port  + "...");

        boolean salir = false;
        while (!salir) {
            printMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    iniciarPartida();
                    break;
                case 2:
                    destaparCelda();
                    break;
                case 3:
                    marcarCelda();
                    break;
                case 4:
                    consultarTablero();
                    break;
                case 5:
                    rendirse();
                    break;
                case 6:
                    salir = true;
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
        scanner.close();
    }

    private static String readHost(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) {
            return args[0];
        }

        System.out.println("IP o host del servidor (Enter para localhost): ");
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? "localhost" : value;
    }

    private static int readPort(String[] args) {
        if (args.length > 1) {
            try {
                return Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                //sigue el flujo
            }
        }

        System.out.println("Puerto del servidor (Enter para 12345): ");
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            return 12345;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Puerto inválido, usando 12345");
            return 12345;
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("--------------------");
        System.out.println("1. Iniciar nueva partida (Fila, columna, minas)");
        System.out.println("2. Destapar celda (Fila, columna");
        System.out.println("3. Marcar / Desmarcar bandera (Fila, columna");
        System.out.println("5. Rendirse y revelar el tablero completo");
        System.out.println("6. Salir");
    }

    private static int leerEntero(String mensaje) {
        while(true) {
            System.out.println(mensaje);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido");
            }
        }
    }

    private static void mostrarTableroDe(Response response) {
        if (!"OK".equals(response.status)) {
            System.out.println("Error del servidor: " + client.extractMessage(response));
        }

        Cell[][] board = client.extractBoard(response);
        renderer.render(board);
    }

    private static Response enviar(Request request) {
        try {
            return client.sendRequest(host, port, request);
        } catch (Exception e) {
            System.out.println("No se pudo comunicar con el sevridor: " + e.getMessage());
            return null;
        }
    }

    private static void iniciarPartida() {
        int n = leerEntero("Número de filas: ");
        int m = leerEntero("Número de columnas: ");
        int minas = leerEntero("Número de minas: ");

        Map<String, String> data = new HashMap<>();
        data.put("n", String.valueOf(n));
        data.put("m", String.valueOf(m));
        data.put("minas", String.valueOf(minas));

        Response response = enviar(new Request("INIT GAME", data));
        if (response == null) {
            return;
        }
        gameEnded = false;
        mostrarTableroDe(response);

    }



}
