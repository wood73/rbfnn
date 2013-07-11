package fileExport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

public class AnglinFileTemplate 
{
	//vector or array of string
	//input parameters added to end of corresponding elements
	//save string to .anglin
	//last parameter is message
	public void createFileSave
	(int in, int hidden, int out, double centres[], double widths[], double weights[])
	{
		//if no message present
		createFileSave(in, hidden, out, centres, widths, weights, null);
	}
	
	public void createFileSave
	(int in, int hidden, int out, double centres[], double widths[], double weights[], String message)
	{
		Vector<String> saves= new Vector<String>();
		saves.add("Number of Inputs: "+in);
		saves.add("Number of Hidden: "+hidden);
		saves.add("Number of Outputs: "+out);
		
		//Create String with arrays values and '-' as deliminator
		String centreString = null;
		String widthsString = null;
		String weightsString = null;
		
		for (int c=0; c<centres.length; c++)
		{
			centreString = centreString+Double.toString(centres[c])+"-";
		}
		
		for (int s=0; s<widths.length; s++)
		{
			widthsString = widthsString+Double.toString(widths[s])+"-";
		}
		
		for (int c=0; c<centres.length; c++)
		{
			weightsString = weightsString+Double.toString(weights[c])+"-";
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
	}

}
