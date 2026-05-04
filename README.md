# stardew-mining
A small version of mining from Stardew Valley. 

## Gameplay
You are controlling a dwarf inside a mine. The dwarf can move up, down, left, right using the arrow keys. There will be rocks blocking your way, and you need to break them using your pickaxe. You can swing your pickaxe using the spacebar. Your goal is to clear the path and reach the end of the mine. The game ends when you reach the end of the mine or collect 100 ores. There will be bats and slimes around the mine and if they touch you, you lose health. You start with 100 health. You can kill a bat or slime by hitting it with your pickaxe.


📋 Stardew Mining Game Specification
1. Model (Data & Logic)
    PlayerState:
        - Coordinates (x, y) for smooth movement.
        - Health (int) - starts at 100, Game Over at 0.
        - Ore Count (int) - starts at 0, Win at 100.
        - Current Floor (int) - starts at 1, Win at Floor 5.
    Entity System:
        - Rock: Has a position and a boolean containsLadder.
        - Monster: Types (Slime, Bat). Has a position and an isAggro state triggered by player proximity.
    Level Model:
        - Manages a list of Rocks and Monsters for the current floor.
        - Handles the "Hidden Ladder" logic (revealed when the specific rock is destroyed).
    Game Rules: Logic for checking win/loss conditions (Health <= 0 or Floor == 5 or Ores == 100).
2. View (User Interface & Rendering)
    Main Game Window: A fixed-size JFrame (e.g., 800x600).
    GamePanel (JPanel):
        - Graphics: Custom paintComponent to draw the floor, player, rocks, and monsters.
        - HUD (Heads-Up Display):
            - Top-left: Text counter for "Ores Collected".
            - Top-right: "Floor X" indicator.
            - Bottom: Visual Health Bar (Green bar that shrinks).
    State Screens:
        - Game Over Screen: Displayed when health reaches zero.
        - Win Screen: Displayed when floor 5 is reached or 100 ores are collected.
3. Controller (Input & Coordination)
    Input Handler:
        - KeyListener to map Arrow Keys to player velocity (smooth movement).
        - Spacebar listener to trigger the "Action" (checks for collision with rocks or monsters in range).
    Game Loop:
        - A javax.swing.Timer running at ~60 FPS.
        - Update Cycle:
            - Update player position based on velocity.
            - Update monster AI (move toward player if within "aggro" radius).
            - Check for collisions (Player/Monster contact deals damage).
            - Trigger repaint() on the View.
    Interaction Logic:
        - When Spacebar is pressed:
            - If a Rock is in range: Remove rock, add +1 to ore count, check if ladder is revealed.
            - If a Monster is in range: Remove monster instantly.
