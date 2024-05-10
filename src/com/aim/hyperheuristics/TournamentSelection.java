package com.aim.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.SolutionPrinter;
import com.aim.UZFDomain;
import com.aim.interfaces.UAVSolutionInterface;

import java.util.Arrays;

public class TournamentSelection extends HyperHeuristic  {
    private static final int SECOND_PARENT_INDEX = 2;

    private static final int BEST_ACCEPTED_INDEX = 3;

    public TournamentSelection(long lSeed) {

        super(lSeed);
    }

    @Override
    protected void solve(ProblemDomain oProblem) {
        //sets the size of memory
        oProblem.setMemorySize(4);
        //creates an array with the number of heuristics
        int numberOfHeuristics = oProblem.getNumberOfHeuristics();
        int[] heuristicScores = new int[numberOfHeuristics];
        Arrays.fill(heuristicScores, 1);
        //creates two indices with the solution intiialized and a copy solution function created
        int currentIndex = 0;
        int candidateIndex = 1;
        //Initialises the solution
        oProblem.initialiseSolution(currentIndex);
        oProblem.copySolution(currentIndex, BEST_ACCEPTED_INDEX);
        //brings the original solution cost
        double currentCost = oProblem.getFunctionValue(currentIndex);
        // Creates boolean array to identify crossover Heuristics
        boolean[] isCrossover = new boolean[numberOfHeuristics];
        Arrays.fill(isCrossover, false);
        //sets a boolean array to true for each array with which crossover is true
        for(int i : oProblem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER)) {
            isCrossover[i] = true;
        }
        // main search loop
        double candidateCost;
        //Creates a while loop
        while(!hasTimeExpired()) {
            //Creates a random tournament size
            int tournamentSize = rng.nextInt(numberOfHeuristics);
            //Creates a permutation
            int[] tournament = createRandomPermutation(numberOfHeuristics);
            int h = tournament[0];
            //Chooses a heuristic that returns h with the best tournment score
            int bestscore = heuristicScores[tournament[0]];
            for (int i = 0; i < tournamentSize; i++) {
                if (heuristicScores[tournament[i]] < bestscore) {
                    bestscore = heuristicScores[i];
                    h = i;
                }
            }
            //If the heuristic is a crossover heuristic
            if(isCrossover[h]) {
                if(rng.nextBoolean()) {
                    // randomly choose between crossover with newly initialised solution
                    oProblem.initialiseSolution(SECOND_PARENT_INDEX);
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, SECOND_PARENT_INDEX, candidateIndex);
                } else {
                    // or with best solution accepted so far
                    candidateCost = oProblem.applyHeuristic(h, currentIndex, BEST_ACCEPTED_INDEX, candidateIndex);
                }
            } else {
                candidateCost = oProblem.applyHeuristic(h, currentIndex, candidateIndex);
            }

            // Evaluation Function
            if(candidateCost < currentCost) {
                oProblem.copySolution(candidateIndex, BEST_ACCEPTED_INDEX);
            }
            if (candidateCost <= currentCost){
                heuristicScores[h]++;
                currentCost = candidateCost;
                currentIndex = 1 - currentIndex;
                candidateIndex = 1 - candidateIndex;
            } else {
                heuristicScores[h]--;
            }
        }

        UAVSolutionInterface oSolution = ((UZFDomain) oProblem).getBestSolution();
        SolutionPrinter oSolutionPrinter = new SolutionPrinter("out.csv");
        oSolutionPrinter.printSolution( ((UZFDomain) oProblem).getLoadedInstance().getSolutionAsListOfLocations(oSolution));
    }

    public int[] createRandomPermutation (int length) {
        int[] perm = new int[length];
        for (int i = 0; i < length; i++) {
            perm[i] = i;
        }
        java.util.Collections.shuffle(Arrays.asList(perm));
        return perm;
    }

    @Override
    public String toString() {

        return "Reinforcement";
    }
}