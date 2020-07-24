/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ankit Dwivedi
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A network port scanner with GUI
 *
 *
 */
public class PortScanner extends JFrame implements ActionListener, ChangeListener {
	
	//Final variables
	private static final long serialVersionUID = 2884600754343147821L;
	private static final int W = 250;
	private static final int H = 375;
	
	//Flags
	private boolean displayAll = false;
	
	//Compoents
	private JTextField ipAddress, lowerPort, higherPort;
	private JTextArea output;
	private JScrollPane outputScroller;
	private JCheckBox toggleDisplayAll;
	private JButton scanPorts;
	private JPanel settingsPanel, outputPanel;
	
	/**
	 * Sets up the frame and calls
	 * 
	 * @see #initComponents()
	 */
	public PortScanner() {
		super( "Port Scanner" );
		
		initComponents();
		
		super.setLayout( new FlowLayout() );
		super.setSize( W, H );
		super.setLocationRelativeTo( null );
		super.setResizable( false );
		super.setVisible( true );
		super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	/**
	 * Sets up the components, panels, and adds them to the Frame
	 */
	private void initComponents() {
		//Text fields
		this.ipAddress = new JTextField( 12 );
		this.lowerPort = new JTextField( 5 );
		this.higherPort = new JTextField( 5 );
		
		//TextArea & ScrollPane
		this.output = new JTextArea( 10, 20 );
		this.output.setEditable( false );
		this.output.setLineWrap( true );
		this.outputScroller = new JScrollPane( this.output );
		
		//Check box
		this.toggleDisplayAll = new JCheckBox( "Display all results (open & closed)" );
		this.toggleDisplayAll.addChangeListener( this );
		
		//Buttons
		this.scanPorts = new JButton( "Scan" );
		this.scanPorts.addActionListener( this );
		
		//JPanels
		this.settingsPanel = new JPanel( new FlowLayout() );
		this.settingsPanel.setBorder( BorderFactory.createTitledBorder( "Scan information" ) );
		this.settingsPanel.setPreferredSize( new Dimension( 230, 135 ) );
		this.settingsPanel.add( new JLabel( "IP Address: " ) );
		this.settingsPanel.add( this.ipAddress );
		this.settingsPanel.add( new JLabel( "Port range: " ) );
		this.settingsPanel.add( this.lowerPort );
		this.settingsPanel.add( new JLabel( "-" ) );
		this.settingsPanel.add( this.higherPort );
		this.settingsPanel.add( this.toggleDisplayAll );
		this.settingsPanel.add( this.scanPorts );
		
		this.outputPanel = new JPanel( new FlowLayout() );
		this.outputPanel.setBorder( BorderFactory.createTitledBorder( "Results: " ) );
		this.outputPanel.add( outputScroller );	
		
		//add components
		super.add( this.settingsPanel );
		super.add( this.outputPanel );
	}
	
	/**
	 * Manages the button actions
     * @param ae
	 */
        @Override
	public void actionPerformed(ActionEvent ae ) {
		if( ae.getSource() == this.scanPorts ) {
			this.output.setText("Starting scan..." + System.lineSeparator() );
			scan( this.ipAddress.getText(), this.lowerPort.getText(), this.higherPort.getText(), 200 );
			this.output.append( "Scan finished." );
		}
	}
	
	/**
	 * Manages the state change for the JCheckBox
     * @param ce
	 */
        @Override
	public void stateChanged(ChangeEvent ce) {
		if( ce.getSource() == toggleDisplayAll ) {
			this.displayAll = this.toggleDisplayAll.isSelected();
		}
		
	}
	
	/**
	 * Scans the specified ip address and tests the ports in the given range
	 * @param ipAddress The IP address to scan
	 * @param lowPort The port number that the scanner starts with
	 * @param highPort The port number that the scanner ends with
	 * @param timeout The timeout in miliseconds for the scan
	 * 
	 * @see #java.net.InetSocketAddress
	 */
	private void scan( String ipAddress, String lowPort, String highPort, int timeout ) {
		int start, end;
		
		//verify port numbers
		try {
			start = Integer.parseInt( lowPort );
			end = Integer.parseInt( highPort );
			
			if( end <= start ) {
				this.output.append( "The second port must be higher than the first port" + System.lineSeparator() );
				return;
			}
		}
		catch( NumberFormatException nfe ) {
			this.output.append( "Please enter valid port numbers." + System.lineSeparator() );
			return;
		}
		
		//Scan ports in range
		for( int current = start; current <= end; current++ ) {
			try {
                            try (Socket s = new Socket()) {
                                s.connect( new InetSocketAddress( ipAddress, current ), timeout ); //attempt a connection
                            } //attempt a connection
				
				this.output.append( "Open port: " + current + System.lineSeparator() );
			}
			catch( IOException ioe ) { //connection failed
				if( this.displayAll ) {
					this.output.append( "Closed port: " + current + System.lineSeparator() );
				}
			}
		}
	}
	
	/**
	 * Creates an instance of this GUI
     * @param args
	 */
	public static void main( String[] args ) {
		@SuppressWarnings("unused")
		PortScanner psg = new PortScanner();
	}
}
