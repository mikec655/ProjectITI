import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    			System.err.println(e + "X");
    			close();
    		}
    	}
    	
    	long startTime = System.nanoTime();
		int charIndex = 55;
		int[] charIndexSteps = {14, 15, 15, 15, 14, 13, 15, 16, 15, 15, 17, 17, 17, 45};
		
    	
    	int j = 0;
		for (int i = 0; i < 10; i++) {
			char c;
			byte[] bytes = new byte[43];
			String str = new String();
			int num = 0;
			float f = 0;
			int strIndex =0;
			
			while ((c = xml.charAt(charIndex)) != '<') {
				//str = str.concat(c);
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			
			//System.out.println(str);
			charIndex += charIndexSteps[j++];
			num = Integer.parseInt(str);
			stations[i].setStn(num);

			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				//str += c;
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			stations[i].setDate(str);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			try {
				num = (int) sdf.parse(str).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			bytes[0] = (byte) (num >> 24);
			bytes[1] = (byte) (num >> 16);
			bytes[2] = (byte) (num >> 8);
			bytes[3] = (byte) (num);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			f = Float.parseFloat(str);
			if (stations[i].sizeOfTemp() > 0) {
				float extrapolatedTemp = stations[i].extrapolateTemp();
				if (Math.abs(f) > Math.abs(extrapolatedTemp) * 1.2) {
					f = extrapolatedTemp ;
				} else if (Math.abs(f) < Math.abs(extrapolatedTemp) * 0.8) {
					f = extrapolatedTemp ;
				}
			}
			stations[i].addTemp(f);
			addFloatToByteArray(bytes, 4, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].extrapolateDewp(); 
			else f = Float.parseFloat(str);
			stations[i].addDewp(f);
			addFloatToByteArray(bytes, 8, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].extrapolateStp(); 
			else f = Float.parseFloat(str);
			stations[i].addStp(f);
			addFloatToByteArray(bytes, 12, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].extrapolateSlp(); 
			else f = Float.parseFloat(str);
			stations[i].addSlp(f);
			addFloatToByteArray(bytes, 16, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].getVisib(); 
			else f = Float.parseFloat(str);
			stations[i].setVisib(f);
			addFloatToByteArray(bytes, 20, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].extrapolateWdsp();
			else f = Float.parseFloat(str);
			stations[i].addWdsp(f);
			addFloatToByteArray(bytes, 24, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].getPrcp();
			else f = Float.parseFloat(str);
			stations[i].setPrcp(f);
			addFloatToByteArray(bytes, 28, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].getSndp();
			else f = Float.parseFloat(str);
			stations[i].setSndp(f);
			addFloatToByteArray(bytes, 32, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) num = stations[i].getFrshtt();
			else num = Integer.parseInt(str, 2);
			stations[i].setFrshtt(num);
			bytes[36] = (byte) (num);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) f = stations[i].getCldc();
			else f = Float.parseFloat(str);
			stations[i].setCldc(f);
			addFloatToByteArray(bytes, 37, f);
			
			strIndex = 0;
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				strIndex ++;
				charIndex++;
			}
			str = xml.substring(charIndex-strIndex, charIndex);
			
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) num = stations[i].getWnddir();
			else num = Integer.parseInt(str);
			stations[i].setWnddir(num);
			bytes[41] = (byte) (num >> 8);
			bytes[42] = (byte) num;
			j = 0;
			
			stations[i].addRecord(bytes);
			
		}
		
		long stopTime = System.nanoTime();
		long speed = stopTime - startTime;
		if (speed > 1000000) {
			System.out.println("Reading XML took " + speed + "ms");
		}
		
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
