package chat_application_client_side;
import javax.swing.*;

public class ClientTest {
	public static void main(String[] args) {
		Client client;
		 client = new Client("127.0.0.1"); // means local host  the server programme is at the same compuiter
		 client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 client.startRunning();
		 
		 
	}

}
