import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import draw.ShapeInter;
import draw.draw;
import draw.shape;
import draw.text;

import java.awt.event.ActionListener;

    /*  
     *  @Author: Zhankui Lyu
     *  @Student ID: 1095734
     *  @Institution: University of Melbourne
     *
     */
public class Listener implements ActionListener, MouseListener, MouseMotionListener {

    private Color currentcolor;
    private Graphics2D canvas;
    private String function= "Draw";
    private JPanel colorind;
    private Point bufferpoint;
    private whiteboardGUI wb;
    private ArrayList<ShapeInter> log = new ArrayList<ShapeInter>();
    private int strokesize;
    private boolean fill = false;
    private draw drawshape = new draw();
    
    public void setcolor(Color c) {
        currentcolor = c;
    }
    
    public void setcanvas(Graphics g) {
        canvas =  (Graphics2D) g;
    }
    
    public void setcolorind(JPanel ci) {
        colorind = ci;
    }
    public void setupGUI(whiteboardGUI wb) {
        this.wb = wb;
    }
    public void initialog(ArrayList<ShapeInter> log) {
        this.log = log;
    }
    
    public void changelog(ShapeInter dr) {
        log.add(dr);
        drawnow(dr);
    }
    
    public void loadlog(ArrayList<ShapeInter> loadlist) {
        log.clear();
        log = loadlist;
    }
    
    public ArrayList<ShapeInter> getlog(){
        ArrayList<ShapeInter> tmp = log;
        return tmp;
    }
    
    public void clearlog() {
        log.clear();
        clearcanvas();
    }
    
    public void setstrokesize(int i) {
        canvas.setStroke(new BasicStroke(i));
        strokesize = i;
    }
    
    public void setfill(boolean b) {
        fill = b;
    }
    
    
    private void drawnow(ShapeInter s) {
        drawshape.drawshape(s, canvas);
    }
    
// ---------------------------------------------------------------drag-------------------------------------   
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (function) {
            case "Draw":
                ShapeInter line = new shape("Line", currentcolor, strokesize, bufferpoint, e.getPoint(), fill);
                drawnow(line);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                bufferpoint = e.getPoint();
                break;
            case "Eraser":
                ShapeInter line2 = new shape("Line", Color.white, strokesize, bufferpoint, e.getPoint(), fill);
                drawnow(line2);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line2);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                bufferpoint = e.getPoint();
                break;
            default:
                break;
        }
        
    }
    
    private void clearcanvas() {
        canvas.setColor(Color.white);
        canvas.fillRect(0, 0, 1252, 839);
        canvas.setColor(currentcolor);
    }

    
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    
//  ------------------------------------click-----------------------------------------------  
    @Override
    public void mouseClicked(MouseEvent e) {
        if (function.equals("Text")) {
            bufferpoint = e.getPoint();
        }
    }
    
    
    
//    --------------------------------------pressed-----------------------------------------------------------
    @Override
    public void mousePressed(MouseEvent e) {
        switch (function) {
            case "Draw":
                bufferpoint = e.getPoint();
                ShapeInter line = new shape("Line", currentcolor, strokesize, bufferpoint, bufferpoint, fill);
                drawnow(line);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Line":
            case "Rectangle":
            case "Oval":
            case "Text":
                bufferpoint = e.getPoint();
                break;
            case "Eraser":
                bufferpoint = e.getPoint();
                ShapeInter line2 = new shape("Line", Color.white, strokesize, bufferpoint, bufferpoint, fill);
                drawnow(line2);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line2);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            default:
                break;      
        }       
    }
    
    
//    ------------------------------------------------------------released -----------------------------------------------------------
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (function) {
            case "Draw":
                ShapeInter line = new shape("Line", currentcolor, strokesize, bufferpoint, e.getPoint(), fill);
                drawnow(line);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Line":
                ShapeInter line2 = new shape("Line", currentcolor, strokesize, bufferpoint, e.getPoint(), fill);
                drawnow(line2);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(line2);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Rectangle":
                ShapeInter rec = new shape("Rec", currentcolor, strokesize, bufferpoint, e.getPoint(),fill);
                drawnow(rec);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(rec);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Oval":
                ShapeInter oval = new shape("Oval", currentcolor, strokesize, bufferpoint, e.getPoint(),fill);
                drawnow(oval);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(oval);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Text":
                String tmp = JOptionPane.showInputDialog("");
                ShapeInter txt = new text(tmp, currentcolor, e.getPoint());
                drawnow(txt);
                new Thread() { 
                    public void run() {
                        try {
                            wb.newdraw(txt);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Eraser":
                ShapeInter line3= new shape("Line", Color.white, strokesize, bufferpoint, e.getPoint(), fill);
                drawnow(line3);
                new Thread() { 
                    public void run() {
                        try {                
                            wb.newdraw(line3);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            default:
                break;      
        }   
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Color":
                Color c = JColorChooser.showDialog(null, "Choose a Color", currentcolor);
                if (c != null) {
                    currentcolor = c;
                    colorind.setBackground(currentcolor);
                }
                break;
            case "Clear":
                log.clear();
                clearcanvas();
                new Thread() { 
                    public void run() {
                        try {
                            wb.clearcanvas();
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();
                break;
            default:
                function = e.getActionCommand();
                break;
        }
    }  

    
}


