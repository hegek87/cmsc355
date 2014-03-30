import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client{
	public static final String host = "127.0.0.1";
	public static final int port = 8080;
	
	private PrintWriter sockWriter;
	private BufferedReader sockRead;
	private Scanner in;
	
	public Client(){}
	
	public void openConnections() throws 	UnknownHostException,
											IOException{
		Socket clientSock = new Socket(host, port);
		
		this.sockWriter = new PrintWriter(clientSock.getOutputStream(), true);
		InputStreamReader isr = 
			new InputStreamReader(clientSock.getInputStream());
		this.sockRead = new BufferedReader(isr);
		this.in = new Scanner(System.in);
	}
	
	public void go(){
		try{
			this.openConnections();
			while(true){
				System.out.print("> ");
				String message = this.in.nextLine();
				if(message.equals("q")){ return; }
				sockWriter.println(message);
				String result = this.sockRead.readLine();
				System.out.println(result);
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} 
	}
	
	public static void main(String[] args){
		Client c = new Client();
		c.go();
	}
}
