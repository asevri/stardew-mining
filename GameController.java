import javax.swing.*;
import java.awt.event.*;

public class GameController implements KeyListener, ActionListener {
    private GameModel model;
    private GameView view;
    private Timer gameLoop;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        // 60 FPS Timer
        gameLoop = new Timer(1000 / 60, this);
        view.addKeyListener(this);
    }

    public void startGame() {
        gameLoop.start();
        view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Coordination Sequence
        model.updateMonsters();
        // TODO: Check Collisions
        
        view.render(model);

        if (model.isGameOver() || model.isWin()) {
            gameLoop.stop();
            // TODO: Display end-game message based on Conflict Rule
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // TODO: Set player velocity based on Arrow Keys
        if (key == KeyEvent.VK_SPACE) {
            model.handleAction();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
