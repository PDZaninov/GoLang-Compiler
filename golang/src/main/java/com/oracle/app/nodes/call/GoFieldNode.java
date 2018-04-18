package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFieldNode extends GoExpressionNode{

	@Child GoArrayExprNode names;
	@Child GoIdentNode type;
	
	String typeName;
	
	public GoFieldNode(GoArrayExprNode names, GoIdentNode type, String typeName) {
		this.names = names;
		this.type = type;
		this.typeName = typeName;
	}

	public GoExpressionNode[] getNames(){
		return names.getArguments();
	}
	
	public GoIdentNode getIdentifier() {
		return (GoIdentNode) names.getArguments()[0];
	}

	public String getName() {
		return ((GoIdentNode) names.getArguments()[0]).getName();
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(names != null) {
			FrameDescriptor frameDescriptor = frame.getFrameDescriptor();
			String name = (String) ((Object[]) names.executeGeneric(frame))[0];
			FrameSlot slot = frameDescriptor.findFrameSlot(name);
			frame.setInt(slot, (int) frame.getArguments()[0]);
			//return names.executeGeneric(frame);
		}
		return null;
	}
}