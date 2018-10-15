public class Cluster {

   List<Individual> clusters = new List<Individual>();
   int amount_of_clusters = 0;

   Cluster(int amount_of_clusters) {
      this.nitialize_clusters(amount_of_clusters);
      this.amount_of_clusters = amount_of_clusters;
   }

   public void initialize_clusters(int amount_of_clusters) {
     // Initialize random individuals that function as the initial cluster means
     int dimension = 10;

     // initialize population randomly
     for (int i = 0; i < pop_size; i++) {
       Individual child = new Individual();

       for (int j = 0; j < dimension; j++) {
         double random_double = this.get_random_double(-5, 5);
         child.genome[j] = random_double;
       }

       // add child to population
       clusers.add(child);
     }
     cluster_count_array = new int[num_of_clusters];
     for (int i = 0; i < num_of_clusters; i++){
       cluster_count_array[i] = 0;
 		}
   }

   public void name() {
     // Arange population to clusters
 					int max_cluster_iterations = 10;
 					int iterations_counter = 0;
 					int correction_counter;
 					do {
 						iterations_counter++;

 						// Create copy of clusters
            // NOTE: Not entirally sure if this works.
 						List<Individual> clusters_clone = new List<Individual>(num_of_clusters);
 						for (Cluster c : clusters ){
 							clusters_clone.add(c.clone());
 						}
 						// Arange all individuals of population to a certain cluster
 						survival_chances = arange_children_to_clusters(population, clusters);
 						// Rearange clusters by recalculating the mean
 						clusters = rearrange_clusters(population, survival_chances, clusters);


 						System.out.println(Arrays.toString(clusters.get(0).toArray()));
 						System.out.println(Arrays.toString(clusters_clone.get(0).toArray()));
 						System.out.println("\n");

 						correction_counter = 0;
 						for (int i = 0; i < num_of_clusters; i++){
 							if (clusters_clone.get(i) == clusters.get(i)) {
 								correction_counter++;
 							}
 						}
 					} while (iterations_counter < max_cluster_iterations || correction_counter != num_of_clusters);
   }

   // arrange clusters
   // Part 1 of the k-means clustering
 	// The children are assigned to a cluster
 	public Population arange_children_to_clusters(Population children, double[][] survival_chances, Population clusters) {

 		// loop through all kids
 		for (int i = 0; i < amount_of_clusters; i++) {

 			// Find the corresponding genes.
 			Individual child = children.getIndividual(i);

 			double childs_min = 1000;
 			int cluster_num = -1;

 			// Determine in which cluster the child fits the best.
 			for (int j = 0; j < amount_of_clusters; j++) {

 				Individual cluster = clusters.getIndividual(j);

 				double dist = 0.0;

 				// compare 1 gene of the kid with 10 genes from the cluster... @ Tobais, I think this is wrong
 				for (int k = 0; k < child.genome.length; k++){

 					// take the square distance
 					dist = dist + (child.genome.get(k) - cluster.genome.get(k)) * (child.genome.get(k) - cluster.genome.get(k));
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
 	public Population rearrange_clusters(Population children, Population clusters) {

 		Individual cluster_tot = new Individual();
 		double kids_in_cluster;

 		// for 5 clusters
 		for (int i = 0; i < amount_of_clusters; i++) {

 			// Place zeros in cluster total for calculating mean
 			for (int j = 0; j < cluster_tot.length; j++){
 				cluster_tot[j] = 0;
 			}

 			kids_in_cluster = 0;

 			// loop throw all kids and place them in the right cluster
 			for (int k = 0; k < amount_of_clusters; k++){

 				// take the row from surv. changes
 				double[] row = survival_chances[k];

 				// take the cluster number
 				int clust_num = children.get(k).cluster;

 				// if the child you are looking at is cluster i
 				if (clust_num == i) {

 					int index = (int) row[1];

 					// add to the cluster with the genes from the child you are looking at
 					for (int l = 0; l < cluster_tot.length; l++){
 						cluster_tot[l] = cluster_tot[l] + children[index][l];
 					}
 					// cluster_tot = cluster_tot + children[index];
 					kids_in_cluster = kids_in_cluster + 1;
 				}
 			}

 			if (kids_in_cluster > 0.0 ) {
 				// System.out.println		kids_in_cluster);

 				// calculate the new cluster mean
 				for (int c = 0; c < cluster_tot.length; c++) {
 					cluster_tot[c] = cluster_tot[c] /		kids_in_cluster;
 					clusters[i][c] = cluster_tot[c];
 				}
 			}
 		}

 		// for (int a = 0; a < clusters.length; a++){
 		// 	System.out.println(Arrays.toString(clusters[a]));
 		// }
 		return clusters;
 	}

 }
