
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
	
	private Socket socket;
	private DataInputStream input;
	private int number;
	
	public Client(Socket socket, int number) {
		this.socket = socket;
		this.number = number;
		try {
		 	input = new DataInputStream( 
	            new BufferedInputStream(socket.getInputStream())); 
		}
		catch (IOException e){
			System.out.println(e);
		}
	}

	@Override
	public void run() {
		try {
			String text = ""; 
	        boolean endOfFile = false;
	        BufferedWriter writer = new BufferedWriter(new FileWriter("thread" + number + ".xml"));
	        
	        // variabelen voor het meten van de tijd dat het duurt om 1 bestand te ontvangen
	        long startTime = 0;
	        long stopTime = 0;
	        
	        // leest totdat einde van xml-bestand bereikt is
	        while (!endOfFile) 
	        { 
	            try
	            { 
	            	text = input.readUTF();
	            	if (text != null) {
	            		startTime = System.currentTimeMillis();
	            	}
	                String[] lines = text.split("\\r?\\n");
	                for (int i = 0; i < lines.length; i++) {
	                	// zoeken naar het einde van het xml-bestand
	                	System.out.println(lines[i] + "*");
	                	writer.write(lines[i] + "\r\n");
	                	if (lines[i].equals("</WEATHERDATA>")){
	                		endOfFile = true;
	                		stopTime = System.currentTimeMillis();
	                		break;
	                	}
	                }
	            } 
	            catch(IOException i) 
	            { 
	                System.out.println(i); 
	            } 
	        } 
	        long time = stopTime - startTime;
	        System.out.println("Writing XML file took " + time + "ms");
	        System.out.println("Closing connection"); 
	        writer.close();
	
	        // close connection 
	        input.close();
	        socket.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
