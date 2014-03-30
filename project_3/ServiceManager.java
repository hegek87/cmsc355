import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;
import java.util.Scanner;
import java.net.Socket;

/********************************************************************
*                       Service Manager Module
* Starts a server, which, upon a client connecting, spawns a User se-
* ssion for the client in a seperate thread. 
*********************************************************************
* Input: None
* Output: User Session
*********************************************************************
* Maintenance Log
*********************************************************************
* FIX001 03/30/14 Kenny Hegeland
* Fixed the server to properly handle multiple clients
* FIX002 03/30/14 Kenny Hegeland
* Allowed the server to be shut down.
* FIX003 03/30/14 Kenny Hegeland
* Properly handle bad input.
********************************************************************/

/********************************************************************
* 							ServiceManager class
* Allows multiple users to connect to the server, producing a new us-
* er session for each user that connects.
********************************************************************/
public class ServiceManager{
	private ServerSocket server;	
	public static final int port = 8080;
	
	public ServiceManager(){}
	
	public void closeServer() {
		try{
			server.close();
		} catch(SocketException se){
			System.out.println("Server shutdown");
		} catch(IOException ioe){
			ioe.printStackTrace();
		} 
	}
	
	/****************************************************************
	* Opens a server socker and continually waits for connections. W-
	* hen a new connection is made, we start a new user session thre-
	* ad to handle to client.
	****************************************************************/
	public void openConnections() throws 	UnknownHostException,
											IOException{
		this.server = new ServerSocket(port);
		// Wait for connections
		while(true){
			// Handle server kill
			Thread admin = new Thread(new Runnable(){
				public void run(){
					Scanner in = new Scanner(System.in);
					String message = in.nextLine();
					if(message.equals("kill()")){
						ServiceManager.this.closeServer();
						System.exit(0);
					}
				}
			});
			admin.start();
			// Handle connections
			Socket client = this.server.accept();
			Thread t = new Thread(new UserSession(client));
			t.start();
		}
	}
	
	// Get everything started
	public static void go(){
		ServiceManager sm = new ServiceManager();
		try{
			sm.openConnections();
		} catch(SocketException se){
			System.out.println("Server shutdown");
		} catch(UnknownHostException uhe){
			uhe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		go();
	}
}		
