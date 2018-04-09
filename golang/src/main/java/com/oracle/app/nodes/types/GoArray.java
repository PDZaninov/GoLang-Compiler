package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
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
	
	public void setType(String type){
		switch(type){
		case "int":
			this.type = GoPrimitiveTypes.INT;
			break;
		case "string":
			this.type = GoPrimitiveTypes.STRING;
			break;
		default:
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
		switch(type){
		case BOOL:
			fillArray(frame,false,FrameSlotKind.Boolean);
			break;
		case FLOAT64:
			fillArray(frame,(float)0,FrameSlotKind.Float);
			break;
		case INT:
			fillArray(frame,0,FrameSlotKind.Int);
			break;
		case STRING:
			fillArray(frame,"",FrameSlotKind.Object);
			break;
		default:
			break;
		}
		return this;
	}
	
	protected void fillArray(VirtualFrame frame, Object value, FrameSlotKind type){
		CompilerDirectives.transferToInterpreter();
		for(int i = 0; i < length; i++){
			arr[i].setKind(type);
			frame.setObject(arr[i], value);
		}
	}
	
	public int len(){
		return length;
	}

}
