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

            // Draw Hitbox (Visualization)
            float[] hb = model.getHitbox();
            g.setColor(new Color(255, 255, 0, 50));
            g.fillRect((int)hb[0], (int)hb[1], (int)hb[2], (int)hb[3]);

            // Draw Player (Dwarf)
            g.setColor(Color.GREEN);
            int px = (int)model.getPlayerX();
            int py = (int)model.getPlayerY();
            int size = (int)GameModel.PLAYER_SIZE;
            g.fillRect(px, py, size, size);

            // Draw Facing Triangle
            g.setColor(Color.WHITE);
            int[] tx = new int[3];
            int[] ty = new int[3];
            int tSize = 8;
            
            switch (model.getFacing()) {
                case UP:
                    tx[0] = px + size/2; ty[0] = py - tSize;
                    tx[1] = px + size/2 - tSize; ty[1] = py;
                    tx[2] = px + size/2 + tSize; ty[2] = py;
                    break;
                case DOWN:
                    tx[0] = px + size/2; ty[0] = py + size + tSize;
                    tx[1] = px + size/2 - tSize; ty[1] = py + size;
                    tx[2] = px + size/2 + tSize; ty[2] = py + size;
                    break;
                case LEFT:
                    tx[0] = px - tSize; ty[0] = py + size/2;
                    tx[1] = px; ty[1] = py + size/2 - tSize;
                    tx[2] = px; ty[2] = py + size/2 + tSize;
                    break;
                case RIGHT:
                    tx[0] = px + size + tSize; ty[0] = py + size/2;
                    tx[1] = px + size; ty[1] = py + size/2 - tSize;
                    tx[2] = px + size; ty[2] = py + size/2 + tSize;
                    break;
            }
            g.fillPolygon(tx, ty, 3);

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
