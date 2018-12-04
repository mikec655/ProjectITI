import java.sql.*;

public class Database {
	
	private Connection connection;
	
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/unwdmi?serverTimezone=CET", "root", "root");
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	public void executeQuery(String query) {
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
