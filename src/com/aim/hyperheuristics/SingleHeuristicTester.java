package com.aim.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.SolutionPrinter;
import com.aim.UZFDomain;
import com.aim.interfaces.UAVSolutionInterface;

public class SingleHeuristicTester extends HyperHeuristic {

    private static final int BEST_ACCEPTED_INDEX = 3;
    private static final int TEST_HEURISTIC_INDEX = 2;  // Example: index of the heuristic to test

    public SingleHeuristicTester(long lSeed) {
        super(lSeed);
    }

    @Override
    protected void solve(ProblemDomain oProblem) {
        oProblem.setMemorySize(4);
        oProblem.initialiseSolution(0);  // Initialize the primary solution
        oProblem.copySolution(0, BEST_ACCEPTED_INDEX);  // Copy it to the best accepted index

        double currentCost = oProblem.getFunctionValue(0);

        // Main search loop
        while(!hasTimeExpired()) {
            // Apply the specific heuristic and store the candidate cost
            double candidateCost = oProblem.applyHeuristic(1, 0, 1);

            // Update best if the new solution is better
            if(candidateCost < currentCost) {
                oProblem.copySolution(1, BEST_ACCEPTED_INDEX);
                currentCost = candidateCost;
            }
        }

        UAVSolutionInterface bestSolution = ((UZFDomain) oProblem).getBestSolution();
        SolutionPrinter solutionPrinter = new SolutionPrinter("out.csv");
        solutionPrinter.printSolution(((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(bestSolution));
    }

    @Override
    public String toString() {
        return "Single Heuristic Tester";
    }
}
