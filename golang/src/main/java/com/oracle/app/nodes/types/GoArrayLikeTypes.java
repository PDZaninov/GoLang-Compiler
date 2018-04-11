package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoArrayLikeTypes extends GoNonPrimitiveType {

	public abstract FrameSlot readArray(VirtualFrame frame, int index);
	
	public abstract GoPrimitiveTypes getType();
	
	public abstract int len();
	
	public abstract int cap();

	public abstract int lowerBound();

}
