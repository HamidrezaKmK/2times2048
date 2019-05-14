package View;

import com.sun.javafx.scene.SceneEventDispatcher;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Grid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GraphicsView implements View {

    private static final int WINDOW_HEIGHT = 1000;
    private static final int WINDOW_WIDTH = 800;
    private static final int FRAME = 50;
    private static final int FRAME_BETWEEN_CELLS = 5;
    private static final int TABLE_SIZE = Math.min(WINDOW_HEIGHT, WINDOW_WIDTH) - 2 * FRAME;
    private int numberOfColumns;
    private int numberOfRows;

    Node[][] components;

    String[] addresses = new String[]{
            "./photos/2.png",
            "./photos/4.png",
            "./photos/8.png",
            "./photos/16.png",
            "./photos/32.png",
            "./photos/64.png",
            "./photos/128.png",
            "./photos/256.png",
            "./photos/512.png",
            "./photos/1024.png",
            "./photos/2048.png"
    };

    private Group root;
    private Scene scene;



    public GraphicsView() {
        root = new Group();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void setTable(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        components = new Node[numberOfRows][numberOfColumns];

        Rectangle rectangle = new Rectangle(FRAME - FRAME_BETWEEN_CELLS, FRAME - FRAME_BETWEEN_CELLS,
                TABLE_SIZE + 2 * FRAME_BETWEEN_CELLS, TABLE_SIZE + 2 * FRAME_BETWEEN_CELLS);
        rectangle.setFill(Color.rgb(38, 31, 93));

        root.getChildren().add(rectangle);
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                components[row][column] = makeEmptyRectangle(row, column);
                root.getChildren().add(components[row][column]);
            }
        }
    }

    private int getBlockWidth() {
        return TABLE_SIZE / numberOfColumns;
    }

    private int getBlockHeight() {
        return TABLE_SIZE / numberOfRows;
    }

    private Node getComponent(int row, int column) {
        return components[row][column];
    }

    public void setComponent(int row, int column, Node node) {
        root.getChildren().remove(components[row][column]);
        components[row][column] = node;
        root.getChildren().add(components[row][column]);
    }

    public Node makeEmptyRectangle(int row, int column) {
        Rectangle rectangle1 = new Rectangle(FRAME + column * getBlockWidth() + FRAME_BETWEEN_CELLS, FRAME + row * getBlockHeight() + FRAME_BETWEEN_CELLS,
                getBlockWidth() - 2 * FRAME_BETWEEN_CELLS, getBlockHeight() - 2 * FRAME_BETWEEN_CELLS);
        rectangle1.setFill(Color.rgb(221, 250, 255));
        return rectangle1;
    }

    public Node makeBlockWithValue(int row, int column, int t) {
        int cnt = -1;
        while (t > 1) {
            t /= 2;
            cnt++;
        }

        try {
            Image image = new Image(new FileInputStream(addresses[Math.min(addresses.length - 1, cnt)]));
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(getBlockWidth() - 2 * FRAME_BETWEEN_CELLS);
            imageView.setFitHeight(getBlockHeight() - 2 * FRAME_BETWEEN_CELLS);
            imageView.relocate(column * getBlockWidth() + FRAME_BETWEEN_CELLS + FRAME, row * getBlockHeight() + FRAME_BETWEEN_CELLS + FRAME);
            return imageView;
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND");
        }
        return null;
    }

    public Scene getScene() {
        return scene;
    }

    public void moveBlock(int initialRow, int initialColumn, int initialValue, int destinationRow, int destinationColumn, int destinationValue) {
        Node component = makeBlockWithValue(initialRow, initialColumn, initialValue);

        AnimationTimer animationTimer = new AnimationTimer() {
            long totalAnimationTime = 1000 * 1000 * 100;
            long totalTimeLeft = totalAnimationTime;
            long lastTime = 0;

            int distanceX = (destinationColumn - initialColumn) * getBlockWidth();
            int distanceY = (destinationRow - initialRow) * getBlockHeight();
            @Override
            public void handle(long now) {

                if (totalTimeLeft <= 0) {
                    setComponent(destinationRow, destinationColumn, makeBlockWithValue(destinationRow, destinationColumn, destinationValue));
                    root.getChildren().remove(component);
                    stop();
                }
                if (lastTime == 0) {
                    setComponent(initialRow, initialColumn, makeEmptyRectangle(initialRow, initialColumn));
                    root.getChildren().add(component);
                    lastTime = now;
                }

                double delta = now - lastTime;
                totalTimeLeft -= delta;
                lastTime = now;

                component.relocate(initialColumn * getBlockWidth() + FRAME + FRAME_BETWEEN_CELLS + distanceX * ((double)(totalAnimationTime - totalTimeLeft) / totalAnimationTime),
                        initialRow * getBlockHeight() + FRAME + FRAME_BETWEEN_CELLS + distanceY * ((double) (totalAnimationTime - totalTimeLeft) / totalAnimationTime ));
            }
        };
        animationTimer.start();
    }

    public void setComponentWithAnimation(int r, int c, Node makeBlockWithValue) {
        AnimationTimer animationTimer = new AnimationTimer() {
            long animationTime = 1000 * 1000 * 400;
            long firstTime = 0;
            Rectangle rectangle;
            boolean flag = false;

            @Override
            public void handle(long now) {
                if (firstTime == 0) {
                    firstTime = now;
                }
                long elpassed = now - firstTime;
                if (300 * 1000 * 1000 <= elpassed && elpassed <= animationTime) {
                    if (!flag) {
                        rectangle = (Rectangle) makeEmptyRectangle(r, c);
                        rectangle.setFill(Color.rgb(151, 27, 33));
                        root.getChildren().add(rectangle);
                    }
                    flag = true;
                } else if (elpassed > animationTime) {
                    root.getChildren().remove(rectangle);
                    setComponent(r, c, makeBlockWithValue);
                    stop();
                }
            }
        };
        animationTimer.start();
    }
}
