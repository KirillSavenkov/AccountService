import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Stack;
import java.io.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Server for serving clients.
 */

public class Server implements AccountService {

	/**
	 * Gets user's amount with some id from database.
	 * 
	 * @param id user id.
	 * @return user's amount.
	 */
	
	public Long getAmount(Integer id) {
		try {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery("SELECT amount FROM Amounts WHERE id=" + id);			
			if (res.next()){
				return (long) res.getLong("amount");
			}
			else {
				return (long) 0;
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getting amount");
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Increases user's amount by some value.
	 * 
	 * @param id user id.
	 * @param value value to be added to user's amount.
	 */
	
	public void addAmount(Integer id, Long value) {
		try {
			int updated = conn.createStatement().executeUpdate("UPDATE Amounts SET amount=amount+" + value + " WHERE id=" + id);
			if (updated == 0) {
				conn.createStatement().executeUpdate("INSERT INTO Amounts VALUES (" + id + ", " + value + ")");
			}
		}
		catch (Exception e) {
			System.out.println("Exception in adding to amount");
			System.out.println(e);
		}
	}
	
	/**
	 * Server statistics.
	 */
	
	public Statistics statistics = new Statistics();

	/**
	 *  Port only for waiting for new clients.
	 */
	
	public final int listeningPort = 4778;	
	
	/**
	 * Flag which makes server continue serving clients (true) or close (false).
	 */	
	
	public boolean toContinue = true;

	/**
	 * listening ServerSocket
	 */
	
	public ServerSocket ss;
	
	/**
	 * Set known connection as server parameter
	 * 
	 * @param toSetConn known connection to be set
	 */
	
	public void setConnection(Connection toSetConn) {
		conn = toSetConn;
	}
	
	/**
	 * Number of threads except of main listening thread and statistics thread.
	 */
	
	public static int threadsNumber = 100;
	
	public int cacheMaximumSize = 10000;
	
	/**
	 * Server cache.
	 */
	
	private LoadingCache<Integer, Long> cache = CacheBuilder.newBuilder()
			.maximumSize(this.cacheMaximumSize)
			.build(
				new CacheLoader<Integer, Long>() {
					public Long load(Integer key) {
						return getAmount(key);
					}
				}
			);
	
	/**
	 * Class of client serving threads.
	 */
	
	private class ServingThread implements Runnable {
		
		/**
		 * Working port.
		 */
		
		public int port;
		
		/**
		 * Initiates server socket on known port and initiates working thread.
		 * 
		 * @param port known port.
		 */
		
		public ServingThread(int port) {
			try {
				this.ss = new ServerSocket(port);
				this.port = port;
				this.thread = new Thread(this);
			} catch (Exception e) {
				System.out.println("Exception in ServingThread constructor");
				System.out.println(e);
			}
		}

		/**
		 * Initiates Socket and serves client's request. Closes Socket and pushes itself to a vacant stack after sending an answer.
		 */
		
		public void run() {
			while (true) {
				// Waits for a signal to start serving.
					
				synchronized (this) {
					this.wait();
				}
					
				// Generates Socket and reads a request.
				
				try{
							
					this.socket = this.ss.accept();
					
					InputStream sin = this.socket.getInputStream();
					OutputStream sout = this.socket.getOutputStream();
					DataInputStream in = new DataInputStream(sin);
					DataOutputStream out = new DataOutputStream(sout);
					Command command = new Command();
					command.scanCommand(in.readUTF());
					
					// Handles request.
										
					if (command.getType() == 1) {  // add command.
						int id = command.getId();
						Long value = command.getValue();
						
						// Modifies cache data, loads it if necessary.
						
						synchronized (_LOCK) {	
							Long oldAmount = cache.get(id);
							cache.put(id, oldAmount + value);
						}
												
						// Modifies database.
						
						addAmount(id, value);
											
						// Sends an answer.
						
						out.writeUTF(value + " was added to account " + id);
						out.flush();
						statistics.addQuery();
						this.socket.close();
						
						// Pushes itself to the stack of vacant threads.
						
						vacantThreads.push(this);
					}
					if (command.getType() == 2) {  // get command.
						int id = command.getId();
						Long res = cache.get(id);
						out.writeUTF("Account " + id + " has " + res);
						out.flush();
						statistics.getQuery();
						this.socket.close();
						vacantThreads.push(this);
					}
					if (command.getType() == 3) {  // drop statistics command.
						statistics.dropStatistics();
						out.writeUTF("Statistic has been dropped.");
						out.flush();
						this.socket.close();
						vacantThreads.push(this);
					}
				}
				catch (Exception e) {
					System.out.println("Exception in one server's thread.");
					System.out.println(e);
					vacantThreads.push(this);
				}
			}
		}
		
		/**
		 * Working thread.
		 */
		
		private Thread thread;
	
		/**
		 * Closes server socket of thread.
		 */
		
		public void close() {
			try {
				this.ss.close();
			}
			catch (Exception e) {
				System.out.println("Exception in closing of serving thread");
				System.out.println(e);
			}
		}

		/**
		 * Listening server socket.
		 */
		
		private ServerSocket ss = null;
		
		/**
		 * Connection socket.
		 */
		
		private Socket socket = null;
	}
	
	/**
	 * Stack of vacant serving threads.
	*/
	
	private Stack<ServingThread> vacantThreads;
	
	/**
	* Initiates stack of vacant serving threads.
	*/
	
	public void initVacantPorts() {
		this.vacantThreads = new Stack<ServingThread>();
		int counter = 0;  // Number of serving threads which were pushed.
		int i = 2000;
		while ((i < 60000) && (counter < Server.threadsNumber)) {
			if (checkPort(i)){
				ServingThread r = new ServingThread(i);
				this.vacantThreads.push(r);
				r.thread.start();
				counter++;
			}
			i++;
		}
	}
	
	/**
	 * Closes ServerSocket.
	 */
	
	public void closeServerSocket() {
		try {
			this.ss.close();
		} catch (Exception e) {
			System.out.println("Exception in server socket closing.");
			System.out.println(e);
		}
	}
	
	/**
	 * Waits for a client on listening port. Asks client to connect using another port. Closes old Socket.
	 * In case of no vacant ServingThreads asks client to wait.
	 * Matches ServingThread which will serve this client and remembers it in according field of Server.
	 */
	
	public void startListening() {
		try {
			Socket socket = this.ss.accept();
			OutputStream sout = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(sout);
			
			// Case of no vacant ServingThreads.
			
			if (this.vacantThreads.empty()) {
				this.toBeUsed = null;
				out.writeInt(0);
				out.flush();
				socket.close();
				return;
			}
			
			// Obtains ServingThread which will serve current client.
			
			this.toBeUsed = this.vacantThreads.pop();
			
			// Asks client to connect using a new port.
			
			out.writeInt(this.toBeUsed.port);
			out.flush();
			socket.close();
		}
		catch (IOException e) {
			System.out.println("Exception in listening of client.");
			System.out.println(e);
		}
	}
	
	/**
	 * Makes serving thread serve client.
	 * Does nothing in case of emptiness of vacant threads' stack.
	 */
	
	public void serveClient() {
		if (this.toBeUsed == null) {
			return;
		}
		synchronized (toBeUsed) {
			this.toBeUsed.notifyAll();
		}
	}
		
	/**
	 * Closes all serving threads.
	 */
	
	public void closeThreads() {
		try {
			int threadsClosed = 0;
			while (threadsClosed < Server.threadsNumber) {
				if (this.vacantThreads.empty() == false) {
					this.vacantThreads.pop().close();
					threadsClosed++;
				}
				else {
					Thread.sleep(100);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Exception in threads closing.");
			System.out.println(e);
		}
	}
	
	/**
	 * Object which locks thread. Used only by synchronized instructions.
	 */
	
	private static final Object _LOCK = new Object();	
	
	/**
	 * Serving thread to be started in a current moment.
	 */
	
	private ServingThread toBeUsed;
	
	/**
	 * Connection with database.
	 */
	
	private Connection conn = null;
		
	/**
	 * Checks if current port is available.
	 * 
	 * @param port number of port to be checked.
	 * @return true or false.
	 */
	
	private static boolean checkPort(int port) {
		try {
			ServerSocket tester = new ServerSocket(port);
			tester.close();
		}
		catch (BindException be) {
			return false;
		}
		catch (IOException e) {
			System.out.println("Exception in checking ports.");
			System.out.println(e);
		}
		return true;
	}
}