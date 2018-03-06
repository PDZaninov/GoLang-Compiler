package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoIndexExprNode extends GoBinaryNode{

	
    @Specialization
    protected Object doIndex(Object[] array, int index) {
    	System.out.println("reading index");
    	System.out.println(array.toString());
    	System.out.println(index);
        return array[index];
    }
    
    @Specialization
    protected Object doIndex(int array, int index) {
    	System.out.println("reading index");
    	System.out.println(array);
    	System.out.println(index);
        return array;
    }
}
