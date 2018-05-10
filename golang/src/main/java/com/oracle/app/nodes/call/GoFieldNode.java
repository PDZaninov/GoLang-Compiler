package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFieldNode extends GoExpressionNode{

	@Child GoArrayExprNode names;
	@Child GoReadLocalVariableNode type;
	
	public GoFieldNode(GoArrayExprNode names, GoReadLocalVariableNode type) {
		this.names = names;
		this.type = type;
	}

	public GoExpressionNode[] getNames(){
		return names.getArguments();
	}
	
	public GoIdentNode getIdentifier() {
		return (GoIdentNode) names.getArguments()[0];
	}

	/**
	 *	Temporary way to get names only when there is one name in the field
	 * Idea - should create a seperate field node that holds one name and type 
	 * and separate this field in gotruffle
	 */
	@Override
	public String getName() {
		return ((GoIdentNode) names.getArguments()[0]).getName();
	}

	public GoReadLocalVariableNode getType() {
		return type; 
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