public class FrameLoop implements Runnable{

    private Renderer renderer;

    private boolean running;

    private long nextStatTime;
    private int fps;
    private int frameCount;

    public FrameLoop(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void run() {
        running = true;
        frameCount = 0;
      
        while (running) {
            this.render();
            this.printStats();
        }
    }
    private void printStats() {
        if (System.currentTimeMillis() > nextStatTime) {
            System.out.println(String.format("FPS: %d", fps));
            fps = 0;
            nextStatTime = System.currentTimeMillis() + 1000;
        }
    }
    private void render() {
        renderer.render(frameCount);
        fps++;
        frameCount++;
    }
}
