package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFieldNode extends GoExpressionNode{

	private final String name;
	private final GoReadLocalVariableNode type;
	
	public GoFieldNode(String names, GoReadLocalVariableNode type) {
		this.name = names;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public GoReadLocalVariableNode getType() {
		return type; 
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		/*TODO see if i can delete
		if(name != null) {
			FrameDescriptor frameDescriptor = frame.getFrameDescriptor();
			FrameSlot slot = frameDescriptor.findFrameSlot(name);
			frame.setObject(slot, frame.getArguments()[0]);
		}*/
		return null;
	}
}