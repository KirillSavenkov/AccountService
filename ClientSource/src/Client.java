import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * Class of clients.
 */

public class Client {
	
	/**
	 * Reads configuration.
	 */
	
	public Client() {
		Scanner in = null;
		try {
			in = new Scanner(new File("Config.txt"));
			Client.ipAddress = InetAddress.getByName(in.nextLine().split(" ")[2]);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Port for first connecting with server.
	 */
	
	public static int listeningPort = 4778;
	
	/**
	 * Server address.
	 */
	
	public static InetAddress ipAddress = null;
	
	/**
	 * Command to be sent on server.
	 */
	
	private String command;
	
	/**
	 * Opens connection with server using address in String.
	 * 
	 * @param port port to connect.
	 * @param ipAddressString address.
	 * @return socket of connection.
	 */
	
	protected static Socket startTalking(int port){
		while (true) {
			try {
				return new Socket(Client.ipAddress, port);
			} catch (Exception e) {
				try {
					
					// Waits in case of impossibility to connect.
					
					Random randomizator = new Random();
					int wait = randomizator.nextInt(50);
					Thread.sleep(wait);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Reads a command from user's keyboard, sends it to server, prints answer and after all closes socket.
	 */
	
	public void startWorking() {
		try {
			
			// Scans command.
			
			System.out.println("Input a command. Command format look in readme.");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			this.command = br.readLine();
			
			// Checks command.
			
			while (check(this.command) == false) {
				this.command = br.readLine();
			}
			
			// Asks server on listening port to send working port.
			
			int newPort = 0;
			int waitingTime = 0;
			int wait = 1;
			while (newPort == 0) {
				
				// Waits if all serving threads of server are busy.
				
				if (waitingTime > 0) {
					Random randomizator = new Random();
					wait = randomizator.nextInt(50);
					Thread.sleep(wait);
				}
				
				// Connects with server to get a working port.
				
				try {
					Socket socket = startTalking(Client.listeningPort);
					InputStream sin = socket.getInputStream();
					DataInputStream in = new DataInputStream(sin);
					newPort = in.readInt();
					socket.close();
					waitingTime = waitingTime + wait;
				}
				
				// Waits in case of extremal business of server and refuse in connection to listening port.
				
				catch (Exception e) {
					Thread.sleep(wait);
				}
			}
		
			// Sends command to a server.
			
			Socket socket = startTalking(newPort);
			InputStream sin = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();
			DataInputStream in = new DataInputStream(sin);
			DataOutputStream out = new DataOutputStream(sout);
			out.writeUTF(this.command);
			out.flush();
			
			// Gets an answer.
			
			String answer = in.readUTF();
			System.out.println(answer);
			socket.close();
		}
		catch (Exception e) {
			System.out.println("Exception in client working.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if input command is correct.
	 * 
	 * @param command input command.
	 * @return true is correct, false is incorrect.
	 */
	
	private static boolean check(String command) {
		String[] params = command.split(" ");
		if (params[0].toLowerCase().equals("add")) {
			try {
				Integer.parseInt(params[1]);
				Long.parseLong(params[2]);
			}
			catch (Exception e) {
				return false;
			}
			return true;
		}
		if (params[0].toLowerCase().equals("get")) {
			try {
				Integer.parseInt(params[1]);
			}
			catch (Exception e) {
				return false;
			}
			return true;
		}
		if (params[0].toLowerCase().equals("drop")) {
			return true;
		}
		return false;
	}
	
}
