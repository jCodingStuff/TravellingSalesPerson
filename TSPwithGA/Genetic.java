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

		for (int i = 0; i < canvas.population.size(); i++) {
			double d = canvas.calcDistance(canvas.cities, canvas.population.get(i));
			if (d < canvas.recordDistance) {
				canvas.recordDistance = d;
				System.out.println(d);
				canvas.bestEver = canvas.population.get(i);
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
			int[] order = pickOne(canvas.population, canvas.fitness);
			mutate(order, 1);
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

	public void mutate(int[] order, double mutationRate) {
		int indexA = (int) (Math.random()*order.length);
		int indexB = (int) (Math.random()*order.length);
		canvas.swap(order, indexA, indexB);
	}

}