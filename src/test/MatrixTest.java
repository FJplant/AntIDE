/*
 *	package : test
 *	source  : MatrixTest.java
 *	date    : 1999.9.19
 */

package test;

public class MatrixTest {
	final static int MATRIX_NUM = 10;
	final static int MATRIX_ROW = 500;
	final static int MATRIX_COL = 500;
	/**
	 *	MatrixTest -  constructor 
	 */
	public MatrixTest() {
	}
	
	public static void main( String[] args ) {
		int[][] a, b, c;
		a = new int[MATRIX_ROW][MATRIX_COL];
		b = new int[MATRIX_ROW][MATRIX_COL];
		c = new int[MATRIX_ROW][MATRIX_COL];
		
		// fill the contents of the matrix
		System.out.println( " Fill the contents of the matrix..." );
		for ( int i = 0; i < MATRIX_ROW; i++ ) {
			for ( int j = 0; j < MATRIX_COL; j++ ) {
				a[i][j] = i + j;
				b[i][j] = i + j;
				c[i][j] = 0;
			}
		}
		
		// multiply a with b, and input the result into c
		System.out.println( " Multiply matrix a and b " );
		for ( int i = 0; i < MATRIX_ROW; i++ ) {
			for ( int j = 0; j < MATRIX_COL; j++ ) {
				for ( int k = 0; k < MATRIX_COL; k++ ) {
					c[i][j] += a[i][k]*b[k][j];
				}
			}
		}
		
		System.out.println( " The result is : " );
	}
}
