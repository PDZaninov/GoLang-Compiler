package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoIndexExprNode extends GoBinaryNode{

	
    @Specialization
    protected Object doIndex(Object[] array, int index) {
        return array[index];
    }
}
