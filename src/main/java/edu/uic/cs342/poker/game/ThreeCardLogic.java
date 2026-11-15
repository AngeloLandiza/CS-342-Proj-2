package edu.uic.cs342.poker.game;

import edu.uic.cs342.poker.model.Card;
import java.util.ArrayList;
import java.util.Arrays;

public class ThreeCardLogic {
    public static final int HIGH_CARD = 0;
    public static final int STRAIGHT_FLUSH = 1;
    public static final int THREE_OF_A_KIND = 2;
    public static final int STRAIGHT = 3;
    public static final int FLUSH = 4;
    public static final int PAIR = 5;

    public static int evalHand(ArrayList<Card> hand) {
        int[] v = sortedValues(hand);
        boolean flush = hand.get(0).getSuit() == hand.get(1).getSuit()
                && hand.get(1).getSuit() == hand.get(2).getSuit();
        boolean straight = (v[0] + 1 == v[1] && v[1] + 1 == v[2])
                || (v[0] == 2 && v[1] == 3 && v[2] == 14);
        if (straight && flush)
            return STRAIGHT_FLUSH;
        if (v[0] == v[2])
            return THREE_OF_A_KIND;
        if (straight)
            return STRAIGHT;
        if (flush)
            return FLUSH;
        if (v[0] == v[1] || v[1] == v[2])
            return PAIR;

        return HIGH_CARD;
    }

    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        return switch (evalHand(hand)) {
            case STRAIGHT_FLUSH -> bet * 40;
            case THREE_OF_A_KIND -> bet * 30;
            case STRAIGHT -> bet * 6;
            case FLUSH -> bet * 3;
            case PAIR -> bet;
            default -> 0;
        };
    }

    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerScore = score(dealer);
        int playerScore = score(player);

        if (dealerScore > playerScore)
            return 1;
        if (playerScore > dealerScore)
            return 2;

        return 0;
    }

    public static boolean dealerQualifies(ArrayList<Card> dealer) {
        return evalHand(dealer) != HIGH_CARD || sortedValues(dealer)[2] >= 12;
    }

    public static String handName(ArrayList<Card> hand) {
        return switch (evalHand(hand)) {
            case STRAIGHT_FLUSH -> "Straight Flush";
            case THREE_OF_A_KIND -> "Three of a Kind";
            case STRAIGHT -> "Straight";
            case FLUSH -> "Flush";
            case PAIR -> "Pair";
            default -> "High Card";
        };
    }

    private static int[] sortedValues(ArrayList<Card> hand) {
        int[] values = { hand.get(0).getValue(), hand.get(1).getValue(), hand.get(2).getValue() };
        Arrays.sort(values);

        return values;
    }

    private static int score(ArrayList<Card> hand) {
        int rank = evalHand(hand);
        int strength = rank == HIGH_CARD ? 0 : 6 - rank;
        int[] v = sortedValues(hand);

        if (rank == PAIR)
            v = new int[] { 0, v[0] == v[1] ? v[2] : v[0], v[1] };
        if ((rank == STRAIGHT || rank == STRAIGHT_FLUSH) && v[0] == 2 && v[2] == 14)
            v = new int[] { 1, 2, 3 };

        return strength * 1_000_000 + v[2] * 10_000 + v[1] * 100 + v[0];
    }
}
