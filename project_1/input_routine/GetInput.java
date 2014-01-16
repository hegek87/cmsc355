import java.util.Scanner;
import java.io.InputStream;

public class GetInput{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.print(">");
			String userInput = in.nextLine();
			if(userInput.equals("exit") || userInput.equals("quit")){
				break;
			}
			//process input, for now just echo
			try{
				Runtime runner = Runtime.getRuntime();
				Process out = runner.exec("echo " + userInput);
				InputStream processOut = out.getInputStream();
				int data;
				while((data = processOut.read()) > 0){
					System.out.print((char)data);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
} 
