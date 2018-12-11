import java.sql.*;

public abstract class Database {
	
	private static Connection connection;
	
	public static void connect() {
		try {
			// verbinding opzetten
			connection = DriverManager.getConnection("jdbc:mysql://62.131.183.80:5000/unwdmi?serverTimezone=CET", "kakidioot", "ProjectITI3306");
			//connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/unwdmi?serverTimezone=CET", "gerben", "361273gerben");
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	public static void executeQuery(String query) {
		// TODO
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
