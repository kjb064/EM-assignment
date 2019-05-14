
public class CoinEM {

	final static int h = 0;		// h = "Heads"
	final static int t = 1;		// t = "Tails"

	// INITIALIZATION (randomly assign the initial likelihoods)
	static double thetaA = 0.60;	
	static double thetaB = 0.50;

	public static void main(String[] args){
		
		final int flips = 10;
		final int rounds = 5;
		final int iterations = 10;	// running 10 iterations
		
		int[][] results = {{h, t, t, t, h, h, t, h, t, h},
						   {h, h, h, h, t, h, h, h, h, h},
						   {h, t, h, h, h, h, h, t, h, h},
						   {h, t, h, t, t, t, h, h, t, t},
						   {t, h, h, h, t, h, h, h, t, h}};

		System.out.println("Initial thetaA: " + thetaA + "\tInitial thetaB: " + thetaB);

		for(int i = 0; i < iterations; i++){
			EMIteration(results, flips, rounds);
			System.out.print("Iteration " + (i+1) + ": ");
			System.out.printf("%.2f\t", thetaA);
			System.out.printf("%.2f\n", thetaB);
		}

	} // end main

	public static void EMIteration(int[][] results, int flips, int rounds){
		int headCount, tailCount;

		double headEstimateA, tailEstimateA, headEstimateB, tailEstimateB;
		headEstimateA = tailEstimateA = headEstimateB = tailEstimateB = 0.0;

		double totalHeadEstimateA, totalHeadEstimateB, totalTailEstimateA, totalTailEstimateB;
		totalHeadEstimateA = totalHeadEstimateB = totalTailEstimateA = totalTailEstimateB = 0.0;

		double likelihoodCoinA, likelihoodCoinB, probabilityCoinA, probabilityCoinB;

		for(int i = 0; i < rounds; i++){

			// EXPECTATION 

			headCount = countSuccess(results[i], flips);
			tailCount = flips - headCount;
			
			likelihoodCoinA = binomialDistribution(flips, headCount, thetaA);			// compute likelihoods
			likelihoodCoinB = binomialDistribution(flips, headCount, thetaB);

			probabilityCoinA = likelihoodCoinA / (likelihoodCoinA + likelihoodCoinB);	// normalization of likelihoods
			probabilityCoinB = 1.0 - probabilityCoinA;

			headEstimateA = probabilityCoinA * (double) headCount;
			tailEstimateA = probabilityCoinA * (double) tailCount;						// estimate heads and tails
			headEstimateB = probabilityCoinB * (double) headCount;
			tailEstimateB = probabilityCoinB * (double) tailCount;

			totalHeadEstimateA += headEstimateA;
			totalTailEstimateA += tailEstimateA;
			totalHeadEstimateB += headEstimateB;
			totalTailEstimateB += tailEstimateB;

		}

		// MAXIMIZATION (update the likelihoods)
		thetaA = totalHeadEstimateA / (totalHeadEstimateA + totalTailEstimateA);
		thetaB = totalHeadEstimateB / (totalHeadEstimateB + totalTailEstimateB);


	} // end EMIteration

	/*
		Calculates the number of times 'h' appears in a round of tosses
	*/
	public static int countSuccess(int[] round, int flips){
		int count = 0;
		for(int i = 0; i < flips; i++){
			if(round[i] == h) 
				count++;
		}
		return count;
	} // end countSuccess

	public static double binomialDistribution(int n, int x, double p){		
		double q, binCoeff, answer;
		q = 1 - p;
		binCoeff = (double) choose(n, x);
		answer = binCoeff * Math.pow(p, x) * Math.pow(q, n-x);
		return answer;

	} // end binomialDistribution
	
	/* 
		Calculates factorial 
	*/
	public static int fact(int num){
		int nfact = 1;

		for(int i = 1; num > 0; num--){
			nfact *= num;
		} 

		return nfact;
	} // end fact

	/*
		(n choose x) =   n!
					  --------
					  (n-x)!x!
	*/
	public static int choose(int n, int x){		
		// Base Cases 
        if (x == 0 || x == n) 
            return 1; 
          
        // Recursion 
        return choose(n - 1, x - 1) +  choose(n - 1, x); 
	} // end choose

} // end CoinEM