package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class GoIndexExprNode extends GoBinaryNode{

	
	
    @Specialization
    protected Object doIndex(GoArray array, int index) {
    	//System.out.println("Indexnode reads: " + array.readArray(index));
        return array.readArray(index);
    }
    
}
