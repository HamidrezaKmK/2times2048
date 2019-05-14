package model;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
    private int numberOfRows;
    private int numberOfColumns;
    private Block[][] blocks;

    private ArrayList<Node> components = new ArrayList<>();

    private int frameBetweenCells = 4;
    // 700 is the grid
    private int upRowPixel = 50;
    private int downRowPixel = 750;
    private int leftColumnPixel = 50;
    private int rightColumnPixel = 750;

    public Grid() {
        this.numberOfRows = 4;
        this.numberOfColumns = 4;
        blocks = new Block[4][4];
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    // refresh all components on screen
  /*  public void refresh() {
        components.clear();
        Rectangle rectangle = new Rectangle(upRowPixel - frameBetweenCells, leftColumnPixel - frameBetweenCells,
                rightColumnPixel - leftColumnPixel + 2 * frameBetweenCells, downRowPixel - upRowPixel + 2 * frameBetweenCells);
        rectangle.setFill(Color.rgb(38, 31, 93));

        components.add(rectangle);
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                components.add(makeComponent(row, column));
            }
        }
    }
*/
    public Grid(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        blocks = new Block[numberOfRows][numberOfColumns];
        //refresh();
    }

    private int getBlocHeight() {
        return (downRowPixel - upRowPixel) / numberOfRows;
    }

    private int getBlockWidth() {
        return (rightColumnPixel - leftColumnPixel) / numberOfColumns;
    }


    public Block getBlock(int row, int column) {
        return blocks[row][column];
    }

    public void setBlockWithValue(int row, int column, int x) {
        blocks[row][column] = new Block(x);
    }

    public void eraseBlock(int row, int column) {
        blocks[row][column] = null;
    }
}
