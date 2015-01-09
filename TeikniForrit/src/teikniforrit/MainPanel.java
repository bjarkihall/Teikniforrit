/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package teikniforrit;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
/**
 *
 * @author Bjarki_2
 */
public class MainPanel extends javax.swing.JPanel {

    //Músin er á teiknifletinum.
    boolean mouseOnDrawingArea = true;
    //Mynd sem teiknað er á.
    Image image;
    //Afrit af myndinni.
    BufferedImage backup;
    //Þurfum þetta til að teikna á myndina.
    Graphics2D graphics2D;
    //Fyllanlegt form er smíðað.
    DrawingShape shape = new DrawingShape();
    //Hnitastrengur fyrir MainFrame.coordinates.
    String coordinatesStr;
    //Hnit músar, núverandi og síðustu.
    int currentX, currentY, oldX, oldY;
    //Segir til um hvort búið sé að teikna eitthvað á skjáinn.
    boolean startedPainting = false;
    //Segir til um hvort búið sé að vista mynd.
    boolean savedPainting = true;
    //Segir til um hvort form sé í formi bursta.
    private boolean brush;
    //Litur, stærð og staða penna.
    private Color color;
    private int penSize;
    private int state;
    //Bakgrunnslitur
    private Color backgroundColor;

    //Smiður MainPanels, sem heldur utan um svæði sem teiknað er á.
    public MainPanel()
    {
        this.brush = false;
        this.color = Color.BLACK;
        this.penSize = MainFrame.standardPenSize;
        this.state = MainFrame.drawFree;
        this.backgroundColor = Color.WHITE;
    }
    
    //Hnitastrengur er uppfærður.
    public void reportPosition(MouseEvent e) {
        coordinatesStr = "";
        coordinatesStr += "Hnit: " + "(X: " + (e.getPoint().x+1) + ", Y: " + (e.getPoint().y+1) + ").";
    }
    
    //Frjáls teikning með mús.
    public void drawFree(){
        if(shape.getFillBoolean()){
            graphics2D.setStroke(new BasicStroke(getPenSize()));
            graphics2D.drawLine(oldX, oldY, currentX, currentY);
            graphics2D.setStroke(new BasicStroke());
        }
        else{ 
            for(int i=0;i<getPenSize();i++){
            //Búið er að teikna jafnmargar samsíða línur og pennastærð segir til um.
            //Passað er að linurnar fari ekki útfyrir skjáinn.
                if(
                    oldX>0&&
                    oldY-penSize/2+i>0&&
                    currentX>0&&
                    currentY-penSize/2+i>0&&

                    oldX<getSize().width-1&&
                    oldY-penSize/2+i<getSize().height-1&&
                    currentX<getSize().width-1&&
                    currentY-penSize/2+i<getSize().width-1&&
                    mouseOnDrawingArea
                ){
                    graphics2D.drawLine(oldX, oldY-penSize/2+i, currentX, currentY-penSize/2+i);
                }
            }
        }
        repaint();
    }
      
    //Formi með staðlaðri lögun er plantað á flötinn þegar smellt er á hann með mús.
    public void placeShape(int x,int y, int state){
        shape.setShapeSize(penSize);
        shape.setX(x);
        shape.setY(y);
        
        if(state==MainFrame.drawRect||state==MainFrame.eraser)
            graphics2D = shape.paintRect(graphics2D, color, penSize, state);
        else if(state==MainFrame.drawCircle)
            graphics2D = shape.paintCircle(graphics2D, color, penSize);
        else if(state==MainFrame.drawTriangle)
            graphics2D = shape.paintTriangle(graphics2D, color, penSize);
        repaint();
    }
    
    //Formi sem lögunin var dregin með mús er plantað á flötinn.
    public void placeSizableShape(Rectangle area, int state){
        shape.setX((int)area.getX());
        shape.setY((int)area.getY());
        shape.setShapeWidth((int)area.width);
        shape.setShapeHeight((int)area.height);
        if(state==MainFrame.drawRect){
            graphics2D = shape.paintRect(graphics2D, color, area);
        }
        else if(state==MainFrame.drawCircle){
            graphics2D = shape.paintCircle(graphics2D, color, area);
        }
        else if(state==MainFrame.drawTriangle){
            graphics2D = shape.paintTriangle(graphics2D, color, area);
        }
        repaint();
    }

    //Form er fært (strokað út og staðsett miðað við ný hnit músar).
    public void moveShape(int x, int y, int state){
        if ((shape.getX()!=x) || (shape.getY()!=y)) {
            if(!this.brush&&(getState()!=MainFrame.eraser)){
                graphics2D.drawImage(backup, 0, 0, null);
            }
        placeShape(x,y, state);
        repaint();
        }
    }
    
    //Form er stækkað/minnkað miðað við breytanleg hnit músar.
    public void moveSizableShape(Point p1, Point p2, int state){
        if ((shape.getX()!=p1.getX()) || (shape.getY()!=p1.getX())) {
            if(!this.brush){
                graphics2D.drawImage(backup, 0, 0, null);
            }
            Rectangle area = new Rectangle(p1);
            area.add(p2);
            placeSizableShape(area, state);
            repaint();
        }
    }
    
    //Lína er teiknuð á flötinn.        
    public void drawLine(Point p1, Point p2){
        if(!this.brush)
            graphics2D.drawImage(backup, 0, 0, null);
        if(shape.getFillBoolean())
            graphics2D.setStroke(new BasicStroke(getPenSize()));
        graphics2D.drawLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
        graphics2D.setStroke(new BasicStroke());
        repaint();
    }
    
    //Texti er skrifaður á flötinn (fenginn með inntaki frá notanda úr nýjum glugga).
    //Fyrsta skipti á það til að taka sér tíma í að birta textann.
    public void drawText(int x, int y){
        String input = JOptionPane.showInputDialog("Skrifaðu það sem þú vilt fá á skjáinn");
        if (input!=null) {
            graphics2D.setFont(new Font("TimesRoman", Font.PLAIN, getPenSize()));
            graphics2D.drawString(input,x,y);
            repaint();
        }
    }

    @Override
    //Teiknar allt á flötinn. Aðalteiknifallið.
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        if(image == null)
        {
            image = createImage(MainFrame.dimensionOfScreen.width,MainFrame.dimensionOfScreen.height);
            graphics2D = (Graphics2D)image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }   
        g.drawImage(image, 0, 0, null);
    }
    
    //Tekur afrit af myndinni.
    public void takeSnapshot(){
        backup = new BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );
        paint(backup.getGraphics());
    }
    
    //Teiknar þá mynd sem er í afriti.
    public void undoRedo(){
        BufferedImage temp = backup;
        takeSnapshot();
        graphics2D.drawImage(temp, 0, 0, null);
        repaint();
    }

    //Hreinsar skjáinn i þeim bakgrunnslit sem er valinn.
    public void clear()
    {
        graphics2D.setPaint(backgroundColor);
        graphics2D.fillRect(1,1,MainFrame.dimensionOfScreen.width-1,MainFrame.dimensionOfScreen.height-1);
        graphics2D.setPaint(this.color);
        repaint();
        startedPainting=false;
    }
    
    //Stillir hvort form eigi að virka eins og bursti.
    public void setBrushBoolean(boolean b){
        this.brush = b;
    }
    
    //Segir hvort form eigi að virka eins og bursti.
    public boolean getBrushBoolean(){
        return this.brush;
    }
    
    //Breytir forgrunnslit.
    public void setColor(Color c)
    {
        this.color = c;
        graphics2D.setPaint(this.color);
        repaint();
    }
    
    //Segir hvaða forgrunnslitur er til staðar.
    public Color getColor() {
        return this.color;
    }    
    
    //Breytir bakgrunnslit.
    public void setBackgroundColor(Color c)
    {
        this.backgroundColor = c;
    }
    
    //Segir hvaða bakgrunnslitur er til staðar.
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }
    
    //Breytir pennastærð.
    public void setPenSize(int penSize){
        this.penSize = penSize;
    }
    
    //Segir hvaða pennastærð er til staðar.
    public int getPenSize(){
        return this.penSize;
    }
        
    //Stillir stöðu músar.
    public void setState(int state){
        this.state = state;
    }
    
    //Segir hver staða músar er.
    public int getState(){
        return this.state;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
