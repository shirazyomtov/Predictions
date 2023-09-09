package world.twoDimensionalGrid;

import world.entity.instance.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoDimensionalGrid {
    private final Integer rows;
    private final Integer cols;
    private boolean[][] twoD_arr = null;

    private Random random = new Random();
    public TwoDimensionalGrid(Integer rows, Integer cols) {
        this.rows = rows;
        this.cols = cols;
        this.twoD_arr = new boolean[rows][cols];
    }

    public void setTwoD_arr(int x, int y, boolean isNotEmpty) {
      twoD_arr[x][y] = isNotEmpty;
    }

    public Location createNewLocation() {
        int row, col;
        do
        {
            row = random.nextInt(rows);
            col = random.nextInt(cols);

        }while( twoD_arr[row][col]);
        twoD_arr[row][col] = true;
        return new Location(row, col);
    }

    public Location getNewLocation(Location location) {
        Location newLocation;
        List<Location> optionalSides = new ArrayList<>();
        int row = location.getRow();
        int col = location.getCol();

        if (!twoD_arr[(row + 1) % rows][col]) {
            optionalSides.add(new Location((row + 1) % rows, col));
        }
        if (!twoD_arr[(row - 1 + rows) % rows][col]) {
            optionalSides.add(new Location((row - 1 + rows) % rows, col));
        }
        if (!twoD_arr[row][(col + 1) % cols]) {
            optionalSides.add(new Location(row, (col + 1) % cols));
        }
        if (!twoD_arr[row][(col - 1 + cols) % cols]) {
            optionalSides.add(new Location(row, (col - 1 + cols) % cols));
        }

        if (optionalSides.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(optionalSides.size());
            setTwoD_arr(row, col, false);
            newLocation = optionalSides.get(randomIndex);
            setTwoD_arr(newLocation.getRow(), newLocation.getCol(), true);
            return newLocation;
        }
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getCols() {
        return cols;
    }
}
