package edu.uic.cs342.poker.game;

public record RoundResult(String outcome, int ante, int play, int pairPlus) {
    public int total() {
        return ante + play + pairPlus;
    }
}
