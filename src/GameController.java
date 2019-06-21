import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* Check for changes in size of the window */
        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> resize();
        rootPane.heightProperty().addListener(resizeListener);
        rootPane.widthProperty().addListener(resizeListener);

    }

    @FXML private BorderPane rootPane;
    @FXML private ToolBar toolbar;
    @FXML private Label stopwatchLbl;
    private GridPane gridPane;

    private ArrayList<Tile> tiles;
    private AnchorPane[][] grid;
    int dimension = -1;

    int seconds = 0;
    int minutes = 0;

    public void prepare() {

        dimension = Game.getCurrent().getDifficulty();
        gridPane = new GridPane();
        gridPane.setPrefSize(SceneManager.getPrimaryStage().getWidth(), SceneManager.getPrimaryStage().getHeight());

        initGrid();
        initImages();

        SceneManager.getPrimaryStage().setHeight(SceneManager.getPrimaryStage().getHeight() + 1);
        SceneManager.getPrimaryStage().setWidth(SceneManager.getPrimaryStage().getWidth() + 1);

        rootPane.setCenter(gridPane);

        Task<Void> stopwatch = new Task<Void>() {
            @Override
            protected Void call() {
                long last = System.currentTimeMillis();
                long elapsed = 0;
                while (Game.getCurrent().isRunning()) {
                    long current = System.currentTimeMillis();
                    elapsed += current - last;

                    if (elapsed >= 1000) {
                        seconds++;
                        elapsed = 0;

                        if (seconds >= 60) {
                            minutes++;
                            seconds = 0;
                        }

                        Platform.runLater(() -> stopwatchLbl.setText(minutes + ":" + (seconds < 10 ? "0" : "") + seconds));
                    }
                    last = current;

                }
                return null;
            }
        };

        Thread thread = new Thread(stopwatch);
        thread.setDaemon(true);
        thread.start();

    }

    private void resize() {

        if (gridPane != null) {
            gridPane.setPrefSize(rootPane.getWidth(), rootPane.getHeight() - 40);

            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (grid[i][j] != null) {
                        grid[i][j].setPrefSize(gridPane.getPrefWidth() / dimension, gridPane.getPrefHeight() / dimension);
                    }
                }
            }
        }

    }

    private void initGrid() {

        gridPane.setPrefSize(rootPane.getWidth(), rootPane.getHeight() - 40);
        grid = new AnchorPane[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (!(i == dimension - 1 && j == dimension - 1)) {

                    grid[i][j] = new AnchorPane();
                    grid[i][j].setPrefSize(rootPane.getWidth() / dimension, rootPane.getHeight() / dimension);

                    gridPane.add(grid[i][j], i, j);
                }
            }
        }
    }

    private void initImages() {

        BufferedImage bi = Game.getCurrent().getImage();
        ArrayList<BufferedImage> images = new ArrayList<>();
        ArrayList<Coords> coords = new ArrayList<>();
        tiles = new ArrayList<>();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grid[i][j] != null) {
                    BufferedImage subimg = bi.getSubimage(
                            bi.getWidth() / dimension * i,
                            bi.getHeight() / dimension * j,
                            bi.getWidth() / dimension,
                            bi.getHeight() / dimension
                    );

                    images.add(subimg);
                    coords.add(new Coords(i, j));
                }
            }
        }

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grid[i][j] != null) {
                    int el = (int) (Math.random() * (double) images.size());
                    //Tile tile = new Tile(SwingFXUtils.toFXImage(images.get(el), null), i, j, coords.get(el).x, coords.get(el).y);

                    /* Uncomment this and comment the above line to win immediately */
                    Tile tile = new Tile(SwingFXUtils.toFXImage(images.get(el), null), i, j, i, j);

                    images.remove(el);
                    coords.remove(el);

                    grid[i][j].getChildren().add(tile);
                    tile.fitHeightProperty().bind(grid[i][j].prefHeightProperty());
                    tile.fitWidthProperty().bind(grid[i][j].prefWidthProperty());

                    tile.setOnMousePressed(event -> moveTile(tile));

                    tiles.add(tile);

                }
            }
        }


    }

    @FXML
    private void mainMenuBtn() {

        Game.getCurrent().end();
        reset();
        SceneManager.changeScene(SceneManager.MAIN_MENU);

    }

    private void reset() {
        stopwatchLbl.setText("0:00");

        seconds = 0;
        minutes = 0;
    }

    private void moveTile(Tile tile) {

        // coordinates of the tile that was pressed
        int currentX = tile.getCurrentX();
        int currentY = tile.getCurrentY();

        // coordinates of the empty tile
        int emptyX = 0;
        int emptyY = 0;

        // find the empty tile in the scene
        OUTER: for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grid[i][j] == null) {
                    emptyX = i;
                    emptyY = j;
                    break OUTER;
                }
            }
        }

        // check for adjacency
        if (
                (emptyX == currentX - 1 && emptyY == currentY) ||
                (emptyX == currentX + 1 && emptyY == currentY) ||
                (emptyX == currentX && emptyY == currentY - 1) ||
                (emptyX == currentX && emptyY == currentY + 1)
        ) {
            // create a pressed tile in a place of an empty tile and delete it from its previous position
            gridPane.getChildren().remove(grid[currentX][currentY]);
            gridPane.add(grid[currentX][currentY], emptyX, emptyY);

            // update the grid array and tile coords
            grid[emptyX][emptyY] = grid[currentX][currentY];
            grid[currentX][currentY] = null;
            tile.move(emptyX, emptyY);
        }

        // check for puzzle correctness
        for (Tile t : tiles) {
            if (!t.checkCorrectLocation()) return;
        }

        // if the puzzle is correct, show the win game message
        winGameMessage();

    }

    public void winGameMessage() {

        Game.getCurrent().end();

        TextInputDialog nameInputDialog = new TextInputDialog();
        nameInputDialog.setTitle("Congratulations!");
        nameInputDialog.setHeaderText("You won!");
        nameInputDialog.setContentText("Please, enter your name:");

        Optional<String> result = nameInputDialog.showAndWait();
        result.ifPresent(name -> {

            FileWriter fw = null;
            try {
                fw = new FileWriter("src/scoreboard.txt", true);
            } catch (IOException e) { e.printStackTrace(); }

            StringBuilder sb = new StringBuilder();

            // date & current time
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = new Date();
            sb.append(dateFormat.format(date) + " ");

            // time of solving
            sb.append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds + " ");

            // name
            sb.append(name + System.lineSeparator());

            try {
                fw.write(sb.toString());
                fw.close();
            } catch (IOException e) { e.printStackTrace(); }

        });

        reset();
        SceneManager.changeScene(SceneManager.MAIN_MENU);

    }

    private static class Coords {
        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x, y;
    }
}
