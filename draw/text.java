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

public class text implements ShapeInter, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String str;
    Color c;
    int stro;
    Point p1;
    Point p2;  
    
    public text(String str, Color c, Point p1) {
        this.str = str;
        this.c = c;
        this.p1 = p1;
    }
    
    @Override
    public String getshape() {
        return "";
    }

    @Override
    public Color getcolor() {
        return c;
    }

    @Override
    public int getStrokesize() {
        return 0;
    }

    @Override
    public Point getp1() {
        return p1;
    }

    @Override
    public Point getp2() {
        return p1;
    }

    @Override
    public String gettext() {
        return str;
    }

    @Override
    public boolean getfill() {
        // TODO Auto-generated method stub
        return false;
    }

}
