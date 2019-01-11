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
		for (int i = 0; i < 10; i++) {
			stations[i] = new Station();
		}
	}
	
	@Override
	public void run() {
		String xml = "";
    	
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
    		
    		// controleer of einde van xml is bereikt
        	if (xml.contains("</WEATHERDATA>")) break;
    	}
    	
    	//System.out.println(xml);
		int charIndex = 58;
		int[] charIndexSteps = {15, 16, 16, 16, 15, 14, 16, 17, 16, 16, 18, 18, 18, 48};
		char c;
		
    	String inserts = ""; //DB inserts
    	
    	int j = 0;
		for (int i = 0; i < 10; i++) {
			String str = "";
			String stn = "";
			String date = "";
			String time = "";
			float temp = 0.0f;
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
			charIndex += charIndexSteps[j++];
			
			while ((c = xml.charAt(charIndex)) != '<') {
				date += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			
			while ((c = xml.charAt(charIndex)) != '<') {
				time += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			temp = Float.parseFloat(str);
			if (stations[i].sizeOfTemp() > 0) {
				float extrapolatedTemp = stations[i].extrapolateTemp();
				if (Math.abs(temp) > Math.abs(extrapolatedTemp) * 1.2) {
					temp = extrapolatedTemp * 1.2f;
					//System.out.println("Wrong value -> " + temp);
				} else if (Math.abs(temp) < Math.abs(extrapolatedTemp) * 0.8) {
					temp = extrapolatedTemp * 0.8f;
					//System.out.println("Wrong value -> " + temp);
				}
			}
			stations[i].addTemp(temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) dewp = stations[i].extrapolateDewp(); 
			else dewp = Float.parseFloat(str);
			stations[i].addDewp(dewp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) stp = stations[i].extrapolateStp(); 
			else stp = Float.parseFloat(str);
			stations[i].addStp(stp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) slp = stations[i].extrapolateSlp(); 
			else slp = Float.parseFloat(str);
			stations[i].addSlp(slp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) visib = stations[i].extrapolateSlp(); 
			else visib = Float.parseFloat(str);
			stations[i].setVisib(visib);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) wdsp = stations[i].extrapolateWdsp();
			else wdsp = Float.parseFloat(str);
			stations[i].addWdsp(wdsp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) prcp = stations[i].getPrcp();
			else prcp = Float.parseFloat(str);
			stations[i].setPrcp(prcp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) sndp = stations[i].getSndp();
			else sndp = Float.parseFloat(str);
			stations[i].setSndp(sndp);
			
			while ((c = xml.charAt(charIndex)) != '<') {
				frshtt += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) frshtt = stations[i].getFrshtt();
			stations[i].setFrshtt(frshtt);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) cldc = stations[i].getCldc();
			else cldc = Float.parseFloat(str);
			stations[i].setCldc(cldc);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) wnddir = stations[i].getWnddir();
			else wnddir = Integer.parseInt(str);
			stations[i].setWnddir(wnddir);
			j = 0;
			
			inserts += "(" + stn + ", '" + date + "', '" + time + "', " + temp + ", " + dewp + ", " + stp + ", " + slp + ", " + visib + ", " + wdsp + ", " + prcp + ", " + sndp + ", '" + frshtt + "', " + cldc + ", " + wnddir + ")";
			if (i < 9) {
				inserts += ", ";
			}
		}
    	
		//Database.addInsert(inserts);
	
		//stopTime = System.nanoTime();
		//long speed = stopTime - startTime;
		//System.out.println(speed + "ns");
		
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
