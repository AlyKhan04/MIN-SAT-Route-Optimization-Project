package com.aim.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.SolutionPrinter;
import com.aim.UZFDomain;
import com.aim.interfaces.UAVSolutionInterface;

public class GreedySearch extends HyperHeuristic {

    public GreedySearch(long seed) {
        super(seed);
    }

    @Override
    protected void solve(ProblemDomain problemDomain) {
        problemDomain.setMemorySize(4);

        int currentIndex = 0;
        problemDomain.initialiseSolution(currentIndex);
        problemDomain.copySolution(currentIndex, 1);  // Best known solution copy
        double bestCost = problemDomain.getFunctionValue(currentIndex);

        int numberOfHeuristics = problemDomain.getNumberOfHeuristics();

        // Main search loop
        while (!hasTimeExpired()) {
            int bestHeuristic = -1;
            double bestHeuristicCost = Double.POSITIVE_INFINITY;

            // Evaluate all heuristics to find the most effective one
            for (int h = 0; h < numberOfHeuristics; h++) {
                // Apply heuristic temporarily
                problemDomain.copySolution(currentIndex, 2); // Temporary candidate index
                double candidateCost = problemDomain.applyHeuristic(h, currentIndex, 2);

                // Check if the heuristic provides a better solution
                if (candidateCost < bestHeuristicCost) {
                    bestHeuristic = h;
                    bestHeuristicCost = candidateCost;
                }
            }

            // If an improvement heuristic was found, apply it for real
            if (bestHeuristic != -1 && bestHeuristicCost < bestCost) {
                bestCost = bestHeuristicCost;
                problemDomain.applyHeuristic(bestHeuristic, currentIndex, 1);
                problemDomain.copySolution(1, currentIndex);  // Set the best found as the new current
            }
        }

        // Optional: Use this solution or print it
        UAVSolutionInterface bestSolution = ((UZFDomain) problemDomain).getBestSolution();
        SolutionPrinter solutionPrinter = new SolutionPrinter("out.csv");
        solutionPrinter.printSolution(((UZFDomain) problemDomain).getLoadedInstance().getSolutionAsListOfLocations(bestSolution));
    }

    @Override
    public String toString() {
        return "Greedy Search Hyper-Heuristic";
    }
}
