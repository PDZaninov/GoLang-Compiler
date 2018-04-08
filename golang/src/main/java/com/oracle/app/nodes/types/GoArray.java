package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;


public class GoArray extends GoExpressionNode {
	protected int length;
	protected GoPrimitiveTypes type;
	protected FrameSlot[] arr;
	
	public GoArray(GoIntNode length){
		this.length = length.executeInteger(null);
	}
	
	public void insert(FrameSlot slot, int index){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		arr[index] = slot;
	}
	
	@Override
	public String toString() {
		
		return "GoArray [length=" + length + "]";
	}

	@Override
	public GoArray executeGeneric(VirtualFrame frame){
		return null;
	}
	
	public int len(){
		return length;
	}

}
