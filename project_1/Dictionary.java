import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

/********************************************************
*                  Dictionary
* Translate an english word to some foreign language. The
* language translated to by default is norwegian.
*********************************************************
* Input: String fileName, String englishWord -- fileName
* is optional.
* Output: Displays the foreign word to the user and will
* write "English: englishWord, Foreign: TranslatedWord"
* to the file output.txt
********************************************************/
public class Dictionary{
	private String fileName;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public Dictionary(String fileName){
		this.fileName = fileName;
	}
	
	// open files for reading and writing dictionary data
	private void openDict(){
		try{
			/****************************************************
			* Open reader and writer. The writer will write the
			* results to output.txt, and the reader will read the
			* (english, foreign) word dictionary file.
			****************************************************/
			FileReader fr = new FileReader(this.fileName);
			FileWriter fw = new FileWriter("output.txt", true);
			this.reader = new BufferedReader(fr);
			this.writer = new PrintWriter(new BufferedWriter(fw));
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	// clean up -- close the open readers
	public void cleanUp(){
		try{
			// we are done reading and writing. Close our streams.
			reader.close();
			writer.close();
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
	
	public BufferedReader getReader(){ return reader; }
	public PrintWriter getWriter(){ return writer; }
	
	/*********************************************************
	* Creates a dictionary and uses it to translate a supplied 
	* english word to a foreign word. Writes the result to the
	* user and output.txt
	*********************************************************/
	public static int processWord(String en, String fileName){
		Dictionary d;
		d = new Dictionary(fileName);
		d.openDict();
		String foreign = d.translate(en);
		// Determine if the (english, foreign) word pair was found
		String found = (foreign != null) ? 
						foreign : "Translation not found";
		
		// display to user
		System.out.println(found);
		d.getWriter().print("English: " + en);
		d.getWriter().println(", Foreign: " + found);
		
		d.cleanUp();
		return 0;
	}
	
	/***************************
	*  args[0] == English word
	*  args[1] == fileName
	***************************/
	public static void main(String[] args){
		/****************************************************
		* Check whether or not we should use the default file
		* containing (english, norwegian) word pairs.
		****************************************************/
		String fileName = (args.length==2) ? args[1] : "norwegian.txt";
		processWord(args[0], fileName);
		
	}
		
}
