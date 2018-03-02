package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.*;

public interface GoIRVisitor {
	
	Object visitObject(GoBaseIRNode node);
	
	Object visitIdent(GoIRIdentNode node);
	
	Object visitBinaryExpr(GoIRBinaryExprNode node);
	
	Object visitBasicLit(GoIRBasicLitNode node);
	
	Object visitInvoke(GoIRInvokeNode node);
	
	Object visitGenericDispatch(GoIRGenericDispatchNode node);

	Object visitFuncDecl(GoIRFuncDeclNode node);

	Object visitArrayListExpr(GoIRArrayListExprNode node);

	Object visitDecl(GoIRDeclNode node);

	Object visitBlockStmt(GoIRBlockStmtNode node);

	Object visitExprStmt(GoIRExprStmtNode node);
	
	Object visitExpr(GoIRExprNode node);
	
	Object visitStmt(GoIRStmtNode node);
	
	Object visitUnary(GoIRUnaryNode node);

	Object visitDeclStmt(GoIRDeclStmtNode goIRDeclStmtNode);

	Object visitGenDecl(GoIRGenDeclNode goIRGenDeclNode);

	Object visitValueSpec(GoIRValueSpecNode goIRValueSpecNode);

    Object visitIf(GoIRIfStmtNode goIRIfNode);
}
