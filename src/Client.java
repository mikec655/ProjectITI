import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;


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
    			else xml += line;
    			if (line.equals("</WEATHERDATA>")) break;
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    	}
    	
    	long startTime = System.currentTimeMillis();
		int charIndex = 55;
		int[] charIndexSteps = {14, 15, 15, 15, 14, 13, 15, 16, 15, 15, 17, 17, 17, 45};
		char c;
    	
    	int j = 0;
		for (int i = 0; i < 10; i++) {
			byte[] bytes = new byte[43];
			String str = "";
			int stn = 0;
			String date = "";
			int time = 0;
			float temp = 0.0f;
			float dewp = 0.0f;
			float stp = 0.0f;
			float slp = 0.0f;
			float visib = 0.0f;
			float wdsp = 0.0f;
			float prcp = 0.0f;
			float sndp = 0.0f;
			int frshtt = 0;
			float cldc = 0.0f;
			int wnddir = 0;
	
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			stn = Integer.parseInt(str);
			stations[i].setStn(stn);

			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				date += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			stations[i].setDate(date);
			
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			try {
				time = (int) sdf.parse(str).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			bytes[0] = (byte) (time >> 24);
			bytes[1] = (byte) (time >> 16);
			bytes[2] = (byte) (time >> 8);
			bytes[3] = (byte) (time);
			
			str = "";
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
				} else if (Math.abs(temp) < Math.abs(extrapolatedTemp) * 0.8) {
					temp = extrapolatedTemp * 0.8f;
				}
			}
			stations[i].addTemp(temp);
			addFloatToByteArray(bytes, 4, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) dewp = stations[i].extrapolateDewp(); 
			else dewp = Float.parseFloat(str);
			stations[i].addDewp(dewp);
			addFloatToByteArray(bytes, 8, dewp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) stp = stations[i].extrapolateStp(); 
			else stp = Float.parseFloat(str);
			stations[i].addStp(stp);
			addFloatToByteArray(bytes, 12, stp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) slp = stations[i].extrapolateSlp(); 
			else slp = Float.parseFloat(str);
			stations[i].addSlp(slp);
			addFloatToByteArray(bytes, 16, slp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) visib = stations[i].getVisib(); 
			else visib = Float.parseFloat(str);
			stations[i].setVisib(visib);
			addFloatToByteArray(bytes, 20, visib);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) wdsp = stations[i].extrapolateWdsp();
			else wdsp = Float.parseFloat(str);
			stations[i].addWdsp(wdsp);
			addFloatToByteArray(bytes, 24, wdsp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) prcp = stations[i].getPrcp();
			else prcp = Float.parseFloat(str);
			stations[i].setPrcp(prcp);
			addFloatToByteArray(bytes, 28, prcp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) sndp = stations[i].getSndp();
			else sndp = Float.parseFloat(str);
			stations[i].setSndp(sndp);
			addFloatToByteArray(bytes, 32, sndp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) frshtt = stations[i].getFrshtt();
			else frshtt = Integer.parseInt(str, 2);
			stations[i].setFrshtt(frshtt);
			bytes[36] = (byte) (frshtt);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) cldc = stations[i].getCldc();
			else cldc = Float.parseFloat(str);
			stations[i].setCldc(cldc);
			addFloatToByteArray(bytes, 37, cldc);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) wnddir = stations[i].getWnddir();
			else wnddir = Integer.parseInt(str);
			stations[i].setWnddir(wnddir);
			bytes[41] = (byte) (wnddir >> 8);
			bytes[42] = (byte) wnddir;
			j = 0;
			
			stations[i].addRecord(bytes);
			
		}
	
		long stopTime = System.currentTimeMillis();
		long speed = stopTime - startTime;
		System.out.println("Reading XML took " + speed + "ms");
		
	}
	
	private static void addFloatToByteArray(byte[] array, int index, float var) {
		byte[] newBytes = ByteBuffer.allocate(4).putFloat(var).array();
		array[index + 0] = newBytes[0];
		array[index + 1] = newBytes[1];
		array[index + 2] = newBytes[2];
		array[index + 3] = newBytes[3];
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
