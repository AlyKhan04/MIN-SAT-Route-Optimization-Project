package com.aim.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.UAVSolutionInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	public DavissHillClimbing(Random random) {
		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double searchDepth, double iom) {
		int numberOfIterations = numberIterations(searchDepth);
		int[] numOfVariables = solution.getSolutionRepresentation().getSolutionRepresentation();
		int numOfLocations = solution.getNumberOfLocations();
		int objValue = solution.getObjectiveFunctionValue();
		int[] perm = randomPermutation(numOfLocations);

		//for the legnth of the location array,
		for (int x = 0; x < numOfLocations; x++) {
			//Get the current solution and gets the objective function value
			int[] currentRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int currentValue = solution.getObjectiveFunctionValue();
			//for the amount of iterations set based on the depth of the search
			for (int y = 0; y < numberOfIterations; y++) {
				//creates two indices to swap elements in the solution representation.
				int firstIndex = perm[(y+x) % numOfLocations];
				int secondIndex = (firstIndex+1) % numOfLocations;
				int temp = numOfVariables[firstIndex];
				//these line perform the swap moving first index into second
				numOfVariables[firstIndex] = numOfVariables[secondIndex];
				numOfVariables[secondIndex] = temp;
			}
			//sets the solution value and gets the objective function value
			solution.getSolutionRepresentation().setSolutionRepresentation(numOfVariables);
			double tmpEval = solution.getObjectiveFunctionValue();
			//if the temp eval is better than the current solution, solution gets saved
			if (tmpEval < currentValue) {
				solution.getSolutionRepresentation().setSolutionRepresentation(currentRepresentation);
				solution.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(solution.getSolutionRepresentation()));
				System.out.println(solution.getObjectiveFunctionValue());
				System.out.println("---DBHC---");
			}
		}
		//returns the new or old objective function value
		return solution.getObjectiveFunctionValue();
	}
	//code to permutate the function
	public int[] randomPermutation (int length) {
		//creates a new int array with the same length as the number of locations
		int[] perm = new int[length];
		//for int i to legnth, permutates the array
		for (int i = 0; i < length; i++) {
			perm[i] = i;
		}
		//shuffles the permutations
		java.util.Collections.shuffle(Arrays.asList(perm));
		//returns the list
		return perm;
	}

	//calculates the number of iterations based on the given search depth
	public int numberIterations(double searchDepth) {
		if (searchDepth < 0.2) {
			return 1;
		} else if (searchDepth < 0.4) {
			return 2;
		} else if (searchDepth < 0.6) {
			return 3;
		} else if (searchDepth < 0.8) {
			return 4;
		} else if (searchDepth < 1) {
			return 5;
		}
		return 0; //depth of search always between 0 and 1.
	}
	//returns false since crossover is not used
	public boolean isCrossover() {

		// TODO
		//return random.nextBoolean();
		return false;
	}

	//since IOM is not used
	@Override
	public boolean usesIntensityOfMutation() {

		// TODO
		//return random.nextBoolean();
		return false;
	}
	// since depth of search is used
	@Override
	public boolean usesDepthOfSearch() {

		// TODO
		//return random.nextBoolean();
		return true;
	}
}