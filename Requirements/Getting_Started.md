
## What we Built
we created a card game called "Zwei" which is basically our own version of UNO. The game runs either in console mode or with a graphical interface.

## How It Works
When you start the program:
1. You see a welcome screen with two buttons: "Play Game" and "Exit"
2. Clicking "Play Game" takes you to the game board
3. The game board has spaces for:
   - Your cards at the bottom
   - Opponent cards at the top and sides
   - A deck to draw from
   - A discard pile in the center

## The Main Components

### Main.java
This is where everything starts:
- It decides whether to run as text-only or with graphics
- For graphics mode, it launches the game window

### ZweiGUI.java
This creates the game window:
- Makes that cool red-to-purple gradient background
- Sets up the welcome screen and game screen
- Handles switching between screens

### CardView.java
This draws the actual cards:
- Makes different colored cards (red, green, blue, yellow)
- Shows numbers or special abilities on cards
- Draws the card backs with "ZWEI" text
- Highlights cards you can play

### PlayerHandView.java
This shows the cards in each player's hand:
- Arranges cards in a slightly overlapping row
- Hides opponent's card faces
- Shows which cards you can legally play
- Displays player name and how many cards they have

### GamePlayPanel.java
This runs the actual game:
- Manages whose turn it is
- Handles clicking on cards to play them
- Lets you draw cards from the deck
- Updates the screen when something happens

 

 
 
