import java.util.List;
import java.util.ArrayList;

public class ModelTester {
    public static void main(String[] args) {
        System.out.println("🚀 Starting GameModel Test Suite...\n");

        runTest("Player Spawn & Walls", testPlayerSpawn());
        runTest("Level Density", testLevelDensity());
        runTest("Ladder Existence", testLadderExists());
        runTest("Player Collision", testPlayerCollision());
        runTest("Axe Hitbox Range", testAxeRange());
        runTest("Combat Damage", testDamageCalculation());
        runTest("Knockback Boundary Safety", testKnockbackSafety());
        runTest("Monster AI & Obstacles", testMonsterAI());
        runTest("Win/Loss Logic", testWinLossConditions());
        runTest("Timer Consistency", testTimerConsistency());
        runTest("Single Target Axe", testSingleTargetAxe());
        runTest("HUD Data Availability", testHUDData());

        System.out.println("\n✅ Testing Complete.");
    }

    private static void runTest(String name, boolean passed) {
        System.out.printf("[%s] %s\n", passed ? "PASS" : "FAIL", name);
    }

    // 11. Player always starts inside walls in top-left
    private static boolean testPlayerSpawn() {
        GameModel model = new GameModel();
        model.generateLevel();
        return model.getPlayerX() == 40 && model.getPlayerY() == 40;
    }

    // 9, 10. Level density boundaries
    private static boolean testLevelDensity() {
        GameModel model = new GameModel();
        model.generateLevel();
        int rocks = model.getRocks().size();
        int monsters = model.getMonsters().size();
        return rocks >= 20 && rocks <= 35 && monsters >= 4 && monsters <= 8;
    }

    // 1. Ladder spawns under a rock
    private static boolean testLadderExists() {
        GameModel model = new GameModel();
        model.generateLevel();
        boolean found = false;
        for (GameModel.Entity r : model.getRocks()) {
            if (r.containsLadder) found = true;
        }
        return found && model.getLadder() != null;
    }

    // 12. Player cannot pass through walls or rocks
    private static boolean testPlayerCollision() {
        GameModel model = new GameModel();
        model.generateLevel();
        // Try to move left into the wall (walls are at x=0 to 32)
        float startX = model.getPlayerX();
        model.updatePlayer(-20, 0); 
        return model.getPlayerX() == startX; // Should be blocked by left wall
    }

    // 3. Axe range is 1 square (32px * 2 = 64px)
    private static boolean testAxeRange() {
        GameModel model = new GameModel();
        float[] hb = model.getHitbox();
        // Default facing is DOWN. hb[3] is height. hb[3] should be 64 (2x player size)
        return hb[3] == 64;
    }

    // 4. Health reduction 10 points
    private static boolean testDamageCalculation() {
        GameModel model = new GameModel();
        int startHealth = model.getHealth();
        // Force monster collision by spawning it on player
        GameModel.Entity slime = new GameModel.Entity(model.getPlayerX(), model.getPlayerY(), 32, 32, GameModel.EntityType.SLIME);
        model.getMonsters().add(slime);
        model.updateMonsters();
        return model.getHealth() == startHealth - 10;
    }

    // 2. Knockback does not cause entities to get stuck in walls
    private static boolean testKnockbackSafety() {
        GameModel model = new GameModel();
        model.generateLevel();
        // Place player near left wall (player is at 40, wall is at 32)
        // Apply knockback from the right (sourceX = 100)
        model.applyKnockback(100, model.getPlayerY());
        return model.getPlayerX() >= 32; // Should stop at the wall boundary
    }

    // 6. Monster AI chasing & collisions
    private static boolean testMonsterAI() {
        GameModel model = new GameModel();
        model.generateLevel();
        // Clear obstacles for clean test
        model.getRocks().clear();
        // Place monster within aggro range (5x player size = 160px)
        GameModel.Entity slime = new GameModel.Entity(model.getPlayerX() + 100, model.getPlayerY(), 32, 32, GameModel.EntityType.SLIME);
        model.getMonsters().add(slime);
        float startX = slime.x;
        model.updateMonsters();
        return slime.x < startX; // Should move left towards player
    }

    // 5. Win/Loss conditions
    private static boolean testWinLossConditions() {
        GameModel model = new GameModel();
        boolean initial = !model.isWin() && !model.isGameOver();
        // Test health loss
        while (model.getHealth() > 0) {
            GameModel.Entity m = new GameModel.Entity(model.getPlayerX(), model.getPlayerY(), 32, 32, GameModel.EntityType.SLIME);
            model.getMonsters().add(m);
            model.updateMonsters();
            model.getMonsters().clear();
        }
        boolean loss = model.isGameOver();
        return initial && loss;
    }

    // 7. Timer consistency
    private static boolean testTimerConsistency() {
        GameModel model = new GameModel();
        int start = model.getTimeLeft();
        model.updateTimer();
        return model.getTimeLeft() == start - 1;
    }

    // 8. Axe targets only one entity at a time
    private static boolean testSingleTargetAxe() {
        GameModel model = new GameModel();
        model.getRocks().clear();
        // Place two rocks in front of player
        float px = model.getPlayerX();
        float py = model.getPlayerY() + 32; // Directly below player (DOWN hitbox)
        model.getRocks().add(new GameModel.Entity(px, py, 32, 32, GameModel.EntityType.ROCK));
        model.getRocks().add(new GameModel.Entity(px, py + 10, 32, 32, GameModel.EntityType.ROCK));
        int startCount = model.getRocks().size();
        model.handleAction();
        return model.getRocks().size() == startCount - 1;
    }

    // 14. HUD text data availability
    private static boolean testHUDData() {
        GameModel model = new GameModel();
        // Verify getters for all HUD components
        try {
            model.getOreCount();
            model.getHealth();
            model.getTimeLeft();
            model.getMonstersKilled();
            model.getCurrentFloor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
