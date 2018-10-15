import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;
import java.util.Properties;

public class Population {
  // NIEUW
  ArrayList<Individual> population = new ArrayList<Individual>();

  Population(int pop_size) {
   // population = new ArrayList<Individual>(pop_size);
   this.initialize_population(pop_size);
  }

  // Initialize the population with random individuals.
  public void initialize_population(int pop_size) {
   int dimension = 10;

   // initialize population randomly
   for (int i = 0; i < pop_size; i++) {
     Individual child = new Individual();

     for (int j = 0; j < dimension; j++) {
       double random_double = this.get_random_double(-5, 5);
       child.genome[j] = random_double;
     }

     // add child to population
     population.add(child);
   }
  }

  public void sort(int pop_size) {
   Individual temp_child = new Individual();

    for (int n = 0; n < pop_size; n++) {
      for (int m = 0; m < pop_size; m++){
        if (this.getIndividual(n).fitness < this.getIndividual(m).fitness) {
          temp_child = population.get(n);
          this.setIndividual(n, this.getIndividual(m));
          this.setIndividual(m, temp_child);
        }
      }
    }
  }

  public Population tournament_selection(int parents_amount, int population_size, int tournament_size) {
    Population parents = new Population(parents_amount);

    for (int i = 0; i < parents_amount; i++) {
      // TODO: it would be prettier if it didn't get initialized now with random individuals.
        Population parents_pool = new Population(tournament_size);

        int random_number_list[] = printRandomNumbers(tournament_size, population_size - 1);

        for (int j = 0; j < tournament_size; j++) {
            // Select "tournament_size" random individuals.
            parents_pool.setIndividual(j, this.getIndividual(random_number_list[j]));
        }

        parents_pool.sort(tournament_size);
        // Select best parent from the pool.
        parents.setIndividual(i, parents_pool.getIndividual(tournament_size - 1));
    }

    return parents;
  }

  public void setIndividual(int i, Individual individual) {
   population.set(i, individual);
  }

  public Individual getIndividual(int i) {
   return population.get(i);
  }

  public double get_random_double(int min, int max) {
   return (Math.random() * (max - min)) + min;
  }

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

   public void replace_worst(Population new_children, boolean multiple_parents, int amount_children) {
     if (multiple_parents) {
			for (int i = 0; i < amount_children; i += 3) {
        // Replace wordt individuals with new kids.
        this.setIndividual(i, new_children.getIndividual(i));
        this.setIndividual(i + 1, new_children.getIndividual(i + 1));
        this.setIndividual(i + 2, new_children.getIndividual(i + 2));
			}
		} else {
			for (int i = 0; i < amount_children; i += 2) {
        // Replace wordt individuals with new kids.
        this.setIndividual(i, new_children.getIndividual(i));
        this.setIndividual(i + 1, new_children.getIndividual(i + 1));

		}
   }

   // public List<Individual> insertion_mutation(List<Individual> new_children) {
   //   // Iterate over every new child.
   //  for (int i = 0; i < new_children.size(); i++) {
   //    // Get indices to switch, and sort them.
   //     int[] indices = printRandomNumbers(2, 9);
   //     if (indices[0] > indices[1]) {
   //       int temp = indices[0];
   //       indices[0] = indices[1];
   //       indices[1] = temp;
   //     }
   //
   //  List<Individual> backup;
   //  // Create backup array
   //  for (int j = 0; j < new_children.get(i).size(); j++) {
   //     backup.add(new_children.get(i).get(j.clone()));
   //   }
   //
   //   // Switch the
   //   new_children.get(i).get([indices[0] + 1]) = new_children.set(i).set(indices[1]);
   //
   //   for (int j = indices[0] + 2; j < new_children.get(i).size(); j++) {
   //     if (i > indices[1]) {
   //       new_children.get(i).get(j) = backup.get(j);
   //     } else {
   //       new_children.get(i).get(j) = backup.get(j - 1);
   //     }
   //   }
   // }
   //
   // return new_children;
   // }
   //
   // public List<Individual> inversion_mutation(List<Individual> new_children) {
   //   Individual individual_kid = new Individual();
   //
		// for (int i = 0; i < new_children.size(); i++){
		// 	individual_kid = new_children.get(i);
   //
		// 	int random_numbers[] = printRandomNumbers(2, 9);
		// 	int begin = random_numbers[0];
		// 	int end = random_numbers[1];
   //
   //    // Sort indices.
		// 	if (begin > end) {
		// 		int temp = begin;
		// 		begin = end;
		// 		end = temp;
		// 	}
   //
		// 	for (int j = begin; j < Math.ceil((begin + end + 1) / 2); j++) {
		// 			double temp_gen = individual_kid.get(j);
		// 			individual_kid.set(j, individual_kid.get(end - j + begin));
		// 			individual_kid.set(end - j + begin, temp_gen);
		// 		}
		// 	new_kids.setIndividual(i, individual_kid);
		// }
		// return new_kids;
   // }
   //
   // public List<Individual> small_mutation(List<Individual> new_children, int num_of_mutations) {
   //   for (int i = 0; i < new_kids.size(); i++){
 		// 	Individual individual_kid[] = new_kids.get(i);
   //
 		// 	// create a random digit between 0 and 9
 		// 	int random_numbers [] = printRandomNumbers(num_of_mutations, 9);
   //
 		// 	for (int j = 0; j < random_numbers.length; j++) {
 		// 		double r = get_random_double(-1,1);
 		// 		individual_kid.set(j, individual_kid.get(j) + r);
 		// 		if (individual_kid.get(j) > 5 || individual_kid.get(j) < -5) {
 		// 			individual_kid.set(j, individual_kid.get(j) - r - r);
 		// 		}
 		// 	}
 		// 	new_kids.set(i, individual_kid);
 		// }
   //
 		// return new_kids;
   // }

 }
}
