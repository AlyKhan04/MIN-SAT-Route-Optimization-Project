package com.aim.runners;


import Examples.ExampleHyperHeuristic1;
//import com.aim.hyperheuristics.GreedySearch;
import com.aim.hyperheuristics.*;

import AbstractClasses.HyperHeuristic;

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

		return new TournamentSelection(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new SR_IE_VisualRunner(0);
		runner.run();
	}

}
