import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private SceneManager() { }

    /* Scene Constants */
    public static final int MAIN_MENU  = 0;
    public static final int GAME       = 1;
    public static final int SCOREBOARD = 2;

    /* Loaders */
    private static FXMLLoader mainMenuLoader;
    private static FXMLLoader gameLoader;
    private static FXMLLoader scoreboardLoader;

    /* Scenes */
    private static Scene mainMenu;
    private static Scene game;
    private static Scene scoreboard;

    private static Stage primaryStage;

    public static FXMLLoader getMainMenuLoader() {
        return mainMenuLoader;
    }

    public static FXMLLoader getGameLoader() {
        return gameLoader;
    }

    public static FXMLLoader getScoreboardLoader() {
        return scoreboardLoader;
    }

    public static Scene getMainMenu() {
        return mainMenu;
    }

    public static Scene getGame() {
        return game;
    }

    public static Scene getScoreboard() {
        return scoreboard;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void init(Stage primaryStage, FXMLLoader mainMenuLoader, FXMLLoader gameLoader, FXMLLoader scoreboardLoader) {

        SceneManager.mainMenuLoader = mainMenuLoader;
        SceneManager.gameLoader = gameLoader;
        SceneManager.scoreboardLoader = scoreboardLoader;

        primaryStage.setTitle("GUI Project 2: Puzzle");

        if (mainMenu == null && game == null && scoreboard == null) {
            SceneManager.primaryStage = primaryStage;

            try {
                mainMenu = new Scene(mainMenuLoader.load(), 600, 400);

                game = new Scene(gameLoader.load(), 600, 400);

                scoreboard = new Scene(scoreboardLoader.load(), 300, 300);
            } catch (IOException e) { e.printStackTrace(); }

        }

        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(500);

        primaryStage.setScene(mainMenu);

    }

    public static void changeScene(int sceneID) {

        switch (sceneID) {
            case MAIN_MENU:
                primaryStage.setScene(mainMenu);
                break;

            case GAME:
                primaryStage.setScene(game);
                break;

            case SCOREBOARD:
                primaryStage.setScene(scoreboard);
                break;

        }

    }
}
