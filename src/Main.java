
public class Main {

    public static void main(String[] args) {
        ImagesComparingService service = new ImagesComparingService("image1.jpg", "image2.jpg");
        service.compareImages();
        service.saveNewImage( "myImage.jpg");
    }
}
