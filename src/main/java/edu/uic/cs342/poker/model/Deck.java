package edu.uic.cs342.poker.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private static final char[] SUITS = { 'C', 'D', 'H', 'S' };

    private final ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();

        for (char suit : SUITS)
            for (int value = 2; value <= 14; value++)
                cards.add(new Card(suit, value));

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.remove(cards.size() - 1);
    }

    public int size() {
        return cards.size();
    }

    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards);
    }
}
