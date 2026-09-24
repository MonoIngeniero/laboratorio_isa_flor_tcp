package co.icesi.buscaminas.client.model;

public class Cell {

    private boolean isLandMine;
    private int value;
    private boolean hide;
    private boolean showAll;
    private boolean isMarked;

    public boolean isLandMine() {
        return isLandMine;
    }

    public int getValue() {
        return value;
    }

    public boolean isHide() {
        return hide;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public boolean isMarked() {
        return isMarked;
    }
}