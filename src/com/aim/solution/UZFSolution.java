package com.aim.solution;

import com.aim.instance.UZFInstance;
import com.aim.interfaces.UAVSolutionInterface;
import com.aim.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFSolution implements UAVSolutionInterface {
	int objectivefunctionvalue;
	SolutionRepresentationInterface representation;
	public UZFSolution(SolutionRepresentationInterface representation, int objectiveFunctionValue) {
		this.objectivefunctionvalue = objectiveFunctionValue;
		this.representation = representation;
	}

	@Override
	public int getObjectiveFunctionValue() {

		// returns the value of the objective function value
		return objectivefunctionvalue;
	}

	@Override
	public void setObjectiveFunctionValue(int objectiveFunctionValue) {
		//sets objective function value
		objectivefunctionvalue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {

		// Done, returns representation
		return representation;
	}

	@Override
	public UAVSolutionInterface clone() {
		//Copied Interface
		SolutionRepresentationInterface clonedRep = this.representation.clone();
		return new UZFSolution(clonedRep,this.objectivefunctionvalue);
	}

	@Override
	public int getNumberOfLocations() {
		// returns the numberoflocations
		return this.representation.getNumberOfLocations();
	}
}
