import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Runner of test client. Initiates test client with parameters from console input and calls its working method.
 */
public class TestClientRunner {
	public static void main (String[] args) {
		Scanner in = null;
		try {
			in = new Scanner(new File("Test Client Config.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int minId = Integer.parseInt(in.nextLine().split(" ")[3]);
		int maxId = Integer.parseInt(in.nextLine().split(" ")[3]);
		int maxGet = Integer.parseInt(in.nextLine().split(" ")[4]);
		int maxAdd = Integer.parseInt(in.nextLine().split(" ")[4]);
		String address = in.nextLine().split(" ")[2];
		TestClient client = new TestClient(minId, maxId, maxGet, maxAdd, address);
		try {
			client.startWorking();
		}
		catch (Exception e) {
			System.out.println(e);
			return;
		}
	}
}
