public class Individual {
   double[] genome = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
   double fitness = 0;

   public int setFitness(int evals, double fitness) {
     this.fitness = fitness;
     evals++;
     return evals;
   }
 }
