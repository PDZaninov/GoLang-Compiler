package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoArrayLikeTypes extends GoNonPrimitiveType {

	//public abstract Object readArray(VirtualFrame frame, int index);
	
	public abstract GoPrimitiveTypes getType();
	
	public abstract int len();
	
	public abstract int cap();

	public abstract int lowerBound();
	/*
	public abstract void insert(VirtualFrame frame, int index, int value);
	public abstract void insert(VirtualFrame frame, int index, float value);
	public abstract void insert(VirtualFrame frame, int index, double value);
	public abstract void insert(VirtualFrame frame, int index, boolean value);
	public abstract void insert(VirtualFrame frame, int index, Object value);
	*/
}
