import java.util.ArrayList;
import java.util.List;

public class GameModel {
    // Constants
    public static final float PLAYER_SIZE = 32f;
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = 600f;

    // Player State
    private float playerX = 100, playerY = 100;
    private int health = 100;
    private int oreCount = 0;
    private int currentFloor = 1;
    private Direction facing = Direction.DOWN;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public enum EntityType { ROCK, SLIME, BAT, LADDER }

    // Entities
    private List<Entity> rocks = new ArrayList<>();
    private List<Entity> monsters = new ArrayList<>();
    private Entity ladder;
    private boolean ladderRevealed = false;

    public static class Entity {
        public float x, y, width, height;
        public EntityType type;
        public boolean containsLadder;

        public Entity(float x, float y, float w, float h, EntityType type) {
            this.x = x; this.y = y; this.width = w; this.height = h;
            this.type = type;
        }
        public boolean intersects(float ex, float ey, float ew, float eh) {
            return x < ex + ew && x + width > ex && y < ey + eh && y + height > ey;
        }
    }

    public GameModel() {
        generateLevel();
    }

    public void generateLevel() {
        rocks.clear();
        monsters.clear();
        
        // Add some dummy entities for visual testing
        rocks.add(new Entity(200, 200, 32, 32, EntityType.ROCK));
        rocks.add(new Entity(240, 200, 32, 32, EntityType.ROCK));
        
        monsters.add(new Entity(400, 300, 32, 32, EntityType.SLIME));
        monsters.add(new Entity(500, 100, 32, 32, EntityType.BAT));
        
        ladder = new Entity(300, 300, 32, 32, EntityType.LADDER);
        ladderRevealed = true; // Show for testing
    }

    public void updateMonsters() {
        // TODO: Handle AI pathing and aggro logic
    }

    public void updatePlayer(float dx, float dy) {
        if (dx > 0) facing = Direction.RIGHT;
        else if (dx < 0) facing = Direction.LEFT;
        else if (dy > 0) facing = Direction.DOWN;
        else if (dy < 0) facing = Direction.UP;

        float nextX = playerX + dx;
        float nextY = playerY + dy;

        // Collision & 20% Corner Forgiveness
        boolean blockedX = false;
        boolean blockedY = false;

        for (Entity rock : rocks) {
            if (rock.type == EntityType.ROCK) {
                // Check X movement
                if (rock.intersects(nextX, playerY, PLAYER_SIZE, PLAYER_SIZE)) {
                    blockedX = true;
                    // Forgiveness logic: If mostly past the rock vertically, slide
                    float overlapTop = (playerY + PLAYER_SIZE) - rock.y;
                    float overlapBottom = (rock.y + rock.height) - playerY;
                    if (overlapTop < PLAYER_SIZE * 0.2f) playerY -= overlapTop;
                    else if (overlapBottom < PLAYER_SIZE * 0.2f) playerY += overlapBottom;
                }
                // Check Y movement
                if (rock.intersects(playerX, nextY, PLAYER_SIZE, PLAYER_SIZE)) {
                    blockedY = true;
                    // Forgiveness logic: If mostly past the rock horizontally, slide
                    float overlapLeft = (playerX + PLAYER_SIZE) - rock.x;
                    float overlapRight = (rock.x + rock.width) - playerX;
                    if (overlapLeft < PLAYER_SIZE * 0.2f) playerX -= overlapLeft;
                    else if (overlapRight < PLAYER_SIZE * 0.2f) playerX += overlapRight;
                }
            }
        }

        if (!blockedX) playerX = Math.max(0, Math.min(WORLD_WIDTH - PLAYER_SIZE, nextX));
        if (!blockedY) playerY = Math.max(0, Math.min(WORLD_HEIGHT - PLAYER_SIZE, nextY));
    }

    public void applyKnockback(float sourceX, float sourceY) {
        float dx = playerX - sourceX;
        float dy = playerY - sourceY;
        float mag = (float)Math.sqrt(dx*dx + dy*dy);
        if (mag == 0) return;
        
        // Push 2x size away
        playerX += (dx / mag) * PLAYER_SIZE * 2;
        playerY += (dy / mag) * PLAYER_SIZE * 2;
        
        // Clamp to screen
        playerX = Math.max(0, Math.min(WORLD_WIDTH - PLAYER_SIZE, playerX));
        playerY = Math.max(0, Math.min(WORLD_HEIGHT - PLAYER_SIZE, playerY));
    }

    public float[] getHitbox() {
        float hW = PLAYER_SIZE;
        float hH = PLAYER_SIZE;
        float hX = playerX;
        float hY = playerY;

        switch (facing) {
            case UP:    hY -= PLAYER_SIZE; hH = PLAYER_SIZE * 2; break;
            case DOWN:  hH = PLAYER_SIZE * 2; break;
            case LEFT:  hX -= PLAYER_SIZE; hW = PLAYER_SIZE * 2; break;
            case RIGHT: hW = PLAYER_SIZE * 2; break;
        }
        return new float[]{hX, hY, hW, hH};
    }

    public void handleAction() {
        float[] hb = getHitbox();
        // TODO: Interaction check with entities in hb
    }

    public boolean isGameOver() { return health <= 0; }
    public boolean isWin() { return currentFloor >= 5 || oreCount >= 100; }

    public List<Entity> getRocks() { return rocks; }
    public List<Entity> getMonsters() { return monsters; }
    public Entity getLadder() { return ladder; }
    public boolean isLadderRevealed() { return ladderRevealed; }
    
    public float getPlayerX() { return playerX; }
    public float getPlayerY() { return playerY; }
    public int getHealth() { return health; }
    public int getOreCount() { return oreCount; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getFacing() { return facing; }
}
