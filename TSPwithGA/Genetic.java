import java.util.List;
import java.util.ArrayList;

public class Genetic {

	DrawingCanvas canvas;

	public Genetic(DrawingCanvas canvas) {
		this.canvas = canvas;
	}

	public void calculateFitness() {

		for (int i = 0; i < canvas.population.size(); i++) {
			double d = canvas.calcDistance(canvas.cities, canvas.population.get(i));
			if (d < canvas.recordDistance) {
				canvas.recordDistance = d;
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
		double r = Math.random();

		while (r > 0) {
			r = r - prob.get(index);
			index++;
		}
		index--;
		//I am getting an error in the return, out of bounds
		int[] copy = canvas.copyArray(list.get(index));
		return copy; //I return a copy because I will start messing with them
	}

	public void mutate(int[] order, double mutationRate) {
		int indexA = (int) (Math.random()*order.length);
		int indexB = (int) (Math.random()*order.length);
		canvas.swap(order, indexA, indexB);
	}

}