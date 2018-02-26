package com.oracle.app.parser.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.app.nodes.controlflow.GoFunctionBodyNode;
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
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenericDispatchNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
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
    
    public GoStatementNode[] arrayListtoArray(GoBaseIRNode node) {
    	ArrayList<GoBaseIRNode> children = node.getChildren();
    	int argumentsize = children.size();
		GoStatementNode[] arguments = new GoStatementNode[argumentsize];
		for(int i = 0; i < argumentsize; i++){
			arguments[i] = (GoStatementNode) children.get(i).accept(this);
		}
		return arguments;
    }

	@Override
	public Object visitObject(GoBaseIRNode node) {
		System.out.println("Visited Truffle temp: " + node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		GoExpressionNode result = null;
		if(node.getChild() != null)
			result = (GoExpressionNode) node.getChild().accept(this);
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
		GoExpressionNode result = null;
		if(node.getType() != null) {
			String type = node.getType();
			String value = node.getValue();
			
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
		
		
		GoBlockNode blockNode = (GoBlockNode) node.getBody().accept(this);
		GoFunctionBodyNode bodyNode = new GoFunctionBodyNode(blockNode);
		
		String name = node.getIdent();
		
		GoRootNode root = new GoRootNode(language,frameDescriptor,bodyNode,null,name);
		allFunctions.put(name,root);
		
		
		return null;
	}

	@Override
	public Object visitDecl(GoIRDeclNode node) {
		return new GoDeclNode(arrayListtoArray(node));
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
		GoStatementNode[] body = (GoStatementNode[]) node.getChild().accept(this);
		return new GoBlockNode(body);
	}

	@Override
	public Object visitExprStmt(GoIRExprStmtNode node) {
		return new GoExprNode( (GoExpressionNode) node.getChild().accept(this));
	}

	@Override
	public Object visitExpr(GoIRExprNode node) {
		return new GoExprNode( (GoExpressionNode) node.getChild().accept(this));
	}

	@Override
	public Object visitStmt(GoIRStmtNode node) {
		int argumentsize = node.getSize();
		GoStatementNode[] arguments = new GoStatementNode[argumentsize];
		ArrayList<GoBaseIRNode> children = node.getChildren();
		for(int i = 0; i < argumentsize; i++){
			arguments[i] = (GoExpressionNode) children.get(i).accept(this);
		}
		return arguments;
	}

}
