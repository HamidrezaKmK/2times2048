package Controller;

public class ChoiceMenu extends GraphicsMenu {
    public int[] choices = new int[]{4, 6, 8, 10};

    public void initiateChoiceMenuGraphicsView() {
        getView().clear();
        getView().setTableSizeChoiceButton(choices);
    }

}
