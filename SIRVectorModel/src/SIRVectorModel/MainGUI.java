package SIRVectorModel;



/*
This project was written exclusively by Matthew J Young for research purposes.
It is not intended for or designed for general use, but if you want to copy and adapt it
for your own research, you may do so with proper credit/citation.

This simulates an SIR model for a vector-driven disease in a population with two host types.
Wild hosts (type 1) are vulnerable to the disease, but breed faster in its absence.
Mutant hosts (type 2) are more resistant/tolerant to the disease, at the cost of their breeding rate.
Two papers are currently in progress using this model


Comments are sparse, which I know is a bad habit but was easier in the moment.
The MainGUI class runs the main method and sets things up, and the display window.
The Simulator class runs the actual SIR model, remembering the population states, the dynamics
and simulates the daily and yearly updates.
The ActionManager class contains static methods, each of which enacts a specific scenario of events.
which usually end up generating a figure at the end.
The other classes do useful generic stuff like converting data into figures.

Almost all of the variables and methods are hard-coded, rather than having input fields when the code is run.
The set of events I want to occur are written step-by-step into an ActionManager method, then that method is
stuck into the ActionButton in MainGUI, and the old methods are commented out, so when the program is executed
pressing the Action Button will do the useful thing.  If you wish to run things yourself, find the relevant section
(search for "actionpaction"), set the corresponding boolean variable to TRUE and all others to FALSE,
and then run the program and press the action button.  Specific methods may need to be commented/uncommented

Figures used in the papers can be reproduced with the exact parameters used for those figures in the FigureArchive
class.  These can be run by selecting them using the above Action Button




*/



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainGUI extends JPanel {
	//handles the main window and buttons
	
	public JFrame simFrame;
	public MainGUI myself;
	public Simulator sim;
	
	public JPanel runSimPanel;
	public JButton runSimButton;
	public int runSimTimes;
	public JTextField runSimTimesField;
	
	public JPanel resetPanel;
	public JButton resetButton;
	
	public JPanel actionPanel;
	public JButton actionButton;

	
public static void main(String[] args){  //main
		
		MainGUI mg = new MainGUI();
		
		
		
		 JFrame frame = new JFrame("Main Simulation");
	        frame.add(mg);
	        frame.setSize(1400, 1000);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        mg.simFrame = frame;
	        
		
	}



	public MainGUI() {
		myself = this;
		
		sim = new Simulator();
		//temp
		//sim.wildInfectedDeath = 0;
		//sim.wildUninfectedDeath = 0;
		//sim.mutantInfectedDeath = 0;
		//sim.mutantUninfectedDeath = 0;
		//
		
		
		runSimPanel = new JPanel(new GridLayout(2,1));
		runSimButton = new JButton("Run Simulation");
		runSimButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			for(int i = 0; i < runSimTimes; i++){	
				sim.simDay();
			}
			repaint();
			//refreshReportingPanel();
		  }
		});
			
		runSimTimes = 1;
		runSimTimesField = new JTextField("1");
		runSimTimesField.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    runSimTimes = Integer.parseInt(runSimTimesField.getText());
		  }
		});	
		runSimPanel.add(runSimButton);
		runSimPanel.add(runSimTimesField);
		this.add(runSimPanel);
		//end runSim structure
		
		
		
		
		resetPanel = new JPanel(new GridLayout(1,1));
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			sim = new Simulator();
			repaint();
		  }
		});
			
		resetPanel.add(resetButton);
		this.add(resetPanel);

		
		//TODO actionpaction
		actionPanel = new JPanel(new GridLayout(1,1));
		actionButton = new JButton("Action");
		actionButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			
			boolean squareStuff = false;
			boolean parameterStuff = false;
			boolean graphStuff = false;
			boolean figureArchive = true;
			
			if(squareStuff) {
				//VarySquare.testSquare();
				//ActionManager.infectedDeathVSmutantRecovery(myself);
				//ActionManager.infectedDeathVSmutantRecoveryRGB(myself);
				//ActionManager.mutantDeathVsMutantBite(myself);
				//ActionManager.mutationStrengthVsMutantBite(myself);
				//ActionManager.findSlopesAsGensVary(myself);
				//ActionManager.mutationStrengthVsMutantBiteOverlay();
				//ActionManager.survivabilityScan(myself, 80);
				//ActionManager.thriveVsInfectivity(myself, 80);           		 //main varySquare
				//ActionManager.mutantInfectivityVsmutantInitial(myself, 80);	//initial condition
				//VarySquare.keySquare();
				//ActionManager.wildbiteVsWilddeath(myself, 640);
				
				 ActionManager.RwildVsRmut(myself, 1000);
			}
			  
			if(parameterStuff) {
				//ActionManager.applyBiteStuff(sim);
				//sim.applyMutationStrength(0.1);
				//ActionManager.nastyQuadraticTester();
				ActionManager.wildSweeper();
			}
			if(graphStuff) {
				//ActionManager.graphInfectionOverTime();
				//ActionManager.graphInfectionOverTimeMulti();
				//ActionManager.r0Estimator();
				ActionManager.graphOutcomeScatter();
				//ActionManager.simpleVectorSIR();
			}
			if(figureArchive) {
				//FigureArchive.bioFigure1();
				//FigureArchive.bioFigure2();
				//FigureArchive.bioFigure34();
				//FigureArchive.bioFigure5();
				//FigureArchive.mathFigure1();
				//FigureArchive.mathFigure2();
				//FigureArchive.mathFigure3();
				//FigureArchive.mathFigure45();
				FigureArchive.mathFigure6();
				
				

			}
			
			  
			  
			repaint();
		  }
		});
			
		actionPanel.add(actionButton);
		this.add(actionPanel);

		
		
	}
	
	public void paint(Graphics g) {
	    this.getRootPane().setBackground(Color.WHITE);
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        boolean statusPaint = true;
        int xLoc = 0;
        int yLoc = 100;
        if(statusPaint) paintStatus(g2d, xLoc, yLoc);
	}
	
	public void paintStatus(Graphics2D g2d, int xLoc, int yLoc) {
		int width = 1000;
		int height = 600;
		
		g2d.setColor(Color.white);
		g2d.fillRect(xLoc, yLoc, width, height);
		
		int xGap = 50;
		int xWide = 20;
		int yFloor = yLoc + height - 100;
		double yScale = 400.0/15000;
		//double yScale = 400;
		double[] stuff = getPaintArray();
		String[] labels = {"Wild", "Sw", "Iw", "Rw", "Mutant", "Sm", "Im", "Rm", "Sv", "Iv"};
		for(int i = 0; i < stuff.length; i++) {
			if(i==0 || i==4) g2d.setColor(Color.orange); else 	g2d.setColor(Color.red);
			int x = xLoc + xGap*(i+1);
			int yHeight = (int) (stuff[i]*yScale);
			g2d.fillRect(x, yFloor-yHeight, xWide, yHeight);
			g2d.setColor(Color.black);
			g2d.drawString(labels[i], x+1, yFloor+13);
		}
		
	}
	
	public double[] getPaintArray() {
		//inserts population totals for painting
		double[] pop = sim.getPopArray();
		double[] stuff = new double[pop.length+2];
		stuff[0] = pop[0]+pop[1]+pop[2];
		stuff[4] = pop[3]+pop[4]+pop[5];
		for(int i = 0; i < 3; i++) {
			stuff[i+1] = pop[i];
			stuff[i+5] = pop[i+3];
		}
		stuff[8] = pop[6];
		stuff[9] = pop[7];
		return stuff;
	}
	
	
	
}
