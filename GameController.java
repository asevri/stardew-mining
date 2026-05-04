import javax.swing.*;
import java.awt.event.*;

public class GameController implements KeyListener, ActionListener {
    private GameModel model;
    private GameView view;
    private Timer gameLoop;

    private boolean up, down, left, right;
    private float moveSpeed = 4f;

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
        float dx = 0, dy = 0;
        if (up) dy -= moveSpeed;
        if (down) dy += moveSpeed;
        if (left) dx -= moveSpeed;
        if (right) dx += moveSpeed;

        if (dx != 0 || dy != 0) {
            model.updatePlayer(dx, dy);
        }

        // Coordination Sequence
        model.updateMonsters();
        
        view.render(model);

        if (model.isGameOver() || model.isWin()) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) up = true;
        if (key == KeyEvent.VK_DOWN) down = true;
        if (key == KeyEvent.VK_LEFT) left = true;
        if (key == KeyEvent.VK_RIGHT) right = true;
        
        if (key == KeyEvent.VK_SPACE) {
            model.handleAction();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) up = false;
        if (key == KeyEvent.VK_DOWN) down = false;
        if (key == KeyEvent.VK_LEFT) left = false;
        if (key == KeyEvent.VK_RIGHT) right = false;
    }
    @Override public void keyTyped(KeyEvent e) {}
}
