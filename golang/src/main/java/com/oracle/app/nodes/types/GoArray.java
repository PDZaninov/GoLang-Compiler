package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;


public class GoArray extends GoNonPrimitiveType {
	protected int length;
	protected GoPrimitiveTypes type;
	protected FrameSlot[] arr;
	
	public GoArray(GoIntNode length){
		this.length = length.executeInteger(null);
		arr = new FrameSlot[this.length];
	}
	
	public int len(){
		return length;
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
	
	/**
	 * Will need to change to account for objects, not just primitives
	 * @return - The type of array
	 */
	public GoPrimitiveTypes getType(){
		return type;
	}
	
	@Override
	public String toString() {
		return "GoArray [length=" + length + "]";
	}

	/**
	 * Default array initialized
	 */
	@Override
	public GoArray executeGeneric(VirtualFrame frame){
		CompilerDirectives.transferToInterpreter();
		switch(type){
		case BOOL:
			for(int i = 0; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Boolean);
				frame.setBoolean(arr[i], false);
			}
			break;
		case FLOAT64:
			for(int i = 0; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Float);
				frame.setFloat(arr[i], (float) 0);
			}
			break;
		case INT:
			for(int i = 0; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Int);
				frame.setInt(arr[i], (int) 0);
			}
			break;
		case STRING:
			for(int i = 0; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Object);
				frame.setObject(arr[i], "");
			}
			break;
		default:
			break;
		}
		return this;
	}

	/**
	 * Array initialized with user inputted fields
	 */
	@Override
	public Object fillCompositeFields(VirtualFrame frame, GoArrayExprNode elts) {
		Object[] results = elts.gatherResults(frame);
		//GoExpressionNode[] writes = new GoExpressionNode[results.length];
		int i = 0;
		switch(type){
		case BOOL:
			for(; i < results.length; i++){
				frame.setBoolean(arr[i], (boolean) results[i]);
			}
			for(;i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Boolean);
				frame.setBoolean(arr[i], false);
			}
			break;
		case FLOAT64:
			for(; i < results.length; i++){
				frame.setFloat(arr[i], (float) results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Float);
				frame.setFloat(arr[i], (float) 0);
			}
			break;
		case INT:
			for(; i < results.length; i++){
				frame.setInt(arr[i], (int) results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Int);
				frame.setInt(arr[i], (int) 0);
			}
			break;
		case STRING:
			for(; i < results.length; i++){
				frame.setObject(arr[i], results[i]);
			}
			for(; i < arr.length; i++){
				arr[i].setKind(FrameSlotKind.Object);
				frame.setObject(arr[i], "");
			}
			break;
		default:
			break;
		
		}
		return this;
	}

}
