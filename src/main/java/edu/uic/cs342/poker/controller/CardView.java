package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.model.Card;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CardView extends Label {
    public CardView(Card card) {
        getStyleClass().add("card");
        setCard(card);
    }

    public void setCard(Card card) {
        getStyleClass().removeAll("card-back", "card-red");
        if (card == null) {
            setText("?");
            getStyleClass().add("card-back");
        } else {
            setText(card.toString());
            if (card.getSuit() == 'D' || card.getSuit() == 'H')
                getStyleClass().add("card-red");
        }
    }

    public void flipTo(Card card) {
        ScaleTransition close = new ScaleTransition(Duration.millis(200), this);
        close.setToX(0);
        close.setOnFinished(event -> setCard(card));
        ScaleTransition open = new ScaleTransition(Duration.millis(200), this);
        open.setToX(1);
        new SequentialTransition(close, open).play();
    }
}
