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
    public enum EntityType { ROCK, SLIME, BAT, LADDER, WALL }

    // Entities
    private List<Entity> rocks = new ArrayList<>();
    private List<Entity> monsters = new ArrayList<>();
    private List<Entity> walls = new ArrayList<>();
    private Entity ladder;
    private boolean ladderRevealed = false;

    // Interaction
    private int axeTimer = 0;

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

    private java.util.Random rand = new java.util.Random();

    public GameModel() {
        generateLevel();
    }

    public void generateLevel() {
        rocks.clear();
        monsters.clear();
        walls.clear();
        ladderRevealed = false;

        // Reset player to top-left (inside walls)
        playerX = 40;
        playerY = 40;

        // 0. Generate Walls
        float thickness = 32;
        walls.add(new Entity(0, 0, WORLD_WIDTH, thickness, EntityType.WALL)); // Top
        walls.add(new Entity(0, WORLD_HEIGHT - thickness, WORLD_WIDTH, thickness, EntityType.WALL)); // Bottom
        walls.add(new Entity(0, 0, thickness, WORLD_HEIGHT, EntityType.WALL)); // Left
        walls.add(new Entity(WORLD_WIDTH - thickness, 0, thickness, WORLD_HEIGHT, EntityType.WALL)); // Right

        // 1. Generate Rocks
        int rockCount = 20 + rand.nextInt(15); 
        for (int i = 0; i < rockCount; i++) {
            Entity rock = createSafeEntity(EntityType.ROCK, 32, 32);
            if (rock != null) rocks.add(rock);
        }

        // 2. Hide Ladder under a random rock
        if (!rocks.isEmpty()) {
            Entity hiddenUnder = rocks.get(rand.nextInt(rocks.size()));
            hiddenUnder.containsLadder = true;
            ladder = new Entity(hiddenUnder.x, hiddenUnder.y, 32, 32, EntityType.LADDER);
        }

        // 3. Generate Monsters
        int monsterCount = 4 + rand.nextInt(4);
        for (int i = 0; i < monsterCount; i++) {
            EntityType type = rand.nextBoolean() ? EntityType.SLIME : EntityType.BAT;
            Entity monster = createSafeEntity(type, 32, 32);
            if (monster != null) monsters.add(monster);
        }
    }

    private Entity createSafeEntity(EntityType type, float w, float h) {
        int attempts = 0;
        float padding = 40; // Stay away from walls
        while (attempts < 100) {
            float rx = padding + rand.nextInt((int)(WORLD_WIDTH - w - padding * 2));
            float ry = padding + rand.nextInt((int)(WORLD_HEIGHT - h - padding * 2));

            // Don't spawn on player
            if (new Entity(rx, ry, w, h, type).intersects(playerX - 20, playerY - 20, PLAYER_SIZE + 40, PLAYER_SIZE + 40)) {
                attempts++;
                continue;
            }

            // Don't spawn on existing entities
            boolean overlaps = false;
            List<Entity> all = new ArrayList<>(rocks);
            all.addAll(monsters);
            all.addAll(walls);
            for (Entity other : all) {
                if (other.intersects(rx, ry, w, h)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                return new Entity(rx, ry, w, h, type);
            }
            attempts++;
        }
        return null;
    }

    public void updateMonsters() {
        float monsterSpeed = 4f / 3f; // 1/3 of player speed
        float aggroRange = PLAYER_SIZE * 5;

        for (int i = 0; i < monsters.size(); i++) {
            Entity m = monsters.get(i);
            
            // 1. Calculate distance to player
            float dx = playerX - m.x;
            float dy = playerY - m.y;
            float dist = (float)Math.sqrt(dx*dx + dy*dy);

            // 2. Chasing logic
            if (dist < aggroRange && dist > 0) {
                float moveX = (dx / dist) * monsterSpeed;
                float moveY = (dy / dist) * monsterSpeed;
                
                float nextX = m.x + moveX;
                float nextY = m.y + moveY;

                // Check collisions with Rocks, Walls, and OTHER monsters
                boolean blockedX = false;
                boolean blockedY = false;
                
                List<Entity> obstacles = new ArrayList<>(rocks);
                obstacles.addAll(walls);
                for (int j = 0; j < monsters.size(); j++) {
                    if (i != j) obstacles.add(monsters.get(j));
                }

                for (Entity obs : obstacles) {
                    if (obs.intersects(nextX, m.y, m.width, m.height)) blockedX = true;
                    if (obs.intersects(m.x, nextY, m.width, m.height)) blockedY = true;
                }

                if (!blockedX) m.x = nextX;
                if (!blockedY) m.y = nextY;
            }

            // 3. Collision with Player (Damage & Knockback)
            if (m.intersects(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)) {
                health = Math.max(0, health - 10);
                applyKnockback(m.x, m.y);
                
                // Knockback monster away from player
                float kx = m.x - playerX;
                float ky = m.y - playerY;
                float kMag = (float)Math.sqrt(kx*kx + ky*ky);
                if (kMag > 0) {
                    m.x += (kx / kMag) * PLAYER_SIZE * 2;
                    m.y += (ky / kMag) * PLAYER_SIZE * 2;
                }
            }
        }
    }

    public void updateSwing() {
        if (axeTimer > 0) axeTimer--;
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

        List<Entity> solidEntities = new ArrayList<>(rocks);
        solidEntities.addAll(walls);

        for (Entity solid : solidEntities) {
            // Check X movement
            if (solid.intersects(nextX, playerY, PLAYER_SIZE, PLAYER_SIZE)) {
                blockedX = true;
                // Forgiveness logic: If mostly past the rock vertically, slide
                float overlapTop = (playerY + PLAYER_SIZE) - solid.y;
                float overlapBottom = (solid.y + solid.height) - playerY;
                if (overlapTop < PLAYER_SIZE * 0.2f) playerY -= overlapTop;
                else if (overlapBottom < PLAYER_SIZE * 0.2f) playerY += overlapBottom;
            }
            // Check Y movement
            if (solid.intersects(playerX, nextY, PLAYER_SIZE, PLAYER_SIZE)) {
                blockedY = true;
                // Forgiveness logic: If mostly past the rock horizontally, slide
                float overlapLeft = (playerX + PLAYER_SIZE) - solid.x;
                float overlapRight = (solid.x + solid.width) - playerX;
                if (overlapLeft < PLAYER_SIZE * 0.2f) playerX -= overlapLeft;
                else if (overlapRight < PLAYER_SIZE * 0.2f) playerX += overlapRight;
            }
        }

        if (!blockedX) playerX = Math.max(0, Math.min(WORLD_WIDTH - PLAYER_SIZE, nextX));
        if (!blockedY) playerY = Math.max(0, Math.min(WORLD_HEIGHT - PLAYER_SIZE, nextY));

        // Ladder Collision (Next Floor)
        if (ladderRevealed && ladder != null) {
            if (ladder.intersects(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE)) {
                currentFloor++;
                if (currentFloor < 5) {
                    generateLevel();
                }
            }
        }
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
        axeTimer = 30; // ~0.5 seconds at 60fps
        float[] hb = getHitbox();
        
        // Check Rocks
        for (int i = rocks.size() - 1; i >= 0; i--) {
            Entity r = rocks.get(i);
            if (r.intersects(hb[0], hb[1], hb[2], hb[3])) {
                if (r.containsLadder) ladderRevealed = true;
                rocks.remove(i);
                oreCount++;
                return; // Hit one rock per swing
            }
        }

        // Check Monsters
        for (int i = monsters.size() - 1; i >= 0; i--) {
            Entity m = monsters.get(i);
            if (m.intersects(hb[0], hb[1], hb[2], hb[3])) {
                monsters.remove(i);
                return; // Hit one monster per swing
            }
        }
    }

    public List<Entity> getWalls() { return walls; }
    public boolean isSwinging() { return axeTimer > 0; }

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
