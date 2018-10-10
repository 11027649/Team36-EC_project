public class Population {
   List<Individual> individuals;

   Population(pop_size) {
     this.individuals = List<Individual>();
   }

   public void initialize_population(population_size) {

   }

   public void setIndividual(int i, Individual individual) {
     individuals.set(i, individual);
   }

   public Individual getIndividual(int i) {
     return individuals.get(i);
   }

   // replace worst?

 }
