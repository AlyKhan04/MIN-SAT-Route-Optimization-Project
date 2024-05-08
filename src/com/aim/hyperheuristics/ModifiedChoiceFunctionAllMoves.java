package com.aim.hyperheuristics;
import java.text.DecimalFormat;
import java.util.Arrays;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/*****************************************************************/
//Implementation of the Modified Choice Function of: John H. Drake, Ender �zcan and Edmund K. Burke.
//An Improved Choice Function Heuristic Selection for Cross Domain Heuristic Search
//The 12th International Conference on Parallel Problem Solving From Nature (PPSN 2012)
//Lecture Notes in Computer Science, Volume 7492, pp. 307-316, 2012.
//Copyright: John H. Drake, 2012
//This code can be freely used for non-commercial purpose.
//Any use of this implementation or a modification of the code
//must acknowledge the work of John H. Drake
/****************************************************************/

public class ModifiedChoiceFunctionAllMoves extends HyperHeuristic {

    /**
     * creates a new ModifiedChoiceFunctionAllMoves object with a random seed
     */
    public ModifiedChoiceFunctionAllMoves(long seed){
        super(seed);
    }

    /**
     * This method defines the strategy of the hyper-heuristic
     * @param problemDomain the problem domain to be solved
     */@Override
    protected void solve(ProblemDomain problemDomain) {
        // Set the memory for storing solutions
        problemDomain.setMemorySize(4);

        // Initialize all solutions in memory
        for (int i = 0; i < 4; i++) {
            problemDomain.initialiseSolution(i);
        }

        double currentCost = problemDomain.getFunctionValue(0);
        int numberOfHeuristics = problemDomain.getNumberOfHeuristics();

        // Arrays to track heuristic performance metrics
        double[] F = new double[numberOfHeuristics];
        double[] f1 = new double[numberOfHeuristics];
        double[][] f2 = new double[numberOfHeuristics][numberOfHeuristics];
        double[] f3 = new double[numberOfHeuristics];
        Arrays.fill(f3, Double.MAX_VALUE);  // Initialize f3 with high values to give non-crossover heuristics a chance initially

        // Main loop until time expires
        while (!hasTimeExpired()) {
            int heuristicToApply = selectHeuristic(F);  // Method to select heuristic based on the F scores
            double newObjValue = problemDomain.applyHeuristic(heuristicToApply, 0, 0); // Apply heuristic and get new objective value

            // Update F scores based on the performance of the applied heuristic
            updateFScores(f1, f2, f3, heuristicToApply, currentCost, newObjValue, getElapsedTime());

            // Update the best known solution cost if improved
            if (newObjValue < currentCost) {
                currentCost = newObjValue;
            }
        }
    }

    private int selectHeuristic(double[] F) {
        int selectedHeuristic = 0;
        double bestScore = -Double.MAX_VALUE;
        for (int i = 0; i < F.length; i++) {
            if (F[i] > bestScore) {
                bestScore = F[i];
                selectedHeuristic = i;
            }
        }
        return selectedHeuristic;
    }

    private void updateFScores(double[] f1, double[][] f2, double[] f3, int heuristic, double oldCost, double newCost, long elapsedTime) {
        double improvement = oldCost - newCost;
        f1[heuristic] += improvement;  // Example update, should be adjusted according to your specific formula
        // Add other updates for f2, f3 as per your heuristic logic
    }
    /**
     * this method must be implemented, to provide a different name for each hyper-heuristic
     * @return a string representing the name of the hyper-heuristic
     */
    public String toString() {
        return "Modified Choice Function - All Moves";
    }

    /**
     * this method is introduced to combat some rounding issues introduced by Java
     * @return a double of the input d, rounded to two decimal places
     */
    public double roundTwoDecimals(double d) {
        DecimalFormat two_d_form = new DecimalFormat("#.##");
        return Double.valueOf(two_d_form.format(d));
    }
}
