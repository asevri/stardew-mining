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
        runTest("Ladder Reachability", testLadderReachability());
        runTest("Monster Obstacle Collisions", testMonsterObstacles());
        runTest("No-Spawn Safety", testNoSpawnSafety());

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

    // New Test 1: Reachable Ladder
    private static boolean testLadderReachability() {
        GameModel model = new GameModel();
        model.generateLevel();
        GameModel.Entity ladder = model.getLadder();
        if (ladder == null) return false;
        
        // Check if ladder is within playable bounds (inside walls)
        float padding = 32; // Wall thickness
        boolean inside = ladder.x >= padding && ladder.x + ladder.width <= GameModel.WORLD_WIDTH - padding &&
                         ladder.y >= padding && ladder.y + ladder.height <= GameModel.WORLD_HEIGHT - padding;
        return inside;
    }

    // New Test 2: Monster Obstacle Collisions
    private static boolean testMonsterObstacles() {
        GameModel model = new GameModel();
        model.generateLevel();
        model.getMonsters().clear();
        model.getRocks().clear();
        
        // Place a rock
        float rx = 200, ry = 200;
        model.getRocks().add(new GameModel.Entity(rx, ry, 32, 32, GameModel.EntityType.ROCK));
        
        // Place monster to the left of the rock
        GameModel.Entity slime = new GameModel.Entity(rx - 30, ry, 32, 32, GameModel.EntityType.SLIME);
        model.getMonsters().add(slime);
        
        // Move player to the right of the rock so monster tries to move RIGHT through it
        // Note: Player at (40,40) by default. Move to (400, 200)
        // Wait, I can't move player easily without updatePlayer which has collision logic.
        // I'll just manually set player position if I could, but I can't.
        // Actually, updateMonsters uses playerX/playerY. I'll just use a fresh model.
        
        // Let's place rock at (100, 40) and monster at (132.5, 40). 
        // Monster at (132.5, 40, 32, 32) -> Right edge at 164.5, Left edge at 132.5.
        // Rock at (100, 40, 32, 32) -> Right edge at 132, Left edge at 100.
        // Monster speed is ~1.33. NextX = 132.5 - 1.33 = 131.17.
        // 131.17 < 132, so it should be blocked immediately.
        model.getRocks().clear();
        model.getRocks().add(new GameModel.Entity(100, 40, 32, 32, GameModel.EntityType.ROCK));
        slime.x = 132.5f; slime.y = 40;
        float startX = slime.x;
        model.updateMonsters();
        return slime.x == startX;
    }

    // New Test 3: No-Spawn Safety (Verify it's nearly impossible to get 0 rocks/monsters)
    private static boolean testNoSpawnSafety() {
        for (int i = 0; i < 100; i++) {
            GameModel model = new GameModel();
            model.generateLevel();
            if (model.getRocks().size() < 20 || model.getMonsters().size() < 4) return false;
            if (model.getLadder() == null) return false;
        }
        return true;
    }
}
