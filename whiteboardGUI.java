import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import draw.ShapeInter;
import draw.draw;
import draw.shape;
import draw.text;




/*  
 *  @Author: Zhankui Lyu
 *  @Student ID: 1095734
 *  @Institution: University of Melbourne
 *
 */

public class whiteboardGUI{
    private JFrame frame;
    private JPanel toolbar;
    private JPanel drawarea;
    private JPanel control;
    private JPanel content;
    private JTextArea clientpane  = new JTextArea();
    private JTextArea messagepane = new JTextArea();
    private JTextArea roomidtext;
    private Color defaultcolor = Color.black;
    private Listener drawlistener = new Listener();
    private RMIServerInter server;
    private JTextField chat; 
    private JTextField kickt;
    private boolean filesaved=false;
    private File defaultpath;
    
    Font buttonfont = new Font("Times New Roman", Font.BOLD, 20);
    Font menufont = new Font("Times New Roman", Font.PLAIN, 20);
    
    
    public void Initiate(boolean host, String username, String roomid, RMIServerInter server) throws MalformedURLException, RemoteException, NotBoundException{
        this.server = server;
        
        
        
        frame = new JFrame();
        frame.setSize(1652, 929);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (host)
            frame.setTitle("Whiteboard ----- HOST");
        else
            frame.setTitle("Whiteboard ----- CLIENT");
        frame.setBackground(Color.white);
//        frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowevent) {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            server.leave(username);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                
                frame.dispose();                
            }
        });


        
        
    /*------------------------ menu ----------------------------- */  
        JMenuBar menu = new JMenuBar();
        menu.setBackground(new Color(209,224,240));
        JMenu file = new JMenu("File");
        file.setFont(menufont);
        JMenuItem ne = new JMenuItem("New");
        ne.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                newfile();
            }
        });
        
        
        
        
//        ============================= open===================================
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openfile();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
        });
        
//        =====================================save =============================
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
                
        });

//        =================================save as===========================
        JMenuItem saveas = new JMenuItem("Save As");
        saveas.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveas();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }       
                
            }
        });
        
//        =============================close===================================
        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.leave(username);
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.exit(0);
                
            }
        });
        file.add(ne);
        file.add(open);
        file.add(save);
        file.add(saveas);
        file.add(close);
        menu.add(file);
        frame.setJMenuBar(menu);
        
        

        
        content = new JPanel();
        
    /*------------------- functions ------------------------*/
        String[] functions = {"Draw", "Line", "Rectangle", "Oval", "Text", "Eraser"};
        toolbar = new JPanel();
        toolbar.setPreferredSize(new Dimension(1652, 50));
        toolbar.setBackground(Color.lightGray);
        toolbar.setLayout(new GridLayout(1, functions.length +1, 0, 2));
        ButtonGroup Bgroup = new ButtonGroup();
        for (int i = 0; i < functions.length;i++) {
            JToggleButton tb;
            if (i==0) {
                tb = new JToggleButton(functions[i]); 
                tb.setFont(buttonfont); 
                tb.setSelected(true);
            }
            else {
                tb = new JToggleButton(functions[i]);
                tb.setFont(buttonfont); 
            }
            tb.addActionListener(drawlistener);
            Bgroup.add(tb);
            toolbar.add(tb);
        }
        

     /*------------------- clear button--------------------------*/   
        JButton clear = new JButton("Clear");
        clear.setFont(buttonfont);
        clear.addActionListener(drawlistener);
        toolbar.add(clear);
        
        

//      ------------------------------- color button ----------------------------------  
        JButton bcolor = new JButton("Color");
        JPanel colorind = new JPanel();
        bcolor.setLayout(new FlowLayout(FlowLayout.TRAILING));
        bcolor.setHorizontalAlignment(SwingConstants.LEFT);  // set text allign to left 
        colorind.setPreferredSize(new Dimension(60, 30));
        colorind.setBackground(defaultcolor);
        bcolor.setFont(buttonfont);
        bcolor.addActionListener(drawlistener);
        drawlistener.setcolorind(colorind);
        bcolor.add(colorind);       
        toolbar.add(bcolor);
        content.add(toolbar);
        
        
//        ---------------------------------- size button --------------------------------
        JPanel size = new JPanel();
        size.setLayout(new FlowLayout(FlowLayout.LEFT));
        size.setPreferredSize(new Dimension(1500,50));
        JTextArea siz = new JTextArea("Size:");
        siz.setFont(buttonfont);
        siz.setEditable(false);
        siz.setBackground(new Color(238,238,238));
        JSlider sizeslider = new JSlider(JSlider.HORIZONTAL, 1, 50, 5);
        sizeslider.setPreferredSize(new Dimension(200,50));
        sizeslider.addChangeListener(new ChangeListener() {       
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                int s = sizeslider.getValue();
                drawlistener.setstrokesize(s);
            }
        });
        size.add(siz);
        size.add(sizeslider);
        
        JTextArea fillcolor = new JTextArea("Fill Color:");
        fillcolor.setFont(buttonfont);
        fillcolor.setEditable(false);
        fillcolor.setBackground(new Color(238,238,238));
        JCheckBox fill = new JCheckBox("");
        fill.setFont(buttonfont);
        fill.setPreferredSize(new Dimension(100,50));
        fill.addActionListener(new ActionListener() {   
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean tmp = fill.isSelected();
                drawlistener.setfill(tmp);
            }
        });       
        size.add(fillcolor);
        size.add(fill);
        
        content.add(size);
    
    /*---------------------- draw and control----------------------------*/        
        
        JPanel dac = new JPanel();
        dac.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        
        initialog();
        Graphics g;
        drawarea = new JPanel() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ArrayList<ShapeInter> array = drawlistener.getlog();
                draw drawnow = new draw();
                for (ShapeInter shape:array) {
                    drawnow.drawshape(shape, (Graphics2D)g);
                }
            }
        };
        drawarea.setPreferredSize(new Dimension(1252,839));
        drawarea.setBackground(Color.white);

        
        dac.add(drawarea);
//  ----------------------------------------------control panel------------------------------------------      
        control = new JPanel();
        control.setPreferredSize(new Dimension(400,839));
        control.setBackground(Color.LIGHT_GRAY);
        control.setLayout(new FlowLayout(SwingConstants.VERTICAL));
        roomidtext = new JTextArea();
        roomidtext.setText("Room ID: "+ roomid);
        roomidtext.setFont(new Font("times new roman", Font.BOLD, 25));
        roomidtext.setForeground(Color.blue);
        roomidtext.setBackground(Color.LIGHT_GRAY);
        roomidtext.setPreferredSize(new Dimension(350,50));
        roomidtext.setEditable(false);
        control.add(roomidtext);
        
        JLabel currentclient = new JLabel("Current User:");
        currentclient.setFont(buttonfont);
        currentclient.setPreferredSize(new Dimension(350,50));
        control.add(currentclient);
        
        JScrollPane js = new JScrollPane();
        js.setPreferredSize(new Dimension(350,200));
        js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        control.add(js);
        clientpane.setFont(new Font("Times New Roman",Font.PLAIN,20));
        clientpane.setEditable(false); 
        clientpane.setLineWrap(true);
        clientpane.setBackground(Color.white);
        js.setViewportView(clientpane);
        
        kickt = new JTextField();
        kickt.setPreferredSize(new Dimension(350,30));
        JButton kick = new JButton("Kick Out");
        kick.setFont(buttonfont);
        kick.setPreferredSize(new Dimension(230,30));
        if (host) {
            control.add(kickt);
            control.add(kick);
        }
        kickt.setForeground(Color.gray);
        kickt.setText("Enter a User Name to Remove");
        kickt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                kickt.setText("");
                kickt.setForeground(Color.black);
            }
        });
        kick.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               String name = kickt.getText().trim();
               if (name.equals(username)) {
                   JOptionPane.showMessageDialog(frame, "You cannot Remove yourself");
               }
               else {
                   try {
                       if(server.checknamevali(name)) {
                           JOptionPane.showMessageDialog(frame, "Please enter A correct User Name", "ERROR", JOptionPane.ERROR_MESSAGE);
                       }
                       else {
                           server.kickout(name);
                           Thread thread = new Thread() {
                             public void run() {  
                                 try {
                                    server.broadcast("SYSTEM: <"+name + "> was Kicked Out by HOST");
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                             }
                           };
                           thread.start();
                       }
                       kickt.setText("");
                   }catch (RemoteException ex) {
                       ex.printStackTrace();
                   }
               }
           }
        });
        
        JLabel LatestEvent = new JLabel("Chat and Latest Event");
        LatestEvent.setFont(buttonfont);
        LatestEvent.setPreferredSize(new Dimension(350,50));
        control.add(LatestEvent);
        

        JScrollPane js2 = new JScrollPane();
        js2.setPreferredSize(new Dimension(350,200));
        js2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        js2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        js2.setAutoscrolls(true);
        control.add(js2);
        messagepane.setFont(new Font("Times New Roman",Font.PLAIN,15));
        messagepane.setEditable(false); 
        messagepane.setLineWrap(true);
        messagepane.setBackground(Color.white);
        js2.setViewportView(messagepane);
        
        
        JPanel chatp = new JPanel();
        chatp.setLayout(new FlowLayout());
        chat = new JTextField();
        chat.setFont(new Font("Times New Roman",Font.PLAIN,20));
        chatp.setPreferredSize(new Dimension(350,50));
        chat.setPreferredSize(new Dimension(250,45));
        chat.setBackground(Color.white);
        chatp.add(chat);
        
        JButton send = new JButton("Send");
        send.setPreferredSize(new Dimension(80,40));
        chatp.add(send);
        
        control.add(chatp);
        chat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String str = chat.getText();
                    str = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())+"   " + username +" : " +str;
                    try {
                        server.broadcast(str);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                    chat.setText("");
                    
                }}                 
        });
        
        send.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e){
                String str = chat.getText();
                str = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())+"   " + username +": " +str;
                try {
                    server.broadcast(str);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                chat.setText("");
                
            }
        });

        dac.add(control);

        if(!host) {
          clear.setEnabled(false);
          saveas.setEnabled(false);
          save.setEnabled(false);
          open.setEnabled(false);
          ne.setEnabled(false);
          
        }
        
        content.add(dac);     
        frame.add(content);
        frame.setVisible(true);
        drawarea.setLayout(null);
        g = drawarea.getGraphics();
        drawlistener.setcanvas(g);
        drawlistener.setcolor(defaultcolor);
        drawarea.addMouseListener(drawlistener);
        drawarea.addMouseMotionListener(drawlistener);
        drawlistener.setstrokesize(5);
        drawlistener.setfill(false);
        drawlistener.setupGUI(this);
        messagepane.append("======== Welcome To the Whiteboard ========\r\n" + 
                "*************By Zhankui LYU**************\r\n" + 
                "=====================================\r\n" + 
                 System.lineSeparator());
    }  
    
    
    public JTextArea getnamepane() {
        
        return clientpane;
    }
    public JFrame getframe() {
        
        return frame;
    }
    public JTextArea getmessagepane() {
        return messagepane;
    }
    public void newdraw(ShapeInter dr) throws RemoteException {
        server.newdraw(dr);
    }
    
    public void changelog(ShapeInter dr) {
        drawlistener.changelog(dr);
    }
    
    public void clearlog() {
        drawlistener.clearlog();
    }
    
    public void loadlog(ArrayList<ShapeInter> loadlist) {
        drawlistener.loadlog(loadlist);
        drawarea.repaint();
    }
    
    private void initialog() throws RemoteException {
        drawlistener.initialog(server.getlog());
    }
    
    
    
    
    private void openfile() throws IOException {
        int i = JOptionPane.showConfirmDialog(frame, "save current canvas?");
        if (i==JOptionPane.YES_OPTION) {
            save();
        }
        
        JFileChooser filechooser = new JFileChooser();
        filechooser.setDialogType(JFileChooser.OPEN_DIALOG);
        if (filechooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
            File path = filechooser.getSelectedFile();
            ArrayList<ShapeInter> loadlist = readfile(path);
            if (loadlist !=null) {
                server.clear();
                server.loadlog(loadlist);
                new Thread() {
                    public void run() {
                        try {
                            server.broadcast("SYSTEM: HOST Load A Drawed board");
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();
                JOptionPane.showMessageDialog(frame, "Board Loaded successful");
            }
            else 
                JOptionPane.showMessageDialog(frame, "File ERROR");
        }
        
    }
    
    private ArrayList<ShapeInter> readfile(File file) throws IOException{
        ArrayList<ShapeInter> array = new ArrayList<ShapeInter>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        line = br.readLine();
        String shape = null;
        Color color = null;
        int Strokesize = 0;
        Point p1 = null;
        Point p2 = null;
        String text = null;
        boolean fill;
        String[] p;
        
        int counter = 0;
        if (!line.equals("whiteboard file"))
            JOptionPane.showMessageDialog(frame, "File error");
        
        else {
            while((line = br.readLine())!=null){
              switch (counter){  
              case 0:
                  shape = line;
                  counter++;
                  break;
              case 1:
                  p = line.split(",");
                  color = new Color(Integer.parseInt(p[0]),Integer.parseInt(p[1]),Integer.parseInt(p[2]));
                  counter++;
                  break;
              case 2:
                  Strokesize = Integer.parseInt(line);
                  counter++;
                  break;
              case 3: 
                  p = line.split(",");
                  p1 = new Point(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                  counter++;
                  break;
              case 4:
                  p = line.split(",");
                  p2 = new Point(Integer.parseInt(p[0]),Integer.parseInt(p[1]));
                  counter++;
                  break;
              case 5:
                  if (line.equals("null"))
                      text = null;
                  else 
                      text = line;
                  counter++;
                  break;
              case 6:
                  if (line.equals("true"))
                      fill = true;
                  else 
                      fill = false;
                  if (shape.isBlank())
                      array.add(new text(text, color, p1));
                  else
                      array.add(new shape(shape, color, Strokesize, p1, p2, fill));
                  counter =0;
                  break;
                  
              }
            }
        }
        
        br.close();
        return array;
    }
    
    
    private void newfile() {
        if (filesaved) {
            try {
                savelog(defaultpath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
            Thread thread = new Thread(){
                public void run() {
                    try{
                        server.broadcast("SYSTEM: HOST Start New Board");
                        server.clear();
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } 
                }
            };
            thread.start();
            JOptionPane.showMessageDialog(frame, "Current work saved"); 
        }
        
        else {
            int i = JOptionPane.showConfirmDialog(frame, "save current work?", "warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (i==JOptionPane.YES_OPTION) {
                try {
                    saveas();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            Thread thread = new Thread(){
                public void run() {
                    try{
                        server.broadcast("SYSTEM: HOST Start New Board");
                        server.clear();
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } 
                }
            };
            thread.start();
        }
        filesaved = false;
    }
    
    
    private void save() {
        if (!filesaved) {
            String name = JOptionPane.showInputDialog("Naming the file");
            if ((new File(System.getProperty("user.dir")+name+".txt")).exists()) {
                JOptionPane.showMessageDialog(frame, "Name already exist at "+System.getProperty("user.dir"));
                
            }
            else {
                defaultpath = new File(System.getProperty("user.dir")+File.separator+ name +".txt");
                try {
                    defaultpath.createNewFile();
                    savelog(defaultpath);
                    filesaved = true;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }                     
                Thread thread = new Thread(){
                    public void run() {
                        try{
                            server.broadcast("SYSTEM: HOST Saved Current Work");
                        } catch (RemoteException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } 
                    }
                };
                thread.start();
                JOptionPane.showMessageDialog(frame, "File saved at" + defaultpath);  
            }
        }
        else {
            try {
                savelog(defaultpath);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            Thread thread = new Thread(){
                public void run() {
                    try{
                        server.broadcast("SYSTEM: HOST Saved Current Work");
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } 
                }
            };
            thread.start();
            JOptionPane.showMessageDialog(frame, "File saved"); 
        }
    }
    
    private void saveas() throws IOException {
            JFileChooser choosefile = new JFileChooser();
            choosefile.setDialogTitle("Save file");
            choosefile.setDialogType(JFileChooser.SAVE_DIALOG);
            if (choosefile.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File path = choosefile.getSelectedFile(); 
                String filename = path.getName();
                if (filename.contains(".txt")){
                    path.getParentFile().mkdirs();
                    path.createNewFile();
                    savelog(path);
                }
                else {
                    path = new File(path.toString() +".txt");
                    path.getParentFile().mkdirs();
                    path.createNewFile();
                    savelog(path);
                }
                Thread thread = new Thread(){
                    public void run() {
                        try{
                            server.broadcast("SYSTEM: HOST Saved Current Work");
                        } catch (RemoteException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } 
                    }
                };
                thread.start();
                JOptionPane.showMessageDialog(frame, "File saved at" + path); 
            }
    }
    
    private void savelog(File path) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString()));
        String n = System.lineSeparator();
        ArrayList<ShapeInter> array = drawlistener.getlog();
        bw.append("whiteboard file" +n);
        for (ShapeInter shape:array) {
            bw.append(shape.getshape()+n);
            bw.append(shape.getcolor().toString().replaceAll("[a-zA-Z.=\\[\\]]", "")+n);
            bw.append(shape.getStrokesize()+n);
            bw.append(shape.getp1().toString().replaceAll("[a-zA-Z.=\\[\\]]", "")+n);
            bw.append(shape.getp2().toString().replaceAll("[a-zA-Z.=\\[\\]]", "")+n);
            bw.append(shape.gettext()+n);
            bw.append(shape.getfill()+n);
        }
        bw.close();
    }
    
    public void clearcanvas() throws RemoteException {
        server.clear();
        Thread thread = new Thread() {
            public void run() {
                try {
                    server.broadcast("SYSTEM: HOST CLeared Current whiteboard");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
