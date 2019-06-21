import java.awt.image.BufferedImage;

public class Game {

    public static final int EASY      = 3;
    public static final int MEDIUM    = 4;
    public static final int DIFFICULT = 5;

    public Game(int difficulty, BufferedImage image) {

        this.image = image;
        this.difficulty = difficulty;

        current = this;

    }

    private static Game current;

    private BufferedImage image;
    private int difficulty;
    private boolean running = true;

    public static Game getCurrent() {
        return current;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean isRunning() {
        return running;
    }

    public void end() {
        running = false;
    }
}
