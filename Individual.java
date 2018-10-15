import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashSet;
import java.util.Set;

import java.lang.Math;

public class Individual {
   double[] genome = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
   double fitness = 0;
   double cluster = 0;

   Individual() {
   }

   public void setCluster(int cluster) {
     this.cluster = cluster;
   }

   public double getCluster() {
     return this.cluster;
   }

   public void setFitness(double fitness) {
     this.fitness = fitness;
   }

   public double getFitness() {
     return this.fitness;
   }

   public void insertion_mutation() {
       // Get indices to switch, and sort them.
       int[] indices = printRandomNumbers(2, 9);
       if (indices[0] > indices[1]) {
         int temp = indices[0];
         indices[0] = indices[1];
         indices[1] = temp;
       }

    double[] backup = new double[10];
    // Create backup array
    for (int i = 0; i < this.genome.length; i++) {
       backup[i] = this.genome[i];
     }

     // Switch the
     this.genome[indices[0] + 1] = this.genome[indices[1]];

     for (int i = indices[0] + 2; i < this.genome.length; i++) {
       if (i > indices[1]) {
         this.genome[i] = backup[i];
       } else {
         this.genome[i] = backup[i - 1];
       }
     }

   }

   public void inversion_mutation() {


			int random_numbers[] = printRandomNumbers(2, 9);
			int begin = random_numbers[0];
			int end = random_numbers[1];

      // Sort indices.
			if (begin > end) {
				int temp = begin;
				begin = end;
				end = temp;
			}

			for (int i = begin; i < Math.ceil((begin + end + 1) / 2); i++) {
					double temp_gen = this.genome[i];
					this.genome[i] = this.genome[end - i + begin];
					this.genome[end - i + begin] = temp_gen;
				}
   }

   public void small_mutation(int num_of_mutations) {
 			// Create a random digit between 0 and 9
 			int[] random_numbers = printRandomNumbers(num_of_mutations, 9);

 			for (int i = 0; i < random_numbers.length; i++) {
 				double r = get_random_double(-1,1);
 				this.genome[i] = this.genome[i] + r;
 				if (this.genome[i] > 5 || this.genome[i] < -5) {
 					this.genome[i] = this.genome[i] - r - r;
 				}
 			}
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

 }
