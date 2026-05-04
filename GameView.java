import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private GamePanel gamePanel;

    public GameView() {
        setTitle("Stardew Mining");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(800, 600));
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
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
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw Walls
            for (GameModel.Entity wall : model.getWalls()) {
                g.setColor(new Color(101, 67, 33)); // Brown
                g.fillRect((int)wall.x, (int)wall.y, (int)wall.width, (int)wall.height);
                g.setColor(new Color(70, 40, 20)); // Darker outline
                g.drawRect((int)wall.x, (int)wall.y, (int)wall.width, (int)wall.height);
            }

            // Draw Ladder (if revealed)
            if (model.isLadderRevealed() && model.getLadder() != null) {
                GameModel.Entity l = model.getLadder();
                g.setColor(new Color(139, 69, 19)); // Brown
                g.fillRect((int)l.x, (int)l.y, (int)l.width, (int)l.height);
                g.setColor(Color.YELLOW);
                for (int i = 0; i < 4; i++) {
                    g.drawLine((int)l.x, (int)l.y + i*8, (int)l.x + (int)l.width, (int)l.y + i*8);
                }
            }

            // Draw Rocks
            for (GameModel.Entity rock : model.getRocks()) {
                g.setColor(Color.GRAY);
                g.fillRect((int)rock.x, (int)rock.y, (int)rock.width, (int)rock.height);
                g.setColor(Color.DARK_GRAY);
                g.drawRect((int)rock.x, (int)rock.y, (int)rock.width, (int)rock.height);
            }

            // Draw Monsters
            for (GameModel.Entity m : model.getMonsters()) {
                if (m.type == GameModel.EntityType.SLIME) {
                    g.setColor(Color.GREEN);
                    g.fillOval((int)m.x, (int)m.y, (int)m.width, (int)m.height);
                } else if (m.type == GameModel.EntityType.BAT) {
                    g.setColor(new Color(50, 0, 50)); // Dark Purple
                    g.fillRect((int)m.x, (int)m.y, (int)m.width, (int)m.height);
                    g.setColor(Color.BLACK);
                    g.drawLine((int)m.x, (int)m.y, (int)m.x + (int)m.width, (int)m.y + (int)m.height);
                }
            }

            // Draw Player (Dwarf)
            g.setColor(new Color(0, 150, 0));
            int px = (int)model.getPlayerX();
            int py = (int)model.getPlayerY();
            int size = (int)GameModel.PLAYER_SIZE;
            g.fillRect(px, py, size, size);

            // Draw Facing Triangle
            g.setColor(model.isSwinging() ? Color.RED : Color.WHITE);
            int[] tx = new int[3];
            int[] ty = new int[3];
            int tSize = 8;
            switch (model.getFacing()) {
                case UP:    tx[0] = px + size/2; ty[0] = py - tSize; tx[1] = px + size/2 - tSize; ty[1] = py; tx[2] = px + size/2 + tSize; ty[2] = py; break;
                case DOWN:  tx[0] = px + size/2; ty[0] = py + size + tSize; tx[1] = px + size/2 - tSize; ty[1] = py + size; tx[2] = px + size/2 + tSize; ty[2] = py + size; break;
                case LEFT:  tx[0] = px - tSize; ty[0] = py + size/2; tx[1] = px; ty[1] = py + size/2 - tSize; tx[2] = px; ty[2] = py + size/2 + tSize; break;
                case RIGHT: tx[0] = px + size + tSize; ty[0] = py + size/2; tx[1] = px + size; ty[1] = py + size/2 - tSize; tx[2] = px + size; ty[2] = py + size/2 + tSize; break;
            }
            g.fillPolygon(tx, ty, 3);

            // Draw HUD
            drawHUD(g);

            // End Screens
            if (model.isGameOver() || model.isWin()) {
                drawEndScreen(g);
            }
        }

        private void drawHUD(Graphics g) {
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.WHITE);
            g.drawString("Ores: " + model.getOreCount(), 20, 30);
            g.drawString("Floor: " + model.getCurrentFloor(), 700, 30);

            // Health Bar
            g.setColor(Color.DARK_GRAY);
            g.fillRect(20, 540, 200, 25);
            g.setColor(model.getHealth() > 30 ? Color.GREEN : Color.RED);
            g.fillRect(20, 540, (int)(2 * model.getHealth()), 25);
            g.setColor(Color.WHITE);
            g.drawRect(20, 540, 200, 25);
            g.drawString("HEALTH", 25, 560);
        }

        private void drawEndScreen(Graphics g) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.WHITE);

            String msg = "";
            if (model.isGameOver() && model.isWin()) {
                g.setFont(new Font("Arial", Font.BOLD, 25));
                msg = "Winning does not matter if you die at the same time.";
            } else if (model.isGameOver()) {
                msg = "GAME OVER";
            } else if (model.isWin()) {
                msg = "YOU WIN!";
            }

            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = (getHeight() / 2);
            g.drawString(msg, x, y);
        }
    }
}
