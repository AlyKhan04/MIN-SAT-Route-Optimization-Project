package com.aim.heuristics;

import com.aim.interfaces.HeuristicInterface;
import com.aim.interfaces.ObjectiveFunctionInterface;
import com.aim.interfaces.UAVSolutionInterface;
import com.aim.interfaces.XOHeuristicInterface;

import java.util.Random;

public class OrderedCrossover implements XOHeuristicInterface {
    private final Random random;
    ObjectiveFunctionInterface objfunc;

    public OrderedCrossover(Random random) {
        this.random = random;
    }
    @Override
    public int apply(UAVSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
        return oSolution.getObjectiveFunctionValue();
    }

    @Override
    public boolean isCrossover() {
        return true;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }

    @Override
    public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
        this.objfunc = oObjectiveFunction;
    }

    @Override
    public double apply(UAVSolutionInterface oParent1, UAVSolutionInterface oParent2, UAVSolutionInterface oChild, double dDepthOfSearch, double dIntensityOfMutation) {
        int[] firstParent = oParent1.getSolutionRepresentation().getSolutionRepresentation();
        int[] secondParent = oParent2.getSolutionRepresentation().getSolutionRepresentation();
        int[] child = new int[firstParent.length];

        // Determine crossover points
        int segmentLength = Math.max(1, (int) (dDepthOfSearch * firstParent.length));
        int crossPoint1 = random.nextInt(firstParent.length - segmentLength);
        int crossPoint2 = crossPoint1 + segmentLength - 1;

        // Ensure crossPoint1 is less than crossPoint2
        if (crossPoint1 > crossPoint2) {
            int temp = crossPoint1;
            crossPoint1 = crossPoint2;
            crossPoint2 = temp;
        }

        // Apply Ordered Crossover between crossPoint1 and crossPoint2
        System.arraycopy(firstParent, crossPoint1, child, crossPoint1, crossPoint2 - crossPoint1 + 1);

        // Fill the rest of the offspring array with elements from secondParent
        int currentParentIndex = 0;
        for (int i = 0; i < firstParent.length; i++) {
            if (i < crossPoint1 || i > crossPoint2) {
                // Find the next element in secondParent that's not already in the child
                while (contains(child, secondParent[currentParentIndex], crossPoint1, crossPoint2)) {
                    currentParentIndex++;
                }
                child[i] = secondParent[currentParentIndex];
                currentParentIndex++;
            }
        }
        // Set the representation to the child
        oChild.getSolutionRepresentation().setSolutionRepresentation(child);
        oChild.setObjectiveFunctionValue(this.objfunc.getObjectiveFunctionValue(oChild.getSolutionRepresentation()));
        System.out.println(oChild.getObjectiveFunctionValue());

        // Optionally, evaluate and return the quality of the new solution
        return oChild.getObjectiveFunctionValue();
    }
    private boolean contains(int[] array, int value, int start, int end) {
        for (int i = 0; i < array.length; i++) {
            if ((i < start || i > end) && array[i] == value) {
                return true;
            }
        }
        return false;
    }
}
