package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFloatNode extends GoExpressionNode {
    public final float number;

    public GoFloatNode(float number) {
        this.number = number;
    }

    @Override
    public float executeFloat(VirtualFrame virtualFrame) {
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