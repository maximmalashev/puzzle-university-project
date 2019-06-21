import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("fxml/MainMenu.fxml"));
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("fxml/Game.fxml"));
        FXMLLoader scoreboardLoader = new FXMLLoader(getClass().getResource("fxml/Scoreboard.fxml"));

        SceneManager.init(
                primaryStage,
                mainMenuLoader,
                gameLoader,
                scoreboardLoader
        );

        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (Game.getCurrent() != null)
            Game.getCurrent().end();
    }
}
