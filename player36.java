import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
// import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashSet;
import java.util.Set;

import java.lang.Math;

import java.awt.Font;
import java.awt.Color;

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

		// int evals = 0;

		// amount of random people from population
		int n = 5;

		// maak de populatie aan
		double childrens[][] = create_population();
		int glb_best = 0;

		// bepaal voor de hele populatie per kind de fitness.
		double survival_chances[][] = score_checker(childrens);

		// sorteer de score array op fitness
		double sort_list[][] = sort_survival_chances(survival_chances);
		for (int i = 0; i < sort_list.length; i++) {
			System.out.println(Arrays.toString(sort_list[i]));
		}

		// ask for 5 random ints between  0 - 99
		int random_number_list[] = printRandomNumbers(n, 99);

		double random_5_people[][] = new double[n][];

		// search these 5 people and save
		for (int i = 0; i < random_number_list.length; i++) {

			// put the index number in a variable
			int index_number = random_number_list[i];
			random_5_people[i] = sort_list[index_number];
			System.out.println(Arrays.toString(random_5_people[i]));
		}



		// calculate fitness
		while (evals < evaluations_limit_/10) {
			// System.out.println(evals);
			// for (int generations = 0; generations < 100; generations++) {

			// Select parents

			// Apply crossover / mutation operators

			// Check fitness of unknown fuction: determines your grade
			// needs real-value values but conversions before are okay
			// Double fitness = (double) evaluation_.evaluate(childrens[2]);

			double avg_fitness = 0;
			int best = 0;
			double best_value = -100;
			for (int i = 0; i < childrens.length; i++) {

				// calculate and save the fitness of this individual
				Double fitness = (double) evaluation_.evaluate(childrens[i]);
				avg_fitness += fitness;
				survival_chances[i][0] = fitness;
				survival_chances[i][1] = i;

				// save the best individual
				if (fitness > best_value) {
					best_value = fitness;
					best = i;
				}
				evals++;
			}

			// Sort algorithm from min to max fitness
			double[][] sorted_survival_chances = sort_survival_chances(survival_chances);

			// print out the individual and the score
			// for (int a = 0; a < sorted_survival_chances.length; a++){
			// 	System.out.println(Arrays.toString(sorted_survival_chances[a]));
			// 	System.out.println(Arrays.toString(childrens[(int) sorted_survival_chances[a][1]]));
			// }

			// Create average gene for best fitness and for each of population
			// for (int j = 0; j < childrens.length; j++) {
			// 	for (int c = 0; c < childrens[j].length; c++) {
			// 		childrens[j][c] = (childrens[j][c]+childrens[best][c])/2;
			// 	}
			// }

			// Have for child_n in range of popsize, mate 1 with 2 and 2 with 3 ... to n.
			// for (int j = survival_chances.length/2; j < sorted_survival_chances.length-1; j++) {
			// 	int index_val1 = (int) sorted_survival_chances[j][1];
			// 	int index_val2 = (int) sorted_survival_chances[j+1][1];
			// 	for (int gen_index = 0; gen_index < childrens[index_val1].length; gen_index++) {
			// 		//childrens[index_val1][c] = (childrens[index_val1][c]+childrens[index_val2][c])/2;
			// 		//recombine genes for best 50 with their +1 incremented counterparts
			// 		for (int i : printRandomNumbers(5,9)) {
   //      				childrens[index_val1][i] = childrens[index_val2][i];
   //  				}

   //  				// replace worst 50 children with best 50 with a slight mutation
			// 		int index_val_mutate = (int) sorted_survival_chances[j-sorted_survival_chances.length/2][1];
			// 		childrens[index_val_mutate] = childrens[index_val1];
			// 		for (int i : printRandomNumbers(2,9)) {
			// 			double random_double = get_random_double(-5, 5);
   //      				childrens[index_val_mutate][i] = random_double;
   //  				}
			// 	}
			// }

			glb_best = best;
			// System.out.println(glb_best);

		}

		System.out.println("OHOHHOHO");

		// plot the scores of the last population
		// makeGraph();

		// print out global best
	// 	System.out.println(Arrays.toString(childrens[glb_best]));
	// 	for (double child : childrens[glb_best]) {
	// 		System.out.print(0.01*(int) Math.round(child*100));
	// 		System.out.print("\t");
	// 	}
	}

	public double[][] score_checker( double[][] childrens) {

		// in this array we place the score and index
		
		double fitness_index_array[][] = new double[childrens.length][];

		for (int i = 0; i < childrens.length; i++) {

			double fit_index_array[] = new double [2];

			// calculate and save the fitness of this individual
			double fitness = (double) evaluation_.evaluate(childrens[i]);
			
			// on the ith array at the left side, place the fitness
			fit_index_array[0] = fitness;

			// on the ith array at the right side, place the index number
			fit_index_array[1] = i;

			// place the array in the big array
			fitness_index_array[i] = fit_index_array;

			evals++;
		}

		return fitness_index_array;
	}

	// TODO write better sorting algorithm
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

	// TODO write function that loops over array for prettyprinting
	// Prettify genes for better readibilty
	// public float prettify_genes(double childrens) {
	// 	for (int child : childrens) {
	// 		System.out.println(child);
	// 	}


	// 	return childrens
	// }

	public void makeGraph() {
//		 ArrayList<Bar> values = new ArrayList<Bar>();
//
//		for (int i = 0; i < 365; i++) {
//		      double d = Math.random();
//		      values.add(new Bar((int)(100 * d), Color.GRAY, ""));
//		 }
//
//		 Axis yAxis = new Axis(100, 0, 50, 10, 1, "Percent Sunlight");
//		 BarChart barChart = new BarChart(values, yAxis);
//
//		 barChart.width = 1000;
//		 barChart.xAxis = "Day of Year";
//		 barChart.titleFont = new Font("Ariel", Font.PLAIN, 24);
//		 barChart.title = "Annual Sunlight Variability";
//
//		 barChart.barWidth = 1;
	}

	// This is a function that generates random numbers between a range, without repetition
	// http://www.codecodex.com/wiki/Generate_Random_Numbers_Without_Repetition
	public static final Random gen = new Random();
    public static int[] printRandomNumbers(int n, int maxRange) {
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
	public double[][] create_population() {
		// define population size
		int pop_size = 100;
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
			// System.out.println(Arrays.toString(children[i]));
		}
		// double[] curr_top = new double[] {-1.1891876987039534, 3.802704764222131, -0.7045031488811008, -3.214820204008321, 0.9342012108821499, -1.6280075915460186, 1.1164003795515511, -0.7854278491364934, 1.7478175537980336, -0.45014115827163426};
		// children[1] = curr_top;
		return children;
	}

	// CODE EMMA@@@@@@@@
	// Creates two children that mirror each other, and together can form their parents. 
	public void create_two_children(double[] mom, double[] dad) {
	   double[] boy = new double[10];
	   double[] girl = new double[10];
	   int[] parent_indices = printRandomNumbers(5, 9);
	   System.out.println(Arrays.toString(parent_indices));

	   for (int i = 0; i < mom.length; i++) {
	      if (in_parent_indices(parent_indices, i)) {
	         boy[i] = mom[i];
	         girl[i] = dad[i];
	      } else {
	         boy[i] = dad[i];
	         girl[i] = mom[i];
	      }
	   }
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
