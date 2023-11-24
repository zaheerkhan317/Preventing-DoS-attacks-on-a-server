package com;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.swing.*;
import java.awt.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import java.util.HashMap;
public class Network extends JFrame{
	Simulation node;
	JPanel p1,p2;
	JButton b1,b2,b3;
	static JLabel l3;
	JLabel l1,l2;
	JComboBox c1,c2;
	Font f1;
	int size;
	int port;
	JFileChooser chooser;
	
	
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
public Network(int sz,int p){
	super("Cyber Security");
	size = sz;
	port = p;
	f1 = new Font("Courier New",Font.BOLD,14);
	node = new Simulation();
	p1 = new JPanel();
	p1.setLayout(new BorderLayout());
	p1.add(node,BorderLayout.CENTER);
	p1.setBackground(new Color(119,69,0));
	getContentPane().add(p1,BorderLayout.CENTER);
	p2 = new JPanel();
	p2.setLayout(new MigLayout("wrap 1")); 

	chooser = new JFileChooser();
	
	l1 = new JLabel("Users ID");
	l1.setFont(f1);
	p2.add(l1,"span,split 5");
	c1 = new JComboBox();
	c1.setFont(f1);
	for(int i=1;i<=size;i++){
		c1.addItem("U"+Integer.toString(i));
	}
	p2.add(c1);

	l2 = new JLabel("Filename");
	l2.setFont(f1);
	p2.add(l2);
	c2 = new JComboBox();
	c2.setFont(f1);
	p2.add(c2);

	l3 = new JLabel();
	l3.setFont(f1);
	p2.add(l3);

	b1 = new JButton("Upload File to Server");
	p2.add(b1,"span,split 4");
	b1.setFont(f1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Runnable r = new Runnable(){
				public void run(){
					upload();
				}
			};
			new Thread(r).start();
		}
	});

	b2 = new JButton("Download File from Server");
	p2.add(b2);
	b2.setFont(f1);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Runnable r = new Runnable(){
				public void run(){
					download();
				}
			};
			new Thread(r).start();
		}
	});

	b3 = new JButton("Exit Simulation");
	p2.add(b3);
	b3.setFont(f1);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			System.exit(0);
		}
	});
	
	getContentPane().add(p2,BorderLayout.SOUTH);
	Node.randomNodes(size,800,600,node,400);
	node.option = 0;
	node.repaint();
}
public void getFiles(){
	try{
		c2.removeAllItems();
		Socket soc = new Socket("localhost",3333);
		ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(soc.getInputStream());
		Object res[] = {"filelist"};
		oos.writeObject(res);
		oos.flush();
		Object obj[] = (Object[])ois.readObject();
		String files[] = (String[])obj[0];
		if(!files[0].equals("nofiles")) {
			for(int i=0;i<files.length;i++){
				c2.addItem(files[i]);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void upload(){
	try{
		String user = c1.getSelectedItem().toString();
		int option = chooser.showOpenDialog(this);
		if(option == chooser.APPROVE_OPTION) {
			//File filePath = chooser.getSelectedFile();
			/* ------SHA-256 Hashing------*/
			
			
			ArrayList<String> sha256HashValues = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader("virusDef.txt"));
			String line = reader.readLine();
			while (line != null) {
				sha256HashValues.add(line);
				line = reader.readLine();
			}
			reader.close();

			File file = chooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			Path path = Paths.get(filePath);
			String fileName = path.getFileName().toString();
			try {
					
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[1024 * 1024];
				int bytesRead;
				int messageLength = 0;
				while ((bytesRead = bis.read(buffer)) != -1) {
					messageLength += bytesRead;
				}
				bis.close();
				
				bis = new BufferedInputStream(new FileInputStream(file));
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
				
				int[] state = {
						0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
						0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
				};
				
				for (i = 0; i < words.length; i += 16) {
					int[] block = new int[64];
					System.arraycopy(words, i, block, 0, 16);
					for (int j = 16; j < 64; j++) {
						int s0 = rightRotate(block[j - 15], 7) ^ rightRotate(block[j - 15], 18) ^ (block[j - 15] >>> 3);
						int s1 = rightRotate(block[j - 2], 17) ^ rightRotate(block[j - 2], 19) ^ (block[j - 2] >>> 10);
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
				
				//System.out.println(hashString);
				//System.out.println(hashString);
				
				
				
				
				
				
				
				/*MessageDigest digest = MessageDigest.getInstance("SHA-256");
				FileInputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[8192];
				int bytesRead = 0;
				while ((bytesRead = fis.read(buffer)) != -1) {
					digest.update(buffer, 0, bytesRead);
				}
				fis.close();
				byte[] hashBytes = digest.digest();
				StringBuilder sb = new StringBuilder();
				for (byte b : hashBytes) {
					sb.append(String.format("%02x", b));
				}
				String fileSHA256Hash = sb.toString();
				*/
				
				
				
				String hashcode = hashString;

				

				boolean isInfected = false;
				for (String sha256HashValue : sha256HashValues) {
					if (hashString.equals(sha256HashValue)) {
						isInfected = true;
						break;
					}
				}

				if (isInfected) {
					JOptionPane.showMessageDialog(null,fileName + " The file is infected with a virus.");
				} else {
					JOptionPane.showMessageDialog(null,fileName + " The file is not infected with a virus.");
					FileInputStream fin = new FileInputStream(file);
					byte b[] = new byte[fin.available()];
					fin.read(b,0,b.length);
					fin.close();
					Upload up = new Upload(user,filePath,b,node,c2,hashcode);
					up.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
				/*====SHA256 Hash Code ====*/
			
        }
	
		
			
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void download() {
	try{
		String user = c1.getSelectedItem().toString();
		String file = c2.getSelectedItem().toString();
		Download dl = new Download(user,file,node);
		dl.start();
	}catch(Exception e){
		e.printStackTrace();
	}
}
}

/* ---------MD5 Hashing --------
			
			ArrayList<String> md5HashValues = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader("virusDef.txt"));
			String line = reader.readLine();
			while (line != null) {
			md5HashValues.add(line);
			line = reader.readLine();
			}
			reader.close();
				

				File file = chooser.getSelectedFile();

				try {
					MessageDigest digest = MessageDigest.getInstance("MD5");
					FileInputStream fis = new FileInputStream(file);
					byte[] buffer = new byte[8192];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) != -1) {
						digest.update(buffer, 0, bytesRead);
					}
					fis.close();
					byte[] hashBytes = digest.digest();
					StringBuilder sb = new StringBuilder();
					for (byte b : hashBytes) {
						sb.append(String.format("%02x", b));
					}
					String fileMD5Hash = sb.toString();

					boolean isInfected = false;
					for (String md5HashValue : md5HashValues) {
						if (fileMD5Hash.equals(md5HashValue)) {
							isInfected = true;
							break;
						}
					}

					if (isInfected) {
						JOptionPane.showMessageDialog(null,file + " The file is infected with a virus.");
						//System.out.println("The file is infected with a virus.");
					} else {
						JOptionPane.showMessageDialog(null,file + " The file is not infected with a virus.");
						//JOptionPane.showMessageDialog(null,file + " is clean.");
						FileInputStream fin = new FileInputStream(file);
						byte b[] = new byte[fin.available()];
						fin.read(b,0,b.length);
						fin.close();
						Upload up = new Upload(user,file.getName(),b,node,c2);
						up.start();
						//System.out.println("The file is not infected with a virus.");
					}

				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				*/
			
			//System.out.println(responseBody);

			// Print response
			//System.out.println(response.toString());
			
			//
			//System.out.println(content.toString());
			
			/*if (!file.exists()) {
				JOptionPane.showMessageDialog(null,"File not found: " + file);
            return;
			}
			
			if (file.isDirectory()) {
				JOptionPane.showMessageDialog(null,file + " is a directory.");
            return;
			}bhbhbbhhhhhhhbbbbbbbbgbbbg
			
			// Define the virus signature to look for
        byte[] virusSignature = new byte[] { 0x56, 0x49, 0x52, 0x55, 0x53 };

        // Open the file for reading
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // Read the file contents into a byte array
            byte[] fileContents = new byte[(int) file.length()];
            fileInputStream.read(fileContents);

            // Check if the virus signature is present in the file contents
            boolean isVirus = false;
            for (int i = 0; i <= fileContents.length - virusSignature.length; i++) {
                if (fileContents[i] == virusSignature[0]) {
                    boolean match = true;
                    for (int j = 1; j < virusSignature.length; j++) {
                        if (fileContents[i + j] != virusSignature[j]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        isVirus = true;
                        break;
                    }
                }
            }
			*/
            // Print the scan result
            /*if (isVirus) {
				JOptionPane.showMessageDialog(null,file + " contains a virus!");
            } else {
				JOptionPane.showMessageDialog(null,file + " is clean.");
				FileInputStream fin = new FileInputStream(file);
				byte b[] = new byte[fin.available()];
				fin.read(b,0,b.length);
				fin.close();
				Upload up = new Upload(user,file.getName(),b,node,c2);
				up.start();
				
            }*/