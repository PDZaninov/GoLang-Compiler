package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;

public class GoVisitor implements GoIRVisitor {

	public GoVisitor() {
		
	}

	@Override
	public Object visitObject(GoBaseIRNode node) {
		System.out.println("Temp node: " + node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		if(node.getChild() != null)
			node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitBinaryExpr(GoIRBinaryExprNode node) {
		node.getLeft().accept(this);
		node.getRight().accept(this);
		return null;
	}

	@Override
	public Object visitBasicLit(GoIRBasicLitNode node) {
		return null;
	}

	@Override
	public Object visitInvoke(GoIRInvokeNode node) {
		node.getFunctionNode().accept(this);
		if(node.getArgumentNode() != null){
			node.getArgumentNode().accept(this);
		}
		node.getDispatchNode().accept(this);
		return null;
	}

	@Override
	public Object visitGenericDispatch(GoIRGenericDispatchNode node) {
		return null;
	}

	@Override
	public Object visitFuncDecl(GoIRFuncDeclNode node) {
		node.getName().accept(this);
		if(node.getReceiver() != null){
			node.getReceiver().accept(this);
		}
		node.getType().accept(this);
		if(node.getBody() != null){
			node.getBody().accept(this);
		}
		return null;
	}

	@Override
	public Object visitDecl(GoIRDeclNode node) {
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitArrayListExpr(GoIRArrayListExprNode node) {
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitBlockStmt(GoIRBlockStmtNode node) {
		if(node.getChild() != null){
				node.getChild().accept(this);
		}
		return null;
	}

	@Override
	public Object visitExprStmt(GoIRExprStmtNode node) {
		node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitExpr(GoIRExprNode node) {
		node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitStmt(GoIRStmtNode node) {
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitUnary(GoIRUnaryNode node) {
		node.getChild().accept(this);
		return null;
	}

}
