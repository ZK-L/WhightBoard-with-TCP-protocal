/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

package draw;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class shape implements ShapeInter, Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String str;
    private Color c;
    private int stro;
    private Point p1;
    private Point p2;
    private boolean fill;
    
    public shape(String str, Color c,  int stro, Point p1, Point p2,boolean fill) {
        this.str = str;
        this.c = c;
        this.stro = stro;
        this.p1 = p1;
        this.p2 = p2;
        this.fill = fill;
    }
    
    @Override
    public String getshape() {
        return str;
    }

    @Override
    public Color getcolor() {
        return c;
    }

    @Override
    public int getStrokesize() {
        return stro;
    }

    @Override
    public Point getp1() {
        return p1;
    }

    @Override
    public Point getp2() {
        return p2;
    }

    @Override
    public String gettext() {
        return null;
    }

    @Override
    public boolean getfill() {
        return fill;
    }

}
