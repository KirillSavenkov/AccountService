import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for statistics collected from server.
 */

public class Statistics  implements Runnable{
	
	/**
	 * Number of "get" requests handled for all time.
	 */
	
	private int totalGetNumber = 0;
	
	/**
	 * Number of "add" requests handled for all time.
	 */
	
	private int totalAddNumber = 0;
	
	/**
	 * Number of "get" requests handled for the last period.
	 */
	
	private int getsPerTime = 0;
	
	/**
	 * Number of "add" requests handled for the last period.
	 */
	
	private int addsPerTime = 0;
	
	/**
	 * Generates thread and makes it start to compute statistics.
	 */
	
	public Statistics() {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Object for locking threads. Used only with synchronized instruction.
	 */
	
	private Object _LOCK = new Object();
	
	/**
	 * Increases counters of "get" requests.
	 */
	
	public void getQuery() {
		synchronized (_LOCK) {
			totalGetNumber++;
			getsPerTime++;
		}
	}
	
	/**
	 * Increases counters of "add" requests.
	 */
	
	public void addQuery() {
		synchronized (_LOCK) {
			totalAddNumber++;
			addsPerTime++;
		}
	}
	
	/**
	 * Logging period of statistics in minutes.
	 */
	
	private static int printRegularity = 10;
	
	/**
	 * Periodically prints statistics.
	 */
	
	public void run() {
		try {
			while (true) {
				this.print();
				try {
					Thread.sleep(60000*Statistics.printRegularity);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Drops statistics and clears log.
	 */
	
	public void dropStatistics() {
		this.totalGetNumber = 0;
		this.totalAddNumber = 0;
		this.addsPerTime = 0;
		this.getsPerTime = 0;
		try {
			this.out = new PrintStream(new BufferedOutputStream(new FileOutputStream("Statistics.txt", false)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println();
		out.println();
		Date now = new Date();
		DateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
		String s = formatter.format(now);
		out.println("STATISTICS HAS BEEN DROPPED. " + s);
		out.close();
	}
	
	/**
	 * Printstream for statistics log.
	 */
	
	private PrintStream out;
	
	/**
	 * Prints statistics.
	 */
	
	private void print() {
		try {
			this.out = new PrintStream(new BufferedOutputStream(new FileOutputStream("Statistics.txt", true)));
			out.println("Statistics for " + Statistics.printRegularity + " minutes period:");
			out.println("Add requests handled: " + addsPerTime);
			out.println("Get requests handled: " + getsPerTime);
			out.println("Add requests handled total: " + totalAddNumber);
			out.println("Get requests handled total: " + totalGetNumber);
			Date now = new Date();
			DateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
			String s = formatter.format(now);
			out.println(s);
			out.println("____________________________________________");
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		getsPerTime = 0;
		addsPerTime = 0;
	}
}
