package View;

import com.sun.javafx.scene.SceneEventDispatcher;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Account;
import model.Grid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GraphicsView implements View {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 1000;

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

    public Scene getScene() {
        return scene;
    }

    // In Game:
    public void setTableBackGround(int x, int y, int tableWidth, int tableHeight) {
        Rectangle rectangle = new Rectangle(x, y, tableWidth, tableHeight);
        rectangle.setFill(Color.rgb(38, 31, 93));
        root.getChildren().add(rectangle);
    }

    public void addEmptyCellRectangle(int x, int y, int width, int height) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(Color.rgb(221, 250, 255));
        root.getChildren().add(rectangle);
    }

    public void addCellWithValue(int x, int y, int width, int height, int index) {
        try {
            Image image = new Image(new FileInputStream(addresses[index]));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
            imageView.relocate((double)x, (double)y);
            root.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
    }

    public void removeEmptyCellRectangle(int x, int y, int width, int height) {
        for (int i = root.getChildren().size() - 1; i >= 0; i--) {
            Node node = root.getChildren().get(i);
            if (node instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) node;
                if (rectangle.getX() == x && rectangle.getY() == y && rectangle.getWidth() == width &&
                        rectangle.getHeight() == height && rectangle.getFill().equals(Color.rgb(221, 250, 255))) {
                    root.getChildren().remove(node);
                }
            }
        }
    }

    public void removeFillRectangleWithValue(int x, int y, int width, int height) {
        for (int i = root.getChildren().size() - 1; i >= 0; i--) {
            Node node = root.getChildren().get(i);
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                if (imageView.getX() == x && imageView.getY() == y && imageView.getFitWidth() == width
                    && imageView.getFitHeight() == height) {
                    root.getChildren().remove(node);
                }
            }
        }
    }

    public void removeCells(int x, int y, int width, int height) {
        removeEmptyCellRectangle(x, y, width, height);
        removeFillRectangleWithValue(x, y, width, height);
    }

    public void popUpBlockWithValue(int x, int y, int width, int height, int index) {
        AnimationTimer animationTimer = new AnimationTimer() {
            long firstTime = 0;
            boolean hasStarted = false;
            Rectangle popUpRectangle = null;
            long totalAnimationTime = 1000 * 1000 * 100;
            long delayTime = 1000 * 1000 * 50;
            @Override
            public void handle(long now) {
                if (!hasStarted) {
                    hasStarted = true;
                    firstTime = now;
                }

                long passedTime = now - firstTime;
                if (passedTime > totalAnimationTime) {
                    root.getChildren().remove(popUpRectangle);
                    addCellWithValue(x, y, width, height, index);
                    stop();
                }

                if (passedTime >= delayTime && popUpRectangle == null) {
                    popUpRectangle = new Rectangle(x, y, width, height);
                    popUpRectangle.setFill(Color.RED);
                    root.getChildren().add(popUpRectangle);
                }
            }
        };
        animationTimer.start();
    }


    public void moveRectangleWithValue(int x1, int y1, int index1, int x2, int y2, int index2, int width, int height) {
        try {
            removeCells(x1, y1, width, height);
            addEmptyCellRectangle(x1, y1, width, height);
            Image image = new Image(new FileInputStream(addresses[index1]));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.relocate(x1, y1);

            AnimationTimer animationTimer = new AnimationTimer() {
                long animationTime = 1000 * 1000 * 100;
                boolean hasStarted = false;
                long firstTime;

                @Override
                public void handle(long now) {
                    if (!hasStarted) {
                        hasStarted = true;
                        firstTime = now;
                        root.getChildren().add(imageView);
                    }
                    long passedTime = now - firstTime;
                    if (passedTime > animationTime) {
                        root.getChildren().remove(imageView);
                        removeCells(x2, y2, width, height);
                        addCellWithValue(x2, y2, width, height, index2);
                        stop();
                    }

                    imageView.relocate(x1 + (x2 - x1) * (double) passedTime / animationTime,
                            y1 + (y2 - y1) * (double) passedTime / animationTime);

                }
            };
            animationTimer.start();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
    }

    Label inGamePointLabel;
    public void setPoint(int x, int y, String message) {
        if (inGamePointLabel == null) {
            inGamePointLabel = new Label();
            root.getChildren().add(inGamePointLabel);
        }
        inGamePointLabel.setText(message);
        inGamePointLabel.setFont(Font.font(40));
        inGamePointLabel.relocate(x, y);
    }
    //
    public void setPlayButton() {
        Button button = new Button("Play");
        button.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5);
        root.getChildren().add(button);
    }

    public void setQuitButton() {
        Button button = new Button("Quit");
        button.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 2);
        root.getChildren().add(button);
    }

    public Button getButtonWithText(String text) {
        for (Node node : root.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().equals(text)) {
                return (Button) node;
            }
        }
        return null;
    }

    public void clear() {
        root.getChildren().clear();
    }

    public void setTableSizeChoiceButton(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            Button choiceButton = new Button(nums[i] + "*" + nums[i]);
            choiceButton.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / (nums.length + 1) * (i + 1));
            root.getChildren().add(choiceButton);
        }
    }

    public int getNumberOfComponents() {
        return root.getChildren().size();
    }

    public void setCreateAccountButton() {
        Button createAccount = new Button("Create Account");
        createAccount.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 3);
        root.getChildren().add(createAccount);
    }


    Button loginButton = null;
    Button logoutButton = null;
    Button changeUserNameButton = null;

    public void setLoginButton() {
        System.err.println("HI I'm gonna get set!");
        if (loginButton == null) {
            System.err.println("WHSDA");
            loginButton = new Button("Login");
            loginButton.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 4);
            root.getChildren().add(loginButton);
        }
    }

    public void deleteLoginButton() {
        if (loginButton != null) {
            root.getChildren().remove(loginButton);
            loginButton = null;
        }
    }


    public void setLogoutButton(int x, int y) {
        if (logoutButton == null) {
            logoutButton = new Button("Logout");
            logoutButton.relocate(x, y);
            root.getChildren().add(logoutButton);
        }
    }

    public void setChangeAccountNameButton(int x, int y) {
        if (changeUserNameButton == null) {
            changeUserNameButton = new Button("Change username");
            changeUserNameButton.relocate(x, y);
            root.getChildren().add(changeUserNameButton);
        }
    }

    public void deleteChangeAccountNameButton() {
        if (changeUserNameButton != null) {
            root.getChildren().remove(changeUserNameButton);
            changeUserNameButton = null;
        }
    }

    public void deleteLogoutButton() {
        if (logoutButton != null) {
            root.getChildren().remove(logoutButton);
            logoutButton = null;
        }
    }

    TextField createAccountUserNameTextField, createAccountPasswordTextField;

    public void deleteCreateAccountTextFields() {
        root.getChildren().remove(createAccountUserNameTextField);
        root.getChildren().remove(createAccountPasswordTextField);
    }

    public String getCreateAccountUserName() {
        return createAccountUserNameTextField.getText();
    }

    public String getCreateAccountPassword() {
        return createAccountPasswordTextField.getText();
    }

    public void setCreateAccountTextFields() {
        createAccountUserNameTextField = new TextField("Enter username");
        createAccountUserNameTextField.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 3 + (double) WINDOW_HEIGHT / 5 / 3);
        root.getChildren().add(createAccountUserNameTextField);

        createAccountPasswordTextField = new TextField("Enter password");
        createAccountPasswordTextField.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 3 + (double) WINDOW_HEIGHT / 5 / 3 * 2);
        root.getChildren().add(createAccountPasswordTextField);

    }

    TextField loginUserNameTextField, loginPasswordTextField;

    public void deleteLoginTextFields() {
        root.getChildren().remove(loginUserNameTextField);
        root.getChildren().remove(loginPasswordTextField);
    }

    public String getLoginUserName() {
        return loginUserNameTextField.getText();
    }

    public String getLoginPassword() {
        return loginPasswordTextField.getText();
    }

    public void setLoginTextFields() {
        loginUserNameTextField = new TextField("Enter username");
        loginUserNameTextField.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 4 + (double) WINDOW_HEIGHT / 5 / 3);
        root.getChildren().add(loginUserNameTextField);

        loginPasswordTextField = new TextField("Enter password");
        loginPasswordTextField.relocate((double) WINDOW_WIDTH / 2, (double) WINDOW_HEIGHT / 5 * 4 + (double) WINDOW_HEIGHT / 5 / 3 * 2);
        root.getChildren().add(loginPasswordTextField);

    }

    public void addRanking(ArrayList<Account> scoreBoard, int x, int y, int width, int height) {
        int numberOfAccounts = Math.min(scoreBoard.size(), 5);
        for (int i = 0; i < numberOfAccounts; i++) {
            Label label = new Label((i + 1) + ". " + scoreBoard.get(i).getUsername() + " : " + scoreBoard.get(i).getHighScore());
            label.relocate(x + (double) width / 2, y + (double) height / numberOfAccounts * i);
            root.getChildren().add(label);
        }
    }


    Label gameOverLabel = null;
    public void showGameOver(int x, int y) {
        if (gameOverLabel == null) {
            gameOverLabel = new Label("GAME OVER! :(\nPress ESC to return");
            gameOverLabel.relocate(x, y);
            gameOverLabel.setFont(Font.font(50));
            gameOverLabel.setTextFill(Color.RED);
            root.getChildren().add(gameOverLabel);
        }
    }

    TextField changeUserNameTextField = null;

    public void deleteChangeUserNameTextField() {
        if (changeUserNameTextField != null) {
            root.getChildren().remove(changeUserNameTextField);
            changeUserNameTextField = null;
        }
    }

    public void setChangeUserNameTextField(int x, int y) {
        if (changeUserNameTextField == null) {
            changeUserNameTextField = new TextField("New username");
            changeUserNameTextField.relocate(x, y);
            root.getChildren().add(changeUserNameTextField);
        }
    }

    public String getChangeUserNameTextField() {
        if (changeUserNameTextField != null) {
            return changeUserNameTextField.getText();
        }
        return null;
    }
}
