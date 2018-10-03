public class Individual {
   double[] genome = new double[10];
   double fitness;

   public int setFitness(int evals, double fitness) {
     this.fitness = fitness;
     evals++;
     return evals;
   }
 }
