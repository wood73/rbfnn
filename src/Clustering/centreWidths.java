package Clustering;

public class centreWidths 
{
	public static double[] computeWidths(double[][] centres)
	{
		double[] widths = new double[centres.length];
		
		//for each centres...
		for (int c=0; c<centres.length; c++)
		{
			double firstMin = Double.MAX_VALUE, secondMin = Double.MAX_VALUE;
			
			//..find 2 nearest centre distances
			for (int x=0; x<centres.length; x++)
			{
				if (x==c){;}
				else
				{
					double dist = Euclidean(centres[c], centres[x]);
					
					//if smallest replace firstMin with dist and replace secondMin with firstMin
					if (dist<firstMin)
					{
						secondMin = firstMin;
						firstMin = dist;
					}
					//else if second smallest, replace secondMin
					else if(dist<secondMin)
					{
						secondMin = dist;
					}
				}
				
				//calculate width for centres
				double width = 0.5*(Math.pow(firstMin, 2) + Math.pow(secondMin, 2));
				width = Math.sqrt(width);
				widths[c] = width;
			}
		}
		
		return widths;
	}
	
	private static double Euclidean(double x[], double c[])
	{
		double Esum = 0;
		for (int i=0; i<x.length; i++)
		{
			Esum += Math.pow((x[i]-c[i]), 2);
		}		
		return Math.sqrt(Esum);
	}
}
