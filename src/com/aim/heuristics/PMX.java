package com.aim.heuristics;

import java.util.HashMap;
import java.util.Random;

import com.aim.interfaces.ObjectiveFunctionInterface;
import com.aim.interfaces.UAVSolutionInterface;
import com.aim.interfaces.XOHeuristicInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class PMX implements XOHeuristicInterface {

	private final Random random;

	public PMX(Random random) {

		this.random = random;
	}

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		//returns the objective value of the function
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(UAVSolutionInterface firstP, UAVSolutionInterface secondP, UAVSolutionInterface offspring, double depthOfSearch, double intensityOfMutation) {
		//saves the first parent and the second parent
		int[] firstParent = firstP.getSolutionRepresentation().getSolutionRepresentation();
		int[] secondParent = secondP.getSolutionRepresentation().getSolutionRepresentation();
		int[] child = new int[firstParent.length];
		//makes the child the same legnth as the parent
		// crossover points
		int crossPoint1 = (int) (Math.random() * firstParent.length);
		int crossPoint2 = (int) (Math.random() * firstParent.length);

		// Ensure crossPoint1 is less than crossPoint2
		if (crossPoint1 > crossPoint2)
		{
			int temp = crossPoint1;
			crossPoint1 = crossPoint2;
			crossPoint2 = temp;
		}

		// Apply PMX Crossover between crossPoint1 and crossPoint2
		System.arraycopy(firstParent, crossPoint1, child, crossPoint1, crossPoint2 - crossPoint1 + 1);

		// Create a map to track which values have been swapped
		HashMap<Integer, Integer> mapvalue = new HashMap<>();
		for (int x = crossPoint1; x <= crossPoint2; x++) {
			mapvalue.put(firstParent[x], secondParent[x]);
		}

		// Fill in the rest of the child array with elements from parent2, fixing conflicts
		for (int x = 0; x < firstParent.length; x++) {
			if (x < crossPoint1 || x > crossPoint2) {
				int candidate = secondParent[x];
				while (mapvalue.containsKey(candidate)) {
					candidate = mapvalue.get(candidate);
				}
				child[x] = candidate;
			}
		}

		// Set the representation to the child
		offspring.getSolutionRepresentation().setSolutionRepresentation(child);

		// Optionally, evaluate and return the quality of the new solution
		return offspring.getObjectiveFunctionValue();

	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		// TODO
	}

	@Override
	public boolean isCrossover() {

		// TODO
		//return random.nextBoolean();
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		//return random.nextBoolean();
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		//return random.nextBoolean();
		return false;
	}
}