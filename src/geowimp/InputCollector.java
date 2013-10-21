package geowimp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


class InputCollector {
	JButton click;
	JFrame f;
	JTextField textfield;
	GeoDraw gd;
	
	
	
	InputCollector(GeoDraw gd){
		this.gd = gd;	
	}
	

	public void run() {
        // Create the window
        f = new JFrame ("Please type in artist to find");
        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add a label and a button
        f.getContentPane().add(new JLabel("Artist to find:"));
        textfield = new JTextField("                    ");
        f.getContentPane().add(textfield);
        click = new JButton("Draw onto map!");
        click.addActionListener(new ButtonListener());
        f.getContentPane().add(click);
        // Add a layout manager so that the button is not placed on top of the label
        f.getContentPane().setLayout(new FlowLayout());
        // arrange the components inside the window
        f.pack();
        textfield.setText("");
        //By default, the window is not visible. Make it visible.
        f.setVisible(true);
    }
	
	private class ButtonListener implements ActionListener {
		  public void actionPerformed (ActionEvent event) {
		    Object source = event.getSource();

		    if (source == click) {
		    	textfield.getText();
		        f.setVisible(false);
		        String artist = textfield.getText();
		        System.out.println("Got from GUI: "+ artist);
		        gd.setArtist(artist);
		        gd.load();
		    }
		  }
		 }
	
}
