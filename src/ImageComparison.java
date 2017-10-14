import java.io.IOException;

public class ImageComparison {

    public static void main(String[] args) {
        ImageComparisonService service = null;
        try {
            service = new ImageComparisonService("image1.jpg", "image2.jpg");
        } catch (IOException e) {
            System.out.println("Image cannot be uploaded. Try write image name again. " + e.getMessage());
        }
        service.drawRectangles();
        try {
            service.saveNewImage( "result.jpg");
        } catch (IOException e) {
            System.out.println("Image cannot be saved. " + e.getMessage());
        }
    }
}
