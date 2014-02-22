import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

/********************************************************************
*                          Translation Module
* Repeatedly prompts the user for input, translating the words to a
* foreign language.
*********************************************************************
* Input: String fileName
* Output: Writes a file containing all english words entered by the
* user and the translated words.
*********************************************************************
* Maintenance Log
*********************************************************************
* FIX001 01/24/14 Kenny Hegeland
* Allowing an optional dictionary file to be supplied
* FIX002 01/24/14 Kenny Hegeland
* Restructured the Translate class to not contain all logic in main
********************************************************************/

/*******************************************************************
*                            Translate Class
* Gets user input and passes it to the Dictionary module to translate
* it to a foreign word.
*********************************************************************
* Input: English word
* Output: Foreign word
********************************************************************/
public class Translate{
	public static final String cmd = "java -jar ";
	public static final String dict = "Dictionary.jar ";
	private Scanner in;
	private PrintWriter writer;
	private BufferedReader socketRead;
	private PrintWriter socketWrite;
	
	public static final String host = "127.0.0.1";
	public static final int port = 8080;
	
	public Translate(){
		// Open scanner to read user input
		this.in = new Scanner(System.in);
	}
	
	public void openStreams(){
		try{
			// Open writer for output file
			FileWriter fw = new FileWriter("output.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			this.writer = new PrintWriter(bw);
		
			// Open a socket to the dictionary
			Socket clientSock = new Socket(host, port);
			
			// Open streams to the socket for reading and writing
			InputStreamReader is = 
				new InputStreamReader(clientSock.getInputStream());
			this.socketRead = new BufferedReader(is);
		
			this.socketWrite = 
				new PrintWriter(clientSock.getOutputStream(),true);
		} catch(UnknownHostException uhe){
			uhe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void callDictionary(String en, String fileName){
		/*********************************************************
		* Write the english word to the socket for the dictionary.
		* We do this by sending the string and optional file name
		* as a colon delimited pair.
		*********************************************************/
		socketWrite.println(fileName + ":" + en);
		String foreign;
		
		// Read translated word from the socket
		try{
			foreign = socketRead.readLine();
			writer.print("English: " + en);
			writer.println(", Foreign: " + foreign);
			System.out.println(foreign);
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public Scanner getIn(){ return this.in; }
	
	public static void run(String fileName){
		Translate tran = new Translate();	
		tran.openStreams();
		/**************************************************
		* Continually get user input until user enters q.
		**************************************************/
		while(true){
			System.out.print("> ");
			// Read user input
			String en = tran.getIn().nextLine();
			// Has user entered q?
			if(en.equals("q")){ return; }
			tran.callDictionary(en, fileName);
		}
	}
	
	/********************
	* args[0] == fileName
	********************/
	public static void main(String[] args){
		// Scanner to get user input
		String fName;
		/*********************************************************
		* Test if user has supplied an (english,foreign) word pair
		* file.
		*********************************************************/
		if(args.length == 1){ fName = args[0]; }
		else{ fName = null; }
		run(fName);
	}	
}			
