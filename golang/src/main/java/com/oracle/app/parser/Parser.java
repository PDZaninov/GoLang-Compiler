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
					recParse(type);
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
	    		
	    		
	    		body.add(recParse(nodeType));
	    		
	    		
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
		String type = "";
		switch(nodeType) {
			case "File":
				System.out.println(nodeType);
				break;
			case "Ident":
				//Definitely will need changing but is going to be used for call expr for now
				//without chained calls
				//TEMPORARY PLS CHANGE
				return new GoFunctionLiteralNode(language, searchAttr("Name: ", attrs));
				
			case "Decl":
				//Start a new lexical scope for decls
				return decl(attrs, body);
				
			case "Spec":
				System.out.println(nodeType);
				break;
				
			case "ImportSpec":
				System.out.println(nodeType);
				break;
				
			case "BasicLit":
				return basicLit(attrs);
				
			case "FuncDecl":
				//Start a new lexical scope
				createFunction(attrs, body);
				break;
				
			case "Object":
				System.out.println(nodeType);
				break;
				
			case "FuncType":
				System.out.println(nodeType);
				break;
				
			case "BlockStmt":
				//needs to return a block node
				//return createFunctionBlock(attrs, body);
				
			case "Stmt":
				System.out.println("Skip " + nodeType);
				break;
				
			case "ExprStmt":
				//Needs changing
				//return getNodeType(type.substring(1), attrs, body);
				System.out.println("ExprStmt " + nodeType);
			case "CallExpr":
				//Create invoke node
				return createInvoke(attrs, body);
				
			case "SelectorExpr":
				System.out.println(nodeType);
				break;
				
			case "Expr":
				System.out.println(nodeType);
				break;
				
			case "GenDecl":
				genDecl();
				System.out.println(nodeType);
				break;
				
			case "FieldList":
				System.out.println(nodeType);
				break;
				
			case "Scope":
				System.out.println(nodeType);
				break;
				
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
		
		return name;
	}
	
	public GoExpressionNode basicLit(ArrayList<String> attrs) throws IOException{
		return new GoStringNode(searchAttr("Value: ", attrs));
	}
	
	public GoInvokeNode createInvoke(ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{
		/*
		String type;
		GoFunctionLiteralNode function = null;
		List<GoExpressionNode> argumentNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				//Temporary way to get the function call
				if(type.equals(".Ident")){
					function = (GoFunctionLiteralNode) getNodeType(type.substring(1), attrs, body);
				}
				else if(!type.equals(".Expr")){
					//Need to track how many arguments were reading in instead of breaking on the first one
					argumentNodes.add((GoExpressionNode) getNodeType(type.substring(1), attrs, body));
					break;
				}
			}
		}
		GoInvokeNode invokeNode = new GoInvokeNode(function,argumentNodes.toArray(new GoExpressionNode[argumentNodes.size()]));
		return invokeNode;
		*/
		GoFunctionLiteralNode function = (GoFunctionLiteralNode) getNodeType("Ident", attrs, body);

		GoInvokeNode invokeNode = new GoInvokeNode(function, body.toArray(new GoExpressionNode[body.size()]));
		return invokeNode;
	}
	
	/*
	 * Create a block of statements. Currently only specific to function blocks
	 */
	/*
	public GoFunctionBodyNode createFunctionBlock(ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{
		
		String type;
		List<GoStatementNode> bodyNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				if(!type.equals(".Stmt")){
					//Also need to keep track of how many statmeents to process
					bodyNodes.add((GoStatementNode) getNodeType(type.substring(1), attrs, body)); 
					break;
				}
			}
		}
		//Something about flattening nodes
		//Experimental flattening tactics
		List<GoStatementNode> flatNodes = new ArrayList<>(bodyNodes.size());
		flattenBlocks(bodyNodes, flatNodes);
		//Loop around to give source section to each node
		GoBlockNode blockNode = new GoBlockNode(flatNodes.toArray(new GoStatementNode[flatNodes.size()]));
		GoFunctionBodyNode functionBlockNode = new GoFunctionBodyNode(blockNode);
		//Blocknode source section
		return functionBlockNode;
		
	} */
	
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
	public void createFunction(ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{
		//String name = "", type;
		/*
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				if(type.equals(".Ident")){
					//Get the name
					name = ident();
				}
				else if(type.equals(".FuncType")){
					//fill in paramters and function return type
					//Might just skip this for now LUL
				}
				else if(type.equals(".BlockStmt")){
					//body nodes Might be able to just create a block node
					GoFunctionBodyNode bodyNode = (GoFunctionBodyNode) getNodeType(type.substring(1), attrs, body);
					GoRootNode root = new GoRootNode(language,null,bodyNode,null,name);
					allFunctions.put(name,root);
					return;
				}
			}
			//Create function node out here and add it to the allFunctions hashmap
			//and maybe break out of this loop somewhere
			 */
		String name = searchAttr("Name: ", attrs);
		GoBlockNode blockNode = new GoBlockNode(body.toArray(new GoStatementNode[body.size()]));
		GoFunctionBodyNode bodyNode = new GoFunctionBodyNode(blockNode);
		GoRootNode root = new GoRootNode(language,null,bodyNode,null,name);
		allFunctions.put(name,root);
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
	
	public GoDeclNode decl(ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{
		GoDeclNode node = new GoDeclNode(body.toArray(new GoStatementNode[body.size()]));
		return node;
	}
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source) throws IOException{
		Parser parser = new Parser(language, source);
		return parser.beginParse();
	}
	

}