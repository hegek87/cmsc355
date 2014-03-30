import java.net.ServerSocket;
import java.net.UnknownHostException;
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
						System.out.println("KILLING");
						ServiceManager.this.closeServer();
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
	
	public static void go(){
		ServiceManager sm = new ServiceManager();
		try{
			sm.openConnections();
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
