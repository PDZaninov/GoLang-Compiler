package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoLongNode extends GoExpressionNode {
    public final long number;

    public GoLongNode(long number) {
        this.number = number;
    }

    @Override
    public long executeLong(VirtualFrame virtualFrame) {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + this.number;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.number;
	}
}