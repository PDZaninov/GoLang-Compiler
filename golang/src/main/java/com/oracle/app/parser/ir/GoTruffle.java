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
import com.oracle.app.nodes.expression.GoSubNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode.GoReadArrayNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen.GoReadArrayNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteArrayNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteSliceNodeGen;
import com.oracle.app.nodes.types.GoIntArray;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringArray;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
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
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIfStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIncDecStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRStringNode;
import com.oracle.app.parser.ir.nodes.GoIRSwitchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoIRWriteIndexNode;
import com.oracle.app.parser.ir.nodes.GoTempIRNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
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
    
    /*TODO Remove this after removing GoIRDeclNode
    public GoStatementNode[] arrayListtoArray(GoBaseIRNode node) {
    	ArrayList<GoBaseIRNode> children = node.getChildren();
    	int argumentsize = children.size();
		GoStatementNode[] arguments = new GoStatementNode[argumentsize];
		for(int i = 0; i < argumentsize; i++){
			arguments[i] = (GoStatementNode) children.get(i).accept(this);
		}
		return arguments;
    }
	*/
	@Override
	public Object visitObject(GoTempIRNode node) {
		//System.out.println("Visited Truffle temp: " + node.toString());
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		String name = node.getIdent();
		//System.out.println("name of ident: " + name);
		GoExpressionNode result = null;
		//Cannot check for if writing value yet
		System.out.println(name+" "+lexicalscope.locals);
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
			/*
		case"IndexExpr":
			System.out.println(leftNode);
			result = GoIndexExprNodeGen.create(leftNode, rightNode);
			break;
			*/
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

		//System.out.println(frameDescriptor);
		finishBlock();
		
		frameDescriptor = null;
		return null;
	}

	@Override
	public Object visitDecl(GoIRDeclNode node) {
		//TODO check if this can be removed, replaced with GoIRArraylistExprNode
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
		return new GoArrayExprNode(result.getArguments());
	}

	public Object visitAssignment(GoIRAssignmentStmtNode node) {
		//GoExpressionNode name = (GoExpressionNode) node.getLHS().accept(this);
		String name = node.getIdentifier();
		
		GoBaseIRNode child = node.getLHS();
		GoExpressionNode value = (GoExpressionNode) node.getRHS().accept(this);
		FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
		if(child instanceof GoIRWriteIndexNode) {
			GoIndexExprNode index = (GoIndexExprNode) node.getLHS().accept(this);
			String type = lexicalscope.types.get(name);
			if(type.equals("Slice")) {
				return GoWriteSliceNodeGen.create(value, index.getIndex(), frameSlot);
			}
			return GoWriteArrayNodeGen.create(value, index.getIndex(), frameSlot);
		}
		else if(child instanceof GoIRIdentNode){
			lexicalscope.locals.put(name, frameSlot);
			
			GoIRBasicLitNode type = null;
			if(node.getRHS() instanceof GoIRBasicLitNode) {
				type = (GoIRBasicLitNode) node.getRHS();
				lexicalscope.types.put(name, type.getType());
			}
			
			//Need to put array as type into types hashmap
			/*			
					GoIRArrayTypeNode arrType = null;
			else if(node.getRHS() instanceof GoIRArrayTypeNode) {
				arrType = (GoIRArrayTypeNode) node.getRHS();
				arrType.getType();
			}
			*/
		}
		
		//Will cause error for Map, need to figure out better way.
		
		if(node.getRHS() instanceof GoIRInvokeNode) {
			String methodCall = ((GoIRInvokeNode) node.getRHS()).getFunctionNode().getIdentifier();
			if(methodCall.equals("make")) {
				lexicalscope.types.put(name, "Slice");
			}
		}
		return GoWriteLocalVariableNodeGen.create(value, frameSlot);
	}
	
	/**
	 * Only used for writing in assignments, but the value to write is not known so it needs to return
	 * some extra information. 
	 * Can probably be changed, was copy pasted from earlier stuff
	 */
	public Object visitWriteIndex(GoIRWriteIndexNode node){
		GoReadLocalVariableNode name = (GoReadLocalVariableNode) node.getName().accept(this);
		GoIndexExprNode array = new GoIndexExprNode(name,(GoExpressionNode) node.getIndex().accept(this));
		return array;
	}
	
	/**
	 * Only called when needing to read from an array so return a read.
	 */
	@Override
	public GoReadArrayNode visitIndexNode(GoIRIndexNode node){
		FrameSlot slot = frameDescriptor.findFrameSlot(node.getIdentifier());
		GoExpressionNode index = (GoExpressionNode) node.getIndex().accept(this);
		return GoReadArrayNodeGen.create(index, slot);
	}
	
	/**
	 * Known Code Smell:
	 * Type currently returns a BasicLit expression node, might be able to
	 * switch around to use a visitor pattern or some fancy enum pattern,but not
	 * known if anything else would use it. Might not even be that many cases to run through.
	 * Could also possibly go for a hashmap
	 */
	@Override
	public Object visitArrayType(GoIRArrayTypeNode node){

		GoExpressionNode length;
		if(node.getLength() == null) {
			length= new GoIntNode(0);
		}
		else {
			length = (GoExpressionNode) node.getLength().accept(this);
		}
		GoExpressionNode type = (GoExpressionNode) node.getType().accept(this);
		//Length can be assumed BasicLit. Only other value it can be is a const variable
		try{
			int size = length.executeInteger(null);
			if(type instanceof GoIntNode){
				return new GoIntArray(size);
			}
			else if(type instanceof GoStringNode){
				return new GoStringArray(size);
			}
			else{
				System.out.println("Array Type "+ type +" not implemented");
			}
		}
		catch(UnexpectedResultException e){
			//Throws error when the size value isn't an int or const
			System.out.println(e);
		}
		return type;
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
		GoIRIntNode one = new GoIRIntNode(1, null);
		
		String op = node.getOp();
		final GoIRBinaryExprNode binary_expr = new GoIRBinaryExprNode(op.substring(0,1), ident, one, null);

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

}
