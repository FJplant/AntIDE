
package com.antsoft.ant.pool.sourcepool;

import java.util.Vector;
import java.io.Serializable;
import javax.swing.undo.UndoableEdit;

public abstract class MyGapVector implements Serializable {


    /**
     * Creates a new GapVector object.  Initial size defaults to 10.
     */
    public MyGapVector() {
	this(10);
    }

    /**
     * Creates a new GapVector object, with the initial
     * size specified.
     *
     * @param initialLength the initial size
     */
    public MyGapVector(int initialLength) {
	array = allocateArray(initialLength);
	g0 = 0;
	g1 = initialLength;
    }

    /**
     * Allocate an array to store items of the type
     * appropriate (which is determined by the subclass).
     */
    protected abstract Object allocateArray(int len);
	
    /**
     * Get the length of the allocated array
     */
    protected abstract int getArrayLength();

    /**
     * Access to the array.  The actual type
     * of the array is known only by the subclass.
     */
    protected final Object getArray() {
	return array;
    }

    /**
     * Access to the start of the gap.
     */
    protected final int getGapStart() {
	return g0;
    }

    /**
     * Access to the end of the gap.
     */
    protected final int getGapEnd() {
	return g1;
    }

    // ---- variables -----------------------------------

    /**
     * The array of items.  The type is determined by the subclass.
     */
    private Object array;

    /**
     * start of gap in the array
     */
    private int g0;

    /**
     * end of gap in the array
     */
    private int g1;


    // --- gap management -------------------------------

    /**
     * Replace the given logical position in the storage with
     * the given new items.  This will move the gap to the area
     * being changed if the gap is not currently located at the
     * change location.
     *
     * @param position the location to make the replacement.  This
     *  is not the location in the underlying storage array, but
     *  the location in the contiguous space being modeled.
     * @param rmSize the number of items to remove
     * @param addItems the new items to place in storage.
     */
    protected void replace(int position, int rmSize, Object addItems, int addSize) {
	int addOffset = 0;
	if (addSize == 0) {
	    close(position, rmSize);
	    return;
	} else if (rmSize > addSize) {
	    /* Shrink the end. */
	    close(position+addSize, rmSize-addSize);
	} else {
	    /* Grow the end, do two chunks. */
	    int endSize = addSize - rmSize;
	    int end = open(position + rmSize, endSize);
	    System.arraycopy(addItems, rmSize, array, end, endSize);
	    addSize = rmSize;
	}
	System.arraycopy(addItems, addOffset, array, position, addSize);
    }

    /** 
     * Delete nItems at position.  Squeezes any marks 
     * within the deleted area to position.  This moves
     * the gap to the best place by minimizing it's 
     * overall movement.  The gap must intersect the
     * target block.
     */
    void close(int position, int nItems) {
	if (nItems == 0)  return;

	int end = position + nItems;
	int new_gs = (g1 - g0) + nItems;
	if (end <= g0) {
	    // Move gap to end of block.
	    if (g0 != end) {
		shiftGap(end);
	    }
	    // Adjust g0.
	    shiftGapStartDown(g0 - nItems);
	} else if (position >= g0) {
	    // Move gap to beginning of block.
	    if (g0 != position) {
		shiftGap(position);
	    }
	    // Adjust g1. 
	    shiftGapEndUp(g0 + new_gs);
	} else {
	    // The gap is properly inside the target block.
	    // No data movement necessary, simply move both gap pointers.
	    shiftGapStartDown(position);
	    shiftGapEndUp(g0 + new_gs);
	}
    }

    /**
     * Make space for the given number of items at the given
     * location.  
     *
     * @returns the location that the caller should fill in.
     */
    int open(int position, int nItems) {
	int gapSize = g1 - g0;
	if (nItems == 0) {
	    if (position > g0)  
		position += gapSize;
	    return position;
	}

	// Expand the array if the gap is too small.
	shiftGap(position);
	if (nItems >= gapSize) {
	    // Pre-shift the gap, to reduce total movement.
	    shiftEnd(getArrayLength() - gapSize + nItems);
	    gapSize = g1 - g0;
	}

	g0 = g0 + nItems;
	return position;
    }

    /** 
     * resize the underlying storage array to the
     * given new size
     */
    void resize(int nsize) {
	Object narray = allocateArray(nsize);
	System.arraycopy(array, 0, narray, 0, Math.min(nsize, getArrayLength()));
	array = narray;
    }

    /**
     * Make the gap bigger, moving any necessary data and updating 
     * the appropriate marks
     */
    protected void shiftEnd(int newSize) {
	int oldSize = getArrayLength();
	int oldGapEnd = g1;
	int upperSize = oldSize - oldGapEnd;
	int arrayLength = (newSize + 1) * 2;
	int newGapEnd = arrayLength - upperSize;
	resize(arrayLength);
	g1 = newGapEnd;

	if (upperSize != 0) {
	    // Copy array items to new end of array.
	    System.arraycopy(array, oldGapEnd, array, newGapEnd, upperSize);
	}
    }

    /**
     * Move the start of the gap to a new location,
     * without changing the size of the gap.  This 
     * moves the data in the array and updates the
     * marks accordingly.
     */
    protected void shiftGap(int newGapStart) {
	if (newGapStart == g0) {
	    return;
	}
	int oldGapStart = g0;
	int dg = newGapStart - oldGapStart;
	int oldGapEnd = g1;
	int newGapEnd = oldGapEnd + dg;
	int gapSize = oldGapEnd - oldGapStart;

	g0 = newGapStart;
	g1 = newGapEnd;
	if (dg > 0) {
	    // Move gap up, move data down.
	    System.arraycopy(array, oldGapEnd, array, oldGapStart, dg);
	} else if (dg < 0) {
	    // Move gap down, move data up.
	    System.arraycopy(array, newGapStart, array, newGapEnd, -dg);
	}
    }

    /**
     * Adjust the gap end downward.  This doesn't move
     * any data, but it does update any marks affected 
     * by the boundry change.  All marks from the old
     * gap start down to the new gap start are squeezed
     * to the end of the gap (their location has been
     * removed).
     */
    protected void shiftGapStartDown(int newGapStart) {
	g0 = newGapStart;
    }

    /**
     * Adjust the gap end upward.  This doesn't move
     * any data, but it does update any marks affected 
     * by the boundry change. All marks from the old
     * gap end up to the new gap end are squeezed
     * to the end of the gap (their location has been
     * removed).
     */
    protected void shiftGapEndUp(int newGapEnd) {
	g1 = newGapEnd;
    }

}

