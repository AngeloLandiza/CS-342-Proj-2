---
title: "Three Card Poker — Part 1: Interface Design & Usability Evaluation"
author: "Angelo Landiza — CS 342"
---

# 1. Wireframes (Original, v1)

Legend: `[ Button ]` = button, `[5 ^v]` = number spinner, `[x]` = checkbox,
`(disabled)` = greyed out / not clickable, `>` = status/feedback message.

## Screen 1 — Welcome / Main Menu

```
+----------------------------------------------------------------------+
|                                           [ Theme ]  [ Exit X ]      |
|                                                                      |
|   THREE CARD POKER                                                   |
|   Two players. One dealer. Beat the hand.                            |
|                                                                      |
|   [  START GAME  ]   How to play ->     <- Start is primary, focused |
|                                         "How to play" opens Rules    |
|                                         Exit (top right) closes app  |
|                                                                      |
+----------------------------------------------------------------------+
Transition: screen fades out / next screen fades in.
```

## Screen 2 — Game Play (Phase 1: Place Bets)

```
+----------------------------------------------------------------------+
| [ < Main Menu ]       PHASE 1 of 2: PLACE BETS      [ Theme: Light ] |
|----------------------------------------------------------------------|
|                              DEALER                                  |
|                       [ ? ]  [ ? ]  [ ? ]   (face down)              |
|----------------------------------------------------------------------|
|  PLAYER 1                          |  PLAYER 2                       |
|  [   ]  [   ]  [   ]   (no cards)  |  [   ]  [   ]  [   ]           |
|  Ante:          [ 5 ^v]  $5-$25    |  Ante:          [ 5 ^v]  $5-$25 |
|  [x] Pair Plus  [ 5 ^v]  $5-$25    |  [ ] Pair Plus  [ - ] (disabled)|
|  Play wager:  --                   |  Play wager:  --                |
|  [ PLAY ]  [ FOLD ]    (disabled)  |  [ PLAY ]  [ FOLD ]  (disabled) |
|  Total winnings: $0                |  Total winnings: $0             |
|----------------------------------------------------------------------|
|  > Set each player's Ante (Pair Plus is optional), then Deal.        |
|                       [    DEAL CARDS    ]                           |
|                       [   SHOW RESULTS   ]  (disabled)               |
+----------------------------------------------------------------------+
Error prevention: spinners only allow $5-$25, so invalid wagers are impossible.
Main Menu during a round asks "Leave game? Progress will be lost." [Yes][No].
```

## Screen 2 — Game Play (Phase 2: Play or Fold)

```
+----------------------------------------------------------------------+
| [ < Main Menu ]      PHASE 2 of 2: PLAY OR FOLD     [ Theme: Light ] |
|----------------------------------------------------------------------|
|                              DEALER                                  |
|                       [ ? ]  [ ? ]  [ ? ]   (still hidden)           |
|----------------------------------------------------------------------|
|  PLAYER 1                          |  PLAYER 2                       |
|  [K S]  [K D]  [4 C]  <- dealt in  |  [9 H]  [5 C]  [2 S]            |
|  Ante:      $10   (locked)         |  Ante:      $5    (locked)      |
|  Pair Plus: $5    (locked)         |  Pair Plus: --                  |
|  Play wager:  --                   |  Play wager:  --                |
|  [ PLAY $10 ]  [ FOLD ]  (enabled) |  [ PLAY $5 ]  [ FOLD ] (enabled)|
|  Total winnings: $0                |  Total winnings: $0             |
|----------------------------------------------------------------------|
|  > Each player: Play (bet = Ante) or Fold.                           |
|                       [    DEAL CARDS    ]  (disabled)               |
|                       [   SHOW RESULTS   ]  (disabled until both     |
|                                              players have decided)   |
+----------------------------------------------------------------------+
Animation: cards slide from the deck to each player (TranslateTransition).
When both decide, dealer cards flip face up (ScaleTransition flip),
then SHOW RESULTS becomes enabled.
```

## Alternate State — Player 1 Folded

```
+----------------------------------------------------------------------+
| [ < Main Menu ]      PHASE 2 of 2: PLAY OR FOLD     [ Theme: Light ] |
|----------------------------------------------------------------------|
|                       [ ? ]  [ ? ]  [ ? ]                            |
|----------------------------------------------------------------------|
|  PLAYER 1  -- FOLDED --            |  PLAYER 2                       |
|  [///]  [///]  [///]  (greyed)     |  [9 H]  [5 C]  [2 S]            |
|  "Folded: Ante $10 and Pair Plus   |  Ante: $5                       |
|   $5 are lost."                    |  [ PLAY $5 ]  [ FOLD ]          |
|  [ PLAY ]  [ FOLD ]   (disabled)   |                                 |
|----------------------------------------------------------------------|
|  > Player 1 folded. Waiting for Player 2 to Play or Fold.            |
+----------------------------------------------------------------------+
Folded status is shown in text, not only by grey color.
```

## Screen 3 — Results / Round Summary

```
+----------------------------------------------------------------------+
|                         ROUND RESULTS                                |
|  DEALER:  [Q H]  [7 S]  [3 D]    High Card (Queen)   QUALIFIES       |
|----------------------------------------------------------------------|
|  PLAYER 1                          |  PLAYER 2                       |
|  [K S]  [K D]  [4 C]    PAIR       |  [9 H]  [5 C]  [2 S]  HIGH CARD |
|  RESULT:  WIN vs. Dealer           |  RESULT:  LOSE vs. Dealer       |
|  Ante       $10   ->  +$10         |  Ante       $5    ->  -$5       |
|  Play       $10   ->  +$10         |  Play       $5    ->  -$5       |
|  Pair Plus  $5 (Pair 1:1) -> +$5   |  Pair Plus  --                  |
|  This round:  +$25                 |  This round:  -$10              |
|  TOTAL WINNINGS:  $25   <- pulses  |  TOTAL WINNINGS:  -$10          |
|----------------------------------------------------------------------|
|        [   PLAY AGAIN   ]                 [   MAIN MENU   ]          |
+----------------------------------------------------------------------+
If the dealer does NOT qualify: "Dealer does not qualify (needs Queen-high).
Play wager returned; Ante carries to next round."
Animation: total winnings scale up briefly (ScaleTransition).
```

# 2. Navigation Diagram

```
                 [Start Game]                    [Show Results]
  +---------+ ---------------> +-----------+ ------------------> +---------+
  | WELCOME |                  | GAME PLAY |                     | RESULTS |
  +---------+ <--------------- +-----------+ <------------------ +---------+
     |   |    [Main Menu]+confirm            [Play Again]          |
     |   |                                                         |
     |   +--[How to Play]--> Rules dialog --[Close]--> WELCOME     |
     |                                                             |
     +--[Exit]--> app closes          WELCOME <----[Main Menu]-----+
```

# 3. Software Architecture

The app is split into three layers. Rules live only in the model/logic
layer, the service runs the round, and controllers only display state and
forward clicks.

| Layer | Class | Responsibility |
|---|---|---|
| Model | `Card` | Suit and value (2–14, Ace = 14). |
| Model | `Deck` | 52 cards; initialize, shuffle, reset. |
| Model | `Dealer` | Owns the `Deck`, deals 3-card hands, holds the dealer hand. |
| Model | `Player` | Hand, ante, pair plus, play wager, total winnings. |
| Logic | `ThreeCardLogic` | Static `evalHand`, `evalPPWinnings`, `compareHands`. |
| Service | `GameService` | Holds the dealer, both players and the current phase. Places bets, deals, records play/fold, resolves the round, updates winnings, starts the next round. |
| UI | `App` | JavaFX entry point; switches scenes (`show("game.fxml")`) and applies the current theme stylesheet. |
| UI | `WelcomeController`, `GameController`, `ResultsController` | One per FXML screen; handle events, call `GameService`, update labels/buttons, run animations. |

**Keeping rules independent of the UI:** `Card`, `Deck`, `Dealer`,
`Player`, `ThreeCardLogic` and `GameService` import nothing from JavaFX.
Controllers never evaluate hands or compute payouts; they call
`GameService` and read its results. All of the logic can be tested with
JUnit without starting JavaFX, and screens can be redesigned without
touching the rules. A single `GameService` instance is shared by all
controllers, so state survives screen changes.

**Screen navigation:** `App` keeps the `Stage` and loads each FXML into a
new `Scene`. Controllers call `App.show(...)` when a navigation button is
clicked.

# 4. Usability Testing

**Tasks given to each participant** (they were not told where to click):

1. Start a new game.
2. Find the Ante and Pair Plus betting controls and place bets for both players.
3. Decide how to continue playing or fold.
4. Find the results of the completed round.
5. Start another round, then return to the main menu.

> **TODO:** Fill in from your real sessions with two students who are not on your team.

## Participant 1 — _name/initials, date_

| Task | Completed? | Hesitation / confusion | Questions asked |
|---|---|---|---|
| 1 | | | |
| 2 | | | |
| 3 | | | |
| 4 | | | |
| 5 | | | |

Suggestions / feedback:

## Participant 2 — _name/initials, date_

| Task | Completed? | Hesitation / confusion | Questions asked |
|---|---|---|---|
| 1 | | | |
| 2 | | | |
| 3 | | | |
| 4 | | | |
| 5 | | | |

Suggestions / feedback:

# 5. Usability Findings (at least 3)

> **TODO:** Write these from what you observed. Problems worth watching for:
> whether Pair Plus looks optional, whether it is clear that *both*
> players must decide before results appear, and whether "Main Menu"
> looks like it will throw away the game.

1.
2.
3.

# 6. Wireframe Revisions (at least 2)

> **TODO:** For each revision, paste the v1 excerpt, the revised excerpt,
> and the finding that caused the change.

## Revision 1

- **Original:**
- **Revised:**
- **Why:**

## Revision 2

- **Original:**
- **Revised:**
- **Why:**

# 7. UI/UX Principles Applied

- **Visual hierarchy:** the primary action on each screen (Start Game, Deal,
  Play Again) is the largest button. Dealer is at the top and players below.
- **Feedback:** a phase banner and a one-line status message on the game
  screen always say what to do next. Results spell out each payout.
- **Error prevention:** spinners limit wagers to $5–$25, buttons that are
  not valid in the current phase are disabled, and leaving an active game
  asks for confirmation.
- **Consistency:** both player panels use the same layout and the same
  buttons sit in the same places. One shared stylesheet (plus an alternate
  theme) covers every screen.
- **Accessibility:** all controls work with the keyboard and show focus,
  text is readable, and WIN/LOSE/FOLDED/PUSH are written out instead of
  relying on color alone.
- **Purposeful animation:** card dealing shows where cards go, the dealer
  flip marks the reveal, the winnings pulse draws attention to the new
  total, and screen fades make navigation clear. Every result is also shown
  as text, so nothing is lost if animations are skipped.
