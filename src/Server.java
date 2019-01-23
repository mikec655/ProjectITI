
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit; 
  
public abstract class Server 
{ 
    private static final int PORT = 11000;
    private static final int MAX_CLIENTS = 800;
	private static int clientCounter = 0;
	private static ScheduledExecutorService executor;
	private static ServerSocket server;
  
    public static void main(String args[]) 
    { 
    	new File("data").mkdir();
    	
    	// Aanmaken van een Threadpool met Scheduler
    	executor = Executors.newScheduledThreadPool(MAX_CLIENTS);

        Socket connection; // Client socket
		try {
			// Server socket die luistert naar nieuwe clients
			server = new ServerSocket(PORT);
			System.out.println("Server Started, Waiting for clients.");
			
			while (true) {
				// Accepteren van nieuwe clients
				connection = server.accept();		
				// Client in Thread stoppen
				Thread client = new Thread(new Client(connection));
				clientCounter++;
				//Thread toevoegen aan Threadpool
				long delay = 1000 + clientCounter - (System.currentTimeMillis() % 1000);
				executor.scheduleAtFixedRate(client, delay, 1000, TimeUnit.MILLISECONDS);
				System.out.println("New client accepted. client count: " + clientCounter);
			}
		}

		catch (java.io.IOException e) { 
			System.out.println(e);
		}        
    } 
} 