package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.Main;
import edu.uic.cs342.poker.game.GameService;
import edu.uic.cs342.poker.game.RoundResult;
import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.Player;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ResultsController {
    @FXML
    private HBox dealerCards;
    @FXML
    private Label dealerLabel;
    @FXML
    private Label qualifyLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label hand1;
    @FXML
    private Label hand2;
    @FXML
    private HBox cards1;
    @FXML
    private HBox cards2;
    @FXML
    private Label outcome1;
    @FXML
    private Label outcome2;
    @FXML
    private Label total1;
    @FXML
    private Label total2;
    @FXML
    private Label detail1;
    @FXML
    private Label detail2;
    @FXML
    private Label round1;
    @FXML
    private Label round2;

    private final GameService game = Main.getGame();

    @FXML
    private void initialize() {
        for (Card card : game.getDealer().getHand())
            dealerCards.getChildren().add(new CardView(card));

        dealerLabel.setText(game.getDealerHandName());
        qualifyLabel.setText(game.dealerQualifies()
                ? "QUALIFIES"
                : "DOES NOT QUALIFY - Play returned, Ante carries over");
        messageLabel.setText(describe(0) + " " + describe(1));

        showPlayer(0, hand1, cards1, outcome1, total1, detail1, round1);
        showPlayer(1, hand2, cards2, outcome2, total2, detail2, round2);
    }

    @FXML
    private void handlePlayAgainButton() {
        game.nextRound();
        Main.show("game");
    }

    @FXML
    private void handleMainMenuButton() {
        Main.show("welcome");
    }

    @FXML
    private void handleRulesButton() {
        Main.showRules();
    }

    @FXML
    private void handleThemeButton() {
        Main.toggleTheme();
    }

    private void showPlayer(int index, Label hand, HBox cards, Label outcome,
            Label total, Label detail, Label round) {
        Player player = game.getPlayer(index);
        RoundResult result = game.getResult(index);

        hand.setText(game.getPlayerHandName(index).toUpperCase());
        for (Card card : player.getHand())
            cards.getChildren().add(new CardView(card));
        outcome.setText(result.outcome());

        total.setText("Total " + Main.money(player.getTotalWinnings()));
        detail.setText(line("Ante", player.getAnteBet(), result.ante()) + "\n"
                + line("Play", player.getPlayBet(), result.play()) + "\n"
                + line("Pair Plus", player.getPairPlusBet(), result.pairPlus()));

        round.setText("This round " + signed(result.total()));

        if (result.total() > 0) {
            ScaleTransition pulse = new ScaleTransition(Duration.millis(300), total);

            pulse.setToX(1.25);
            pulse.setToY(1.25);
            pulse.setCycleCount(4);
            pulse.setAutoReverse(true);
            pulse.play();
        }
    }

    private String describe(int index) {
        String verb = switch (game.getResult(index).outcome()) {
            case "WIN" -> "wins";
            case "LOSE" -> "loses";
            case "FOLDED" -> "folded";
            default -> "pushes";
        };

        return "Player " + (index + 1) + " " + verb + ".";
    }

    private String line(String label, int bet, int result) {
        if (bet == 0)
            return label + " -";
        return label + " " + Main.money(bet) + " → " + signed(result);
    }

    private String signed(int amount) {
        return amount > 0 ? "+" + Main.money(amount) : Main.money(amount);
    }
}
