package com.oracle.app.parser.ir;

public interface GoIRVisitor {
	
	Object visitObject(GoBaseIRNode node, Object obj);
}
