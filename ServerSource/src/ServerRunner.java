import java.io.File;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.*;


/**
 * Runner of server. Initiates server and configurates it. Call serving methods.
 */

public class ServerRunner {
	
	/**
	 * Generates server, starts searching for a client and serves it after.
	 * 
	 * @param args console input.
	 */
	
	public static void main(String[] args) {
		Server server = configServer();					 
		try {
			while (server.toContinue) {
				server.startListening();
				server.serveClient();
			}
		}
		catch (Exception e) {
			System.out.println(e);
			server.closeServerSocket();
			return;
		}
		finally {
			server.closeServerSocket();
			server.closeThreads();
		}
	}
	
	/**
	 * Creates a Server variable, configurates it and prepare for work.
	 * 
	 * @return server variable with set connection with database and vacant ports.
	 */
	
	private static Server configServer() {
		try {
			
			// Sets Scanner from configuration file.
			
			Scanner in = new Scanner(new File("Config.txt"));
			
			// Scans nonempty username.
			
			String userName = in.nextLine().split(" ")[2];
			
			// Scans possible empty password.
			
			String password;
			try {
				password = in.nextLine().split(" ")[2];
			}
			catch (ArrayIndexOutOfBoundsException e) {
				password = "";
			}
			
			// Scans nonempty url.
			
			String url = in.nextLine().split(" ")[2];
			
			// Scans nonempty database name
			
			String databaseName = in.nextLine().split(" ")[2];
			
			// Initiates JDBC driver.
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// Initiates server.
			
			Server res = new Server();
			
			// Scans and sets number of working threads. 
			
			Server.threadsNumber = Integer.parseInt(in.nextLine().split(" ")[4]);
			
			// Scans and sets maximum cache size.
			
			res.cacheMaximumSize = Integer.parseInt(in.nextLine().split(" ")[3]);
			
			// Creates database if it doesn't exist.
			
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + url + "/", userName, password);
			Statement statement;
			statement = conn.createStatement();
			try {
				statement.executeUpdate("CREATE DATABASE " + databaseName);
			}
			catch (SQLException e){
				// Database exists. Do nothing.
			}
			finally {
				statement.close();
				conn.close();
			}
			
			// Sets connection with database.
			conn = DriverManager.getConnection("jdbc:mysql://" + url + "/" + databaseName, userName, password);
			res.setConnection(conn);
			
			// Creates table 'amounts'.
			
			try {
				statement = conn.createStatement();
				String sqlQuery = "CREATE TABLE amounts" +
								  "(id INTEGER UNIQUE PRIMARY KEY, " + 
								  "amount LONG)";
				statement.executeUpdate(sqlQuery);
			}
			catch (SQLException e) {
				//Table exists. Do nothing.
			}
			finally {
				statement.close();
			}
			
			// Sets listening server socket.
			
			res.ss = new ServerSocket(res.listeningPort, 1000000);
			
			// Generates stack of vacant serving threads.
			
			res.initVacantPorts();
			
			System.out.println("Server is ready to serve clients.");
			in.close();
			return res;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
