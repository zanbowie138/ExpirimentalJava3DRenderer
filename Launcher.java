public class Launcher {
    public static void main(String[] args) {
        new Thread(new FrameLoop(new Renderer(800,600))).start();
        //Display display = new Display(800, 600);
    }
}