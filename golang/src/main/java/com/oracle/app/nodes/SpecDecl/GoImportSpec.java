package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoImportSpec extends GoExpressionNode {
	
	@Child
	GoStringNode child;

	public GoImportSpec(GoStringNode child) {
		this.child = child;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return child.executeGeneric(frame);
	}
}