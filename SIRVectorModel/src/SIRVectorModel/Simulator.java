package SIRVectorModel;


public class Simulator {
	//handles the main simulation of the population

	
	public double wildSusceptible;
	public double wildInfected;
	public double wildRecovered;
	public double mutantSusceptible;
	public double mutantInfected;
	public double mutantRecovered;
	public double vectorSusceptible;
	public double vectorInfected;
	//public static final double[] defaultStart = {12900,100,0,1500,0,0,14000,0};
	
	public static final double[] defaultStart = {13000,0,0,1300,200,0,14000,0};
	
	//public static final double[] defaultStart = {0.8666, 0.0006666, 0, 0.13333334,0,0,1,0};
	
	public double wildTransmissionRate;
	public double wildRecoveryRate;
	public double wildUninfectedDeath;
	public double wildInfectedDeath;
	public double wildUninfectedBirth;
	public double wildInfectedBirth;
	
	public double mutantTransmissionRate;
	public double mutantRecoveryRate;
	public double mutantUninfectedDeath;
	public double mutantInfectedDeath;
	public double mutantUninfectedBirth;
	public double mutantInfectedBirth;
	
	public double vectorTransmissionRate;
	
	public double vectorTransmissionFromWild;
	public double vectorTransmissionFromMutant;
	
	public double vectorDeathRate;
	public double carryingCapacity;
	
	public double biteRateWild;
	public double biteRateMutant;
	public double vectorMultiplier;
	
	
	//public static final double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.00164, 0.000274, 0.005, 0.003, 0.001, 0.001, 0.0015, 0.0012, 0.01, 0.02, 15000,1,1,1};	
	public static final double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.0011, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
	
	
	
	public double dayCount = 0;
	public double daysPerYear = 365;
	//public double daysPerYear = 1;
	
	
	public Simulator() {
		this(defaultStart, defaultParameters);
	}
	
	public Simulator(double[] start, double[] parameters) {
		wildSusceptible = start[0];;
		wildInfected = start[1];
		wildRecovered = start[2];
		mutantSusceptible = start[3];
		mutantInfected = start[4];
		mutantRecovered = start[5];
		vectorSusceptible = start[6];
		vectorInfected = start[7];
		
		wildTransmissionRate = parameters[0];				 //default 0.008
		wildRecoveryRate = parameters[1];					 //default 0		
		wildUninfectedDeath = parameters[2];				 //default 0.001
		wildInfectedDeath = parameters[3];					 //default 0.0025	
		wildUninfectedBirth = parameters[4];				 //default 0.002
		wildInfectedBirth = parameters[5];					 //default 0.0003
		
		mutantTransmissionRate = parameters[6];				 //default 0.005
		mutantRecoveryRate = parameters[7];					 //default 0.003
		mutantUninfectedDeath = parameters[8];				 //default 0.001
		mutantInfectedDeath = parameters[9];				 //default 0.001
		mutantUninfectedBirth = parameters[10];				 //default 0.0018
		mutantInfectedBirth = parameters[11];				 //default 0.0014
		
		vectorTransmissionRate = parameters[12];			 //default 0.05
		vectorTransmissionFromWild = parameters[12];
		vectorTransmissionFromMutant = parameters[12];
		
		vectorDeathRate = parameters[13];				 //default 0.02
		
		carryingCapacity = parameters[14];					 //default 15,000
		
		biteRateWild = parameters[15];						 //default 1
		biteRateMutant = parameters[16];					 //default 1
		vectorMultiplier = parameters[17];					 //default 1
		
		//
		//TEMP, probably delete
		//wildUninfectedBirth = convertBirth(wildUninfectedBirth);
		//wildInfectedBirth = convertBirth(wildInfectedBirth);
		//mutantUninfectedBirth = convertBirth(mutantUninfectedBirth);
		//mutantInfectedBirth = convertBirth(mutantInfectedBirth);
		//
		
		//reportParameters();
		//
		
	}
	
	
	//temp?
	public double convertBirth(double value) {
		//convert from daily values into yearly values based on continuous compounding
		//this is usually not used.
		

		//double newValue = (Math.pow(1+value, daysPerYear)-1)/daysPerYear ;
		double newValue = value*1.2;
		return newValue;
		
	}
	
	public void differentiateVectorTransmissions(double wild, double mutant) {
		vectorTransmissionFromWild = wild;
		vectorTransmissionFromMutant = mutant;
	}
	
	public void applyBiteRates(double wildBite, double mutantBite) {
		//wildTransmissionRate = wildTransmissionRate * wildBite;
		//vectorTransmissionFromWild = vectorTransmissionFromWild * wildBite;
		//mutantTransmissionRate = mutantTransmissionRate * mutantBite;
		//vectorTransmissionFromMutant = vectorTransmissionFromMutant * mutantBite;
		biteRateWild = wildBite;
		biteRateMutant = mutantBite;
	}
	
	public void applyMutationStrength(double r) {
		//continuous scale between wild (at 0), standard mutant (at 1) and beyond
		mutantTransmissionRate = wildTransmissionRate/(1+r);
		mutantRecoveryRate = 0.003*r;	
		//mutantRecoveryRate = 0;
		mutantUninfectedDeath =  wildUninfectedDeath;
		if(r < 1) {
			mutantInfectedDeath = 0.0025-0.0015*r;
			mutantUninfectedBirth = (0.00164 - 0.00013*r)*1.2;	
		}
		else {
			mutantInfectedDeath = 0.001;
			mutantUninfectedBirth = 0.00151*1.2;
		}
		mutantInfectedBirth = mutantUninfectedBirth*(16*r)/(16*r+5); // 0.2 at 0 and 0.8 at 1, asymptotically going to 1
		vectorTransmissionFromMutant = r*vectorTransmissionFromMutant+(1-r)*vectorTransmissionFromWild;
		if(vectorTransmissionFromMutant < 0) vectorTransmissionFromMutant = 0;
	}
	
	public void reportParameters() {
		//temp test class
		System.out.println("wildTransmissionRate: " + wildTransmissionRate);
		System.out.println("wildRecoveryRate: " + wildRecoveryRate);
		System.out.println("wildUninfectedDeath: " + wildUninfectedDeath);
		System.out.println("wildInfectedDeath: " + wildInfectedDeath);
		System.out.println("wildUninfectedBirth: " + wildUninfectedBirth);
		System.out.println("wildInfectedBirth: " + wildInfectedBirth);
		
		System.out.println("mutantTransmissionRate: " + mutantTransmissionRate);
		System.out.println("mutantRecoveryRate: " + mutantRecoveryRate);
		System.out.println("mutantUninfectedDeath: " + mutantUninfectedDeath);
		System.out.println("mutantInfectedDeath: " + mutantInfectedDeath);
		System.out.println("mutantUninfectedBirth: " + mutantUninfectedBirth);
		System.out.println("mutantInfectedBirth: " + mutantInfectedBirth);
		
		System.out.println("vectorTransmissionFromWild: " + vectorTransmissionFromWild);
		System.out.println("vectorTransmissionFromMutant: " + vectorTransmissionFromMutant);
		System.out.println("vectorReplacementRate: " + vectorDeathRate);
		System.out.println("carryingCapacity: " + carryingCapacity);
		System.out.println("biteRatewild: " + biteRateWild);
		System.out.println("biteRateMutant: " + biteRateMutant);
		System.out.println("vectorMultiplier: " + vectorMultiplier);

	}
		
	
	public void addToPop(double[] add) {
		//takes a vector and adds it to the relevant population values 
		//consider adding a check/cap at zero
		wildSusceptible += add[0];;
		wildInfected += add[1];
		wildRecovered += add[2];
		mutantSusceptible += add[3];
		mutantInfected += add[4];
		mutantRecovered += add[5];
		vectorSusceptible += add[6];
		vectorInfected += add[7];
	}
	
	public double[] getPopArray() {
		double[] pop = new double[8];
		pop[0] = wildSusceptible;
		pop[1] = wildInfected;
		pop[2] = wildRecovered;
		pop[3] = mutantSusceptible;
		pop[4] = mutantInfected;
		pop[5] = mutantRecovered;
		pop[6] = vectorSusceptible;
		pop[7] = vectorInfected;
		return pop;
	}
	
	
	
	public double getTotalPop() {
		//only includes hosts, not vectors
		double[] pop = getPopArray();
		double total = pop[0]+pop[1]+pop[2]+pop[3]+pop[4]+pop[5];
		return total;
	}
	
	public double getTotalWild() {
		double[] pop = getPopArray();
		double total = pop[0]+pop[1]+pop[2];
		return total;
	}
	
	public double getTotalMutant() {
		double[] pop = getPopArray();
		double total = pop[3]+pop[4]+pop[5];
		return total;
	}
	
	public double getTotalInfected() {
		double inf = wildInfected+mutantInfected;
		return inf;
	}
	
	public double getTotalVector() {
		double total = vectorSusceptible+vectorInfected;
		return total;
	}
	
	
	
	public void simDay() {
		//this is the main method to call from other classes
		
		boolean absurdVersion = false; //temp to test SRE paper
		if(absurdVersion) {
			//absurdTransmitDay();
			transmitDay();
			dayCount++;
			if(dayCount % daysPerYear == 0) absurdBreedingEvent();
			return;
		}
		
		
		
		boolean vector = true;
		//if(vector) transmitDay();
		if(vector) transmitDayVector();
		else transmitDayNoVector();
		dayCount++;
		
		//normal
		if(dayCount % daysPerYear == 0) breedingEvent();
		
		//
		//if(dayCount % 365 == 0) System.out.println("Total mutant pop: " + (mutantSusceptible+mutantInfected+mutantRecovered));
		//
		
	}
	
	public void transmitDay() {
		//runs one instance of the SIR model with infection spread
		//NOTE, this is outdated due to not properly accounting for conservation of bite rate
		//vector population is constant
		double add[] = new double[8];
		
		double vectorInfectionFreq = vectorInfected/(vectorSusceptible+vectorInfected);
		double wildInfectedFreq = wildInfected/getTotalPop();
		double mutantInfectedFreq = mutantInfected/getTotalPop();
		
		add[0] = wildSusceptible*(-wildTransmissionRate * vectorInfectionFreq - wildUninfectedDeath); //susceptibles get infected or die
		add[1] = wildSusceptible*(wildTransmissionRate * vectorInfectionFreq) + wildInfected*(-wildRecoveryRate - wildInfectedDeath); //new infections or recovered/dead
		add[2] = wildInfected*wildRecoveryRate - wildRecovered*wildUninfectedDeath; //will be 0 under default parameters
		
		add[3] = mutantSusceptible*(-mutantTransmissionRate * vectorInfectionFreq - mutantUninfectedDeath); 
		add[4] = mutantSusceptible*(mutantTransmissionRate * vectorInfectionFreq) + mutantInfected*(-mutantRecoveryRate - mutantInfectedDeath);
		add[5] = mutantInfected*mutantRecoveryRate - mutantRecovered*mutantUninfectedDeath;
		
		add[6] = -vectorSusceptible*(vectorTransmissionFromWild*wildInfectedFreq+vectorTransmissionFromMutant*mutantInfectedFreq)+vectorInfected*vectorDeathRate;
		add[7] = -add[6]; //vectorPopulation is held constant
		
		
		addToPop(add);
		//System.out.println("wildInfected: " + wildInfected);
		
		
		boolean vectorlessHack = false;
		if(vectorlessHack) { //replaces vectors with hosts, as in normal SIR model
			vectorInfected = wildInfected+mutantInfected;
			vectorSusceptible = getTotalPop() - vectorInfected;
		}

		
	}
	
	public void transmitDayVector() {
		//adjusts vector population to follow host population
		double add[] = new double[8];
		

		double wildInfectedFreq = wildInfected/getTotalPop();
		double mutantInfectedFreq = mutantInfected/getTotalPop();
		
		//assume each mosquito bites a constant number of hosts equal to biterate
		//each host gets bitten by (biteRate * vectorInfected / getTotalPop) infected mosquitos
		
		add[0] = wildSusceptible*(-wildTransmissionRate * (biteRateWild * vectorInfected /getTotalPop())  - wildUninfectedDeath); //susceptibles get infected or die
		add[1] = wildSusceptible*(wildTransmissionRate * (biteRateWild * vectorInfected /getTotalPop())) + wildInfected*(-wildRecoveryRate - wildInfectedDeath); //new infections or recovered/dead
		add[2] = wildInfected*wildRecoveryRate - wildRecovered*wildUninfectedDeath; //will be 0 under default parameters
		
		add[3] = mutantSusceptible*(-mutantTransmissionRate * (biteRateMutant * vectorInfected /getTotalPop()) - mutantUninfectedDeath); 
		add[4] = mutantSusceptible*(mutantTransmissionRate * (biteRateMutant * vectorInfected /getTotalPop())) + mutantInfected*(-mutantRecoveryRate - mutantInfectedDeath);
		add[5] = mutantInfected*mutantRecoveryRate - mutantRecovered*mutantUninfectedDeath;
		
		double vectorBirth = vectorDeathRate*vectorMultiplier*getTotalPop();
		double vectorInfectionGain = vectorSusceptible*(vectorTransmissionFromWild*biteRateWild*wildInfectedFreq+vectorTransmissionFromMutant*biteRateMutant*mutantInfectedFreq);
		add[6] = -vectorInfectionGain + vectorBirth - vectorSusceptible * vectorDeathRate;
		add[7] = vectorInfectionGain - vectorInfected * vectorDeathRate;
		
		
		addToPop(add);
		
		//
		//resuscept();
		//
	}
	
	public void transmitDayNoVector() {
		//runs one instance of the SIR model with infection spread with direct transmission
		double add[] = new double[8];
		
		double wildInfectedFreq = wildInfected/getTotalPop();
		double hostInfectionFreq = (wildInfected+mutantInfected)/getTotalPop();
		
		add[0] = wildSusceptible*(-wildTransmissionRate * hostInfectionFreq - wildUninfectedDeath); //susceptibles get infected or die
		add[1] = wildSusceptible*(wildTransmissionRate * hostInfectionFreq) + wildInfected*(-wildRecoveryRate - wildInfectedDeath); //new infections or recovered/dead
		add[2] = wildInfected*wildRecoveryRate - wildRecovered*wildUninfectedDeath; //will be 0 under default parameters
		
		add[3] = mutantSusceptible*(-mutantTransmissionRate * hostInfectionFreq - mutantUninfectedDeath); 
		add[4] = mutantSusceptible*(mutantTransmissionRate * hostInfectionFreq) + mutantInfected*(-mutantRecoveryRate - mutantInfectedDeath);
		add[5] = mutantInfected*mutantRecoveryRate - mutantRecovered*mutantUninfectedDeath;
		
		add[6] = 0;
		add[7] = 0;
		
		
		addToPop(add);
		//System.out.println("wildInfected: " + wildInfected);

		
	}
	
	public void absurdTransmitDay() {
		//way larger due to not normalizing for population, temp to test SRE paper
		
		double add[] = new double[8];
		
		double totalInfected = (wildInfected+mutantInfected);
		
		add[0] = wildSusceptible*(-wildTransmissionRate * totalInfected - wildUninfectedDeath); //susceptibles get infected or die
		add[1] = wildSusceptible*(wildTransmissionRate * totalInfected) + wildInfected*(-wildRecoveryRate - wildInfectedDeath); //new infections or recovered/dead
		add[2] = wildInfected*wildRecoveryRate - wildRecovered*wildUninfectedDeath; //will be 0 under default parameters
		
		add[3] = mutantSusceptible*(-mutantTransmissionRate * totalInfected - mutantUninfectedDeath); 
		add[4] = mutantSusceptible*(mutantTransmissionRate * totalInfected) + mutantInfected*(-mutantRecoveryRate - mutantInfectedDeath);
		add[5] = mutantInfected*mutantRecoveryRate - mutantRecovered*mutantUninfectedDeath;
		
		add[6] = 0;
		add[7] = 0;
		
		
		addToPop(add);
	}
	
	
	
	public void breedingEvent() {
		
		double currentPop = getTotalPop();
		if(currentPop > carryingCapacity) return;
		
		double babyWild = daysPerYear*(wildUninfectedBirth * (wildSusceptible+wildRecovered) + wildInfectedBirth*wildInfected);
		double babyMutant = daysPerYear*(mutantUninfectedBirth * (mutantSusceptible+mutantRecovered) + mutantInfectedBirth*mutantInfected);
		
		double capacityScaling = 0;
		if(currentPop + babyWild + babyMutant < carryingCapacity) capacityScaling = 1;
		else capacityScaling = (carryingCapacity - currentPop)/(babyWild+babyMutant);
		
		wildSusceptible += capacityScaling*babyWild;
		mutantSusceptible += capacityScaling*babyMutant;
		
		//System.out.println("breedingEvent.  New babies: " + (babyWild+babyMutant));
		//System.out.println("Total mutant pop: " + (mutantSusceptible+mutantInfected+mutantRecovered));
		
		
	}
	
	
	public void absurdBreedingEvent() {
		//I think this method of breeding is incorrect and absurd, but it appears to be what the SRE paper does.  Here for test purposes
		double currentPop = getTotalPop();
		if(currentPop > carryingCapacity) return;
		
		
		
		double avgAlpha = (wildUninfectedBirth+wildInfectedBirth + mutantUninfectedBirth+mutantInfectedBirth)/4;
		double absurdWildUninfectedBirth = (wildSusceptible+wildRecovered)/currentPop*wildUninfectedBirth/avgAlpha;
		double absurdWildInfectedBirth = (wildInfected)/currentPop*wildInfectedBirth/avgAlpha;
		double absurdMutantUninfectedBirth = (mutantSusceptible+mutantRecovered)/currentPop*mutantUninfectedBirth/avgAlpha;
		double absurdMutantInfectedBirth = (mutantInfected)/currentPop*mutantInfectedBirth/avgAlpha;
		

		double babyWild = daysPerYear*(absurdWildUninfectedBirth * (wildSusceptible+wildRecovered) + absurdWildInfectedBirth*wildInfected);
		double babyMutant = daysPerYear*(absurdMutantUninfectedBirth * (mutantSusceptible+mutantRecovered) + absurdMutantInfectedBirth*mutantInfected);
		
		double capacityScaling = 0;
		if(currentPop + babyWild + babyMutant < carryingCapacity) capacityScaling = 1;
		else capacityScaling = (carryingCapacity - currentPop)/(babyWild+babyMutant);
		
		wildSusceptible += capacityScaling*babyWild;
		mutantSusceptible += capacityScaling*babyMutant;
		
		//System.out.println("breedingEvent.  New babies: " + (babyWild+babyMutant));
		//System.out.println("Total mutant pop: " + (mutantSusceptible+mutantInfected+mutantRecovered));
		
		
		
		
	}
	
	
	public void resuscept() {
		//hack method to remove the recovered population by making them susceptible again
		//there is definitely a better way to do this, but it would mess with equations
		mutantSusceptible += mutantRecovered;
		mutantRecovered = 0;	
	}
	
	
	
	public double transmitDayR0Measure() {
		//runs a modified version of a day where no new infected vectors happen
		//so the impact of a single infected vector can be measured
		//susceptible host deaths have been removed to allow longer timescales without breeding events
		//returns the infected vectors for this particular day.  sum over a looong time to estimate r0

		double add[] = new double[8];
		double wildInfectedFreq = wildInfected/getTotalPop();
		double mutantInfectedFreq = mutantInfected/getTotalPop();
		double omittedInfected = 0;
		
		add[0] = wildSusceptible*(-wildTransmissionRate * (biteRateWild * vectorInfected /getTotalPop())); //susceptibles get infected or die
		add[1] = wildSusceptible*(wildTransmissionRate * (biteRateWild * vectorInfected /getTotalPop())) + wildInfected*(-wildRecoveryRate - wildInfectedDeath); //new infections or recovered/dead
		add[2] = wildInfected*wildRecoveryRate - wildRecovered*wildUninfectedDeath; //will be 0 under default parameters
		
		add[3] = mutantSusceptible*(-mutantTransmissionRate * (biteRateMutant * vectorInfected /getTotalPop())); 
		add[4] = mutantSusceptible*(mutantTransmissionRate * (biteRateMutant * vectorInfected /getTotalPop())) + mutantInfected*(-mutantRecoveryRate - mutantInfectedDeath);
		add[5] = mutantInfected*mutantRecoveryRate - mutantRecovered*mutantUninfectedDeath;
		
		double vectorBirth = vectorDeathRate*vectorMultiplier*getTotalPop();
		double vectorInfectionGain = vectorSusceptible*(vectorTransmissionFromWild*biteRateWild*wildInfectedFreq+vectorTransmissionFromMutant*biteRateMutant*mutantInfectedFreq);
		add[6] = vectorBirth - vectorSusceptible * vectorDeathRate;
		add[7] = -vectorInfected * vectorDeathRate;
		
		omittedInfected = vectorInfectionGain;
		
		addToPop(add);
		return omittedInfected;
	}
	
	
	public double computeRW() {
		double RW = (wildTransmissionRate * vectorTransmissionFromWild * biteRateWild * biteRateWild * vectorMultiplier)/((wildRecoveryRate+wildInfectedDeath)*vectorDeathRate);
		return RW;
	}
	
	public double computeRM() {
		double RM = (mutantTransmissionRate * vectorTransmissionFromMutant * biteRateMutant * biteRateMutant * vectorMultiplier)/((mutantRecoveryRate+mutantInfectedDeath)*vectorDeathRate);
		return RM;
	}
	
	public double computeFormulaRV() {
		double Iv = nastyQuadraticTest(0);
		Iv = nastyQuadraticTest(Iv);	double Sv = 1 - Iv;
		double Iwc = wildTransmissionRate*biteRateWild*vectorMultiplier/(wildInfectedDeath+wildRecoveryRate);
		double Imc = mutantTransmissionRate*biteRateMutant*vectorMultiplier/(mutantInfectedDeath+mutantRecoveryRate);
		double Iwf = (Iv*Iwc)/(1+Iwc * Iv);	double Swf = 1 - Iwf;
		double Imf = (Iv*Imc)/(1+Imc * Iv);	double Smf = 1 - Imf;
		double Sw = Swf * getTotalWild()/getTotalPop();
		double Sm = Smf * getTotalMutant()/getTotalPop();
		//
		double naiveRV = (computeRW()*getTotalWild()+computeRM()*getTotalMutant())/getTotalPop();
		System.out.println("naiveRV: " + naiveRV);
		//
		double RV = Sv*(computeRW()*Sw+computeRM()*Sm);
		System.out.println("formula RV: " + RV);
		
		//
		
		System.out.println("computeFormulaRV variable comparison:");
		//System.out.println("Sv: " + Sv + " vectorSusceptible: " + vectorSusceptible/getTotalVector());
		//System.out.println("Swf: " + Swf + " wildSusceptibleF: " + wildSusceptible/getTotalWild());
		//System.out.println("Smf: " + Smf + " mutantSusceptibleF: " + mutantSusceptible/getTotalMutant());
		
		
		double predictedW = (1-computeRM()*Smf)/(Sv * (computeRW()*Swf-computeRM()*Smf));
		System.out.println("predictedW: " + predictedW + " actualW " + getTotalWild()/getTotalPop());
		
		return RV;		
	}
	
	public double computeCurrentRV() {
		double RV = vectorSusceptible/getTotalVector()*(computeRW()*wildSusceptible+computeRM()*mutantSusceptible)/getTotalPop();
		return RV;
	}
	
	public double predictW() {
		double w0 = defaultStart[4]/carryingCapacity;
		return predictW(w0);
	}
	
	public double predictW(double w0) {
		//almost the same as computeFormulaRV, but returns predictedW
		double Iv = nastyQuadraticTest(0);
		Iv = nastyQuadraticTest(Iv);	double Sv = 1 - Iv;
		double Iwc = wildTransmissionRate*biteRateWild*vectorMultiplier/(wildInfectedDeath+wildRecoveryRate);
		double Imc = mutantTransmissionRate*biteRateMutant*vectorMultiplier/(mutantInfectedDeath+mutantRecoveryRate);
		double Iwf = (Iv*Iwc)/(1+Iwc * Iv);	double Swf = 1 - Iwf;
		double Imf = (Iv*Imc)/(1+Imc * Iv);	double Smf = 1 - Imf;
		

		double RMeff = computeRM()*Smf;
		double RWeff = computeRW()*Swf;
		
		

		
		
		double wEq = (1-RMeff)/(Sv * (RWeff-RMeff));
		//System.out.println("predictedW: " + predictedW + " actualW " + getTotalWild()/getTotalPop());
		
		
		double predictedW;
		if(RMeff < 1 && RWeff < 1) predictedW = 1; //no infection, wild dominates
		else if(RWeff < 1 && RMeff > 1) {
			if(w0 > wEq) predictedW = 1;
			else predictedW = 0;
		}
		else if(RWeff > 1 && RMeff > 1) predictedW = 0;
		else predictedW = wEq;
		
		//System.out.println("predictedW: " + predictedW);
		if(predictedW < 0) predictedW = 0;
		if(predictedW > 1) predictedW = 1;
		
		//
		if(vectorMultiplier == 1) {
			System.out.println("vd1: RW: " + computeRW() + "  RWeff " + RWeff);
			System.out.println("vd1: RM: " + computeRM() + "  RMeff " + RMeff);
			System.out.println("wEq: " + wEq + "  w0: " + w0 + "  wPred: " + predictedW);
		}
		//
		return predictedW;		
	}
	
	public double nastyQuadraticTest(double estimatedIV) {
		//stuff that combines algebraic coefficients in the equilibrium infection
		//assumes mutantInfectedDeath equals mutantUninfectedDeath for simplicity
		
		
		//double Iw = wildTransmissionRate*biteRateWild/(wildInfectedDeath+wildRecoveryRate);
		//double Im = mutantTransmissionRate*biteRateMutant/(mutantInfectedDeath+mutantRecoveryRate);
		double Iw = wildTransmissionRate*biteRateWild*vectorMultiplier/(wildInfectedDeath+wildRecoveryRate);
		double Im = mutantTransmissionRate*biteRateMutant*vectorMultiplier/(mutantInfectedDeath+mutantRecoveryRate);
		
		Iw = Iw/(1+Iw * estimatedIV);
		Im = Im/(1+Im * estimatedIV);
		double muw = wildInfectedDeath-wildUninfectedDeath;
		double mum = mutantInfectedDeath-mutantUninfectedDeath;
		double alpw = wildInfectedBirth - wildUninfectedBirth;
		double alpm = mutantInfectedBirth - mutantUninfectedBirth;
		//double a = Iw*Im*muw*alpm;  //old version with approximation that mum = 0
		double a = Iw*Im*(muw*alpm-alpw*mum);
		//double b = Iw*muw*mutantUninfectedBirth - Iw * alpw * mutantUninfectedDeath + Im * alpm * wildUninfectedDeath;
		double b = Iw*muw*mutantUninfectedBirth - Iw*alpw*mutantUninfectedDeath + Im*alpm*wildUninfectedDeath-Im*mum*wildUninfectedBirth;
		double c = wildUninfectedDeath * mutantUninfectedBirth - wildUninfectedBirth * mutantUninfectedDeath;
		

		
		double quadraticCenter = -b / (2*a);
		double radicand = b*b-4*a*c;
		//if(radicand < 0) System.out.println("radicand < 0 in Simulator.nastyQuadraticTest(): " + radicand);
		//else {
			double side = Math.sqrt(radicand)/(2*a);
			double left = quadraticCenter - side;
			double right = quadraticCenter + side;
			//System.out.println("left: " + left + "  right: " + right);
		//}
			
			//
			//System.out.println("a: " + a);
			//System.out.println("b: " + b);
			//System.out.println("c: " + c);
			//System.out.println("quad: " + quadraticCenter + " +/- " + side);
			
			//
		
		
		//System.out.println("actual infected vector freq: " + vectorInfected/getTotalVector());	
		return right;
	}
	
	
	public boolean predictExtinction(double w) {
		boolean extinction = false;
		int numDays = 365;
		//estimates whether population goes extinct based on infectivity levels, and then birth/death from those
		double RW = computeRW();
		double RM = computeRM();
		int region;
		if(RW <= 1 && RM <= 1) region = 1; //no infection, only wilds
		else if (w == 1) region = 2; //some infection, but no mutants
		else if (w > 0) region = 3; //some infection, balanced by wild/mutant
		else region = 4; //much infection, wilds are extinct
		
		//System.out.println("predictExtinction region: " + region);
		
		switch(region) {
		case 1:
			double numBirths = numDays*Math.pow(1-wildUninfectedDeath,numDays)*wildUninfectedBirth;
			double numDeaths = 1 - Math.pow(1-wildUninfectedDeath, numDays);
			//System.out.println("numDeaths: " + numDeaths);
			//System.out.println("numBirths: " + numBirths);
			if(numDeaths > numBirths) extinction = true;
			else extinction = false;
			break;
		case 2:
			double wSus = 1/RW;
			double avgDeath = wSus*wildUninfectedDeath+(1-wSus)*wildInfectedDeath;
			double avgBirth = wSus*wildUninfectedBirth+(1-wSus)*wildInfectedBirth;
			double nmBirths = numDays*Math.pow(1-avgDeath,numDays)*avgBirth;
			double nmDeaths = 1 - Math.pow(1-avgDeath, numDays);
			if(nmDeaths > nmBirths) extinction = true;
			else extinction = false;
			break;
		case 3:
			double Iv = nastyQuadraticTest(0);
			Iv = nastyQuadraticTest(Iv);
			double Imc = mutantTransmissionRate*biteRateMutant/(mutantInfectedDeath+mutantRecoveryRate);
			double Imf = (Iv*Imc)/(1+Imc * Iv);	double Smf = 1 - Imf;
			double avgMDeath = Smf*mutantUninfectedDeath+(1-Smf)*mutantInfectedDeath;
			double avgMBirth = Smf*mutantUninfectedBirth+(1-Smf)*mutantInfectedBirth;
			double mBirths = numDays*Math.pow(1-avgMDeath,numDays)*avgMBirth;
			double mDeaths = 1 - Math.pow(1-avgMDeath, numDays);
			if(mDeaths > mBirths) extinction = true;
			else extinction = false;
			break;
		case 4:
			//double mInf = (mutantUninfectedDeath)/(mutantRecoveryRate+mutantInfectedDeath+mutantUninfectedDeath);
			double mInf = (mutantUninfectedDeath)/(mutantRecoveryRate+mutantUninfectedDeath);
			double mUn = 1-mInf;
			//the above is weird and should be adjusted to make fewer approximations
			//System.out.println("Simulator.predictExtinction case4 mUn: " + mUn);
			

			
			
			double avMDeath = mUn*mutantUninfectedDeath+(1-mUn)*mutantInfectedDeath;
			double avMBirth = mUn*mutantUninfectedBirth+(1-mUn)*mutantInfectedBirth;
			double muBirths = numDays*Math.pow(1-avMDeath,numDays)*avMBirth;
			double muDeaths = 1 - Math.pow(1-avMDeath, numDays);
			
			//double daysInfected = 1/(mutantInfectedDeath+mutantRecoveryRate);
			//double infDeaths = mutantInfectedDeath * daysInfected;
			//double uninfDeaths = 1- (1-infDeaths)*Math.pow(1-mutantUninfectedDeath, numDays-daysInfected);
			//double newmuDeaths = infDeaths + uninfDeaths;
			//double newmuBirths = (1-newmuDeaths)*numDays*avMBirth;
			
			
			//System.out.println("Simulator.predictExtinction oldmuDeaths: " + oldmuDeaths);
			//System.out.println("Simulator.predictExtinction newmuDeaths: " + newmuDeaths);			
			
			
			if(muDeaths > muBirths) extinction = true;
			else extinction = false;
			break;
			
		
		
		default: System.out.println("invalid region in Simulator.predictExtinction()");
		}
			
		
		return extinction;
		
	}
	
	
	
	public double simplifiedPredictW() {
		double w;
		double RW = computeRW();
		double RM = computeRM();
		if(RW <= 1) { //region 1
			w = 1;
			if(RM > 1) System.out.println("RM > 1 and RW < 1 in SimulatorsimplifiedPredictW(), this may lead to inaccurate predictions given initial conditions");
		}
		else if(RM < 1) w = (1-RM)/(RW-RM); //region 3
		else w = 0;
		return w;		
	}
	
	public boolean simplifiedExtinction(double w) {
		boolean extinct = false;
		if(w == 1 && wildUninfectedBirth < wildUninfectedDeath) extinct = true;
		if (w < 1 && mutantUninfectedBirth < mutantUninfectedDeath) extinct = true;
		return extinct;
		
	}
	
	
	public double realismLine(boolean mutant) {
		//finds the threshhold where an uninfected population would be able to survive
		double death;
		double baseBirth;
		if(mutant) { death = mutantUninfectedDeath; baseBirth = mutantUninfectedBirth;}
		else { death = wildUninfectedDeath; baseBirth = wildUninfectedBirth;}
		
		double inc = 0.01;
		double bMult = 0;
		boolean passed = false;
		while(!passed) {
			bMult += inc;
			double birth = baseBirth * bMult;
			double numBirths = daysPerYear*Math.pow(1-death,daysPerYear)*birth;
			double numDeaths = 1 - Math.pow(1-death, daysPerYear);
			if(numDeaths < numBirths) passed = true;
		}
		System.out.println("realismLine, mutant " + mutant + ": " + bMult);
		return bMult;
		
		
	}
	
	
	
}
