package fileExport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

public class AnglinFile 
{
	public static void createFileSave
	(int in, int hidden, int out, double centres[], double widths[], double weights[])
	{
		//if no message present
		createFileSave(in, hidden, out, centres, widths, weights, null);
	}
	
	public static void createFileSave
	(int in, int hidden, int out, double centres[], double widths[], double weights[], String message)
	{
		Vector<String> saves= new Vector<String>();
		saves.add(message+"\n\n\n");
		saves.add("Number of Inputs: "+in);
		saves.add("Number of Hidden: "+hidden);
		saves.add("Number of Outputs: "+out);
		
		//Create String with arrays values and '/' as deliminator
		String centreString = new String();
		String widthsString = new String();
		String weightsString = new String();
		
		for (int c=0; c<centres.length; c++)
		{
			centreString = centreString+Double.toString(centres[c])+"/";
		}
		
		for (int s=0; s<widths.length; s++)
		{
			widthsString = widthsString+Double.toString(widths[s])+"/";
		}
		
		for (int r=0; r<weights.length; r++)
		{
			weightsString = weightsString+Double.toString(weights[r])+"/";
		}
		
		saves.add("Centres: "+centreString);
		saves.add("Widths: "+widthsString);
		saves.add("Linear Weights: "+weightsString);
		
		//save file
		try{
			FileWriter fout = new FileWriter("Network Configuration.anglin");
			BufferedWriter write = new BufferedWriter(fout);
			
			for(int i=0; i<saves.size(); i++)
			{
				write.write(saves.elementAt(i));
				write.newLine();
			}
			
			write.close();
			fout.close();
		}catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		  }
	}//end of function

}
