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
	int[] cluster_count_array;
	int num_parents_from_cluster;
	boolean use_k_means;

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
				num_of_clusters = 20;
				num_parents_from_cluster = 1;
				mutate_big = false;
				use_k_means = false;

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
				num_of_clusters = 20;
				num_parents_from_cluster = 2;
				mutate_big = false;
				use_k_means = false;

				multiple_parents = true;
				amount_parents = 9;
			}
			else if (katsuura) {
				population_size = 1000;
				tournament_size = 50;

				num_of_mutations = 10;
				num_of_unchanged_best = 0;
				max_of_unchanged_best = 200;
				num_of_clusters = 100;
				num_parents_from_cluster = 5;
				mutate_big = false;
				use_k_means = false;

				multiple_parents = true;
				amount_parents = 9;
			}
    }

	public void run() {
		// Create population, each individual has 10 genes
		Population population = new Population(population_size);

		// Determine fitness for each child in population
		for (int i = 0; i < population_size; i++) {
			Individual child = population.getIndividual(i);
			double fitness = (double) evaluation_.evaluate(child.genome);

			child.setFitness(fitness);
			evals++;
		}

		// Sort algorithm that sorts the children on fitness from min to max.
		population.sort(population_size);

		if (use_k_means){
			Cluster clusters = new Cluster(num_of_clusters);
			clusters.cluster_convergence(population);
		}

		// Cluster cluster = new Cluster(num_of_clusters);


		// // Initialize random individuals that function as the initial cluster means
		// double clusters[][] = create_population(num_of_clusters);
		// cluster_count_array = new int[num_of_clusters];
		// for (int i = 0; i < num_of_clusters; i++){
		// 	cluster_count_array[i] = 0;
		// }
		//
		// if (use_k_means){
		//
		// 			// Arange population to clusters
		// 			int max_cluster_iterations = 10;
		// 			int iterations_counter = 0;
		// 			int correction_counter;
		// 			do {
		// 				iterations_counter++;
		// 				// Create copy of clusters
		// 				double[][] clusters_clone = new double[num_of_clusters][10];
		// 				for (int i = 0; i < num_of_clusters; i++){
		// 					clusters_clone[i] = Arrays.copyOf(clusters[i],clusters[i].length);
		// 				}
		// 				// Arange all individuals of population to a certain cluster
		// 				survival_chances = arange_children_to_clusters(population, survival_chances, clusters);
		// 				// Rearange clusters by recalculating the mean
		// 				clusters = rearrange_clusters(population, survival_chances, clusters);
		//
		//
		// 				System.out.println(Arrays.toString(clusters[0]));
		// 				System.out.println(Arrays.toString(clusters_clone[0]));
		// 				System.out.println("\n");
		//
		// 				correction_counter = 0;
		// 				for (int i = 0; i < num_of_clusters; i++){
		// 					if (Arrays.equals(clusters_clone[i],clusters[i])){
		// 						correction_counter++;
		// 					}
		// 				}
		// 			} while (iterations_counter < max_cluster_iterations || correction_counter != num_of_clusters);
		// 		}

		while (evals < evaluations_limit_) {

			Individual old_best_person = population.getIndividual(population_size - 1);

			// Select who are going to be parents.
			Population parents = population.tournament_selection(amount_parents, population_size, tournament_size, clusters);

			// Guarantee that the best individual reproduces
			// parents[parents.length-1] = sorted_survival_chances[sorted_survival_chances.length-1];

			if (use_k_means) {
				if (mutate_big){
					System.out.println("place shit");
				}
			}
			// if (use_k_means) {
			// 	if (mutate_big) {
			// 		Random random = new Random();
			// 		int r = random.nextInt(num_of_clusters);
			// 		// System.out.println(r);
			// 		int parents_counter = 0;
			//
			// 		// loop throw all kids
			// 		for (int i = 0; i < sorted_survival_chances.length; i++){
			//
			// 			// if the child belongs to the clusers we are looking at
			// 			if ((int) sorted_survival_chances[i][2] == r){
			//
			// 				// place the child in the parents pool
			// 				parents[parents_counter] = sorted_survival_chances[i];
			// 				parents_counter++;
			//
			// 				// do this untill the paretns pool is full
			// 				if (parents_counter == parents.length){
			// 					break;
			// 				}
			// 			}
			// 		}
			// 	}
			// 	for (int i = 0; i < parents.length; i++){
			// 		cluster_count_array[(int) parents[i][2]]++;
			// 	}
			// }


			Population new_children = new Population(amount_parents);

			if (multiple_parents) {
				new_children = create_children_from_multiple_parents(parents, amount_parents);
			} else {
				new_children = create_n_children(parents, amount_parents);
			}

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
			population.replace_worst(new_children, multiple_parents, amount_parents);
			// population = survivor_selection(sorted_survival_chances, population, new_children);

			// Score new_children
			for (int i = 0; i < amount_parents; i ++) {
				Individual child = new_children.getIndividual(i);
				double fitness = (double) evaluation_.evaluate(child.genome);
				child.setFitness(fitness);
				evals++;
			}

			// Sort population
			population.sort(population_size);

			Individual new_best_person = population.getIndividual(population_size - 1);

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

			// Run for python plot code
			print_average_score(population, population_size);
		}

		// System.out.println("SWAG");
		// int least_used_cluster_index = 0;
		// System.out.println(cluster_count_array[0]);
		// for(int i = 1; i < num_of_clusters; i++) {
		// 	System.out.println(cluster_count_array[i]);
		// 	if (cluster_count_array[i] < cluster_count_array[least_used_cluster_index]) {
		// 		least_used_cluster_index = i;
		// 	}
		// }
		// System.out.println(least_used_cluster_index);
		// // survival_chances = arange_children_to_clusters(population, survival_chances, clusters);
		// // for (int i = 0; i < survival_chances.length; i++) {
		// // 	System.out.println(Arrays.toString(sorted_survival_chances[i]));
		// // clusters = rearrange_clusters(population, survival_chances, clusters);
		//
		// // for (int i = 0; i < clusters.length; i++) {
		// // 	System.out.print(i);
		// // 	System.out.println(Arrays.toString(clusters[i]));
		// for (int c2 = 0; c2 < clusters.length; c2++){
		// 	for (int c = 0; c < population.length; c++){
		// 		if ((int) sorted_survival_chances[c][2] == c2){
		// 			System.out.println(Arrays.toString(sorted_survival_chances[c]));
		// 		}
		// 	}
		// }


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
	public double[][] rearrange_clusters(double[][] children, double[][] survival_chances, double[][] clusters) {

		double[] cluster_tot = new double[10];
		double kids_in_cluster;

		// for 5 clusters
		for (int i = 0; i < clusters.length; i++) {

			// Place zeros in cluster total for calculating mean
			for (int j = 0; j < cluster_tot.length; j++){
				cluster_tot[j] = 0;
			}

			kids_in_cluster = 0;

			// loop throw all kids and place them in the right cluster
			for (int k = 0; k < survival_chances.length; k++){

				// take the row from surv. changes
				double[] row = survival_chances[k];

				// take the cluster number
				int clust_num = (int) row[2];

				// if the child you are looking at is cluster i
				if (clust_num == i) {

					int index = (int) row[1];

					// add to the cluster with the genes from the child you are looking at
					for (int l = 0; l < cluster_tot.length; l++){
						cluster_tot[l] = cluster_tot[l] + children[index][l];
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
					clusters[i][c] = cluster_tot[c];
				}
			}
		}

		// for (int a = 0; a < clusters.length; a++){
		// 	System.out.println(Arrays.toString(clusters[a]));
		// }
		return clusters;
	}


	public Population create_children_from_multiple_parents(Population parents, int amount_parents) {

		Population children = new Population(amount_parents);

		for (int i = 0; i < amount_parents; i += 3) {
			Population temp_children = create_three_children(parents.getIndividual(i), parents.getIndividual(i + 1), parents.getIndividual(i + 2));
			children.setIndividual(i, temp_children.getIndividual(0));
			children.setIndividual(i + 1, temp_children.getIndividual(1));
			children.setIndividual(i + 2, temp_children.getIndividual(2));
		}

		return children;
	}

	public Population inversion_mutation(Population new_children) {

		for (int i = 0; i < amount_parents; i++){

			new_children.getIndividual(i).inversion_mutation();
		}
		return new_children;
	}

	public Population insertion_mutation(Population new_children) {

		for (int i = 0; i < amount_parents; i++) {
			new_children.getIndividual(i).insertion_mutation();
		}

		return new_children;
	}

	public void print_average_score(Population population, int population_size) {
		double total = 0.0;

		for (int i = 0; i < population_size; i++){
			total = total + population.getIndividual(i).fitness;
		}
		double average = total / population_size;

		System.out.print("avg: ");
		System.out.println(average);

		System.out.print("best: ");
		System.out.println(population.getIndividual(population_size - 1).fitness);
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
	public Population small_mutation(Population new_children, int num_of_mutations) {
		for (int i = 0; i < amount_parents; i++){
			new_children.getIndividual(i).small_mutation(num_of_mutations);
		}

		return new_children;
	}

	public Population create_n_children(Population parents, int amount_parents) {
		Population kids = new Population(amount_parents);

		for (int i = 0; i < amount_parents; i += 2) {
			Population temp_children = create_two_children(parents.getIndividual(i), parents.getIndividual(i + 1));
			kids.setIndividual(i, temp_children.getIndividual(0));
			kids.setIndividual(i + 1, temp_children.getIndividual(1));
		}

		return kids;
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

	// Creates two children that mirror each other, and together can form their parents.
	public Population create_two_children(Individual parent1, Individual parent2) {
		Population kids = new Population(2);

	    // make 5 int in an array from 0 - 9
	    int[] indices = printRandomNumbers(10, 9);
	    // System.out.println(Arrays.toString(parent_indices));

	    for (int i = 0; i < 10; i++) {
				if (i < 5) {
						kids.getIndividual(0).genome[indices[i]] = parent1.genome[indices[i]];
						kids.getIndividual(1).genome[indices[i]] = parent2.genome[indices[i]];
	       } else {
						kids.getIndividual(0).genome[indices[i]] = parent2.genome[indices[i]];
 						kids.getIndividual(1).genome[indices[i]] = parent1.genome[indices[i]];
	       }
	    }
			return kids;

	}

	public Population create_three_children(Individual parent1, Individual parent2, Individual parent3) {
		// make children array for 3 kids with 10 alleles
		Population kids = new Population(3);

		for (int i = 0; i < 3; i ++) {
			Individual kid = new Individual();
				int[] indices = printRandomNumbers(10,9);

				for (int j = 0; j < 10; j ++) {
					if (j < 4) {
							kid.genome[indices[j]] = parent1.genome[indices[j]];
					} else if (j < 7) {
						kid.genome[indices[j]] = parent2.genome[indices[j]];
					} else {
						kid.genome[indices[j]] = parent3.genome[indices[j]];
					}
				}
				kids.setIndividual(i, kid);
		}

		return kids;
	}
}
