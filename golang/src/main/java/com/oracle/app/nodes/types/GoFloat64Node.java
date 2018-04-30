package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFloat64Node extends GoPrimitiveType {
    public final double number;

    public GoFloat64Node(double number) {
        this.number = number;
    }

    @Override
    public double executeDouble(VirtualFrame virtualFrame) {
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

	@Override
	public GoPrimitiveTypes getType() {
		return GoPrimitiveTypes.FLOAT64;
	}
}