package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoSelectorExprNode extends GoExpressionNode {

    @Child private GoExpressionNode expr;
    @Child private GoIdentNode name;


    public GoSelectorExprNode(GoExpressionNode expr, GoIdentNode name) {
        this.expr = expr;
        this.name = name;
    }

    //Only covering for the case of a struct selector currently
    //TO-DO Make sure this does not overlap with import selectors
    @Override
    public Object executeGeneric(VirtualFrame frame) {
    	GoExpressionNode result = (GoExpressionNode) expr.executeGeneric(frame);
    	if(result instanceof GoStruct){
    		return ((GoStruct) result).read(name.getName());
    	}
    	else{
    		//The selector expression variable was not found in the framedescriptor, error
    		System.out.println("Undefined selector");
    		return null;
    	}
    }

}
