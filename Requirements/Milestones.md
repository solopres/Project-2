# Zwei Game   🎴

## Project Overview
- Zwei is a Java-based game similar to UNO. The basic rules will remain the same, but more complicated rules like “jumping in” or special features of the zero and seven will not be implemented. Zwei will support both player versus player and, in later deliverables, player versus computer gameplay. The implementation of a GUI will be dependent on the success rate of previous sprints and is not guaranteed due to time constraints.

**Scrum Master** (Solomon)
-  Solomon create the GitHub repository and sets up the Kanban board. He creates issues, assigns tasks to each team member, and send out email to ensure clear communication and keep everyone informed about their responsibilities and progress.


## Sprint 1:  Basic Card and Deck Setup 🎴
 
**User Story:** 📜
- AS A: Card Game Enthusiast
- I WANT: A Java UNO-like game
- SO THAT: I can play UNO solo or with friends

 **1. Create the Card Class:** 🧑‍💻 (Jawaria)
- Define the fields (color, type, value).
- Implement constructor and getters.
- Add toString() method  

**2. Create the Deck Class:**  🎴(Jawaria)
- Initialize deck with 108 cards (numbers, actions, and wild cards).
- Implement methods like drawCard(), discard(), shuffle(), remainingCards(), and getTopDiscard().

 **3. Task Assignments:** 👥
- Task 1: Implement the Card Class – Assigned to me (Jawaria).
- Task 2: Implement the Deck Class – Assigned to me (Jawaria).  
- Task 3: Testing: Write basic unit tests for both the Card and Deck classes.
-  Ensure methods like drawCard(), remainingCards(), and discard() are working correctly.
- Validate that cards are created with the correct properties (color, type, value).

**4. Deliverables:** 📦
- Card Class: A fully functioning class that represents a single card.
- Deck Class: A deck of 108 cards that can shuffle, draw, and discard cards.
- Unit Tests: Basic tests for the methods in both classes.

**5. Security Requirements:** 🔒
- Input validation when creating cards (ex: card value and type must match).
- Prevent any invalid actions (like adding a null card to the deck).
-  Logging of card-related activities

**6. Testing:** 🧪
- Test that the Deck correctly initializes and shuffles the cards.
- Test the Card class for valid properties and ensure the toString() method returns readable output.

**7.Documentation:** 📚  (Lateefat)
- Maintain documentation related to the Card and Deck classes.
- Include user stories, requirements, security considerations, and testing outcomes in the documentation binder.

**8. Design:** 📐 (Braxton):
...

 

## Sprint 2: Player Mechanics and Basic Gameplay 🎮
**Requirements and User Stories:**
User Story:
- As a player, I want to be able to hold a hand of cards, draw cards from the deck, and play matching cards according to the game rules, so that I can participate in a game of Zwei.
 
**1. Creating The Player Class :** 🧑‍💻(Jawaria):
- Define the fields (name, hand, isAI).
- Implement methods to draw cards from the deck and play cards.
- Implement helper methods to check if a card can be played (matching the top discard).
- Create basic gameplay mechanics such as checking if the player has any playable cards.

**2. Basic Game Logic:** ⚙️(Jawaria):
- Implement basic card matching rules (color, type, or value match).
- Ensure the deck is updated when cards are drawn or played.

**3. Task Assignments:** 👥

- Task 1: Implement the Player Class – Assigned to me (Jawaria). 💻
 
**4. Deliverables:** 📦
- Player Class: A fully functioning class that tracks the player’s hand and actions. 🎮
- Methods to draw cards, play cards, check the hand for playable cards.
- Basic Game Logic: Rules for card matching and updating the deck.
- Unit Tests: Basic tests for Player actions like drawing cards and playing cards.

**5. Security Requirements:** 🔐
- Input validation to ensure only valid cards are played.
- Prevent invalid player actions like trying to play a card when it’s not the player’s turn.
- Log player actions for debugging and tracking.

**6. Testing:** 🧪  
- Player Class Testing: Test that players can draw cards, play valid cards, and that their hand is correctly updated.
- Basic Game Logic Testing: Test the card matching logic to ensure players can only play cards that match the top discard.

**7.Documentation:** 📚  (Lateefat)

**Player Class Documentation:**
- Document the methods and functionalities of the Player class.
- Include details on how the player interacts with the deck and other players
**-Gameplay Logic Documentation:**
- Document the rules that are implemented for drawing and playing cards.
- Provide a description of how turns progress and how moves are validated.

**8. Design:** 📐 (Braxton):
...

 **Sprint Deadline:** ⏳
- End of Week 1 Mar 30, 2025
