package com.oracle.app.nodes.types;

import java.util.ArrayList;

import com.oracle.truffle.api.frame.VirtualFrame;


public class GoIntSlice extends GoSlice {

	private ArrayList<Integer> slice;
	
	public GoIntSlice(int size, int capacity) {
		slice = new ArrayList<Integer>(size);
		length = slice.size();
		this.capacity = capacity;
	}
	
	public GoIntSlice(int size) {
		slice = new ArrayList<>(size);
		length = slice.size();
	}
	
	public void setSlice(int index, int value){
		slice.add(index, value);
	}
	
	@Override
	public Object readSlice(int index) {
		return slice.get(index);
	}
	
	@Override
	public int len(GoSlice a) {
		return a.length;
	}

	@Override
	public GoSlice executeGoSlice(VirtualFrame virtualFrame) {
		return this;
	}

	@Override
	public GoSlice executeGeneric(VirtualFrame frame) {
		return this;
	}

	@Override
	public String toString() {
		return "GoIntSlice[]";
	}


}
