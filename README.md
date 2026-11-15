# Three Card Poker

CS 342 Project 2 by Ethan Luu and Angelo Landiza.

Contributions:

	Ethan: UI/UX design, styling, and implementation

	Angelo: Buisness/game logic and code architecture/design

## Run

- Run: mvn clean javafx:run
- Test: mvn test

## Components

- model (Card, Deck, Dealer, Player): the cards, the deck, dealing, and each player's hand, wagers and winnings.
- game.ThreeCardLogic: hand evaluation, hand comparison and Pair Plus payouts.
- game.GameService: runs a round: bets, deal, Play or Fold, dealer reveal, results, next round.
- controller: one controller per FXML screen; handles events and updates the interface.
- Main: switches screens, opens the rules window, switches the theme.

## Screens

- Welcome: start the game, read the rules, switch theme, exit.
- Game Play: place bets, see your cards, then Play or Fold.
- Results: hands, winner, wager results and updated totals; play again or return to the menu.

## Animations

- Cards slide to each player: shows the deal.
- Dealer cards flip: shows the reveal.
- Winner's total pulses: points to the payout.
- Screens fade in: shows the screen changed.

## Changes from Part 1 testing

- Larger, clearer cards with red hearts and diamonds.
- Rules button on the game and results screens.

## Design questions

**1. Why did you separate game logic from the FXML controllers?**

It allows for junit testing without opening a window, and lets us change a screen without any risk of breaking a payout. 

**2. What usability problems did other students identify?**

- Participant 1: the cards were hard and slow to read.
- Participant 1: the different bet amounts were hard to tell apart, and betting vs playing was confusing.
- Participant 2: the rules could only be seen from the main menu, so there was no way to check them during a game.
- Participant 2: it took a moment to find out how to bet.

**3. Which wireframe revisions most improved the user experience?**

Adding the Rules button to the game and results screens helped the most. Before it, a player who forgot the rules had to leave the game to read them. Making the cards clearer also helped (making the rank and suit larger and color coding them),  because reading the cards is something a player does every round

**4. How do the animations help users understand game events?**

Each animation is tied to one game event ex. cards sliding from the dealer show that the deal happened and which cards belong to which player and the dealer's cards flipping show when the dealer's hand is revealed. The same information is also shown as text to ensure no infomration is lost if the user misses an animation.

**5. How could the interface be redesigned without changing the game rules?**

The controllers only call  methods from the GameService class which means the fxml layouts, the CSS and the controllers could all be replaced.

**6. How does your architecture make the application easier to test and maintain?**

Each part of the code has one job, so change is independent and stays in one place.
