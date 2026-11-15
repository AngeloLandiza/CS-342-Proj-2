package edu.uic.cs342.poker;

import edu.uic.cs342.poker.game.GameService;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private static final String RESOURCES = "/edu/uic/cs342/poker/";

    private static Stage stage;
    private static Stage rulesStage;
    private static GameService game;
    private static boolean darkTheme;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        rulesStage = new Stage();
        rulesStage.initOwner(stage);
        rulesStage.setTitle("How to Play");
        show("welcome");
        stage.setTitle("CS 342 - Three Card Poker");
        stage.show();
    }

    public static void show(String screen) {
        Scene scene = loadScene(screen, 1000, 680);
        stage.setScene(scene);
        FadeTransition fade = new FadeTransition(Duration.millis(300), scene.getRoot());
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public static void showRules() {
        rulesStage.setScene(loadScene("rules", 860, 340));
        rulesStage.show();
    }

    public static void closeRules() {
        rulesStage.close();
    }

    public static GameService getGame() {
        return game;
    }

    public static void startGame() {
        game = new GameService();

        show("game");
    }

    public static void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme(stage.getScene());
    }

    public static String money(int amount) {
        if (amount < 0)
            return "-$" + -amount;

        return "$" + amount;
    }

    private static Scene loadScene(String screen, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource(RESOURCES + "fxml/" + screen + ".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            applyTheme(scene);

            return scene;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load screen " + screen, e);
        }
    }

    private static void applyTheme(Scene scene) {
        String theme = darkTheme ? "dark.css" : "classic.css";

        scene.getStylesheets().clear();
        scene.getStylesheets().add(
                Main.class.getResource(RESOURCES + "css/style.css").toExternalForm());
        scene.getStylesheets().add(
                Main.class.getResource(RESOURCES + "css/" + theme).toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
