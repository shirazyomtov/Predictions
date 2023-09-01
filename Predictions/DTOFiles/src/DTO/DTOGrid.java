package DTO;

public class DTOGrid {
    private String rows;
    private String cols;


    public DTOGrid(String rows, String cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public String getRows() {
        return rows;
    }

    public String getCols() {
        return cols;
    }
}
