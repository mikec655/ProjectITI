
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
	
	private Socket socket;
	private BufferedInputStream input; 
	
	public Client(Socket socket, int number) {
		this.socket = socket;
		try {
		 	input = 
	            new BufferedInputStream(socket.getInputStream()); 
		}
		catch (IOException e){
			System.out.println(e);
		}
	}

	@Override
	public void run() {
		try {
	        while(true) {
	        	String text = "";
	        	
	        	// variabelen voor het meten van de tijd dat het duurt om 1 bestand te ontvangen
		        long startTime = 0;
		        long stopTime = 0;
	        	
	        	// leest totdat einde van xml-bestand bereikt is
	        	while(true) {
	        		text += (char) input.read();
	        		startTime = System.currentTimeMillis();
		        	if (text.contains("</WEATHERDATA>")) {
		        		//System.out.println(text);
			        	text = "";
			        	stopTime = System.currentTimeMillis();
			        	break;
		        	}
	        	}
	        	long time = stopTime - startTime;
		        System.out.println("Writing XML file took " + time + "ms");
		        System.out.println("======================================================="); 
		        if (time < 0) break;
	        } 
	        System.out.println("Closing connection");
	        input.close();
	        socket.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}
}
