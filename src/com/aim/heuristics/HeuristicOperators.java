package com.aim.heuristics;

import java.util.Random;

import com.aim.interfaces.ObjectiveFunctionInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 * <br>
 * This class is included (and all non-crossover heuristics subclass this class) to simplify your implementation and it
 * is intended that you include any common operations in this class to simplify your implementation of the other heuristics.
 * Furthermore, if you implement and test common functionality here, it is less likely that you introduce a bug elsewhere!
 * <br>
 * For example, think about common neighbourhood operators and any other incremental changes that you might perform
 * while applying low-level heuristics.
 */
public class HeuristicOperators {

	protected ObjectiveFunctionInterface objfunc;

	protected final Random random;
	public HeuristicOperators(Random random) {

		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		this.objfunc = f;
	}
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
}
