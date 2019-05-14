package Controller;

import View.CommandlineView;
import View.GraphicsView;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Block;
import model.Grid;

import java.util.Random;

public class Controller {
    // implement moveLeft-Right- ...
    // insert at random

    private Grid grid = new Grid(4, 4);
    private CommandlineView commandlineView = new CommandlineView();
    private GraphicsView graphicsView;

    private int[] directionsR = new int[]{-1, 0, 1, 0};
    private int[] directionsC = new int[]{0, 1, 0, -1};

    public void initiateGraphicsView() {
        getGraphicsView().setTable(grid.getNumberOfRows(), grid.getNumberOfColumns());
    }

    public Grid getGrid() {
        return grid;
    }

    public int getFreeBlocks() {
        int returnValue = 0;
        for (int i = 0; i < grid.getNumberOfRows(); i++)
            for (int j = 0; j < grid.getNumberOfColumns(); j++)
                if (grid.getBlock(i, j) == null)
                    returnValue++;
        return returnValue;
    }

    private boolean checkValid(int r, int c) {
        return 0 <= r && r < grid.getNumberOfRows() && 0 <= c && c < grid.getNumberOfColumns();
    }

    public boolean checkCanMove(int direction) {
        if (getFreeBlocks() > 0) {
            return true;
        }
        direction = (direction + 2) % 4;
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int column = 0; column < grid.getNumberOfColumns(); column++) if (grid.getBlock(row, column) != null){
                int r = row + directionsR[direction];
                int c = column + directionsC[direction];
                if (checkValid(r, c) && grid.getBlock(r, c).getValue() == grid.getBlock(row, column).getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkIsFull(int row, int column) {
        return grid.getBlock(row, column) != null;
    }


    public void addAtRandom() {
        if (getFreeBlocks() == 0) {
            return;
        }
        Random random = new Random();
        int repeat = Math.min(1, random.nextInt(getFreeBlocks()));
        while (repeat >= 0) {
            int r = random.nextInt(grid.getNumberOfRows());
            int c = random.nextInt(grid.getNumberOfColumns());
            if (!checkIsFull(r, c)) {
                repeat--;
                insertBlockWithValue(r, c, (random.nextInt(2) + 1) * 2);
                graphicsView.setComponentWithAnimation(r, c, graphicsView.makeBlockWithValue(r, c, (random.nextInt(2) + 1) * 2));
            }
        }
    }

    public void insertBlockWithValue(int row, int column, int x) {
        grid.setBlockWithValue(row, column, x);
    }

    public void deleteBlock(int row, int column) {
        grid.eraseBlock(row, column);
    }

    private void moveGrid(int direction) {
        if (!checkCanMove(direction)) {
            return;
        }
        int[] currentRows;
        int[] currentColumns;
        int size = 0;
        switch (direction) {
            case 0:
                size = grid.getNumberOfColumns();
                currentRows = new int[size];
                currentColumns = new int[size];
                for (int i = 0; i < size; i++) {
                    currentRows[i] = 0;
                    currentColumns[i] = i;
                }
                break;
            case 1:
                size = grid.getNumberOfRows();
                currentRows = new int[size];
                currentColumns = new int[size];
                for (int i = 0; i < size; i++) {
                    currentRows[i] = i;
                    currentColumns[i] = grid.getNumberOfColumns() - 1;
                }
                break;
            case 2:
                size = grid.getNumberOfColumns();
                currentRows = new int[size];
                currentColumns = new int[size];
                for (int i = 0; i < size; i++) {
                    currentRows[i] = grid.getNumberOfRows() - 1;
                    currentColumns[i] = i;
                }
                break;
            case 3:
                size = grid.getNumberOfRows();
                currentRows = new int[size];
                currentColumns = new int[size];
                for (int i = 0; i < size; i++) {
                    currentRows[i] = i;
                    currentColumns[i] = 0;
                }
                break;
            default:
                return;
        }

        direction = (direction + 2) % 4;
        for (int index = 0; index < size; index++) {
            int r = currentRows[index];
            int c = currentColumns[index];

            int lastR = -1;
            int lastC = -1;
            int addR = r;
            int addC = c;
            while (0 <= r && r < grid.getNumberOfRows() && 0 <= c && c < grid.getNumberOfColumns()) {
                if (grid.getBlock(r, c) != null) {
                    if (lastR == -1 && lastC == -1) {
                        lastR = r;
                        lastC = c;
                    } else { // add the two blocks
                        int sumValue = 0;
                        if (grid.getBlock(r, c).getValue() != grid.getBlock(lastR, lastC).getValue()) {
                            sumValue = grid.getBlock(lastR, lastC).getValue();
                            graphicsView.moveBlock(lastR, lastC, sumValue, addR, addC, sumValue);
                            deleteBlock(lastR, lastC);
                            lastR = r;
                            lastC = c;

                        } else {
                            sumValue = grid.getBlock(lastR, lastC).getValue() + grid.getBlock(r, c).getValue();
                            graphicsView.moveBlock(lastR, lastC, grid.getBlock(lastR, lastC).getValue(),
                                                    addR, addC, sumValue);
                            graphicsView.moveBlock(r, c, grid.getBlock(r, c).getValue(),
                                                    addR, addC, sumValue);
                            deleteBlock(lastR, lastC);
                            deleteBlock(r, c);
                            lastR = -1;
                            lastC = -1;
                        }

                        insertBlockWithValue(addR, addC, sumValue);
                        addR += directionsR[direction];
                        addC += directionsC[direction];
                    }
                }

                r += directionsR[direction];
                c += directionsC[direction];
            }

            if (lastR != -1 && lastC != -1) {
                int t = grid.getBlock(lastR, lastC).getValue();
                graphicsView.moveBlock(lastR, lastC, t, addR, addC, t);
                deleteBlock(lastR, lastC);
                insertBlockWithValue(addR, addC, t);
            }
        }
    }

    public CommandlineView getCommandlineView() {
        return commandlineView;
    }

    public GraphicsView getGraphicsView() {
        return graphicsView;
    }

    public void moveUp() {
        moveGrid(0);
    }

    public void moveRight() {
        moveGrid(1);
    }

    public void moveDown() {
        moveGrid(2);
    }

    public void moveLeft() {
        moveGrid(3);
    }

    public void setGraphicsView() {
        graphicsView = new GraphicsView();
    }
}
