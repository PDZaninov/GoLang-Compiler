 package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Slice class which is just a reference to {@link GoArray}. Can be dynamically sized
 * but it does not grow the array dynamically. Because slices are just references, the
 * slices can be done by converting the index values to the window size the slice references.
 * @author Trevor
 *
 */
public class GoSlice extends GoArrayLikeTypes {

	FrameSlot array;
	GoPrimitiveTypes type;
	int cap;
	int len;
	int low;
	int high;
	
	public GoSlice(FrameSlot array, int low, int high, int cap, GoPrimitiveTypes type){
		this.array = array;
		this.low = low;
		this.high = high-1;
		this.cap = cap;
		len = high - low;
		this.type = type;
	}
	
	public GoSlice(FrameSlot array, int low, int high, int cap){
		this.array = array;
		this.low = low;
		this.high = high-1;
		this.cap = cap;
		len = high - low;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this;
	}

	public FrameSlot getSlot(){
		return array;
	}
	
	@Override
	public int lowerBound(){
		return low;
	}
	
	@Override
	public int len(){
		return len;
	}
	
	public int cap(){
		return cap;
	}
	
	@Override
	public GoPrimitiveTypes getType(){
		return type;
	}
	
	public void setType(GoPrimitiveTypes type) {
		this.type = type;
	}

	/**
	 * When reading from an array in a slice, the index needs to be adjusted for the slice size.
	 */
	@Override
	public FrameSlot readArray(VirtualFrame frame, int index) {
		GoArrayLikeTypes arr = (GoArrayLikeTypes) FrameUtil.getObjectSafe(frame, array);
		int realindex = index + low;
		//Error out, index out of bounds
		if(realindex < low || realindex > high || realindex > cap){
			System.out.println("Slice index out of bounds");
			return null;
		}
		FrameSlot result = arr.readArray(frame, realindex);
		return result;
	}

}
