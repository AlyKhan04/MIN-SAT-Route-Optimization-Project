package com.aim.instance;


import java.util.ArrayList;
import java.util.Random;

import com.aim.UZFObjectiveFunction;
import com.aim.interfaces.ObjectiveFunctionInterface;
import com.aim.interfaces.SolutionRepresentationInterface;
import com.aim.interfaces.UZFInstanceInterface;
import com.aim.interfaces.UAVSolutionInterface;
import com.aim.solution.SolutionRepresentation;
import com.aim.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public class UZFInstance implements UZFInstanceInterface {
	private int numberOfLocations;
	private Location[] aoLocations;
	private Location foodPreparationLocation;
	private final Random random;
	private ObjectiveFunctionInterface oObjectiveFunction;

	public UZFInstance(int numberOfLocations, Location[] aoLocations, Location foodPreparationLocation, Random random) {
		this.numberOfLocations = numberOfLocations;
		this.aoLocations = aoLocations;
		this.foodPreparationLocation = foodPreparationLocation;
		this.random = random;
		this.oObjectiveFunction = new UZFObjectiveFunction(this);  // Assuming UZFObjectiveFunction exists and takes UZFInstance.
	}

	@Override
	public UZFSolution createSolution(InitialisationMode mode) {
		int[] indices = new int[numberOfLocations];
		//creates a new number of locations
		for (int i = 0; i < numberOfLocations; i++) {
			indices[i] = i;
		}
		//randomly sets the next integer
		if (mode == InitialisationMode.RANDOM) {
			for (int i = 0; i < indices.length; i++) {
				int swapIndex = random.nextInt(indices.length);
				int temp = indices[i];
				indices[i] = indices[swapIndex];
				indices[swapIndex] = temp;
			}
		} else if (mode == InitialisationMode.CONSTRUCTIVE) {
			int[] newPath = new int[numberOfLocations];
			boolean[] visited = new boolean[numberOfLocations];
			int current = random.nextInt(numberOfLocations);
			newPath[0] = current;
			visited[current] = true;
			for (int i = 1; i < numberOfLocations; i++) {
				int nextIndex = -1;
				double minDistance = Double.MAX_VALUE;
				for (int j = 0; j < numberOfLocations; j++) {
					if (!visited[j]) {
						double distance = oObjectiveFunction.getCost(current, j);
						if (distance < minDistance) {
							minDistance = distance;
							nextIndex = j;
						}
					}
				}
				newPath[i] = nextIndex;
				visited[nextIndex] = true;
				current = nextIndex;
			}
			indices = newPath;  // replace original indices with new path
		} else {
			throw new IllegalArgumentException("Unsupported initialisation mode: " + mode);
		}

		SolutionRepresentationInterface solRep = new SolutionRepresentation(indices);
		return new UZFSolution(solRep, oObjectiveFunction.getObjectiveFunctionValue(solRep));
	}

	@Override
	public ObjectiveFunctionInterface getUZFObjectiveFunction() {
		//returns the objective function
		return this.oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {
		//returns the number of locations
		return this.numberOfLocations;
	}

	@Override
	public Location getLocationForEnclosure(int iEnclosureId) {
		//if the enclosure is exists then returns the enclosure
		if (iEnclosureId >= 0 && iEnclosureId < aoLocations.length) {
			return aoLocations[iEnclosureId];
		}
		throw new IllegalArgumentException("Enclosure ID out of range: " + iEnclosureId);
	}

	@Override
	public Location getLocationOfFoodPreparationArea() {
		//returns location for food preperation
		return this.foodPreparationLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(UAVSolutionInterface oSolution) {
	//Gets the location for the enclosure
		int[] indices = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		ArrayList<Location> locations = new ArrayList<>();
		for (int index : indices) {
			locations.add(getLocationForEnclosure(index));
		}
		return locations;
	}
}