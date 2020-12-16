/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

package draw;

import java.awt.Color;
import java.awt.Point;

public interface ShapeInter {
    public abstract String getshape();
    public abstract Color getcolor();
    public abstract int getStrokesize();
    public abstract Point getp1();
    public abstract Point getp2();
    public abstract String gettext();
    public abstract boolean getfill();
}
