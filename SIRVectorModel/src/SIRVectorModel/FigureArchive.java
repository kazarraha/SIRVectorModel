package SIRVectorModel;

import java.awt.Color;

public class FigureArchive {

	/*This class exists as a repository for the exact details and settings required to produce
	 * the figures in the papers, so they can be referred to and remade, or adjusted to adjust the figures.
	 * The corresponding methods in ActionManager are frequently changed to experiment with
	 * different parameters or display settings, so these have been copy/pasted from there
	 * in order to preserve the settings used in figures.
	 * These should not be changed unless the corresponding figure in the submitted paper changes
	 * 
	 * 
	 * 
	 */
	
	
	
	
	public static void bioFigure1() {
		//tracks the total population and number of infected over time
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.0011, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;
		int numStartGens = 0;
		int numGens = 40;
		int numPoints = 200;
		int daysPerPoint = 365*numGens/numPoints;
		
		double[][] multiGraph = new double[4][numPoints];
		double[] totalPop = new double[numPoints];
		double[] totalMutant = new double[numPoints];
		double[] totalWild = new double[numPoints];
		double[] totalInfected = new double[numPoints];
		
		double[] defaultStart = {13000,0,0,1300,200,0,14000,0};
		
		Simulator sim = new Simulator(defaultStart, defaultParameters);
		
		
		
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
	
	
	public static void bioFigure2() {
		//tracks the total population and number of infected over time
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.0011, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		//double[] defaultParameters = Simulator.defaultParameters;
		int numStartGens = 0;
		int numGens = 200;
		int numPoints = 200;
		int daysPerPoint = 365*numGens/numPoints;
		
		double[][] multiGraph = new double[3][numPoints];
		double[][] hackMulti = new double[2][numPoints]; //leaves 2nd blank so I can use GraphHandler.multi
		double[] totalPop = new double[numPoints];
		double[] totalMutant = new double[numPoints];
		double[] totalWild = new double[numPoints];
		double[] totalInfected = new double[numPoints];
		
		double[] defaultStart = {13000,0,0,1300,200,0,14000,0};
		
		Simulator sim = new Simulator(defaultStart, defaultParameters);
		
		
		
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
		hackMulti[0] = totalInfected;
		int[] xMarks = {0,50,100,150,200};
		double[] yMarks = {0,5000,10000,15000.001};
		GraphHandler handler = new GraphHandler(multiGraph, "total pop and over time for: " + numGens + " generations");
		handler.setMarks(xMarks, yMarks);
		Color[] customColors = {Color.magenta, Color.red, Color.blue};
		handler.setCustomColors(customColors);
		
		GraphHandler infHandler = new GraphHandler(hackMulti, "total infected over time for: " + numGens + " generations");
		double[] infMarks = {0,500,1000,1500,2000,2500,5000};
		infHandler.setMarks(xMarks, infMarks);
		Color[] customColors2 = {Color.green, Color.black};
		infHandler.setCustomColors(customColors2);
		
		
		
	}
	
	
	
	public static void bioFigure3() {
		//makes scatterplot of wild/mutant population outcomes with small parameter changes  (Figure 3)
		//method creates both figure 3 and 4, since they display different data from the same simulations
		double[] defaultParameters = {0.008, 0, 0.001, 0.0025, 0.002, 0.0003, 0.005, 0.003, 0.001, 0.0011, 0.0018, 0.0014, 0.01, 0.02, 15000,1,1,1};	
		int numGens = 800;
		int numCopies = 101;
		int totalDays = 365*numGens;
			
		double[] totalPop = new double[numCopies];
		double[] totalMutant = new double[numCopies];
		double[] totalWild = new double[numCopies];
		double[] totalInfected = new double[numCopies];
			
		double[] infM = new double[numCopies]; //% of mutants who are infected
		double[] infW = new double[numCopies]; //wilds
		double[] infH = new double[numCopies]; //all hosts
		double[] infV = new double[numCopies]; //vectors

		for(int c = 0; c < numCopies; c++) {
	
		double[] defaultStart = {13000,0,0,1300,200,0,14000,0};
		Simulator sim = new Simulator(defaultStart, defaultParameters);
		ActionManager.adjustParameters(sim, "infectivity", 0.025*c);
		for(int i = 0; i < totalDays; i++) 	sim.simDay();
			
		totalPop[c] = sim.getTotalPop();
		totalMutant[c] = sim.getTotalMutant();
		totalWild[c] = sim.getTotalWild();
		totalInfected[c] = sim.getTotalInfected();
			
		infM[c] = sim.mutantInfected/sim.getTotalMutant();
		infW[c] = sim.wildInfected/sim.getTotalWild();
		infV[c] = sim.vectorInfected/sim.getTotalVector();
		infH[c] = (sim.mutantInfected+sim.wildInfected)/sim.getTotalPop();

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

	public static void bioFigure4() {
		//method creates both figure 3 and 4, since they display different data from the same simulations
		bioFigure3();
	}
	
	
	public static void bioFigure5() {
		int numGens = 80;
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
				double[] defaultStart = {13000,0,0,1300,200,0,14000,0};
				Simulator sim = new Simulator(defaultStart, parameters);
				
				
				ActionManager.adjustParameters(sim, "wThrive", bothThrive);
				ActionManager.adjustParameters(sim, "mThrive", bothThrive);
				ActionManager.adjustParameters(sim, "infectivity", infectivity);


								
				for(int i = 0; i < 365*numGens; i++) sim.simDay();
				
								
				v[y][x][0] = sim.getTotalMutant(); //Red
				v[y][x][2] = sim.getTotalWild(); //Blue
				v[y][x][1] = sim.getTotalInfected(); //Green
								
								
				vInf[y][x] = sim.getTotalInfected()/15000; //raw
				vInfVec[y][x] = 3*sim.vectorInfected;
				vInfW[y][x][0] = sim.wildInfected/sim.getTotalWild();
				vInfM[y][x][0] = sim.mutantInfected/sim.getTotalMutant();

				
			}
		}	

		VarySquare square = new VarySquare(v, "bothThrive (y) vs Infectivity (x)");

	

	
	}		
	
}
