public class Run {

	private static DrawingCanvas canvas;
	private static Window frame;
	private static int RPS = 0;
	private static int FPS = 0;

	public static void main(String[] args) {
		startProgram();
	}

	public static void startProgram() {
		createWindow(800, 800, "Travelling Salesperson");
		iterateMainLoop();
	}

	public static void refresh() {
		RPS++;
		canvas.draw();
	}

	public static void draw() {
		FPS++;
	}

	public static void createWindow(int width, int height, String title) {
		canvas = new DrawingCanvas(width, height);
		frame = new Window(title, canvas);
	}

	//Create the animation
	public static void iterateMainLoop() {
		final int NS_PER_SECOND = 1000000000;
		final int RPS_OBJECTIVE = 60;
		final int NS_PER_FRAME = NS_PER_SECOND / RPS_OBJECTIVE;

		long referenceRefreshTime = System.nanoTime();
		long referenceTimeCounter = System.nanoTime();

		double delta = 0;
		double timeWithoutProcessing;

		//Loop until the window is closed
		while (true) {
			long initialTime = System.nanoTime();
			timeWithoutProcessing = initialTime - referenceRefreshTime;
			referenceRefreshTime = initialTime;
			delta += timeWithoutProcessing / NS_PER_FRAME;

			while (delta >= 1) {
				delta--;
				refresh();
				draw();
			}

			if (System.nanoTime() - referenceTimeCounter >= NS_PER_SECOND) {
				System.out.println("RPS = " + RPS + ", FPS = " + FPS);
				referenceTimeCounter = System.nanoTime();
				RPS = 0;
				FPS = 0;
			}
		}
	}

}