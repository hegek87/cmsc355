import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

/********************************************************************
*							Client class
* The client connects to our server, and communicates with the servic
* e that is called. The user can repeatedly specify a service to call
* and can exit by sending 'q' to the server.
********************************************************************/
public class Client{
	public static final String host = "127.0.0.1";
	public static final int port = 8080;
	
	private PrintWriter sockWriter;
	private BufferedReader sockRead;
	private Scanner in;
	
	public Client(){}
	
	/****************************************************************
	* Open streams to the server for the client to communicate with 
	* and make calls to the service broker.
	****************************************************************/
	public void openConnections() throws 	UnknownHostException,
											IOException{
		Socket clientSock = new Socket(host, port);
		
		this.sockWriter = new PrintWriter(clientSock.getOutputStream(), true);
		InputStreamReader isr = 
			new InputStreamReader(clientSock.getInputStream());
		this.sockRead = new BufferedReader(isr);
		this.in = new Scanner(System.in);
	}
	
	// Done with streams - close them
	public void closeConnections() throws IOException{
		this.sockWriter.close();
		this.sockRead.close();
		this.in.close();
	}
	
	// Start up the client
	public void go(){
		try{
			this.openConnections();
			/*************************************************
			* Continually get input, and send it to the server
			* where it is called by the service broker.
			*************************************************/
			while(true){
				System.out.print("> ");
				// Get input
				String message = this.in.nextLine();
				// Check if user terminates session
				if(message.equals("q")){ return; }
				// Send to server
				sockWriter.println(message);
				String result = this.sockRead.readLine();
				// Print result
				System.out.println(result);
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} finally{
			try{
				this.closeConnections();
			} catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		Client c = new Client();
		c.go();
	}
}
