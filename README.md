# stardew-mining
A small version of mining from Stardew Valley. 

## Gameplay
You are controlling a dwarf inside a mine. The dwarf can move left and right using the arrow keys. There will be rocks blocking your way, and you need to break them using your pickaxe. You can swing your pickaxe using the spacebar. Your goal is to clear the path and reach the end of the mine. The game ends when you reach the end of the mine.

## Model - MiningModel.java
- dwarf position (x, y) (groid coords)
- stone position (x, y) (groid coords)
- game state (playing, won, lost)

## View - MiningView.java
- draws dwarf as green square at dwarf position
- draws stone as grey square at stone position
- if the dwarf is next to the stone and swing pickaxe - stone disappears
- if the dwarf reaches the end of the mine - display "You win!" text
- if the dwarf falls into a hole - display "You lose!" text

## Controller - MiningController.java
- handles key presses for moving the dwarf
- handles key presses for swinging the pickaxe
- handles game state transitions
- updates the model
- draws the view

## ModelTester.java
- tests the model
