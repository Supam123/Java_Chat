package chat_application;

import java.io.*;
//server is a computer than anyone in the world can access
//kind of a public 
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// JFrame works like the main window where components like labels, buttons, textfields are added to create a GUI.

public class Server extends JFrame {
	
	private JTextField  userText;
	private JTextArea chatWindow;
	// output stream is message sent in  a stream away to your frnd computer
	// input stream is message snet to your pc in a stream from ur friend computer
	
	private ObjectOutputStream output; // sending messgaes away thru output stream
	private ObjectInputStream input; // receveing  messgaes in thru input stream
	private ServerSocket server; // on server connectin on clients
	private Socket connection; // connection  = Socket between two computer
	
	public Server() {
		super("Supam's Instant Messenger");
		userText = new JTextField();
		userText.setEditable(false); // bydefault before u connnect youa re not aallowed to type anything
		// cannot type
		//created an anonymouis class so that when anyone types what u want to happen
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						// we are getting the text from textfiedl
						// when we hit enter it sends 
						sendMessage(event.getActionCommand());
						// AFTER your message is sent we wanna clear the 
						// text field cuz when u send it goes away from ur text fiedl
						
						userText.setText("");
						
					}
					
				}
				
				
				);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add (new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);

		
	
	
	}
	//setup and run the server 
	public void startRunning() {
		try{
			server = new ServerSocket(6789,100); // its kind of an addres of the application located at the server
			// located at port number 6789 
			// backlog is how many pppl can wait to access the instant message
			// basically where is your application on server port number
			// and how many pppl can acces it is backlog number
			while (true) { // run forever 
				try {
					// connect and have conversation
					waitForConnection();// wait to be connected 
					setupStreams();// once connected build streams
					
					whileChatting();// allows us to chat back and forth
					
				}
				catch(EOFException eofException) { // this means end of the conncetion end of the server end of the stream end of the connection
					showMessage("\n Server ended the conncetion!! ");
				}finally {
					closeStuff();
				}
			}
			
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	
	
	

	//wait for connectionn, then display connection information
	private void waitForConnection () throws IOException{
		
		showMessage("Waiting for someone to connect....");
		// setup conncetion
		connection = server.accept();// accepts an conncetion to an server from this socker/conncetion
		// accepts a conncetion to build a conection between client and server
		
		showMessage("Now connected to " + connection.getInetAddress().getHostName());//the remote IP address to which this socket is connected and returns the IP ADDRESS
		
		// socket is kind of client connetce to the server 
		// we get socket ip and remote addresses 
	}
	
	
	//pathway of communication to and from
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		// output stream to send the data away 
		//Output stream is like a bus for a socket or a client 
		// which carries messages from the server to the socket 
		
		
		//SENDING DATA AWAY FROM OUR COMPUTER TO OTHER COMPUTER
		// sendin shit from server to cliebnt 
		output  = new ObjectOutputStream(connection.getOutputStream());
		//creating the pathway allows us to connect to other computer
		output.flush();//
		// RECEIEVONG DATA IN TO OUR COMPUTER FROM SOMEONE ELSE COMPUTER
		// data inot the server fromm the client
		input  = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now Setup! \n");
		 
	}
	
	// during the chat conversation
	private void  whileChatting() throws IOException{
		String message = "You are now Connected!";
		sendMessage(message);
		ableTotype(true);
		
		do {
			try { 
				//input is the input stream where they can send stuff to us
				// and we read the type f that input stream hopefully string 
				//otherwise that will be the error
				message = (String)input.readObject();
				// these are the messages sent to us by the user
				// every msg on the new lline 
				showMessage("\n"+message);
				
			}
			catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n idk wtf that user sent!");
			}
			
			//have conversation
			
			
		}while(!message.equals("CLIENT - END"));
		
		
		
		
		
	}
	
	
	
	
	
	
	

	
	
	
	
	
	
	// close streams and sockets after you are done chatting  
	private void closeStuff() {
		showMessage("\n Closing Connections......\n");
		ableTotype(false);
		try {
			// closing streams
			output.close();
			input.close();
			//socket = main conncetion between ur comp and there comp
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	// send message sends the message to the cliebt
	//SHOW MESSAGE JUST DISPLAYS THE messahes of both guys in the chat area
	
	
	
	// send a message to client (a computer connected to a server)
	
	private void sendMessage(String message) {
		try {
			// sends a message thru the output stream goes to the client
			output.writeObject("SERVER - "+message);// goes to client
			output.flush();//in case any bytes left over to the user nothing left
			showMessage("\nServer: "+ message); // shows up on ur side 
			
			
			
		}catch(IOException ioException) {
			//CHATWINDOW IS THE JTextArea
			chatWindow.append("\n ERROR: DUDE I CANT SEND THAT MESSAGE");
		}
		
	}
	
	
	//playing with chatwindow
	
	//updates the chat window
	private void showMessage(final String text) {
		// updates the parts of the gui 
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(text);
					}
				}
				
				
				);
		
		
	}
	
	
	//allow user to type at times
	//let the user to type stuff in there box
	private void ableTotype(final boolean tof) {
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
