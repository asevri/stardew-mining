import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private GamePanel gamePanel;

    public GameView() {
        setTitle("Stardew Mining");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);
    }

    public void render(GameModel model) {
        gamePanel.setModel(model);
        gamePanel.repaint();
    }

    private class GamePanel extends JPanel {
        private GameModel model;

        public void setModel(GameModel model) { this.model = model; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (model == null) return;

            // Background
            g.setColor(new Color(40, 40, 40));
            g.fillRect(0, 0, getWidth(), getHeight());

            // TODO: Draw Rocks & Monsters
            
            // Draw Player (Placeholder)
            g.setColor(Color.GREEN);
            g.fillRect((int)model.getPlayerX(), (int)model.getPlayerY(), 32, 32);

            // Draw HUD
            drawHUD(g);
        }

        private void drawHUD(Graphics g) {
            g.setColor(Color.WHITE);
            g.drawString("Ores: " + model.getOreCount(), 20, 20);
            g.drawString("Floor: " + model.getCurrentFloor(), 720, 20);

            // Health Bar
            g.setColor(Color.RED);
            g.fillRect(20, 540, 200, 20);
            g.setColor(Color.GREEN);
            g.fillRect(20, 540, (int)(2 * model.getHealth()), 20);
            g.setColor(Color.WHITE);
            g.drawRect(20, 540, 200, 20);
        }
    }
}
