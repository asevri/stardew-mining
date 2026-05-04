## Implementation To Do List
- Basic:
    - Make sure player starts at the same position on every floor (top left corner). - DONE
    - Add a wall around the playing window to prevent player from going off screen. - DONE
    - Add collision between player and monsters, and rocks and monsters (except bats). - DONE
    - Add range of generated rocks/monsters, not too much, not too little.
    - Fix health bar to not go past zero.
    - Add tests to make sure that the game is working as expected.
    - When player gets knocked back from a monster, it should not get stuck on walls or rocks.
- To Make It More Fun:
    - Make a mine dark and only light up the area around the player.
    - Add a counter to track the number of monsters killed. Display the count on the HUD.
    - Add a timer countdown. When time runs out, you lose.


## Prompt 1: The MVC Skeleton
I am building Stardew Valley Mining in Java with Swing using MVC. Here is my spec:

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

Generate three class shells — GameModel.java, GameView.java, GameController.java — with method stubs based on this design. GameModel must not import any Swing classes. The program should compile and open a blank window.

Result: Four Java files were created: Main.java, GameController.java, GameView.java, and GameModel.java. The main character shows up in the top left corner with health bar at the bottom.

Fixes: Nothing to fix.

Observation: The model took a step further by creating a player than expected.

## Prompt 2: Building the Model

Implement in GameModel.java the player's coordinates (x, y) for smooth movement and facing direction (UP, DOWN, LEFT, RIGHT) from the design document. To indicate facing direction, draw a triangle at the front of the player. Create the 2x hitbox for the player and implement the logic for knockback and 20% corner forgiveness on diagonal movement. Do not implement anything else.

Result: Character can move with keys and direction player is facing is clear. 

Fixes: The player no longer starts at the top left corner. Fix it later, not a priority.

Observation: It might be bettwe to add a wall around the playing window to prevent player from going off screen. Implement it later.

## Prompt 3: Building the View
Fill in GameView.java with all the UI and visuals of the game based on the design document. That includes the HUD, the player, the rocks, monsters, the ladder, and the win/loss screens. Do not implement anything else. Do not implement combat or controller.

Result: 
    Rocks: Solid gray blocks with dark outlines.
    Slimes: Rendered as vibrant green circles.
    Bats: Rendered as dark purple squares with a diagonal "wing" line.
    Ladder: Drawn as a brown wooden structure with yellow rungs (revealed when a rock is broken).

Fixes: Nothing to fix yet.

Observation: Good start, need to add playable features. Also add collision between player and monsters, and rocks and monsters (except bats).

## Prompt 4: Random Level Generation
Implement random level generation in GameModel.java based on the design document. That includes: rocks, monsters, and a ladder. The ladder should be hidden under a rock. Ensure that there are no overlapping entities and that they are all within the bounds of the playing window. Do not implement anything else. 

Result: Looks great! Plenty of rocks and monsters. I never set a range to generate them, maybe it's better to add it later. 

Fixes: Nothing to fix yet.

Observation: Some rocks are half off the screen.

## Prompt 5: Combat and Mining Interactions (Axe)
Add a mining interaction and combat interaction. When player hits a rock with an axe, it breaks. When player hits a monster with an axe, it dies. When player hits a ladder with an axe, nothing should happen. When player uses axe show it by changing color of the triangle for a second. Add a wall around the playing window to prevent player from going off screen, and generate rocks/monsters inside the walls. If the player hits a wall with an axe, nothing should happen. Do not implement anything else. 

Result: Weirdly it already tried adding walls (is it reading my prompts file?) It changed playing background to white, I hate it. Axe interaction works as expected.

Fixes: Fix wall (only 3 out of 4 walls show up). Revert background to black.

Observation: AI took some liberties to jump ahead anyway.

## Prompt 6: Adding Wall
Add a wall around the playing window to prevent player from going off screen, and make sure generated rocks/monsters stay inside the walls. If the player hits a wall with an axe, nothing should happen. Make playing background black again. Make wall brown color. Do not implement anything else.

Result: Fixed the wall and colors.

Fixes: None

Observation: No new liberties were taken this time.

## Prompt 7: Implement Monster AI
Implement basic AI for slimes and bats in GameModel.java. Make monsters move with speed of 1/3 of player speed. Make monsters start moving towards the player when they get close, within 5 lengths of player size. Add damage to player when monsters touch them. Add knockback to player and monster when monsters touch them. Monsters should not be able to move through walls, rocks or other monsters. Do not implement anything else.

Result: Knockback works! Monsters start moving at good distance. Health is deducted.

Fixes: None

Observation: Add a count of Monsters killed for fun.

## Prompt 8: Adding Win and Loose Conditions
Implement win and loose conditions in GameModel.java. Player wins when they reach floor 5 or collect 100 ores. Player loses when their health reaches 0. When player loses, show "GAME OVER". When player wins, show "YOU WIN!". If both conditions are met at the same time player looses and show "Winning does not matter if you die at the same time." message. Do not implement anything else.

Result: Died successfully.

Fixes: None.

Observation: Health bar went past zero, minor issue at the moment.

## Prompt 9: Floor Transition
Implement a floor transition in GameModel.java. When player walks over the ladder, regenerate the level with new rocks and monsters. Reset player position to top left corner. Update floor counter. Make sure there are no overlaps of any entities. Do not implement anything else. 

Result: Implemented floor transition.

Fixes: Need to fix knockback from monsters because it can cause player to get stuck on walls or rocks.

Observation: Game has become sophisticated and needs testing.

## Prompt 10: Fixing Knockback Issue

Result: 

Fixes: 

Observation:

## Prompt 11: Fix Health Bar To Not Go Past Zero

Result: 

Fixes: 

Observation:

## Prompt 12: Add Monster Kill Counter

Result: 

Fixes: 

Observation:

## Prompt 13: Add 10 min Timer

Result: 

Fixes: 

Observation:

## Prompt 14: Testing The Game

Result: 

Fixes: 

Observation:

## Prompt 15: Adding Darkness

Result: 

Fixes: 

Observation: 