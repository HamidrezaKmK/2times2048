package Controller;

import View.GraphicsView;
import model.Account;

public abstract class GraphicsMenu {

    protected static int WINDOW_HEIGHT = 1000;
    protected static int WINDOW_WIDTH = 800;

    private GraphicsView view;

    public GraphicsView getView() {
        return view;
    }

    public void setGraphicsView() {
        view = new GraphicsView();
    }

}
