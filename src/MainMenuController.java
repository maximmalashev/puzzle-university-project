import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initDifficultyChb();
        initImageChb();

        /* Check for changes in size of the window */
        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> center();
        rootPane.heightProperty().addListener(resizeListener);
        rootPane.widthProperty().addListener(resizeListener);

    }

    @FXML public Pane rootPane;
    @FXML public Pane mainMenu;
    @FXML public ChoiceBox difficultyChb;
    @FXML public ChoiceBox imageChb;

    private void center() {

        if (SceneManager.getMainMenu() != null)
            mainMenu.relocate((rootPane.getWidth() - mainMenu.getWidth()) / 2, (rootPane.getHeight() - mainMenu.getHeight()) / 2);

    }

    private void initDifficultyChb() {

        difficultyChb.getItems().addAll("3x3", "4x4", "5x5");
        difficultyChb.setValue(difficultyChb.getItems().get(0));

    }

    private void initImageChb() {

        File f = new File("src/images");
        ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));

        imageChb.getItems().addAll(names);
        imageChb.setValue(imageChb.getItems().get(0));
    }

    @FXML
    private void scoreboardBtn() {

        ScoreboardController sc = SceneManager.getScoreboardLoader().getController();
        sc.prepare();
        SceneManager.changeScene(SceneManager.SCOREBOARD);
    }

    @FXML
    private void newGameBtn() {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File("src/images/" + imageChb.getValue()));
        } catch (IOException e) { e.printStackTrace(); }

        int difficulty = Game.EASY;

        if (difficultyChb.getValue() == "4x4")
            difficulty = Game.MEDIUM;
        else if (difficultyChb.getValue() == "5x5")
            difficulty = Game.DIFFICULT;

        new Game(difficulty, image);

        GameController gc = SceneManager.getGameLoader().getController();
        gc.prepare();

        SceneManager.changeScene(SceneManager.GAME);

    }

}
