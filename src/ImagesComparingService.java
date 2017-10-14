import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by nikita on 13.10.2017.
 */
public class ImagesComparingService {

    private static final int XPARTS = 40;
    private static final int YPARTS = 40;
    private static final double TOLERANCE = 0.10;
    private static final int ACCEPTABLE_SIMILAR_PIXELS_IN_DIFF_AREA = 10;
    private BufferedImage firstImage;
    private BufferedImage secondImage;
    private BufferedImage resultImage;

    public ImagesComparingService(String firstImage, String secondImage) {
        this.firstImage = loadImage(firstImage);
        this.secondImage = loadImage(secondImage);
    }

    public void compareImagesAndDrawRectangles() {
        resultImage = new BufferedImage(secondImage.getWidth(), secondImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D newImage = resultImage.createGraphics();
        newImage.drawImage(secondImage, null, null);
        newImage.setColor(Color.red);
        int xPortions = secondImage.getWidth() / XPARTS;
        int yPortions = secondImage.getHeight() / YPARTS;
        for (int row = 0; row < YPARTS; row++) {
            for (int column = 0; column < XPARTS; column++) {
                int firstImageArray[][] = getPixelsArray(firstImage.getSubimage(column * xPortions, row * yPortions, xPortions - 1, yPortions - 1));
                int secondImageArray[][] = getPixelsArray(secondImage.getSubimage(column * xPortions, row * yPortions, xPortions - 1, yPortions - 1));
                drawRectangle(firstImageArray, secondImageArray, column, row, xPortions, yPortions, newImage);
            }
        }
    }

    private void drawRectangle(int [][] firstImageArray, int [][] secondImageArray, int column, int row, int xPortions, int yPortions, Graphics2D newImage) {
        boolean isEquals = true;
        int firstX = 0;
        for (int i = 0; i < firstImageArray.length; i++) {
            for (int j = 0; j < firstImageArray[0].length; j++) {
                int difference = Math.abs(firstImageArray[i][j] - secondImageArray[i][j]);
                if (difference > TOLERANCE) {
                    if (isEquals) {
                        isEquals = false;
                        firstX = (column * xPortions) + j;
                    }
                }
                if (difference == 0 && !isEquals && (column * xPortions) - firstX > ACCEPTABLE_SIMILAR_PIXELS_IN_DIFF_AREA) {
                    isEquals = true;
                    newImage.drawRect(firstX, row * yPortions, (column * xPortions) - firstX, yPortions);
                }
            }
        }
    }

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

    private BufferedImage loadImage(String imageName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void saveNewImage(String imageName) {
        File newImage = new File(imageName);
        try {
            ImageIO.write(resultImage, "jpg", newImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
