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
		System.out.println(node.toString());
		if(node.getChild() != null)
			node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitBinaryExpr(GoIRBinaryExprNode node) {
		System.out.println(node.toString());
		node.getLeft().accept(this);
		node.getRight().accept(this);
		return null;
	}

	@Override
	public Object visitBasicLit(GoIRBasicLitNode node) {
		System.out.println(node.toString());
		return null;
	}

	@Override
	public Object visitInvoke(GoIRInvokeNode node) {
		System.out.println(node.toString());
		node.getFunctionNode().accept(this);
		if(node.getArgumentNode() != null){
			node.getArgumentNode().accept(this);
		}
		node.getDispatchNode().accept(this);
		return null;
	}

	@Override
	public Object visitGenericDispatch(GoIRGenericDispatchNode node) {
		System.out.println(node.toString());
		return null;
	}

	@Override
	public Object visitFuncDecl(GoIRFuncDeclNode node) {
		System.out.println(node.toString());
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
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitArrayListExpr(GoIRArrayListExprNode node) {
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitBlockStmt(GoIRBlockStmtNode node) {
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChild())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitExprStmt(GoIRExprStmtNode node) {
		System.out.println(node.toString());
		node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitExpr(GoIRExprNode node) {
		System.out.println(node.toString());
		node.getChild().accept(this);
		return null;
	}

	@Override
	public Object visitStmt(GoIRStmtNode node) {
		System.out.println(node.toString());
		node.getChild().accept(this);
		return null;
	}

}
