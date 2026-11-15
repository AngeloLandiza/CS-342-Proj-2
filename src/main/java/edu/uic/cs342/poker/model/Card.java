package edu.uic.cs342.poker.model;

public class Card {
    private final char suit;
    private final int value;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public char getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        String rank = switch (value) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(value);
        };
        String symbol = switch (suit) {
            case 'C' -> "♣";
            case 'D' -> "♦";
            case 'H' -> "♥";
            default -> "♠";
        };
        return rank + symbol;
    }
}
