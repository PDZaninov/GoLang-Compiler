package com.oracle.app.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoBasicNode;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoFileNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.call.GoInvokeNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.app.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.expression.GoFunctionLiteralNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

public class Parser {
	private final String file;
	private GoLanguage language;
	private BufferedReader reader;
	private String currentLine;
	private Matcher matchedTerm; 
	private Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+");
	private Pattern attr	   = Pattern.compile("[a-zA-Z][.]*");
	private GoNodeFactory factory;
	private Map<String, GoRootNode> allFunctions;
	

	public Parser(GoLanguage language, Source source) throws FileNotFoundException {
		this.file = source.getName();
		this.language = language;
		reader = new BufferedReader(new FileReader(this.file));
		factory = new GoNodeFactory(language,source);
		allFunctions = new HashMap<>();
	}
	
	public Map<String, GoRootNode> beginParse() throws IOException{
		String type;
		
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group();
				if(type.equals(".File")){
					recParse(type.substring(1));
				}
			}
		}
		return allFunctions;
		
	}
	
	private GoStatementNode recParse(String currNode) throws IOException {
		ArrayList<GoStatementNode> body = new ArrayList<>();
		ArrayList<String> attrs = new ArrayList<>();
		String nodeName = currNode;
		int bindex;
		
		//while statement
		while((currentLine = reader.readLine()) != null) {
			if(currentLine.indexOf('}') != -1) {
	    		
				return getNodeType(nodeName,attrs,body);

	    	}
	    	else if(currentLine.indexOf('{') >= 0) {
	    		matchedTerm = astPattern.matcher(currentLine);
	    		matchedTerm.find();
	    		bindex = matchedTerm.start();
	    		
	    		//It works
	    		String nodeType = currentLine.substring(bindex+1, currentLine.length()-2);
	    		if(nodeType.contains("(len")) {
	    			nodeType = nodeType.substring(0, nodeType.indexOf("(") - 1);
	    		}
	    		
	    		GoStatementNode par = recParse(nodeType);
	    		if(par != null)
	    			body.add(par);
	    		
	    		
	    	}
	    	else {
	    		matchedTerm = attr.matcher(currentLine);
	    		matchedTerm.find();
	    		bindex = matchedTerm.start();
	    		attrs.add(currentLine.substring(bindex));
	    		
	    	}
		}
		
		return null;
	}


	public GoStatementNode getNodeType(String nodeType, ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{
		//System.out.println(nodeType + " " + body.size());
		String name = searchAttr("Name: ", attrs);
		String value = searchAttr("Value: ", attrs);
		switch(nodeType) {
			case "File":
				return new GoBasicNode(nodeType, body.toArray(new GoStatementNode[body.size()]));
			case "Ident":
				//Definitely will need changing but is going to be used for call expr for now
				//without chained calls
				//TEMPORARY PLS CHANGE
				return new GoFunctionLiteralNode(language, name);
				
			case "Decl":
				//Start a new lexical scope for decls
				return decl(body);
				
			case "Spec":
				System.out.println(nodeType);
				break;
				
			case "ImportSpec":
				System.out.println(nodeType);
				break;
				
			case "BasicLit":
				return basicLit(value);
				
			case "FuncDecl":
				//Start a new lexical scope
				createFunction(name, body);
				break;
				
			case "Object":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "FuncType":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "BlockStmt":
				//needs to return a block node
				return createFunctionBlock(body);
				
			case "Stmt":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "ExprStmt":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "CallExpr":
				//Create invoke node
				return createInvoke(body);
			case "SelectorExpr":
				System.out.println(nodeType);
				break;
				
			case "Expr":
				return createExpr(body);
			case "GenDecl":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "FieldList":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "Scope":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			default:
				System.out.println("Error, in default: " + nodeType);
				
		}
		return null;

	}
	
	public String searchAttr(String attr, ArrayList<String> attrs) {
		String name = "";
		for(int i = 0; i < attrs.size(); i++) {
			if(attrs.get(i).contains(attr)) {
				name = attrs.get(i);
			}
		}
		if(!name.isEmpty()) {
			name = name.split(": ", 2)[1];
			name = name.substring(1, name.length()-1);
		}
		//System.out.println(name);
		return name;
	}
	
	public GoExpressionNode basicLit(String name) throws IOException{
		return new GoStringNode(name);
	}
	
	public GoExpressionNode createExpr(ArrayList<GoStatementNode> body) throws IOException{
		return new GoExprNode(body.toArray(new GoExpressionNode[body.size()]));
	}
	
	public GoInvokeNode createInvoke(ArrayList<GoStatementNode> body) throws IOException{
		GoExpressionNode function = (GoExpressionNode) body.remove(0);
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
	
	/*
	 * Creates a function node and adds it to the function hashmap
	 * Needs to still add in paramters and return types/parameters
	 * and lexical scope is needed too
	 */
	public void createFunction(String name, ArrayList<GoStatementNode> body) throws IOException{
/*
		String name = searchAttr("Name: ", attrs);
		System.out.println("name in create function: " + name);
		for(int i = 0; i < body.size(); i++) {
			if(body.get(i) instanceof GoFunctionBodyNode) {
				System.out.println("FOUND THE BODY NODE " + i);
			}
		}
		*/
		System.out.println("name in create function: " + name);
		GoRootNode root = new GoRootNode(language,null,(GoFunctionBodyNode)body.get(2),null,"main");
		allFunctions.put("main",root);
	}
	
	/*
	 * Currently only used by createFunction to get the function name
	 * Working only off of the assumption of a HelloWorld main function
	 * A BUNCH OF ASSUMPTIONS BUT EH..... jk should be fixed
	 */
	public String ident() throws IOException{
		reader.readLine();
		currentLine = reader.readLine();
		return currentLine.split("\"")[1];
	}
	
	public GoStatementNode[] genDecl() throws IOException{
		return null;
	}
	
	public GoDeclNode decl(ArrayList<GoStatementNode> body) throws IOException{
		GoDeclNode node = new GoDeclNode(body.toArray(new GoStatementNode[body.size()]));
		return node;
	}
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source) throws IOException{
		Parser parser = new Parser(language, source);
		return parser.beginParse();
	}
	

}