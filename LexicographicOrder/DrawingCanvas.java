import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.awt.Font;

public class DrawingCanvas extends Canvas {

	private BufferStrategy buffer;
	private Graphics g;
	int width;
	int height;
	int[] vals = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8 ,9};

	public DrawingCanvas(int width, int height) { //SETUP
		this.width = width;
		this.height = height;
		setSize(this.width, this.height);
	}

	public void draw() { //DRAW
		buffer = getBufferStrategy();
		if (buffer == null) {
			createBufferStrategy(2);
			return;
		}
		g = buffer.getDrawGraphics();

			//DRAW HERE!
			g.setColor(Color.BLACK); //Background
			g.fillRect(0, 0, this.width, this.height);

			printArray(vals);

			// STEP 1 of the algorithm
			// https://www.quora.com/How-would-you-explain-an-algorithm-that-generates-permutations-using-lexicographic-ordering
			int largestI = -1;
			for (int i = 0; i < vals.length - 1; i++) { //Don't go to the very end, or array out of bounds exception
				if (vals[i] < vals[i+1]) {
					largestI = i;
				}
			}
			if (largestI == -1) {
				System.out.println("Finished!");
				System.exit(0);
			}

			// STEP 2
			int largestJ = -1;
			for (int j = 0; j < vals.length; j++) {
				if (vals[largestI] < vals[j]) {
					largestJ = j;
				}
			}

			// STEP 3
			swap(vals, largestI, largestJ);

			// STEP 4: reverse from largestI + 1 to the end
			int[] endArray = copyPartArray(vals, largestI + 1, vals.length - 1);
			endArray = reverse(endArray);
			copyArrayValues(endArray, vals, largestI + 1);

			String s = "";
			for (int i = 0; i < vals.length; i++) {
				s += vals[i];
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("verdana", Font.PLAIN, 64));
			g.drawString(s, 20, this.height / 2);

		g.dispose();
		buffer.show();
	}

	public void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public int[] copyPartArray(int[] from, int start, int end) {
		int[] result = new int[end - start + 1];
		int index = 0;
		for (int i = start; i <= end; i++) {
			result[index] = from[i];
			index++;
		}
		return result;
	}

	public void copyArrayValues(int[] from, int[] to, int startTo) {
		int index = startTo;
		for (int i = 0; i < from.length; i++) {
			to[startTo] = from[i];
			startTo++;
		}
	}

	public void printArray(int[] array) {
		System.out.print("[");
		for (int i = 0; i < array.length; i++) {
			if (i == array.length -1 ) {
				System.out.print(array[i]);
			}
			else {
				System.out.print(array[i] + ", ");
			}
		}
		System.out.println("]");
	}

	public int[] reverse(int[] array) {
		int[] copy = new int[array.length];
		int index = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			copy[index] = array[i];
			index++;
		}
		return copy;
	}

}