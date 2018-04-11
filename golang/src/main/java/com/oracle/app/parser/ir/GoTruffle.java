package com.oracle.app.parser.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.SpecDecl.GoImportSpec;
import com.oracle.app.nodes.SpecDecl.GoSelectorExprNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.app.nodes.controlflow.GoBreakNode;
import com.oracle.app.nodes.controlflow.GoCaseClauseNode;
import com.oracle.app.nodes.controlflow.GoContinueNode;
import com.oracle.app.nodes.controlflow.GoForNode;
import com.oracle.app.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.controlflow.GoIfStmtNode;
import com.oracle.app.nodes.controlflow.GoReturnNode;
import com.oracle.app.nodes.controlflow.GoSwitchNode;
import com.oracle.app.nodes.expression.GoAddNodeGen;
import com.oracle.app.nodes.expression.GoBinaryLeftShiftNodeGen;
import com.oracle.app.nodes.expression.GoBinaryRightShiftNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseAndNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseComplementNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseOrNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseXORNodeGen;
import com.oracle.app.nodes.expression.GoCompositeLitNode;
import com.oracle.app.nodes.expression.GoDivNodeGen;
import com.oracle.app.nodes.expression.GoEqualNodeGen;
import com.oracle.app.nodes.expression.GoGreaterOrEqualNodeGen;
import com.oracle.app.nodes.expression.GoGreaterThanNodeGen;
import com.oracle.app.nodes.expression.GoIndexExprNode;
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
import com.oracle.app.nodes.expression.GoStarExpressionNode;
import com.oracle.app.nodes.expression.GoSubNodeGen;
import com.oracle.app.nodes.expression.GoUnaryAddressNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode.GoReadArrayNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen.GoReadArrayNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteArrayNodeGen;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoNonPrimitiveType;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.nodes.*;
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
        protected final Map<String, String> types;

        LexicalScope(LexicalScope outer) {
        	//Sets the outerscope to be the calling scope
            this.outer = outer;
            //Creates the local scope
            this.locals = new HashMap<>();
            
            this.types = new HashMap<>();
            //If there is an outerscope then put all the variables in there
            //into this scope
            if (outer != null) {
                locals.putAll(outer.locals);
                types.putAll(outer.types);
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
        frameDescriptor = new FrameDescriptor();
    }
	
	public GoTruffle initialize(){
        startFunction();
        FrameSlot frameSlot;
        frameSlot = frameDescriptor.addFrameSlot("int",FrameSlotKind.Int);
		lexicalscope.locals.put("int", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("float64", FrameSlotKind.Float);
		lexicalscope.locals.put("float64", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("bool", FrameSlotKind.Boolean);
		lexicalscope.locals.put("bool", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("true", FrameSlotKind.Boolean);
		lexicalscope.locals.put("true", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("false", FrameSlotKind.Boolean);
		lexicalscope.locals.put("false", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("string", FrameSlotKind.Object);
		lexicalscope.locals.put("string", frameSlot);
		return this;
	}

    public Map<String, GoRootNode> getAllFunctions() {
        return allFunctions;
    }
    
    public void startBlock(){
    	lexicalscope = new LexicalScope(lexicalscope);
    }
    
    public void startFunction(){
    	startBlock();
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

	@Override
	public Object visitObject(GoBaseIRNode node) {
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		String name = node.getIdent();
		GoExpressionNode result = null;
		//Cannot check for if writing value yet
	    final FrameSlot frameSlot = lexicalscope.locals.get(name);
	    if (frameSlot != null) {
	            /* Read of a local variable. */
	    	return (GoExpressionNode)GoReadLocalVariableNodeGen.create(frameSlot);
	    } 
		
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
		default:
			throw new RuntimeException("Unexpected Operation: "+op);
	}
		return result;
	}

	public GoIntNode visitIRIntNode(GoIRIntNode node){
		return new GoIntNode(node.getValue());
	}
	
	public GoStringNode visitIRStringNode(GoIRStringNode node){
		return new GoStringNode(node.getValue());
	}
	
	@Override
	public Object visitInvoke(GoIRInvokeNode node) {
		GoExpressionNode functionNode = (GoExpressionNode) node.getFunctionNode().accept(this);
		GoArrayExprNode arguments = null;
		if(node.getArgumentNode() != null){
			arguments = (GoArrayExprNode) node.getArgumentNode().accept(this);
		}
		else{
			arguments = new GoArrayExprNode(new GoExpressionNode[0]);
		}
		return new GoInvokeNode(functionNode, arguments.getArguments());
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
		
		//frameDescriptor = null;
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
		return new GoArrayExprNode(arguments);
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
			case "&":
				if(child instanceof GoReadArrayNode){
					FrameSlot array = ((GoReadArrayNode)child).getSlot();
					GoIntNode index = (GoIntNode) ((GoReadArrayNode)child).getIndex();
					result = new GoUnaryAddressNode(array,true,index);
				}else{
					result = new GoUnaryAddressNode(((GoReadLocalVariableNode) child).getSlot());
				}
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
		GoArrayExprNode result;
		switch(type){
		case "var":
			result = (GoArrayExprNode) node.getChild().accept(this);
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
			result = (GoArrayExprNode) node.getChild().accept(this);
			break;
		default:
			System.out.println("GenDecl Error Checking Implementation");
			result = null;
		}
		//Placeholder node. There should be a better way of doing this.
		//Issue: Parent node is a GoNodeExpresion[] filling its array,but
		//we return another array into the parent array.
		return new GoArrayExprNode(result.getArguments());
	}

	public Object visitAssignment(GoIRAssignmentStmtNode node) {	
		GoBaseIRNode child = node.getLHS();
		
		GoWriteVisitor miniVisitor = new GoWriteVisitor(lexicalscope,this,frameDescriptor,node);
		GoExpressionNode result = (GoExpressionNode) miniVisitor.visit(child);
		if(child instanceof GoIRIndexNode){
			FrameSlot frameSlot = lexicalscope.locals.get(node.getIdentifier());
			GoExpressionNode value = (GoExpressionNode) node.getRHS().accept(this);
			return GoWriteArrayNodeGen.create(value, ((GoIndexExprNode) result).getIndex(), frameSlot);
		}
		return result;
	}
	
	public Object visitStarNode(GoIRStarNode node){
		GoStarExpressionNode result = new GoStarExpressionNode((GoReadLocalVariableNode) node.getChild().accept(this));
		return result;
	}
	
	/**
	 * Only called when needing to read from an array so return a read.
	 */
	@Override
	public Object visitIndexNode(GoIRIndexNode node){
		FrameSlot slot = frameDescriptor.findFrameSlot(node.getIdentifier());
		GoExpressionNode index = (GoExpressionNode) node.getIndex().accept(this);
		return GoReadArrayNodeGen.create(index, slot);
	}
	
	/**
	 * 
	 * return - A GoArray with filled frameSlots, but values are not written in yet
	 */
	@Override
	public Object visitArrayType(GoIRArrayTypeNode node){

		GoExpressionNode length;
		if(node.getLength() == null) {
			length = new GoIntNode(0);
		}
		else {
			length = (GoExpressionNode) node.getLength().accept(this);
		}
		//GoExpressionNode type = (GoExpressionNode) node.getType().accept(this);
		String type = node.getType().getIdentifier();
		//Catch error where length is not an int node or possibly an int const
		GoArray result = new GoArray((GoIntNode) length);
		result.setType(type);
		//Fill the array with frameslots that are reachable in the framedescriptor, frameslots will have values when executed
		int hash = result.hashCode();
		FrameSlot indexSlot;
		String temporaryIdentifier;
		for(int i = 0; i < result.len(); i++){
			temporaryIdentifier = String.format("_0x%x_%d", hash,i);
			indexSlot = frameDescriptor.addFrameSlot(temporaryIdentifier);
			result.insert(indexSlot, i);
		}
		return result;
	}
	
	@Override
	public Object visit(GoIRCompositeLitNode node){
		GoExpressionNode type = (GoExpressionNode) node.getExpr().accept(this);
		GoArrayExprNode elts = (GoArrayExprNode) node.getElts().accept(this);
		GoCompositeLitNode result = new GoCompositeLitNode((GoNonPrimitiveType) type, elts);
		return result;
	}
	
	@Override
	public Object visitCaseClause(GoIRCaseClauseNode node) {
		GoArrayExprNode list = null;
		GoStatementNode[]  body = null;

		if (node.getList() != null){
			list = (GoArrayExprNode) node.getList().accept(this);
		}
		if (node.getBody() != null) {
			body = (GoStatementNode[]) node.getBody().accept(this);
		}

		return new GoCaseClauseNode(list.getArguments(), body);
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
		startBlock();
		
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
		
		finishBlock();
		
		return new GoForNode(init, cond, post, body);
		
	}

	@Override
	public Object visitIncDecStmt(GoIRIncDecStmtNode node) {
		
		final GoExpressionNode result;
		GoIRIdentNode ident = (GoIRIdentNode) node.getChild();
		GoIRIntNode one = new GoIRIntNode(1);
		
		String op = node.getOp();
		final GoIRBinaryExprNode binary_expr = new GoIRBinaryExprNode(op.substring(0,1), ident, one);

		GoIRAssignmentStmtNode res = new GoIRAssignmentStmtNode(ident,binary_expr);
		result = (GoExpressionNode) res.accept(this);
		return result;
	}
	
	@Override
	public Object visitIfStmt(GoIRIfStmtNode node){
		startBlock();
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
		finishBlock();
		return new GoIfStmtNode(Init,CondNode,Body,Else);
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

	@Override
	public Object visitImportSpec(GoIRImportSpecNode goIRImportSpecNode){
		String name = goIRImportSpecNode.getIdentifier();
		FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
		lexicalscope.locals.put(name, frameSlot);

		GoStringNode ident = (GoStringNode) goIRImportSpecNode.getChild().accept(this);
		return new GoImportSpec(ident);
	}

	@Override
	public Object visitSelectorExpr(GoIRSelectorExprNode goIRSelectorExprNode){
		GoIdentNode importPackage = (GoIdentNode) goIRSelectorExprNode.getImportName().accept(this);
		GoIdentNode importMethod = (GoIdentNode) goIRSelectorExprNode.getImportMethod().accept(this);
		return new GoSelectorExprNode(language, importPackage, importMethod);
	}
	
	@Override
	public Object visitReturnStmt(GoIRReturnStmtNode node){
		
		return new GoReturnNode((GoExpressionNode)node.getChild().accept(this));
		
	}
	
	@Override
	public Object visitField(GoIRFieldNode node){
		
		return node.accept(this);
		
	}

}
