/*
 *	package : test
 *	source  : Calculator Component.java
 *	date    : 1999.8.24
 */

package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.antsoft.ant.util.WindowDisposer;

public class AntCalculator extends JPanel implements ActionListener {
	private JTextField calcArea;
	
	public void actionPerformed( ActionEvent e )	{
		//TO DO 
		JButton source = (JButton)e.getSource();
		if ( source.getText().equals("Show") ) {		
			source.setText("Hide");
			setVisible(false);
		}
		else if ( source.getText().equals("Hide") ) {
			source.setText("Show");
			setVisible(true);
		}
		else {
			int val = Integer.parseInt(((JButton)e.getSource()).getText());
			calcArea.setText( calcArea.getText() + val );
		}
	}


	/**
	 *	AntCalculator -  constructor 
	 */
	public AntCalculator() {
		uiInit();		
	}
	
	public void uiInit( ) {
		calcArea = new JTextField("0.");
		setLayout( new BorderLayout() );
		add( calcArea, BorderLayout.NORTH );
		JPanel numberP = new JPanel(new GridLayout(0, 3));
		
		JButton num;
		for ( int i = 9; i > 0; i -= 3 ) {
			for ( int j = 2; j >= 0; j-- ) {				
				num = new JButton( Integer.toString( i - j ) );
				num.addActionListener(this);
				numberP.add( num );
			}
		}
		num = new JButton( "0" );
		num.addActionListener(this);
		numberP.add( num );

		num = new JButton( "." );
		num.addActionListener(this);
		numberP.add( num );

		num = new JButton( "+/-" );
		num.addActionListener(this);
		numberP.add( num );

		add( numberP, BorderLayout.CENTER );
	}

	public static void main( String[] args ) {
		System.out.println("Starting...");
		JFrame frame = new JFrame("Ω√«Ë¡ﬂ");
		AntCalculator calc = new AntCalculator();
		frame.getContentPane().add(calc, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
