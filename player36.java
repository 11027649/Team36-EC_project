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

		// calculate fitness
		while (evals < evaluations_limit_) {

			// Select parents

			// Apply crossover / mutation operators

			// Check fitness of unknown fuction: determines your grade
			// needs real-value values but conversions before are okay
			Double fitness = (double) evaluation_.evaluate(childrens[2]);
			evals++;

			// Select survivors

		}
	}


	public double[][] create_population() {


		// define population size
		int pop_size = 100;
		int dim = 10;

		double children[][] = new double[pop_size][];

		// initialize population randomly
		for (int i = 0; i < 100; i++) {
			double child[] = new double[dim];

			for (int j = 0; j < 10; j++) {
				int min = -5;
				int max = 5;
				double random_double = (Math.random() * (max - min)) + min;

				// TODO: remember population
				child[j] = random_double;

			}
			children[i] = child;

//			System.out.println(Arrays.toString(child));
		}
		return children;


	}
}
