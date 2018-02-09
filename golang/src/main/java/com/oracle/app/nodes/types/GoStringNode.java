package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoStringNode extends GoExpressionNode {
	public final String str;

    public GoStringNode(String str) {
        this.str = str;
    }

    @Override
    public String executeString(VirtualFrame frame) {
        return this.str;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.str;
	}

	@Override
	public String toString() {
		return "String Node " + str;
	}

	

}