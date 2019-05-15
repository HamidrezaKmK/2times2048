package Controller;

import model.Account;
import model.Grid;

import java.awt.*;
import java.util.Random;

public class InGameMenu extends GraphicsMenu {

    private static final int WINDOW_HEIGHT = 1000;
    private static final int WINDOW_WIDTH = 800;
    private static final int FRAME = 50;
    private static final int FRAME_BETWEEN_CELLS = 5;
    private static final int TABLE_SIZE = Math.min(WINDOW_HEIGHT, WINDOW_WIDTH) - 2 * FRAME;

    private static int blockWidth, blockHeight;

    private Grid grid = new Grid(4, 4);
    private Account player;
    private int currentScore;
    private int[] directionsR = new int[]{-1, 0, 1, 0};
    private int[] directionsC = new int[]{0, 1, 0, -1};

    public void initiateInGameGraphicsView(int rows, int columns) {
        getView().clear();
        grid = new Grid(rows, columns);
        blockWidth = TABLE_SIZE / columns;
        blockHeight = TABLE_SIZE / rows;
        getView().setTableBackGround(FRAME - FRAME_BETWEEN_CELLS, FRAME - FRAME_BETWEEN_CELLS,
                TABLE_SIZE + 2 * FRAME_BETWEEN_CELLS, TABLE_SIZE + 2 * FRAME_BETWEEN_CELLS);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                getView().addEmptyCellRectangle(FRAME + blockWidth * column + FRAME_BETWEEN_CELLS,
                        FRAME + blockHeight * row + FRAME_BETWEEN_CELLS,
                        blockWidth - FRAME_BETWEEN_CELLS * 2,
                        blockHeight - FRAME_BETWEEN_CELLS * 2);
            }
        }
    }


    public void setPlayer(Account account) {
        this.player = account;
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

    public boolean isGameOver() {
        for (int i = 0; i < 4; i++)
            if (checkCanMove(i))
                return false;
        return true;
    }

    public boolean checkCanMove(int direction) {
        if (getFreeBlocks() > 0) {
            return true;
        }
        direction = (direction + 2) % 4;
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int column = 0; column < grid.getNumberOfColumns(); column++)
                if (grid.getBlock(row, column) != null) {
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
                int t = random.nextInt(2);
                grid.setBlockWithValue(r, c, (t + 1) * 2);
                getView().removeCells(FRAME + c * blockWidth + FRAME_BETWEEN_CELLS, FRAME + r * blockHeight + FRAME_BETWEEN_CELLS,
                        blockWidth - 2 * FRAME_BETWEEN_CELLS, blockHeight - 2 * FRAME_BETWEEN_CELLS);
                getView().popUpBlockWithValue(FRAME + c * blockWidth + FRAME_BETWEEN_CELLS, FRAME + r * blockHeight + FRAME_BETWEEN_CELLS,
                        blockWidth - 2 * FRAME_BETWEEN_CELLS, blockHeight - 2 * FRAME_BETWEEN_CELLS,
                        t);
            }
        }
    }


    private void moveRectangleWithAnimation(int initialRow, int initialColumn, int initialValue, int destinationRow, int destinationColumn, int destinationValue) {
        System.err.println("FROM " + initialRow + " " + initialColumn + " WITH VALUE(" + initialValue + ") TO " + destinationRow + " " + destinationColumn + " WITH VALUE(" + destinationValue + ")");
        getView().moveRectangleWithValue(FRAME + FRAME_BETWEEN_CELLS + initialColumn * blockWidth,
                FRAME + FRAME_BETWEEN_CELLS + initialRow * blockHeight,
                initialValue,
                FRAME + FRAME_BETWEEN_CELLS + destinationColumn * blockWidth,
                FRAME + FRAME_BETWEEN_CELLS + destinationRow * blockHeight,
                destinationValue,
                blockWidth - 2 * FRAME_BETWEEN_CELLS,
                blockHeight - 2 * FRAME_BETWEEN_CELLS);
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
                        int sumValue;
                        if (grid.getBlock(r, c).getValue() != grid.getBlock(lastR, lastC).getValue()) {
                            sumValue = grid.getBlock(lastR, lastC).getValue();
                            moveRectangleWithAnimation(lastR, lastC, getLogarithm(sumValue) - 1, addR, addC, getLogarithm(sumValue) - 1);
                            grid.eraseBlock(lastR, lastC);
                            lastR = r;
                            lastC = c;

                        } else {
                            sumValue = grid.getBlock(lastR, lastC).getValue() + grid.getBlock(r, c).getValue();
                            moveRectangleWithAnimation(r, c, getLogarithm(grid.getBlock(r, c).getValue()) - 1, addR, addC, getLogarithm(sumValue) - 1);
                            moveRectangleWithAnimation(lastR, lastC, getLogarithm(grid.getBlock(lastR, lastC).getValue()) - 1, addR, addC, getLogarithm(sumValue) - 1);
                            currentScore += grid.getBlock(r, c).getValue() * 2;
                            if (player != null) {
                                player.setScore(currentScore);
                            }
                            grid.eraseBlock(lastR, lastC);
                            grid.eraseBlock(r, c);
                            lastR = -1;
                            lastC = -1;
                        }

                        grid.setBlockWithValue(addR, addC, sumValue);
                        addR += directionsR[direction];
                        addC += directionsC[direction];
                    }
                }

                r += directionsR[direction];
                c += directionsC[direction];
            }

            if (lastR != -1 && lastC != -1) {
                int t = grid.getBlock(lastR, lastC).getValue();
                moveRectangleWithAnimation(lastR, lastC, getLogarithm(t) - 1, addR, addC, getLogarithm(t) - 1);
                grid.eraseBlock(lastR, lastC);
                grid.setBlockWithValue(addR, addC, t);
            }
        }
    }

    public void showPoint() {
        getView().setPoint(FRAME, TABLE_SIZE + FRAME * 4, getPlayer().getUsername() + " : score : " + currentScore);
    }

    public int getLogarithm(int value) {
        int returnValue = 0;
        while (value > 1) {
            returnValue++;
            value /= 2;
        }
        return returnValue;
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

    public Account getPlayer() {
        return player;
    }


    public void showGameOver() {
        getView().showGameOver(FRAME, TABLE_SIZE + FRAME);
    }
}
