package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class WelcomeController {
    @FXML
    private void handleStartButton() {
        Main.startGame();
    }

    @FXML
    private void handleRulesButton() {
        Main.showRules();
    }

    @FXML
    private void handleThemeButton() {
        Main.toggleTheme();
    }

    @FXML
    private void handleExitButton() {
        Platform.exit();
    }
}
