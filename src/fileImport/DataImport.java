package fileImport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class DataImport 
{
	Vector<dataVals> Data = new Vector<dataVals>(); 
	
	public DataImport(String filename) throws Exception
	{
		FileReader fr = new FileReader(filename); 
		BufferedReader br = new BufferedReader(fr); 
		String str;
		String strLine[];
			
		while((str = br.readLine()) != null) 
		{ 
			//split string into 4 values, create new data object with 4 values, profit!!!
			strLine = str.split("\t");
			double tempvals[] = new double[strLine.length];
			for (int a=0; a<strLine.length; a++)
			{
				tempvals[a] = Double.parseDouble(strLine[a]);
			}
			
			dataVals tempdata = new dataVals(tempvals[0], tempvals[1], tempvals[2], tempvals[3]);
			Data.add(tempdata);
		}
		
		fr.close();
		br.close();
	}
	
	public Vector<dataVals> sendData()
	{
		return Data;
	}
	
	public static class dataVals
	{
		double values[];
		
		private dataVals(double Dist, double LinVelocity, double Angle, double AngSpeed)
		{
			values = new double[4];
			values[0] = Dist; values[1]=LinVelocity; values[2]=Angle; values[3]=AngSpeed;
		}
		
		public double getValue(int index)
		{
			return values[index];
		}
	}
}
