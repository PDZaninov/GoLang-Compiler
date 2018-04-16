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
import com.oracle.app.nodes.SpecDecl.GoImportSpec;
import com.oracle.app.nodes.SpecDecl.GoSelectorExprNode;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.app.nodes.call.GoFuncTypeNode;
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
import com.oracle.app.nodes.expression.GoArrayTypeExprNode;
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
import com.oracle.app.nodes.expression.GoSliceExprNode;
import com.oracle.app.nodes.expression.GoStarExpressionNode;
import com.oracle.app.nodes.expression.GoSubNodeGen;
import com.oracle.app.nodes.expression.GoUnaryAddressNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode.GoReadArrayNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen.GoReadArrayNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen.GoWriteArrayNodeGen;
import com.oracle.app.nodes.types.GoFloat32Node;
import com.oracle.app.nodes.types.GoFloat64Node;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRArrayTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRAssignmentStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRBranchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRCaseClauseNode;
import com.oracle.app.parser.ir.nodes.GoIRCompositeLitNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFieldNode;
import com.oracle.app.parser.ir.nodes.GoIRFloat32Node;
import com.oracle.app.parser.ir.nodes.GoIRFloat64Node;
import com.oracle.app.parser.ir.nodes.GoIRForNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncTypeNode;
import com.oracle.app.parser.ir.nodes.GoIRGenDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRIfStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRImportSpecNode;
import com.oracle.app.parser.ir.nodes.GoIRIncDecStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRIndexNode;
import com.oracle.app.parser.ir.nodes.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRReturnStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRSelectorExprNode;
import com.oracle.app.parser.ir.nodes.GoIRSliceExprNode;
import com.oracle.app.parser.ir.nodes.GoIRStarNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRStringNode;
import com.oracle.app.parser.ir.nodes.GoIRSwitchStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoTempIRNode;
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
	
    //Can create a global function block and append writes to the top most node
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
		frameSlot = frameDescriptor.addFrameSlot("float64", FrameSlotKind.Double);
		lexicalscope.locals.put("float64", frameSlot);
		frameSlot = frameDescriptor.addFrameSlot("float32", FrameSlotKind.Float);
		lexicalscope.locals.put("float32", frameSlot);
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
		for(GoBaseIRNode child : node.getChildren())
			if(child != null)
				child.accept(this);
		return null;
	}

	@Override
	public Object visitIdent(GoIRIdentNode node) {
		String name = node.getIdentifier();
		GoExpressionNode result = null;
		//System.out.println(name+" "+lexicalscope.locals);
	    final FrameSlot frameSlot = lexicalscope.locals.get(name);
	    
	    if (frameSlot != null) {
	            /* Read of a local variable. */
	    	result = (GoExpressionNode)GoReadLocalVariableNodeGen.create(frameSlot);
	    } else {
	    	result = new GoIdentNode(language, name, result);
	    }
	    //result.setSourceSection(node.getSource(source));
	    return result;
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
		//int start = leftNode.getSourceSection().getCharIndex();
		//int end = rightNode.getSourceSection().getCharEndIndex() - start;
		//result.setSourceSection(source.createSection(start,end));
		return result;
	}

	public GoIntNode visitIRIntNode(GoIRIntNode node){
		GoIntNode result = new GoIntNode(node.getValue());
		//result.setSourceSection(node.getSource(source));
		return result;
	}

	public GoFloat32Node visitIRFloat32Node(GoIRFloat32Node node) { return new GoFloat32Node(node.getValue()); }

	public GoFloat64Node visitIRFloat64Node(GoIRFloat64Node node) { return new GoFloat64Node(node.getValue()); }
	
	public GoStringNode visitIRStringNode(GoIRStringNode node){
		GoStringNode result = new GoStringNode(node.getValue());
		//result.setSourceSection(node.getSource(source));
		return result;
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
		GoInvokeNode result = new GoInvokeNode(functionNode, arguments.getArguments());
		//int start = functionNode.getSourceSection().getCharIndex();
		//int end = arguments.getSourceSection().getCharEndIndex() + 1 - start;
		//result.setSourceSection(source.createSection(start,end));
		return result;
	}

	@Override
	public Object visitFuncDecl(GoIRFuncDeclNode node) {

		startFunction();

		GoBlockNode blockNode = (GoBlockNode) node.getBody().accept(this);
		GoFunctionBodyNode bodyNode = new GoFunctionBodyNode(blockNode);
		GoExpressionNode typeNode = (GoExpressionNode) node.getType().accept(this);
		GoExpressionNode nameNode = (GoExpressionNode) node.getName().accept(this);
		String name = node.getIdentifier();
		//int start = nameNode.getSourceSection().getCharIndex();
		//int end = blockNode.getSourceSection().getCharEndIndex();
		//SourceSection section = source.createSection(start, end);
		//System.out.println(section);
		GoRootNode root = new GoRootNode(language,frameDescriptor,bodyNode,null,name);
		allFunctions.put(name,root);
		finishBlock();
		
		//frameDescriptor = null;
		return null;
	}
	
	@Override
	public Object visitFuncType(GoIRFuncTypeNode node) {
		GoArrayExprNode params = (GoArrayExprNode) node.getParams().accept(this);
		GoArrayExprNode results = (GoArrayExprNode) node.getResults().accept(this);
		return new GoFuncTypeNode(params, results);
	}
	
	@Override
	public Object visitField(GoIRFieldNode node){
		GoArrayExprNode names = (GoArrayExprNode) node.getNames().accept(this);
		GoIdentNode type = (GoIdentNode) node.getType().accept(this);
		String typeName = node.getTypeName();
		return new GoFieldNode(names, type, typeName);
	}
	
	@Override
	public Object visitReturnStmt(GoIRReturnStmtNode node){
		return new GoReturnNode((GoExpressionNode)node.getChild().accept(this));	
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
		GoArrayExprNode result = new GoArrayExprNode(arguments);
		/*
		if(argumentsize > 0 && arguments[0] != null){
			int start = arguments[0].getSourceSection().getCharIndex();
			int end = arguments[argumentsize-1].getSourceSection().getCharEndIndex() - start;
			result.setSourceSection(source.createSection(start,end));
		}*/
		return result;
	}

	@Override
	public Object visitBlockStmt(GoIRBlockStmtNode node) {
		GoStatementNode[] body = (GoStatementNode[]) node.getChild().accept(this);
		GoBlockNode result = new GoBlockNode(body);
		/*
		if(body.length > 0){
			int start = node.getLbrace();
			int end = node.getRbrace();
			result.setSourceSection(source.createSection(start, end));
		}*/
		return result;
	}

	@Override
	public Object visitExprStmt(GoIRExprStmtNode node) {
		GoExprNode result = new GoExprNode( (GoExpressionNode) node.getChild().accept(this));
		//result.setSourceSection(source.createUnavailableSection());
		return result;
	}

	@Override
	public Object visitExpr(GoIRExprNode node) {
		GoExprNode result = new GoExprNode( (GoExpressionNode) node.getChild().accept(this));
		//result.setSourceSection(source.createUnavailableSection());
		return result;
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
		//int start = node.getOpTok();
		//int end = child.getSourceSection().getCharEndIndex() - start;
		//result.setSourceSection(source.createSection(start,end));
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

		GoArrayExprNode results = new GoArrayExprNode(result.getArguments());
		//int start = node.getTokPos();
		//int end = result.getSourceSection().getCharEndIndex() - start;
		//result.setSourceSection(source.createSection(start,end));
		return results;
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
		GoReadArrayNode read = GoReadArrayNodeGen.create(index, slot);
		//int start = node.getLBrack();
		//int startLine = node.getLineNumber();
		//int length = node.getSourceSize();
		//read.setSourceSection(source.createSection(startLine,start,length));
		return read;
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
		GoArrayTypeExprNode result = new GoArrayTypeExprNode((GoIntNode) length,type);

		return result;
	}
	
	@Override
	public Object visitSliceExpr(GoIRSliceExprNode node){
		GoReadLocalVariableNode expr = (GoReadLocalVariableNode) node.getExpr().accept(this);
		GoExpressionNode low = null;
		if(node.getLow() != null){
			low = (GoExpressionNode) node.getLow().accept(this);
		}
		GoExpressionNode high = null;
		if(node.getHigh() != null){
			high = (GoExpressionNode) node.getHigh().accept(this);
		}
		GoExpressionNode max = null;
		if(node.isSlice3()){
			max = (GoExpressionNode) node.getMax().accept(this);
		}
		GoSliceExprNode result = new GoSliceExprNode(expr,low,high,max);
		

		//String lbrack = node.getSource();
		//int startLine = Integer.parseInt(lbrack.split(":")[1]);
		//int start = Integer.parseInt(lbrack.split(":")[2]);
		//int len = type.getSourceSection().getEndColumn();
		//int len = 3;
		//result.setSourceSection(source.createSection(startLine, start, len));
		return result;
	}
	
	@Override
	public Object visit(GoIRCompositeLitNode node){
		GoExpressionNode type = (GoExpressionNode) node.getExpr().accept(this);
		GoArrayExprNode elts = (GoArrayExprNode) node.getElts().accept(this);
		GoCompositeLitNode result = new GoCompositeLitNode((GoArrayTypeExprNode) type, elts);
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
		GoCaseClauseNode result = new GoCaseClauseNode(list, body);
		//int startLine = node.getSourceLine();
		//int start = node.getCaseStart();
		//int length = node.getSourceLength();
		//result.setSourceSection(source.createSection(startLine,start,length));
		return result;
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

		GoSwitchNode result = new GoSwitchNode(init, tag, body);
		//int line = node.getSourceLine();
		//result.setSourceSection(source.createSection(line));
		return result;
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
		
		GoForNode result = new GoForNode(init, cond, post, body);
		//result.setSourceSection(source.createSection(node.getSourceLine()));
		return result;
	}

	@Override
	public Object visitIncDecStmt(GoIRIncDecStmtNode node) {
		
		final GoExpressionNode result;
		GoIRIdentNode ident = (GoIRIdentNode) node.getChild();
		GoIRIntNode one = new GoIRIntNode(1, node.getTokPos());
		
		String op = node.getOp();
		final GoIRBinaryExprNode binary_expr = new GoIRBinaryExprNode(op.substring(0,1), ident, one, null);

		GoIRAssignmentStmtNode res = new GoIRAssignmentStmtNode(ident,binary_expr);
		result = (GoExpressionNode) res.accept(this);
		return result;
	}
	
	/**
	 * TODO Fix scoping of if statements to only create new scopes per condition block not over the entire
	 * if statement
	 */
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
		GoIfStmtNode result = new GoIfStmtNode(Init,CondNode,Body,Else);
		//result.setSourceSection(source.createSection(node.getSourceLine()));
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
		//result.setSourceSection(source.createSection(node.getSourceLine()));
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

}
