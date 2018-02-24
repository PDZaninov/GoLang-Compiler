package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;

public interface GoIRVisitor {
	
	void visitObject(GoBaseIRNode node);
	
	void visitIdent(GoIRIdentNode node);
	
	void visitBinaryExpr(GoIRBinaryExprNode node);
	
	void visitBasicLit(GoIRBasicLitNode node);
	
	void visitInvoke(GoIRInvokeNode node);
	
	void visitGenericDispatch(GoIRGenericDispatchNode node);

	void visitFuncDecl(GoIRFuncDeclNode goIRFuncDeclNode);
}
