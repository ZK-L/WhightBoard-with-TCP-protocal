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

public interface RMIServerInter extends Remote {
    
    public boolean requireaccess(String name) throws RemoteException;
    public void newclient(String name, RMIClientInter rmi) throws RemoteException;
    public void newdraw(ShapeInter newshape) throws RemoteException;
    public boolean checknamevali(String name) throws RemoteException; 
    
    public void broadcast(String str) throws RemoteException;
    public void kickout(String client) throws RemoteException;
    public void clear() throws RemoteException;
    public ArrayList<ShapeInter> getlog() throws RemoteException;
    public void loadlog(ArrayList<ShapeInter> loadlist) throws RemoteException;

    public void popoutevent(String event) throws RemoteException;

    public void updateuserlist() throws RemoteException;
    public void setupframe(whiteboardGUI wb) throws RemoteException;
    public void setHost(String host) throws RemoteException;
    public void leave(String name) throws RemoteException;


}
