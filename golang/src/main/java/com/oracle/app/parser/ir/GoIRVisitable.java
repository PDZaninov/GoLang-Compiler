package com.oracle.app.parser.ir;

public interface GoIRVisitable {
	public Object accept(GoIRVisitor visitor);
}
