package edu.uic.cs342.poker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DealerTest {
    @Test
    void dealHandReturnsThreeCards() {
        assertEquals(3, new Dealer().dealHand().size());
    }

    @Test
    void dealHandRemovesThreeCardsFromTheDeck() {
        Dealer dealer = new Dealer();
        dealer.dealHand();
        assertEquals(49, dealer.getDeck().size());
    }

    @Test
    void handsInOneRoundShareNoCards() {
        Dealer dealer = new Dealer();
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            for (Card card : dealer.dealHand())
                seen.add(card.toString());
        }
        assertEquals(9, seen.size());
    }

    @Test
    void dealerReshufflesAFullDeckAt34Cards() {
        Dealer dealer = new Dealer();
        for (int i = 0; i < 7; i++)
            dealer.dealHand();
        assertEquals(49, dealer.getDeck().size());
    }
}
