import java.awt.image.BufferedImage;
public class Renderer {
    private Display display;

    private int screen_width;
    private int screen_height;

    private Camera cam;
    private int[] cam_center;
    private int cam_distance;
    private double[] cam_rotation;

    private Cube cube;
    private int[] cube_center;
    private int cube_size;
    private double[] cube_rotation;

    private BufferedImage bImg;

    public Renderer(int width, int height) {
        this.screen_width = width;
        this.screen_height = height;

        cam_center = new int[]{-screen_width/2,-screen_height/2,-1000};
        cam_distance = 1000;
        cam_rotation = new double[] {0,0,0};

        cube_center = new int[]{0,0,0};
        cube_size = 200;
        cube_rotation = new double[] {0,0,0};

        cam = new Camera(cam_center, screen_width, screen_height, cam_distance, cam_rotation);
        cube = new Cube(screen_width, screen_height, cube_center, cube_size, cube_rotation);

        display = new Display(width, height);
    }
    public void render(int frameCount) {
        bImg = new BufferedImage(screen_width, screen_height, BufferedImage.TYPE_INT_RGB);
        cube.setRotation(new double[] {0,(frameCount%72)*5,(frameCount%72)*5});
        cam.print(bImg,new RenderableObject[] {cube});
        display.render(this);
    }
    public BufferedImage getFrame() {
        return bImg;
    }
}
