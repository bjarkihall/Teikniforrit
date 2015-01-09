/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package teikniforrit;

import java.awt.*;

public class DrawingShape {
    //Hnit forms og þvermál þess.
    private int xPos;
    private int yPos;
    private int diam;
    private int width;
    private int height;
    //Notað til að segja til um hvort form eigi að hafa fyllingu og/eða ramma.
    private boolean fill;
    private boolean border;
    
    //Smiður DrawingShape, sem eru formin sem hægt er að teikna.
    public DrawingShape(){
        this.fill = true;
        this.border = true;
    }
    
    //Teikna ferning af staðlaðri stærð, einn punktur.
    public Graphics2D paintRect(Graphics2D g,Color c, int penSize, int state_L){
        setShapeSize(penSize);
        if(state_L == MainFrame.eraser)
            g.setColor(Color.white);
        if(fill||state_L == MainFrame.eraser)
            g.fillRect(xPos,yPos,diam,diam);
        if(border && (state_L != MainFrame.eraser))
            g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,diam,diam); 
        g.setColor(c);
        return g;
    }
    
    //Teikna ferning útfrá tveimur gefnum punktum.
    public Graphics2D paintRect(Graphics2D g, Color c, Rectangle area){
        //Færa hliðrun til baka.
        xPos = xPos +(diam/2);
        yPos = yPos +(diam/2);
        
        if(fill)
            g.fillRect(xPos,yPos,width,height);
        if(border)
            g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,width,height); 
        g.setColor(c);
        
        return g;
    }
        
    //Teikna sporbaug (báðar aðferðir eins og á ferhyrningi).
    
    public Graphics2D paintCircle(Graphics2D g,Color c, int penSize){
        setShapeSize(penSize);
        if(fill)
            g.fillOval(xPos,yPos,diam,diam);
        if(border)
            g.setColor(Color.BLACK);
        g.drawOval(xPos,yPos,diam,diam); 
        g.setColor(c);
        return g;
    }
    
    public Graphics2D paintCircle(Graphics2D g, Color c, Rectangle area){
        xPos = xPos +(diam/2);
        yPos = yPos +(diam/2);
        setShapeWidth((int)area.getWidth());
        setShapeHeight((int)area.getHeight());
        if(fill)
            g.fillOval(xPos,yPos,width,height);
        if(border)
            g.setColor(Color.BLACK);
        g.drawOval(xPos,yPos,width,height); 
        g.setColor(c);
        
        return g;
    }
    
    //Teikna þríhyrning, tvær aðferðir eins og á hinum formunum.
    
    public Graphics2D paintTriangle(Graphics2D g, Color c, int penSize){
        setShapeSize(penSize);
        int nPoints = 3;
        int diff = (diam-(int)((Math.sqrt(3)/2)*diam))/2;
        int[] xPoints = {xPos,xPos+diam,xPos+(diam/2)};
        int[] yPoints = {yPos+diam-diff,yPos+diam-diff,yPos+diam-(int)((Math.sqrt(3)/2)*diam)-diff};
        if(fill)
            g.fillPolygon(xPoints, yPoints, nPoints);
        if(border)
            g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, nPoints);
        g.setColor(c);
        return g;
    }    

    public Graphics2D paintTriangle(Graphics2D g, Color c, Rectangle area){
        xPos = xPos +(diam/2);
        yPos = yPos +(diam/2);
        setShapeWidth((int)area.getWidth());
        setShapeHeight((int)area.getHeight());
        
        int nPoints = 3;
        int[] xPoints = {xPos,xPos+width,xPos+(width/2)};
        int[] yPoints = {yPos+height,yPos+height,yPos+height-(int)((Math.sqrt(3)/2)*height)};
        if(fill)
            g.fillPolygon(xPoints, yPoints, nPoints);
        if(border)
            g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, nPoints);
        g.setColor(c);
        
        return g;
    }
    
    //Stilla "þvermál"/stærð forms.
    public void setShapeSize(int penSize_L){
        this.diam=penSize_L;
    }
    
    //Stilla breidd forms.
    public void setShapeWidth(int n){
       this.width = n; 
    }
    
    //Stilla hæð forms.
    public void setShapeHeight(int n){
        this.height = n;
    }
    
    //Skila þvermáli.
    public int getShapeSize(){
        return this.diam;
    }
    
    //Skila breidd.
    public int getShapeWidth(){
        return this.width;
    }
    
    //Skila hæð.
    public int getShapeHeight(){
        return this.height;
    }
    
    //Setur x-hnit á form og passar að músin smelli á miðju þess.
    public void setX(int xPos){ 
        this.xPos = xPos-(diam/2);
    }
    
    //Skilar x-gildi forms.
    public int getX(){
        return this.xPos;
    }
    
    //Setur y-hnit á form og passar að músin smelli á miðju þess.
    public void setY(int yPos){
        this.yPos = yPos-(diam/2);
    }
    
    //Skilar y-gildi forms.
    public int getY(){
        return this.yPos;
    }
    
    //Stillir hvort form eigi að hafa fyllingu.
    public void setFillBoolean(boolean b){
        this.fill = b;
    }
    
    //Segir hvort form eigi að hafa fyllingu.
    public boolean getFillBoolean(){
        return this.fill;
    }
    
    //Stillir hvort form sé með ramma.
    public void setBorderBoolean(boolean b){
        this.border = b;
    }
    
    //Segir hvort form sé með ramma.
    public boolean getBorderBoolean(){
        return this.border;
    }
}