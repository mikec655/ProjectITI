
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataSender implements Runnable{
	
	private BlockingQueue<byte[]> data = new ArrayBlockingQueue<byte[]>(800);
	private Socket socket;
	
	public void addRecord(byte[] record) {
		try {
			data.put(record);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		OutputStream output;
		try {
			while (true) {
				socket = new Socket("hcweather.ddns.net", 3389);
				//socket = new Socket("localhost", 3389);
				//socket.connect(new InetSocketAddress("hcweather.ddns.net", 3389));
				output = socket.getOutputStream();
				output.write(data.take());
				socket.close();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
