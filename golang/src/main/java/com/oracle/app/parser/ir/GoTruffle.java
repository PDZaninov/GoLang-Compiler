package com.oracle.app.parser.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.*;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.app.nodes.controlflow.GoBreakNode;
import com.oracle.app.nodes.controlflow.GoCaseClauseNode;
import com.oracle.app.nodes.controlflow.GoContinueNode;
import com.oracle.app.nodes.controlflow.GoForNode;
import com.oracle.app.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.controlflow.GoIfStmtNode;
import com.oracle.app.nodes.controlflow.GoSwitchNode;

import com.oracle.app.nodes.expression.GoAddNodeGen;
import com.oracle.app.nodes.expression.GoBinaryLeftShiftNodeGen;
import com.oracle.app.nodes.expression.GoBinaryRightShiftNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseAndNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseComplementNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseOrNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseXORNodeGen;
import com.oracle.app.nodes.expression.GoDecNodeGen;
import com.oracle.app.nodes.expression.GoDivNodeGen;
import com.oracle.app.nodes.expression.GoEqualNodeGen;
import com.oracle.app.nodes.expression.GoGreaterOrEqualNodeGen;
import com.oracle.app.nodes.expression.GoGreaterThanNodeGen;
import com.oracle.app.nodes.expression.GoIncNodeGen;
import com.oracle.app.nodes.expression.GoIndexExprNodeGen;
import com.oracle.app.nodes.expression.GoLessOrEqualNodeGen;
import com.oracle.app.nodes.expression.GoLessThanNodeGen;
import com.oracle.app.nodes.expression.GoLogicalAndNode;
import com.oracle.app.nodes.expression.GoLogicalNotNodeGen;
import com.oracle.app.nodes.expression.GoLogicalOrNode;
import com.oracle.app.nodes.expression.GoModNodeGen;
import com.oracle.app.nodes.expression.GoMulNodeGen;
import com.oracle.app.nodes.expression.GoNegativeSignNodeGen;
import com.oracle.app.nodes.expression.GoNotEqualNodeGen;
import com.oracle.app.nodes.expression.GoPositiveSignNodeGen;
import com.oracle.app.nodes.expression.GoSubNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoFloatNode;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;

import com.oracle.app.parser.ir.nodes.*;

import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
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
import com.oracle.app.parser.ir.nodes.GoIRIncDecStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRSwitchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoIRValueSpecNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;

/**
 * Constructs the Truffle tree using a visitor pattern to visit
 * every node in the IRTree and translate the information into Truffle
 *
 */
public class GoTruffle implements GoIRVisitor {
	/**
	 * LexicalScope holds the symbol table information when creating the Truffle tree
	 *
	 */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
        	//Sets the outerscope to be the calling scope
            this.outer = outer;
            //Creates the local scope
            this.locals = new HashMap<>();
            //If there is an outerscope then put all the variables in there
            //into this scope
            if (outer != null) {
                locals.putAll(outer.locals);
            }
            }
        }
    
	GoLanguage language;
	
    private final Source source;
    private final Map<String, GoRootNode> allFunctions;
    private FrameDescriptor frameDescriptor;
    
    private LexicalScope lexicalscope;
	
	public GoTruffle(GoLanguage language, Source source) {
		this.language = language;
		this.source = source;
        this.allFunctions = new HashMap<>();
        //Creates a block to cover for idents located outside of a function body
        startFunction();
        //FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot("int",FrameSlotKind.Int);
		//lexicalscope.locals.put("int", frameSlot);
        
    }

    public Map<String, GoRootNode> getAllFunctions() {
        return allFunctions;
    }
    
    public void startBlock(){
    	lexicalscope = new LexicalScope(lexicalscope);
    }
    
    public void startFunction(){
    	startBlock();
    	frameDescriptor = new FrameDescriptor();
    	
    }
    
    public void finishBlock(){
    	lexicalscope = lexicalscope.outer;
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

	public Object visitIfStmt(GoIRIfStmtNode node){
		GoStatementNode Init = null;
		GoExpressionNode CondNode = null;
		GoStatementNode Body = null;
		GoStatementNode Else = null;
		
		if(node.getInit() != null)
			Init = (GoStatementNode) node.getInit().accept(this);
		
		CondNode = (GoExpressionNode) node.getCond().accept(this);
		Body = (GoStatementNode)node.getBody().accept(this);
		
		if(node.getElse() != null)
			Else = (GoStatementNode)node.getElse().accept(this);

		return new GoIfStmtNode(Init,CondNode,Body,Else);
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
		String name = node.getIdent();
		System.out.println("name of ident: " + name);
		GoExpressionNode result = null;
		//Cannot check for if writing value yet
	    final FrameSlot frameSlot = lexicalscope.locals.get(name);
	    if (frameSlot != null) {
	            /* Read of a local variable. */
	    	System.out.println(name + " is a read node");
	    	return (GoExpressionNode)GoReadLocalVariableNodeGen.create(frameSlot);
	    } 
	    	/*else {
	             Read of a global name. In our language, the only global names are functions. 
	        	return (GoExpressionNode)new GoFunctionLiteralNode(language, name);
	        }*/
		else {
			result = null;
			if(node.getChild() != null)
				result = (GoExpressionNode) node.getChild().accept(this);

			
		}
		return new GoIdentNode(language, name, result);
	}

	@Override
	public Object visitBinaryExpr(GoIRBinaryExprNode node) {
		GoExpressionNode rightNode = (GoExpressionNode) node.getRight().accept(this);
		GoExpressionNode leftNode = (GoExpressionNode) node.getLeft().accept(this);
		String op = node.getOp();
		final GoExpressionNode result;
		System.out.println("In case:");
		System.out.println(leftNode.toString());
		System.out.println(rightNode.toString());
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
		case"%":
			result = GoModNodeGen.create(leftNode, rightNode);
			break;
		case"<":
			result = GoLessThanNodeGen.create(leftNode, rightNode);
			break;
		case"<=":
			result = GoLessOrEqualNodeGen.create(leftNode, rightNode);
			break;
		case"==":
			result = GoEqualNodeGen.create(leftNode, rightNode);
			break;
		case">":
			result = GoGreaterThanNodeGen.create(leftNode, rightNode);
			break;
		case">=":
			result = GoGreaterOrEqualNodeGen.create(leftNode, rightNode);
			break;
		case"!=":
			result = GoNotEqualNodeGen.create(leftNode, rightNode);
			break;
		case"&&":
			result = new GoLogicalAndNode(leftNode, rightNode);
			break;
		case"||":
			result = new GoLogicalOrNode(leftNode, rightNode);
			break;
		case"<<":
			result = GoBinaryLeftShiftNodeGen.create(leftNode, rightNode);
			break;
		case">>":
			result = GoBinaryRightShiftNodeGen.create(leftNode, rightNode);
			break;
		case"&":
			result = GoBitwiseAndNodeGen.create(leftNode, rightNode);
			break;
		case"|":
			result = GoBitwiseOrNodeGen.create(leftNode, rightNode);
			break;
		case"^":
			result = GoBitwiseXORNodeGen.create(leftNode, rightNode);
			break;
		case"IndexExpr":
			result = GoIndexExprNodeGen.create(leftNode, rightNode);
			break;
			
		default:
			throw new RuntimeException("Unexpected Operation: "+op);
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
				result = new GoFloatNode(Float.parseFloat(value));
				break;
			case "IMAG":
				result = new GoIntNode(Integer.parseInt(value));
				break;
			case "CHAR":
				result = new GoIntNode(Integer.parseInt(value));
				break;
			case "STRING":
				value = value.substring(2, value.length()-2);
				value = value.replace("\\\\", "\\");
				
				value = StringEscape.unescape_perl_string(value);
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

		startFunction();

		GoBlockNode blockNode = (GoBlockNode) node.getBody().accept(this);
		GoFunctionBodyNode bodyNode = new GoFunctionBodyNode(blockNode);
		
		String name = node.getIdent();
		
		GoRootNode root = new GoRootNode(language,frameDescriptor,bodyNode,null,name);
		allFunctions.put(name,root);

		finishBlock();
		
		frameDescriptor = null;
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
		System.out.println(node.getChild());
		return new GoExprNode( (GoExpressionNode) node.getChild().accept(this));
	}

	@Override
	public Object visitStmt(GoIRStmtNode node) {
		int argumentsize = node.getSize();
		GoStatementNode[] arguments = new GoStatementNode[argumentsize];
		ArrayList<GoBaseIRNode> children = node.getChildren();
		for(int i = 0; i < argumentsize; i++){
			arguments[i] = (GoStatementNode) children.get(i).accept(this);
		}
		return arguments;
	}

	@Override
	public Object visitUnary(GoIRUnaryNode node) {
		GoExpressionNode child = (GoExpressionNode) node.getChild().accept(this);
		String op = node.getOp();
		final GoExpressionNode result;
		switch(op) {
			case"!":
				result = GoLogicalNotNodeGen.create(child);
				break;
			case"^":
				result = GoBitwiseComplementNodeGen.create(child);
				break;
			case"+":
				result = GoPositiveSignNodeGen.create(child);
				break;
			case"-":
				result = GoNegativeSignNodeGen.create(child);
				break;
			default:
				throw new RuntimeException("Unexpected Operation: "+op);
		}
		return result;
	}
	
    
	//Skips over itself and returns the child node to the parent of this node
	@Override
	public Object visitDeclStmt(GoIRDeclStmtNode node) {
		
		return node.getChild().accept(this);
	}

	@Override
	public Object visitGenDecl(GoIRGenDeclNode node) {
		String type = node.getToken();
		GoExpressionNode[] result;
		switch(type){
		case "var":
			result = (GoExpressionNode[]) node.getChild().accept(this);
			break;
		case "type":
			System.out.println("GenDecl Token: TYPE needs implementation");
			result = null;
			break;
		case "const":
			System.out.println("GenDecl Token: CONST needs implementation");
			result = null;
			break;
		case "import":
			System.out.println("GenDecl Token: IMPORT needs implementation");
			result = null;
			break;
		default:
			System.out.println("GenDecl Error Checking Implementation");
			result = null;
		}
		//Placeholder node. There should be a better way of doing this.
		//Issue: Parent node is a GoNodeExpresion[] filling its array,but
		//we return another array into the parent array.
		return new GoArrayExprNode(result);
	}

	@Override
	public Object visitValueSpec(GoIRValueSpecNode node) {
		GoExpressionNode[] names = (GoExpressionNode[]) node.getNames().accept(this);
		GoExpressionNode defaultval = null;
		if(node.getType() != null){
			defaultval = (GoExpressionNode)node.getType().accept(this);
			System.out.println("type: ");
			System.out.println(node.getType().name);
		}
		GoExpressionNode[] values = null;
		if(node.getExpr() != null){
			values = (GoExpressionNode[])node.getExpr().accept(this);
			System.out.println("values: ");
			System.out.print(node.getExpr().name);
		}
		GoExpressionNode[] result = new GoExpressionNode[names.length];
		//Unbalanced arrays arent actually a thing. Thats a mismatch error
		//Throw an exception when values array has values but unbalanced
		if(values != null){
			for(int i = 0; i < names.length; i++){
				String name = "";
				FrameSlot frameSlot = null;
				if(names[i] instanceof GoIdentNode){
					name = ((GoIdentNode) names[i]).getName();
					frameSlot = frameDescriptor.findOrAddFrameSlot(name);
				}
				else if(names[i] instanceof GoReadLocalVariableNode){
					frameSlot = ((GoReadLocalVariableNode) names[i]).getSlot();
				}
			
				lexicalscope.locals.put(name, frameSlot);
				result[i] = GoWriteLocalVariableNodeGen.create(values[i], frameSlot);
			
			}
		}
		else{
			GoExpressionNode val = null;
			
			if(defaultval instanceof GoIdentNode) {
				String type = ((GoIdentNode)defaultval).getName();
				switch (type){
				case "int":
					val = new GoIntNode(0);
					break;
				case "string":
					val = new GoStringNode("");
					break;
				case "float":
					val = new GoFloatNode(0);
					break;
				default:
					System.out.println("Unimplemented ValueSpec default case "+type);
				}
			}else {
				val = new GoArray();
			}
			for(int i = 0; i < names.length; i++){
				String name = ((GoIdentNode) names[i]).getName();
				FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
				lexicalscope.locals.put(name, frameSlot);
				result[i] = GoWriteLocalVariableNodeGen.create(val, frameSlot);
				//System.out.println("name: " + name);
				//System.out.println("val: " + val.toString());
			}
			
		}
		//Placeholder node. There should be a better way of doing this.
		//Issue: Parent node is a GoNodeExpresion[] filling its array,but
		//we return another array into the parent array.
		return new GoArrayExprNode(result);
	}

	@Override

	public Object visitCaseClause(GoIRCaseClauseNode node) {
		GoExpressionNode[] list = null;
		GoStatementNode[]  body = null;

		if (node.getList() != null){
			list = (GoExpressionNode[]) node.getList().accept(this);
		}
		if (node.getBody() != null) {
			body = (GoStatementNode[]) node.getBody().accept(this);
		}

		return new GoCaseClauseNode(list, body);
	}

	@Override
	public Object visitSwitchStmt(GoIRSwitchStmtNode node) {
		GoStatementNode init = null;
		GoExpressionNode tag = null;
		GoBlockNode body = null;

		if (node.getInit() != null){
			init = (GoStatementNode) node.getInit().accept(this);
		}
		if (node.getTag() != null){
			tag = (GoExpressionNode) node.getTag().accept(this);
		}
		if (node.getBody() != null){
			body = (GoBlockNode) node.getBody().accept(this);
		}

		return new GoSwitchNode(init, tag, body);
	}

	public Object visitForLoop(GoIRForNode node) {
		GoExpressionNode init = null;
		GoExpressionNode cond = null;
		GoExpressionNode post = null;
		GoStatementNode body;
		if(node.getInit() != null)
			init = (GoExpressionNode) node.getInit().accept(this);
		if(node.getCond() != null)
			cond = (GoExpressionNode) node.getCond().accept(this);
		if(node.getPost() != null)
			post = (GoExpressionNode) node.getPost().accept(this);
		body = (GoStatementNode) node.getBody().accept(this);
		return new GoForNode(init, cond, post, body);
		
	}

	@Override
	public Object visitIncDecStmt(GoIRIncDecStmtNode node) {
		
		final GoExpressionNode result;
		GoIRIdentNode ident = (GoIRIdentNode) node.getChild();
		GoIRBasicLitNode one = new GoIRBasicLitNode("INT", "1");
		
		String op = node.getOp();
		final GoIRBinaryExprNode binary_expr;
		switch(op) {
			case "++":
				binary_expr = new GoIRBinaryExprNode("+", ident, one);
				break;
			case "--":
				binary_expr = new GoIRBinaryExprNode("-", ident, one);
				break;
			default:
				throw new RuntimeException("Unexpected Operation: " + op);
		}
		
		ArrayList<GoBaseIRNode> name_list = new ArrayList<>();
		name_list.add(ident);
		
		ArrayList<GoBaseIRNode> arg_list = new ArrayList<>();
		arg_list.add(binary_expr);
		
		GoIRArrayListExprNode names = new GoIRArrayListExprNode(name_list);
		GoIRArrayListExprNode values = new GoIRArrayListExprNode(arg_list);
		
		GoIRValueSpecNode res = new GoIRValueSpecNode(names, null, values);
		result = (GoExpressionNode) res.accept(this);
		
		return result;
	}

	@Override
	public Object visitBranchStmt(GoIRBranchStmtNode node) {
		
		//The child is for the goto implementation
		//TODO
		
		GoExpressionNode child;
		if(node.getChild() != null)
			child = (GoExpressionNode) node.getChild().accept(this);
		
		String type = node.getType();
		GoStatementNode result = null;
		switch(type) {
			case "break":
				result = new GoBreakNode();
				break;
			case "continue":
				result = new GoContinueNode();
				break;
			case "goto":
				System.out.println("BranchStmt Token: GOTO needs implementation");
				break;
			case "fallthrough":
				System.out.println("BranchStmt Token: FALLTHROUGH needs implementation");
				break;
			default:
				throw new RuntimeException("Unexpected BranchStmt: " + type);
		}
		return result;
	}

}
