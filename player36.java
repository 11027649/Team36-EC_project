import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashSet;
import java.util.Set;

import java.lang.Math;

public class player36 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

    public int evals = 0;

	public player36()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
			evaluation_ = evaluation;

			// Get evaluation properties
			Properties props = evaluation.getProperties();
	    // Get evaluation limit
	    evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

			// Property keys depend on specific evaluation
			// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
	    boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
	    boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
	    boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

			// Do sth with property values, e.g. specify relevant settings of your algorithm
	    if(isMultimodal){
	        // Do sth
	    } else{
	        // Do sth else
	    }
    }

	public void run() {
		// Run your algorithm here
		int dimensions = 10;
		int pop_size = 100;

		int amount_parents = 10;
		int parent_candidates = 10;

		int amount_mutations = 10;

		// Create population of 100 ppl, each person has 10 genes
		ArrayList<Individual> population = create_population(dimensions, pop_size);

		// Sort algorithm that sorts the children on fitness from min to max
		ArrayList<Individual> sorted_population = sort_arraylist(population);

		while (evals < evaluations_limit_) {

			// function input is the list of n random parents. It selects 2 parents from the n input parents.
			ArrayList<Individual> parents = tournament_parent_selection(amount_parents, parent_candidates, sorted_population);

			// Best individual will be a parent.
			// parents.add(sorted_population.get(sorted_population.size() - 1));

			ArrayList<Individual> new_children = create_n_children(population, parents);

			// Mutate new children.
			new_children = small_mutation(new_children, amount_mutations);

			// Calculate fitness after mutating.
			for (int i = 0; i < new_children.size(); i++) {
				Individual kid = new_children.get(i);
				double kidFitness = (double) evaluation_.evaluate(kid.genome);
				evals = kid.setFitness(evals, kidFitness);
			}

			// Replace worst individuals in population with new kids.
			population = replace_worst(sorted_population, new_children);

			// Sort population again (with new children).
			sorted_population = sort_arraylist(population);

			print_average_score(sorted_population);
		}

	}

	public void print_average_score(ArrayList<Individual> sorted_population) {
		double total = 0.0;

		for (int i = 0; i < sorted_population.size(); i++) {
			total = total + sorted_population.get(i).fitness;
		}

		double average = total / sorted_population.size();

		System.out.print("avg: ");
		System.out.println(average);
		System.out.print("best: ");
		System.out.println(sorted_population.get(sorted_population.size() - 1).fitness);
	}

	public ArrayList<Individual> small_mutation(ArrayList<Individual> new_kids, int num_of_mutations) {

		for (int i = 0; i < new_kids.size(); i++){
			Individual kid = new_kids.get(i);

			// create a random digit between 0 and 9
			int random_numbers [] = printRandomNumbers(num_of_mutations, 9);

			for (int j = 0; j < random_numbers.length; j++) {
				double r = get_random_double(-1,1);

				kid.genome[j] = kid.genome[j] + r;

				if (kid.genome[j] > 5 || kid.genome[j] < -5) {
					kid.genome[j] = kid.genome[j] - r - r;
				}
			}

			new_kids.set(i, kid);
		}

		// Swappen kan met hetzelfde kid.
		return new_kids;
	}

	public ArrayList<Individual> select_candidates(int amount_candidates, ArrayList<Individual> sorted_population) {

		// Select n random integers between zero and a maximum value.
		int random_numbers[] = printRandomNumbers(amount_candidates,99);

		ArrayList<Individual> candidates = new ArrayList<Individual>();

		// Save the n random candidates.
		for (int i = 0; i < random_numbers.length; i++) {
			int index_number = random_numbers[i];
			candidates.add(sorted_population.get(index_number));
		}

		return candidates;
	}

	public Individual select_single_parent(ArrayList<Individual> parents_pool) {
		ArrayList<Individual> sorted = sort_arraylist(parents_pool);
		Individual best_parent = sorted.get(parents_pool.size() - 1);

		return best_parent;
	}

	public ArrayList<Individual> tournament_parent_selection(int amount_of_parents, int candidates_drawn, ArrayList<Individual> sorted_population) {
		// amount of parents is how many parents should be selected to make
		// the same number of children. two parents make two children. chidlren have max two paretns.
		// n = how large the pool of randomly selected elements that will form the parents should be.

		ArrayList<Individual> parents = new ArrayList<Individual>();

		for (int i = 0; i < amount_of_parents; i++) {
			ArrayList<Individual> parents_pool = select_candidates(candidates_drawn, sorted_population);
			parents.add(select_single_parent(parents_pool));
		}

		String s = "Amount of evals: " + evals;
		System.out.println(s);

		return  parents;
	}

	public ArrayList<Individual> create_n_children(ArrayList<Individual> population, ArrayList<Individual> parents) {
		ArrayList<Individual> children = new ArrayList<Individual>();

		for (int i = 0; i < parents.size(); i += 2) {

			ArrayList<Individual> temp_children = create_two_children(parents.get(i), parents.get(i + 1));
			children.add(temp_children.get(0));
			children.add(temp_children.get(1));

		}

		return children;
	}

	public ArrayList<Individual> sort_arraylist(ArrayList<Individual> population) {
		// sort algorithm that sorts the children on fitness from min to max

		Individual temp_child = new Individual();

		for (int n = 0; n < population.size(); n++) {
			for (int m = 0; m < population.size(); m++){
				if (population.get(n).fitness < population.get(m).fitness) {
					temp_child = population.get(n);
					population.set(n, population.get(m));
					population.set(m, temp_child);
				}
			}
		}
		return population;
	}

	public double[] make_half_half_child(double[] mom, double[] dad) {

		double[] child = new double[10];

		for (int i = 0; i < mom.length; i++) {
			if (i < mom.length / 2) {
				child[i] = mom[i];
			} else {
				child[i] = dad[i];
			}
		}

		return child;
	}

	// TODO write function with DNA library

	// TODO write function who lives, who dies, who tells your story
	// Slechtste twee per rondje gaan dood (want komen er twee bij)

	public ArrayList<Individual> replace_worst(ArrayList<Individual> population, ArrayList<Individual> new_children) {

		for (int i = 0; i < new_children.size(); i++) {

			// Get index of those to replace
			population.set(i, new_children.get(i));
		}

		return population;
	}

	// This is a function that generates random numbers between a range, without repetition
	// http://www.codecodex.com/wiki/Generate_Random_Numbers_Without_Repetition
	public static final Random gen = new Random();
    public static int[] printRandomNumbers(int n, int maxRange) {

    	// n equals amount of numbers
    	// Maxrange equals the maximum number in range

        assert n <= maxRange : "There aren't more unique numbers in this range";

        int[] result = new int[n];
        Set<Integer> used = new HashSet<Integer>();

        for (int i = 0; i < n; i++) {

            int newRandom;
            do {
                newRandom = gen.nextInt(maxRange+1);
            } while (used.contains(newRandom));
            result[i] = newRandom;
            used.add(newRandom);
        }
        return result;
    }

	// Returns a random double between certain values
	public double get_random_double(int min, int max) {
		return (Math.random() * (max - min)) + min;
	}

	// This is a function that initializes the population with random individuals
	public ArrayList<Individual> create_population(int dimensions, int pop_size) {

		ArrayList<Individual> population = new ArrayList<Individual>();

		// initialize population randomly
		for (int i = 0; i < pop_size; i++) {
			Individual child = new Individual();

			for (int j = 0; j < dimensions; j++) {
				double random_double = get_random_double(-5, 5);
				child.genome[j] = random_double;

			}
			double childFitness = (double) evaluation_.evaluate(child.genome);
			evals = child.setFitness(evals, childFitness);

			population.add(child);
		}
		return population;
	}

	// Creates two children that mirror each other, and together can form their parents.
	public ArrayList<Individual> create_two_children(Individual mom, Individual dad) {
		// mom/dad is a parents with 10 genes

		// make 10 places per child for the genes
    	Individual boy = new Individual();
	    Individual girl = new Individual();

	    // make 5 int in an array from 0 - 9
	    int[] parent_indices = printRandomNumbers(5, 9);

	    for (int i = 0; i < parent_indices.length; i++) {
	       if (in_parent_indices(parent_indices, i)) {
	          boy.genome[i] = mom.genome[i];
	          girl.genome[i] = dad.genome[i];
	       } else {
	          boy.genome[i] = dad.genome[i];
	          girl.genome[i] = mom.genome[i];
	       }
	    }
	  	// In order to return two lists, make them into one list.
		// IMPORTANT: must take two lists apart again!!!
		ArrayList<Individual> children = new ArrayList<Individual>();
		children.add(boy);
		children.add(girl);
	  return children;
	}

	public boolean in_parent_indices(int[] parent_indices, int n) {
    	for (int i = 0; i < parent_indices.length; i++) {
    		if (n == parent_indices[i]) {
	             return true;
    		}
    	}
    	return false;
	}

}
