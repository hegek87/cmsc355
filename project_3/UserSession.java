import java.util.Scanner;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/********************************************************************
*						User Session class
* Runnable class which allows the user to communicate with the servic
* e broker. The user connection is given to this runnable, which open
* s streams to communicate with the user. The session is passed in se
* rvice information, calls the indicated session, and returns the res
* ult.
********************************************************************/
public class UserSession implements Runnable{
	public static final String path = "./";
	public static final String cmd = "java -jar ServiceBroker.jar ";
	private Runtime runner;
	private Socket client;
	
	private PrintWriter socketWrite;
	private BufferedReader socketRead;
	
	public UserSession(Socket client){
		this.client = client;
		this.runner = Runtime.getRuntime();
	}
	
	/*************************************************************
	* Opens streams for the currently running user session to talk
	* to the the client.
	*************************************************************/
	private void openStreams() throws	IOException,
										InterruptedException{
		this.socketWrite = 
			new PrintWriter(this.client.getOutputStream(), true);
			
		InputStreamReader isr = 
			new InputStreamReader(this.client.getInputStream());
		this.socketRead = new BufferedReader(isr);
	}
	
	// Done with streams and sockets - Close them
	private void closeStreams(){
		try{
			socketWrite.close();
			socketRead.close();
			client.close();
		} catch(SocketException se){
			se.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
				
	/*************************************************************
	* The overridden run method that anything implementing Runnab-
	* le must have. The method opens streams to the client, then 
	* it will continuosly read from the client, and pass the user 
	* input to the service broker
	*************************************************************/
	public void run(){
		try{
			this.openStreams();
			String serviceName;
			while(true){
				serviceName = this.socketRead.readLine();
				
				// Has user terminated the session?
				if(serviceName.equals("q")){ closeStreams(); return; }
				// Build process call string
				String pCall = cmd + serviceName;
				// Execute Service
				Process p = this.runner.exec(pCall);
			
				// Open stream to get output from service
				InputStream is = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
			
				// Wait for exec to finish
				p.waitFor();
			
				String result;
				// Read the service result, and send to client
				while((result = br.readLine()) != null){
					socketWrite.println(result);
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(InterruptedException ie){
			ie.printStackTrace();
		}
	}
}
