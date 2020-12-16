/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

package draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class draw {
    private Graphics2D canvas;
    private Color currentcolor;
    private int strokesize;
    private Point p1;
    private Point p2;
    private String str;
    private boolean fill;
    
    public void drawshape(ShapeInter shape, Graphics2D g) {
        canvas = g;
        currentcolor = shape.getcolor();
        strokesize = shape.getStrokesize();
        p1 = shape.getp1();
        p2 = shape.getp2();
        str = shape.gettext();
        fill = shape.getfill();
        String sha = shape.getshape(); 
        
        try {
            if(sha.equals("Line")){
                canvas.setColor(currentcolor);
                canvas.setStroke(new BasicStroke(strokesize));
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            else if(sha.equals("Oval")){
                canvas.setColor(currentcolor);
                canvas.setStroke(new BasicStroke(strokesize));
                Point corner;
                int height;
                int width;
                
                if (p1.x < p2.x) {
                    if (p1.y<p2.y) {
                        corner = p1;
                        height = p2.y - p1.y;
                        width = p2.x - p1.x;
                    }
                    else {
                        corner = new Point(p1.x,p2.y);
                        height = p1.y - p2.y;
                        width = p2.x - p1.x;
                    }
                }
                else {
                    if (p1.y<p2.y) {
                        corner = new Point(p2.x,p1.y);
                        height = p2.y - p1.y;
                        width = p1.x - p2.x;
                    }
                    else {
                        corner = p2;
                        height = p1.y - p2.y;
                        width = p1.x - p2.x;
                    }
                }
    
                if(fill) {
                    canvas.fillOval(corner.x, corner.y, width, height);
                }
                else {
                    canvas.drawOval(corner.x, corner.y, width, height);
                }
            }
            else if(sha.equals("Rec")){
                canvas.setColor(currentcolor);
                canvas.setStroke(new BasicStroke(strokesize));
                Point corner;
                int height;
                int width;
                
                if (p1.x < p2.x) {
                    if (p1.y<p2.y) {
                        corner = p1;
                        height = p2.y - p1.y;
                        width = p2.x - p1.x;
                    }
                    else {
                        corner = new Point(p1.x,p2.y);
                        height = p1.y - p2.y;
                        width = p2.x - p1.x;
                    }
                }
                else {
                    if (p1.y<p2.y) {
                        corner = new Point(p2.x,p1.y);
                        height = p2.y - p1.y;
                        width = p1.x - p2.x;
                    }
                    else {
                        corner = p2;
                        height = p1.y - p2.y;
                        width = p1.x - p2.x;
                    }
                }
                if (fill) {
                    canvas.fillRect(corner.x, corner.y, width, height);
                }
                else {
                    canvas.drawRect(corner.x, corner.y, width, height);
                } 
            }
            else {
                canvas.setColor(currentcolor);
                canvas.drawString(str, p1.x, p1.y);
            }
        }catch (Exception e) {
            // TODO: handle exception
        }
    }
}
