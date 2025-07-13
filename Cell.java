
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

public class Cell extends JButton {

    int row, column;
    boolean isMine = false;
    boolean revealed = false;
    int neighborMines = 0;
    CellState cellState = CellState.nothing;
    MouseAdapter rightClick;
    public static Random rand = new Random();
    public static boolean minesPlaced = false;
    public static int totalFreeFields; // technically all freefields that have not been revealed yet

    public Cell(int row, int col) {
        this.row = row;
        this.column = col;

        setFont(new Font("LastResort", Font.BOLD, 16));
        addActionListener((e) -> {
            if (!minesPlaced) {
                placeMines(Minesweeper.MINES, this.row, this.column);
                countAllNeighbors();
            }
            if (cellState == CellState.nothing) {
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

        rightClick = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag();
                }
            }
        };
        addMouseListener(rightClick);
    }

    void toggleFlag() {
        if (revealed) {

            return;
        }

        switch (cellState) {
            case CellState.nothing:
                cellState = CellState.flagged;
                setText("🚩");
                break;
            case CellState.flagged:
                cellState = CellState.question;
                setText("?");
                break;
            case CellState.question:
                cellState = CellState.nothing;
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
            cellState = CellState.nothing; // important for showing gameover screen
            if (neighborMines > 0) {
                setText(Integer.toString(neighborMines));
                setBackground(Color.LIGHT_GRAY);
            } else {
                setBackground(Color.GRAY);
            }
        }
    }

    void revealAdjacent() {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && r < Minesweeper.SIZE && c >= 0 && c < Minesweeper.SIZE) {
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
        removeMouseListener(rightClick);
    }

    public static void setTotalFreeFields(int dim) {
        totalFreeFields = (dim * dim) - Minesweeper.MINES;
    }

    private static void placeMines(int mineCount, int clickedRow, int clickedColumn) {
        int placed = 0;
        while (placed < mineCount) {
            int row = rand.nextInt(Minesweeper.SIZE);
            int column = rand.nextInt(Minesweeper.SIZE);
            if (!Minesweeper.cells[row][column].isMine && !(clickedRow == row && clickedColumn == column)) {
                Minesweeper.cells[row][column].isMine = true;
                placed++;
            }
        }
        minesPlaced = true;
    }

    private static void countAllNeighbors() {
        for (int r = 0; r < Minesweeper.SIZE; r++) {
            for (int c = 0; c < Minesweeper.SIZE; c++) {
                Minesweeper.cells[r][c].neighborMines = countNeighbors(r, c);
            }
        }
    }

    private static int countNeighbors(int row, int column) {
        int count = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = column - 1; c <= column + 1; c++) {
                if (r >= 0 && r < Minesweeper.SIZE && c >= 0 && c < Minesweeper.SIZE) {
                    if (Minesweeper.cells[r][c].isMine) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

}
