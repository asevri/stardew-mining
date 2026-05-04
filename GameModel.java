import java.util.ArrayList;
import java.util.List;

public class GameModel {
    // Player State
    private float playerX, playerY;
    private int health = 100;
    private int oreCount = 0;
    private int currentFloor = 1;
    private Direction facing = Direction.DOWN;

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    // Entities
    private List<Object> rocks = new ArrayList<>();
    private List<Object> monsters = new ArrayList<>();

    public GameModel() {
        generateLevel();
    }

    public void generateLevel() {
        // TODO: Randomly generate rocks and monsters
        // TODO: Hide ladder under a random rock
    }

    public void updatePlayer(float dx, float dy) {
        // TODO: Handle movement and 20% corner forgiveness logic
    }

    public void updateMonsters() {
        // TODO: Handle AI pathing and aggro logic
    }

    public void handleAction() {
        // TODO: Use 2x hitbox to check for rock/monster interaction
    }

    public boolean isGameOver() { return health <= 0; }
    public boolean isWin() { return currentFloor >= 5 || oreCount >= 100; }

    // Getters for View
    public float getPlayerX() { return playerX; }
    public float getPlayerY() { return playerY; }
    public int getHealth() { return health; }
    public int getOreCount() { return oreCount; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getFacing() { return facing; }
}
