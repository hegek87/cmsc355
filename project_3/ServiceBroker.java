import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServiceBroker{
	public static final String services = "services.txt";
	public static final String cmd = "java -jar ./services/";
	private Runtime runner;
	private String serviceToRun;
	
	public ServiceBroker(String serviceToRun){
		this.runner = Runtime.getRuntime();
		try(	FileReader fr = new FileReader(services);
				BufferedReader br = new BufferedReader(fr)
			){
			String temp;
			while((temp = br.readLine()) != null){
				String[] pair = temp.split(" ");
				if(serviceToRun.equals(pair[0])){
					this.serviceToRun = pair[1];
				}
			}
			if(this.serviceToRun == null){
				this.serviceToRun = "Translate.jar";
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void callService(String arg){
		String pCall = cmd + serviceToRun + " " + arg;
		try{
			// Execute Translate.jar
			Process p = this.runner.exec(pCall);
			// Stream for reading output of the call to Translate.jar
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			//wait for exec to finish
			//p.waitFor();
			String nextLine;
			/**************************************************
			* Read the output from the call to Dictionary.jar
			**************************************************/
			while((nextLine = br.readLine()) != null){
				System.out.println(nextLine);
			}
		} catch(Exception e){ e.printStackTrace(); }
	}
	
	public String getServiceToRun(){ return serviceToRun; }
	
	public static void main(String[] args){
		String arg = (args.length > 0) ? args[0] : "";
		ServiceBroker sb = new ServiceBroker(arg);
		//System.out.println(sb.getServiceToRun());
		String enWord = (args.length > 1) ? args[1] : "";
		sb.callService(enWord);
	}
}
