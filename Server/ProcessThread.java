package com;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
public class ProcessThread extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	JTextArea area;
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	String timestamp = now.format(formatter);

// Format the timestamp as a string with the desired date and time format

	
public ProcessThread(Socket soc,JTextArea area){
    socket=soc;
	this.area=area;
    try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}
@Override
public void run(){
    try{
		Object input[]=(Object[])in.readObject();
        String type=(String)input[0];
		if(type.equals("download")){
			String file = (String)input[1];
			String user = (String)input[2];


			FileInputStream fin = new FileInputStream("UploadFiles/"+file);
			byte b[] = new byte[fin.available()];
			fin.read(b,0,b.length);
			fin.close();
			Object res[] = {b};
			out.writeObject(res);
			out.flush();
			area.append(file+" File sent to user "+user+"\n");
		}
		if(type.equals("upload")){
			String file = (String)input[1];
			byte data[] = (byte[])input[2];
			String user = (String)input[3];
			Path path = Paths.get(file);
			String fileName = path.getFileName().toString();
			FileOutputStream fout = new FileOutputStream("UploadFiles/"+fileName);
			fout.write(data,0,data.length);
			fout.close();
			File uploadedFile = new File("UploadFiles/"+fileName);
			long fileSizeInBytes = uploadedFile.length();
			//double fileSizeInKB = (double) fileSizeInBytes / (1024*1024);
			//long fileSizeInKBRounded = Math.round(fileSizeInKB);
			Object res[] = {fileName+" saved at server"};
			out.writeObject(res);
			out.flush();
			String text = String.format("%-10s | %-30s | %-20d | %-20s%n", user, fileName, fileSizeInBytes, timestamp);
			area.append(text);
			
		}
		if(type.equals("attack")){
			String file = (String)input[1];
			byte data[] = (byte[])input[2];
			FileOutputStream fout = new FileOutputStream("UploadFiles/"+file);
			fout.write(data,0,data.length);
			fout.close();
			Object res[] = {file+" saved at server"};
			out.writeObject(res);
			out.flush();
			area.append(file+" saved at server\n");
			/*
			Object res[] = {file+" ignore this file. File size is beyond limit"};
			out.writeObject(res);
			out.flush();
			area.append(file+" ignoring this file. File size is beyond limit\n");*/
		}
		if(type.equals("filelist")){
			StringBuilder sb = new StringBuilder();
			File file = new File("UploadFiles");
			File list[] = file.listFiles();
			for(int i=0;i<list.length;i++){
				sb.append(list[i].getName()+",");
			}
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length()-1);
			}
			if(sb.length() > 0){
				String arr[] = sb.toString().trim().split(",");
				Object res[] = {arr};
				out.writeObject(res);
				out.flush();
			} else {
				String arr[] = {"nofiles"};
				Object res[] = {arr};
				out.writeObject(res);
				out.flush();
			}
		}
    }catch(Exception e){
        e.printStackTrace();
    }
}
}
