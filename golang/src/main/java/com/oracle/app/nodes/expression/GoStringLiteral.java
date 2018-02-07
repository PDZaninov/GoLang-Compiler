package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * A constant string value that doesn't change
 */
@NodeInfo(shortName = "const")
public class GoStringLiteral extends GoExpressionNode {

	private final String value;
	
	public GoStringLiteral(String value){
		this.value = value;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}

}
