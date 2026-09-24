package co.icesi.buscaminas.client.ui;
import co.icesi.buscaminas.client.model.Cell;

public class BoardRenderer {

    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public void render(Cell[][] board) {
        if (board == null) {
            System.out.println("(No hay tablero para mostrar. Inicie una partida primero.)");
            return;
        }
        int filas = board.length;
        int columnas = board[0].length;

        System.out.print("     ");
        for (int j = 0; j < columnas; j++) {
            System.out.printf("%3d", j);
        }
        System.out.println();

        System.out.print("    ");
        for (int j = 0; j < columnas; j++) {
            System.out.print("---");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            System.out.printf("%3d |", i);
            for (int j = 0; j < columnas; j++) {
                System.out.print(renderCell(board[i][j]));
            }
            System.out.println();
        }
    }

    private String renderCell(Cell cell) {
        if (cell.isMarked()) {
            return "  " + YELLOW + "M" + RESET;
        }
        if (cell.isHide() && !cell.isShowAll()) {
            return "  .";
        }
        if (cell.isLandMine()) {
            return "  " + RED + "*" + RESET;
        }
        int v = cell.getValue();
        return v == 0 ? "   " : String.format("  %d", v);
    }
}