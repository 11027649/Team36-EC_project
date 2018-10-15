public class Options {
  // All settings.
  int population_size;
  int tournament_size;
  int amount_parents;
  int num_of_mutations;
  int num_of_unchanged_best;
  int max_of_unchanged_best;

  // Variations.
  boolean mutate_big;
  boolean multiple_parents;

  // Settings for K-means.
  boolean k_means;
  int[] cluster_count_array;
  int num_parents_from_cluster;
  int num_of_clusters;

  Options(int population_size, int tournament_size,
            int amount_parents, int num_of_mutations, int num_of_unchanged_best,
            int max_of_unchanged_best, boolean mutate_big, boolean multiple_parents,
            boolean k_means, int[] cluster_count_array, int num_parents_from_cluster,
            int num_of_clusters;) {

    population_size = population_size;
    tournament_size = tournament_size;
    amount_parents = amount_parents;
    num_of_mutations = num_of_mutations;
    num_of_unchanged_best = num_of_unchanged_best;
    max_of_unchanged_best = max_of_unchanged_best;

    mutate_big = mutate_big;
    multiple_parents = multiple_parents;

    k_means = k_means;
    cluster_count_array = cluster_count_array;
    num_parents_from_cluster = num_parents_from_cluster;
    num_of_clusters = num_of_clusters;
  }

}
