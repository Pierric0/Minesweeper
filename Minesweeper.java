
import java.awt.*;
import javax.swing.*;

public class Minesweeper extends JFrame {

    public static final int SIZE = 9;
    public static final int MINES = 10;
    public static Cell[][] cells = new Cell[SIZE][SIZE];

    public Minesweeper() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));
        setSize(500, 500);
        setLocationRelativeTo(null);

        // init cells
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Cell cell = new Cell(row, column);
                cells[row][column] = cell;
                add(cell);
            }
        }

        Cell.setTotalFreeFields(SIZE);

        setVisible(true);
    }

    public static void gameOverLoss() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c].reveal();
            }
        }
        Cell.totalFreeFields = -1; // so it doesnt show the win window because everything got revealed
        JOptionPane.showMessageDialog(null, "Game Over!");
    }

    public static void gameOverWin() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c].setEnabled(false);
                cells[r][c].disableRightClick();
            }
        }
        JOptionPane.showMessageDialog(null, "You Win!");
    }
}
