package Controller;

import model.Account;

public class RankingMenu extends GraphicsMenu{

    private static final int FRAME = 100;

    public void initiateGraphics() {
        getView().addRanking(Account.getScoreBoard(), FRAME, FRAME, WINDOW_WIDTH - FRAME * 2, WINDOW_HEIGHT - 2 * FRAME);
    }
}
