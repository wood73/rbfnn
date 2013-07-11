package Clustering;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/*
 * Function class: No objects will be formed from this
 */
public class kMeans
{	
	/*Da Main*/
	public static void main(String[] args)
	{	
		//SAMPLE DATA
		double SAMPLES[][] = new double[][] {{1.0, 1.0}, 
                {1.5, 2.0}, 
                {3.0, 4.0}, 
                {5.0, 7.0}, 
                {3.5, 5.0}, 
                {4.5, 5.0}, 
                {3.5, 4.5}};
		
		double[][] centreClusters = computeClusters(SAMPLES, 2);
		
		for (int t=0; t<centreClusters.length; t++)
		{
			System.out.println(Arrays.toString(centreClusters[t]));
		}
										
	}
	
	/**************************************************************************************
	 * This function will compute the centres of the data points based on the input values 
	 * and the number of centres needed 'k'.
	 * This function uses the generic k-means algorithm.
	 * 
	 * @param values - The batch of input data
	 * @param k - The number of centres to compute. (Usually numHidden)
	 * @return The centres' vectors.
	 * @author fq000476
	 **************************************************************************************/
	public static double[][] computeClusters(double[][] values, int k)
	{
		Vector<Data> data; 	Vector<Cluster> centres;
		double[][] finalCentreVectors = new double[k][values[0].length];
		int motionMax = values.length/2, count=0;
		boolean finish = false;
		
		//Stage 1 - Initial Random centres
		data = Initialise(values);
		centres = new Vector<Cluster>();
		
		Random rand = new Random();
		int randVals[] = new int[k];
		
		for (int c=0; c<k; c++)
		{
			boolean proceed;
			do
			{
				proceed = true;
				int randomInt = rand.nextInt(data.size());
				randVals[c] = randomInt;
				
				//unique checking
				for (int a=0; a<randVals.length; a++)
				{
					if (c==a){;}
					else
					{
						if (randomInt == randVals[a])
						{
							proceed = false;
						}
					}
				}
			} while (!proceed);
			
			//create cluster class using randomly chosen centres; append to cluster vector
			Cluster a_center = new Cluster(data.elementAt(randVals[c]).getALLPoints());
			centres.add(a_center);
		}//end of stage 1.
		
		//Stage 2 - Grab 'n' Run
		for (int s=0; s<data.size(); s++)
		{
			double euclidean[] = new double[k];
			for (int g=0; g<centres.size(); g++)
			{
				euclidean[g] = Euclidean(data.elementAt(s).getALLPoints(), centres.elementAt(g).getALLcentrePoints());
			}
			
			double minVal = Double.MAX_VALUE; int ind = 0;
			
			for (int g=0; g<centres.size(); g++)
			{
				if (euclidean[g] < minVal)
				{
					minVal = euclidean[g];
					ind = g;
				}
			}
			
			/* centre with the minimum euclidean distance wins the data and grabs a copy
			 * of it. Then the mean centre is calculated and updated.
			 */
			centres.elementAt(ind).grabData(data.elementAt(s));
			
			//update centre
			centres.elementAt(ind).updateCentre();
		}//end of stage 2.
		
		boolean relocate;
		//stage 3 - re-evaluate cluster assignment until proper fit
		do {
		relocate = false;
		for (int c=0; c<centres.size(); c++)
		{
			for (int d=0; d<centres.elementAt(c).data.size(); d++)
			{
				double min = Double.MAX_VALUE;
				int index = 0;
				for (int i=0; i<centres.size(); i++)
				{
					double euclidean = Euclidean(centres.elementAt(c).data.elementAt(d).getALLPoints(), 
												 centres.elementAt(i).getALLcentrePoints());
					
					if (euclidean < min)
					{
						min = euclidean;
						index = i;
					}
					
				}
				
				if (index !=c)
				{
					//System.out.print("Data point to move: ");
					//System.out.println(Arrays.toString(centres.elementAt(c).data.elementAt(d).getALLPoints()));
					centres.elementAt(index).grabData(centres.elementAt(c).data.elementAt(d));
					centres.elementAt(c).removeData(centres.elementAt(c).data.elementAt(d));
					centres.elementAt(c).updateCentre();
					centres.elementAt(index).updateCentre();
					relocate = true;
					d--;
					count++;
					if (count >= motionMax)
					{
						finish = true;
					}
				}
			}
		}
		if(finish)
		{ break;}
		}while (relocate == true); //end of stage 3. It Works!
		
		//stage 4 - return final clusters' centres
		for (int t=0; t<centres.size(); t++)
		{
			finalCentreVectors[t] = centres.elementAt(t).getALLcentrePoints();
		}
		
		return finalCentreVectors;
	}
	
	/***************************************************
	 * Function to change data from type double to Data
	 * 
	 * @param values - Input Values
	 * @return The Vector of data-points of type Data
	 **************************************************/
	private static Vector<Data> Initialise(double[][] values)
	{
		int dimension = values[0].length;
		Vector<Data> data = new Vector<Data>();
		
		for (int a=0; a<values.length; a++)
		{
			Data temp = new Data(dimension);
			for (int i=0; i<dimension; i++)
			{
				temp.setPoint(i, values[a][i]);
			}
			
			data.add(temp);
		}
		
		return data;	
	}
	
	/***********************************************************
	 * Function that calculates the Euclidean distance between
	 * the data-points and the centre points 
	 * 
	 * @param x - Data values
	 * @param c - Cluster centres
	 * @return 
	 * @return The Euclidean Distance
	 ***********************************************************/
	private static double Euclidean(double x[], double c[])
	{
		double Esum = 0;
		for (int i=0; i<x.length; i++)
		{
			Esum += Math.pow((x[i]-c[i]), 2);
		}		
		return Math.sqrt(Esum);
	}
	
	/*************************************
	 * Data class Where points are stored. 
	 * 
	 * @author fq000476
	 ************************************/
	private static class Data
	{
		//Class Variables
		private int dimension;
		private double [] points;
		
		
		/*constructor*/
		public Data(int d)
		{
			dimension = d;
			points = new double[dimension]; 
		}
		
		public double getPoint(int ind)
		{
			return points[ind];
		}
		
		public void setPoint(int ind, double value)
		{
			points[ind] = value;
		}
		
		public double[] getALLPoints()
		{
			return points;
		}
		
		public void setALLPoints(double values[])
		{
			points = values;
		}
		
		/* To check if two Data types are the same */
		public boolean equals(Object e)
		{
			boolean isEqual = false;
			
			//first check if objects are of the correct class
			if (this.getClass()==e.getClass())
			{
				Data Ref = (Data)e;
				
				//now check if array points are the same
				if (Arrays.equals(this.getALLPoints(),Ref.getALLPoints()) == true)
				{
					isEqual = true;
				}
			}
			return isEqual;
		}
	}
	
	/***********************************************
	 * Cluster class Where centre points and cluster 
	 * data are stored.
	 * 
	 * @param
	 * 		values[] - The centre points of the 
	 * cluster
	 * 
	 * @author fq000476
	 ***********************************************/
	private static class Cluster
	{
		//Class Variables
		Vector<Data> data; //vector of 'captured' data
		private double[] centrePoints; //points of the centre
		
		public Cluster()
		{
			data = new Vector<Data>();
		}
		
		public Cluster(double values[])
		{
			data = new Vector<Data>();
			centrePoints=values;
		}
		
		//centre point setters and getters
		public void setCentrePoint(int ind, double value)
		{
			centrePoints[ind] = value;
		}
		
		public double getCentrePoint(int ind)
		{
			return centrePoints[ind];
		}
		
		public void setALLcentrePoints(double values[])
		{
			centrePoints = values;
		}
		
		public double[] getALLcentrePoints()
		{
			return centrePoints;
		}
		
		//datapoints grab and remove
		public void grabData(Data d)
		{
			data.add(d);
		}
		
		//remove data points with matching vectors (or matching Data type? Test...)
		public void removeData(Data ref)
		{
			for (int i=0; i<data.size(); i++)
			{
				if (data.elementAt(i).equals(ref) == true)
				{
					data.remove(i);
					break;
				}
			}
		}
		
		public void updateCentre()
		{
			//calculate mean
			double meanVec[] = new double[centrePoints.length];
			for (int dimension=0; dimension<centrePoints.length; dimension++)
			{
				double sum = 0;
				
				for (int q=0; q<data.size(); q++)
				{
					sum += data.elementAt(q).getPoint(dimension);
				}
				
				meanVec[dimension] = sum/data.size();
			}
			
			//update centre
			this.setALLcentrePoints(meanVec);
		}
	}
}