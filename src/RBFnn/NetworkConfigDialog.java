package RBFnn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jfree.ui.RefineryUtilities;
import javax.swing.JSpinner;

public class NetworkConfigDialog extends JDialog
{
	private int patterns, numin, numhid, numout;
	private JSpinner PATspinner, INspinner, HIDspinner, OUTspinner;
	
	public NetworkConfigDialog()
	{
		JLabel lblSelectFile = new JLabel("Number of Patterns:");
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		sendAndClose();
            }
        });
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		closeOnly();
            }
        });
		
		JLabel lblSelectValidationSet = new JLabel("Number of Input Nodes:");
		
		JLabel lblNumberOfHidden = new JLabel("Number of Hidden Nodes:");
		
		JLabel lblNumberOfOutput = new JLabel("Number of Output Nodes:");
		
		PATspinner = new JSpinner();
		INspinner = new JSpinner();
		HIDspinner = new JSpinner();
		OUTspinner = new JSpinner();
		
		/*Group Layout Config */
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnOk)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSelectFile)
								.addComponent(lblSelectValidationSet)
								.addComponent(lblNumberOfHidden)
								.addComponent(lblNumberOfOutput))
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(OUTspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(HIDspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(PATspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(INspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(4)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectFile)
						.addComponent(PATspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectValidationSet)
						.addComponent(INspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNumberOfHidden)
						.addComponent(HIDspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNumberOfOutput)
						.addComponent(OUTspinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOk)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		/* end config */
		
		this.pack();
		this.setAlwaysOnTop(true);
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
    
    public void sendAndClose()
    {
    	//call function to send network parameters
		//close the dialog
    	patterns = (Integer)PATspinner.getValue();
    	numin = (Integer)INspinner.getValue();
    	numhid = (Integer)HIDspinner.getValue();
    	numout = (Integer)OUTspinner.getValue();
    	
    	RBFnn.RBFnn_GUI.receiveNetParams(patterns, numin, numhid, numout);
    	
    	this.dispose();
    }
    
    public void closeOnly()
    {
    	this.dispose();
    }
}
