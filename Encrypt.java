/*  Author		T Allen Lucas
    Purpose		to password encrypt any file
    Program		Encrypt.java
*/

// included files
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Random;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import javax.swing.JFormattedTextField;


public class Encrypt extends JPanel {
   private JLabel label, space1, space2, space3;
   private JLabel seedLbl;
   private JProgressBar pb;
   private JButton button;
   private JFileChooser fc;
   private static Random generator;
   private JCheckBox view;
   private String s;
   private JTextArea textArea;
   private JFormattedTextField mySeed;

   //Encrypt constructor
   public Encrypt() {
   	  JFrame.setDefaultLookAndFeelDecorated(true);
      JFrame frame = new JFrame("XCipher");
      frame.setResizable(false);
      frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

      button = new JButton("Open");
      button.addActionListener(new ButtonListener());

      fc = new JFileChooser();

      pb = new JProgressBar(0, 100);
      pb.setValue(0);
      pb.setStringPainted(true);
	  pb.setPreferredSize(new Dimension(400,25));

      label = new JLabel(" Open only to view.");
      space1 = new JLabel("     ");
      space2 = new JLabel("");
      space3 = new JLabel("                                                                               ");
      seedLbl = new JLabel("Seed");

      //seed = new JTextField(6);
      mySeed = new JFormattedTextField(Integer.valueOf(5));
      mySeed.setColumns(6);



      view = new JCheckBox();
	  textArea = new JTextArea("", 8, 38);
      textArea.setFont(new Font("Serif", Font.ITALIC, 16));
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setEditable(false);

      JScrollPane scroll = new JScrollPane ( textArea );
      scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      panel.setBorder(BorderFactory.createEmptyBorder(5, 1, 1, 1));
      panel.add(button);
      panel.add(space1);
      panel.add(pb);
      panel.add(space2);

      JPanel panel2 = new JPanel();
      panel2.add(view);
      panel2.add(label);
      panel2.add(space3);
      panel2.add(seedLbl);
      panel2.add(mySeed);

      JPanel panel3 = new JPanel();
      panel3.setBorder(new TitledBorder(new EtchedBorder(), ""));
      panel3.add(scroll, BorderLayout.SOUTH);

      JPanel panel1 = new JPanel();
      panel1.setLayout(new BorderLayout());
      panel1.add(panel, BorderLayout.NORTH);
	  panel1.add(panel2, BorderLayout.WEST);
	  panel1.add(panel3, BorderLayout.SOUTH);

      frame.setContentPane(panel1);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(500, 300);
   }

   //Main
   public static void main(String[] args) {
      Encrypt spb = new Encrypt();
   }

// This class puts the file processing into a thred.
class EncryptThread extends Thread {
   public EncryptThread(String str) {
      super(str);
   }

   public void run() {
      int seedValue = Integer.parseInt(mySeed.getText().replaceAll(",", ""));

      generator = new Random(seedValue);

      //This opens the open dialog
      int returnVal = fc.showOpenDialog(Encrypt.this);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
      File inFile = fc.getSelectedFile();

      textArea.setText("");
      textArea.append("File opened: " + inFile.getName() + "\n");

         try {
         	//Opens files in binary format
	        FileInputStream reader = new FileInputStream(inFile);
	        byte inFileContent[] = new byte[(int)inFile.length()];
	        byte outFileContent[] = new byte[(int)inFile.length()];
	        byte mask[] = new byte[1];

		    long inFileLength = inFile.length();
		    long bytesRead = 0;

		    // variables
		    String passWord = "";
		    String newFile ="";
		    int passWordLength = 0;
		    int counter = 0;
		    int pbBarValue = 0;
		    long fileLen = 0;

			//Input box
		    passWord = JOptionPane.showInputDialog("Enter a password.");
			if(passWord != null) {

			   if (!view.isSelected())
			   {
				   newFile = inFile.toString();

				   if (!newFile.endsWith(".enc"))
				   {
				   	newFile += ".enc";
				   }
					else
					{
						newFile = newFile.replace(".enc", ".dec");
					}



				   System.out.println(newFile);

				   //returnVal = fc.showSaveDialog(Encrypt.this);
				   //if (returnVal == JFileChooser.APPROVE_OPTION){

				      File outFile = new File(newFile); //fc.getSelectedFile();

					  FileOutputStream writer = new FileOutputStream(outFile);

			          passWordLength = passWord.length();
				      pb.setValue(0);
	                  button.setEnabled(false);

	                  reader.read(inFileContent);
					  fileLen = inFile.length();

					  textArea.append("File Saved: " + outFile.getName() + "\n");

					  // loop through each byte of the input file
				      for (int i = 0; i<fileLen; i++){
				      	 generator.nextBytes(mask);
				      	 outFileContent[i] = (byte)(mask[0] ^ inFileContent[i]);
				         counter++;
				         bytesRead ++;

	                     //Display progress
				         pbBarValue = ((int)((double)bytesRead/inFileLength*100));
				         pb.setValue(pbBarValue);

				         //Keeps looping through each character of the password
				         if(counter >= passWordLength)
				         {
				            counter=0;
				         }

				      }
	                  s = new String(outFileContent);
	                  if (s.length() > 1000)
	                  {
	                  	s = s.substring(0,1000);
	                  }
	                  textArea.append("Password: " + passWord + "\nSeed: " + mySeed.getText() +"\n\n");
	                  textArea.append("First 1,000 characters of the Enc/Dec text:\n" + s +"\n\n");

	                  button.setEnabled(true);
	                  writer.write(outFileContent);
	                  writer.close();
			      //}

	           }
		       else //This section below for viewing only
		       {

			          passWordLength = passWord.length();
				      pb.setValue(0);
	                  button.setEnabled(false);

	                  reader.read(inFileContent);
					  fileLen = inFile.length();

					  // loop through each byte of the input file
				      for (int i = 0; i<fileLen; i++){
				      	 generator.nextBytes(mask);
				      	 outFileContent[i] = (byte)(mask[0] ^ inFileContent[i]);
				         counter++;
				         bytesRead ++;

	                     //Display progress
				         pbBarValue = ((int)((double)bytesRead/inFileLength*100));
				         pb.setValue(pbBarValue);

				         //Keeps looping through each character of the password
				         if(counter >= passWordLength)
				         {
				            counter=0;
				         }

				      }
	                  button.setEnabled(true);
	                  s = new String(outFileContent);
	                  if (s.length() > 1000)
	                  {
	                  	s = s.substring(0,1000);
	                  }
	                  textArea.append("Password: " + passWord + "\nSeed: " + mySeed.getText() +"\n\n");
	                  textArea.append("First 1,000 characters of the Enc/Dec text:\n" + s +"\n\n");

		       }
		    }
		    reader.close();
	     }
	     catch(IOException e)
         {
            //label.setText("Could not open file.");
            button.setEnabled(true);
         }
      }
   }
}

//Checks for a button press
class ButtonListener implements ActionListener {
   public void actionPerformed(ActionEvent ae) {
      new EncryptThread("Enc").start();
   }
}
}