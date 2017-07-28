/*
 *	package : test
 *	source  : SocketTest.java
 *	date    : 1999.9.24
 */

package test;

import java.net.*;
import java.io.*;

public class SocketTest {
	final static int MAX_CONNECTION = 2000;
	public SocketTest( int port ) {
		ServerListener listener = new ServerListener( port );
		ClientSocket client = new ClientSocket( port );
		
		listener.start();
		client.start();
	}
	
	public static void main( String[] args ) {
		int port = 2020;
		if ( args.length > 0 )
			port = Integer.parseInt( args[0] );

		new SocketTest( port );		
	}	

	class ServerListener extends Thread {
		Socket[] serverEndPoint = new Socket[MAX_CONNECTION];

		int connectedCount = 0;
		ServerSocket listener;
		public ServerListener( int port ) {
			try {
				listener = new ServerSocket( port, 10 );
				System.out.println( "Listening to " + port );
			} catch ( IOException e ) {
				System.err.println( e.getMessage() );
			}
		}
		
		public void run() {
			while ( connectedCount < serverEndPoint.length ) {
				try {
					serverEndPoint[connectedCount] = listener.accept();
					System.out.println( "[Server] " + connectedCount + ": " + serverEndPoint[connectedCount] );				
					connectedCount++;
				} catch ( IOException e ) {
					System.err.println( e.getMessage() );
					break;
				}
			}
		}

		public void finalize() {
			System.out.println( "server finalize() called" );
			for ( int i = 0; i < serverEndPoint.length; i++ ) {
				if ( serverEndPoint[i] != null ) {
					try {
						serverEndPoint[i].close();
					} catch ( IOException e ) {
			
					}
				}
			}
		}
	}

	class ClientSocket extends Thread {
		Socket[] clientEndPoint = new Socket[MAX_CONNECTION];
		int port = 2020;	

		int connectedCount = 0;
		
		public ClientSocket( int port ) {
			this.port = port;
		}

		public void run() {
			while ( connectedCount < clientEndPoint.length ) {
				try {
					clientEndPoint[connectedCount] = new Socket( "127.0.0.1", port );
					System.out.println( "[Client] " + connectedCount + ": " + clientEndPoint[connectedCount] );
					connectedCount++;
					Thread.currentThread().sleep( 50 );
				} catch ( UnknownHostException e ) {
					System.err.println( e.getMessage() );
					break;
				} catch ( IOException e ) {
					System.err.println( e.getMessage() );
					break;
				} catch ( InterruptedException e ) {
					System.err.println( e.getMessage() );
				}
			}
		}

		public void finalize() {
			System.out.println( "finalize() called" );
			for ( int i = 0; i < clientEndPoint.length; i++ ) {
				if ( clientEndPoint[i] != null ) {
					try {
						clientEndPoint[i].close();
					} catch ( IOException e ) {
					}
				}
			}
		}
	}
}
