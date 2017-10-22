import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DrawingCanvas extends Canvas {

	private BufferStrategy buffer;
	private Graphics g;
	int width;
	int height;

	Genetic algorithm;

	int totalCities = 12;
	JVector[] cities;

	int[] order;
	int popSize = 500;
	ArrayList<int[]> population;
	ArrayList<Double> fitness;

	final int CITY_DIAMETER = 8;

	double recordDistance = Double.MAX_VALUE;
	int[] bestEver;
	int[] currentBest;

	DecimalFormat df;

	Random generator;

	public DrawingCanvas(int width, int height) { //SETUP
		this.width = width;
		this.height = height;
		setSize(this.width, this.height);

		order = new int[totalCities];
		cities = new JVector[totalCities];
		for (int i = 0; i < totalCities; i++) {
			JVector v = new JVector(Math.random()*this.width, Math.random()*this.height/2);
			cities[i] = v;
			order[i] = i;
		}

		population = new ArrayList<int[]>();
		for (int i = 0; i < popSize; i++) {
			population.add(copyArray(order));
			shuffle(population.get(i), 300);
		}

		fitness = new ArrayList<Double>();

		this.algorithm = new Genetic(this); //Link the Genetic Algorithm

		/*
		for (int i = 0; i < 100; i++) {
			printArray(population.get(i));
		}
		*/

		/* CALCULATING THE FITNESS
		fitness = new ArrayList<Double>();
		for (int i = 0; i < population.size(); i++) {
			double d = calcDistance(cities, population.get(i));
			if (d < recordDistance) {
				recordDistance = d;
				bestEver = population.get(i);
			}
			fitness.add(1/(d));
		}
		*/

		//df = new DecimalFormat();
		//df.setMaximumFractionDigits(2);
	}

	public void draw() { //DRAW
		buffer = getBufferStrategy();
		if (buffer == null) {
			createBufferStrategy(2);
			return;
		}
		g = buffer.getDrawGraphics();

			//DRAW HERE!
			g.setColor(new Color(51, 51, 51)); //Background
			g.fillRect(0, 0, this.width, this.height);

			//Genetic Algorithm
			algorithm.calculateFitness();
			//System.out.println(fitness);
			algorithm.normalizeFitness();
			//System.out.println(fitness);
			algorithm.nextGeneration();
			
			g.setColor(Color.WHITE); //Draw the cities
			for (int i = 0; i < totalCities; i++) { //Could be cities.length
				g.drawOval(cities[i].intX(), cities[i].intY(), CITY_DIAMETER, CITY_DIAMETER);
			}

			g.setColor(new Color(255, 0, 255)); //Draw the best ever
			for (int i = 0; i < bestEver.length - 1; i++) { //Draw lines between them, don't draw a line from the ending one
				int n = bestEver[i];
				int follow = bestEver[i+1];
				g.drawLine(cities[n].intX() + CITY_DIAMETER/2, cities[n].intY() + CITY_DIAMETER/2, cities[follow].intX() + CITY_DIAMETER/2, cities[follow].intY() + CITY_DIAMETER/2);

			}

			g.setColor(Color.WHITE); //Draw the cities bottom
			for (int i = 0; i < totalCities; i++) {
				g.drawOval(cities[i].intX(), cities[i].intY() + this.height/2, CITY_DIAMETER, CITY_DIAMETER);
			}

			//Draw the current Best
			for (int i = 0; i < currentBest.length - 1; i++) {
				int n = currentBest[i];
				int follow = currentBest[i+1];
				g.drawLine(cities[n].intX() + CITY_DIAMETER/2, cities[n].intY() + CITY_DIAMETER/2 + this.height/2, cities[follow].intX() + CITY_DIAMETER/2, cities[follow].intY() + CITY_DIAMETER/2 + this.height/2);

			}

		g.dispose();
		buffer.show();
	}

	public void shuffle(int[] array, int times) {
		for (int i = 0; i < times; i++) {
			int indexA = (int) (Math.random()*array.length);
			int indexB = (int) (Math.random()*array.length);
			swap(array, indexA, indexB);
		}
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

	public int[] copyArray(int[] array) {
		int[] copy = new int[array.length];
		copyArrayValues(array, copy, 0);
		return copy;
	}

	public void copyArrayValues(int[] from, int[] to, int startTo) {
		int index = startTo;
		for (int i = 0; i < from.length; i++) {
			to[index] = from[i];
			index++;
		}
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

}