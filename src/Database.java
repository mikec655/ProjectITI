import java.sql.*;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Database {
	
	private static Connection connection;
	private static ArrayBlockingQueue<String> inserts = new ArrayBlockingQueue<String>(16000);
	
	public static void connect() {
		try {
			// verbinding opzetten
			// connection = DriverManager.getConnection("jdbc:mysql://62.131.183.80:5000/unwdmi?serverTimezone=CET", "kakidioot", "ProjectITI3306");
			connection = DriverManager.getConnection("jdbc:mysql://145.37.168.204/unwdmi?serverTimezone=CET", "gerben", "361273gerben");
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	public static void addInsert(String insert) {
		inserts.add(insert);
	}
	
	public static void executeQuery() {
		if (inserts.size() == 0) return;
		long startTime = System.nanoTime();
		String query = "INSERT INTO measurement (stn, date, time, temp, dewp, stp, slp, visib, wdsp, prcp, sndp, frshtt, cldc, wnddir) VALUES ";
		int end = inserts.size();
		for (int i = 0; i < end; i++) {
			if (i == end - 1) {
				query += inserts.poll() + ";";
			} else {
				query += inserts.poll() + ", ";
			}
		}
		Statement stmt;
		try {
			stmt = connection.createStatement();
			long stopTime = System.nanoTime();
			long time = stopTime - startTime;
			System.out.println("Database inserted " + stmt.executeUpdate(query) + " rows in " + time + "ns");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void executeTestQuery(String query) {
		// Schrijft alle data uit stations tabel naar console
		try (Statement stmt = connection.createStatement();
			    ResultSet rs = stmt.executeQuery( "SELECT * FROM stations" )
			) {
				int c = 0;
			    while ( rs.next() ) {
			        int numColumns = rs.getMetaData().getColumnCount();
			        String row = new String();
			        for ( int i = 1 ; i <= numColumns ; i++ ) {
			           row += rs.getObject(i);
			           row += " ";
			        }
			        System.out.println(row);
			        c++;
			    }
			    System.out.println(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

}
