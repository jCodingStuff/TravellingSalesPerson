import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.awt.Font;
import java.text.DecimalFormat;

public class DrawingCanvas extends Canvas {

	private BufferStrategy buffer;
	private Graphics g;
	int width;
	int height;
	int totalCities = 6;
	JVector[] cities;

	int[] order;
	double totalPermutations;
	int count = 0;

	final int CITY_DIAMETER = 8;

	double recordDistance;
	int[] bestEver;

	DecimalFormat df;

	public DrawingCanvas(int width, int height) { //SETUP
		this.width = width;
		this.height = height;
		setSize(this.width, this.height);
		cities = new JVector[totalCities];
		order = new int[totalCities];
		for (int i = 0; i < totalCities; i++) {
			JVector v = new JVector(Math.random()*this.width, Math.random()*this.height/2);
			cities[i] = v;
			order[i] = i;
		}

		double d = calcDistance(cities, order);
		recordDistance = d;

		bestEver = new int[totalCities];
		copyArrayValues(order, bestEver, 0);

		totalPermutations = factorial(totalCities);
		System.out.println(totalPermutations);

		df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
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

			g.setColor(Color.WHITE); //Draw the cities
			for (int i = 0; i < totalCities; i++) { //Could be cities.length
				g.fillOval(cities[i].intX(), cities[i].intY(), CITY_DIAMETER, CITY_DIAMETER);
			}

			g.setColor(new Color(255, 0, 255)); //Draw the best ever
			for (int i = 0; i < bestEver.length - 1; i++) { //Draw lines between them, don't draw a line from the ending one
				int n = bestEver[i];
				int follow = bestEver[i+1];
				g.drawLine(cities[n].intX() + CITY_DIAMETER/2, cities[n].intY() + CITY_DIAMETER/2, cities[follow].intX() + CITY_DIAMETER/2, cities[follow].intY() + CITY_DIAMETER/2);

			}

			//Every checking
			g.setColor(Color.WHITE);
			for (int i = 0; i < totalCities; i++) { //Could be cities.length
				g.fillOval(cities[i].intX(), cities[i].intY() + this.height/2, CITY_DIAMETER, CITY_DIAMETER);
			}
			for (int i = 0; i < order.length - 1; i++) { //Draw lines between them, don't draw a line from the ending one
				int n = order[i]; //The citites I want to link, relating order and cities
				int follow = order[i+1];
				//Draw it from the center adding the radius to the position
				g.drawLine(cities[n].intX() + CITY_DIAMETER/2, cities[n].intY() + CITY_DIAMETER/2 + this.height/2, cities[follow].intX() + CITY_DIAMETER/2, cities[follow].intY() + CITY_DIAMETER/2 + this.height/2);

			}

			double d = calcDistance(cities, order);
			if (d < recordDistance) {
				recordDistance = d;
				copyArrayValues(order, bestEver, 0);
				System.out.println(recordDistance);
			}

			/*
			String s = "";
			for (int i = 0; i < order.length; i++) {
				s += order[i];
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("verdana", Font.PLAIN, 64));
			g.drawString(s, 20, this.height - 50);
			*/

			double percent = 100 * (count / totalPermutations);
			g.setColor(Color.WHITE);
			g.setFont(new Font("verdana", Font.PLAIN, 32));
			g.drawString(df.format(percent) + "% completed!", 20, this.height - 20);

			nextOrder();

		g.dispose();
		buffer.show();
	}

	public void swap(JVector[] array, int i, int j) {
		JVector temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public double calcDistance(JVector[] points, int[] order) {
		double sum = 0;
		for (int i = 0; i < order.length - 1; i++) {
			int cityAIndex = order[i];
			JVector cityA = points[cityAIndex]; //Select the cities in order
			int cityBIndex = order[i+1];
			JVector cityB = points[cityBIndex]; //Select the cities in order
			double distance = JVector.dist2Vecs(cityA, cityB);
			sum += distance;
		}
		return sum;
	}

	public void copyArrayValues(JVector[] from, JVector[] to) {
		for (int i = 0; i < from.length; i++) {
			to[i] = from[i];
		}
	}

	public void copyArrayValues(int[] from, int[] to, int startTo) {
		int index = startTo;
		for (int i = 0; i < from.length; i++) {
			to[startTo] = from[i];
			startTo++;
		}
	}

	public void nextOrder() {
		count++;
		// STEP 1 of the algorithm
		// https://www.quora.com/How-would-you-explain-an-algorithm-that-generates-permutations-using-lexicographic-ordering
		int largestI = -1;
		for (int i = 0; i < order.length - 1; i++) { //Don't go to the very end, or array out of bounds exception
			if (order[i] < order[i+1]) {
				largestI = i;
			}
		}
		if (largestI == -1) {
			//System.out.println("Finished!");
			//printArray(bestEver);
			count = (int) totalPermutations;
			return; //Stop the animation
		}

		// STEP 2
		int largestJ = -1;
		for (int j = 0; j < order.length; j++) {
			if (order[largestI] < order[j]) {
				largestJ = j;
			}
		}

		// STEP 3
		swap(order, largestI, largestJ);

		// STEP 4: reverse from largestI + 1 to the end
		int[] endArray = copyPartArray(order, largestI + 1, order.length - 1);
		endArray = reverse(endArray);
		copyArrayValues(endArray, order, largestI + 1);
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

	public double factorial(double n) { //A recursive function to calculate factorials
		if (n == 1) {
			return 1;
		}
		else {
			return n*factorial(n-1);
		}
	}

}