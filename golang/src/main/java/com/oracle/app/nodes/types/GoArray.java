package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;


public class GoArray extends GoArrayLikeTypes{
	protected int length;
	protected GoPrimitiveTypes type;
	protected FrameSlot[] arr;
	
	public GoArray(int length, GoPrimitiveTypes type, FrameSlot[] arr){
		this.length = length;
		this.arr = arr;
		this.type = type;
	}
	
	public GoArray(int length) {
		this.length = length;
		arr = new FrameSlot[this.length];
	}

	public int len(){
		return length;
	}
	
	public int cap(){
		return length;
	}
	
	public void insert(VirtualFrame frame, int index, int value){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		frame.setInt(arr[index], value);
	}
	
	public void insert(VirtualFrame frame, int index, float value){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		frame.setFloat(arr[index], value);
	}
	
	public void insert(VirtualFrame frame, int index, boolean value){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		frame.setBoolean(arr[index], value);
	}

	public void insert(VirtualFrame frame, int index, Object value){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		frame.setObject(arr[index], value);
	}
	
	/**
	 * Inserts new frameslots for a new array
	 * @param slot - The new slot to insert
	 * @param index - The index to insert the new frameslot in
	 */
	public void insert(FrameSlot slot, int index){
		if(index > length || index < 0){
			//Throws error
			System.out.println("Invalid array index");
			return;
		}
		arr[index] = slot;
	}
	
	@Override
	public FrameSlot readArray(VirtualFrame frame, int index){
		return arr[index];
	}
	
	@Override
	public int lowerBound(){
		return 0;
	}
	
	/**
	 * Will need to change to account for objects, not just primitives
	 * @return - The type of array
	 */
	@Override
	public GoPrimitiveTypes getType(){
		return type;
	}
	
	@Override
	public String toString() {
		return "GoArray [length=" + length + "]";
	}

	/**
	 * Needs to return a copy of itself?? or a reference to itself??
	 */
	@Override
	public GoArray executeGeneric(VirtualFrame frame){
		return this;
	}

}
