import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Cluster {

   List<Individual> clusters = new ArrayList<Individual>();
   // ArrayList<Individual> clusters = new ArrayList<Individual>();
   int amount_of_clusters = 0;
   int[] cluster_count_array;

   Cluster(int amount_of_clusters) {
      this.amount_of_clusters = amount_of_clusters;
      this.initialize_clusters(amount_of_clusters);
   }

   public void initialize_clusters(int amount_of_clusters) {
     // Initialize random individuals that function as the initial cluster means
     int dimension = 10;

     // initialize population randomly
     for (int i = 0; i < amount_of_clusters; i++) {
       Individual child = new Individual();

       for (int j = 0; j < dimension; j++) {
         double random_double = this.get_random_double(-5, 5);
         child.genome[j] = random_double;
       }

       // add child to population
       clusters.add(child);
     }
     cluster_count_array = new int[amount_of_clusters];
     for (int i = 0; i < amount_of_clusters; i++){
       cluster_count_array[i] = 0;
 		 }
   }

   public void cluster_convergence(Population population) {
     // Arrange population to clusters
 					int max_cluster_iterations = 10;
 					int iterations_counter = 0;
 					int correction_counter;
 					do {
 						iterations_counter++;

 						// Create copy of clusters
            // NOTE: Not entirally sure if this works.
            List<Individual> clusters_clone = new ArrayList<Individual>(amount_of_clusters);
            // ArrayList<Individual> clusters_clone = new ArrayList<Individual>(amount_of_clusters);
 						
       //      for (Individual c : clusters ){
       //        // Individual cloned = c.clone();
 						// 	clusters_clone.add(c.clone());
 						// }
            for (int i = 0; i < amount_of_clusters; i++){
            
             // clusters_clone.set(i, clusters.get(i));
             Individual temporary_cluster = clusters.get(i);
             Individual cloned_cluster = new Individual();
             for(int j = 0; j < 10; j++) {
              cloned_cluster.genome[j] = temporary_cluster.genome[j];
             }
             clusters_clone.set(i,cloned_cluster);
             // Arrays.copyOf(clusters[i],clusters[i].length);
            
            }

 						// Arrange all individuals of population to a certain cluster
 						population = arrange_children_to_clusters(population);
 						// Rearrange clusters by recalculating the mean
 						rearrange_clusters(population);


 						correction_counter = 0;
 						for (int i = 0; i < amount_of_clusters; i++){
 							if (clusters_clone.get(i) == clusters.get(i)) {
 								correction_counter++;
 							}
 						}
 					} while (iterations_counter < max_cluster_iterations || correction_counter != amount_of_clusters);
   }

  // arrange clusters
  // Part 1 of the k-means clustering
 	// The children are assigned to a cluster
 	public Population arrange_children_to_clusters(Population children) {

 		// loop through all kids
 		for (int i = 0; i < amount_of_clusters; i++) {

 			// Find the corresponding genes.
 			Individual child = children.getIndividual(i);

 			double childs_min = 1000;
 			int cluster_num = -1;

 			// Determine in which cluster the child fits the best.
 			for (int j = 0; j < amount_of_clusters; j++) {

 				Individual cluster = clusters.get(j);

 				double dist = 0.0;

 				// compare 1 gene of the kid with 10 genes from the cluster
 				for (int k = 0; k < child.genome.length; k++){

 					// take the square distance
 					dist = dist + (child.genome[k] - cluster.genome[k]) * (child.genome[k] - cluster.genome[k]);
 				}

 				dist = Math.sqrt(dist);

 				if (dist < childs_min) {
 					childs_min = dist;
 					cluster_num = j;
 				}
 			}
 			child.cluster = cluster_num;
 		}
    return children;
 	}

 	// Part 2 of the k-means clustering
 	// The clusters means are updated
 	public void rearrange_clusters(Population children) {

 		Individual cluster_tot = new Individual();
 		double kids_in_cluster;

 		// for 5 clusters
 		for (int i = 0; i < amount_of_clusters; i++) {

 			// Place zeros in cluster total for calculating mean
 			for (int j = 0; j < cluster_tot.genome.length; j++){
 				cluster_tot.genome[j] = 0.0;
 			}

 			kids_in_cluster = 0;

 			// loop throw all kids and place them in the right cluster
 			for (int k = 0; k < children.pop_size; k++){

 				// take the cluster number
 				int clust_num = children.getIndividual(k).getCluster();

 				// if the child you are looking at is cluster i
 				if (clust_num == i) {

 					// int index = (int) row[1];

 					// add to the cluster with the genes from the child you are looking at
 					for (int l = 0; l < cluster_tot.genome.length; l++){
 						cluster_tot.genome[l] = cluster_tot.genome[l] + children.getIndividual(k).genome[l];
 					}
 					// cluster_tot = cluster_tot + children[index];
 					kids_in_cluster = kids_in_cluster + 1;
 				}
 			}

 			if (kids_in_cluster > 0.0 ) {
 				// System.out.println		kids_in_cluster);

 				// calculate the new cluster mean
 				for (int c = 0; c < cluster_tot.genome.length; c++) {
 					clusters.get(i).genome[c] = cluster_tot.genome[c] /	kids_in_cluster;
 				}
 			}
 		}

 		// for (int a = 0; a < clusters.length; a++){
 		// 	System.out.println(Arrays.toString(clusters[a]));
 		// }
 		// return clusters;
 	}

  public double get_random_double(int min, int max) {
   return (Math.random() * (max - min)) + min;
  }

 }
