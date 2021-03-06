
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
  
public abstract class Server 
{ 
	public static DataSender dataSender;
    private static final int PORT = 7789;
    private static final int MAX_CLIENTS = 800;
	private static int clientCounter = 0;
	private static ServerSocket server;
	private static ExecutorService executor;
  
    public static void main(String args[]) 
    { 
    	executor = Executors.newFixedThreadPool(MAX_CLIENTS);
    	
    	dataSender = new DataSender();
    	Thread sendThread = new Thread(dataSender);
    	sendThread.start();

        Socket conn; // Client socket
		try {
			// Server socket listening for new clients
			server = new ServerSocket(PORT);
			System.out.println("Server Started, Waiting for clients.");
			
			while (true) {
				// Accept new clients
				if (clientCounter <= MAX_CLIENTS) {
					conn = server.accept();		
					// Make new client an start a new thread
					Client client = new Client(clientCounter, conn);
					executor.execute(client);
					// Increase the client count and prints it to the socket
					clientCounter++;
					System.out.println("New client accepted. client count: " + clientCounter);
				} else {
					// If max clients is reached now new clients are accepted  
					System.out.println("Client not accepted. client count is already " + MAX_CLIENTS);
				}
			}
		}
		catch (java.io.IOException e) { 
			System.out.println(e);
		}        
    } 
} 