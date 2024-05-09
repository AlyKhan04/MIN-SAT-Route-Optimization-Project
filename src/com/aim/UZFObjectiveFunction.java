package com.aim;

import com.aim.instance.Location;
import com.aim.instance.UZFInstance;
import com.aim.interfaces.ObjectiveFunctionInterface;
import com.aim.interfaces.UZFInstanceInterface;
import com.aim.interfaces.SolutionRepresentationInterface;


/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFObjectiveFunction implements ObjectiveFunctionInterface {
	UZFInstanceInterface oInstance;

	public UZFObjectiveFunction(UZFInstanceInterface oInstance) {
		this.oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		int[] solutionOrder = oSolution.getSolutionRepresentation();
		if (solutionOrder.length == 0) return 0;

		int totalDistance = getCostBetweenFoodPreparationAreaAnd(solutionOrder[0]);

		for (int i = 0; i < solutionOrder.length - 1; i++) {
			totalDistance += getCost(solutionOrder[i], solutionOrder[i + 1]);
		}

		totalDistance += getCostBetweenFoodPreparationAreaAnd(solutionOrder[solutionOrder.length - 1]);

		return totalDistance;
	}

	public int getCost(Location oLocationA, Location oLocationB) {
		double deltaX = oLocationA.x() - oLocationB.x();
		double deltaY = oLocationA.y() - oLocationB.y();
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	@Override
	public int getCost(int iLocationA, int iLocationB) {
		return getCost(oInstance.getLocationForEnclosure(iLocationA),oInstance.getLocationForEnclosure(iLocationB));
	}

	@Override
	public int getCostBetweenFoodPreparationAreaAnd(int iLocation) {
		return getCost(oInstance.getLocationOfFoodPreparationArea(),oInstance.getLocationForEnclosure(iLocation));
	}
}
