package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoIntArray extends GoArray{

	public int[] array;
	
	public GoIntArray(int size) {
		array = new int[size];
	}
	
	@Override
	public Object readArray(int index) {
		return array[index];
	}

	@Override
	public GoArray executeGoArray(VirtualFrame virtualFrame) {
		return this;
	}

	@Override
	public GoArray executeGeneric(VirtualFrame frame) {
		return this;
	}



}
