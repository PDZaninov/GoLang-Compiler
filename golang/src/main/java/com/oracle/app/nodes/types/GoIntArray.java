package com.oracle.app.nodes.types;

import java.util.Arrays;

import com.oracle.truffle.api.frame.VirtualFrame;


public class GoIntArray extends GoArray{

	private int[] array;
	
	public GoIntArray(int size) {
		array = new int[size];
		length = array.length;
	}
	
	public void setArray(int index, int value){
		array[index] = value;
	}
	
	@Override
	public Object readArray(int index) {
		return array[index];
	}
	
	@Override
	public int len(GoArray a) {
		return a.length;
	}

	@Override
	public GoArray executeGoArray(VirtualFrame virtualFrame) {
		return this;
	}

	@Override
	public GoArray executeGeneric(VirtualFrame frame) {
		return this;
	}

	@Override
	public String toString() {
		return "GoIntArray [array=" + Arrays.toString(array) + "]";
	}


}
