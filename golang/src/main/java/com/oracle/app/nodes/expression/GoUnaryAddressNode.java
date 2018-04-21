package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.app.nodes.types.GoArrayLikeTypes;
import com.oracle.app.nodes.types.GoPointerNode;
import com.oracle.app.nodes.types.GoPointerNode.GoArrayIndexPointerNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&")
public class GoUnaryAddressNode extends GoUnaryNode {

	private FrameSlot value;
	private boolean isIndex;
	private GoExpressionNode index;
	
	public GoUnaryAddressNode(FrameSlot value, GoExpressionNode index){
		this.value = value;
		this.isIndex = true;
		this.index = index;
	}
	
	public GoUnaryAddressNode(FrameSlot value){
		this.value = value;
		this.isIndex = false;
	}

	@Override
	public String toString(){
		return "& Node";
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(isIndex){
			GoArrayLikeTypes array = (GoArrayLikeTypes) FrameUtil.getObjectSafe(frame, value);
			Object arrayindex = index.executeGeneric(frame);
			return new GoArrayIndexPointerNode(array.hashCode(),array,arrayindex);
		}
		return GoPointerNode.createPointer(value, value.getKind());
	}
}
