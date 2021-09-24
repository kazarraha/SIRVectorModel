package SIRVectorModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GraphHandler extends JPanel {

	
	public JFrame window;
	public double[] graphList;
	public String label;
	public boolean logScaleX = false;

	double[][] multipleGraphList;
	boolean multiple = false;
	
	boolean thickLines = true;
	
	public int[] markX;
	public double[] markY;
	public boolean useMarks = false;
	
	public Color[] customColors;
	
	
	
	public GraphHandler(double[] gL, String lab){

		window = new JFrame("sub window");
        window.add(this);
        window.setSize(1400, 1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        graphList = gL;
        label = lab;
        repaint();
	}
	
	public GraphHandler(double[][] multipleGraphs, String lab){
        multiple = true;
		window = new JFrame("sub window");
        window.add(this);
        window.setSize(1400, 1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        multipleGraphList = multipleGraphs;

        //set to avoid crashing...?
        graphList = multipleGraphs[0];
        
        
        label = lab;
        repaint();
	}
	
	
	public void setMarks(int[] xM, double[] yM) {
		markX = xM;
		markY = yM;
		useMarks = true;
		repaint();		
	}
	
	public void paint(Graphics g){
		if(multiple){
			multiplePaint(g);
			return;
		}
		//
		//System.out.println("got past return statement in GraphHandler.paint, multiple boolean is false");
		//
		 Graphics2D g2d = (Graphics2D)g;
		 int top = 100;
		 int left = 100;
		 int height = 800;
		 int length = 1200;
		 
		 //find bounds
		 double yMax = -10000;
		 for(int i = 0; i < graphList.length; i++) if(graphList[i] > yMax) yMax = graphList[i];
	     
		 
		 int xMax = graphList.length;
		 
		 //boolean figureSettings = true;
		 
		 //TEMP
		 //yMax = 0.03;
		 //if(figureSettings) {
		 //yMax = 0.5;
		 //}
		 //
		 
		 if(useMarks){
			 yMax = markY[markY.length-1];
			 xMax = markX[markX.length-1];
		 }
		 
		 
		 
		 double yInc = (double)height / yMax; //number of pixels per point in the y direction
		 //if(zoom) yInc = 400;
		 int xInc;
		 if(! logScaleX) xInc = length / graphList.length; //number of pixels per x point
		 else xInc = (int) ((double)length / Math.log(graphList.length + 1));
		 
		  
		 boolean whitebackground = true;
		 if(whitebackground){ g2d.setColor(Color.white); g2d.fillRect(0,0,2*left+length,2*top+height); }
		 
		 g2d.setColor(Color.black);
		 DecimalFormat df = new DecimalFormat("#.00");
		 
		 Font smallFont = g2d.getFont();
		 Font largeFont = smallFont.deriveFont(smallFont.getSize() * 2F);
		 g2d.setFont(largeFont);
		 
		 g2d.drawString(label, left+length/2,top/2);
		 //g2d.drawString(df.format(yMax), left-50, top);
		 
		 g2d.drawLine(left,top,left, top+height); //vertical line at left
		 g2d.drawLine(left,top+height, left+length, top+height); //horizontal line at bottom
		 
		 int numXTics = 8;
		 int numYTics = 10;
		 
		 int xTicInterval = length/numXTics;
		 int yTicInterval = height/numYTics;
		 int ticLength = 2;
		 int xTicSpot = left;
		 
		 //int xMax = 16;
		 
		 
		 
		 for(int i = 0; i < numXTics+1; i++){
			 xTicSpot = left+((i*length)/numXTics);
			 g2d.drawLine(xTicSpot, top + height, xTicSpot, top + height - ticLength);
			 g2d.drawString(Integer.toString((xTicSpot-left)*xMax/length), xTicSpot, top+height+30);	 
			 //xTicSpot += xTicInterval;
		 }
		 
		 int yTicSpot = top + height;
		 while(yTicSpot >= top){
			 g2d.drawLine(left, yTicSpot, left + ticLength, yTicSpot);
			 double yval = ((double)height + top - yTicSpot)/height * yMax;
			 g2d.drawString(df.format(yval), left-50, yTicSpot+5);
			 yTicSpot -= yTicInterval;
		 }
		 for(int i = 0; i < graphList.length; i++){
			 int yPosition = (int) (top + height - yInc*graphList[i]);  
			 int xPosition;
			 if(!logScaleX) xPosition = left + xInc*i;
			 else xPosition = left + (int)(xInc * Math.log(i+1));
			 if(i > 0){ //connector line
				 int oldY = (int) (top + height - yInc*graphList[i-1]);  
				 int oldX;
				 if(!logScaleX) oldX = left + xInc*(i-1);
				 else oldX = left + (int)(xInc * Math.log(i));
				 if(thickLines) drawThickLine(g2d, oldX, oldY, xPosition, yPosition);	//Draw lines here
				 else g2d.drawLine(oldX, oldY, xPosition, yPosition);
				 //
				 //System.out.println("x: " + i + "   y: " + graphList[i]);
				 //
			 }
		 }
		 g2d.setFont(smallFont);
	}
	
	
	
	
	public static double[] logTransform(double[] realData){
		//requires realData to only have points >= 0
		double[] logData = new double[realData.length];
		for(int i = 0; i < realData.length; i++) logData[i] = Math.log(realData[i]+1);
		return logData;
		
	}
	
	public void multiplePaint(Graphics g){
		 boolean thickLines = true;
		 //boolean thickLines = false;
		 Graphics2D g2d = (Graphics2D)g;
		 int top = 100;
		 int left = 100;
		 int height = 800;
		 int length = 1200;
		 
		 g2d.setColor(Color.white);
		 g2d.fillRect(0, 0, 2000, 2000);
		 //find bounds
		 double yMax = -10000;
		 for(int graphIndex = 0; graphIndex < multipleGraphList.length; graphIndex++){
		 graphList = multipleGraphList[graphIndex];
		
		
		 for(int i = 0; i < graphList.length; i++) if(graphList[i] > yMax) yMax = graphList[i];
		 }
		 
		 double xMax = multipleGraphList[0].length;
		 if(useMarks){
			 yMax = markY[markY.length-1];
			 xMax = markX[markX.length-1];
		 }
		 
		 
		 double yInc = (double)height / yMax; //number of pixels per point in the y direction
		 //if(zoom) yInc = 400;
		 int xInc;
		 if(! logScaleX) xInc = length / graphList.length; //number of pixels per x point
		 else xInc = (int) ((double)length / Math.log(graphList.length + 1));
		 
		 g2d.setColor(Color.black);
		 DecimalFormat df = new DecimalFormat("#.000");
		 
		 g2d.drawString(label, left+length/2,top/2);
		 //g2d.drawString(df.format(yMax), left-50, top);
		 
		 g2d.drawLine(left,top,left, top+height); //vertical line at left
		 g2d.drawLine(left,top+height, left+length, top+height); //horizontal line at bottom	
		 
		 int ticLength = 4;
		 //autotic
		 boolean autoTic = false;
		 if(autoTic) {
		 int xTicInterval = 10 * xInc;
		 if(xTicInterval < 1) xTicInterval = 1; //to prevent infinite loops
		 int yTicInterval = (int)(yInc);
		 if(yTicInterval < 1) yTicInterval = 1; //ditto
		 if(yInc > height) yTicInterval = (int)(yInc/100);
		 int xTicSpot = left;
		 while(xTicSpot <= left+length){
			 g2d.drawLine(xTicSpot, top + height, xTicSpot, top + height - ticLength);
			 xTicSpot += xTicInterval;
		 }
		 
		 int yTicSpot = top + height;
		 while(yTicSpot >= top){
			 g2d.drawLine(left, yTicSpot, left + ticLength, yTicSpot);
			 yTicSpot -= yTicInterval;
		 }
		 }
		 //minx and miny assumed 0.  fix if this is no longer true
		 //string values assume integer
		 //marktic
		 
		 Font smallFont = g2d.getFont();
		 Font largeFont = smallFont.deriveFont(smallFont.getSize() * 2F);
		 g2d.setFont(largeFont);
		 g2d.setColor(Color.black);
		 if(markX != null) {
			 int offset = 20;
			 for(int i = 0; i < markX.length; i++) {
				 int xSpot = left - offset + (int)(xInc * (markX[i]-0));
				 g2d.drawString(Integer.toString(markX[i]), xSpot, top+height+30);
				 g2d.drawLine(xSpot+offset, top+height, xSpot+offset, top+height-ticLength);
			 } 
		 }
		 if(markY != null) {
			 int offset = 80;
			 for(int i = 0; i < markY.length; i++) {
				 int ySpot = top + height - (int)(yInc * (markY[i]-0));
				 g2d.drawString(Integer.toString((int) markY[i]), left-offset , ySpot);
				 g2d.drawLine(left, ySpot, left+ticLength, ySpot);
			 } 
		 }
		 
		 g2d.setFont(smallFont);
		 
		 
		 //actual graph Lines
		 for(int graphIndex = 0; graphIndex < multipleGraphList.length; graphIndex++){
	     graphList = multipleGraphList[graphIndex];
	     if(customColors != null && customColors.length > graphIndex) g2d.setColor(customColors[graphIndex]);
	     else {
	    	 g2d.setColor(new Color(255-graphIndex*25, 0, graphIndex*25));
	    	 if(graphIndex == multipleGraphList.length-1) g2d.setColor(Color.green);
	     }
	     
		 for(int i = 0; i < graphList.length; i++){
			 int yPosition = (int) (top + height - yInc*graphList[i]);  
			 int xPosition;
			 if(!logScaleX) xPosition = left + xInc*i;
			 else xPosition = left + (int)(xInc * Math.log(i+1));
			 if(i > 0){ //connector line
				 int oldY = (int) (top + height - yInc*graphList[i-1]);  
				 int oldX;
				 if(!logScaleX) oldX = left + xInc*(i-1);
				 else oldX = left + (int)(xInc * Math.log(i));
				 
				 if(!thickLines)g2d.drawLine(oldX, oldY, xPosition, yPosition);
				 else drawThickLine(g2d, oldX, oldY, xPosition, yPosition);
			 }
			 if(i == graphList.length-1) g2d.drawString("" + graphIndex, xPosition+50, yPosition);
		 }
		 
		 }
	}
	
	
	
	public static void drawThickLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		//draws lines near the desired point in an attempt to make a line 2 pixels wide
		
		g2d.drawLine(x1, y1, x2, y2);
		g2d.drawLine(x1+1, y1, x2+1, y2);
		g2d.drawLine(x1, y1+1, x2, y2+1);
		
	}
	
	public void setCustomColors(Color[] custom) {
		customColors = custom;
		repaint();
	}
	
	
}
