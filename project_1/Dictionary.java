import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

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
			while((temp = reader.readLine()) != null){
				enFo = temp.split(", ");
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
	
	public static int processWord(String en, String fileName){
		Dictionary d;
		String temp;
		if(fileName == null){
			// no dictionary supplied
			temp = "norwegian.txt";
		}
		else{
			temp = fileName;
		}
		d = new Dictionary(temp);
		d.openDict();
		String foreign = d.translate(en);
		String found = (foreign != null) ? 
						foreign : "Translation not found";
		
		// display to user
		System.out.println(found);
		d.getWriter().print("English: " + en);
		d.getWriter().println(", Foreign: " + found);
		
		d.cleanUp();
		return 0;
	}
	
	/*
	*  args[0] == Dictionary
	*  args[1] == English word
	*  args[2] == fileName
	*/
	public static void main(String[] args){
		processWord(args[0], ((args.length != 2) ? null : args[1]));
	}
		
}
