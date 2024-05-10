package com.aim.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.SolutionPrinter;
import com.aim.UZFDomain;
import com.aim.interfaces.UAVSolutionInterface;

import java.util.Arrays;
import java.util.Collections;

public class TournamentSelection extends HyperHeuristic  {
    private static final int SECOND_PARENT_INDEX = 2;

    private static final int BEST_ACCEPTED_INDEX = 3;

    public TournamentSelection(long lSeed) {

        super(lSeed);
    }

    @Override
    protected void solve(ProblemDomain oProblem) {
        // Set memory size to 5 slots (0 through 4)
        oProblem.setMemorySize(4);

        // Initialize arrays and indices
        int numberOfHeuristics = oProblem.getNumberOfHeuristics();
        int[] heuristicScores = new int[numberOfHeuristics];
        Arrays.fill(heuristicScores, 1);
        int currentIndex = 0, candidateIndex = 1, bestIndex = 3;

        // Initialize the solution and set the best solution index
        oProblem.initialiseSolution(currentIndex);
        oProblem.copySolution(currentIndex, bestIndex);
        double bestCost = oProblem.getFunctionValue(currentIndex);
        double currentCost = bestCost;

        // Identify crossover heuristics
        boolean[] isCrossover = new boolean[numberOfHeuristics];
        Arrays.fill(isCrossover, false);
        System.out.println(isCrossover);
        for (int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {
            isCrossover[i] = true;
        }

        // Main optimization loop
        double candidateCost;
        while (!hasTimeExpired()) {
            int tournamentSize = rng.nextInt(numberOfHeuristics);
            int[] tournament = createRandomPermutation(numberOfHeuristics);
            int h = tournament[0];
            int bestscore = heuristicScores[tournament[0]];
            for (int i = 0; i < tournamentSize; i++) {
                if (heuristicScores[tournament[i]] < bestscore) {
                    bestscore = heuristicScores[tournament[i]];
                    h = tournament[i];
                }
            }

            // Apply heuristic
            if (isCrossover[h]) {
                if (rng.nextBoolean()) {
                    oProblem.initialiseSolution(2);  // Use index 2 for the second parent
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, 2, candidateIndex);
                } else {
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, bestIndex, candidateIndex);
                }
            } else {
                candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
            }

            // Update best solution if found
            if (candidateCost < bestCost) {
                oProblem.copySolution(candidateIndex, bestIndex);
                bestCost = candidateCost;
            }

            // Swap indices for the next iteration
            int temp = currentIndex;
            currentIndex = candidateIndex;
            candidateIndex = temp;

            // Update heuristic scores
            if (candidateCost <= currentCost) {
                heuristicScores[h]++;
                currentCost = candidateCost;
            } else {
                heuristicScores[h]--;
            }
        }

        UAVSolutionInterface oSolution = ((UZFDomain) oProblem).getBestSolution();
        SolutionPrinter oSolutionPrinter = new SolutionPrinter("out.csv");
        oSolutionPrinter.printSolution( ((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(oSolution));
    }

    public int[] createRandomPermutation(int length) {
        Integer[] perm = new Integer[length];
        for (int i = 0; i < length; i++) {
            perm[i] = i;
        }
        Collections.shuffle(Arrays.asList(perm));
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = perm[i];
        }
        return result;
    }

    @Override
    public String toString() {

        return "Reinforcement";
    }
}