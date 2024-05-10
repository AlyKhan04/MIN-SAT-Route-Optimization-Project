package com.aim.runners;


import Examples.ExampleHyperHeuristic1;
//import com.aim.hyperheuristics.GreedySearch;
import com.aim.hyperheuristics.GreedySearch;
import com.aim.hyperheuristics.ModifiedChoiceFunctionAllMoves;
import com.aim.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;
import com.aim.hyperheuristics.SingleHeuristicTester;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 * Runs a simple random IE hyper-heuristic then displays the best solution found
 */
public class SR_IE_VisualRunner extends HH_Runner_Visual {

	public SR_IE_VisualRunner(int instanceId) {
		super(instanceId);
	}
	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new SR_IE_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new SR_IE_VisualRunner(1);
		runner.run();
	}

}
