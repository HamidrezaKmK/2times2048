import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FX extends Application {

    public void listenForKeyPress(Scene scene, Controller controller) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    controller.moveUp();
                    break;
                case DOWN:
                    controller.moveDown();
                    break;
                case LEFT:
                    controller.moveLeft();
                    break;
                case RIGHT:
                    controller.moveRight();
                    break;
                default:
                    break;
            }
            controller.addAtRandom();
            controller.getCommandlineView().showEveryThing(controller.getGrid());
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        controller.setGraphicsView();
        Scene scene = controller.getGraphicsView().getScene();

        controller.initiateGraphicsView();

        listenForKeyPress(scene, controller);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
