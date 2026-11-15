package edu.uic.cs342.poker.game;

import edu.uic.cs342.poker.model.Dealer;
import edu.uic.cs342.poker.model.Player;
import java.util.ArrayList;

public class GameService {
    public static final int MIN_BET = 5;
    public static final int MAX_BET = 25;

    private final Dealer dealer = new Dealer();
    private final Player[] players = { new Player(), new Player() };
    private final RoundResult[] results = new RoundResult[2];
    private final boolean[] anteCarried = new boolean[2];
    private Phase phase = Phase.BETTING;

    public Phase getPhase() {
        return phase;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public RoundResult getResult(int index) {
        return results[index];
    }

    public boolean isAnteCarried(int index) {
        return anteCarried[index];
    }

    public void placeBets(int index, int ante, int pairPlus) {
        if (!anteCarried[index] && !inRange(ante)) {
            throw new IllegalArgumentException("Ante must be $" + MIN_BET + " to $" + MAX_BET + ".");
        }

        if (pairPlus != 0 && !inRange(pairPlus)) {
            throw new IllegalArgumentException("Pair Plus must be $" + MIN_BET + " to $" + MAX_BET + ".");
        }

        if (!anteCarried[index])
            players[index].setAnteBet(ante);

        players[index].setPairPlusBet(pairPlus);
    }

    public void deal() {
        for (Player player : players)
            player.setHand(dealer.dealHand());
        dealer.setHand(dealer.dealHand());

        phase = Phase.DECIDING;
    }

    public void play(int index) {
        decide(index, false);
    }

    public void fold(int index) {
        decide(index, true);
    }

    public boolean hasDecided(int index) {
        return players[index].isFolded() || players[index].getPlayBet() > 0;
    }

    public boolean dealerQualifies() {
        return ThreeCardLogic.dealerQualifies(dealer.getHand());
    }

    public String getDealerHandName() {
        return ThreeCardLogic.handName(dealer.getHand());
    }

    public String getPlayerHandName(int index) {
        return ThreeCardLogic.handName(players[index].getHand());
    }

    public void nextRound() {
        boolean qualifies = dealerQualifies();
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            anteCarried[i] = !qualifies && !player.isFolded();

            if (!anteCarried[i])
                player.setAnteBet(0);

            player.setPairPlusBet(0);
            player.setPlayBet(0);
            player.setFolded(false);
            player.setHand(new ArrayList<>());

            results[i] = null;
        }

        dealer.setHand(new ArrayList<>());
        phase = Phase.BETTING;
    }

    private void decide(int index, boolean fold) {
        Player player = players[index];

        if (fold) {
            player.setFolded(true);
        } else {
            player.setPlayBet(player.getAnteBet());
        }

        if (hasDecided(0) && hasDecided(1))
            resolve();
    }

    private void resolve() {
        boolean qualifies = dealerQualifies();
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];

            if (player.isFolded()) {
                results[i] = new RoundResult("FOLDED", -player.getAnteBet(), 0, -player.getPairPlusBet());
            } else {
                int won = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlusBet());
                int pairPlus = won > 0 ? won : -player.getPairPlusBet();
                int winner = qualifies ? ThreeCardLogic.compareHands(dealer.getHand(), player.getHand()) : 0;
                int sign = winner == 2 ? 1 : winner == 1 ? -1 : 0;
                String outcome = sign > 0 ? "WIN" : sign < 0 ? "LOSE" : "PUSH";

                results[i] = new RoundResult(outcome, sign * player.getAnteBet(), sign * player.getPlayBet(), pairPlus);
            }

            player.setTotalWinnings(player.getTotalWinnings() + results[i].total());
        }
        phase = Phase.RESOLVED;
    }

    private boolean inRange(int bet) {
        return bet >= MIN_BET && bet <= MAX_BET;
    }
}
