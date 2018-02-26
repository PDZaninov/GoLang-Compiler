package com.oracle.app.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoBasicNode;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.app.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.expression.GoAddNodeGen;
import com.oracle.app.nodes.expression.GoBinaryLeftShiftNodeGen;
import com.oracle.app.nodes.expression.GoBinaryRightShiftNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseAndNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseComplementNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseOrNodeGen;
import com.oracle.app.nodes.expression.GoBitwiseXORNodeGen;
import com.oracle.app.nodes.expression.GoDivNodeGen;
import com.oracle.app.nodes.expression.GoEqualNodeGen;
import com.oracle.app.nodes.expression.GoFunctionLiteralNode;
import com.oracle.app.nodes.expression.GoGreaterOrEqualNodeGen;
import com.oracle.app.nodes.expression.GoGreaterThanNodeGen;
import com.oracle.app.nodes.expression.GoLessOrEqualNodeGen;
import com.oracle.app.nodes.expression.GoLessThanNode;
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
import com.oracle.app.nodes.local.GoReadLocalVariableNodeGen;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;


public class GoNodeFactory {
    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }

    /* Gotta remove them magic numbers for clarity*/
    private int node = 0;
    
    /* State while parsing a source unit. */
    private final Source source;
    private final Map<String, GoRootNode> allFunctions;

    /* State while parsing a function. */
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    private List<GoStatementNode> methodNodes;

    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    private final GoLanguage language;

    public GoNodeFactory(GoLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, GoRootNode> getAllFunctions() {
        return allFunctions;
    }
    
    public void startFunction(){
    	startBlock();
    	frameDescriptor = new FrameDescriptor();
    }
    
    /*
     * *************************************************************************
     * All the create*() methods. Here we create the nodes from the information
     * we get from the parser.
     * *************************************************************************
     */
    
    /*
     * Create file node should be the last node created in the entire tree so
     * it finishes off the lexical scope.
     */
    public GoExpressionNode createFileNode(String name, GoStatementNode[] body){
    	lexicalScope = null;
    	return new GoBasicNode(name, body);
    }
    
    public GoExpressionNode createIdentNode(String name, ArrayList<GoStatementNode> body){

    	return new GoIdentNode(language, name, (GoExpressionNode) body.get(0));
    }
    
    public GoExpressionNode createBasicLit(String value, String kind) throws IOException{
    	final GoExpressionNode result;
    	//Still need to change FLOAT IMAG CHAR to proper nodes
    	//And need to catch for bigger numbers
    	switch(kind){
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
    			throw new RuntimeException("Undefined type: "+kind);
    	}
		return result;
	}
	
	public GoExpressionNode createExpr(ArrayList<GoStatementNode> body) throws IOException{
		String name = body.get(0).toString();
//		System.out.println("********: " + name);
    	if(body.get(0) instanceof GoIdentNode) {
	        final GoExpressionNode result;
	        final FrameSlot frameSlot = lexicalScope.locals.get(name);
	        if (frameSlot != null) {
	            /* Read of a local variable. */
	            result = GoReadLocalVariableNodeGen.create(frameSlot);
	        } else {
	            /* Read of a global name. In our language, the only global names are functions. */
	            result = new GoFunctionLiteralNode(language, name);
	        }
	        return result;
    	}

		return new GoExprNode((GoExpressionNode) body.get(node));
	}
	
	public GoInvokeNode createInvoke(ArrayList<GoStatementNode> body) throws IOException{
		GoExpressionNode function = (GoExpressionNode) body.remove(node);
		GoInvokeNode invokeNode = new GoInvokeNode(function, body.toArray(new GoExpressionNode[body.size()]));
		return invokeNode;
	}
	
	/*
	 * Create a block of statements. Currently only specific to function blocks
	 */
	public GoFunctionBodyNode createFunctionBlock(ArrayList<GoStatementNode> body) throws IOException{
		
		GoBlockNode blockNode = new GoBlockNode(body.toArray(new GoStatementNode[body.size()]));
		GoFunctionBodyNode bodyNode = new GoFunctionBodyNode(blockNode);
		return bodyNode;
	} 
	
	public GoExpressionNode createBinaryExprNode(String op, ArrayList<GoStatementNode> body){
		if(body.size() != 2){
			return null;
		}
		final GoExpressionNode result;
		switch(op){
			case"+":
				result = GoAddNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"-":
				result = GoSubNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"*":
				result = GoMulNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"/":
				result = GoDivNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"%":
				result = GoModNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"<":
				result = GoLessThanNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"<=":
				result = GoLessOrEqualNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"==":
				result = GoEqualNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case">":
				result = GoGreaterThanNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case">=":
				result = GoGreaterOrEqualNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"!=":
				result = GoNotEqualNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"&&":
				result = new GoLogicalAndNode((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"||":
				result = new GoLogicalOrNode((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"<<":
				result = GoBinaryLeftShiftNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case">>":
				result = GoBinaryRightShiftNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"&":
				result = GoBitwiseAndNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"|":
				result = GoBitwiseOrNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			case"^":
				result = GoBitwiseXORNodeGen.create((GoExpressionNode)body.get(0), (GoExpressionNode)body.get(1));
				break;
			default:
				throw new RuntimeException("Unexpected Operation: "+op);
		}
		return result;
	}
	public GoExpressionNode createUnaryExprNode(String op, ArrayList<GoStatementNode> body){
		if(body.size() != 1){
			return null;
		}
		final GoExpressionNode result;
		switch(op){
			case"!":
				result = GoLogicalNotNodeGen.create((GoExpressionNode)body.get(0));
				break;
			case"^":
				result = GoBitwiseComplementNodeGen.create((GoExpressionNode)body.get(0));
				break;
			case"+":
				result = GoPositiveSignNodeGen.create((GoExpressionNode)body.get(0));
				break;
			case"-":
				result = GoNegativeSignNodeGen.create((GoExpressionNode)body.get(0));
				break;
			default:
				throw new RuntimeException("Unexpected Operation: "+op);
		}
		return result;
	}
	/*
	 * Creates a function node and adds it to the function hashmap
	 * Needs to still add in paramters and return types/parameters
	 * Switches to the outer lexical scope because this is the end of the function
	 */
	
	//Assumes the first value is the ident node, but i did see there might be a case
	//where the first one is not ident. But that is also something for the future
	public void createFunction(ArrayList<GoStatementNode> body) throws IOException{
		String name = body.get(0).toString();
		
		GoRootNode root = new GoRootNode(language,frameDescriptor,(GoFunctionBodyNode)body.get(2),null,name);
		allFunctions.put(name,root);
		
		frameDescriptor = null;
		lexicalScope = lexicalScope.outer;
	}
	
	//TODO
	public GoStatementNode[] createGenDecl() throws IOException{
		return null;
	}
	
	public GoDeclNode createDecl(ArrayList<GoStatementNode> body) throws IOException{
		GoDeclNode node = new GoDeclNode(body.toArray(new GoStatementNode[body.size()]));
		return node;
	}
	
	
	
	private void flattenBlocks(Iterable<? extends GoStatementNode> bodyNodes, List<GoStatementNode> flatNodes){
		for(GoStatementNode node : bodyNodes){
			if(node instanceof GoBlockNode){
				flattenBlocks(((GoBlockNode) node).getStatements(), flatNodes);
			}
			else{
				flatNodes.add(node);
			}
		}
	}
    
    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
    }

	public GoStatementNode createGoWriteLocalVariableNode(ArrayList<GoStatementNode> body) {

		System.out.println("writing local variable");
        String name = body.get(0).toString();
        FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
        lexicalScope.locals.put(name, frameSlot);
        final GoExpressionNode result = GoWriteLocalVariableNodeGen.create((GoExpressionNode) body.get(1), frameSlot);


        return result;
	}
}