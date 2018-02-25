package com.oracle.app.parser.ir;

public interface GoIRVisitable {
	public void accept(GoIRVisitor visitor);
}
