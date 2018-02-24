package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;

public interface GoTruffleVisitor {
	
	void visitObject(GoBaseIRNode node);
	
	void visitIdent(GoIRIdentNode node);
	
	void visitBinaryExpr(GoIRBinaryExprNode node);
	
	void visitBasicLit(GoIRBasicLitNode node);
	
	void visitInvoke(GoIRInvokeNode node);

}
