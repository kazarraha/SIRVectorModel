package SIRVectorModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VarySquare extends JPanel {

	
	public JFrame window;
	public Simulator sim;
	
	public double[][] values;
	public String label;
	
	public boolean rgbBoolean = false;
	public double[][][] rgbValues;
	
	public Color mainColor = Color.red;
	
	public Color[][] paintColors; //declare all colors to be displayed so they can be assigned from elsewhere
	
	
	public ArrayList<int[]> extraLines = new ArrayList<int[]>();
	
	
	public VarySquare(double[][] v, String lab){

		window = new JFrame("sub window");
        window.add(this);
        window.setSize(1400, 1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        values = v;
        label = lab;
        repaint();
	}
	
	public VarySquare(double[][][] v, String lab) {
		//runs RGB on the third coordinate
		
		window = new JFrame("sub window");
        window.add(this);
        window.setSize(1400, 1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        rgbValues = v;
        rgbBoolean = true;
        label = lab;
        repaint();
		
	}
	
	public VarySquare(Color[][] c, String lab) {

		window = new JFrame("sub window");
        window.add(this);
        window.setSize(1400, 1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        paintColors = c;
        label = lab;
        repaint();
	}
	
	
	
	public void paint(Graphics g){
		 Graphics2D g2d = (Graphics2D)g;
		 
		 g2d.setColor(Color.white);
		 g2d.fillRect(0, 0, 1800, 1000);
		 
		 if(paintColors != null) {
			 colorPaint(g2d);
			 return;
		 }
		 
		 if(rgbBoolean) {
			 rgbPaint(g2d);
			 return;
		 }

		 
		 int xStart = 100;
		 int yStart = 100;
		 int width = 800;
		 int height = 800;
		 int numX = values[0].length;
		 int numY = values.length;
		 int xW = width/numX;
		 int yW = height/numY;
		 
		 g2d.setColor(Color.black);
		 g2d.drawString(label, xStart+50, yStart-50);
		 
		 double gradCap = 15000; //value that will show up as pure red from the gradient
		 
		 g2d.setColor(Color.black);
		 g2d.drawRect(xStart-1, yStart-1, width+2, height+2);
		 
		 for(int y = 0; y < numY; y++) {
			 int yPoint = yStart+height-(y+1)*yW;
			 for(int x = 0; x < numX; x++) {
				int xPoint = xStart+x*xW; 
				
				Color[] baseColors = {Color.white, mainColor};
				g2d.setColor(checkGradient(values[y][x]/gradCap, baseColors));
				g2d.fillRect(xPoint, yPoint, xW, yW);
			 }
		 }
		 
 
		 
	}
	
	public void rgbPaint(Graphics2D g2d) {
		 int xStart = 100;
		 int yStart = 100;
		 int width = 800;
		 int height = 800;
		 int numX = rgbValues[0].length;
		 int numY = rgbValues.length;
		 int xW = width/numX;
		 int yW = height/numY;
		 
		 g2d.setColor(Color.black);
		 g2d.drawString(label, xStart+50, yStart-50);
		 
		 g2d.drawRect(xStart-1, yStart-1, width+2, height+2);
		 
		 
		 double gradCap = 15000; //value that will show up as pure red from the gradient, possibly carrying capacity
		 
		 for(int y = 0; y < numY; y++) {
			 int yPoint = yStart+height-(y+1)*yW;
			 for(int x = 0; x < numX; x++) {
				int xPoint = xStart+x*xW; 
				boolean normalizePop = false;
				//temp to brighten diagram, normalize by total population
				if(normalizePop) gradCap = rgbValues[y][x][0]+rgbValues[y][x][2];
				
				g2d.setColor(checkrgbGradientV2(rgbValues[y][x], gradCap));
				g2d.fillRect(xPoint, yPoint, xW, yW);
				
			 }
		 }
		 //extra lines on the graph
		 g2d.setColor(Color.black);
		 //System.out.println("extraLines.size(): " + extraLines.size());
		 for(int i = 0; i < extraLines.size(); i++) {
			 int[] linePoints = extraLines.get(i);
			 g2d.drawLine(linePoints[0], linePoints[1], linePoints[2], linePoints[3]);
		 }
		 
		 //System.out.println("x:5 y:60");
		 //g2d.setColor(checkrgbGradientV2(rgbValues[60][5],gradCap));
	}
	
	public void colorPaint(Graphics2D g2d) {
		//paint directly from an array of colors, without doing computations on values
		 int xStart = 100;
		 int yStart = 100;
		 int width = 800;
		 int height = 800;
		 int numX = paintColors[0].length;
		 int numY = paintColors.length;
		 int xW = width/numX;
		 int yW = height/numY;
		 
		 g2d.setColor(Color.black);
		 g2d.drawString(label, xStart+50, yStart-50);
		 
		 //double gradCap = 15000; //value that will show up as pure red from the gradient, possibly carrying capacity
		 
		 for(int y = 0; y < numY; y++) {
			 int yPoint = yStart+height-(y+1)*yW;
			 for(int x = 0; x < numX; x++) {
				int xPoint = xStart+x*xW; 
				g2d.setColor(paintColors[y][x]);
				g2d.fillRect(xPoint, yPoint, xW, yW);
				
			 }
		 }
		 
	}
	
	public static Color checkGradient(double proportion){
		//alter to be more general if needed
		 Color[] baseValues = {Color.white, Color.red};	 
		 int index = 0;
		 
		 if(proportion< 0) proportion = 0;
		 if(proportion > 1) proportion = 1;
		 

		 int red = (int)((1-proportion)*baseValues[index].getRed() + (proportion)*baseValues[index+1].getRed());
		 int green = (int)((1-proportion)*baseValues[index].getGreen() + (proportion)*baseValues[index+1].getGreen());
		 int blue = (int)((1-proportion)*baseValues[index].getBlue() + (proportion)*baseValues[index+1].getBlue());
		 
		 Color chosen = new Color(red,green,blue);
		 return chosen;
	 }
	
	public static Color checkGradient(double proportion, Color[] baseValues) {
		//alter to be more general if needed
		 int index = 0;
		 
		 if(proportion< 0) proportion = 0;
		 if(proportion > 1) proportion = 1;
		 

		 int red = (int)((1-proportion)*baseValues[index].getRed() + (proportion)*baseValues[index+1].getRed());
		 int green = (int)((1-proportion)*baseValues[index].getGreen() + (proportion)*baseValues[index+1].getGreen());
		 int blue = (int)((1-proportion)*baseValues[index].getBlue() + (proportion)*baseValues[index+1].getBlue());
		 
		 Color chosen = new Color(red,green,blue);
		 return chosen;
	}
	
	public static Color checkrgbGradient(double[] raw, double max) {		
		
		double[] proportions = new double[3];
		for(int i = 0; i < 3; i++) {
			proportions[i] = raw[i]/max;
			if(proportions[i] > 1) proportions[i] = 1;
			if(proportions[i] < 0) proportions[i] = 0;
		}
		


		
		
		int red = (int) (255 * proportions[0]);
		int green = (int) (255 * proportions[1]);
		int blue = (int) (255 * proportions[2]);
		
		Color chosen = new Color(red,green,blue);
		return chosen;
	}
	
	public static Color checkrgbGradientV2(double[] raw, double max) {
		//represents dead populations using white
		double[] proportions = new double[3];
		proportions[0] = raw[0]/max;
		proportions[2] = raw[2]/max;
		proportions[1] = (max - raw[0]-raw[2])/max; //empty space below carrying capacity
		//System.out.println("max: " + max + " mut: " + raw[0] + " wild: " + raw[2]);
		//System.out.println("empty: " + proportions[1]);
		
		//quality control
		for(int i = 0; i < proportions.length; i++) {
			if(proportions[i] < 0) { proportions[i] = 0; }
			if(proportions[i] > 1) { proportions[i] = 1; }
		}
		
		//red for mutant, blue for wild, white for empty
		int red = (int) (255 * (proportions[0]+proportions[1]));
		int green = (int) (255 * proportions[1]);
		if(green < 0) green = 0;
		int blue = (int) (255 * (proportions[2]+proportions[1]));
		Color chosen = new Color(red,green,blue);
		return chosen;		
	}
	

	
	
	public static void testSquare() {
		
		//double[][] v = new double [10][10];
		double[][][] v = new double[10][10][3];
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				v[y][x][0] = 2000*x+8000*y;
				v[y][x][1] = 10000-2000*x;
			}
		}
		
		VarySquare test = new VarySquare(v, "testSquare");
		
	}
	
	public static void overlay(ArrayList<double[][][]> set) {
		//takes a set of varySquare images and averages them (which is equivalent to overlaying them with transparency)
		double gradCap = 15000;
		int numMaps = set.size();
		double[][][] rgbSum = new double[set.get(0).length][set.get(0)[0].length][3];
		for(int m = 0; m < numMaps; m++) {
			for(int y = 0; y < set.get(m).length; y++) {
				for(int x = 0; x < set.get(m)[y].length; x++) {
					Color c = checkrgbGradientV2(set.get(m)[y][x],gradCap);
					rgbSum[y][x][0] += c.getRed()/numMaps;
					rgbSum[y][x][1] += c.getGreen()/numMaps;
					rgbSum[y][x][2] += c.getBlue()/numMaps;
				}
			}
		}
		Color[][] avgColors = new Color[rgbSum.length][rgbSum[0].length];
		for(int y = 0; y < rgbSum.length; y++) {
			for(int x = 0; x < rgbSum[y].length; x++) {
				Color c = new Color((int)rgbSum[y][x][0],(int)rgbSum[y][x][1], (int)rgbSum[y][x][2]);
				avgColors[y][x] = c;
			}
		}
		VarySquare square = new VarySquare(avgColors, "Multiple graphs overlayed");
		
	}
	
	public void addExtraLine(int[] linepoints) {
		extraLines.add(linepoints);
	}
	
	
	
	public static void keySquare() {
	
		int numSlots = 800;
		Color[][] slots = new Color[numSlots][numSlots];
		for(int y = 0; y < numSlots; y++) {
			for(int x = 0; x < numSlots; x++) {
				double tot = (double)y/numSlots;
				double wf = (double)x/numSlots;
				double w = wf*tot; double m = (1-wf)*tot;
				double[] raw = {w,0,m};
				//int r = (int)(255*m);
				//int b = (int)(255*w);
				//int g = 0;
				//if(r < 0 || r > 255 || b < 0 || b > 255) System.out.println("rgb value out of bounds in VarySquare.keySquare.  r: " + r + " g: " + g + " b: " + b);
				//slots[y][x] = new Color(r,g,b);
				slots[y][x] = checkrgbGradientV2(raw, 1);
			}
		}
		VarySquare key = new VarySquare(slots, "keySquare");
	}
	
	public void setCustomColors(double[][][] raw, double max, Color[] colors) {
		//runs customColorGradient and saves it.  normal color gradient v2 should be equivalent to this with {Red,Blue} passed in
		paintColors = new Color[raw.length][raw[0].length];
		for(int i = 0; i < paintColors.length; i++) {
			for(int j = 0; j < paintColors[i].length; j++) {
				paintColors[i][j] = customColorGradient(raw[i][j], max, colors);
			}
		}
	}
	
	public static Color customColorGradient(double[] raw, double max, Color[] colors) {		
		
		double[] proportions = new double[raw.length];
		double red = 0;
		double green = 0;
		double blue = 0;
		double emptySpace = 1;
		for(int i = 0; i < proportions.length; i++) {
			proportions[i] = raw[i]/max;
			if(proportions[i] > 1) proportions[i] = 1;
			if(proportions[i] < 0) proportions[i] = 0;
			red += colors[i].getRed()*proportions[i];
			green += colors[i].getGreen()*proportions[i];
			blue += colors[i].getBlue()*proportions[i];
			emptySpace -= proportions[i];
		}
		if(emptySpace < 0) emptySpace = 0;
		red += 255*emptySpace; green += 255*emptySpace; blue += 255*emptySpace;


		
		Color chosen = new Color((int)red,(int)green,(int)blue);
		return chosen;
	}
	
	
}
