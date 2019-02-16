import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
  
public abstract class Server 
{ 
    private static final int PORT = 3389;
	private static ServerSocket server;
	private static ExecutorService executor;
  
    public static void main(String args[]) 
    { 
    	executor = Executors.newFixedThreadPool(16);
        
    	Socket conn; // Client socket
		try {
			// Server socket listening for new clients
			server = new ServerSocket(PORT);
			System.out.println("Server Started");
			
			while (true) {
				// Accept new clients
				conn = server.accept();		
				// Make new writer an start a new thread
				Writer writer = new Writer(conn);
				executor.execute(writer);
				System.out.println("New writer thread created");
			}
		}
		catch (java.io.IOException e) { 
			System.out.println(e);
		}        
    } 
} 