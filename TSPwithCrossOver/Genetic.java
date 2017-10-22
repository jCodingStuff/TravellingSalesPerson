import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Genetic {

	DrawingCanvas canvas;
	Random generator;

	public Genetic(DrawingCanvas canvas) {
		this.canvas = canvas;
		this.generator = new Random();
	}

	public void calculateFitness() {
		double currentRecord = Double.MAX_VALUE;
		for (int i = 0; i < canvas.population.size(); i++) {
			double d = canvas.calcDistance(canvas.cities, canvas.population.get(i));
			if (d < canvas.recordDistance) {
				canvas.recordDistance = d;
				System.out.println("Record distance: " + d);
				canvas.bestEver = canvas.population.get(i);
			}
			if (d < currentRecord) {
				currentRecord = d;
				//System.out.println(d);
				canvas.currentBest = canvas.population.get(i);
			}
			canvas.fitness.add(1/(d+1));
		}
	}

	public void normalizeFitness() {
		double sum = 0;
		for (int i = 0; i < canvas.fitness.size(); i++) {
			sum += canvas.fitness.get(i);
		}
		for (int i = 0; i < canvas.fitness.size(); i++) {
			canvas.fitness.set(i, canvas.fitness.get(i)/sum); //Making a probability
		}
	}

	public void nextGeneration() {
		ArrayList<int[]> newPopulation = new ArrayList<int[]>();
		for (int i = 0; i < canvas.population.size(); i++) {
			int[] orderA = pickOne(canvas.population, canvas.fitness);
			int[] orderB = pickOne(canvas.population, canvas.fitness);
			int[] order = crossOver(orderA, orderB); //This crossover is hard because number cannot be repeated
			mutate(order, 0.01);
			newPopulation.add(order);
		}
		canvas.population = newPopulation;
	}

	public int[] pickOne(ArrayList<int[]> list, ArrayList<Double> prob) {
		int index = 0;
		double r = generator.nextDouble();
		//System.out.println(r);

		while (r > 0) {
			r -= prob.get(index);
			//System.out.println(r);
			index++;
		}
		index--;
		
		if (index >= 0 && index < list.size()) {
			return canvas.copyArray(list.get(index)); //I return a copy because I will start messing with them
		}
		else {
			int r2 = (int) (Math.random()*list.size()); //I have to fix this, sometimes the index is out of bounds...
			return canvas.copyArray(list.get(r2));
		}
	}

	public int[] crossOver(int[] orderA, int[] orderB) { //Taking a random part of order A, and then filling the rest with order B as the missing values appear
		int start = (int) (Math.random()*orderA.length);
		while (start == orderA.length - 1) {
			start = (int) (Math.random()*orderA.length); //I don't want to pick the last one as start
		}
		int end = (int) (Math.random()*(orderA.length-(start+1))+(start+1)); //A random index between start + 1 and end of array
		while (end <= start) { //Being safe
			end = (int) (Math.random()*(orderA.length-(start+1))+(start+1));
		}
		int[] newOrder = new int[orderA.length]; //The array is automatically filled with 0. I will fill it with -1 because 0 is a city and I don't want errors
		fillArray(newOrder, -1);
		int[] orderACopy = canvas.copyPartArray(orderA, start, end);
		canvas.copyArrayValues(orderACopy, newOrder, 0); //First part of newOrder is filled

		//int left = canvas.totalCities - orderACopy.length; //How many items do I need to add from B? In fact i don't need this
		//I need to know the startpoint for newOrder now
		int actualPoint = orderACopy.length;
		for (int i = 0; i < orderB.length; i++) {
			int city = orderB[i];
			if (!arrayIncludes(newOrder, city)) {
				newOrder[actualPoint] = city;
				actualPoint++;
			}
		}
		return newOrder;
	}

	public void mutate(int[] order, double mutationRate) {
		for (int i = 0; i < canvas.totalCities; i++) {
			if (generator.nextDouble() < mutationRate) {
				int indexA = (int) (Math.random()*order.length);
				int indexB = (indexA + 1) % canvas.totalCities;
				canvas.swap(order, indexA, indexB);
			}
		}
	}

	public void fillArray(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
	}

	public boolean arrayIncludes(int[] array, int value) {
		boolean found = false;
		int i = 0;
		while (!found && i < array.length) {
			if (array[i] == value) {
				found = true;
			}
			i++;
		}
		return found;
	}

}