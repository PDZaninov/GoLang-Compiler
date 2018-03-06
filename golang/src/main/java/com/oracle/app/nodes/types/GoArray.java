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
    	Object[] array = {0,0,99,0,0,0,0,0};
    	this.array = array;
    }
    
    public Object[] getArray() {
    	return array;
    }
    
    public void setArray(Object[] array) {
    	this.array= array;
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
    	String m = "ToString:";
    	for(int a = 0; a < array.length; a ++) {
    		m += a+ ": " + array[a] +"\n";
    	}
        return m;
    }


}
