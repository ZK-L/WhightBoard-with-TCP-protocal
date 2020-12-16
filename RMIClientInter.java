import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import draw.ShapeInter;

/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

public interface RMIClientInter extends Remote {

    public void broadcast(String str) throws RemoteException;
    public void updateuserlist(ArrayList<String> userlist) throws RemoteException;
    public void newdraw(ShapeInter newshape) throws RemoteException;
    public void clear() throws RemoteException;
    public void popoutevent(String event) throws RemoteException;
    public void kickout() throws RemoteException;
    public void loadlog(ArrayList<ShapeInter> loadlist) throws RemoteException;
    
    
    public void setupframe(whiteboardGUI wb) throws RemoteException;

}
