package edu.uic.cs342.poker.model;

import java.util.ArrayList;

public class Dealer {
    private static final int RESHUFFLE_AT = 34;

    private final Deck deck = new Deck();
    private ArrayList<Card> hand = new ArrayList<>();

    public Dealer() {
        deck.shuffle();
    }

    public ArrayList<Card> dealHand() {
        if (deck.size() <= RESHUFFLE_AT) {
            deck.reset();
            deck.shuffle();
        }

        ArrayList<Card> dealt = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            dealt.add(deck.draw());

        return dealt;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
}
