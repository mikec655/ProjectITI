import java.io.BufferedReader;
import java.io.FileOutputStream;
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
    	//System.out.println(xml);
		int charIndex = 55;
		int[] charIndexSteps = {14, 15, 15, 15, 14, 13, 15, 16, 15, 15, 17, 17, 17, 45};
		char c;
		
    	byte[] bytes = new byte[43];
    	
    	int j = 0;
		for (int i = 0; i < 10; i++) {
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
//			bytes[0] = (byte) (stn >> 24);
//			bytes[1] = (byte) (stn >> 16);
//			bytes[2] = (byte) (stn >> 8);
//			bytes[3] = (byte) (stn);

			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				date += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			try {
				time = (int) sdf.parse(str).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			bytes[0] = (byte) (time >> 24);
			bytes[1] = (byte) (time >> 16);
			bytes[2] = (byte) (time >> 8);
			bytes[3] = (byte) (time);
			
//			System.out.println(bytes[0]);
//			System.out.println(bytes[1]);
//			System.out.println(bytes[2]);
//			System.out.println(bytes[3]);
			
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
					//System.out.println("Wrong value -> " + temp);
				} else if (Math.abs(temp) < Math.abs(extrapolatedTemp) * 0.8) {
					temp = extrapolatedTemp * 0.8f;
					//System.out.println("Wrong value -> " + temp);
				}
			}
			stations[i].addTemp(temp);
			addFloatToByteArray(bytes, 4, temp);
			
//			System.out.println(temp);
//			System.out.println(bytes[4]);
//			System.out.println(bytes[5]);
//			System.out.println(bytes[6]);
//			System.out.println(bytes[7]);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) dewp = stations[i].extrapolateDewp(); 
			else dewp = Float.parseFloat(str);
			stations[i].addDewp(dewp);
			addFloatToByteArray(bytes, 8, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) stp = stations[i].extrapolateStp(); 
			else stp = Float.parseFloat(str);
			stations[i].addStp(stp);
			addFloatToByteArray(bytes, 12, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) slp = stations[i].extrapolateSlp(); 
			else slp = Float.parseFloat(str);
			stations[i].addSlp(slp);
			addFloatToByteArray(bytes, 16, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) visib = stations[i].getVisib(); 
			else visib = Float.parseFloat(str);
			stations[i].setVisib(visib);
			addFloatToByteArray(bytes, 20, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) wdsp = stations[i].extrapolateWdsp();
			else wdsp = Float.parseFloat(str);
			stations[i].addWdsp(wdsp);
			addFloatToByteArray(bytes, 24, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) prcp = stations[i].getPrcp();
			else prcp = Float.parseFloat(str);
			stations[i].setPrcp(prcp);
			addFloatToByteArray(bytes, 28, temp);
			
			str = "";
			while ((c = xml.charAt(charIndex)) != '<') {
				str += c;
				charIndex++;
			}
			charIndex += charIndexSteps[j++];
			if (str.isEmpty()) sndp = stations[i].getSndp();
			else sndp = Float.parseFloat(str);
			stations[i].setSndp(sndp);
			addFloatToByteArray(bytes, 32, temp);
			
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
			
			
//			try (FileOutputStream fos = new FileOutputStream(stn + ".dat")) {
//				fos.write(bytes);
//			} catch (IOException e) {
//				System.out.println(e);
//			}
//			
//			System.out.println(stn);
//			System.out.println(date);
//			System.out.println(time);
//			System.out.println(temp);
//			System.out.println(dewp);
//			System.out.println(stp);
//			System.out.println(slp);
//			System.out.println(visib);
//			System.out.println(prcp);
//			System.out.println(sndp);
//			System.out.println(frshtt);
//			System.out.println(cldc);
//			System.out.println(wnddir);
		}
	
		long stopTime = System.currentTimeMillis();
		long speed = stopTime - startTime;
		System.out.println(speed + "ms");
		
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
