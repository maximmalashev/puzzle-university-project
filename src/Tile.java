import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile extends ImageView {

    public Tile(Image image, int currentX, int currentY, int properX, int properY) {
        super(image);

        this.currentX = currentX;
        this.currentY = currentY;

        this.properX = properX;
        this.properY = properY;

    }

    private int currentX, currentY;
    private final int properX, properY;

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void move(int x, int y) {
        currentX = x;
        currentY = y;
    }

    public boolean checkCorrectLocation() {
        return currentX == properX && currentY == properY;
    }
}
