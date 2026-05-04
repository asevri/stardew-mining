## Implementation To Do List
- Basic:
    - Make sure player starts at the same position on every floor (top left corner).
    - Add a wall around the playing window to prevent player from going off screen.
    - Add collision between player and monsters, and rocks and monsters (except bats).
- To Make It More Fun:
    - Make a mine dark and only light up the area around the player.


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

## Prompt 4: Wiring the Controller 


Result: 

Fixes: 

Observation: 

## Prompt 5: Basic Model Testing

Result: 

Fixes: 

Observation: 