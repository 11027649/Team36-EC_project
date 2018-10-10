public class Individual {
   double[] genome = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
   double fitness = 0;
   double cluster = 0;

   public void setCluster(int cluster) {
     this.cluster = cluster;
   }

   public int getCluster() {
     return this.cluster;
   }

   public int setFitness(int evals, double fitness) {
     this.fitness = fitness;
     evals++;
     return evals;
   }

   public int getFitness() {
     return this.fitness;
   }

   // Mutation functions here

 }
