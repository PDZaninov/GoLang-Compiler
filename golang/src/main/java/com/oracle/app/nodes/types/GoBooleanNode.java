package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoBooleanNode extends GoExpressionNode {
    public final boolean value;

    public GoBooleanNode(boolean bool) {
        this.value = bool;
    }

    @Override
    public boolean executeBoolean(VirtualFrame virtualFrame) {
        return this.value;
    }


    @Override
    public String toString() {
        if (this.value) {
            return "true";
        } else {
            return "false";
        }
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.value;
	}
}