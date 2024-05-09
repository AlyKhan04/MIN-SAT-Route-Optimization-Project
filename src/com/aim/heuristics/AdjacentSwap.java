package com.aim.heuristics;

import java.util.Random;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.UAVSolutionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {

		super(random); //constructor calls constructor in parent class (HeuristicOperators) - which just says this.random = random
	}

//solution is an argument that expects a class that implements UAVSolutionInterface, interfaces can't have classes
//applies heuristic and then returns objective value.
//in terms of techniques apply does the move/neighbourhood operator the other techniques are implemented elsewhere - eval func for example actual technique/method for that is elsewhere.

	@Override
	public int apply(UAVSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		//number of swaps is based on the intensity of the mutation
		int number_of_swaps = numberOfSwaps(intensityOfMutation);
		//adds the solution as a representation
		int[] solutions = solution.getSolutionRepresentation().getSolutionRepresentation();
		//chooses a random integer from the index
		for (int i = 0; i < number_of_swaps; i++) {
			int index = random.nextInt(solution.getNumberOfLocations());//0 inclusive and bound exclusive
			//sets a circular array
			int nextIndex = (index + 1) % solution.getNumberOfLocations();//circular array
			//gets the location of the index
			int temp = solutions[index];
			//moves the index
			solutions[index] = solutions[nextIndex];
			//swaps then iterates
			solutions[nextIndex] = temp;
			i++;
		}

		solution.getSolutionRepresentation().setSolutionRepresentation(solutions); //update solution after heuristic applied.
		solution.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(solution.getSolutionRepresentation()));
		System.out.println(solution.getObjectiveFunctionValue());
		return solution.getObjectiveFunctionValue();

	}

	public int numberOfSwaps(double intensityOfMutation) {
		if (intensityOfMutation < 0.2) {
			return 1;
		}
		else if (intensityOfMutation < 0.4) {
			return 2;
		}
		else if (intensityOfMutation < 0.6) {
			return 4;
		}
		else if (intensityOfMutation < 0.8) {
			return 8;
		}
		else if (intensityOfMutation < 1) {
			return 16;
		}
		else {
			return 32;
		}
	}


	//mainly not used for now used later when we make our hyperheuristic and wanna know if we use mutation or not.
	@Override
	public boolean isCrossover() {

// TODO
//return random.nextBoolean();
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

// TODO
//return random.nextBoolean();
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

// TODO
//return random.nextBoolean();
		return false;
	}
}