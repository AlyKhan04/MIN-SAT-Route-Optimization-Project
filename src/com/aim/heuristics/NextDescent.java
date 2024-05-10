package com.aim.heuristics;


import java.util.Random;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.UAVSolutionInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

	public NextDescent(Random random) {

		super(random);
	}

	@Override
	public int apply(UAVSolutionInterface solution, double dos, double iom) {
		int numberOfIterations = numberIterations(dos);
		for (int i = 0; i < numberOfIterations; i++) {
			int[] bestRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int[] tmpRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
			int bestEval = solution.getObjectiveFunctionValue();
			int startPosition = random.nextInt(solution.getNumberOfLocations());
			for (int j = startPosition; j < solution.getNumberOfLocations() + startPosition; j++) {
				int index1 = j % solution.getNumberOfLocations();
				int index2 = (index1 + 1) % solution.getNumberOfLocations();
				int temp = tmpRepresentation[index1];
				tmpRepresentation[index1] = tmpRepresentation[index2];
				tmpRepresentation[index2] = temp;
				solution.getSolutionRepresentation().setSolutionRepresentation(tmpRepresentation);
				int tmpEval = solution.getObjectiveFunctionValue();
				if (tmpEval < bestEval) {
					solution.getSolutionRepresentation().setSolutionRepresentation(bestRepresentation);
					solution.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(solution.getSolutionRepresentation()));
					System.out.println(solution.getObjectiveFunctionValue());
					System.out.println("---ND---");
				}
			}
		}
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {

// TODO
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

// TODO
		return random.nextBoolean();
	}

	@Override
	public boolean usesDepthOfSearch() {

// TODO
		return random.nextBoolean();
	}
}