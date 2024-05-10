package com.aim;

import com.aim.heuristics.*;
import com.aim.instance.InitialisationMode;
import com.aim.instance.Location;
import com.aim.instance.UZFInstance;
import com.aim.instance.reader.UAVInstanceReader;
import com.aim.interfaces.*;
import AbstractClasses.ProblemDomain;
import com.aim.solution.UZFSolution;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFDomain extends ProblemDomain implements Visualisable {
	HeuristicInterface[] heuristic;
	Random random;
	UZFInstanceInterface instance;
	UAVSolutionInterface[] uzfSolutionArray;
	private final int[] mutationheuristics = {1,3};
	private final int[] localsearchheuristics = {0,2,5};
	private final int[] crossoverheuristics = {4,6};
	UAVSolutionInterface Best;


	public UZFDomain(long seed) {

		// TODO - set default memory size and create the array of low-level heuristics
		super(seed);
		this.random = new Random();
		heuristic = new HeuristicInterface[]{
				new DavissHillClimbing(random), //best on square 40
				new AdjacentSwap(random), // 60
				new NextDescent(random), //55
				new PMX(random),//55
				new Reinsertion(random),//40
				new GreedySearchMethod(random),//55
				new OrderedCrossover(random),
		};
		this.setMemorySize(4);
		//heuristicsIOM = getHeuristicsThatUseDepthOfSearch();
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		UAVSolutionInterface currentSolution = uzfSolutionArray[currentIndex].clone();
		heuristic [hIndex].apply(currentSolution, depthOfSearch, intensityOfMutation);
		uzfSolutionArray[candidateIndex] = currentSolution;
		return currentSolution.getObjectiveFunctionValue();
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		UAVSolutionInterface offspring, parent1, parent2;
		parent1 = uzfSolutionArray[parent1Index];
		parent2 = uzfSolutionArray[parent2Index];
		offspring = parent1.clone();
		if (hIndex == 3) {
			PMX pmx = (PMX) heuristic[4];
			pmx.apply(parent1, parent2, offspring, depthOfSearch, intensityOfMutation);
		}
		if (hIndex == 6) {
			OrderedCrossover oc = (OrderedCrossover) heuristic[6];
			oc.apply(parent1, parent2, offspring, depthOfSearch, intensityOfMutation);
		}
			uzfSolutionArray[candidateIndex] = offspring;
			return offspring.getObjectiveFunctionValue();
	}

	@Override
	public String bestSolutionToString() {
		int[] solutionRepresentation = getBestSolutionRepresentation();
		StringBuilder sb = new StringBuilder();
		//appends the commas between elements
		sb.append("[");
		//goes through an array and adds the solutions to a string
		for (int i = 0; i < solutionRepresentation.length; i++) {
			sb.append(solutionRepresentation[i]);
			if (i < solutionRepresentation.length - 1) {
				sb.append(", "); // Append a comma between elements only
			}
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean compareSolutions(int a, int b) {
		if (a < 0 || a >= uzfSolutionArray.length || b < 0 || b >= uzfSolutionArray.length) {
			throw new IndexOutOfBoundsException("Solution index out of bounds");
		}
		if (uzfSolutionArray[a] == null || uzfSolutionArray[b] == null) {
			return false; // Return false if any solution is missing
		}
		return uzfSolutionArray[a].getSolutionRepresentation().equals(uzfSolutionArray[b].getSolutionRepresentation());
	}

	@Override
	public void copySolution(int a, int b) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		// Check if the source and destination indices are within the bounds of the array
		if (a < 0 || a >= uzfSolutionArray.length || b < 0 || b >= uzfSolutionArray.length) {
			throw new IndexOutOfBoundsException("Source or destination index out of bounds");
		}

		// Ensure that there is a solution at the source index to copy from
		if (uzfSolutionArray[a] == null) {
			throw new NullPointerException("No solution at source index to copy");
		}

		// Copy the solution from the source index to the destination index
		// This assumes that UZFSolution has a method to create a deep copy (e.g., a copy constructor or cloning method)
		try {
			uzfSolutionArray[b] =  uzfSolutionArray[a].clone();
		} catch (RuntimeException e) {
			throw new IllegalStateException("Failed to clone the solution: " + e.getMessage(), e);
		}
	}

	@Override
	public double getBestSolutionValue() {
		return getBestSolution().getObjectiveFunctionValue();
	}

	@Override
	public double getFunctionValue(int index) {
		if (index < 0 || index >= uzfSolutionArray.length) {
			throw new IllegalArgumentException("Invalid solution index: " + index);
		}//returns the best possible value
		return uzfSolutionArray[index].getObjectiveFunctionValue();
	}

	// TODO
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		int[] result;
		switch (type) {
			case MUTATION -> {
				// Fill this array with mutation heuristic indices
				result = mutationheuristics;
			}
			case CROSSOVER -> {
				// Fill this array with crossover heuristic indices
				result = crossoverheuristics;
			}
			case LOCAL_SEARCH -> {
				// Fill this array with local search heuristic indices
				result = localsearchheuristics;
			}
			default -> {
				// Handle the case where no matching type is found (return an empty array or similar)
				result = new int[0];
			}
		}
		return result;
	}


	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		int count = 0;
		//counts the heuristics
		for (HeuristicInterface h : heuristic) {
			if (h.usesDepthOfSearch()) {
				count++;
			}
		}
		//sets an array and goes through it and collects the necessary variables
		int[] indices = new int[count];
		int index = 0;
		for (int i = 0; i < heuristic.length; i++) {
			if (heuristic[i].usesDepthOfSearch()) {
				indices[index++] = i;
			}
		}
		return indices;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		int count = 0;
		//same thing but for intensity of mutation
		for (HeuristicInterface h : heuristic) {
			if (h.usesIntensityOfMutation()) {
				count++;
			}
		}
		int[] indices = new int[count];
		int index = 0;
		for (int i = 0; i < heuristic.length; i++) {
			if (heuristic[i].usesIntensityOfMutation()) {
				indices[index++] = i;
			}
		}
		return indices;
	}

	@Override
	public int getNumberOfHeuristics() {
		//returns the number of heuristics
		return 7;
	}

	@Override
	public int getNumberOfInstances() {
		// IF GIVEN NEW FILE COME HERE AND CHANGE THIS
		return instance.getNumberOfLocations();
	}

	@Override
	public void initialiseSolution(int index) {
		// TODO - make sure that you also update the best solution!
		UZFInstanceInterface instance = getLoadedInstance();
		UAVSolutionInterface constructiveSolution = instance.createSolution(InitialisationMode.CONSTRUCTIVE);
		UAVSolutionInterface randomSolution = instance.createSolution(InitialisationMode.RANDOM);
		if (constructiveSolution.getObjectiveFunctionValue() < randomSolution.getObjectiveFunctionValue()) {
			uzfSolutionArray[index] =  constructiveSolution;
		} else {
			uzfSolutionArray[index] = randomSolution;
		}
		updateBestSolution(index);
	}

	public void loadInstance(int instanceId) {
		// Declare the Path variable
		Path filename;
		// Switch to set the filename based on instanceId
		switch (instanceId) {
			case 0: filename = Paths.get("instances/uzf/square.uzf"); break;
			case 1: filename = Paths.get("instances/uzf/libraries-15.uzf"); break;
			case 2: filename = Paths.get("instances/uzf/carparks-40.uzf"); break;
			case 3: filename = Paths.get("instances/uzf/tramstops-85.uzf"); break;
			case 4: filename = Paths.get("instances/uzf/grid.uzf"); break;
			case 5: filename = Paths.get("instances/uzf/clustered-enclosures.uzf"); break;
			case 6: filename = Paths.get("instances/uzf/chatgpt-instance-100-enclosures.uzf"); break;
			default: throw new IllegalArgumentException("Invalid instance ID");
		}
		// Instantiate the reader and read the instance
		UAVInstanceReader reader = new UAVInstanceReader();
		try {
			// Pass the filename Path and a new Random object to the method
			this.instance = reader.readUZFInstance(filename, new Random());
			ObjectiveFunctionInterface objfunc = this.instance.getUZFObjectiveFunction();
			for(HeuristicInterface huer : this.heuristic){
				huer.setObjectiveFunction(objfunc);
			}
			// Store or process the uzfInstance as needed

		} catch (Exception e) {
			System.err.println("Error reading UZF instance: " + e.getMessage());
		}
	}

	@Override
	public void setMemorySize(int size) {
		// Validate the size to ensure it's a positive number
		if (size < 1) {
			throw new IllegalArgumentException("Memory size must be at least 1 to store solutions.");
		}
		// Allocate new memory for the solutions array with the specified size
		this.uzfSolutionArray = new UAVSolutionInterface[size];
	}

	@Override
	public String solutionToString(int index) {
		int[] solution = uzfSolutionArray[index].getSolutionRepresentation().getSolutionRepresentation();
		return "[" + String.join(", ", Arrays.stream(solution).mapToObj(String::valueOf).toArray(String[]::new)) + "]";
	}

	@Override
	public String toString() {

		return "UZF Zoo Domain Problem";
	}

	private void updateBestSolution(int index) {
		//Cannot manipulate the addition
		if (Best == null || uzfSolutionArray[index].getObjectiveFunctionValue() < Best.getObjectiveFunctionValue()) {
			Best = uzfSolutionArray[index].clone(); // Assumes clone is properly overridden.
		}
	}

	@Override
	public UZFInstanceInterface getLoadedInstance() {
		//returns the loaded instance
		return instance;
	}

	/**
	 * @return The integer array representing the ordering of the best solution.
	 */
	@Override
	public int[] getBestSolutionRepresentation() {
		return getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
	}

	@Override
	public Location[] getRouteOrderedByLocations() {
		//returns an array of locations
		Location[] locations = new Location [this.instance.getNumberOfLocations()];
		int[] bestroute = this.Best.getSolutionRepresentation().getSolutionRepresentation();
		for(int i = 0; i < locations.length; i++) {
			locations[i] = this.instance.getLocationForEnclosure(bestroute[i]);
		}
		return locations;
	}

	public UAVSolutionInterface getBestSolution() {
		return Best;
	}
}
