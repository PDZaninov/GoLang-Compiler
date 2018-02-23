package com.oracle.app.parser.ir;

public interface GoTruffleVisitor {
	
	Object visitObject(GoBaseIRNode node, Object obj);

}
