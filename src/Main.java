import java.sql.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/unwdmi?serverTimezone=CET", "root", "root");
			try (Statement stmt = conn.createStatement();
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
				}
		} catch (Exception e){
			System.out.println(e);
		}
		
		
		
	}

}
