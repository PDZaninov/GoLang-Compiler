package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoImportSpec extends GoExpressionNode {
	
	@Child GoIdentNode child;

	public GoImportSpec(GoIdentNode child) {
		this.child = child;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return child.executeGeneric(frame);
	}
}