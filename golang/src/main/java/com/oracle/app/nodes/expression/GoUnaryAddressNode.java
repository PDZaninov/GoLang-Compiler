package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&")
public class GoUnaryAddressNode extends GoUnaryNode {

	private FrameSlot value;
	
	public GoUnaryAddressNode(FrameSlot value){
		this.value = value;
	}

	@Override
	public String toString(){
		return "& Node";
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return GoPointerNode.createPointer(value, value.getKind());
	}
}
