# stardew-mining
A small version of mining from Stardew Valley. 

## Gameplay
You are controlling a dwarf inside a mine. The dwarf can move up, down, left, right using the arrow keys. There will be rocks blocking your way, and you need to break them using your pickaxe. You can swing your pickaxe using the spacebar. Your goal is to clear the path and reach the end of the mine. The game ends when you reach the end of the mine or collect 100 ores. There will be bats and slimes around the mine and if they touch you, you lose health. You start with 100 health. You can kill a bat or slime by hitting it with your pickaxe.


📋 Stardew Mining Game Specification

1. Model (Data & Logic)
    - **PlayerState**: 
        - Coordinates (x, y) for smooth movement.
        - Facing direction (UP, DOWN, LEFT, RIGHT).
        - Health (int) - starts at 100, Game Over at 0.
        - Ore Count (int) - starts at 0, Win at 100.
        - Current Floor (int) - starts at 1, Win at Floor 5.
    - **Entity System**:
        - Rock: Square bounding box. Contains a boolean `containsLadder`.
        - Monster: Types (Slime, Bat). Aggro state triggered by proximity.
    - **Level Model**:
        - Random generation of rocks and monsters per floor.
        - Rules: No overlapping (Player/Rock/Monster). Everything must be inside the play window.
        - Ladder hidden under one random rock.
    - **Physics & Combat Logic**:
        - Hitbox: 2x player size in the direction they are facing.
        - Knockback: On monster contact, player and monster are pushed apart by 2x player size.
        - Collision: Square bounding boxes. 20% "forgiveness" on corners for diagonal movement.

2. View (User Interface & Rendering)
    - **Main Game Window**: Fixed-size JFrame (800x600).
    - **GamePanel (JPanel)**:
        - Custom rendering for all entities.
        - HUD: Ore counter (top-left), Floor indicator (top-right), Health bar (bottom).
    - **Screens**:
        - Win/Loss screens. 
        - Conflict Rule: If win and loss happen simultaneously, display: "Winning does not matter if you die at the same time."

3. Controller (Input & Coordination)
    - **Input**: Arrow keys (movement/facing) and Spacebar (action).
    - **Game Loop**: 60 FPS Timer.
    - **Updates**: Movement -> AI -> Collision -> Repaint.
    - **Action Logic**: Spacebar triggers a check in the 2x hitbox area in front of the player.

