package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&")
public class GoUnaryAddressNode extends GoUnaryNode {

	private GoExpressionNode value;
	
	public GoUnaryAddressNode(GoExpressionNode value){
		this.value = value;
	}

	@Override
	public String toString(){
		return "& Node";
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return String.format("0x%x", value.hashCode());
	}
}
