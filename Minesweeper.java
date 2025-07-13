
import java.awt.*;
import java.util.EnumMap;
import javax.swing.*;

public class Minesweeper extends JFrame {

    // -- difficulty settings --
    // Cell is the amount of cells in one row/column
    private static final int CELLEASY = 9;
    private static final int MINEEASY = 10;
    private static final int CELLMEDIUM = 21;
    private static final int MINEMEDIUM = 54;
    private static final int CELLHARD = 33;
    private static final int MINEHARD = 134;
    // -- difficulty settings --

    public static final int CELLAMOUNT = 0;
    public static final int MINES = 1;
    public static final int WINDOWWIDTH = 2;
    public static final int WINDOWHEIGHT = 3;

    public static final Integer[] EASY = new Integer[]{CELLEASY, MINEEASY, CELLEASY * 30, CELLEASY * 30 + 27};
    public static final Integer[] MEDIUM = new Integer[]{CELLMEDIUM, MINEMEDIUM, CELLMEDIUM * 30, CELLMEDIUM * 30 + 27};
    public static final Integer[] HARD = new Integer[]{CELLHARD, MINEHARD, CELLHARD * 30, CELLHARD * 30 + 27};

    public static Cell[][] cells;

    private static EnumMap<DifficultyState, Integer[]> difficultyPresets;
    private static DifficultyState difficultyState = DifficultyState.easy;

    public Minesweeper() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new NavBar(this));

        difficultyPresets = new EnumMap<>(DifficultyState.class);
        difficultyPresets.put(DifficultyState.easy, EASY);
        difficultyPresets.put(DifficultyState.medium, MEDIUM);
        difficultyPresets.put(DifficultyState.hard, HARD);

        newGame(EASY[CELLAMOUNT], EASY[MINES], EASY[WINDOWWIDTH], EASY[WINDOWHEIGHT]);

        setVisible(true);
    }

    public static void gameOverLoss() {
        for (int r = 0; r < difficultyPresets.get(difficultyState)[CELLAMOUNT]; r++) {
            for (int c = 0; c < difficultyPresets.get(difficultyState)[CELLAMOUNT]; c++) {
                cells[r][c].reveal();
            }
        }
        Cell.setTotalFreeFields(-1); // so it doesnt show the win window because everything got revealed
        JOptionPane.showMessageDialog(null, "Game Over!");
    }

    public static void gameOverWin() {
        for (int r = 0; r < difficultyPresets.get(difficultyState)[CELLAMOUNT]; r++) {
            for (int c = 0; c < difficultyPresets.get(difficultyState)[CELLAMOUNT]; c++) {
                cells[r][c].setEnabled(false);
                cells[r][c].disableRightClick();
            }
        }
        JOptionPane.showMessageDialog(null, "You Win!");
    }

    public final void newGame(int size, int mines, int width, int height) {
        getContentPane().removeAll();
        setLayout(new GridLayout(size, size));
        setSize(width, height);
        setLocationRelativeTo(null);
        cells = new Cell[size][size];
        Cell.setMinesPlaced(false);
        Cell.setTotalMines(mines);
        // init cells
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Cell cell = new Cell(row, column);
                cells[row][column] = cell;
                add(cell);
            }
        }

        Cell.calculateTotalFreeFields(size);

        // dont know if they help but it wont hurt
        revalidate();
        repaint();
    }

    public static EnumMap<DifficultyState, Integer[]> getDifficultyPresets() {
        return difficultyPresets;
    }

    public static DifficultyState getDifficultyState() {
        return difficultyState;
    }

    public static void setDifficultyState(DifficultyState difficultyState) {
        Minesweeper.difficultyState = difficultyState;
    }
}
