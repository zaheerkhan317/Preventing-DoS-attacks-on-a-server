package com;
import java.util.ArrayList;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Upload extends Thread{
	Simulation sim;
	String user,filePath,hashcode;
	byte data[];
	JComboBox cb;
public Upload(String user,String filePath,byte data[],Simulation sim,JComboBox cb,String hashcode){
	this.user = user;
	this.filePath = filePath;
	this.sim = sim;
	this.data = data;
	this.cb = cb;
	this.hashcode = hashcode;
}

public void run(){
	try{
		Users sender = null;
		for(int i=0;i<sim.users.size();i++){
			Users node = sim.users.get(i);
			if(node.getNode().equals(user)){
				sender = node;
				break;
			}
		}
		Path path = Paths.get(filePath);
		String fileName = path.getFileName().toString();
		Network.l3.setText("User : "+sender.getNode()+" uploading file "+fileName);
		Socket socket = new Socket("localhost",4444);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"upload",filePath,data,user,hashcode};
		JOptionPane.showMessageDialog(null,"Connected to Server");
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
		String msg = (String)res[0];
		for(int k=0;k<6;k++){
			sim.setSender(sender);
			sim.option=1;
			sim.repaint();
			sleep(80);
			sim.option=0;
			sim.repaint();
			sleep(40);
		}
		Network.l3.setText(msg);
		
		if(msg.equals(fileName+" saved at server"))
			cb.addItem(filePath);
		JOptionPane.showMessageDialog(null,msg);
		socket.close();
		JOptionPane.showMessageDialog(null,"Connection Revoked");
	}catch(Exception e){
		e.printStackTrace();
	}
}
}