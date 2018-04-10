package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoSlice extends GoArrayLikeTypes {

	FrameSlot array;
	GoPrimitiveTypes type;
	int cap;
	int len;
	int low;
	int high;
	
	public GoSlice(FrameSlot array, int low, int high, int cap){
		this.array = array;
		this.low = low;
		this.high = high;
		this.cap = cap;
		len = high - low;
	}
	
	@Override
	public Object fillCompositeFields(VirtualFrame frame, GoArrayExprNode elts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GoPrimitiveTypes getType(){
		return type;
	}
	
	public void setType(GoPrimitiveTypes type) {
		this.type = type;
	}

	@Override
	public FrameSlot readArray(VirtualFrame frame, int index) {
		GoArray arr = (GoArray) FrameUtil.getObjectSafe(frame, array);
		int realindex = index + low;
		//Error out, index out of bounds
		if(realindex < low || realindex > len){
			System.out.println("Slice index out of bounds");
			return null;
		}
		FrameSlot result = arr.readArray(frame, realindex);
		return result;
	}

}
