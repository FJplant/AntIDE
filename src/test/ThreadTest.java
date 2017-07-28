/*
 *	package : test
 *	source  : ThreadTest.java
 *	date    : 1999.9.4
 */

package test;

public class ThreadTest {
  final static int NUM_WORKERS = 200;
	static int workerCount = 0;
	private WorkerThread[] workers = new WorkerThread[NUM_WORKERS];
	
	/**
	 *	ThreadTest -  constructor 
	 */
	public ThreadTest() {
		while ( workerCount < NUM_WORKERS ) {
			workers[workerCount] = new WorkerThread( workerCount );
			workerCount++;
		}
		
		int count = 0;
		while ( count < workerCount ) {
		  System.out.println(" Starting worker: " + count + "...");
		  workers[count].start();
		  count ++;
		}
	}

	public static void main( String[] args ) {
		ThreadTest test = new ThreadTest();
	}

	public class WorkerThread extends Thread {
		int id;
		public WorkerThread( int id ) {		  
			this.id = id;
			setPriority( Thread.MIN_PRIORITY );
			System.out.println(" Thread " + id + " generated..." );
		}
		
		public void run( ) {
			long count = 0;
			long temp = 0;
			while ( true ) {
				count++;
				temp = count * count;
				if ( ( count % 100000 ) == 0 )
					System.out.println( "ID " + id + ": " + count );

				// 1000 카운트 후에 0.1초 쉰다.
				if ( ( count % 1000 ) == 0 ) {
					try {
						Thread.currentThread().sleep( 100 );
					} catch ( InterruptedException e ) {
						System.out.println( e.getMessage() );
					}
				}
			}
		}
	}
}
