import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*	
 *	@Author: Zhankui Lyu
 *	@Student ID: 1095734
 *	@Institution: University of Melbourne
 *
 */

public class Client extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel content = new JPanel();
    private JPanel Host = new JPanel();
    private JPanel Client = new JPanel();
    private Font buttonfont = new Font("Times New Roman", Font.BOLD, 20);
    private Font menufont = new Font("Times New Roman", Font.BOLD, 15);
    private Font pswfont = new Font("Times New Roman", Font.PLAIN, 25);
    private void Initialize() {
        this.setTitle("Whiteboard Client");
        this.setSize(500, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(1,1));
        this.setBackground(Color.white);
        this.setResizable(false);
        
//        ---------------host-------------------------
        Host.setPreferredSize(new Dimension(500,350));    
        Host.setBackground(Color.lightGray);
        JPanel tick1 = new JPanel();
        tick1.setLayout(new FlowLayout());
        JCheckBox hostc = new JCheckBox("Host");
        tick1.setPreferredSize(new Dimension(500,30));
        hostc.setFont(buttonfont);
        tick1.add(hostc);
        Host.add(tick1);
        JPanel host2 = new JPanel();
        host2.setPreferredSize(new Dimension(500,300));
        host2.setLayout(new FlowLayout(SwingConstants.VERTICAL));
        JLabel setrooml = new JLabel("Set Name for your Room :");
        setrooml.setFont(menufont);
        setrooml.setAlignmentY(SwingConstants.CENTER);
        setrooml.setPreferredSize(new Dimension(450,50));
        host2.add(setrooml);
        
        JTextField setroom = new JTextField();
        setroom.setForeground(Color.gray);
        setroom.setFont(pswfont);
        setroom.setPreferredSize(new Dimension(400,40));
        host2.add(setroom);
        
        JLabel Hostnamel = new JLabel("Set Your Name :");
        Hostnamel.setFont(menufont);
        Hostnamel.setAlignmentY(SwingConstants.CENTER);
        Hostnamel.setPreferredSize(new Dimension(450,50));
        host2.add(Hostnamel);
        
        JTextField Hostname = new JTextField();
        Hostname.setForeground(Color.gray);
        Hostname.setFont(pswfont);
        Hostname.setPreferredSize(new Dimension(400,40));
        host2.add(Hostname);
        

        
        JButton createnew = new JButton("Click here to Start");
        createnew.setFont(buttonfont);
        createnew.setPreferredSize(new Dimension(300,50));
        host2.add(createnew);
        
        Host.add(host2);   
        content.add(Host);

        
//        -----------------client-------------------------
        Client.setPreferredSize(new Dimension(500,350));
        Client.setBackground(Color.lightGray);
        JPanel tick2 = new JPanel();
        tick2.setLayout(new FlowLayout());
        JCheckBox clientc = new JCheckBox("Client");
        tick2.setPreferredSize(new Dimension(500,30));
        clientc.setFont(buttonfont);
        tick2.add(clientc);
        Client.add(tick2);
        JPanel client2 = new JPanel();
        client2.setPreferredSize(new Dimension(500,300));
        Client.add(client2);
        
        
        client2.setLayout(new FlowLayout(SwingConstants.VERTICAL));
        JLabel Hostipl = new JLabel("Enter Host IP:");
        Hostipl.setFont(menufont);
        Hostipl.setAlignmentY(SwingConstants.CENTER);
        Hostipl.setPreferredSize(new Dimension(450,50));
        client2.add(Hostipl);
        
        JTextField hostip = new JTextField();
        hostip.setForeground(Color.gray);
        hostip.setFont(pswfont);
        hostip.setPreferredSize(new Dimension(400,40));
        client2.add(hostip);
        
        JLabel rooml = new JLabel("Enter the Room ID:");
        rooml.setFont(menufont);
        rooml.setAlignmentY(SwingConstants.CENTER);
        rooml.setPreferredSize(new Dimension(450,50));
        client2.add(rooml);
        
        JTextField room = new JTextField();
        room.setForeground(Color.gray);
        room.setFont(pswfont);
        room.setPreferredSize(new Dimension(400,40));
        client2.add(room);
        
        JButton enterroom = new JButton("Enter Room");
        enterroom.setFont(buttonfont);
        enterroom.setPreferredSize(new Dimension(300,50));
        client2.add(enterroom);
        
        
        content.add(Client);
        this.add(content);
        
        
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(hostc);
        bgroup.add(clientc);
        hostc.setSelected(true);
        setpanel(host2, true);
        setpanel(client2,false);  

        
        hostc.addActionListener(new ActionListener() {      
            @Override
            public void actionPerformed(ActionEvent e) {
                    setpanel(host2, true);
                    setpanel(client2,false);  
            }
        });
        
        clientc.addActionListener(new ActionListener() {      
            @Override
            public void actionPerformed(ActionEvent e) {
                    setpanel(host2,false);
                    setpanel(client2, true);                    
            }
        });
        
        
        
//      =================================  create room =====================================
        createnew.addActionListener(new ActionListener() {         
            @Override
            
            public void actionPerformed(ActionEvent e) {
                if ((setroom.getText().trim().equals(""))|(Hostname.getText().trim().equals(""))) {
                    JOptionPane.showMessageDialog(host2, "The input area cannot be blank", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        int port = (new Random().nextInt(10000)) + 1200;
                        LocateRegistry.createRegistry(port);
                        RMIServerInter server = new RMIServer(); 
                        String address = "//localhost:"+port+"/"+setroom.getText().trim(); 
                        Naming.bind(address, server);
                        
                        RMIClient client = new RMIClient();
                        whiteboardGUI ui = new whiteboardGUI();
                        client.setupframe(ui);
                        server.setupframe(ui);
                        server.setHost(Hostname.getText());
    
                        ui.Initiate(true,Hostname.getText(), port+"/"+setroom.getText(),server);
                        server.newclient(Hostname.getText(), client);
                        dispose();
                     
                    } catch (RemoteException e1) {
                        JOptionPane.showMessageDialog(host2, "Remote Error, Try again", "ERROR", JOptionPane.ERROR_MESSAGE);
                        e1.printStackTrace();
                    } catch (AlreadyBoundException e1) {
                        JOptionPane.showMessageDialog(host2, "Remote Error, Try again", "ERROR", JOptionPane.ERROR_MESSAGE);
                        JOptionPane.showMessageDialog(Client, "try again");
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (NotBoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
    
//      =================================  enter room =====================================
        enterroom.addActionListener(new ActionListener() {         
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((hostip.getText().trim().equals(""))|(room.getText().trim().equals(""))) {
                    JOptionPane.showMessageDialog(host2, "The input area cannot be blank", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String name;
                    name = JOptionPane.showInputDialog("Enter your Name");
                    try {
                        RMIServerInter server = (RMIServerInter) Naming.lookup("//"+hostip.getText()+":"+ room.getText()); 
                        boolean flag = server.checknamevali(name);
                        while(!flag) {
                            name = JOptionPane.showInputDialog("Name has been taken, try another one.");
                            flag = server.checknamevali(name);
                            if (name == null) {
                                break;
                            }
                        }
                        if (flag) {
                                                        RMIClient client = new RMIClient();
                            if(server.requireaccess(name)) {
                                whiteboardGUI ui = new whiteboardGUI();
                                client.setupframe(ui);
        
                                ui.Initiate(false, name, room.getText(),server);
                                server.broadcast("SYSTEM: New CLIENT <" + name +"> Entered Room");
                                server.newclient(name, client);
                                dispose();
                            }
                            else {
                                JOptionPane.showMessageDialog(client2, "Host reject your Apply");
                            }
                        }
                    } catch (RemoteException | MalformedURLException | NotBoundException e2) {
                        JOptionPane.showMessageDialog(client2, "Cannot connect server,Please check your Input");
                    }
                }
            }
        });
    }


    private void setpanel(JPanel jp, boolean able) {
        jp.setEnabled(able);
        Component[] components = jp.getComponents();

        for (Component comp:components) {
            comp.setEnabled(able);
        }
    }
    
    
    public static void main (String[] args) {
        Client newclient = new Client();
        newclient.Initialize();
        newclient.setVisible(true);
    }
}
