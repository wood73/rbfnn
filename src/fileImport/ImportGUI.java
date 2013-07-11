package fileImport;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jfree.ui.RefineryUtilities;

public class ImportGUI extends JDialog 
{
	File t_filepath, v_filepath;
	
	private JTextField textField;
	private JTextField textField_1;
	public ImportGUI()
	{
		JLabel lblSelectFile = new JLabel("Select TRAINING Set:");
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		FileChooserActionPerformed(e);
            }
        });
		
		textField = new JTextField();
		textField.setColumns(10);
		
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
		
		JLabel lblSelectValidationSet = new JLabel("Select VALIDATION Set:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JButton button = new JButton("Open File");
		button.addActionListener(new ActionListener() 
        { 
        	public void actionPerformed(ActionEvent e)
            {
        		FileChooserAction2(e);
            }
        });
		
		/*Group Layout Config */
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSelectFile)
							.addPreferredGap(ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
							.addComponent(btnOpenFile)
							.addGap(32))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnOk)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)
							.addGap(33))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addComponent(lblSelectValidationSet)
									.addPreferredGap(ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
									.addComponent(button, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
								.addComponent(textField_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE))
							.addGap(32))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(38)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectFile)
						.addComponent(btnOpenFile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblSelectValidationSet)
						.addComponent(button))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
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
	
	private void FileChooserActionPerformed(ActionEvent evt) 
	{
        t_filepath = ChooseFile(this);
        textField.setText(t_filepath.getAbsolutePath());
    }
	
	private void FileChooserAction2(ActionEvent evt) 
	{
        v_filepath = ChooseFile(this);
        textField_1.setText(v_filepath.getAbsolutePath());
    }
	
	public static File ChooseFile(Container parent)
	{
        //file for putting returned value in
        File dataFile = null;
        
        //get the type option from the menu
        
        try{
                dataFile = OpenFile();
            }catch(IOException E){
                System.out.println(E.getMessage());
            }

            return dataFile;
           
    }
	
	//presents user with the JFileChoose option so they can select the File to open
    public static File OpenFile() throws IOException
    {
    	JFileChooser opTxtFile = new JFileChooser();
    	File txtDataFile = null;

        //set File Chooser to show files and directories then show the dialogue and get
        // the return value to see if the operation was successful
    	opTxtFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	int returnVal = opTxtFile.showOpenDialog(null);

    	//if the chosen file is approved (whatever that means I guess just not invalid) then
    	//store the directory of the folder
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
        	txtDataFile = opTxtFile.getSelectedFile();
        }

     //pass on the file to something that can actually do something with it
        return txtDataFile;
    }
    
    public void sendAndClose()
    {
    	//call function to send filepath back to GUI
		//close the dialog
    	RBFnn.RBFnn_GUI.receiveFilePath(t_filepath, v_filepath);
    	this.dispose();
    }
    
    public void closeOnly()
    {
    	this.dispose();
    }
}
