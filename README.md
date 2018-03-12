# TravellingSalesPerson

A simulation approach for the Travelling Salesperson problem, where a person has to travel among several cities.
The challenge consists of finding the shortest path!

There are some different ways of "solving" this:
  1. In the directory BruteForce, cities are just randomly swapped
  2. In the directory LexicographicalOrder, I translate to code an algorithm to find all the permutations of an array
  3. In the directory ImplementingLO, I implement the algorithm to check every single permutation of the cities
  4. In the directory TSPwithGA ,I implement a genetic algorithm to make the process faster (and delete the permutation thing)
  5. In the directory TSPwithCrossOver, I implement crossover feature to the genetic algorithm
 
There are a few improvements to do:
  1. Since the matter of distance is only comparing an order with another to find the one that fits the best, there is no need to use the distance function from JVector, because it uses the square root and that slows the computations. So I could make a new distance function only using the square of the coordinates
  2. Maybe improving the mutate and crossover features to make the process faster

Inspired in a video by Dan Shiffman: 
https://www.youtube.com/watch?v=BAejnwN4Ccw
