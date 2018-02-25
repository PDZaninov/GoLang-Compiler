package com.oracle.app.parser.ir;

import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
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
		GoExpressionNode[] result = new GoExpressionNode[1];
		result[0] = (GoExpressionNode) node.getChild().accept(this);
		return new GoIdentNode(language, node.getIdent(), result);
	}

	@Override
	public Object visitBinaryExpr(GoIRBinaryExprNode node) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitGenericDispatch(GoIRGenericDispatchNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFuncDecl(GoIRFuncDeclNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitDecl(GoIRDeclNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitArrayListExpr(GoIRArrayListExprNode node) {
		// TODO Auto-generated method stub
		return null;
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
