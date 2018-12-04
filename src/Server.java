
// A Java program for a Server 
import java.net.*;
import java.io.*; 
  
public class Server 
{ 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
  
    // constructor with port 
    public Server(int port) 
    { 
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted"); 
  
            // takes input from the client socket 
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
  
            String text = ""; 
            boolean endOfFile = false;
            long startTime = 0;
            long stopTime = 0;
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("test.xml"));
            // reads message from client until "Over" is sent 
            while (!endOfFile) 
            { 
                try
                { 
                    
                	text = in.readUTF();
                	if (text != null) {
                		startTime = System.currentTimeMillis();
                	}
                    String[] lines = text.split("\\r?\\n");
                    for (int i = 0; i < lines.length; i++) {
                    	System.out.println(lines[i] + "*");
                    	writer.write(lines[i] + "\r\n");
                    	if (lines[i].equals("</WEATHERDATA>")){
                    		endOfFile = true;
                    		stopTime = System.currentTimeMillis();
                    		break;
                    	}
                    }
                    //System.out.println(text); 
  
                } 
                catch(IOException i) 
                { 
                    System.out.println(i); 
                } 
            } 
            long time = stopTime - startTime;
            System.out.println("Writing XML file took " + time + "ms");
            System.out.println("Closing connection"); 
            writer.close();
  
            // close connection 
            socket.close(); 
            in.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) 
    { 
        Database db = new Database();
        db.executeQuery("");
    	Server server = new Server(11000); 
    } 
} 