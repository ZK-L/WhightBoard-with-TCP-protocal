import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import draw.ShapeInter;

/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

public class RMIServer extends UnicastRemoteObject implements RMIServerInter {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private HashMap<String, RMIClientInter> userlist = new HashMap<String, RMIClientInter>();
    private ArrayList<ShapeInter> drawlog = new ArrayList<ShapeInter>();
    private ArrayList<String> namelist = new ArrayList<String>();
    private whiteboardGUI wb;
    private String host;
    
    protected RMIServer() throws RemoteException {
        super();
    }

    @Override
    public synchronized boolean requireaccess(String name) throws RemoteException {
        int i = JOptionPane.showConfirmDialog(wb.getframe(),name+ " want to share your whiteboard.", "Request", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (i == JOptionPane.YES_OPTION)
            return true;
        else
            return false;
    }

    @Override
    public synchronized void newclient(String name, RMIClientInter rmi) throws RemoteException {
        userlist.put(name, rmi);
        namelist.add(name);

        for(RMIClientInter client:userlist.values()) {
            client.updateuserlist(namelist);
        }
    }
    
 

 
    

    @Override
    public synchronized void newdraw(ShapeInter dr) throws RemoteException {
        for (RMIClientInter c:userlist.values()) {
            c.newdraw(dr);
            drawlog.add(dr);
        }  
    }

    @Override
    public synchronized boolean checknamevali(String name) throws RemoteException {
        if (userlist.get(name)==null)
            return true;
        else
            return false;
    }

    @Override
    public synchronized void broadcast(String str) throws RemoteException {
        for(RMIClientInter client:userlist.values()) {
            client.broadcast(str);
        }
        
    }

    @Override
    public synchronized void kickout(String client) throws RemoteException {
        RMIClientInter c = userlist.get(client);
        new Thread() {
            public void run() {
                try {
                    c.kickout();
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();   
        userlist.remove(client);
        namelist.remove(client);
        updateuserlist();
        JOptionPane.showMessageDialog(wb.getframe(), "Client <" + client + "> has been Removed");
        
    }

    @Override
    public synchronized void clear() throws RemoteException {
        drawlog.clear();
        for (RMIClientInter client:userlist.values()) {
            client.clear();
        }  
        
    }

    @Override
    public synchronized void popoutevent(String event) throws RemoteException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public synchronized void updateuserlist() throws RemoteException {
        for (RMIClientInter client:userlist.values()) {
            client.updateuserlist(namelist);;
        }  
    }

    @Override
    public void setupframe(whiteboardGUI wb) throws RemoteException {
        this.wb = wb;
        
    }

    @Override
    public synchronized ArrayList<ShapeInter> getlog() throws RemoteException {
        return drawlog;
    }

    @Override
    public synchronized void setHost(String host) throws RemoteException {
        this.host = host;
        
    }

    @Override
    public synchronized void leave(String name) throws RemoteException {
        userlist.remove(name);
        namelist.remove(name);

        
        if (!name.equals(host)) {
            broadcast("SYSTEM: CLIENT <" + name + "> Leaved");
            updateuserlist();
        }
        else {
            for (RMIClientInter client:userlist.values()) {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            client.popoutevent("Host <" + name + "> Leaved, Whiteboad will be closed");
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                
            } 
        }
        
    }

    @Override
    public void loadlog(ArrayList<ShapeInter> loadlist) throws RemoteException {
        drawlog.clear();
        drawlog = loadlist;
        for(RMIClientInter client:userlist.values()) {
            client.loadlog(loadlist);
        }
    }





}
