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
		arr = new FrameSlot[this.length];
	}
	
	public void insert(FrameSlot slot, int index){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		arr[index] = slot;
	}
	
	public void setType(GoExpressionNode type){
		if(type instanceof GoIntNode){
			this.type = GoPrimitiveTypes.INT;
		}
		else if(type instanceof GoStringNode){
			this.type = GoPrimitiveTypes.STRING;
		}
		else{
			System.out.println("Array Type "+type+" not implemented");
		}
	}
	
	public FrameSlot readArray(int index){
		return arr[index];
	}
	
	@Override
	public String toString() {
		return "GoArray [length=" + length + "]";
	}

	/**
	 * Initialize slot values here
	 */
	@Override
	public GoArray executeGeneric(VirtualFrame frame){
		return this;
	}
	
	public int len(){
		return length;
	}

}
