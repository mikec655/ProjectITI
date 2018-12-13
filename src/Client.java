
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

public class Client implements Runnable{
	
	private Socket socket;
	private DataInputStream input;
	
	public Client(Socket socket) {
		this.socket = socket;
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
		while(true) {
        	String text = "";
        	
        	// variabelen voor het meten van de tijd dat het duurt om 1 bestand te ontvangen
	        long startTime = 0;
	        long stopTime = 0;
	       
        	// leest totdat einde van xml-bestand bereikt is
        	while(true) {
        		try {
        			text += (char) input.read();
        		} catch (IOException e) {
        			System.out.println(e);
        		}
        		startTime = System.nanoTime();
	        	if (text.contains("</WEATHERDATA>")) {
	        		//System.out.println(text);
		        	break;
	        	}
        	}
	        int charIndex = 59;
			char c = '\n';
			for (int i = 0; i < 10; i++) {
				String str = "";
				String stn = "";
				String date = "";
				String time = "";
				float temp;
				float dewp;
				float stp;
				float slp;
				float visib;
				float wdsp;
				float prcp;
				float sndp;
				String frshtt = "";
				float cldc;
				int wnddir;
				
				while ((c = text.charAt(charIndex)) != '<') {
					stn += c;
					charIndex++;
				}
				//System.out.println(stn);
				
				charIndex += 15;
				while ((c = text.charAt(charIndex))  != '<') {
					date += c;
					charIndex++;
				}
				//System.out.println(date);
				
				charIndex += 16;
				while ((c = text.charAt(charIndex))  != '<') {
					time += c;
					charIndex++;
				}
				//System.out.println(time);
				
				charIndex += 16;
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					temp = Float.parseFloat(str);
				}
				
				charIndex += 16;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					dewp = Float.parseFloat(str);
				}
				
				charIndex += 15;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					stp = Float.parseFloat(str);
				}
				
				charIndex += 14;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					slp = Float.parseFloat(str);
				}
				
				charIndex += 16;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					visib = Float.parseFloat(str);
				}
				
				charIndex += 17;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					wdsp = Float.parseFloat(str);
				}
				
				charIndex += 16;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					prcp = Float.parseFloat(str);
				}
				
				charIndex += 16;
				str = "";
				while ((c = text.charAt(charIndex))  != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					sndp = Float.parseFloat(str);
				}
				
				charIndex += 18;
				while ((c = text.charAt(charIndex))  != '<') {
					frshtt += c;
					charIndex++;
				}
				//System.out.println(frshtt);
				
				charIndex += 18;
				str = "";
				while ((c = text.charAt(charIndex)) != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					cldc = Float.parseFloat(str);
				}
				
				charIndex += 18;
				str = "";
				while ((c = text.charAt(charIndex)) != '<') {
					str += c;
					charIndex++;
				}
				//System.out.println(str);
				if (!str.isEmpty()) {
					wnddir = Integer.parseInt(str);
				}
				
				charIndex += 48;
			}
			stopTime = System.nanoTime();
			long speed = stopTime - startTime;
			System.out.println("Writing XML file took " + speed + "ns");
		    //System.out.println("=======================================================");
	        if (speed < 0) break;
        } 
        //System.out.println("Closing connection");
        try {
        	input.close();
	        socket.close();
        } catch (IOException e) {
        	System.out.println(e);
        } 
	}
}
