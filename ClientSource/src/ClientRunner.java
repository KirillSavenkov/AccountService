/**
 * Runner of client. Initiates client variable and calls working method.
 */

public class ClientRunner {
	
	/**
	 * Generates client variable and makes client begin working.
	 * 
	 * @param args console input.
	 */
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.startWorking();
		}
		catch (Exception e) {
			System.out.println(e);
			return;
		}
	}
	

}
