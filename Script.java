import java.util.*;
import java.io.*;

/**
 * The class Script only aims to deploy the script. It is a quite simple script.
 * 
 * This project has to be executed on a bash-shell compatible console
 * 
 * The required files of this Script to be executed correctly are the .txt files 
 * containing 7x7 squares of 0 and 1 that represents letters.
 * For instance, if you wish to print HEY, you need H.txt, E.txt and Y.txt all containing 
 * at least a square made by 49 0s or 1s. A block will be written in the image once a 1 is encountered.
 * Attention, I chose to replace all the space.txt by _.txt. As you have the code, you can change it!
 * 
 * 
 * @author mmassart
 * @version beta
 */
public class Script {
	/**
	 * Returns the Sunday before the date entered as parameter
	 * 
	 * @param a valid Calendar object initialized on a certain date.
	 * 
	 * @return a Calendar object to the previous sunday of the calendar date entered as parameter
	 */
	private static Calendar getPreviousSunday(Calendar c){
		while(c.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY){
			            c.add(Calendar.DAY_OF_WEEK,-1);
		}
		return c;
	}
	/**
	 * 
	 * @param cmd a valid String to be executed in the Unix Shell
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void execCommand(String cmd) throws IOException, InterruptedException{
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec( new String [] { "bash", "-c",cmd});
		proc.waitFor();
	}

	/**
	 * 
	 * @see class Script specifications
	 * 
	 * Attention you need to have all the .txt files in the same directory!
	 * 
	 * @param args with args[1] is the word you want to print. 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{
		//TODAY
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		//check parameters
		if(args.length != 1){
			System.out.println("Usage \"java Script TO_HIRE\"\n Make sure that all the letters you enter are in the project location and that the folder in which the Script is executed is already git initialized!");
			System.out.println(args[0]);
			return;
		}
		//Init
		String toWrite=args[0];
		int size = toWrite.length();
		
		if(size > 7){
			System.err.println("As a letter takes 7 columns, you are writting outside of the scope of the image in Github");
		}
		
		//Compute the week to begin
		c.add(Calendar.DAY_OF_WEEK, -1*(size*7*7)); // as we have 7x7 square letters.
		c = getPreviousSunday(c);

		char[][]letter = new char[7][7];//as we have 7x7 square letters. Could be improved of course.
		//foreach letter to write
		for(int i=0;i< size;i++){
			
			char[] toWriteC= toWrite.toCharArray();
			System.out.println("/home/mmassart/Desktop/public-repo/"+toWriteC[i]+".txt");
			File file= new File("/home/mmassart/Desktop/public-repo/"+toWriteC[i]+".txt");
			
			Scanner in=null;
			try {
				in = new Scanner(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Error while loading one file! Please try again!");
				file = null;
				c = null;
				return;
			}
			//Read the file by line of char. Not ideal but efficient enough for this application
			for(int j=0;j<7;j++){
				char[] a=in.next().toCharArray();
				letter[j]=a;
			}
			
			//set the message commit number
			int commitnb=0;
			//foreach column of the squared image
			for(int j=0;j<7;j++){
				//foreach line of the squared image
				for(int k=0;k<7;k++){
					//requires action
					if(letter[k][j] == '1'){
						//Prints the evolution of the execution
						System.out.println(c.getTime());
					
						execCommand("echo \"This is a quite simple modification for commit n°"+commitnb+"\" > modification.txt");
						execCommand("git add modification.txt");
						execCommand("git commit -m \"Commit n°"+ (commitnb++) +"\"");
						execCommand("git commit --amend --date=\""+c.getTime()+"\" --no-edit");
						
					}
					c.add(Calendar.DAY_OF_WEEK,1);
				}//end for k
			}//end for j
		}//end for i
		System.out.println("Please add a remote location with git and perform a \"git push\"");

	}
}
