package com.aim.interfaces;

import java.util.ArrayList;

import com.aim.instance.InitialisationMode;
import com.aim.instance.Location;
import com.aim.solution.UZFSolution;

/**
 * @author Warren G Jackson
 * @since 1.0.0 (22/03/2024)
 */
public interface UZFInstanceInterface {

	/**
	 * 
	 * @param mode The initialisation mode to use. Remember to handle both RANDOM and CONSTRUCTIVE methods.
	 * @return
	 */
	public UZFSolution createSolution(InitialisationMode mode);
	
	/**
	 * 
	 * @return The objective function used to evaluate the current problem instance.
	 */
	public ObjectiveFunctionInterface getUZFObjectiveFunction();
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfLocations();

	/**
	 *
	 * @param iEnclosureId
	 * @return
	 */
	public Location getLocationForEnclosure(int iEnclosureId);

	/**
	 *
	 * @return
	 */
	public Location getLocationOfFoodPreparationArea();

	/**
	 *
	 * @param oSolution
	 * @return
	 */
	public ArrayList<Location> getSolutionAsListOfLocations(UAVSolutionInterface oSolution);
}
