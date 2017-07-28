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
	private double ans, rValue;
	private boolean answer = true;
	private boolean operatorPressed = false;
	String operator = "+";
	
	public void actionPerformed( ActionEvent e )	{
		//TO DO
		String key = ((JButton)e.getSource()).getText();
		if ( 0 <= key.compareTo("0") && key.compareTo("9") <= 0) {
			operatorPressed = false;
			int val = Integer.parseInt(key);
			if ( answer ) {
				answer = false;
				calcArea.setText( key );
			} else {
				double newVal = Double.valueOf( calcArea.getText() + val ).doubleValue();
				calcArea.setText( String.valueOf(newVal) );
			}			
		} else if ( key.equals("+") || key.equals("-") || 
								key.equals("*") || key.equals("/") || 
								key.equals("=") ) {
			if ( !operatorPressed ) {
				operatorPressed = true;
				rValue = Double.valueOf(calcArea.getText()).doubleValue();
			}
			if ( operator.equals(key) || key.equals("=") )
				calcStack( key );
			else
				operator = key;
		}
	}

	private void calcStack( String newOp ) {
		if ( operator.equals("=") ) {
			operator = newOp;
			return;
		} else if ( operator.equals("+") ) {
			ans += rValue;
		} else if ( operator.equals("-") ) {
			ans -= rValue;
		} else if ( operator.equals("*") ) {
			ans *= rValue;
		} else if ( operator.equals("/") ) {
			ans /= rValue;
		}
		
		// Push operator on stack
		operator = newOp;
		calcArea.setText(String.valueOf(ans));
		answer = true;
	}
	
	/**
	 *	AntCalculator -  constructor 
	 */
	public AntCalculator() {
		ans = 0;
		rValue = 0;
		uiInit();		
	}
	
	public void uiInit( ) {
		calcArea = new JTextField(String.valueOf(ans));
		setLayout( new BorderLayout() );
		add( calcArea, BorderLayout.NORTH );

		// Add number panel
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

		// Add operator panel
		JPanel operatorP = new JPanel( new GridLayout( 0, 2 ) );
		JButton operator;
		operator = new JButton("/");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("sqrt");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("*");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("%");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("-");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("1/x");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("+");
		operator.addActionListener(this);
		operatorP.add(operator);
		operator = new JButton("=");
		operator.addActionListener(this);
		operatorP.add(operator);
		
		add( numberP, BorderLayout.CENTER );
		add( operatorP, BorderLayout.EAST );
	}

	// Test driver
	public static void main( String[] args ) {
		JFrame frame = new JFrame("Ant calculator");
		AntCalculator calc = new AntCalculator();
		frame.getContentPane().add(calc, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent e ) {
				e.getWindow().dispose();
			}
			
			public void windowClosed( WindowEvent e ) {
				System.exit(0);
			}
		} );
	}
}
