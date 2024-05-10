package com.aim.heuristics;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.UAVSolutionInterface;

import java.util.Random;

public class GreedySearchMethod extends HeuristicOperators implements HeuristicInterface {

    public GreedySearchMethod(Random random) {
        super(random);
    }

    @Override
    public int apply(UAVSolutionInterface solution, double dos, double iom) {
        int[] currentRepresentation = solution.getSolutionRepresentation().getSolutionRepresentation();
        int bestEval = solution.getObjectiveFunctionValue();
        boolean improved = false;

        // Define search intensity based on the depth of search and intensity of mutation parameters
        int maxAttempts = (int) (solution.getNumberOfLocations() * dos * iom);

        // Attempt to find a single improving move, limited by maxAttempts
        int attempts = 0;
        while (attempts < maxAttempts && !improved) {
            int i = random.nextInt(solution.getNumberOfLocations());
            int j = random.nextInt(solution.getNumberOfLocations());

            // Ensure different indices for a meaningful swap
            if (i != j) {
                // Swap elements at i and j
                swap(currentRepresentation, i, j);

                // Update the solution representation temporarily
                solution.getSolutionRepresentation().setSolutionRepresentation(currentRepresentation);
                int newEval = solution.getObjectiveFunctionValue();

                // Check if the new configuration is better
                if (newEval < bestEval) {
                    bestEval = newEval;
                    improved = true; // Found an improving move
                } else {
                    // Swap back if not improved
                    swap(currentRepresentation, i, j);
                }
            }
            attempts++;
        }

        // Set the final solution representation only if improved
        if (improved) {
            solution.getSolutionRepresentation().setSolutionRepresentation(currentRepresentation);
            solution.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(solution.getSolutionRepresentation()));
            System.out.println(solution.getObjectiveFunctionValue());
            System.out.println("---GreedySearch---");
        }

        return solution.getObjectiveFunctionValue();
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        // This implementation uses IOM to determine the number of swaps to attempt.
        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {
        // This implementation uses DOS to increase the breadth of the search.
        return true;
    }
}