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
		//Osolution is an array of integers
		int[] solutionOrder = oSolution.getSolutionRepresentation();
		int totalDistance = 0; //initial distance is zero
		if (solutionOrder.length > 0) { //if the length is greater than 0 then
			totalDistance += getCostBetweenFoodPreparationAreaAnd(solutionOrder[0]);
			// Sum of distances between consecutive enclosures.
			for (int i = 0; i < solutionOrder.length - 1; i++) {
				totalDistance += getCost(solutionOrder[i], solutionOrder[i + 1]);
			}
		}
		// Return trip from the last enclosure back to the food preparation area.
		if (solutionOrder.length > 0) {
			totalDistance += getCostBetweenFoodPreparationAreaAnd(solutionOrder[solutionOrder.length - 1]);
		}
		return totalDistance;
	}
	
	public int getCost(Location oLocationA, Location oLocationB) {
		Location locationA = oLocationA;
		Location locationB = oLocationB;
		int xA = locationA.x();
		int yA = locationA.y();
		int xB = locationB.x();
		int yB = locationB.y();
		int sumX = xA - xB;
		int sumY = yA - yB;
		int squareX = sumX*sumX;
		int squareY = sumY*sumY;
		int squareSum = squareX + squareY;
		double SqrtSum = Math.sqrt(squareSum);
		double ceilingSum = Math.ceil(SqrtSum);
		return (int) ceilingSum;
	}

	@Override
	public int getCost(int iLocationA, int iLocationB) {
		int loc1 = iLocationA;
		int loc2 = iLocationB;
		Location LocationA = oInstance.getLocationForEnclosure(loc1);
		Location LocationB = oInstance.getLocationForEnclosure(loc2);
		int Cost = getCost(LocationA,LocationB);
		return Cost;
	}

	@Override
	public int getCostBetweenFoodPreparationAreaAnd(int iLocation) {
		Location foodLocation = oInstance.getLocationOfFoodPreparationArea();
		Location Location = oInstance.getLocationForEnclosure(iLocation);
		int Cost = getCost(foodLocation,Location);
		return Cost;
	}
}
