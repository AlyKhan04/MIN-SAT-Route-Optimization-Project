package com.aim.solution;

import com.aim.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 *
 */public class SolutionRepresentation implements SolutionRepresentationInterface {

	int[] aiRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {
		//sets the solution representation
		this.aiRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {
		return this.aiRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {
		this.aiRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {
		return this.aiRepresentation.length;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		int[] clonedrep = this.aiRepresentation.clone();
		return new SolutionRepresentation(clonedrep);
	}
}