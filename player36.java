import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class player36 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

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
	    }else{
	        // Do sth else
	    }
    }

	public void run() {
		// Run your algorithm here

		int evals = 0;
		double childrens[][] = create_population();
		int glb_best = 0;

		int survival_chance = 0;
		double survival_chances[][] = new double[childrens.length][];
		for (int k = 0; k < childrens.length; k++) {
			double surv_chance[] = new double[2];
			surv_chance[0] = 0;
			surv_chance[1] = survival_chance;
			survival_chances[k] = surv_chance;
		}

		
		// calculate fitness
		while (evals < evaluations_limit_) {
			System.out.println(evals);
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
				// System.out.println(Arrays.toString(childrens[i]));
				// System.out.println(i);
				Double fitness = (double) evaluation_.evaluate(childrens[i]);
				avg_fitness = avg_fitness + fitness;
				survival_chances[i][0] = fitness;
				survival_chances[i][1] = i;
				//System.out.println(Arrays.toString(survival_chances[i]));
				if (fitness > best_value) {
					best_value = fitness;
					best = i;
				}
				evals++;
			}

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

			for (int a = 0; a < survival_chances.length; a++){
				System.out.println(Arrays.toString(survival_chances[a]));
			}

			// System.out.println(avg_fitness/100);

			// System.out.println(best_value);
			//System.out.println(best);
			// System.out.println(Arrays.toString(childrens[best]));
			for (int j = 0; j < childrens.length; j++) {
				for (int c = 0; c < childrens[j].length; c++) {
					childrens[j][c] = (childrens[j][c]+childrens[best][c])/2;
				}
			}

			glb_best = best;
			// System.out.println(glb_best);

		}

		System.out.println(Arrays.toString(childrens[glb_best]));
	}


	public double[][] create_population() {
		// define population size
		int pop_size = 100;
		int dim = 10;

		

		double children[][] = new double[pop_size][];

		// initialize population randomly
		for (int i = 0; i < pop_size; i++) {
			double child[] = new double[dim];

			for (int j = 0; j < dim; j++) {
				int min = -5;
				int max = 5;
				double random_double = (Math.random() * (max - min)) + min;
				child[j] = random_double;

			}
			children[i] = child;
			// System.out.println(Arrays.toString(children[i]));
		}
		// double[] curr_top = new double[] {-1.1891876987039534, 3.802704764222131, -0.7045031488811008, -3.214820204008321, 0.9342012108821499, -1.6280075915460186, 1.1164003795515511, -0.7854278491364934, 1.7478175537980336, -0.45014115827163426};
		// children[1] = curr_top;
		return children;
	}
}
