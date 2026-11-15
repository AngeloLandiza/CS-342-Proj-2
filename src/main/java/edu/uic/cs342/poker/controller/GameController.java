package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.Main;
import edu.uic.cs342.poker.game.GameService;
import edu.uic.cs342.poker.game.Phase;
import edu.uic.cs342.poker.model.Player;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class GameController {
    @FXML
    private Label phaseLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private HBox leaveBox;
    @FXML
    private HBox dealerCards;
    @FXML
    private Button actionButton;
    @FXML
    private Label anteChip1;
    @FXML
    private Label anteChip2;
    @FXML
    private Label pairPlusChip1;
    @FXML
    private Label pairPlusChip2;
    @FXML
    private Label playChip1;
    @FXML
    private Label playChip2;
    @FXML
    private HBox cards1;
    @FXML
    private HBox cards2;
    @FXML
    private Label status1;
    @FXML
    private Label status2;
    @FXML
    private Label winnings1;
    @FXML
    private Label winnings2;
    @FXML
    private TextField ante1;
    @FXML
    private TextField ante2;
    @FXML
    private TextField pairPlus1;
    @FXML
    private TextField pairPlus2;
    @FXML
    private Label info1;
    @FXML
    private Label info2;
    @FXML
    private Button play1;
    @FXML
    private Button play2;
    @FXML
    private Button fold1;
    @FXML
    private Button fold2;

    private final GameService game = Main.getGame();
    private Label[] anteChip;
    private Label[] pairPlusChip;
    private Label[] playChip;
    private HBox[] cards;
    private Label[] status;
    private Label[] winnings;
    private TextField[] ante;
    private TextField[] pairPlus;
    private Label[] info;
    private Button[] play;
    private Button[] fold;

    @FXML
    private void initialize() {
        anteChip = new Label[] { anteChip1, anteChip2 };
        pairPlusChip = new Label[] { pairPlusChip1, pairPlusChip2 };
        playChip = new Label[] { playChip1, playChip2 };
        cards = new HBox[] { cards1, cards2 };
        status = new Label[] { status1, status2 };
        winnings = new Label[] { winnings1, winnings2 };
        ante = new TextField[] { ante1, ante2 };
        pairPlus = new TextField[] { pairPlus1, pairPlus2 };
        info = new Label[] { info1, info2 };
        play = new Button[] { play1, play2 };
        fold = new Button[] { fold1, fold2 };
        for (int i = 0; i < 3; i++)
            dealerCards.getChildren().add(new CardView(null));
        for (int i = 0; i < 2; i++) {
            if (game.isAnteCarried(i))
                ante[i].setText(String.valueOf(game.getPlayer(i).getAnteBet()));
        }
        refresh();
    }

    @FXML
    private void handleActionButton() {
        if (game.getPhase() != Phase.BETTING) {
            Main.show("results");
            return;
        }
        try {
            for (int i = 0; i < 2; i++) {
                game.placeBets(i, parseBet(ante[i].getText()), parseBet(pairPlus[i].getText()));
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText("Invalid bet: " + e.getMessage());
            return;
        }
        game.deal();
        for (int i = 0; i < 2; i++)
            dealCards(i);
        refresh();
    }

    @FXML
    private void handlePlay1Button() {
        game.play(0);
        refresh();
    }

    @FXML
    private void handlePlay2Button() {
        game.play(1);
        refresh();
    }

    @FXML
    private void handleFold1Button() {
        game.fold(0);
        refresh();
    }

    @FXML
    private void handleFold2Button() {
        game.fold(1);
        refresh();
    }

    @FXML
    private void handleMainMenuButton() {
        leaveBox.setVisible(true);
    }

    @FXML
    private void handleLeaveButton() {
        Main.show("welcome");
    }

    @FXML
    private void handleStayButton() {
        leaveBox.setVisible(false);
    }

    @FXML
    private void handleRulesButton() {
        Main.showRules();
    }

    @FXML
    private void handleThemeButton() {
        Main.toggleTheme();
    }

    private int parseBet(String text) {
        if (text.isBlank())
            return 0;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter whole dollars.");
        }
    }

    private void dealCards(int index) {
        for (int i = 0; i < 3; i++) {
            CardView card = new CardView(game.getPlayer(index).getHand().get(i));
            cards[index].getChildren().add(card);
            card.setTranslateY(-150);
            TranslateTransition slide = new TranslateTransition(Duration.millis(350), card);
            slide.setDelay(Duration.millis(120 * i));
            slide.setToY(0);
            slide.play();
        }
    }

    private void refresh() {
        Phase phase = game.getPhase();
        for (int i = 0; i < 2; i++)
            refreshPlayer(i);
        if (phase == Phase.BETTING) {
            phaseLabel.setText("Step 1 of 2: Place Bets");
            messageLabel.setText("Set both bets, then deal.");
            actionButton.setText("Deal Cards");
            return;
        }
        phaseLabel.setText("Step 2 of 2: Play or Fold");
        actionButton.setText("Show Results");
        actionButton.setDisable(phase == Phase.DECIDING);
        if (phase == Phase.DECIDING) {
            messageLabel.setText(decisionMessage());
        } else {
            messageLabel.setText(game.dealerQualifies() ? "Dealer qualifies." : "Dealer does not qualify.");
            for (int i = 0; i < 3; i++) {
                CardView card = (CardView) dealerCards.getChildren().get(i);
                card.flipTo(game.getDealer().getHand().get(i));
            }
        }
    }

    private void refreshPlayer(int index) {
        Player player = game.getPlayer(index);
        boolean betting = game.getPhase() == Phase.BETTING;
        boolean canDecide = game.getPhase() == Phase.DECIDING && !game.hasDecided(index);
        ante[index].setDisable(!betting || game.isAnteCarried(index));
        pairPlus[index].setDisable(!betting);
        play[index].setDisable(!canDecide);
        fold[index].setDisable(!canDecide);
        winnings[index].setText("Winnings " + Main.money(player.getTotalWinnings()));
        anteChip[index].setText("ANTE\n" + chip(player.getAnteBet()));
        pairPlusChip[index].setText("PAIR PLUS\n" + chip(player.getPairPlusBet()));
        playChip[index].setText("PLAY\n" + chip(player.getPlayBet()));
        if (betting) {
            status[index].setText("SET BETS");
            info[index].setText(game.isAnteCarried(index)
                    ? "Ante carried over. Pair Plus is optional."
                    : "Ante is required. Pair Plus is optional.");
        } else if (player.isFolded()) {
            status[index].setText("FOLDED");
            info[index].setText("Ante and Pair Plus lost.");
        } else if (player.getPlayBet() > 0) {
            status[index].setText("PLAYED");
            info[index].setText("Play wager placed.");
        } else {
            status[index].setText("YOUR MOVE");
            info[index].setText("Play costs " + Main.money(player.getAnteBet()) + " (matches Ante).");
            play[index].setText("Play " + Main.money(player.getAnteBet()));
        }
    }

    private String chip(int amount) {
        return amount == 0 ? "-" : Main.money(amount);
    }

    private String decisionMessage() {
        for (int i = 0; i < 2; i++) {
            if (game.hasDecided(i)) {
                String decision = game.getPlayer(i).isFolded() ? " folded" : " plays";
                return "Player " + (i + 1) + decision + ". Waiting for Player " + (2 - i) + ".";
            }
        }
        return "Each player: Play or Fold.";
    }
}
