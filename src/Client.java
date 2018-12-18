
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Client implements Runnable{
	
	private Socket socket;
	private BufferedReader input;
	Station[] stations = new Station[10];

	
	public Client(Socket socket) {
		this.socket = socket;
		try {
		 	input = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		}
		catch (IOException e){
			System.out.println(e);
		}
	}

	@Override
	public void run() {
    	String xml = "";
    	
    	// variabelen voor het meten van de tijd dat het duurt om 1 bestand te ontvangen
        long startTime = 0;
        long stopTime = 0;
       
    	// leest totdat einde van xml-bestand bereikt is
    	while(true) {
    		try {
    			// inlezen van xml per lijn
    			String line;
    			if ((line = input.readLine()) == null) return;
    			else xml += line + "\n";
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    		startTime = System.nanoTime();
    		// controleer of einde van xml is bereikt
        	if (xml.contains("</WEATHERDATA>")) break;
    	}
    	// System.out.println(text);
    	
    	String inserts = ""; //DB inserts
    	
    	// Parsen van xml bestand per measurement 
        int charIndex = 58;
		char c = '\n';
		for (int i = 0; i < 10; i++) {
			if (stations[i]== null) stations[i]= new Station();
			String str = "";
			String stn = "";
			String date = "";
			String time = "";
			double temp = 0.0f;
			float dewp = 0.0f;
			float stp = 0.0f;
			float slp = 0.0f;
			float visib = 0.0f;
			float wdsp = 0.0f;
			float prcp = 0.0f;
			float sndp = 0.0f;
			String frshtt = "";
			float cldc = 0.0f;
			int wnddir = 0;
			
			while ((c = xml.charAt(charIndex)) != '<') {
				stn += c;
				charIndex++;
			}
			//System.out.println(stn);
			
			charIndex += 15;
			while ((c = xml.charAt(charIndex))  != '<') {
				date += c;
				charIndex++;
			}
			//System.out.println(date);
			
			charIndex += 16;
			while ((c = xml.charAt(charIndex))  != '<') {
				time += c;
				charIndex++;
			}
			//System.out.println(time);
			
			charIndex += 16;
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			double extrapolatedTemp = stations[i].extrapolateTemp();
			if (!str.isEmpty()) {
				temp = Double.parseDouble(str);
				if (temp > extrapolatedTemp * 1.2 && temp < extrapolatedTemp * 0.8) {
					temp = extrapolatedTemp;
				}
			
			} else {
				temp = extrapolatedTemp;
			}
			stations[i].addTemp(temp);
			
			charIndex += 16;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				dewp = Float.parseFloat(str);
			}
			
			charIndex += 15;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				stp = Float.parseFloat(str);
			}
			
			charIndex += 14;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				slp = Float.parseFloat(str);
			}
			
			charIndex += 16;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				visib = Float.parseFloat(str);
			}
			
			charIndex += 17;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				wdsp = Float.parseFloat(str);
			}
			
			charIndex += 16;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				prcp = Float.parseFloat(str);
			}
			
			charIndex += 16;
			str = "";
			while ((c = xml.charAt(charIndex))  != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				sndp = Float.parseFloat(str);
			}
			
			charIndex += 18;
			while ((c = xml.charAt(charIndex))  != '<') {
				frshtt += c;
				charIndex++;
			}
			//System.out.println(frshtt);
			
			charIndex += 18;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				cldc = Float.parseFloat(str);
			}
			
			charIndex += 18;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			//System.out.println(str);
			if (!str.isEmpty()) {
				wnddir = Integer.parseInt(str);
			}
			
			charIndex += 48;
			
			// opbouwen van query
			inserts += "(" + stn + ", '" + date + "', '" + time + "', " + temp + ", " + dewp + ", " + stp + ", " + slp + ", " + visib + ", " + wdsp + ", " + prcp + ", " + sndp + ", '" + frshtt + "', " + cldc + ", " + wnddir + ")";
			if (i < 9) {
				inserts += ", ";
			}
			
		}
		// Database.addInsert(inserts);
		
		// meten van verstreken tijd 
		stopTime = System.nanoTime();
		long speed = stopTime - startTime;
		//System.out.println("Writing XML file took " + speed + "ns");
		
	}
	
	public void close() {
		// TODO deze methode wordt niet gebruikt. maar moet ff bewaart worden
		// sluiten van de socket
		try {
	    	input.close();
	        socket.close();
	    } catch (IOException e) {
	    	System.out.println(e);
	    } 
	}
}
