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

// Parses a go file ast dump
public class Parser {
	
	private final String file; // the file we open
	private GoLanguage language; // language we are passed
	private BufferedReader reader; // used to read file
	private String currentLine; // String of the current line we are on
	private Matcher matchedTerm; // used for regex/parsing of file
	private Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+"); //for getting the type of node
	private Pattern attr	   = Pattern.compile("[a-zA-Z][.]*"); //for getting the attributes
	private GoNodeFactory factory; //used to call functions to create nodes
	private Map<String, GoRootNode> allFunctions; //hashmap of function names , go node trees
	

	/*Constructor
	 * Gets the file we open, language...
	 * Opens factory and creates the hashmap
	 */
	public Parser(GoLanguage language, Source source) throws FileNotFoundException {
		this.file = source.getName();
		this.language = language;
		reader = new BufferedReader(new FileReader(this.file));
		factory = new GoNodeFactory(language,source);
		allFunctions = new HashMap<>();
	}
	/* TODO wtf are we doing the .File
	 * Purpose:
	 * Start the parse process. Also returns the hashmap with the nodes inside.
	 * We use this that map to execute. For now ignores the .file
	 * Input:
	 * Output:
	 * Hashmap of <string function name, Go node tree for that function>
	 */
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
		return factory.getAllFunctions();
	}
	
	/*Purpose: 
	 * Parse the ast dump. It does this by creating all the children
	 * before the parent. Depth first. Gets all attributes and children
	 * and once it hits the closing } it creates itself. Does this by recursion.
	 * Input:
	 * String currNode: a string of the current node type
	 * Output:
	 * the first node it tried to parse.
	 * Not sure if needed.
	 */
	private GoStatementNode recParse(String currNode) throws IOException {
		ArrayList<GoStatementNode> body = new ArrayList<>();
		ArrayList<String> attrs = new ArrayList<>();
		String nodeName = currNode;
		int bindex;
		
		//while statement reading the file
		
		while((currentLine = reader.readLine()) != null) {
			if(currentLine.indexOf('}') != -1) {
				
	    		//creating itself, going up
				return getNodeType(nodeName,attrs,body);

	    	}
	    	else if(currentLine.indexOf('{') >= 0) {
	    		//going deeper into the tree creating children first
	    		
	    		matchedTerm = astPattern.matcher(currentLine);
	    		matchedTerm.find();
	    		bindex = matchedTerm.start();
	    		
	    		//gets rid of the (len  = 1 ) part
	    		String nodeType = currentLine.substring(bindex+1, currentLine.length()-2);
	    		if(nodeType.contains("(len")) {
	    			nodeType = nodeType.substring(0, nodeType.indexOf("(") - 1);
	    		}
	    		
	    		GoStatementNode par = recParse(nodeType);
	    		if(par != null)
	    			body.add(par);
	    	}
	    	else {
	    		//adding attributes
	    		matchedTerm = attr.matcher(currentLine);
	    		matchedTerm.find();
	    		bindex = matchedTerm.start();
	    		attrs.add(currentLine.substring(bindex));
	    	}
		}
		return null;
	}
/*
 * TODO
 * eventually replace with map[nodetype] = new node
 * or something like that i believe
 * and maybe get rid of the place holder GoBasicNode
 * Purpose:
 * create and return the creation of the node of type nodeType
 *  with body for children and attrs for attributes
 *  with parse makes a structure of function -> body -> lots of children 
 * Input: 
 * String nodeType: a string of the nodeType for the switch case
 * ArrayList attrs: an arraylist of the attributed for the
 * ArrayList body: contains children if any
 * Output:
 * the creation of the node
 * may do GoBasicNode if no good mapping is avaible.
 */
	public GoStatementNode getNodeType(String nodeType, ArrayList<String> attrs, ArrayList<GoStatementNode> body) throws IOException{

		String name = searchAttr("Name: ", attrs);
		String value = searchAttr("Value: ", attrs);
		switch(nodeType) {
			case "File":
				return new GoBasicNode(nodeType, body.toArray(new GoStatementNode[body.size()]));
				
			case "Ident":
				return new GoFunctionLiteralNode(language, name);
				
			case "Decl":
				//Start a new lexical scope for decls
				return factory.createDecl(body);
				
			case "Spec":
				System.out.println(nodeType);
				break;
				
			case "ImportSpec":
				System.out.println(nodeType);
				break;
				
			case "BasicLit":
				return factory.createBasicLit(value);
				
			case "FuncDecl":
				//Start a new lexical scope
				factory.createFunction(name, body);
				break;
				
			case "Object":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "FuncType":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "BlockStmt":
				//needs to return a block node
				return factory.createFunctionBlock(body);
				
			case "Stmt":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "ExprStmt":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
				
			case "CallExpr":
				//Create invoke node
				return factory.createInvoke(body);
				
			case "SelectorExpr":
				System.out.println(nodeType);
				break;
				
			case "Expr":
				return factory.createExpr(body);
				
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
	/* Purpose:
	 * To find a string within an array list then return whatever comes after :
	 * Input: 
	 * String attr - search for the string attr in the arraylist...
	 * ArrayList<String> attrs - Arraylist of attributes of a node
	 * Output:
	 * -String of the value attached to the given input
	 * Example:
	 * searchAttr("Value: ", attrs);
	 * will find the line with "Value: "hello""
	 * and return hello
	 */
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
		return name;
	}
	/*
	 * This is what is called to start building the ast.
	 * Called from GoLanguage.java
	 * Output:
	 * returns the hashmap of function names, go node trees
	 */
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source) throws IOException{
		Parser parser = new Parser(language, source);
		return parser.beginParse();
	}
}