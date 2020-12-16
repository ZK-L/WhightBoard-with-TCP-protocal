import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import draw.ShapeInter;

/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

public class RMIClient extends UnicastRemoteObject implements RMIClientInter {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private whiteboardGUI wb;
    
    
    protected RMIClient() throws RemoteException {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public synchronized void broadcast(String str) throws RemoteException {
        wb.getmessagepane().append("--"+str+System.lineSeparator());
        wb.getmessagepane().setCaretPosition(wb.getmessagepane().getDocument().getLength());
    }


    @Override
    public synchronized void updateuserlist(ArrayList<String> userlist) throws RemoteException {
        JTextArea ta = wb.getnamepane();
        ta.setText("");
        int flag = 1;
        for(String user:userlist) {
            if (flag==1) {
                ta.append(" "+user + "-------"+ "Host" + System.lineSeparator());
                flag++;
            }
            else
                ta.append(" "+user + "-------"+ "Client" + System.lineSeparator());    
        }
        
    }

    @Override
    public synchronized void newdraw(ShapeInter dr) throws RemoteException {
        wb.changelog(dr);
        
    }

    @Override
    public synchronized void clear() throws RemoteException {
        wb.clearlog();      
    }

    @Override
    public synchronized void popoutevent(String event) throws RemoteException {
        JOptionPane.showMessageDialog(wb.getframe(), event, "", JOptionPane.PLAIN_MESSAGE);
        wb.getframe().dispose();
        
    }

    @Override
    public void setupframe(whiteboardGUI wb) throws RemoteException {
        this.wb = wb;      
    }

    @Override
    public void kickout() throws RemoteException {
        JOptionPane.showMessageDialog(wb.getframe(), "Sorry, You were Removed From the Group\n"
                + " Click OK to Close Whiteboard", "Notice", JOptionPane.PLAIN_MESSAGE);
        wb.getframe().dispose();
    }

    @Override
    public void loadlog(ArrayList<ShapeInter> loadlist) throws RemoteException {
        wb.loadlog(loadlist);
        
    }


  
}
