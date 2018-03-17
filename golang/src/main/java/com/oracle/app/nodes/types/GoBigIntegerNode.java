package com.oracle.app.nodes.types;

import java.math.BigInteger;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;



public class GoBigIntegerNode extends GoExpressionNode {
	public final BigInteger value;

    public GoBigIntegerNode(BigInteger number) {
        this.value = number;
    }

    @Override
    public BigInteger executeBigInteger(VirtualFrame virtualFrame) {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.value;
	}
}