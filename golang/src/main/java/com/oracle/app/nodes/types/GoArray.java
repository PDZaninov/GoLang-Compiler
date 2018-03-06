package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;



public class GoArray extends GoExpressionNode {
    public Object[] array;

    public GoArray(Object[] arr) {
        this.array = arr;
    }

    public GoArray() {
    	super();
    	Object[] array = {0,0};
    	this.array = array;
    }
    
    @Override
    public Object[] executeGoArray(VirtualFrame virtualFrame) {
        return this.array;
    }

	@Override
	public Object[] executeGeneric(VirtualFrame frame) {
		return array;
	}
    
    @Override
    public String toString() {
    	String m = "";
    	for(int a = 0; a < array.length; a ++) {
    		m += a+ ": " + array[a] +"\n";
    	}
        return m;
    }


}
