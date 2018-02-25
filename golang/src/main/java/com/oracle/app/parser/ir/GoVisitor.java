package com.oracle.app.parser.ir;

import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;

public class GoVisitor implements GoIRVisitor {

	public GoVisitor() {
		
	}

	@Override
	public void visitObject(GoBaseIRNode node) {
		System.out.println("Temp node: " + node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
	}

	@Override
	public void visitIdent(GoIRIdentNode node) {
		System.out.println(node.toString());
		if(node.getChild() != null)
			node.getChild().accept(this);
	}

	@Override
	public void visitBinaryExpr(GoIRBinaryExprNode node) {
		System.out.println(node.toString());
		node.getLeft().accept(this);
		node.getRight().accept(this);
	}

	@Override
	public void visitBasicLit(GoIRBasicLitNode node) {
		System.out.println(node.toString());
	}

	@Override
	public void visitInvoke(GoIRInvokeNode node) {
		System.out.println(node.toString());
		if(node.getFunctionNode() != null)
			node.getFunctionNode().accept(this);
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
	}

	@Override
	public void visitGenericDispatch(GoIRGenericDispatchNode node) {
		System.out.println(node.toString());
	}

	@Override
	public void visitFuncDecl(GoIRFuncDeclNode node) {
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
	}

	@Override
	public void visitDecl(GoIRDeclNode node) {
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
	}

	@Override
	public void visitArrayListExpr(GoIRArrayListExprNode node) {
		System.out.println(node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
	}

	@Override
	public void visitBlockStmt(GoIRBlockStmtNode node) {
		System.out.println(node.toString());
		node.getChild().accept(this);
	}

	@Override
	public void visitExprStmt(GoIRExprStmtNode node) {
		System.out.println(node.toString());
		node.getChild().accept(this);
	}

}
