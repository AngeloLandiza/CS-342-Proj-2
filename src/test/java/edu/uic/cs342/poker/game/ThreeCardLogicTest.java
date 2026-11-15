package edu.uic.cs342.poker.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.uic.cs342.poker.model.Card;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ThreeCardLogicTest {
    private static ArrayList<Card> hand(char s1, int v1, char s2, int v2, char s3, int v3) {
        return new ArrayList<>(List.of(new Card(s1, v1), new Card(s2, v2), new Card(s3, v3)));
    }

    @Test
    void evalHandHighCard() {
        assertEquals(0, ThreeCardLogic.evalHand(hand('C', 2, 'D', 7, 'H', 12)));
    }

    @Test
    void evalHandStraightFlush() {
        assertEquals(1, ThreeCardLogic.evalHand(hand('H', 9, 'H', 10, 'H', 11)));
    }

    @Test
    void evalHandThreeOfAKind() {
        assertEquals(2, ThreeCardLogic.evalHand(hand('C', 8, 'D', 8, 'S', 8)));
    }

    @Test
    void evalHandStraight() {
        assertEquals(3, ThreeCardLogic.evalHand(hand('C', 4, 'D', 5, 'S', 6)));
    }

    @Test
    void evalHandFlush() {
        assertEquals(4, ThreeCardLogic.evalHand(hand('S', 2, 'S', 9, 'S', 13)));
    }

    @Test
    void evalHandPair() {
        assertEquals(5, ThreeCardLogic.evalHand(hand('C', 13, 'D', 13, 'S', 4)));
    }

    @Test
    void evalHandAceLowStraight() {
        assertEquals(3, ThreeCardLogic.evalHand(hand('C', 14, 'D', 2, 'S', 3)));
    }

    @Test
    void pairPlusStraightFlushPays40To1() {
        assertEquals(200, ThreeCardLogic.evalPPWinnings(hand('H', 9, 'H', 10, 'H', 11), 5));
    }

    @Test
    void pairPlusThreeOfAKindPays30To1() {
        assertEquals(300, ThreeCardLogic.evalPPWinnings(hand('C', 8, 'D', 8, 'S', 8), 10));
    }

    @Test
    void pairPlusStraightPays6To1() {
        assertEquals(60, ThreeCardLogic.evalPPWinnings(hand('C', 4, 'D', 5, 'S', 6), 10));
    }

    @Test
    void pairPlusFlushPays3To1() {
        assertEquals(45, ThreeCardLogic.evalPPWinnings(hand('S', 2, 'S', 9, 'S', 13), 15));
    }

    @Test
    void pairPlusPairPays1To1() {
        assertEquals(20, ThreeCardLogic.evalPPWinnings(hand('C', 2, 'D', 2, 'S', 4), 20));
    }

    @Test
    void pairPlusHighCardPaysNothing() {
        assertEquals(0, ThreeCardLogic.evalPPWinnings(hand('C', 2, 'D', 7, 'H', 14), 25));
    }

    @Test
    void compareHandsPlayerPairBeatsDealerHighCard() {
        assertEquals(2, ThreeCardLogic.compareHands(hand('C', 14, 'D', 13, 'H', 9), hand('C', 2, 'D', 2, 'S', 3)));
    }

    @Test
    void compareHandsStraightBeatsFlush() {
        assertEquals(2, ThreeCardLogic.compareHands(hand('S', 2, 'S', 9, 'S', 14), hand('C', 4, 'D', 5, 'S', 6)));
    }

    @Test
    void compareHandsHighCardTieBrokenBySecondCard() {
        assertEquals(1, ThreeCardLogic.compareHands(hand('C', 14, 'D', 10, 'H', 4), hand('S', 14, 'H', 9, 'D', 8)));
    }

    @Test
    void compareHandsSamePairTieBrokenByKicker() {
        assertEquals(1, ThreeCardLogic.compareHands(hand('C', 9, 'D', 9, 'H', 13), hand('S', 9, 'H', 9, 'D', 12)));
    }

    @Test
    void compareHandsEqualValuesTie() {
        assertEquals(0, ThreeCardLogic.compareHands(hand('C', 14, 'D', 10, 'H', 4), hand('S', 14, 'H', 10, 'D', 4)));
    }

    @Test
    void compareHandsAceLowStraightLosesToHigherStraight() {
        assertEquals(1, ThreeCardLogic.compareHands(hand('C', 2, 'D', 3, 'H', 4), hand('S', 14, 'H', 2, 'D', 3)));
    }

    @Test
    void dealerQualifiesWithQueenHighButNotJackHigh() {
        assertTrue(ThreeCardLogic.dealerQualifies(hand('C', 12, 'D', 7, 'H', 2)));
        assertFalse(ThreeCardLogic.dealerQualifies(hand('C', 11, 'D', 9, 'H', 2)));
    }
}
