package chat_application_client_side;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


public class Client extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output; // client to server
	private ObjectInputStream input; //server to client
	private String message = "";
	private String serverIP; // IP ADRES OF THE server 
	private Socket connection;
	
	
	
	//constructor
	//client constructo is differen because its my machine i dont want 
	// it to be publicly accesible so 
	// it will take a ip adres to  connect to the server
	public Client (String host) {
		super ("Client!");
		serverIP = host;// IPADDRES OF THE SERVER
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow),BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
		
		 
	}
	
	//START RUNNING 
	// connect to server
	public void startRunning() {
		
		try {
			connectToServer(); // connects to one server with ip adres
			setupStreams(); // build streams 
			whileChatting(); // chatting back and forth
			
			
		}catch(EOFException eofException) {
			showMessage("\n Client terminated the connection");
			
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeStuff();
		}
	}
	
	
	//connect to the server
	private void connectToServer() throws IOException{
		showMessage("Attempting connection....\n");
		
		// 1 ip adres and port no of the app on server
		// this will try to connect to the server with that ip adres 
		// and the port number 
		connection = new Socket(InetAddress.getByName(serverIP),6789);
		// when connected 
		showMessage("Connected to :"+connection.getInetAddress().getHostName());
		
		
	}
	
	
	//set up streams to send and receive messages
	private void setupStreams() throws IOException{
		// stream built from client to the server sending data
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		// stream coming into client from server bringing in the data
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Dude your streams are now good to go !\n");

	}
	
	//while chatting with the server
	private void whileChatting() throws IOException{
		ableToType(true);
		do {
			try {
				message = (String)input.readObject();
				showMessage("\n"+message);
				
				
			}catch(ClassNotFoundException classNotfoundException) {
				showMessage("\n I dont know that object type");
			}
			
		}while(!message.equals("SERVER - END"));
	}
	
	
	// close the stremas and sockets 
	private void closeStuff() {
		showMessage("\n Closing crap down...");
		ableToType(false);
		try {
			
			output.close();
			input.close();
			connection.close();
			
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	// send messages to the server 
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - "+ message);
			output.flush();
			// gui part down here
			showMessage("\n CLIENT - "+ message);
		}catch(IOException ioException) {
			chatWindow.append("\n something messed uo sending message hoss!");
		}
	}
	
	
	//show message updates the gui
	//updates the chat window
	private void showMessage(final String m) {
		// updates the parts of the gui 
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(m);
					}
				}
				
				
				);
		
		
	}
	
	//allow user to type at times
	//let the user to type stuff in there box
	private void ableToType(final boolean tof) {
		//since we are updating the portion of the gui
		//continously so 
		
		
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
				
				
				);
		
		
		
	}
	

}
