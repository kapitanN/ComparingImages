import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by nikita on 13.10.2017.
 */
public class ImageComparisonService {

    /**
     * The TOLERANCE means sensitivity during comparison two pixels
     */
    private static final double TOLERANCE = 0.1;
    private BufferedImage firstImage;
    private BufferedImage secondImage;
    private BufferedImage resultImage;
    private int[][] inequalityMatrix;

    public ImageComparisonService(String firstImage, String secondImage) throws IOException {
        this.firstImage = loadImage(firstImage);
        this.secondImage = loadImage(secondImage);
        inequalityMatrix = new int[this.secondImage.getHeight()][this.secondImage.getWidth()];
    }

    /**
     * Draws rectangles by points
     */
    public void drawRectangles() {
        resultImage = new BufferedImage(secondImage.getWidth(), secondImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D newImage = resultImage.createGraphics();
        newImage.drawImage(secondImage, null, null);
        newImage.setColor(Color.red);
        Rectangle rectangle = new Rectangle();
        setInequalityMatrix();
        for (int y = 0; y < inequalityMatrix.length; y++) {
            for (int x = 0; x < inequalityMatrix[0].length; x++) {
                if (inequalityMatrix[y][x] == 1) {
                    if (x < rectangle.getMinX()) rectangle.setMinX(x);
                    if (x > rectangle.getMaxX()) rectangle.setMaxX(x);

                    if (y < rectangle.getMinY()) rectangle.setMinY(y);
                    if (y > rectangle.getMaxY()) rectangle.setMaxY(y);

                    if ((checkLastYPixel(inequalityMatrix, y, x)) && (checkLastXPixel(inequalityMatrix, y, x))) {
                        newImage.drawRect(rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
                        rectangle = new Rectangle();
                    }
                }
            }
        }
    }

    /**
     * Sets values in inequalityMatrix. If two same pixels in
     * two images is equal, that set 0, otherwise set 1
     */
    private void setInequalityMatrix() {
        int firstImageArray[][] = getPixelsArray(firstImage);
        int secondImageArray[][] = getPixelsArray(secondImage);
        for (int y = 0; y < secondImage.getHeight(); y++) {
            for (int x = 0; x < secondImage.getWidth(); x++) {
                double difference = Math.abs(firstImageArray[y][x] - secondImageArray[y][x]) / 100;
                inequalityMatrix[y][x] = difference > TOLERANCE ? 1 : 0;
            }
        }
    }

    /**
     * Checks that pixel is the last unequal by Y coordinate
     *
     * @param matrix the matrix of inequality
     * @param y      the Y coordinate of pixel
     * @param x      the X coordinate of pixel
     * @return {@code true} if this pixel is the last, {@code false} otherwise
     */
    private boolean checkLastYPixel(int[][] matrix, int y, int x) {
        for (int i = 1; i < 10; i++) {
            if (y + i < matrix.length) {
                if (matrix[y + i][x] == 1) {
                    return false;
                }
            }
            if (y + i == matrix.length) {
                return true;
            }
        }
        return true;
    }

    /**
     * Checks that pixel is the last unequal by X coordinate
     *
     * @param matrix the matrix of inequality
     * @param y      the Y coordinate of pixel
     * @param x      the X coordinate of pixel
     * @return {@code true} if this pixel is the last, {@code false} otherwise
     */
    private boolean checkLastXPixel(int[][] matrix, int y, int x) {
        for (int i = 1; i < 10; i++) {
            if (x + 1 < matrix[0].length) {
                if ((matrix[y][x + i]) == 1) {
                    return false;
                }
                if (x + i == matrix.length) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Gets image pixels array
     *
     * @param image image that need convert to pixels array
     * @return image pixels array
     */
    private int[][] getPixelsArray(BufferedImage image) {
        int rows = image.getHeight();
        int columns = image.getWidth();

        int[][] pixelsArray = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                pixelsArray[i][j] = image.getRGB(j, i);
            }
        }
        return pixelsArray;
    }

    /**
     * Loads image from disk
     *
     * @param imageName image file name
     * @return {@code BufferedImage} object of loaded image
     */
    private BufferedImage loadImage(String imageName) throws IOException {
        BufferedImage image = null;
        image = ImageIO.read(new File(imageName));
        return image;
    }

    /**
     * Saves creating images
     *
     * @param imageName name for the new image
     */
    public void saveNewImage(String imageName) throws IOException {
        File newImage = new File(imageName);
        ImageIO.write(resultImage, "jpg", newImage);
    }
}
