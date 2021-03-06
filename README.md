# Team36-EC_project 21-10-2018
#### Bob Borsboom, Tobias Garritsen, Emma Hokken, Natasja Wezel
#### Implemented for the Evolutionary Computing course at the Free University (VU) Amsterdam
Designing and implementing an EA to maximise three continuous optimisation problems (Bent Cigar, Katsuura, and Schaffers F7 function) in 10 dimensions with a limited computational budget.

## Summary
The effects of the parameter values on an Evolutionary Algorithm (EA) are investigated. The Algorithm's performance is tested on three different mathematical functions in 10D: the Bent Cigar Function, Schaffer's F17 Function and the Katsuura Function. An algorithm is written that initalizes a population of 100 individuals randomly, selects two parents with tournament selection, recombinates and replaces the worst individuals in the population with the new generation. It is found that the algorithm can be adapted with different parameter values to perform better on the Bent Cigar and Schaffers F17 functions. These have different characteristics, and the algorithm works best with different parameter values for these different functions. The algorithm gives sufficient results on these two functions, but not on the Katsuura function. An outlook is given on how to optimize results on this function as well.
