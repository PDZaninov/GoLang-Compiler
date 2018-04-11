package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&")
public class GoUnaryAddressNode extends GoUnaryNode {

	private FrameSlot value;
	private boolean isIndex;
	private int index;
	
	public GoUnaryAddressNode(FrameSlot value, boolean isIndex, GoIntNode index){
		this.value = value;
		this.isIndex = isIndex;
		this.index = index.executeInteger(null);
	}
	
	public GoUnaryAddressNode(FrameSlot value){
		this.value = value;
		this.isIndex = false;
		this.index = -1;
	}

	@Override
	public String toString(){
		return "& Node";
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(isIndex){
			GoArray array = (GoArray) FrameUtil.getObjectSafe(frame, value);
			value = array.readArray(frame, index);
		}
		return GoPointerNode.createPointer(value, value.getKind());
	}
}
