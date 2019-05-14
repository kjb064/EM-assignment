public class DayEM {

	//INITIALIZATION (randomly assign initial means)
	static double muSunny = 80.0;		// initial sunny day mean
	static double muCloudy = 55.0;		// initial cloudy day mean

	final static int choiceSun = 0;
	final static int choiceCloud = 1;

	public static void main(String[] args){

		int[] days = {70, 62, 89, 54, 97, 75, 82, 56, 32, 78}; 

		int sigma = 10;	// standard deviation = 10

		double oldSun, oldCloud;

		System.out.println("Initial muSunny: " + muSunny + " Initial muCloudy: " + muCloudy);
		int count = 0;
		do {
			oldSun = muSunny;
			oldCloud = muCloudy;
			EMIteration(days, sigma);
			count++;
			System.out.print("Iteration " + count +": "); 
			System.out.printf("%.2f\t", muSunny);
			System.out.printf("%.2f\n", muCloudy);
		} while( Math.abs(oldSun - muSunny) > 0.1 && Math.abs(oldCloud - muCloudy) > 0.1 );	// iterate until differences between means
																							// is less than 0.1
	}// end main

	public static void EMIteration(int[] days, int sigma){
		double[] expectedValuesSun = new double[days.length];
		double[] expectedValuesCloud = new double[days.length];
		double[] expectedValues;

		double totalSunE = 0.0;
		double totalCloudE = 0.0;

		for(int i = 0; i < days.length; i++){
			expectedValues = expectationStep(days[i], sigma);

			expectedValuesSun[i] = expectedValues[0];
			expectedValuesCloud[i] = expectedValues[1];

			totalSunE += expectedValuesSun[i];
			totalCloudE += expectedValuesCloud[i];
		}

		maximizationStep(days, expectedValuesSun, choiceSun);
		maximizationStep(days, expectedValuesCloud, choiceCloud);

	} // end EMIteration

	public static double[] expectationStep(int x, int sigma){		
																	
		double likelihoodSunny, likelihoodCloudy;
		double expectedValueSun, expectedValueCloud;

		likelihoodSunny = computeLikelihood(x, sigma, choiceSun);				// compute likelihoods
		likelihoodCloudy = computeLikelihood(x, sigma, choiceCloud);

		expectedValueSun = likelihoodSunny / (likelihoodSunny + likelihoodCloudy);	// compute expected values
		expectedValueCloud = 1.0 - expectedValueSun;

		double[] expectedValues = {expectedValueSun, expectedValueCloud};
		return expectedValues;

	} // end expectationStep

	public static double computeLikelihood(int x, int sigma, int choice){
		double likelihood, exponent;

		if(choice == choiceSun){
			exponent = (-1 / (2 * (double) Math.pow(sigma, 2) ) ) * Math.pow( (x - muSunny) , 2);
		}
		else{
			exponent = (-1 / (2 * (double) Math.pow(sigma, 2) ) ) * Math.pow( (x - muCloudy) , 2);
		}

		likelihood = Math.pow(Math.E, exponent);
		return likelihood;

	} // end computeLikelihood

	public static void maximizationStep(int[] days, double[] expectedValues, int choice){
		
		double totalEValues = 0.0;
		for(int i = 0; i < expectedValues.length; i++){
			totalEValues += expectedValues[i];
		}

		double numerator =  0.0;
		if(choice == choiceSun){
			
			for(int i = 0; i < expectedValues.length; i++){
				numerator += (double) days[i] * expectedValues[i];
			}

			muSunny = numerator / totalEValues;
		}
		else{
			
			for(int i = 0; i < expectedValues.length; i++){
				numerator += (double) days[i] * expectedValues[i];
			}

			muCloudy = numerator / totalEValues;
		}

	} // end maximizationStep

} // end DayEM