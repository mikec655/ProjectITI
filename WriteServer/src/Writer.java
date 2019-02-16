import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Writer implements Runnable {
	
	private Socket socket;
	private static final String FILE_PATH = "/var/www/html/herocycles/public/data/";
	//private static final String FILE_PATH = "C:/xampp/htdocs/ProjectITIWebApp/public/data/";
	
	public Writer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			long startTime = System.currentTimeMillis();
			
			// Getting data
			InputStream input = socket.getInputStream();
			byte[] data = new byte[14 + 43 * 60];
			input.read(data);
			String date = new String(data, 0, 10, "UTF-8");
			int stn = ByteBuffer.wrap(data, 10, 14).getInt();
			
			// Creating file
			File f = new File(FILE_PATH + date); 
			f.mkdirs();
			f = new File(FILE_PATH + date + "/" + stn + ".dat");
			f.createNewFile();
			
			// Writing data
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(data, 14, 43 * 60);
			fos.close();
			
			// Time measurement
			long stopTime = System.currentTimeMillis();
			long writeTime = stopTime - startTime;
			System.out.println("writing file " + stn + " took: " + writeTime + "ms");
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
