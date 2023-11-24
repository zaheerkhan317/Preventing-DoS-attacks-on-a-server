package com;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.swing.*;
import java.util.HashMap;
import java.io.FileWriter;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.nio.channels.FileChannel;
import javax.swing.*;
import java.awt.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import java.util.HashMap;



public class PacketReceiverThread extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	JTextArea area;
	String user;
	


	
public void deleteFiles(File path){
	if(path.exists()){
		File[] dir = path.listFiles();
		for(int d=0;d<dir.length;d++){
			if(dir[d].isFile()){
				dir[d].delete();
			}else if(dir[d].isDirectory()){
				deleteFiles(dir[d]);
			}
		}
		if(path.isDirectory()){
			path.delete();
		}
	}
}


public void sendcode(String hashString, String code, File fileObj, byte data[], String fileName, String user, long fileLength, byte buffer[], int bytesRead) {
	String hashcode = hashString;
	
		String filePath = fileObj.getAbsolutePath();
		String serverAddress = "localhost";
		int serverPort = 3333;
		int CHUNK_SIZE = 1024 * 1024; // 1 MB

		try {
			
			Socket soc = new Socket(serverAddress, serverPort);
			ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());

			Object[] res = { "upload", filePath, data, user, fileLength };
			oos.writeObject(res);
			oos.flush();

			Object[] obj = (Object[]) ois.readObject();
			out.writeObject(obj);
			out.flush();

			area.append(fileName + " size beyond limit. The file divided into chunks and sent to the server for storage\n");
			JOptionPane.showMessageDialog(null, "File uploaded and combined successfully.");

							// Close the streams and socket
			oos.close();
			ois.close();
			soc.close();
			
			
							try (Socket socket = new Socket(serverAddress, serverPort);
								BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath))) {

							oos.writeObject("upload");
							oos.writeObject(fileName);

						
							while ((bytesRead = in.read(buffer)) != -1) {
								oos.writeObject(buffer);
							}

							oos.flush();
							String serverResponse = (String) ois.readObject();
							JOptionPane.showMessageDialog(null, "Server response: " + serverResponse);
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}

						String OUTPUT_DIR = filePath;

						try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
							while (true) {
								try (Socket socket = serverSocket.accept();) {

									String command = (String) ois.readObject();
									if ("upload".equals(command)) {
										String fileName1 = (String) ois.readObject();
										Path outputPath = Paths.get(OUTPUT_DIR, fileName1);

										Files.deleteIfExists(outputPath);
										Files.createFile(outputPath);

										while ((buffer = (byte[]) ois.readObject()) != null) {
											Files.write(outputPath, buffer, StandardOpenOption.APPEND);
										}

										oos.writeObject("File uploaded and combined successfully.");
									}
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
			
		} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		
		
	
}
		

public PacketReceiverThread(Socket soc,JTextArea area){
    socket=soc;
	this.area=area;
    try{
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}
private static int rightRotate(int value, int distance) {
        return (value >>> distance) | (value << (32 - distance));
    }

private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
            0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
            0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
            0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
            0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
            0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
            0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
            0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };
	
	

@Override
public void run() {
	try {
		Object input[] = (Object[]) in.readObject();
		String type = (String) input[0];
		if (type.equals("upload")) {
			String file = (String) input[1];
			byte data[] = (byte[]) input[2];
			String user = (String) input[3];
			String code = (String) input[4];
			deleteFiles(new File("output"));
			FileWriter fout = new FileWriter("packets.txt");
			fout.write(Integer.toString(data.length));
			fout.close();
			Path path = Paths.get(file);
			String fileName = path.getFileName().toString();
			Hadoop.run("packets.txt");
			if (NetworkMonitor.status.equals("normal")) {
				Socket soc = new Socket("localhost", 3333);
				ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
				Object res[] = { "upload", file, data, user };
				oos.writeObject(res);
				oos.flush();
				Object obj[] = (Object[]) ois.readObject();
				out.writeObject(obj);
				out.flush();
				area.append(fileName + " size is under the limit and sent to the server for storage\n");
			} else {
				ArrayList<String> sha256HashValues = new ArrayList<String>();
				BufferedReader reader = new BufferedReader(new FileReader("virusDef.txt"));
				String line = reader.readLine();
				while (line != null) {
					sha256HashValues.add(line);
					line = reader.readLine();
				}
				reader.close();
				
				File fileObj = new File(file);

				try {

					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileObj));
					byte[] buffer = new byte[1024 * 1024];
					int bytesRead;
					int messageLength = 0;
					while ((bytesRead = bis.read(buffer)) != -1) {
						messageLength += bytesRead;
					}
					bis.close();

					bis = new BufferedInputStream(new FileInputStream(fileObj));
					byte[] fileBytes = new byte[messageLength];
					int offset = 0;
					while ((bytesRead = bis.read(buffer)) != -1) {
						System.arraycopy(buffer, 0, fileBytes, offset, bytesRead);
						offset += bytesRead;
					}
					bis.close();

					int numBlocks = ((messageLength + 8) / 64) + 1;
					int[] words = new int[numBlocks * 16];
					int i;
					for (i = 0; i < messageLength; i++) {
						words[i / 4] |= (fileBytes[i] & 0xff) << (24 - (i % 4) * 8);
					}
					words[i / 4] |= 0x80 << (24 - (i % 4) * 8);
					words[numBlocks * 16 - 1] = messageLength * 8;

					int[] state = { 0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c,
							0x1f83d9ab, 0x5be0cd19 };

					for (i = 0; i < words.length; i += 16) {
						int[] block = new int[64];
						System.arraycopy(words, i, block, 0, 16);
						for (int j = 16; j < 64; j++) {
							int s0 = rightRotate(block[j - 15], 7) ^ rightRotate(block[j - 15], 18)
									^ (block[j - 15] >>> 3);
							int s1 = rightRotate(block[j - 2], 17) ^ rightRotate(block[j - 2], 19)
									^ (block[j - 2] >>> 10);
							block[j] = block[j - 16] + s0 + block[j - 7] + s1;
						}
						int a = state[0];
						int b = state[1];
						int c = state[2];
						int d = state[3];
						int e = state[4];
						int f = state[5];
						int g = state[6];
						int h = state[7];
						for (int j = 0; j < 64; j++) {
							int S1 = rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25);
							int ch = (e & f) ^ (~e & g);
							int temp1 = h + S1 + ch + K[j] + block[j];
							int S0 = rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22);
							int maj = (a & b) ^ (a & c) ^ (b & c);
							int temp2 = S0 + maj;
							h = g;
							g = f;
							f = e;
							e = d + temp1;
							d = c;
							c = b;
							b = a;
							a = temp1 + temp2;
						}
						state[0] += a;
						state[1] += b;
						state[2] += c;
						state[3] += d;
						state[4] += e;
						state[5] += f;
						state[6] += g;
						state[7] += h;
					}

					byte[] hashBytes = new byte[32];
					for (i = 0; i < 8; i++) {
						hashBytes[i * 4] = (byte) (state[i] >>> 24);
						hashBytes[i * 4 + 1] = (byte) (state[i] >>> 16);
						hashBytes[i * 4 + 2] = (byte) (state[i] >>> 8);
						hashBytes[i * 4 + 3] = (byte) state[i];
					}

					StringBuilder sb = new StringBuilder();
					for (byte b : hashBytes) {
						sb.append(String.format("%02x", b));
					}
					String hashString = sb.toString();
					long fileLength = fileObj.length();
					//JOptionPane.showMessageDialog(null,hashString);
					//JOptionPane.showMessageDialog(null,code);
					boolean isInfected = false;
					for (String sha256HashValue : sha256HashValues) {
						if (hashString.equals(sha256HashValue)) {
							isInfected = true;
							break;
						}
					}
					if (isInfected) {
					//JOptionPane.showMessageDialog(null,fileName + " The file is infected with a virus.");
					System.exit(0);
					} else if(hashString.equals(code)){
						//JOptionPane.showMessageDialog(null,fileName + " Uploaded file and received file are same");
						sendcode(hashString, code, fileObj, data, fileName, user, fileLength, buffer, bytesRead);
					}
					else{
						//JOptionPane.showMessageDialog(null,fileName + " Uploaded file and received file are not same");
						System.exit(0);
					}
					
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				


						

						
					
				
				}
			}
		} 
	
	catch (Exception e) {
		e.printStackTrace();
	}
	
}
}
