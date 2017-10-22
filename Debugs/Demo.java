public class Demo {

	public static void main(String[] args) {
		int[] a = new int[] {2, 3, 8, 7};
		int[] b = new int[] {7, 2, 8, 3};
		int[] cross = crossOver(a, b);

		printArray(a);
		printArray(b);
		printArray(cross);
	}

	public static int[] crossOver(int[] orderA, int[] orderB) { //Taking a random part of order A, and then filling the rest with order B as the missing values appear
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
		int[] orderACopy = copyPartArray(orderA, start, end);
		copyArrayValues(orderACopy, newOrder, 0); //First part of newOrder is filled

		//int left = totalCities - orderACopy.length; //How many items do I need to add from B? In fact i don't need this
		//I need to know the startpoint for newOrder now
		int actualPoint = orderACopy.length;
		for (int i = 0; i < orderB.length; i++) {
			int city = orderB[i];
			if (!arrayIncludes(newOrder, city)) {
				newOrder[actualPoint] = city;
				actualPoint++;
			}
		}
		//printArray(orderA);
		//printArray(orderB);
		//printArray(newOrder);
		return newOrder;
	}

	public static void printArray(int[] array) {
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

	public static void copyArrayValues(int[] from, int[] to, int startTo) {
		int index = startTo;
		for (int i = 0; i < from.length; i++) {
			to[index] = from[i];
			index++;
		}
	}

	public static int[] copyPartArray(int[] from, int start, int end) {
		int[] result = new int[end - start + 1];
		int index = 0;
		for (int i = start; i <= end; i++) {
			result[index] = from[i];
			index++;
		}
		return result;
	}

	public static boolean arrayIncludes(int[] array, int value) {
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

	public static void fillArray(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			array[i] = value;
		}
	}


}