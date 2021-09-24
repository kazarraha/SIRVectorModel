package SIRVectorModel;

import java.awt.Color;
import java.util.ArrayList;


public class ActionManager {
//contains useful static methods that run sequences of commands to make nice diagrams and test hypotheses and stuff
	
	
	public ActionManager(){
		
	}
	
	
	
	
	public static void infectedDeathVSmutantRecovery(MainGUI gui) {
		//copies Figure 4 from SRE paper
		double[] defaultParameters = Simulator.defaultParameters;
		
		int numGens = 20;
		
		double idRatioMin = 1.5;
		double idRatioMax = 2.3;
		int numY = 20;
		double idRatioInc = (idRatioMax-idRatioMin)/numY;
		
		double mrRatioMin = 0;
		double mrRatioMax = 200;
		int numX = 20;
		double mrRatioInc = (mrRatioMax-mrRatioMin)/numX;
		
		double idMutant = 0.0011;
		double rWild = 0.00001;
		
		
		
		
		//ALTERNATE OVERWRITE
		boolean alternate = true;
		if(alternate) {
			idRatioMin = 1;
			idRatioMax = 3;
			numY = 80;
			idRatioInc = (idRatioMax-idRatioMin)/numY;
			
			mrRatioMin = 0;
			mrRatioMax = 400;
			numX = 80;
			mrRatioInc = (mrRatioMax-mrRatioMin)/numX;
			
			idMutant = 0.0011;
			rWild = 0.00001;
		}
		
		
		double[][] v = new double[numY][numX];
		for(int y = 0; y < numY; y++) {
			double idRatio = idRatioMin+idRatioInc*y;
			double idWild = idMutant*idRatio;
			for(int x = 0; x < numX; x++) {
				double mrRatio = mrRatioMin+mrRatioInc*x;
				double rMutant = rWild * mrRatio;
				double[] parameters = defaultParameters;
				parameters[3] = idWild;
				parameters[7] = rMutant;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				//
				sim.wildInfected = 100;
				//
				
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
				if(sim.getTotalWild() > 0)v[y][x] = sim.getTotalMutant()/sim.getTotalWild();
				else v[y][x] = 10000000;
				
				//
				//System.out.println("total mutant: " + sim.getTotalMutant() + "  total wild: " + sim.getTotalWild());
				//
				
				//
				if(x == 60 && y == 3) {
				gui.sim = sim;
				gui.repaint();
				}
				//
				
			}
		}
		
		VarySquare square = new VarySquare(v, "infectedDeathRatio (y) vs mutantRecovery ratio (x)");
		
		
	}
	
	
	public static void infectedDeathVSmutantRecoveryRGB(MainGUI gui) {
		//copies Figure 4 from SRE paper
		double[] defaultParameters = Simulator.defaultParameters;
		
		int numGens = 8;

			double idRatioMin = 1;
			double idRatioMax = 3;
			int numY = 80;
			double idRatioInc = (idRatioMax-idRatioMin)/numY;
			
			double mrRatioMin = 0;
			double mrRatioMax = 400;
			int numX = 80;
			double mrRatioInc = (mrRatioMax-mrRatioMin)/numX;
			
			double idMutant = 0.0011;
			double rWild = 0.00001;
		
		
		
		double[][][] v = new double[numY][numX][3];
		for(int y = 0; y < numY; y++) {
			double idRatio = idRatioMin+idRatioInc*y;
			double idWild = idMutant*idRatio;
			for(int x = 0; x < numX; x++) {
				double mrRatio = mrRatioMin+mrRatioInc*x;
				double rMutant = rWild * mrRatio;
				double[] parameters = defaultParameters;
				parameters[3] = idWild;
				parameters[7] = rMutant;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				//
				sim.wildInfected = 100;
				//
				
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
				
				//
				//System.out.println("total mutant: " + sim.getTotalMutant() + "  total wild: " + sim.getTotalWild());
				//
				
				//
				if(x == 1 && y == 9) {
				gui.sim = sim;
				gui.repaint();
				}
				//
				
			}
		}
		
		VarySquare square = new VarySquare(v, "infected death (y) vs mutant recovery (x)");
		
		
	}
	
	
	
	public static void applyBiteStuff(Simulator sim) {
		//test alternate stuff with vectors and biterates
		double vectorFromWildScale = 1.2;
		double vectorFromMutantScale = 0.8;
		double wildBiteScale = 1;
		double mutantBiteScale = 2;
		
		sim.differentiateVectorTransmissions(sim.vectorTransmissionFromWild*vectorFromWildScale, sim.vectorTransmissionFromMutant*vectorFromMutantScale);
		sim.applyBiteRates(wildBiteScale, mutantBiteScale);
		
	}
	
	
	public static void mutantDeathVsMutantBite(MainGUI gui) {
		//mutantDeathRate on y axis, mutant bite rate from vector on x axis
		double[] defaultParameters = Simulator.defaultParameters;
		
		int numGens = 30;

			double idMin = 0.001;
			//double idMax = 0.0025;
			double idMax = 0.0051;
			int numY = 80;
			double idInc = (idMax-idMin)/numY;
			
			double biteMin = 0;
			double biteMax = 5;
			int numX = 80;
			double biteInc = (biteMax-biteMin)/numX;
	
		double[][][] v = new double[numY][numX][3];
		for(int y = 0; y < numY; y++) {
			double id = idMin+idInc*y;
			for(int x = 0; x < numX; x++) {
				double bite = biteMin+biteInc*x;
				double[] parameters = defaultParameters;
				parameters[9] = id; //mutant infected death rate
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				sim.applyBiteRates(1, bite);
				//
				sim.wildInfected = 100;
				//
				
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
				
				
				//
				//System.out.println("total mutant: " + sim.getTotalMutant() + "  total wild: " + sim.getTotalWild());
				//
				
				//
				if(x == 1 && y == 9) {
				gui.sim = sim;
				gui.repaint();
				}
				//
				
			}
		}
		
		VarySquare square = new VarySquare(v, "mutant infected death (y) vs mutant bite rate (x)");
		
		
	}
	
	
	public static void mutationStrengthVsMutantBite(MainGUI gui) {
		int numGens = 50;
		mutationStrengthVsMutantBite(gui, numGens);
		
	}

	public static void mutationStrengthVsMutantBite(MainGUI gui, int numGens) {
		//mutation disease resistance varies on y axis, bite rate on x axis
		//double[] defaultParameters = {0.008, 0.00001, 0.001, 0.0025, 0.00164, 0.000274, 0.005, 0.003, 0.001, 0.0011, 0.00151, 0.00121, 0.01, 0.02, 15000,1,1,1};	
		double[] defaultParameters = Simulator.defaultParameters;
			double msMin = 0;
			double msMax = 2;
			//double msMax = 8;
			int numY = 80;
			double msInc = (msMax-msMin)/numY;
			
			double biteMin = 0;
			double biteMax = 5;
			//double biteMax = 20;
			int numX = 80;
			double biteInc = (biteMax-biteMin)/numX;
			
	
		double[][][] v = new double[numY][numX][3];
		double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double ms = msMin+msInc*y;
			for(int x = 0; x < numX; x++) {
				double bite = biteMin+biteInc*x;
				double[] parameters = defaultParameters;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				//sim.applyMutationStrength(ms);
				//sim.applyBiteRates(1, bite);
				adjustParameters(sim, "mutationStrength", ms);
				//adjustParameters(sim, "partialMutationStrength", ms);
				adjustParameters(sim, "bite", bite);
				
				
				sim.wildInfected = 100;
				//
				
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
				
				manageFormulaV(formulaV, y, x, sim); //predicts the red/blue values based on formula
				
				
				vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInfVec[y][x] = 3*sim.vectorInfected;
				//
				//System.out.println("x " + x + " y " + y + " total mutant: " + sim.getTotalMutant() + "  total wild: " + sim.getTotalWild());
				//
				
				//
				if(x == 20 && y == 70) {
				gui.sim = sim;
				gui.repaint();
				}
				
			}
		}	
		VarySquare square = new VarySquare(v, "mutant strength (y) vs mutant bite rate (x)");
		VarySquare formulaSquare = new VarySquare(formulaV, "ms vs br as predicted by formula");
		//VarySquare infectedSquare = new VarySquare(vInf, "infection levels for mutant strength (y) vs mutant bite rate (x)");
		//infectedSquare.mainColor = Color.green; infectedSquare.repaint();
		//VarySquare infectedVectorSquare = new VarySquare(vInfVec, "vector infection levels for mutant strength (y) vs mutant bite rate (x)");
		//infectedVectorSquare.mainColor = Color.green; infectedVectorSquare.repaint();
		//
		findLineSlope(v);
		
	}
	
	public static void mutationStrengthVsMutantBiteOverlay() {
		//runs mutationStrengthVsMutantBite several time and overlays them with transparency on top of each other
		double[] defaultParameters = Simulator.defaultParameters;
		double msMin = 0;
		double msMax = 2;
		int numY = 80;
		double msInc = (msMax-msMin)/numY;
		
		double biteMin = 0;
		double biteMax = 5;
		int numX = 80;
		double biteInc = (biteMax-biteMin)/numX;

		int genInc = 20;
		int numMaps = 1;
		ArrayList<double[][][]> set = new ArrayList<double[][][]>();
		for(int m = 0; m < numMaps; m++) {
		int numGens = (m+1)*genInc; //TEMP -25
		double[][][] v = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double ms = msMin+msInc*y;
			for(int x = 0; x < numX; x++) {
				double bite = biteMin+biteInc*x;
				double[] parameters = defaultParameters;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				adjustParameters(sim, "mutationStrength", ms);
				adjustParameters(sim, "bite", bite);	
				sim.wildInfected = 100;
			
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
	
				vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInfVec[y][x] = 3*sim.vectorInfected;
			}
		}
		set.add(v);
		}
		VarySquare.overlay(set);
		//VarySquare square = new VarySquare(v, "mutant strength (y) vs mutant bite rate (x)");
	}
	
	public static String adjustParameters(Simulator sim, String version, double progress) {
		String label = "";
		switch (version) {
        case "mutationStrength":  		sim.applyMutationStrength(progress);
                 break;
        case "partialMutationStrength":		applyPartialMutationStrength(sim, progress);
        		break;
        case "bite":  sim.applyBiteRates(1, progress);
                 break;
        case "wThrive": sim.wildUninfectedBirth = progress*sim.wildUninfectedBirth;
        				sim.wildInfectedBirth = progress*sim.wildInfectedBirth;
        				//sim.wildUninfectedDeath = (2*sim.wildUninfectedDeath)/(0.01+progress);
        				//sim.wildInfectedDeath = (2*sim.wildInfectedDeath)/(0.01+progress);
        		break;
        case "mThrive": sim.mutantUninfectedBirth = progress*sim.mutantUninfectedBirth;
						sim.mutantInfectedBirth = progress*sim.mutantInfectedBirth;
						//sim.mutantUninfectedDeath = sim.mutantUninfectedDeath/(0.01+progress);
						//sim.mutantInfectedDeath = sim.mutantInfectedDeath/(0.01+progress);
				break;		
        //case "infectivity": sim.mutantTransmissionRate = progress*sim.mutantTransmissionRate;
		//					sim.wildTransmissionRate = progress*sim.wildTransmissionRate;
        case "infectivity": sim.vectorMultiplier *= progress;
        					sim.vectorSusceptible *= progress;
				break;
		
        	
		default: System.out.println("Invalid version passed to ActionManager.adjustParameters()");
		}
		
		
		return label;
		
	}
	
	
	
	public static void applyPartialMutationStrength(Simulator sim, double rate) {
		//applies partial by manually setting r = rate at the appropriate spot
		double r = 0;
		
		
		//continuous scale between wild (at 0), standard mutant (at 1) and beyond
		sim.mutantTransmissionRate = sim.wildTransmissionRate/(1+r);
		r = rate;
		sim.mutantRecoveryRate = 0.003*r;	
		r = 0;
		sim.mutantUninfectedDeath =  sim.wildUninfectedDeath;
		
		if(r < 1) {
			sim.mutantInfectedDeath = 0.0025-0.0015*r;
			sim.mutantUninfectedBirth = (0.00164 - 0.00013*r)*1.2;	
		}
		else {
			sim.mutantInfectedDeath = 0.001;
			sim.mutantUninfectedBirth = 0.00151*1.2;
		}
		sim.mutantInfectedBirth = sim.mutantUninfectedBirth*(16*r)/(16*r+5); // 0.2 at 0 and 0.8 at 1, asymptotically going to 1
		sim.vectorTransmissionFromMutant = r*sim.vectorTransmissionFromMutant+(1-r)*sim.vectorTransmissionFromWild;
		if(sim.vectorTransmissionFromMutant < 0) sim.vectorTransmissionFromMutant = 0;
		System.out.println("vectorTransmissionFromMutant: " + sim.vectorTransmissionFromMutant);
	}
	
	
	public static double findLineSlope(double[][][] v) {
		//find the slope of the purple/red line in the data
		int lowerY = 19;
		int upperY = 79;
		double[][] coord = new double[2][2];
		double threshhold = 1000;
		for(int i = 0; i < 2; i++) {
			int y = lowerY;
			if(i == 1) y = upperY;
			boolean found = false;
			int foundX = 0;
			int x = 0;
			while(!found && x < v[y].length) {
				if(v[y][x][2] < threshhold) { //blue/wild
					foundX = x;
					found = true;
				}	
				x++;
			}
			if(!found) System.out.println("x swap not found in ActionManager.findLineSlope()");			
			coord[i][0] = foundX;
			coord[i][1] = y;
		}
		
		//System.out.println("x1: " + coord[0][0] + " y1: " + coord[0][1] + " x2: " + coord[1][0] + " y2: " + coord[1][1]);
		double slope = (coord[1][1]-coord[0][1])/(coord[1][0]-coord[0][0]);
		System.out.println("slope: " + slope);
		return slope;
	}

	public static void findSlopesAsGensVary(MainGUI gui) {
		//changes number of gens and finds the slope of red/purple line as that changes (inefficiently)
		for(int gens = 16; gens < 80; gens++) {
			System.out.print("gen " + gens + ":  ");
			mutationStrengthVsMutantBite(gui, gens);
		}
		
	}
	
	
	public static void graphInfectionOverTime() {
		//tracks the total population and number of infected over time
		double[] defaultParameters = Simulator.defaultParameters;
		int numStartGens = 0;
		int numGens = 40;
		int numPoints = 200;
		int daysPerPoint = 365*numGens/numPoints;
		
		double[][] multiGraph = new double[4][numPoints];
		double[] totalPop = new double[numPoints];
		double[] totalMutant = new double[numPoints];
		double[] totalWild = new double[numPoints];
		double[] totalInfected = new double[numPoints];
		
		Simulator sim = new Simulator(Simulator.defaultStart, defaultParameters);
		//TEMP for figure
		//sim.applyBiteRates(1,100);
		//
		for(int i = 0; i < 365*numStartGens; i++) sim.simDay(); //skip ahead before the graph starts;
		for(int p = 0; p < numPoints; p++) {
			totalPop[p] = sim.getTotalPop();
			totalMutant[p] = sim.getTotalMutant();
			totalWild[p] = sim.getTotalWild();
			totalInfected[p] = sim.getTotalInfected();
			for(int i = 0; i < daysPerPoint; i++) sim.simDay();
		}
		
		multiGraph[0] = totalPop;
		multiGraph[1] = totalMutant;
		multiGraph[2] = totalWild;
		multiGraph[3] = totalInfected;
		int[] xMarks = {0,10,20,30,40};
		double[] yMarks = {0,5000,10000,15000};
		GraphHandler handler = new GraphHandler(multiGraph, "total pop and total infected over time for: " + numGens + " generations");
		handler.setMarks(xMarks, yMarks);
		Color[] customColors = {Color.magenta, Color.red, Color.blue, Color.green};
		handler.setCustomColors(customColors);
		
		
	}
	
	public static void graphInfectionOverTimeMulti() {
		//graphs mutliple lines over time with small parameter changes
		double[] defaultParameters = Simulator.defaultParameters;
		int numStartGens = 0;
		int numGens = 400;
		int numPoints = 400;
		//int numGens = 2;
		//int numGens = 20000;
		//int numPoints = 800;
		int daysPerPoint = 365*numGens/numPoints;
		int totalDays = 365*numGens;
		//

		
		int numCopies = 8;
		double[][] multiGraph = new double[4*numCopies][numPoints];
		double[][] infectionGraph = new double[numCopies][numPoints];
		
		double[] avgSList = new double[numCopies];
		for(int c = 0; c < numCopies; c++) {
		double[] totalPop = new double[numPoints];
		double[] totalMutant = new double[numPoints];
		double[] totalWild = new double[numPoints];
		double[] totalInfected = new double[numPoints];
		
		Simulator sim = new Simulator(Simulator.defaultStart, defaultParameters);
		adjustParameters(sim, "infectivity", 0.25+0.25*c);
		//adjustParameters(sim, "mThrive", 0.2+0.2*c);
		//adjustParameters(sim, "wThrive", 0.2+0.2*c);
		//sim.wildSusceptible = sim.carryingCapacity/2;
		//sim.applyBiteRates(1,1+0.1*c);
		//sim.applyBiteRates(1, 1.26+0.001*c);
		//sim.applyBiteRates(1, 1.1+0.01*c);
		
		double totalR = 0;
		double totalS = 0;
		for(int i = 0; i < 365*numStartGens; i++) sim.simDay(); //skip ahead before the graph starts;
		for(int p = 0; p < numPoints; p++) {
			totalPop[p] = sim.getTotalPop();
			totalMutant[p] = sim.getTotalMutant();
			totalWild[p] = sim.getTotalWild();
			//totalInfected[p] = sim.getTotalInfected();
			totalInfected[p] = sim.wildInfected/sim.getTotalWild();
			
			for(int i = 0; i < daysPerPoint; i++) {
				sim.simDay();
				totalR += sim.computeCurrentRV();
				//totalS += (sim.mutantSusceptible)/sim.getTotalMutant();
				totalS += sim.vectorInfected/sim.getTotalVector();
			}
		}
		double avgR = totalR/totalDays;
		avgSList[c] = totalS/totalDays;
		
		multiGraph[0+4*c] = totalPop;
		multiGraph[1+4*c] = totalMutant;
		multiGraph[2+4*c] = totalWild;
		//multiGraph[3+4*c] = totalInfected;
		infectionGraph[c] = totalInfected;
		
		//temp for r0 measure
		//System.out.println("biterate: " + sim.biteRateMutant);
		//increasingFunctionDetector(totalWild);
		//System.out.println("avgR: " + avgR);
		//System.out.println("avgS: " + avgSList[c]);
		//halfLifeDetector(totalWild);
		sim.computeFormulaRV();
		//
		}
		int[] xMarks = {0,50,100,150,200};
		double[] yMarks = {0,5000,10000,15000.1};
		GraphHandler handler = new GraphHandler(multiGraph, "total pop and total infected over time for: " + numGens + " generations");
		handler.setMarks(xMarks, yMarks);
		Color[] customColors = new Color[4*numCopies];
		Color[] greenColor = new Color[numCopies];
		for(int c = 0; c < numCopies; c++) {
			customColors[0+4*c] = Color.magenta; customColors[1+4*c] = Color.red; customColors[2+4*c] = Color.blue; customColors[3+4*c] = Color.green;
			greenColor[c] = Color.green;
		}
		handler.setCustomColors(customColors);
		handler.repaint();
		
		//double[] infectionYMarks = {0,500,1000,1500,2000,2500,5000};
		double[] infectionYMarks = {0,500,1000};
		//double[] infectionYMarks = {0, 0.05, 0.1};
		GraphHandler infectionGraphHandler = new GraphHandler(infectionGraph, "infected hosts over time for: " + numGens + " generations");
		infectionGraphHandler.setMarks(xMarks,  infectionYMarks);
		infectionGraphHandler.setCustomColors(greenColor);
		
		//
		//printListDifferences(avgSList);
		GraphHandler avgSGraph = new GraphHandler(avgSList, "average susceptible as a function of biteRate");
		
		
	}
	
	public static void graphOutcomeScatter() {
		//makes scatterplot of wild/mutant population outcomes with small parameter changes
		double[] defaultParameters = Simulator.defaultParameters;
		int numGens = 800;
		int numCopies = 101;
		int totalDays = 365*numGens;
		//
		
		double[] totalPop = new double[numCopies];
		double[] totalMutant = new double[numCopies];
		double[] totalWild = new double[numCopies];
		double[] totalInfected = new double[numCopies];
		
		double[] infM = new double[numCopies]; //% of mutants who are infected
		double[] infW = new double[numCopies]; //wilds
		double[] infH = new double[numCopies]; //all hosts
		double[] infV = new double[numCopies]; //vectors
		

		for(int c = 0; c < numCopies; c++) {
		
		
		Simulator sim = new Simulator(Simulator.defaultStart, defaultParameters);
		adjustParameters(sim, "infectivity", 0.025*c);
		//adjustParameters(sim, "mThrive", 0.2+0.2*c);
		//adjustParameters(sim, "wThrive", 0.2+0.2*c);
		for(int i = 0; i < totalDays; i++) 	sim.simDay();
		
		totalPop[c] = sim.getTotalPop();
		totalMutant[c] = sim.getTotalMutant();
		totalWild[c] = sim.getTotalWild();
		totalInfected[c] = sim.getTotalInfected();
		
		infM[c] = sim.mutantInfected/sim.getTotalMutant();
		infW[c] = sim.wildInfected/sim.getTotalWild();
		infV[c] = sim.vectorInfected/sim.getTotalVector();
		infH[c] = (sim.mutantInfected+sim.wildInfected)/sim.getTotalPop();
		
		
		
		//sim.computeFormulaRV();
		//
		}
		
		//scatter plot setup
		double[] linear = ScatterPlotHandler.linear(numCopies, 2.5);
		double[][] multiX = new double[2][numCopies];
		multiX[0] = linear;  multiX[1] = linear;
		double[][] multiY = new double[2][numCopies];
		multiY[0] = totalMutant; multiY[1] = totalWild;
		ScatterPlotHandler graphOutcome = new ScatterPlotHandler(multiX, multiY, "Number of Hosts after 200 gens as function of infectivity");
		double[] markX = {0,0.5,1,1.5,2,2.5};
		double[] markY = {0,5000,10000, 15000};
		graphOutcome.setMarks(markX, markY);
		
		
		double[] markX2 = {0,0.5,1, 1.5, 2.0};
		
		double[] markY2 = {0, 0.025,0.05,0.075,0.1};
		
		double[][] multiInf = new double[4][numCopies];
		multiX = new double[4][numCopies];
		for(int i = 0; i < multiX.length; i++) multiX[i] = linear;
		multiInf[0] = infH;
		multiInf[1] = infW;
		multiInf[2] = infM;
		multiInf[3] = infV;
		
		ScatterPlotHandler wildInfScatter = new ScatterPlotHandler(multiX, multiInf, "inf as function of infectivity for w/m/h/v " + numGens + " gens");
		wildInfScatter.setMarks(markX2, markY2);
		String[] shapes = {"circle", "square", "diamond", "triangle"};
		wildInfScatter.setShapes(shapes);
		

		
	}
	
	
	public static void r0Estimator() {
		int numDays = 100000;
		
		//double[] start = Simulator.defaultStart;
		//double[] start = {15000,0,0,0,0,0,15000,1}; //1 infected vector, all wild hosts
		double[] start = {0,0,0,15000,0,0,15000,1}; //1 infected vector, all mutant hosts
		double[] parameters = Simulator.defaultParameters;
		Simulator sim = new Simulator(start, parameters);
		
		//sim.applyBiteRates(1, 1.265);
		sim.applyBiteRates(1, 2);
		double computedR = sim.computeCurrentRV();
		
		double infectionsCaused = 0;
		for(int i = 0; i < numDays; i++) {
			double infectionsCausedToday = sim.transmitDayR0Measure();
			//System.out.println("day " + i + ": " + infectionsCausedToday);
			infectionsCaused += infectionsCausedToday;
		}
		System.out.println("total infections: " + infectionsCaused);
		System.out.println("formula R: " + computedR);
		
	}
	
	public static boolean increasingFunctionDetector(double[] data) {
		//detects whether a list of values has portions which are increasing, and prints them
		//not useful
		boolean anyIncrease = false;
		for(int i = 1; i < data.length; i++) {
			if(data[i] > data[i-1]) {
				System.out.print(i + " ");
				anyIncrease = true;
			}
		}
		System.out.println("");
		return anyIncrease;		
	}

	
	public static void halfLifeDetector(double[] data) {
		//reports each time the value in the data gets cut in half
		double recentValue = data[0];
		for(int i = 1; i < data.length; i++) {
			if(data[i] <= recentValue) {
				System.out.print(i + " ");
				recentValue = data[i]/2;
			}
		}
		System.out.println("");
		
	}
	
	public static void printListDifferences(double[] data) {
		System.out.println("list differences");
		for(int i = 1; i < data.length; i++) {
			System.out.println("i " + i + ": " + (data[i-1] - data[i]));
		}
	}
	
	
	public static void nastyQuadraticTester() {
		double numGens = 300;
		Simulator sim = new Simulator(Simulator.defaultStart, Simulator.defaultParameters);
		//sim.wildUninfectedBirth = sim.wildUninfectedBirth * 1.2;
		//sim.mutantUninfectedDeath = sim.mutantUninfectedDeath * 1;
		//sim.mutantInfectedDeath = sim.mutantInfectedDeath * 1;
		sim.applyBiteRates(1,1.15);
		for(int i = 0; i < numGens * 365+364; i++) {
			sim.simDay();
		}
		double estInf = 0;
		int numRepeats = 10;
		for(int i = 0; i < numRepeats; i++) {
			estInf = sim.nastyQuadraticTest(estInf);
		}

	}
	
	public static double wildSweeper(){
		
		double bite = 1;
		double biteInc = 0.1;
		double numDigits = 3;
		for(int i = 0; i< numDigits; i++) {
			boolean overshot = false;
			while(!overshot) {
				Simulator sim = new Simulator(Simulator.defaultStart, Simulator.defaultParameters);
				sim.applyBiteRates(1, bite);
				double predictW = sim.predictW();
				if(predictW < 0 || predictW > 1) overshot = true;
				else bite+= biteInc;
				if(bite > 3) System.out.println("bite > 3 in ActionManager.wildSweeper(), possible endless loop");
			}
			bite -= biteInc;
			biteInc = biteInc/10;			
		}
		System.out.println("wildSweeper predicts equilibrium ends at biteRate: " + bite);
		return bite;
	}
	
	public static void manageFormulaV(double[][][] v, int y, int x, Simulator sim) {
		//assumes initial w is based on default values
		double w0 = sim.defaultStart[4]/sim.carryingCapacity;
		manageFormulaV(v,y,x,sim,w0);
	}
	
	public static void manageFormulaV(double[][][] v, int y, int x, Simulator sim, double w0){
		//measures the formula predictions for wild/mutant/host based on the sim parameters
		double w = sim.predictW(w0);
		double m = 1 - w;
		if(!sim.predictExtinction(w)) {
			v[y][x][0] = 15000*m;
			v[y][x][2] = 15000*w;
		}
		else {
			v[y][x][0] = 0;
			v[y][x][2] = 0;
		}
		
	}
	
	
	
	public static void simplifiedFormulaV(double[][][] v, int y, int x, Simulator sim) {
		//measures the formula predictions for wild/mutant/host based on the sim parameters
		double w = sim.simplifiedPredictW();
		if(w < 0) w = 0;
		if(w > 1) w = 1;
		double m = 1 - w;
		if(!sim.simplifiedExtinction(w)) {
			v[y][x][0] = 15000*m;
			v[y][x][2] = 15000*w;
		}
		else {
			v[y][x][0] = 0;
			v[y][x][2] = 0;
		}
		
		
		
	}
	
	
	public static void survivabilityScan(MainGUI gui, int numGens) {
		//plots outcomes as wThrive and mThrive vary, which scale birth rates up and death rates down
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.001, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;
		double mThriveMin = 0;
		double mThriveMax = 2;
		int numY = 80;
		double mThriveInc = (mThriveMax-mThriveMin)/numY;
					
		double wThriveMin = 0;
		double wThriveMax = 2;
		int numX = 80;
		double wThriveInc = (wThriveMax-wThriveMin)/numX;
					
			
		double[][][] v = new double[numY][numX][3];
		//double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		//double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double mThrive = mThriveMin+mThriveInc*y;
			for(int x = 0; x < numX; x++) {
				double wThrive = wThriveMin+wThriveInc*x;
				double[] parameters = defaultParameters;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				
				//temp to adjust R0
				//sim.wildTransmissionRate = sim.wildTransmissionRate/2;	
				//sim.mutantTransmissionRate = sim.mutantTransmissionRate * 3;
				//
				//sim.wildSusceptible = 1500;
				//sim.mutantSusceptible = 12900;
				//sim.wildInfected = 0;
				//sim.mutantInfected = 100;
				//
				
				
				adjustParameters(sim, "wThrive", wThrive);
				adjustParameters(sim, "mThrive", mThrive);

				sim.wildInfected = 100;
						
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
						
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
						
						//manageFormulaV(formulaV, y, x, sim); //predicts the red/blue values based on formula
						
						
				vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInfVec[y][x] = 3*sim.vectorInfected;

				if(x == 30 && y == 30) {
				gui.sim = sim;
				gui.repaint();
				}
						
			}
		}	
				VarySquare square = new VarySquare(v, "mThrive (y) vs wThrive (x)");
				//VarySquare formulaSquare = new VarySquare(formulaV, "mThrive vs br as predicted by formula");
				
				double slope1 = 0.9; double slope2 = 4.66;
				int[] line1 = {100,900,900,(int)(900-800*slope1)};
				int[] line2 = {100,900,900,(int)(900-800*slope2)};
				square.addExtraLine(line1); square.addExtraLine(line2);
				square.repaint();
				
	}
	
	public static void thriveVsInfectivity(MainGUI gui, int numGens) {
		//plots outcomes as wThrive and mThrive vary, which scale birth rates up and death rates down
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.001, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;
		double bothThriveMin = 0;
		double bothThriveMax = 1;
		int numY = 160;
		double bothThriveInc = (bothThriveMax-bothThriveMin)/numY;
							
		double infectivityMin = 0;
		double infectivityMax = 2;
		int numX = 160;
		double infectivityInc = (infectivityMax-infectivityMin)/numX;
		double[] parameters = defaultParameters;
		//TEMP
		//
		//parameters[3] *= 2; parameters[9] *=2; //infected deaths,  temp for exploration
		//
					
		double[][][] v = new double[numY][numX][3];
		double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		//
		double[][][] vInfW = new double[numY][numX][1];
		double[][][] vInfM = new double[numY][numX][1];	
		//
		//double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double bothThrive = bothThriveMin+bothThriveInc*y;
			for(int x = 0; x < numX; x++) {
				double infectivity = infectivityMin+infectivityInc*x;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				
				sim.wildInfected = 100;
				
				
				//temp to adjust R0
				//sim.wildTransmissionRate = sim.wildTransmissionRate/2;	
				//sim.mutantTransmissionRate = sim.mutantTransmissionRate * 3;
				//
				//sim.wildSusceptible = 1500;
				//sim.mutantSusceptible = 12900;
				//sim.wildInfected = 0;
				//sim.mutantInfected = 100;
				//
						
				
				//
				//sim.wildUninfectedBirth = sim.wildUninfectedBirth*4;
				//sim.wildInfectedDeath *= 10; sim.mutantInfectedDeath *=10;
				//
						
				adjustParameters(sim, "wThrive", bothThrive);
				adjustParameters(sim, "mThrive", bothThrive);
				adjustParameters(sim, "infectivity", infectivity);


								
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
								
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
								
				manageFormulaV(formulaV, y, x, sim); //predicts the red/blue values based on formula
				//simplifiedFormulaV(formulaV, y, x, sim);
								
				//vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInf[y][x] = sim.getTotalInfected()/15000; //raw
				vInfVec[y][x] = 3*sim.vectorInfected;
				vInfW[y][x][0] = sim.wildInfected/sim.getTotalWild();
				vInfM[y][x][0] = sim.mutantInfected/sim.getTotalMutant();

				if(x == 30 && y == 30) {
					gui.sim = sim;
					gui.repaint();
				}
				
				//test mSus prior to new gen
				//for(int i = 0; i < 364; i++) sim.simDay();
				//
				//System.out.println("ActionManager.thriveVsInfectivity peak mUn: " + (sim.mutantSusceptible+sim.mutantRecovered)/sim.getTotalMutant());
				//
				//test deathRate
				boolean birthDeathReport = false;
				if(birthDeathReport) {
				double curMu = sim.getTotalMutant();
				for(int i = 0; i < 364; i++) sim.simDay();
				double muDeath = (curMu - sim.getTotalMutant())/curMu;
				//System.out.println("ActionManager.thriveVsInfectivity actual muDeath: " + muDeath);

				//test birthRate
				double mUn = (sim.mutantSusceptible + sim.mutantRecovered)/sim.getTotalMutant();
				double avMBirth = mUn*sim.mutantUninfectedBirth+(1-mUn)*sim.mutantInfectedBirth;
				double muBirth = (1-muDeath) * 365*avMBirth;
				//System.out.println("ActionManager.thriveVsInfectivity actual muBirth: " + muBirth);
				}
				
			}
		}	
		
		
		
		VarySquare square = new VarySquare(v, "bothThrive (y) vs Infectivity (x)");
		//
		//Color[] custom = {Color.red, Color.green, Color.blue};
		//square.setCustomColors(v, 15000, custom);
		//VarySquare infSquare = new VarySquare(vInf, "infected");
		//Color[] custom = {Color.green};
		//double[][][] vInfHack = new double[vInf.length][vInf[0].length][1];
		//for(int i = 0; i < vInf.length; i++) for(int j = 0; j < vInf[i].length; j++) vInfHack[i][j][0] = vInf[i][j];
		//infSquare.setCustomColors(vInfHack,  0.05, custom);
		//
		VarySquare formulaSquare = new VarySquare(formulaV, "bothThrive vs Infectivity as predicted by formula");
		//VarySquare formulaSquare = new VarySquare(formulaV, "bothThrive vs Infectivity as predicted by simplified formula");

		boolean realismLine = false; //tests which parameters allow uninfectedpopulations to survive
		//only graphs properly if minThrive = 0
		if(realismLine) {
			Simulator sim = new Simulator(Simulator.defaultStart, parameters);
			double wLine = sim.realismLine(false);
			double mLine = sim.realismLine(true);
			int wY = (int)(900-800*(wLine/bothThriveMax));
			int mY = (int)(900-800*(mLine/bothThriveMax));
			int[] wLineCoord = {100, wY,900, wY};
			int[] mLineCoord = {100,mY, 900, mY};
			square.extraLines.add(wLineCoord);
			square.extraLines.add(mLineCoord);
			square.repaint();			
		}
	
		//restricted to subtype, mostly for troubleshooting purposes
		//VarySquare infWSquare = new VarySquare(vInfW, "infected restricted to wild");
		//infWSquare.setCustomColors(vInfW, 0.05, custom);
		//VarySquare infMSquare = new VarySquare(vInfM, "infected restricted to mutant");
		//infMSquare.setCustomColors(vInfM, 0.05, custom);
	
	
	}		
	
	
	
	
	public static void mutantInfectivityVsmutantInitial(MainGUI gui, int numGens) {
		//plots outcomes as vector density and number of initial mutants scales, to investigate phase transition
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.001, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;
		defaultParameters[0] = defaultParameters[0]/2; //cut wildTransmissionRate in half to make Rw < 1
									
		double mInfectivityMin = 0;
		double mInfectivityMax = 2;
		int numY = 80;
		//int numY = 40;
		double mInfectivityInc = (mInfectivityMax-mInfectivityMin)/numY;
		
		double mutantInitialMin = 0;
		double mutantInitialMax = 14000;
		int numX = 70;
		//int numX = 35;
		double mutantInitialInc = (mutantInitialMax-mutantInitialMin)/numX;
							
					
		double[][][] v = new double[numY][numX][3];
		double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		//double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double mInfectivity = mInfectivityMin+mInfectivityInc*y;
			for(int x = 0; x < numX; x++) {
				double mutantInitial = mutantInitialMin+mutantInitialInc*x;
				double w0 = 1 - ((double)mutantInitial)/15000;
				
				double[] parameters = defaultParameters;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				
				sim.mutantInfected = 200;
				
				//sim.mutantTransmissionRate = mInfectivity*sim.mutantTransmissionRate*3;
				//sim.wildTransmissionRate = mInfectivity*sim.wildTransmissionRate;
				sim.mutantTransmissionRate *= 3;
				
				adjustParameters(sim, "infectivity", mInfectivity);
				
				
				sim.mutantSusceptible = mutantInitial - 200;
				if(sim.mutantSusceptible < 0) sim.mutantSusceptible = 0;
				sim.wildSusceptible = 14500 - mutantInitial;
								
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
								
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
								
				manageFormulaV(formulaV, y, x, sim, w0); //predicts the red/blue values based on formula
				//simplifiedFormulaV(formulaV, y, x, sim);
				

								
				vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInfVec[y][x] = 3*sim.vectorInfected;

				if(x == 30 && y == 30) {
					gui.sim = sim;
					gui.repaint();
				}
				
			}
		}	
		VarySquare square = new VarySquare(v, "mInfectivity (y) vs mInitial (x)");
		VarySquare formulaSquare = new VarySquare(formulaV, "mInfectivity (y) vs mInitial (x) as predicted by formula");
		//VarySquare formulaSquare = new VarySquare(formulaV, "mInfectivity (y) vs mInitial (x) as predicted by simplified formula");
	}		
	
	
	public static void wildbiteVsWilddeath(MainGUI gui, int numGens) {
		//plots outcomes as wThrive and mThrive vary, which scale birth rates up and death rates down
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.001, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;

		double wDeathMin = 0;
		double wDeathMax = 0.02;
		int numY = 160;
		double wDeathInc = (wDeathMax-wDeathMin)/numY;
							
		
		double wBiteMin = 0;
		double wBiteMax = 4;
		int numX = 80;
		double wBiteInc = (wBiteMax-wBiteMin)/numX;
		double[] parameters = defaultParameters;

					
		double[][][] v = new double[numY][numX][3];
		double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		//double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double wDeath = wDeathMin+wDeathInc*y;
			for(int x = 0; x < numX; x++) {
				double wBite = wBiteMin+wBiteInc*x;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				
				sim.wildInfected = 100;
				
				

				//adjustParameters(sim, "wThrive", bothThrive);
				//adjustParameters(sim, "mThrive", bothThrive);
				//adjustParameters(sim, "infectivity", infectivity);

				sim.biteRateWild = wBite;
				sim.wildInfectedDeath = wDeath;

								
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
								
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
								
				//manageFormulaV(formulaV, y, x, sim); //predicts the red/blue values based on formula
				simplifiedFormulaV(formulaV, y, x, sim);
								
				vInf[y][x] = 3*sim.getTotalInfected()*sim.carryingCapacity/sim.getTotalPop(); //normalized infected
				vInfVec[y][x] = 3*sim.vectorInfected;

				if(x == 30 && y == 30) {
					gui.sim = sim;
					gui.repaint();
				}
				

	
				}
				
			}
		VarySquare square = new VarySquare(v, "wildbite (y) vs wildInfectedDeath (x)");
		VarySquare formulaSquare = new VarySquare(formulaV, "wildBite vs wildInfectedDeath as predicted by formula");
		}	
	
	
	public static void simpleVectorSIR() {
		//hacks some variables to remove wild population, breeding event, and death rates, so a simple vector SIR model for talk figure
		double[] start = {0,0,0,14900,100,0,15000,0};
		
		Simulator sim = new Simulator(start, Simulator.defaultParameters);
		sim.mutantUninfectedDeath = 0;  sim.mutantInfectedDeath = 0;
		sim.daysPerYear = 365000000; //sufficiently high that breeding events won't occur 
		sim.mutantRecoveryRate = sim.mutantRecoveryRate/2;
		sim.mutantTransmissionRate *= 2;
		sim.vectorTransmissionFromMutant *= 2;
		
		
		int numDays = 4000;
		int numPoints = 400;
		int daysPerPoint = numDays/numPoints;
		
		double[][] multiGraph = new double[4][numPoints];
		double[] totalSus = new double[numPoints];
		double[] totalInf = new double[numPoints];
		double[] totalRec = new double[numPoints];
		double[] infVector = new double[numPoints];
		
		for(int p = 0; p < numPoints; p++) {
			totalSus[p] = sim.mutantSusceptible;
			totalInf[p] = sim.mutantInfected;
			totalRec[p] = sim.mutantRecovered;
			infVector[p] = sim.vectorInfected;
			for(int i = 0; i < daysPerPoint; i++) sim.simDay();
		}
		
		multiGraph[0] = totalSus;
		multiGraph[1] = totalInf;
		multiGraph[2] = totalRec;
		multiGraph[3] = infVector;
		
		
		int[] xMarks = {0,100,200,300,400};
		double[] yMarks = {0,5000,10000,15000.1};
		GraphHandler handler = new GraphHandler(multiGraph, "vector SIR with one host type, no birth/death");
		handler.setMarks(xMarks, yMarks);
		Color[] customColors = {Color.red, Color.green, Color.black, Color.cyan};
		handler.setCustomColors(customColors);
		
		
		
	}
	
	
	
	public static void RwildVsRmut(MainGUI gui, int numGens) {
		//plots outcomes as wildTransmission and mutantTransmission vary
		//double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.001, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		double[] defaultParameters = Simulator.defaultParameters;
		double wTranMin = 0;
		double wTranMax = 4;
		int numY = 40;
		double wTranInc = (wTranMax-wTranMin)/numY;
							
		double mTranMin = 0;
		double mTranMax = 4;
		int numX = 40;
		double mTranInc = (mTranMax-mTranMin)/numX;
		double[] parameters = defaultParameters;
					
		double[][][] v = new double[numY][numX][3];
		double[][][] formulaV = new double[numY][numX][3];
		double[][] vInf = new double[numY][numX];
		double[][] vInfVec = new double[numY][numX];
		//
		double[][][] vInfW = new double[numY][numX][1];
		double[][][] vInfM = new double[numY][numX][1];	
		//
		//double[][] linePoints = new double[2][2];
		for(int y = 0; y < numY; y++) {
			double wTran = wTranMin+wTranInc*y;
			for(int x = 0; x < numX; x++) {
				double mTran = mTranMin+mTranInc*x;
				Simulator sim = new Simulator(Simulator.defaultStart, parameters);
				

				
				sim.wildTransmissionRate *= wTran;
				sim.mutantTransmissionRate *= mTran;


								
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
								
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				//v[y][x][1] = sim.getTotalInfected(); //Green
								
				manageFormulaV(formulaV, y, x, sim); //predicts the red/blue values based on formula
								
				
				vInf[y][x] = sim.getTotalInfected()/15000; //raw
				vInfVec[y][x] = 3*sim.vectorInfected;
				vInfW[y][x][0] = sim.wildInfected/sim.getTotalWild();
				vInfM[y][x][0] = sim.mutantInfected/sim.getTotalMutant();

				if(x == 30 && y == 30) {
					gui.sim = sim;
					gui.repaint();
				}

				boolean birthDeathReport = false;
				if(birthDeathReport) {
				double curMu = sim.getTotalMutant();
				for(int i = 0; i < 364; i++) sim.simDay();
				double muDeath = (curMu - sim.getTotalMutant())/curMu;


				//test birthRate
				//double mUn = (sim.mutantSusceptible + sim.mutantRecovered)/sim.getTotalMutant();
				//double avMBirth = mUn*sim.mutantUninfectedBirth+(1-mUn)*sim.mutantInfectedBirth;
				//double muBirth = (1-muDeath) * 365*avMBirth;
				//System.out.println("ActionManager.thriveVsInfectivity actual muBirth: " + muBirth);
				}
				
			}
		}	
		
		
		
		VarySquare square = new VarySquare(v, "wTransmission (y) vs mTransmission (x)");
		
		//Color[] custom = {Color.white, Color.white, Color.blue};
		//square.setCustomColors(v, 15000, custom);
		
		//
		//Color[] custom = {Color.red, Color.green, Color.blue};
		//square.setCustomColors(v, 15000, custom);
		//VarySquare infSquare = new VarySquare(vInf, "infected");
		//Color[] custom = {Color.green};
		//double[][][] vInfHack = new double[vInf.length][vInf[0].length][1];
		//for(int i = 0; i < vInf.length; i++) for(int j = 0; j < vInf[i].length; j++) vInfHack[i][j][0] = vInf[i][j];
		//infSquare.setCustomColors(vInfHack,  0.05, custom);
		//
		VarySquare formulaSquare = new VarySquare(formulaV, "wTransmission vs mTransmission as predicted by formula");
		//VarySquare formulaSquare = new VarySquare(formulaV, "bothThrive vs Infectivity as predicted by simplified formula");


	
	
	}		
	
	
	
}
