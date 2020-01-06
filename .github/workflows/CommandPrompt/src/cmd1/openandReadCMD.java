package cmd1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class openandReadCMD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 FTPClient ftp = new FTPClient() ; 
		 String serverName = "172.16.212.11";
         String JOBID=null;

	     //Creating Connection to the server 
	     try { 
	          ftp.connect (serverName) ; 
	          String replyText =ftp.getReplyString()  ; 
	          System.out.println (replyText) ; 
	     } 
	     catch (Exception  e)  {
	               e.printStackTrace () ; 
	     } 
	     //Login to the server 
	     try { 
	    	 	//UserName and Password sent as parameter
	               ftp.login ("TPLRDEM", "PWD126") ; 
	               String replyText = ftp.getReplyString() ; 
	               System.out.println (replyText); 
	               //Verification of whether the user is logged in successfully or not
	               if(replyText.contains("230"))
	               {
	            	   System.out.println("Logged In");
	            	   
	            	   //Instructing the FTP Site to have file to be processed as JES file
	            	   ftp.site("QUOTE SITE FILETYPE=JES");
		               
		               String JESSiteText = ftp.getReplyString();
		               System.out.println(JESSiteText);
		               
		               //Submitting the JCL file
		               FileInputStream inputStream = new FileInputStream ("C:\\Workspace\\Mainframe\\test2.jcl") ;
		               ftp.storeFile (serverName,inputStream) ;
		               String jobtext = ftp.getReplyString() ;
		               System.out.println (jobtext) ;
		               String[] names = ftp.listNames("*");
		               for (int i = 0; i < names.length; i++) {
		                   System.out.println("file " + i + " is " + names[i]);
		               }
		               Pattern jcljobpattern = Pattern.compile("JOB[0-9]*");
		               Matcher jcljobmatch = jcljobpattern.matcher(jobtext);
		               
		               while(jcljobmatch.find()) {
		            	   JOBID = jcljobmatch.group(0);
		            	   System.out.println(JOBID);
		               }
		               
		            // Retrieve part of a JES job
		               		                
		               InputStream is = ftp.retrieveFileStream(JOBID);
		               BufferedReader br = new BufferedReader(new InputStreamReader((is)));
		               boolean bContinue = true;
		               while (bContinue) {
		                   String sLine = br.readLine();
		                   if (sLine != null) {
		                       System.out.println("line ... " + sLine);
		                   } else {
		                       bContinue = false;
		                       is.close();
		                       br.close();
		                   }
		               }
		               ftp.completePendingCommand();
		     
	               }
	               else {
	            	   System.out.println("User is not logged into the system");
	               }
	                     
	     } catch (Exception e) { 
	               e.printStackTrace(); 
	     }
	     
	   //Quit the server 
	     try { 
	                 ftp.quit() ; 
	     }
	     catch  (Exception e) { 
	                e.printStackTrace() ; 
	     }

}
}
