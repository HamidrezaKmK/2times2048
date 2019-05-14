package View;

import model.Grid;

public class CommandlineView implements View {

    public void showEveryThing(Grid grid) {
        for (int row = 0; row < grid.getNumberOfRows(); row++) {
            for (int column = 0; column < grid.getNumberOfColumns(); column++) {
                String output;
                if (grid.getBlock(row, column) == null) {
                    output = ".";
                } else {
                    output = Integer.toString(grid.getBlock(row, column).getValue());
                }
                System.out.format("%-5s", output);
            }
            System.out.println();
        }
        System.out.println("-----------------------");
    }

}
