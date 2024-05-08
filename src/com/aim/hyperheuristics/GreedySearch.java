//package com.aim.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.SolutionPrinter;
import com.aim.UZFDomain;
import com.aim.interfaces.UAVSolutionInterface;

import java.util.Arrays;

/*public class GreedySearch extends HyperHeuristic {

    private static final int SECOND_PARENT_INDEX = 2;

    private static final int BEST_ACCEPTED_INDEX = 3;

    public GreedySearch(long lSeed) {

        super(lSeed);
    }
    @Override
    protected void solve(ProblemDomain problemDomain) {
        problemDomain.setMemorySize(4);

        int currentIndex = 0;
        int candidateIndex = 1;
        problemDomain.initialiseSolution(currentIndex);
        problemDomain.copySolution(currentIndex, BEST_ACCEPTED_INDEX);  // Best known solution copy
        double bestCost = problemDomain.getFunctionValue(currentIndex);

        int numberOfHeuristics = problemDomain.getNumberOfHeuristics();
        boolean[] isCrossover = new boolean[numberOfHeuristics];
        Arrays.fill(isCrossover, false);


        }



        // Main search loop

    }


    @Override
    public String toString() {
        return "Greedy Search";
    }
}*/
