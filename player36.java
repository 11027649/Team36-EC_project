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
	int population_size;
	int tournament_size;
	int amount_parents;
	int num_of_mutations;
	int num_of_unchanged_best;
	int max_of_unchanged_best;
	int num_of_clusters;
	boolean mutate_big;
	boolean multiple_parents;

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

			boolean bentCigar = !(isMultimodal || hasStructure || isSeparable);
			boolean schaffers = isMultimodal && hasStructure && !isSeparable;
			boolean katsuura = isMultimodal && !(hasStructure || isSeparable);

			// Do sth with property values, e.g. specify relevant settings of your algorithm
	    if (bentCigar) {
				population_size = 100;
				tournament_size = 25;
				num_of_mutations = 10;
				num_of_unchanged_best = 0;
				max_of_unchanged_best = 200;
				num_of_clusters = 5;
				mutate_big = false;

				// 3 parents (more efficient evolution?)
				multiple_parents = true;

				// if multiple parents = true, the amount of parents must be in the table of 3, else, it must be even
				amount_parents = 9;
			}
			else if (schaffers) {
				population_size = 100;
				tournament_size = 25;

				num_of_mutations = 10;
				num_of_unchanged_best = 0;
				max_of_unchanged_best = 200;
				num_of_clusters = 5;
				mutate_big = false;

				multiple_parents = true;
				amount_parents = 9;
			}
			else if (katsuura) {
				population_size = 250;
				tournament_size = 50;

				num_of_mutations = 10;
				num_of_unchanged_best = 0;
				max_of_unchanged_best = 200;
				num_of_clusters = 5;
				mutate_big = false;

				multiple_parents = true;
				amount_parents = 9;
			}
    }

	public void run() {

		// Create population of 100 ppl, each person has 10 gens
		double population[][] = create_population(population_size);

		// Initialize random individuals that function as the initial cluster means
		double clusters[][] = create_population(num_of_clusters);

		for (int i = 0; i < clusters.length; i++){
			System.out.println(Arrays.toString(clusters[i]));
		}

		// Determine fitness for each child in population
		double survival_chances[][] = score_checker(population);

		// sort algorithm that sorts the children on fitness from min to max
		double sorted_survival_chances[][] = sort_survival_chances(survival_chances);

		// Arange children to clusters
		survival_chances = arange_children_to_clusters(population, survival_chances, clusters);
		clusters = rearange_clusters(population, survival_chances, clusters);
		// for (int i = 0 ; i < clusters.length; i++){
		// 	System.out.println(Arrays.toString(clusters[i]));
		// }
		// survival_chances = arange_children_to_clusters(population, survival_chances, clusters);

		// Calculate fitness
		while (evals < 20) {
		// System.out.println(evaluations_limit_);

		// while (evals < evaluations_limit_) {

			double old_best_person = sorted_survival_chances[sorted_survival_chances.length -1][0];

			// function input is the list of n random parents. It selects 2 parents from the n input parents.
			double[][] parents = tournament_parent_selection(amount_parents, tournament_size, sorted_survival_chances);
			parents[parents.length-1] = sorted_survival_chances[sorted_survival_chances.length-1];

			double[][] new_children;

			if (multiple_parents) {
				new_children = create_children_from_multiple_parents(population, parents);
			} else {
				new_children = create_n_children(population, parents);
			}

			System.out.println(mutate_big);
			if (mutate_big) {

				double chance = get_random_double(0, 1);
				if (chance < 0.5) {
					new_children = inversion_mutation(new_children);
				} else {
					new_children = insertion_mutation(new_children);
				}
			} else {
				new_children = small_mutation(new_children, num_of_mutations);
			}
			population = survivor_selection(sorted_survival_chances, population, new_children);

			// Sort algorithm from min to max fitness
			sorted_survival_chances = update_new_children_score(sorted_survival_chances, new_children);
			System.out.println(evals);

			double new_best_person = sorted_survival_chances[sorted_survival_chances.length-1][0];

			// If there's no new best score for "num_of_unchanged_best", perform bigger mutations on the children
			if (new_best_person == old_best_person) {
				num_of_unchanged_best ++;
				if (num_of_unchanged_best == max_of_unchanged_best){
					mutate_big = true;
				}
			} else {
				num_of_unchanged_best = 0;
				mutate_big = false;
			}

			// print_average_score(sorted_survival_chances);
		}
		// survival_chances = arange_children_to_clusters(population, survival_chances, clusters);
		// for (int i = 0; i < survival_chances.length; i++) {
		// 	System.out.println(Arrays.toString(sorted_survival_chances[i]));
		// clusters = rearange_clusters(population, survival_chances, clusters);

		// for (int i = 0; i < clusters.length; i++) {
		// 	System.out.print(i);
		// 	System.out.println(Arrays.toString(clusters[i]));

		
	}



	// Part 1 of the k-means clustering
	// The children are assigned to a cluster
	public double[][] arange_children_to_clusters(double[][] children, double[][] survival_chances, double[][] clusters) {

		// loop throw all kids
		for (int i = 0; i < children.length; i++) {

			// search the index number
			int child_index = (int) survival_chances[i][1];

			// find the corresponding genes
			double[] child = children[child_index];


			double childs_min = 1000;
			int cluster_num = -1;

			// determine in wich cluster the child fitst the best
			for (int j = 0; j < clusters.length; j++) {
	
				double[] cluster = clusters[j];

				double dist = 0.0;

				// compare 1 gene of the kid with 10 genes from the cluster... @ Tobais, I think this is wrong
				for (int k = 0; k < child.length; k++){

					// take the square distance
					dist = dist + (child[k] - cluster[k]) * (child[k] - cluster[k]);
				}

				dist = Math.sqrt(dist);

				if (dist < childs_min) {
					childs_min = dist;
					cluster_num = j;
				}
			}
			survival_chances[i][2] = cluster_num;
		}
		return survival_chances;
	}

	// Part 2 of the k-means clustering
	// The clusters means are updated
	public double[][] rearange_clusters(double[][] children, double[][] survival_chances, double[][] clusters) {


		for (int g= 0; g < clusters.length; g++){
				System.out.println("tijdelijkse clusters");
				System.out.println(Arrays.toString(clusters[g]));
			}

		double[] cluster_tot = new double[10];
		double kids_in_cluster;

		// for 5 clusters
		for (int i = 0; i < clusters.length; i++) {

			System.out.println(i);
			for (int g= 0; g < clusters.length; g++){
				System.out.println(Arrays.toString(clusters[g]));
			}
			
			// Place zeros in cluster total for calculating mean
			for (int c = 0; c < cluster_tot.length; c++){
				cluster_tot[c] = 0;
			}

			kids_in_cluster = 0;

			// loop throw all kids and place them in the right cluster
			for (int j = 0; j < survival_chances.length; j++){

				// take the row from surv. changes
				double[] row = survival_chances[j];

				// take the cluster number
				int clust_num = (int) row[2];
				// System.out.println(clust_num);
				// if the child you are looking at is cluster i
				if (clust_num == i) {

					int index = (int) row[1];

					// add to the cluster with the genes from the child you are looking at
					for (int c = 0; c < cluster_tot.length; c++){
						cluster_tot[c] = cluster_tot[c] + children[index][c];
					}
					// cluster_tot = cluster_tot + children[index];
					kids_in_cluster = kids_in_cluster + 1;
				}
			}

			if (kids_in_cluster > 0.0 ) {
				// System.out.println		kids_in_cluster);

				// calculate the new cluster mean
				for (int c = 0; c < cluster_tot.length; c++) {
					cluster_tot[c] = cluster_tot[c] /		kids_in_cluster;
				}

				clusters[i] = cluster_tot;
				// System.out.println(Arrays.toString(clusters[i]));
				System.out.println(i);
				for (int g= 0; g < clusters.length; g++){
				System.out.println(Arrays.toString(clusters[g]));
			}
			}
		}
		
		for (int g= 0; g < clusters.length; g++){
			System.out.println("uiteindeloijkse@@@@@@@@@ clusters");
			System.out.println(Arrays.toString(clusters[g]));
		}
		// for (int i = 0; i < clusters.length; i++);{
		// 	System.out.println(Arrays.toString(clusters[i]));	
		// }

		return clusters;
	}

	public double[][] create_children_from_multiple_parents(double[][] population, double[][] parents) {
		double[][] children = new double[parents.length][10];

		for (int i = 0; i < parents.length; i += 3) {
			double[][] temp_children = create_three_children(population[(int) parents[i][1]],
																												population[(int) parents[i + 1][1]],
																												population[(int) parents[i + 2][1]]);
			children[i] = temp_children[0];
			children[i + 1] = temp_children[1];
			children[i + 2] = temp_children[2];
		}

		return children;
	}

	public double[][] inversion_mutation(double [][] new_kids) {

		double individual_kid[] = new double[10];

		for (int i = 0; i < new_kids.length; i++){
			individual_kid = new_kids[i];

			int random_numbers [] = printRandomNumbers(2, 9);
			int begin = random_numbers[0];
			int end = random_numbers[1];

			if (begin > end) {
				int temp = begin;
				begin = end;
				end = temp;
			}

			for (int j = begin; j < Math.ceil((begin + end + 1) / 2); j++) {
					double temp_gen = individual_kid[j];
					individual_kid[j] = individual_kid[end - j + begin];
					individual_kid[end - j + begin] = temp_gen;
				}
			new_kids[i] = individual_kid;
		}
		return new_kids;
	}

	public double[][] insertion_mutation(double[][] new_children) {

		for (int i = 0; i < new_children.length; i++) {
			int[] indices = printRandomNumbers(2, 9);
			if (indices[0] > indices[1]) {
				int temp = indices[0];
				indices[0] = indices[1];
				indices[1] = temp;
			}

			double[] backup = new double[new_children[i].length];
			for (int j = 0; j < new_children[i].length; j++) {
				backup[j] = new_children[i][j];
			}

			new_children[i][indices[0] + 1] = new_children[i][indices[1]];

			for (int j = indices[0] + 2; j < new_children[i].length; j++) {
				if (i > indices[1]) {
					new_children[i][j] = backup[j];
				} else {
					new_children[i][j] = backup[j - 1];
				}
			}
		}

		return new_children;
	}

	public void print_average_score(double[][] sorted_survival_chances) {
		double total = 0.0;
		for (int i = 0; i < sorted_survival_chances.length; i++){
			total = total + sorted_survival_chances[i][0];
		}
		double average = total / sorted_survival_chances.length;
		System.out.print("avg: ");
		System.out.println(average);
		System.out.print("best: ");
		System.out.println(sorted_survival_chances[sorted_survival_chances.length-1][0]);
	}

	public double[][] update_new_children_score(double[][] survival_chances, double[][] new_children) {

		for (int i = 0; i < new_children.length; i++) {
			// Get index of those to replace
			Double fitness = (double) evaluation_.evaluate(new_children[i]);
			evals++;
			survival_chances[i][0] = fitness;

		}

		double sorted_survival_chances[][] = new double[new_children.length][2];
		sorted_survival_chances = sort_survival_chances(survival_chances);
		return sorted_survival_chances;

	}

	//	Lil mutation
	public double[][] small_mutation(double [][] new_kids, int num_of_mutations) {
		for (int i = 0; i < new_kids.length; i++){
			double individual_kid[] = new_kids[i];

			// create a random digit between 0 and 9
			int random_numbers [] = printRandomNumbers(num_of_mutations, 9);

			for (int j = 0; j < random_numbers.length; j++) {
				double r = get_random_double(-1,1);
				individual_kid[j] = individual_kid[j] + r;
				if (individual_kid[j] > 5 || individual_kid[j] < -5) {
					individual_kid[j] = individual_kid[j] - r - r;
				}
			}
			new_kids[i] = individual_kid;
		}

		return new_kids;
	}

	//
	public double[][] select_n_random_elements(int n, double[][] sort_list) {

		// Select n random integers between zero and a maximum value.
		int random_number_list[] = printRandomNumbers(n,99);

		double random_n_elements[][] = new double[n][];

		// Save the n random elements
		for (int i = 0; i < random_number_list.length; i++) {

			// Put the index number in a variable
			int index_number = random_number_list[i];
			random_n_elements[i] = sort_list[index_number];
		}
		return random_n_elements;
	}

	public double[][] select_parents(double[][] random_n_elements) {

		double[][] parents = new double[2][2];
		double[][] sorted = sort_survival_chances(random_n_elements);
		parents[0] = sorted[random_n_elements.length - 1];
		parents[1] = sorted[random_n_elements.length - 2];
		return parents;
	}

	public double[] select_single_parent(double[][] random_n_elements) {
		double[][] sorted = sort_survival_chances(random_n_elements);
		double[] parent = sorted[random_n_elements.length - 1];
		return parent;
	}

	public double[][] tournament_parent_selection(int amount_of_parents, int n, double[][] sorted_survival_chances) {
		// amount of parents is how many parents should be selected to make
		// the same number of children. two parents make two children. chidlren have max two paretns.
		// n = how large the pool of randomly selected elements that will form the parents should be.

		double[][] parents = new double[amount_of_parents][2];
		for (int i = 0; i < amount_of_parents; i++) {
			double[][] parents_pool = select_n_random_elements(n, sorted_survival_chances);
			parents[i] = select_single_parent(parents_pool);

		}

		return  parents;
	}

	public double[][] create_n_children(double[][] population, double[][] parents) {
		double[][] children = new double[parents.length][10];

		for (int i = 0; i < parents.length; i += 2) {
			double[][] temp_children = create_two_children(population[(int) parents[i][1]], population[(int) parents[i + 1][1]]);
			children[i] = temp_children[0];
			children[i + 1] = temp_children[1];
		}

		return children;
	}

	public double[][] score_checker( double[][] population) {

		// in this array we place the score and index

		double fitness_index_array[][] = new double[population.length][];

		for (int i = 0; i < population.length; i++) {

			double fit_index_array[] = new double [3];

			// calculate and save the fitness of this individual
			double fitness = (double) evaluation_.evaluate(population[i]);

			// on the ith array at the left side, place the fitness
			fit_index_array[0] = fitness;

			// on the ith array at the middle side, place the index number
			fit_index_array[1] = i;

			// on the ith array at the right side, place the cluster number, which is initially a dummy
			fit_index_array[2] = -1.0;

			// place the array in the big array
			fitness_index_array[i] = fit_index_array;

			evals++;
		}

		return fitness_index_array;
	}

	public double[][] sort_survival_chances(double[][] survival_chances) {
		// sort algorithm that sorts the children on fitness from min to max

		double temp[] = new double[2];
		for (int n = 0; n < survival_chances.length; n++) {
			for (int m = 0; m < survival_chances.length; m++){
				if (survival_chances[n][0] < survival_chances[m][0]) {
					temp = survival_chances[n];
					survival_chances[n] = survival_chances[m];
					survival_chances[m] = temp;
				}
			}
		}
		return survival_chances;

	}

	public double[][] survivor_selection(double[][] sorted_survival_chances, double[][] children, double[][] new_children) {
		if (multiple_parents) {
			for (int i = 0; i < new_children.length; i += 3) {
				// Get index of those to replace
				int kid1 = (int) sorted_survival_chances[i][1];
				int kid2 = (int) sorted_survival_chances[i + 1][1];
				int kid3 = (int) sorted_survival_chances[i + 2][1];
				// Replace worst ones with boy and girl
				children[kid1] = new_children[i];
				children[kid2] = new_children[i + 1];
				children[kid3] = new_children[i + 2];
			}
		} else {
			for (int i = 0; i < new_children.length; i += 2) {
				// Get index of those to replace
				int boy_index = (int) sorted_survival_chances[i][1];
				int girl_index = (int) sorted_survival_chances[i + 1][1];

				// Replace worst ones with boy and girl
				children[boy_index] = new_children[i];
				children[girl_index] = new_children[i + 1];
			}
		}
		return children;

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
	public double[][] create_population(int pop_size) {

		int dim = 10;

		double children[][] = new double[pop_size][];

		// initialize population randomly
		for (int i = 0; i < pop_size; i++) {
			double child[] = new double[dim];

			for (int j = 0; j < dim; j++) {
				double random_double = get_random_double(-5, 5);
				child[j] = random_double;

			}
			children[i] = child;
		}
		return children;
	}

	// Creates two children that mirror each other, and together can form their parents.
	public double[][] create_two_children(double[] mom, double[] dad) {
		// mom/dad is a parents with 10 genes

		// make 10 places per child for the genes
    	double[] boy = new double[10];
	    double[] girl = new double[10];

	    // make 5 int in an array from 0 - 9
	    int[] parent_indices = printRandomNumbers(5, 9);
	    // System.out.println(Arrays.toString(parent_indices));

	    for (int i = 0; i < mom.length; i++) {
	       if (in_parent_indices(parent_indices, i)) {
	          boy[i] = mom[i];
	          girl[i] = dad[i];
	       } else {
	          boy[i] = dad[i];
	          girl[i] = mom[i];
	       }
	    }
	  	// In order to return two lists, make them into one list.
		// IMPORTANT: must take two lists apart again!!!
		double[][] boygirl = new double[2][10];
	   	boygirl[0] = boy;
	   	boygirl[1] = girl;
	   	return boygirl;
	}

	public double[][] create_three_children(double[] parent1, double[] parent2, double[] parent3) {
		// make children array for 3 kids with 10 alleles
		double[][] children = new double[3][10];

		for (int i = 0; i < 3; i ++) {
				double[] kid = new double[10];
				int[] indices = printRandomNumbers(10,9);

				for (int j = 0; j < 10; j ++) {
					if (j < 4) {
							kid[indices[j]] = parent1[indices[j]];
					} else if (j < 7) {
						kid[indices[j]] = parent2[indices[j]];
					} else {
						kid[indices[j]] = parent3[indices[j]];
					}
				}
				children[i] = kid;
		}
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
