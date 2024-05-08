package com.aim.hyperheuristics;
import java.text.DecimalFormat;

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
     * @param problem the problem domain to be solved
     */
    public void solve(ProblemDomain problem) {
        //initialise the default solution and supporting variables
        problem.initialiseSolution(0);
        double phi = 0.50, delta = 0.50;
        int heuristic_to_apply = 0, init_flag = 0;
        double new_obj_function_value = 0.00;
        long time_exp_before, time_exp_after, time_to_apply;
        int number_of_heuristics = problem.getNumberOfHeuristics();
        double current_obj_function_value = problem.getFunctionValue(0);
        double best_heuristic_score = 0.00, fitness_change = 0.00, prev_fitness_change = 0.00;
        double[] F = new double[number_of_heuristics], f1 = new double[number_of_heuristics], f3 = new double[number_of_heuristics];
        double[][] f2 = new double[number_of_heuristics][number_of_heuristics];
        int last_heuristic_called = 0;
        int[] crossover_heuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
        for (int i = 0; i < crossover_heuristics.length;i++) {//Give crossover no chance of being selected
            f3[crossover_heuristics[i]]=-1.00/0.00;
        }
        while (!hasTimeExpired()) { //main loop which runs until time has expired
            if (init_flag > 1) { //flag used to select heuristics randomly for the first two iterations
                best_heuristic_score = 0.0;
                for (int i = 0; i < number_of_heuristics; i++) {//update F matrix
                    F[i] = phi * f1[i] + phi * f2[i][last_heuristic_called] + delta * f3[i];
                    if (F[i] > best_heuristic_score) {
                        heuristic_to_apply = i;
                        best_heuristic_score = F[i];
                    }
                }
            }
            else {
                //unpleasant way to check crossover not initially selected randomly
                boolean crossflag = true;
                while(crossflag){
                    heuristic_to_apply = rng.nextInt(number_of_heuristics);
                    crossflag = false; //assume not crossover before checking if it is
                    for (int i = 0; i < crossover_heuristics.length;i++) {
                        if(heuristic_to_apply == crossover_heuristics[i]){
                            crossflag = true;
                        }
                    }
                }
            }

            //apply the chosen heuristic to the solution at index 0 in the memory and replace it immediately with the new solution
            time_exp_before = getElapsedTime();
            new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, 0, 0);
            time_exp_after = getElapsedTime();
            time_to_apply = time_exp_after - time_exp_before + 1; //+1 prevents / by 0

            //calculate the change in fitness from the current solution to the new solution
            fitness_change = current_obj_function_value - new_obj_function_value;

            //set the current objective function value to the new function value as the new solution is now the current solution
            current_obj_function_value = new_obj_function_value;

            //update f1, f2 and f3 values for appropriate heuristics
            //first two iterations dealt with separately to set-up variables
            if (init_flag > 1) {
                f1[heuristic_to_apply] = fitness_change / time_to_apply + phi * f1[heuristic_to_apply];
                f2[heuristic_to_apply][last_heuristic_called] = prev_fitness_change + fitness_change / time_to_apply + phi * f2[heuristic_to_apply][last_heuristic_called];
            } else if (init_flag == 1) {
                f1[heuristic_to_apply] = fitness_change / time_to_apply;
                f2[heuristic_to_apply][last_heuristic_called] = prev_fitness_change + fitness_change / time_to_apply + prev_fitness_change;
                init_flag++;
            } else { //i.e. init_flag = 0
                f1[heuristic_to_apply] = fitness_change / time_to_apply;
                init_flag++;
            }
            for (int i = 0; i < number_of_heuristics; i++) {
                f3[i] += time_to_apply;
            }
            f3[heuristic_to_apply] = 0.00;

            if (fitness_change > 0.00) {//in case of improvement
                phi = 0.99;
                delta = 0.01;
                prev_fitness_change = fitness_change / time_to_apply;
            } else {//non-improvement
                if (phi > 0.01) {
                    phi -= 0.01;
                }
                phi = roundTwoDecimals(phi);
                delta = 1.00 - phi;
                delta = roundTwoDecimals(delta);
                prev_fitness_change = 0.00;
            }
            last_heuristic_called = heuristic_to_apply;
        }
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
