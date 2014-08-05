import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

/**
 * Tests client class. Launches lots of requests concurrently.
 */

public class TestClient extends Client {
	
	/**
	 * Range of id on which current testClient can affect.
	 */
	
	public int[] idList;
	
	/**
	 * Number of possible "get" requests.
	 */
	
	public int rCount;
	
	/**
	 * Number of possible "add" requests.
	 */
	
	public int wCount;
	
	/**
	 * Number of times to repeat all operations.
	 */
	
	public int timesToDo;
	
	/**
	 * Sets fields.
	 * 
	 * @param minId to be set in idList[0].
	 * @param maxId to be set in idList[1].
	 * @param rCount to be set in rCount.
	 * @param wCount to be set in wCount.
	 */
	
	public TestClient(int minId, int maxId, int rCount, int wCount, String address) {
		this.idList = new int[2];
		this.idList[0] = minId;
		this.idList[1] = maxId;
		this.rCount = rCount;
		this.wCount = wCount;
		try {
			Client.ipAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runnable class for concurrent requests launching.
	 */
	
	private class TestingThread implements Runnable {
		
		/**
		 * Command to be sent to server.
		 */
		
		private String command;
		
		/**
		 * Constructor. Sets command field using given command.
		 * 
		 * @param command
		 */
		
		public TestingThread(String command) {
			this.command = command;
		}
		
		/**
		 * Sends request to server and prints an answer.
		 */
		
		public void run() {
			try {
				
				// Waits for a working port from server to connect.
				
				int newPort = 0;
				int waitTotal = 0;
				int wait = 1;
				while (newPort == 0) {
					
					// Waits in case of impossibility of server to serve request in the moment.
					
					if (waitTotal > 0) {
						Random randomizator = new Random();
						wait = randomizator.nextInt(100);
						Thread.sleep(wait);
					}
					waitTotal = waitTotal + wait;
					
					// Tries to get a working port.
					
					try {
						Socket socket = startTalking(listeningPort);
						InputStream sin = socket.getInputStream();
						DataInputStream in = new DataInputStream(sin);
						newPort = in.readInt();
					}
					
					// Waits in case of impossibility of server to answer (when listening port is extremely busy).
					
					catch (Exception e) {
						Thread.sleep(wait);
					}
				}
				
				// Connects to working port and sends request.
				
				Socket socket = startTalking(newPort);
				InputStream sin = socket.getInputStream();
				OutputStream sout = socket.getOutputStream();
				DataInputStream in = new DataInputStream(sin);
				DataOutputStream out = new DataOutputStream(sout);
				out.writeUTF(command);
				out.flush();
		
				// Gets an answer and prints it. Closes socket after.
				
				String answer = in.readUTF();			
				System.out.println(answer);
				socket.close();
			}
			catch (Exception e) {
				System.out.println("One of test client's threads working exception.");
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Reads a command for test client (not for one thread), launches all threads with generated requests.
	 */
	
	@Override
	public void startWorking() {
		try {
			
			// Inputs command.
			
			System.out.println("Input a command in a format: <type> <minId> <maxId> <number of repeats> <value(if neccessary)>");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String command = br.readLine();
			while (this.check(command) == false) {
				System.out.println("Wrong format or access denied. Try again.");
				command = br.readLine();
			}
			
			// Parses the command.
			
			String[] parsedCommand = command.split(" ");
			int minId = Integer.parseInt(parsedCommand[1]);
			int maxId = Integer.parseInt(parsedCommand[2]);
			int timesToDo = Integer.parseInt(parsedCommand[3]);
			Long value = null;
			if (parsedCommand[0].equals("add")){
				value = Long.parseLong(parsedCommand[4]);
			}
			
			// Starts all threads.
			
			for (int j = 0; j < timesToDo; j++) {
				for (int i = minId; i <= maxId; i++) {
					String oneCommand = parsedCommand[0] + " " + i;
					if (value != null) {
						oneCommand = oneCommand + " " + value;
					}
					Runnable r = new TestingThread(oneCommand);
					Thread t = new Thread(r);
					t.start();
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception in test client's start working");
			System.out.println(e);
		}
	}
	
	/**
	 * Checks if command is correct and all operations are allowed.
	 * 
	 * @param command input command.
	 * @return true if correct and false otherwise.
	 */
	
	private boolean check(String command) {
		
		// Checks the request format.
		
		if (command.equals("")) {
			return false;
		}
		String[] parsedCommand = command.split(" ");
		if ((parsedCommand[0].equals("add")) || (parsedCommand[0].equals("get"))) {
			int val;
			for (int i = 1; i <= 3; i++) {
				try {
					val = Integer.parseInt(parsedCommand[i]);
				}
				catch (Exception e) {
					return false;
				}
			}
			try {
				if (parsedCommand[0].equals("add")) {
					Long val4 = Long.parseLong(parsedCommand[4]);
				}
			}
			catch (Exception e) {
				return false;
			}
			
			// Checks11 access to this command (number of requests, id range).
			
			int minId = Integer.parseInt(parsedCommand[1]);
			int maxId = Integer.parseInt(parsedCommand[2]);
			int timesToDo = Integer.parseInt(parsedCommand[3]);
			if ((minId < this.idList[0]) || (maxId > this.idList[1])) {
				return false;
			}
			if (parsedCommand[0].equals("add")){
				if (maxId < minId) {
					return false;
				}
				if ((maxId - minId) * timesToDo > this.wCount) {
					return false;
				}
			}
			if (parsedCommand[0].equals("get")){
				if (maxId < minId) {
					return false;
				}
				if ((maxId - minId) * timesToDo > this.rCount) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
}
