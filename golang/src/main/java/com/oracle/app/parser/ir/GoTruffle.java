package com.oracle.app.parser.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.expression.GoAddNodeGen;
import com.oracle.app.nodes.expression.GoDivNodeGen;
import com.oracle.app.nodes.expression.GoMulNodeGen;
import com.oracle.app.nodes.expression.GoSubNodeGen;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
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
	public Object visitObject(GoBaseIRNode node) {
		System.out.println("Visited Truffle temp: " + node.toString());
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		GoExpressionNode result = (GoExpressionNode) node.getChild().accept(this);
		String name = node.getIdent();
		return new GoIdentNode(language, name, result);
	}

	@Override
	public Object visitBinaryExpr(GoIRBinaryExprNode node) {
		GoExpressionNode rightNode = (GoExpressionNode) node.getRight().accept(this);
		GoExpressionNode leftNode = (GoExpressionNode) node.getLeft().accept(this);
		String op = node.getOp();
		final GoExpressionNode result;
		switch(op){
		case "+":
			result = GoAddNodeGen.create(leftNode, rightNode);
			break;
		case "-":
			result = GoSubNodeGen.create(leftNode, rightNode);
			break;
		case "*":
			result = GoMulNodeGen.create(leftNode, rightNode);
			break;
		case "/":
			result = GoDivNodeGen.create(leftNode, rightNode);
			break;
		default:
			throw new RuntimeException("Unimplemented op "+ op);
		}
		return result;
	}

	@Override
	public Object visitBasicLit(GoIRBasicLitNode node) {
		String type = node.getType();
		String value = node.getValue();
		final GoExpressionNode result;
		switch(type) {
		case "INT":
			result = new GoIntNode(Integer.parseInt(value));
			break;
		case "FLOAT":
			result = new GoIntNode(Integer.parseInt(value));
			break;
		case "IMAG":
			result = new GoIntNode(Integer.parseInt(value));
			break;
		case "CHAR":
			result = new GoIntNode(Integer.parseInt(value));
			break;
		case "STRING":
			result = new GoStringNode(value);
			break;
		default:
			throw new RuntimeException("Undefined type: " + type);
		}
		return result;
	}

	@Override
	public Object visitInvoke(GoIRInvokeNode node) {
		GoExpressionNode functionNode = (GoExpressionNode) node.getFunctionNode().accept(this);
		GoExpressionNode[] arguments = (GoExpressionNode[]) node.getArgumentNode().accept(this);
		
		return new GoInvokeNode(functionNode, arguments);
	}

	@Override
	public Object visitGenericDispatch(GoIRGenericDispatchNode node) {
		// Probably not necessary or something we don't need
		return null;
	}

	@Override
	public Object visitFuncDecl(GoIRFuncDeclNode node) {
		// Probably need this node created to the function hashmap
		return null;
	}

	@Override
	public Object visitDecl(GoIRDeclNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitArrayListExpr(GoIRArrayListExprNode node) {
		int argumentsize = node.getSize();
		GoExpressionNode[] arguments = new GoExpressionNode[argumentsize];
		ArrayList<GoBaseIRNode> children = node.getChildren();
		for(int i = 0; i < argumentsize; i++){
			arguments[i] = (GoExpressionNode) children.get(i).accept(this);
		}
		return arguments;
	}

	@Override
	public Object visitBlockStmt(GoIRBlockStmtNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExprStmt(GoIRExprStmtNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
