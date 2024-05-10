package com.aim.heuristics;

import java.util.Random;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.UAVSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {


	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double searchDepth, double intensityOfMutation) {

//int reinsertions = 0;

// Get the current solution representation
		int[] solutions_array = solution.getSolutionRepresentation().getSolutionRepresentation();

// Determine the number of reinsertions based on the intensity of mutation
		int numOfMaxReinsertions = numberIterations(intensityOfMutation);

// Perform reinsertions
		for (int x = 0; x < numOfMaxReinsertions; x++) {
// Select random index to remove an element
			int removeIndex = random.nextInt(solutions_array.length);
			int removedElement = solutions_array[removeIndex];

// Remove
			for (int y = removeIndex; y < solutions_array.length - 1; y++) {
				solutions_array[y] = solutions_array[y + 1];
			}
			solutions_array[solutions_array.length - 1] = -1; // Set the last element to a placeholder value

// Select a random index to reinsert the removed element
			int insertIndex = random.nextInt(solutions_array.length);
// Shift elements to make space for reinsertion
			for (int j = solutions_array.length - 1; j > insertIndex; j--) {
				solutions_array[j] = solutions_array[j - 1];
			}
			solutions_array[insertIndex] = removedElement; // Reinsert the removed element
//reinsertions++;
		}

// Update the solution representation with the modified array
		solution.getSolutionRepresentation().setSolutionRepresentation(solutions_array);
		solution.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(solution.getSolutionRepresentation()));
		System.out.println(solution.getObjectiveFunctionValue());
		System.out.println("---GreedySearch---");
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {

// TODO
		//return random.nextBoolean();
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

// TODO
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

// TODO
		return true;
	}

}
