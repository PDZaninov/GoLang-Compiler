package com.oracle.app.nodes.types;

public abstract class GoArrayLikeTypes extends GoNonPrimitiveType {

	//public abstract Object readArray(VirtualFrame frame, int index);
	
	public abstract GoPrimitiveTypes getType();
	
	public abstract int len();
	
	public abstract int cap();

	public abstract int lowerBound();
	
	public abstract Object read(Object index);

	public abstract void insert(Object index, Object value);
	
}
