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


## Prompt 0: The Pitch
Me: I want to build a mining game from stardew valley in Java with Swing using MVC. Before we write any code, interview me. Ask me one question at a time about gameplay, controls, win and loss conditions, and what should be on the screen. After about 8 questions, summarize what I told you as a one-page spec organized by Model, View, and Controller.
AI: That sounds like an exciting project! Stardew Valley's mining mechanics are a great fit for an MVC-based Java Swing application.

To get started, I'll interview you to build out the specifications. I'll ask one question at a time.

Question 1: In terms of the core gameplay loop, should the focus be purely on mining rocks and collecting ores, or should we also include combat with monsters (like slimes or bats) as seen in Stardew Valley?


Me: Have monsters automatically move toward the player to attack only when the player gets close. Otherwise have monsters wait around the map. Break rocks and kill monsters in one hit.


AI: Thank you for all the details! I have a clear vision of the game now. Here is the technical specification for your Stardew Mining game, organized by the Model-View-Controller (MVC) architecture.

📋 Stardew Mining Game Specification
1. Model (Data & Logic)
PlayerState:
Coordinates (x, y) for smooth movement.
Health (int) - starts at 100, Game Over at 0.
Ore Count (int) - starts at 0, Win at 100.
Current Floor (int) - starts at 1, Win at Floor 5.
Entity System:
Rock: Has a position and a boolean containsLadder.
Monster: Types (Slime, Bat). Has a position and an isAggro state triggered by player proximity.
Level Model:
Manages a list of Rocks and Monsters for the current floor.
Handles the "Hidden Ladder" logic (revealed when the specific rock is destroyed).
Game Rules: Logic for checking win/loss conditions (Health <= 0 or Floor == 5 or Ores == 100).
2. View (User Interface & Rendering)
Main Game Window: A fixed-size JFrame (e.g., 800x600).
GamePanel (JPanel):
Graphics: Custom paintComponent to draw the floor, player, rocks, and monsters.
HUD (Heads-Up Display):
Top-left: Text counter for "Ores Collected".
Top-right: "Floor X" indicator.
Bottom: Visual Health Bar (Green bar that shrinks).
State Screens:
Game Over Screen: Displayed when health reaches zero.
Win Screen: Displayed when floor 5 is reached or 100 ores are collected.
3. Controller (Input & Coordination)
Input Handler:
KeyListener to map Arrow Keys to player velocity (smooth movement).
Spacebar listener to trigger the "Action" (checks for collision with rocks or monsters in range).
Game Loop:
A javax.swing.Timer running at ~60 FPS.
Update Cycle:
Update player position based on velocity.
Update monster AI (move toward player if within "aggro" radius).
Check for collisions (Player/Monster contact deals damage).
Trigger repaint() on the View.
Interaction Logic:
When Spacebar is pressed:
If a Rock is in range: Remove rock, add +1 to ore count, check if ladder is revealed.
If a Monster is in range: Remove monster instantly.
Does this spec look correct to you? If so, we can move on to setting up the project structure!


## Prompt 0.5: Devil's Advocate
Me: Now play devil's advocate. What is still underspecified in what I told you? What will be ambiguous when I start coding?


AI: Playing devil's advocate is a great way to avoid "code-stopping" questions later. Even with a solid spec, there are several "grey areas" that could cause bugs or design inconsistencies once the coding starts:

1. The "Action Range" & Facing Direction
The Ambiguity: When the player presses Spacebar, what determines "in range"?
The Problem: Is it a circle around the player? Or does the player have a direction (Up, Down, Left, Right)? If I'm facing Up but there's a rock to my Left, do I hit it?
Recommendation: Usually, games like this require the player to be facing the target or have the target within a very small hitbox in front of them.
2. Monster Damage Logic
The Ambiguity: "If they touch you, you lose health."
The Problem: How often do you lose health? If a slime sits on top of you for 1 second at 60 FPS, do you lose 1 health 60 times (instant death)?
Recommendation: You likely need an "Invincibility Period" (e.g., 1 second of flashing/no damage after getting hit) or a "knockback" effect to push the player away.
3. Collision Physics (The "Sticky Wall" Problem)
The Ambiguity: "Free movement with smooth walking."
The Problem: Smooth movement + Rocks = Collision detection. What happens when the player walks diagonally into a rock corner? Do they slide past it, or do they get "stuck" on the pixel?
Recommendation: We'll need to decide if we're using simple bounding boxes (Rectangles) and how to handle collisions so the player doesn't feel like they're "glued" to walls.
4. Level Generation
The Ambiguity: "Each floor separate."
The Problem: Are the rocks and monsters placed in the same spot every time you play? Or is it Procedural Generation (randomly scattered)?
Recommendation: Random scattering is more fun, but we need rules to ensure the ladder isn't trapped inside a wall or that the player doesn't spawn on top of a monster.
5. Transitioning Floors
The Ambiguity: "Find ladder under rock."
The Problem: When you go down the ladder to Floor 2, where do you spawn? At the same (x, y) where the ladder was? In the center of the screen? Or at a designated "Entrance"?
Recommendation: Usually, spawning at a fixed "Entry Point" is safest for level design.
6. Win/Loss Priority
The Ambiguity: Two win conditions (Floor 5 OR 100 ores).
The Problem: What happens if the player hits a monster and dies at the exact same moment they mine their 100th ore?
Recommendation: The code needs a clear priority (e.g., if Health <= 0, Loss always overrides Win).

Me: Keep previous specifications as is. Add to the Game specifications to correct imbeguity as following: 1) Have player face the rock or monster in order to hit them. Have hitbox size twice as big as the character. 2) If bat or slime hit the player, both the player and the moster have a knockback effect that pushes monster and player away from eachother by twice the distance of player size. 3) Use simple squares as bounding boxes for player and rocks. Allow player to pass rocks diagonally if collision is less than 20% of rock's bounding box width. 4) Have player start each level in upper left corner of the game play window. Have each floor randomly generate rocks and monsters. Always hide a ladder under a random rock. Do not allow monsters to be generated over rocks or player. Do not allow rocks to be generated over monsters or player. Make sure player, monsters, and rocks are not generated outside play window, 5) Have player start each level in upper left corner of the game play window. 6) If there is a conflict between Win or Loss conditions, pick Loss as priority. If such conflict happens display special message "Winning does not matter if you die at the same time.".

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
The knockback in my game has a few bugs. Player can get stuck on the wall or rocks after monster hits him. Implement knockback in a way that player will not get stuck on the wall or rocks. If player is pushed against a wall or rock, they should stop there and not slide along it or pass through it. Do not implement anything else.

Result: Player no longer gets stuck on walls or rocks. Monsters still sometimes get stuck on walls or rocks.

Fixes: Now fix monsters' knockback.

Observation: I forgot to consider monsters as well and AI did not take any initiative this time.

## Prompt 11: Fixing Knockback For Monsters
Fix monsters' knockback so they don't get stuck on walls or rocks. If a monster is pushed against a wall or rock, it should stop there and not slide along it or pass through it. Do not implement anything else.

Result: Monsters don't get stuck on walls or rocks anymore. 

Fixes: None

Observation: None

## Prompt 12: Fix Health Bar To Not Go Past Zero
Fix health bar so it doesn't go past zero. When health reaches 0, it should stop there and not go negative. Do not implement anything else.

Result: Health bar no longer goes past zero.

Fixes: None

Observation: AI said it was already capped at zero previously. Not sure if it's true.

## Prompt 13: Knockback Interaction Between Monsters
Fix knockback interaction between monsters. If a monster is pushed against another monster, it should stop there and not slide along it or pass through it. Do not implement anything else.

Result: "Stuck together" monsters now bounce off each other.

Fixes: None

Observation: None

## Prompt 14: Add Monster Kill Counter
In the GameModel.java add a counter for the number of monsters killed. In the GameView.java display it in the HUD. When a monster is killed, increment the counter. Do not implement anything else.

Result: Added count

Fixes: None

Observation: None

## Prompt 13: Add 10 min Timer
Add a 10 minute timer to the game. When the timer reaches 0, the player loses and screen displays "TIME'S UP!". Add timer to the game loop. In HUD add the current time as minutes and seconds. Do not implement anything else.

Result: Timer added.

Fixes: None

Observation: None

## Prompt 14: Testing The Game

Result: 

Fixes: 

Observation:

## Prompt 15: Adding Darkness

Result: 

Fixes: 

Observation: 