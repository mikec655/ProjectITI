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
	private Station[] stations = new Station[10];
	private boolean alive;
	private String xml;
	private int charIndex;
	private int valueIndex;
	private static int[] charIndexSteps = {
		14, 15, 15, 15, 14, 13, 15, 16, 15, 15, 17, 17, 17, 45
	};

	public Client(int id, Socket socket) {
		this.socket = socket;
		for (int i = 0; i < 10; i++) {
			stations[i] = new Station();
		}
		alive = true;
		
		xml = new String();
		charIndex = 55;
		valueIndex = 0;
		
	    // Setting buffer sizes
		try {
			socket.setReceiveBufferSize(4194304);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()), 65536);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e2){
			e2.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		while (alive) {
			receiveXML();
			parseXML();
		}
	}
			
	private void receiveXML() {
		StringBuilder xmlBuilder = new StringBuilder(4000);
    	
    	// Read from socket until the end of XML file
    	while(true) {
    		try {
    			// Read XML file per lien 
				String line = input.readLine();
    			xmlBuilder.append(line);
    			if (line.equals("</WEATHERDATA>")) break;
    		} catch (IOException e) {
    			// Close socket and stop thread form running when Exception occurred
    			System.err.println(e);
    			close();
    			alive = false;
    		}
    	}

    	xml = xmlBuilder.toString();
	}
	
	private void parseXML() {
		// Set charIndex on first value of XML file
		charIndex = 55;
		
		// Parsing and correcting values from XML file
		for (int i = 0; i < 10; i++) {
			valueIndex = 0;
			ByteBuffer bytes = ByteBuffer.allocate(43);
			
			// Temporary variables
			String tmpString = new String();
			int tmpInt = 0;
			float tmpFloat = 0;
			short tmpShort = 0;
			
			// STN
			tmpString = readNext();
			tmpInt = Integer.parseInt(tmpString);
			stations[i].setStn(tmpInt);
			
			// DATE
			tmpString = readNext();
			stations[i].setDate(tmpString);
			
			// TIME (parsing date to int)
			tmpString = readNext();
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			try {
				tmpInt = (int) sdf.parse(tmpString).getTime();
			} catch (ParseException e) {
				System.out.println(socket.getPort());
				e.printStackTrace();
			}
			bytes.putInt(tmpInt);
			
			// TEMP (with correction if deviation is 20%)
			tmpString = readNext();
			tmpFloat = Float.parseFloat(tmpString);
			if (stations[i].sizeOfTemp() > 0) {
				float extrapolatedTemp = stations[i].extrapolateTemp();
				if (Math.abs(tmpFloat) > Math.abs(extrapolatedTemp) * 1.2) {
					tmpFloat = extrapolatedTemp ;
				} else if (Math.abs(tmpFloat) < Math.abs(extrapolatedTemp) * 0.8) {
					tmpFloat = extrapolatedTemp ;
				}
			}
			stations[i].addTemp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// DEWP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].extrapolateDewp(); 
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].addDewp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// STP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].extrapolateStp(); 
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].addStp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// SLP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].extrapolateSlp(); 
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].addSlp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// VISIB
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].getVisib(); 
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].setVisib(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// WDSP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].extrapolateWdsp();
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].addWdsp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// PRCP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].getPrcp();
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].setPrcp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// SNDP
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].getSndp();
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].setSndp(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// FRSHTT
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpInt = stations[i].getFrshtt();
			else tmpInt = Integer.parseInt(tmpString, 2);
			stations[i].setFrshtt(tmpInt);
			bytes.put((byte) (tmpInt));
			
			// CLDC
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpFloat = stations[i].getCldc();
			else tmpFloat = Float.parseFloat(tmpString);
			stations[i].setCldc(tmpFloat);
			bytes.putFloat(tmpFloat);
			
			// WNDDIR
			tmpString = readNext();
			if (tmpString.isEmpty()) tmpInt = stations[i].getWnddir();
			else tmpShort = Short.parseShort(tmpString);
			stations[i].setWnddir(tmpShort);
			bytes.putShort(tmpShort);

			// Add record to Station Object
			stations[i].addRecord(bytes.array());
			
		}
	}
	
	private String readNext() {
		// Searching for end of new value in XML file
		int offset = 0;
		while (xml.charAt(charIndex) != '<') {
			offset++;
			charIndex++;
		}
		// Reading the new value and return it
		String value = xml.substring(charIndex - offset, charIndex);
		charIndex += charIndexSteps[valueIndex++];
		return value;
	}
	
	
	private void close() {
		// Close the socket 
		try {
	    	input.close();
	        socket.close();
	    } catch (IOException e) {
	    	System.out.println(e);
	    } 
	}
}
