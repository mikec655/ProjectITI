import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
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
			socket.setReceiveBufferSize(4194304);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()), 65536);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e2){
			e2.printStackTrace();
		}
		for (int i = 0; i < 10; i++) {
			stations[i] = new Station();
		}
	}
	
	@Override
	public void run() {
		boolean alive = true;
		while (alive) {
			StringBuilder xmlBuilder = new StringBuilder(1400);
	    	
	    	// leest totdat einde van xml-bestand bereikt is
	    	while(true) {
	    		try {
	    			// inlezen van xml per lijn
					String line = input.readLine();
	    			xmlBuilder.append(line);
	    			if (line.equals("</WEATHERDATA>")) break;
	    		} catch (IOException e) {
	    			System.err.println(e);
	    			close();
	    			alive = false;
	    		}
	    	}
	
	    	String xml = xmlBuilder.toString();
			int charIndex = 55;
			int[] charIndexSteps = {14, 15, 15, 15, 14, 13, 15, 16, 15, 15, 17, 17, 17, 45};
	    	
	    	int j = 0;
			for (int i = 0; i < 10; i++) {
				char c;
				ByteBuffer bytes = ByteBuffer.allocate(43);
				String str = new String();
				int num = 0;
				float f = 0;
				short s = 0;
				int strIndex = 0;
				
				while ((c = xml.charAt(charIndex)) != '<') {
					strIndex ++;
					charIndex++;
				}
				str = xml.substring(charIndex-strIndex, charIndex);
				charIndex += charIndexSteps[j++];
				num = Integer.parseInt(str);
				stations[i].setStn(num);
	
				strIndex = 0;
				str = "";
				while ((c = xml.charAt(charIndex)) != '<') {
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
				if (str.isEmpty()) {
					str = stations[i].getDate();
				}
				DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				try {
					num = (int) sdf.parse(str).getTime();
				} catch (ParseException e) {
					System.out.println(socket.getPort());
					e.printStackTrace();
				}
				bytes.putInt(num);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.putFloat(f);
				
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
				bytes.put((byte) (num));
				
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
				bytes.putFloat(f);
				
				strIndex = 0;
				str = "";
				while ((c = xml.charAt(charIndex)) != '<') {
					strIndex ++;
					charIndex++;
				}
				str = xml.substring(charIndex-strIndex, charIndex);
				
				charIndex += charIndexSteps[j++];
				if (str.isEmpty()) num = stations[i].getWnddir();
				else s = Short.parseShort(str);
				stations[i].setWnddir(s);
				bytes.putShort(s);
				j = 0;
				
				stations[i].addRecord(bytes.array());
				
			}
		}
	}
	
	
	public void close() {
		// Sluiten van de socket bij Exception
		try {
	    	input.close();
	        socket.close();
	    } catch (IOException e) {
	    	System.out.println(e);
	    } 
	}
}
