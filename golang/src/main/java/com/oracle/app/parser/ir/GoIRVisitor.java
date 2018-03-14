package com.oracle.app.parser.ir;


import com.oracle.app.nodes.expression.GoIndexExprNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode.GoReadArrayNode;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode.GoIRStringNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBranchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRCaseClauseNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRForNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIfStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIncDecStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRSwitchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoIRValueSpecNode;
import com.oracle.app.parser.ir.nodes.GoIRWriteIndexNode;


public interface GoIRVisitor {
	
	default Object visitObject(GoBaseIRNode node){
		System.out.println("Default Base IR Node visit");
		return null;
	}
	
	default Object visitIdent(GoIRIdentNode node){
		System.out.println("Default Identifier Visit");
		return null;
	}
	
	default Object visitBinaryExpr(GoIRBinaryExprNode node){
		System.out.println("Default Binary Expression Visit");
		return null;
	}
	
	default Object visitInvoke(GoIRInvokeNode node){
		System.out.println("Default Invoke Visit");
		return null;
	}
	
	default Object visitGenericDispatch(GoIRGenericDispatchNode node){
		System.out.println("Default Generic Dispatch Visit, USELESS NODE");
		return null;
	}

	default Object visitFuncDecl(GoIRFuncDeclNode node){
		System.out.println("Default Function Declaration Visit");
		return null;
	}

	default Object visitArrayListExpr(GoIRArrayListExprNode node){
		System.out.println("Default ArrayList Expression Visit");
		return null;
	}
	
	default Object visitDecl(GoIRDeclNode node){
		System.out.println("Default Decl Visit");
		return null;
	}

	default Object visitBlockStmt(GoIRBlockStmtNode node){
		System.out.println("Default Block Statement Visit");
		return null;
	}

	default Object visitExprStmt(GoIRExprStmtNode node){
		System.out.println("Default Expression Statement Visit");
		return null;
	}
	
	default Object visitExpr(GoIRExprNode node){
		System.out.println("Default Expression Visit");
		return null;
	}
	
	default Object visitStmt(GoIRStmtNode node){
		System.out.println("Default Statement Visit");
		return null;
	}
	
	default Object visitUnary(GoIRUnaryNode node){
		System.out.println("Default Unary Operator Visit");
		return null;
	}

	default Object visitDeclStmt(GoIRDeclStmtNode goIRDeclStmtNode){
		System.out.println("Default Decl Statement Visit");
		return null;
	}

	default Object visitGenDecl(GoIRGenDeclNode goIRGenDeclNode){
		System.out.println("Default Gen Decl Visit");
		return null;
	}

	default Object visitValueSpec(GoIRValueSpecNode goIRValueSpecNode){
		System.out.println("Default Value Spec Visit");
		return null;
	}
	
	default Object visitForLoop(GoIRForNode node){
		System.out.println("Default For Loop Visit");
		return null;
	}
	
	default Object visitIncDecStmt(GoIRIncDecStmtNode node){
		System.out.println("Default Increment Statement Visit");
		return null;
	}
	
	default Object visitBranchStmt(GoIRBranchStmtNode node){
		System.out.println("Default Branch Statement Visit");
		return null;
	}

	default Object visitCaseClause(GoIRCaseClauseNode goIRCaseClauseNode){
		System.out.println("Default Case Clause Visit");
		return null;
	}

	default Object visitSwitchStmt(GoIRSwitchStmtNode goIRSwitchStmtNode){
		System.out.println("Default Switch Statement Visit");
		return null;
	}

	default Object visitIfStmt(GoIRIfStmtNode goIRIfStmtNode){
		System.out.println("Default If Statement Visit");
		return null;
	}

	default Object visitArrayType(GoIRArrayTypeNode goIRArrayTypeNode){
		System.out.println("Default Array Type Visit");
		return null;
	}

	default GoReadArrayNode visitIndexNode(GoIRIndexNode goIRIndexNode){
		System.out.println("Default Index Node Visit");
		return null;
	}

	default Object writeIndexExprNode(GoIndexExprNode node){
		System.out.println("Default Write Index Visit");
		return null;
	}

	default GoIntNode visitIRIntNode(GoIRIntNode goIRIntNode){
		System.out.println("Default Basic Int Visit");
		return null;
	}

	default GoStringNode visitIRStringNode(GoIRStringNode goIRStringNode){
		System.out.println("Default Basic String Visit");
		return null;
	}

	default Object visitAssignment(GoIRAssignmentStmtNode goIRAssignmentStmtNode){
		System.out.println("Default Assignment Statement Visit");
		return null;
	}

	default Object visitWriteIndex(GoIRWriteIndexNode goIRWriteIndexNode){
		System.out.println("Default Write Index Visit");
		return null;
	}
}
