package application.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is for connecting to SQLite. Singleton design pattern.
 */
public class sqliteConnection {
	
	private static sqliteConnection singleCon = new sqliteConnection();
	private Connection con = null;
	
	/**
	 * Open connection when instance created and close connection when application closes.
	 */
	private sqliteConnection() {
		connect();
		Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));
	}
	
	private Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:DevMate.db");
			System.out.println("Connected");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e+"");
		}
		return con;
	}
	
	public static sqliteConnection getInstance() {
		return singleCon;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	private void closeConnection() {
		try {
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
