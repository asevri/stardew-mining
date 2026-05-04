# stardew-mining
A small version of mining from Stardew Valley. 

## Gameplay
You are controlling a dwarf inside a mine. The dwarf can move up, down, left, right using the arrow keys. There will be rocks blocking your way, and you need to break them using your pickaxe. You can swing your pickaxe using the spacebar. Your goal is to clear the path and reach the end of the mine. The game ends when you reach the end of the mine. There will be bats flying around the mine and if they touch you, you lose a life. You start with 3 lives. You can kill a bat by hitting it with your pickaxe.

## Model - MiningModel.java
- dwarf position (x, y) (groid coords)
- stone position (x, y) (groid coords)
- bat position (x, y) (groid coords)
- game state (playing, won, lost)

## View - MiningView.java
- draws dwarf as green square at dwarf position
- draws stone as grey square at stone position
- draws bat as black square at bat position
- if the dwarf is next to the stone and swing pickaxe - stone disappears
- if the dwarf reaches the end of the mine - display "You win!" text
- if the dwarf falls into a hole - display "You lose!" text
- if the bat touches the dwarf - take a life and display how many lives left
- if the dwarf touches the bat with pickaxe - bat disappears

## Controller - MiningController.java
- handles key presses for moving the dwarf
- handles key presses for swinging the pickaxe
- handles game state transitions
- updates the model
- draws the view

## ModelTester.java
- tests the model
