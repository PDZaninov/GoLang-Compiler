package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoIRValueSpecNode;

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

	Object visitAssignStmt(GoIRAssignStmtNode goIRAssignStmtNode);

}
