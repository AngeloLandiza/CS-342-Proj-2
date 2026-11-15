package edu.uic.cs342.poker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeckTest {
    private static Set<String> names(List<Card> cards) {
        Set<String> names = new HashSet<>();
        for (Card card : cards)
            names.add(card.toString());
        return names;
    }

    @Test
    void newDeckHas52Cards() {
        assertEquals(52, new Deck().size());
    }

    @Test
    void newDeckHasNoDuplicates() {
        assertEquals(52, names(new Deck().getCards()).size());
    }

    @Test
    void cardValuesRangeFromTwoToAce() {
        for (Card card : new Deck().getCards())
            assertTrue(card.getValue() >= 2 && card.getValue() <= 14);
    }

    @Test
    void drawRemovesTheCardFromTheDeck() {
        Deck deck = new Deck();
        Card drawn = deck.draw();
        assertEquals(51, deck.size());
        assertFalse(names(deck.getCards()).contains(drawn.toString()));
    }

    @Test
    void shuffleKeepsTheSame52Cards() {
        Deck deck = new Deck();
        Set<String> before = names(deck.getCards());
        deck.shuffle();
        assertEquals(before, names(deck.getCards()));
    }

    @Test
    void resetRestoresAll52Cards() {
        Deck deck = new Deck();
        for (int i = 0; i < 20; i++)
            deck.draw();
        deck.reset();
        assertEquals(52, names(deck.getCards()).size());
    }
}
