package com;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.net.InetAddress;
import com.jd.swing.custom.component.panel.SimpleGlossyPanel;
import com.jd.swing.util.PanelType;
import com.jd.swing.util.Theme;
public class Server extends JFrame{	
	ProcessThread thread;
	JPanel p1,p2,p3,p4;
	JLabel l1,l2;
	JButton b1;
	JScrollPane jsp;
	JTextArea area;
	Font f1,f2;
	ServerSocket server;
	Socket socket;
	
public void start(){
	try{
		server = new ServerSocket(3333);
		area.append("Server Started\n\n");
		area.append(String.format("%-10s | %-30s | %-20s | %-20s\n", "User", "Filename", "Filesize in Bytes", "Time"));

		while(true){
			socket = server.accept();
			socket.setKeepAlive(true);
			InetAddress address=socket.getInetAddress();
			String ipadd=address.toString();
			
			//area.append("Connected Computers :"+ipadd.substring(1,ipadd.length())+"\n");
			
			thread=new ProcessThread(socket,area);
			thread.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}

public Server(){
	setTitle("Cyber Security Storage Server");
	f1 = new Font("Castellar", 1, 16);
    p1 = new SimpleGlossyPanel(Theme.GLOSSY_MULTIBLUECOLOR_THEME,PanelType.PANEL_ROUNDED_RECTANGLUR);
	l1 = new JLabel("<html><body><center>Server Started".toUpperCase());
	l1.setFont(this.f1);
    l1.setForeground(Color.white);
    p1.add(l1);
	
    f2 = new Font("Courier New", 1, 13);
    p2 = new JPanel();
    p2.setLayout(new BorderLayout());
    area = new JTextArea();
    area.setFont(f2);
    jsp = new JScrollPane(area);
	jsp.setPreferredSize(new Dimension(400,500));
    area.setEditable(false);
    p2.add(jsp);

	
	
    getContentPane().add(p1, "North");
    getContentPane().add(p2, "Center");
	
    addWindowListener(new WindowAdapter(){
            @Override
        public void windowClosing(WindowEvent we){
            try{
				if(socket != null){
					socket.close();
				}
             server.close();
            }catch(Exception e){
                //e.printStackTrace();
            }
        }
    });
}
public static void main(String a[])throws Exception{
	Server cs = new Server();
	cs.setVisible(true);
	cs.setSize(700,600);
	new ServerThread(cs);
}
}