package com.oracle.app.parser.ir;

import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
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
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.source.Source;

public class GoTruffle implements GoIRVisitor {

	GoLanguage language;
	
    private final Source source;
    private final Map<String, GoRootNode> allFunctions;
    private FrameDescriptor frameDescriptor;
	
	public GoTruffle(GoLanguage language, Source source) {
		this.language = language;
		this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, GoRootNode> getAllFunctions() {
        return allFunctions;
    }

	@Override
	public void visitObject(GoBaseIRNode node) {
		System.out.println(node.toString());
	}

	@Override
	public void visitIdent(GoIRIdentNode node) {
		//GoIdentNode n = new GoIdentNode(language, node.getIdent(), node.getChild());
	}

	@Override
	public void visitBinaryExpr(GoIRBinaryExprNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBasicLit(GoIRBasicLitNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInvoke(GoIRInvokeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitGenericDispatch(GoIRGenericDispatchNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitFuncDecl(GoIRFuncDeclNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDecl(GoIRDeclNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitArrayListExpr(GoIRArrayListExprNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitBlockStmt(GoIRBlockStmtNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitExprStmt(GoIRExprStmtNode node) {
		// TODO Auto-generated method stub
		
	}

}
