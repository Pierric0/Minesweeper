
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

public class Cell extends JButton {

    private final MouseAdapter RIGHTCLICK;
    private static final Random RAND = new Random();

    private int row, column;
    private boolean isMine = false;
    private boolean revealed = false;
    private int neighborMines = 0;
    private CellStates cellState = CellStates.nothing;

    private static boolean minesPlaced = false;
    private static int totalMines;
    private static int totalFreeFields; // technically all freefields that have not been revealed yet

    public static void setMinesPlaced(boolean minesPlaced) {
        Cell.minesPlaced = minesPlaced;
    }

    public static void setTotalMines(int totalMines) {
        Cell.totalMines = totalMines;
    }

    public static void setTotalFreeFields(int totalFreeFields) {
        Cell.totalFreeFields = totalFreeFields;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.column = col;

        setFont(new Font("LastResort", Font.BOLD, 18));
        setMargin(new Insets(0, 0, 0, 0));
        addActionListener((_) -> {
            if (!minesPlaced) {
                placeMines(totalMines, this.row, this.column);
                countAllNeighbors();
            }
            if (cellState == CellStates.nothing) {
                if (isMine) {
                    reveal();
                    Minesweeper.gameOverLoss();
                } else {
                    reveal();
                    if (neighborMines == 0) {
                        revealAdjacent();
                    }
                }
                if (totalFreeFields == 0) {
                    Minesweeper.gameOverWin();
                }
            }
        });

        RIGHTCLICK = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag();
                }
            }
        };
        addMouseListener(RIGHTCLICK);
    }

    void toggleFlag() {
        if (revealed) {
            return;
        }

        switch (cellState) {
            case CellStates.nothing:
                cellState = CellStates.flagged;
                setText("🚩");
                break;
            case CellStates.flagged:
                cellState = CellStates.question;
                setText("?");
                break;
            case CellStates.question:
                cellState = CellStates.nothing;
                setText("");
                break;
            default:
                throw new AssertionError("Somehow cellstate was none of the enums: " + this);
        }
    }

    void reveal() {
        if (revealed) {
            return;
        }
        revealed = true;
        setEnabled(false);

        if (isMine) {
            setText("💣");
            setBackground(Color.RED);
        } else {
            totalFreeFields--;
            cellState = CellStates.nothing; // important for showing gameover screen
            if (neighborMines > 0) {
                setText(Integer.toString(neighborMines));
                setBackground(Color.LIGHT_GRAY);
            } else {
                setBackground(Color.GRAY);
            }
        }
    }

    void revealAdjacent() {
        int size = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState())[Minesweeper.CELLAMOUNT];
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && r < size && c >= 0 && c < size) {
                    Cell neighbor = Minesweeper.cells[r][c];
                    if (!neighbor.revealed && !neighbor.isMine) {
                        neighbor.reveal();
                        if (neighbor.neighborMines == 0) {
                            neighbor.revealAdjacent();
                        }
                    }
                }
            }
        }
    }

    void disableRightClick() {
        removeMouseListener(RIGHTCLICK);
    }

    public static void calculateTotalFreeFields(int dim) {
        totalFreeFields = (dim * dim) - totalMines;
    }

    private static void placeMines(int mineCount, int clickedRow, int clickedColumn) {
        int placed = 0;
        int size = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState())[Minesweeper.CELLAMOUNT];
        while (placed < mineCount) {
            int row = RAND.nextInt(size);
            int column = RAND.nextInt(size);
            if (!Minesweeper.cells[row][column].isMine && !(clickedRow == row && clickedColumn == column)) {
                Minesweeper.cells[row][column].isMine = true;
                placed++;
            }
        }
        minesPlaced = true;
    }

    private static void countAllNeighbors() {
        int size = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState())[Minesweeper.CELLAMOUNT];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Minesweeper.cells[r][c].neighborMines = countNeighbors(r, c);
            }
        }
    }

    private static int countNeighbors(int row, int column) {
        int count = 0;
        int size = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState())[Minesweeper.CELLAMOUNT];
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && r < size && c >= 0 && c < size) {
                    if (Minesweeper.cells[r][c].isMine) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

}
