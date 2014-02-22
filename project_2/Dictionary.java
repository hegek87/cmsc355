import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;

/********************************************************
*                  Dictionary
* Translate an english word to some foreign language. The
* language translated to by default is norwegian.
*********************************************************
* Input: String fileName, String englishWord -- fileName
* is optional.
* Output: Displays the foreign word to the user and will
* the foreign word to the socket.
********************************************************/
public class Dictionary{
	private String fileName;
	private BufferedReader reader;
	
	private PrintWriter sockWrite;
	private BufferedReader sockRead;
	
	public static final int port = 8080;
	
	public Dictionary(){
		this.fileName = "norwegian.txt";
	}
	
	public void openConnection(){
		try{
			// Start socket connection
			ServerSocket serSock = new ServerSocket(port);
			Socket client = serSock.accept();
			
			// Open streams to socket
			this.sockWrite = 
				new PrintWriter(client.getOutputStream(), true);
				
			InputStreamReader irs = 
				new InputStreamReader(client.getInputStream());
			this.sockRead = new BufferedReader(irs);
			
			FileReader fr = new FileReader(this.fileName);
			this.reader = new BufferedReader(fr);
		} catch(UnknownHostException uhe){
			uhe.printStackTrace();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// clean up -- close the open readers
	public void cleanUp(){
		try{
			// we are done reading. Close our streams.
			reader.close();
			sockWrite.close();
			sockRead.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// returns the foreign word if found, otherwise return null
	public String translate(String en){
		String[] enFo;
		String temp;
		try{
			// read from file until we reach eof (and get null)
			while((temp = reader.readLine()) != null){
				enFo = temp.split(", ");
				// our word matches an (english, foreign) word pair
				if(en.equals(enFo[0])){
					return enFo[1];
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		return null;
	}
	
	public BufferedReader getSockReader(){ return sockRead; }
	public PrintWriter getSockWrite(){ return sockWrite; }
	
	// Only set the file if a new file was given
	public void setFileName(String fileName){ 
		if(fileName.length() == 0){ return; }
		this.fileName = fileName;
	}
	
	/*********************************************************
	* Creates a dictionary and uses it to translate a supplied 
	* english word to a foreign word. Writes the result to the
	* socket.
	*********************************************************/
	public static int processWord(){
		Dictionary d = new Dictionary();
		
		// Open the socket.
		d.openConnection();
		
		// Read the word from the socket
		try{
			String fileWord[] = 
				d.getSockReader().readLine().split(":");
			d.setFileName(fileWord[0]);
		
			// Translate the word
			String foreign = d.translate(fileWord[1]);
			String found = (foreign != null) ? 
						foreign : "Translation not found";
						
			// Write translated word to the socket
			d.getSockWrite().println(found);
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	/*
		Dictionary d;
		d = new Dictionary();
		d.openConnection();
		String foreign = d.translate(en);
		// Determine if the (english, foreign) word pair was found
		String found = (foreign != null) ? 
						foreign : "Translation not found";
		
		// display to user
		System.out.println(found);
		
		d.cleanUp();
		return 0;
		*/
		return 0;
	}
	
	public static void main(String[] args){
		processWord();
	}
		
}
