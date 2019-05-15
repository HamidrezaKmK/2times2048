import View.CommandlineView;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Account;
import Controller.*;


public class FX extends Application {

    private enum Menu {
        IN_GAME,
        MAIN_MENU,
        CHOICE_MENU,
        RANKING_MENU
    }

    private Menu currentMenu = Menu.MAIN_MENU;

    InGameMenu inGameMenuController;
    MainMenu mainMenuController;
    ChoiceMenu choiceMenuController;
    RankingMenu rankingMenuController;

    Stage currentStage;

    public void rankingMenuActions() {
        rankingMenuController.getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                currentMenu = Menu.MAIN_MENU;
                mainLoop();
            }
        });
    }

    public void inGameMenuActions() {
        inGameMenuController.showPoint();
        inGameMenuController.addAtRandom();
        inGameMenuController.getView().getScene().setOnKeyPressed(event -> {
            if (inGameMenuController.isGameOver()) {
                inGameMenuController.showGameOver();
                if (event.getCode() == KeyCode.ESCAPE) {
                    mainMenuController.saveAccount(inGameMenuController.getPlayer());
                    currentMenu = Menu.MAIN_MENU;
                }
                mainLoop();
                return;
            }
            switch (event.getCode()) {
                case UP:
                    inGameMenuController.moveUp();
                    break;
                case DOWN:
                    inGameMenuController.moveDown();
                    break;
                case LEFT:
                    inGameMenuController.moveLeft();
                    break;
                case RIGHT:
                    inGameMenuController.moveRight();
                    break;
                case ESCAPE:
                    mainMenuController.saveAccount(inGameMenuController.getPlayer());
                    currentMenu = Menu.MAIN_MENU;
                    break;
                default:
                    break;
            }
            mainLoop();
        });
        CommandlineView commandlineView = new CommandlineView();
        commandlineView.showEveryThing(inGameMenuController.getGrid());
    }

    private void mainMenuActions() {
        mainMenuController.getView().getButtonWithText("Play").setOnMouseClicked(event -> {
            if (mainMenuController.getAccount() == null) {
                System.err.println("Please login first!");
            } else {
                currentMenu = Menu.CHOICE_MENU;
                choiceMenuController.initiateChoiceMenuGraphicsView();
            }
            mainLoop();
        });
        mainMenuController.getView().getButtonWithText("Quit").setOnMouseClicked(event -> {
            mainMenuController.quit();
            currentStage.close();
            mainLoop();
        });
        mainMenuController.getView().getButtonWithText("Create Account").setOnMouseClicked(event -> {
            mainMenuController.createAccount();
            mainLoop();
        });
        Button login = mainMenuController.getView().getButtonWithText("Login");
        if (login != null) {
            login.setOnMouseClicked(event -> {
                mainMenuController.login();
                mainLoop();
            });
        }
        Button logout = mainMenuController.getView().getButtonWithText("Logout");
        if (logout != null) {
            logout.setOnMouseClicked(event -> {
                mainMenuController.logout();
                mainLoop();
            });
        }
        Button changeUsername = mainMenuController.getView().getButtonWithText("Change username");
        if (changeUsername != null) {
            changeUsername.setOnMouseClicked(event -> {
                mainMenuController.changeAccount();
                mainLoop();
            });
        }
        mainMenuController.getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                currentMenu = Menu.RANKING_MENU;
                rankingMenuController = new RankingMenu();
                rankingMenuController.setGraphicsView();
                rankingMenuController.initiateGraphics();
                mainLoop();
            }
        });
    }

    private void choiceMenuActions() {

        for (int i = 0; i < choiceMenuController.choices.length; i++) {
            int x = choiceMenuController.choices[i];
            choiceMenuController.getView().getButtonWithText(x + "*" + x).setOnMouseClicked(event -> {
                inGameMenuController = new InGameMenu();
                inGameMenuController.setGraphicsView();
                inGameMenuController.initiateInGameGraphicsView(x, x);
                inGameMenuController.setPlayer(mainMenuController.getAccount());
                currentMenu = Menu.IN_GAME;
                mainLoop();
            });
        }
        choiceMenuController.getView().getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                currentMenu = Menu.MAIN_MENU;
                mainLoop();
            }
        });
    }

    public void mainLoop() {

        switch (currentMenu) {
            case MAIN_MENU:
                mainMenuActions();
                currentStage.setScene(mainMenuController.getView().getScene());
                break;
            case IN_GAME:
                inGameMenuActions();
                currentStage.setScene(inGameMenuController.getView().getScene());
                break;
            case CHOICE_MENU:
                choiceMenuActions();
                currentStage.setScene(choiceMenuController.getView().getScene());
                break;
            case RANKING_MENU:
                rankingMenuActions();
                currentStage.setScene(rankingMenuController.getView().getScene());
                break;
            default:
                break;
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;

        inGameMenuController = new InGameMenu();
        inGameMenuController.setGraphicsView();

        mainMenuController = new MainMenu();
        mainMenuController.setGraphicsView();
        mainMenuController.initiateMainMenuGraphicsView();

        choiceMenuController = new ChoiceMenu();
        choiceMenuController.setGraphicsView();

        currentMenu = Menu.MAIN_MENU;

        mainLoop();

        currentStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
