package com.oracle.app.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    /*
     * *************************************************************************
     * All the create*() methods. Here we create the nodes from the information
     * we get from the parser.
     * *************************************************************************
     */
    
    public GoExpressionNode createIdentNode(String name, ArrayList<GoStatementNode> body){
    	return new GoIdentNode(language, name, body.toArray(new GoStatementNode[body.size()]));
    }
    
    public GoExpressionNode createBasicLit(String name) throws IOException{
		return new GoStringNode(name);
	}
	
	public GoExpressionNode createExpr(ArrayList<GoStatementNode> body) throws IOException{
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
	
	/*
	 * Creates a function node and adds it to the function hashmap
	 * Needs to still add in paramters and return types/parameters
	 * and lexical scope is needed too
	 */
	
	//Assumes the first value is the ident node, but i did see there might be a case
	//where the first one is not ident. But that is also something for the future
	public void createFunction(ArrayList<GoStatementNode> body) throws IOException{
		String name = body.get(0).toString();
		GoRootNode root = new GoRootNode(language,null,(GoFunctionBodyNode)body.get(2),null,name);
		allFunctions.put(name,root);
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
}