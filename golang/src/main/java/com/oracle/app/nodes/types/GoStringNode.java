package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoStringNode extends GoPrimitiveType {
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

	@Override
	public GoPrimitiveTypes getType() {
		return GoPrimitiveTypes.STRING;
	}

	

}