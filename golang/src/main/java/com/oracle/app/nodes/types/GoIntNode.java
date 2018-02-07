package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoIntNode extends GoExpressionNode {
    public final int number;

    public GoIntNode(int number) {
        this.number = number;
    }

    @Override
    public int executeInteger(VirtualFrame virtualFrame) {
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