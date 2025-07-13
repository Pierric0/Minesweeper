
import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem; // maybe später

public class NavBar extends JMenuBar {

    private final JMenuItem NEWGAME;
    private final JMenu DIFFICULTY;
    private final JMenuItem EASY, MEDIUM, HARD;

    // TODO: this is also part of the difficulty data
    public NavBar(Minesweeper minesweeper) {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        NEWGAME = new JMenuItem("New Game");
        NEWGAME.setOpaque(false);
        NEWGAME.addActionListener((_) -> {

            Integer[] temp = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState());
            minesweeper.newGame(temp[Minesweeper.CELLAMOUNT], temp[Minesweeper.MINES], temp[Minesweeper.WINDOWWIDTH], temp[Minesweeper.WINDOWHEIGHT]);

        });

        DIFFICULTY = new JMenu("Difficulty");
        EASY = new JMenuItem("Easy");
        EASY.addActionListener((_) -> {
            Minesweeper.setDifficultyState(DifficultyState.easy);
            Integer[] temp = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState());
            minesweeper.newGame(temp[Minesweeper.CELLAMOUNT], temp[Minesweeper.MINES], temp[Minesweeper.WINDOWWIDTH], temp[Minesweeper.WINDOWHEIGHT]);

        });
        MEDIUM = new JMenuItem("Medium");
        MEDIUM.addActionListener((_) -> {
            Minesweeper.setDifficultyState(DifficultyState.medium);
            Integer[] temp = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState());
            minesweeper.newGame(temp[Minesweeper.CELLAMOUNT], temp[Minesweeper.MINES], temp[Minesweeper.WINDOWWIDTH], temp[Minesweeper.WINDOWHEIGHT]);
        });
        HARD = new JMenuItem("Hard");
        HARD.addActionListener((_) -> {
            Minesweeper.setDifficultyState(DifficultyState.hard);
            Integer[] temp = Minesweeper.getDifficultyPresets().get(Minesweeper.getDifficultyState());
            minesweeper.newGame(temp[Minesweeper.CELLAMOUNT], temp[Minesweeper.MINES], temp[Minesweeper.WINDOWWIDTH], temp[Minesweeper.WINDOWHEIGHT]);
        });

        add(NEWGAME);
        DIFFICULTY.add(EASY);
        DIFFICULTY.add(MEDIUM);
        DIFFICULTY.add(HARD);
        add(DIFFICULTY);
    }

}
