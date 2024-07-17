## Description of Project
This is an AI MIN-SAT project pertaining to optimizing solutions found inside a search space utilizing Hyflex. <br>
The problem instance was optimizing the route between any number of locations. <br>
The coursework required the creation and implementation of various heuristics, metaheuristics and hyperhueristics that we could use to traverse the search space. <br>
Heuristics/MetaHeuristics created were: Adjacent Swap, Davis Bit Hill Climbing, Greedy Search, Next Descent, Ordered Crossover, Reinsertion and Partially Mapped Crossover.<br>
HyperHeuristics created were: Tournament Selection and Greedy Search. <br>

## Breakdown of Heuristics: 
For more detailed information about how each heuristic works, check the directory heuristics, everything is documented <br>
**Adjacent Swap:** Swaps two adjacent bits and repeats this based on given intensity of mutation, calculates and returns the objective value of the new solution congiguration.<br>
**Davis Bit Hill Climbing:** Swaps two adjacent bits and permutates the solutoon based on depth of search variable, calculates new objective value and if lower than previous obj function value returns the new value.<br>
**Greedy Search:** Attempts to find bettering solution in MaxAttempts calulated by a formula (Explained in code) then swaps random elements of the solution representation, when a better solution is found, it is returned.<br>
**Next Descent:** Flips each bit iteratively and returns the first solution representation that returns a better objective value. <br>
**Ordered Crossover:** Retrieves two parent solutions, determines crossover points, copies segment from first parent to corresponding position in the child. Fill remaining positions in child with elements from the second parent. Returns objective function value and solution representation of child. <br>
**Partially Mapped Crossover:** Randomly selects two crossover points. Ensures the first crossover point is less than the second crossover point. Copies the segment of the first parent between the two crossover points to the corresponding positions in the child. Creates a mapping of values between the copied segment of the first parent and the corresponding segment of the second parent. Fills the remaining positions in the child with elements from the second parent, resolving any conflicts using the mapping created. <br>
**Reinsertion:** Calculates the number of reinsertions based on the intensity of mutation. For each reinsertion, randomly select an index to remove and reinsert an element, updating the array accordingly. Returns Objective Function value. <br>

## Breakdown of HyperHeuristics: 
**Greedy Search:** There is a main search loop that runs for a certain amount of time that can be set by user. One loop consists of running each heuristic, calculating the objective function value returned from each heuristic and picking the best objective function value and solution representation in that iteration. This then repeats until the time completes and returns a solution that is either optimal or close to oprimal.<br>
**Tournament Selection:** Sets the memory size of the problem domain to 4. Initializes an array to keep track of heuristic scores, starting each score at 1. Defines indices for the current solution (currentIndex), a candidate solution (candidateIndex), and the best solution (bestIndex). Runs the main loop until a time limit is reached. Randomly selects a heuristic based on a tournament of heuristics. Applies the selected heuristic and evaluate the candidate solution's cost. If the candidate solution is better than the current best, update the best solution. If so, swaps the currentIndex and candidateIndex to use the new solution in the next iteration. Adjusts the score of the heuristic based on whether it improved the solution. This score is then used in the next iteration of the tournament. <br>
