import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class DrawingCanvas extends Canvas {

	private BufferStrategy buffer;
	private Graphics g;
	int width;
	int height;
	int totalCities = 5;
	JVector[] cities;
	final int CITY_DIAMETER = 8;
	double recordDistance;
	JVector[] bestEver;

	public DrawingCanvas(int width, int height) { //SETUP
		this.width = width;
		this.height = height;
		setSize(this.width, this.height);
		cities = new JVector[totalCities];
		for (int i = 0; i < totalCities; i++) {
			JVector v = new JVector(Math.random()*this.width, Math.random()*this.height);
			cities[i] = v;
		}

		double d = calcDistance(cities);
		recordDistance = d;

		bestEver = new JVector[totalCities];
		copyArrayValues(cities, bestEver);
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
			for (int i = 0; i < totalCities - 1; i++) { //Draw lines between them, don't draw a line from the ending one
				//Draw it from the center adding the radius to the position
				g.drawLine(cities[i].intX() + CITY_DIAMETER/2, cities[i].intY() + CITY_DIAMETER/2, cities[i+1].intX() + CITY_DIAMETER/2, cities[i+1].intY() + CITY_DIAMETER/2);

			}

			g.setColor(new Color(255, 0, 255));
			for (int i = 0; i < totalCities - 1; i++) { //Draw the best path
				g.drawLine(bestEver[i].intX() + CITY_DIAMETER/2, bestEver[i].intY() + CITY_DIAMETER/2, bestEver[i+1].intX() + CITY_DIAMETER/2, bestEver[i+1].intY() + CITY_DIAMETER/2);

			}

			int i = (int) (Math.random()*cities.length);
			int j = (int) (Math.random()*cities.length);
			swap(cities, i, j);

			double d = calcDistance(cities);
			if (d < recordDistance) {
				recordDistance = d;
				copyArrayValues(cities, bestEver);
				//System.out.println(recordDistance);
			}

		g.dispose();
		buffer.show();
	}

	public void swap(JVector[] array, int i, int j) {
		JVector temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public double calcDistance(JVector[] points) {
		double sum = 0;
		for (int i = 0; i < points.length - 1; i++) {
			double distance = JVector.dist2Vecs(points[i], points[i+1]);
			sum += distance;
		}
		return sum;
	}

	public void copyArrayValues(JVector[] from, JVector[] to) {
		for (int i = 0; i < from.length; i++) {
			to[i] = from[i];
		}
	}

}