package RBFnn;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

//chart Libraries
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class RBFnn_GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static XYSeries trainingSeries;
    private static XYSeries validationSeries;
    private static java.io.File t_filepath, v_filepath;
    private String networkSelected = "full";
    
    //network params
    private static int patterns = 9, inputNodes = 2, hiddenNodes = 4, outNodes = 2;
    
    //Global components
    static JTextArea console;
    
    public RBFnn_GUI() 
	{
		super("Radial Basis Function NN by Daniel Anglin(FQ000476)");
		
		JPanel layoutPane = new JPanel();
		layoutPane.setLayout(new BoxLayout(layoutPane, BoxLayout.X_AXIS));
		add(layoutPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		add(buttonPane, BorderLayout.SOUTH);
		
		/*A menuBar... */
		setJMenuBar(new menubar());
		
		/*Chart and ChartPanel Configuration*/
	    trainingSeries = new XYSeries("Training Error");
	    validationSeries = new XYSeries("Validation Error");
		
		final XYDataset dataset = initialiseDataset();
		final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        layoutPane.add(chartPanel);
        
        layoutPane.add(Box.createRigidArea(new java.awt.Dimension(3, 0)));
        
        /*Second panel configuration*/
        JPanel networkLayout = new JPanel();
        networkLayout.setPreferredSize(new java.awt.Dimension(400,300));
        networkLayout.setBackground(Color.white);
        networkLayout.setLayout(new BoxLayout(networkLayout, BoxLayout.Y_AXIS));
        layoutPane.add(networkLayout);
        
        /*Adding a panel to paint the network layout */
        networkLayerPane visualNetwork = new networkLayerPane();
        networkLayout.add(visualNetwork);
        
        /*Adding a console */
        console = new JTextArea("", 5, 50);
		console.setLineWrap(true);
		console.setEditable(false);
		JScrollPane scrollText = new JScrollPane(console);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollText.getVerticalScrollBar().addAdjustmentListener((new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
			console.select(console.getHeight()+1000,0);
			}}));
		networkLayout.add(scrollText);
		
        /*Buttons...*/
        JButton startNetwork = new JButton("RUN Neural Net");
        startNetwork.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		callAction("RUN_NETWORK");
            }
        });
        buttonPane.add(startNetwork);
        
        buttonPane.add(Box.createRigidArea(new java.awt.Dimension(5, 0)));
        
        JButton stopNetwork = new JButton("STOP Neural Net");
        buttonPane.add(stopNetwork);
        
        buttonPane.add(Box.createRigidArea(new java.awt.Dimension(5, 0)));
        
        JButton save = new JButton("Save Config");
        buttonPane.add(save);
        
        buttonPane.add(Box.createRigidArea(new java.awt.Dimension(5, 0)));
        
        JButton clear = new JButton("Clear Chart");
        clear.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		callAction("CLEAR");
            }
        });
        buttonPane.add(clear);
        
        /*JFrame configuration*/
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
    
    public static void consolePrint(String message)
    {
    	console.append(message);
    }
    
    /***********************************************************
     * Initialises the dataset so that it will be ready to take
     * in the data once the network is running
     * 
     * @return
     * The empty dataset
     ***********************************************************/
    private XYDataset initialiseDataset()
	{   
	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(validationSeries);
	    dataset.addSeries(trainingSeries);
	    
	    return dataset;
    }
    
    /********************************
     * Creates and renders the chart
     * 
     * @param dataset
     * @return
     ********************************/
    private JFreeChart createChart(final XYDataset dataset)
	{
		//create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart
				(
						"RBFNN Error Chart", 	//chart title
						"Epoch",				//x axis label
						"RMSE",					//Y axis label
						dataset,				//data
						PlotOrientation.VERTICAL,
						true,					//to include legends?
						true,					//to include tooltips?
						false					//to include urls?
				);
		
		//Optional customisation of chart - experiment later
		chart.setBorderPaint(Color.gray);
		
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
        
        render.setSeriesShapesVisible(0, false);
        render.setSeriesStroke(0, new BasicStroke(3.0f));
        render.setSeriesPaint(0, Color.RED);
        render.setSeriesShapesVisible(1, false); //removes those unnecessary shapes
        render.setSeriesPaint(1, new Color(43,128,17));
        
        plot.setRenderer(render);
                
        return chart;
	}
    
	public static void addData(double x, double y, String type)
	{
		if (type == "training")
		{
			trainingSeries.add(x,y);
		}
		else if (type == "validation")
		{
			validationSeries.add(x,y);
		}
	}
	
	public void clearChart()
	{
		trainingSeries.clear();
		validationSeries.clear();
	}
	
	public void callAction(String cmd)
	{
		switch (cmd)
		{
			case "RUN_NETWORK":
			{
				
				SwingWorker rbfnn = new SwingWorker() 
				{            
					protected Object doInBackground() 
					{
						if (networkSelected == "full")
						{
							new RBFnn_Full(t_filepath.getAbsolutePath(), v_filepath.getAbsolutePath()); //starts RBFnn (Full Supervision)
						}
						else if (networkSelected == "partial")
						{
							//starts RBFnn (Partial Learning)
							new RBFnn_Partial(t_filepath.getAbsolutePath(), v_filepath.getAbsolutePath(), 
									patterns, inputNodes, hiddenNodes, outNodes);
						}
						
					    return null;
					}
				};
				rbfnn.execute();
				
				break;
			}
			
			case "CLEAR":
			{
				clearChart(); break;
			}
		}
	}
	
	public static void receiveFilePath(java.io.File t_path, java.io.File v_path)
	{
		t_filepath = t_path;
		consolePrint("Training Data Recived: "+ t_filepath.getName()+"\n");
		v_filepath = v_path;
		consolePrint("Validation Data Recived: "+ v_filepath.getName()+"\n");
	}
	
	public static void receiveNetParams(int pat, int in, int hid, int out)
	{
		patterns = pat; inputNodes = in; hiddenNodes = hid; outNodes = out;
		consolePrint("\nNetwork Parameters:\nPatterns: "+patterns+"\n" +
				"Input Nodes: "+inputNodes+"\nHidden Nodes: "+hiddenNodes+"\nOutput Nodes: "+outNodes+"\n");
	}
	
	/*************************************************
	 * A JMenuBar type class to be added onto the GUI
	 *************************************************/
	private class menubar extends JMenuBar
	{
		private menubar()
		{
			JMenu file = new JMenu("File");

			/* Just save the filepath and pass onto the network */ 
			JMenuItem loadData = new JMenuItem("Load Data");
			loadData.setToolTipText("Load data into the network");
			loadData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new fileImport.ImportGUI();
				}});
			
			JMenuItem saveConfig = new JMenuItem("Save Network Configuration");
			saveConfig.setToolTipText("save the network configuration to file");
			
			JMenuItem exit = new JMenuItem("Exit");
			exit.setToolTipText("Quit application");
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					System.exit(0);
				}});

			file.add(loadData);
			file.add(saveConfig);
			file.add(exit);

			this.add(file);
			
			JMenu console = new JMenu("Console");
			
			JMenuItem clearConsole = new JMenuItem("Clear Console");
			clearConsole.setToolTipText("Clear the console area");
			clearConsole.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent event) 
						{
							RBFnn_GUI.console.setText(null);
						}
					});
			console.add(clearConsole);
			
			this.add(console);
			
			JMenu network = new JMenu("Network");
			JMenu selNet = new JMenu("Select Network");
			network.add(selNet);
			
			// ButtonGroup for radio buttons
		    ButtonGroup networkGroup = new ButtonGroup();
		    
		    //add network type radio buttons
		    JRadioButtonMenuItem fullLearn = new JRadioButtonMenuItem("Full Supervised Learning", true);
		    fullLearn.addActionListener(new RadioAction());
		    selNet.add(fullLearn);
		    networkGroup.add(fullLearn);
		    
		    JRadioButtonMenuItem partialLearn = new JRadioButtonMenuItem("Partial Supervised Learning", false);
		    partialLearn.addActionListener(new RadioAction());
		    selNet.add(partialLearn);
		    networkGroup.add(partialLearn);
		    
		    //Menu Items
			JMenuItem config = new JMenuItem("Set Network Configuration");
			config.setToolTipText("Click to edit the network configuration");
			config.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					new NetworkConfigDialog();
				}});
			network.add(config);
			
			this.add(network);
		}
		
		class RadioAction implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand() == "Partial Supervised Learning")
				{
					networkSelected = "partial";
				}
				else if (e.getActionCommand() == "Full Supervised Learning")
				{
					networkSelected = "full";
				}
				RBFnn_GUI.console.append("Network Type: "+e.getActionCommand()+"\n");
			}
		}
	}
	
	private class networkLayerPane extends JPanel
	{
		
		public void paint(Graphics g)
		{	
			Graphics2D p = (Graphics2D)g;
	        
	        RenderingHints renderer = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        renderer.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        
	        p.setRenderingHints(renderer);
	        
	        //draw connecting lines
	        p.setColor(Color.black);
	        for (int i=0; i<2; i++)
	        {
	        	for (int j=0; j<4; j++)
	        	{
	        		p.drawLine(this.getWidth()/10, (this.getHeight()*(i+1)/3)+8, this.getWidth()/2, (this.getHeight()*(j+1)/5)+8);
	        	}
	        }
	        
	        for (int j=0; j<4; j++)
	        {
	        	for (int k=0; k<2; k++)
	        	{
	        		p.drawLine(this.getWidth()/2, (this.getHeight()*(j+1)/5)+8, this.getWidth()*9/10, (this.getHeight()*(k+1)/3)+8);
	        	}
	        }
	        
	        //colour input nodes red
	        p.setColor(new Color(159, 36, 36));
	        for (int i=0; i<2; i++)
	        {
	        	p.fillOval(this.getWidth()/10, this.getHeight()*(i+1)/3, 15, 15);
	        }
	        
	        //colour hidden nodes orange
	        p.setColor(new Color(200, 123, 14)); 
	        for (int i=0; i<4; i++)
	        {
	        	p.fillOval(this.getWidth()/2, this.getHeight()*(i+1)/5, 20, 20);
	        }
	        
	        //colour output nodes yellow
	        p.setColor(new Color(212, 188, 26));
	        for (int i=0; i<2; i++)
	        {
	        	p.fillOval(this.getWidth()*9/10, this.getHeight()*(i+1)/3, 15, 15);
	        }
	        
	        //fill university logo on top corner
	        BufferedImage img = null;
	        
	        try {                
	            img = ImageIO.read(new File("icons/uni.png"));
	         } catch (IOException ex) 
	         {
	              ex.printStackTrace();
	         }
	        
	        g.drawImage(img, this.getWidth()*37/40, 0, null);
		}
	}
}
