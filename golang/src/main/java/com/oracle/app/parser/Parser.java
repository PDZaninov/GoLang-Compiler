package com.oracle.app.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoBasicNode;
import com.oracle.app.nodes.GoExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.nodes.GoTempIRNode;
import com.oracle.truffle.api.source.Source;

// Parses a go file ast dump
public class Parser {
	//Magic number eraser
	private final int stringAttr  = 1;
	private final int stringVal   = 2;
	private final int regularAttr = 3;
	private final int regularVal  = 4;
	
	private final String file; // the file we open
	private GoLanguage language; // language we are passed
	private BufferedReader reader; // used to read file
	private String currentLine; // String of the current line we are on
	private Matcher matchedTerm; // used for regex/parsing of file
	private Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+"); //for getting the type of node
	private Pattern attrPattern= Pattern.compile("(\\w+): \"(.+)\"|(\\w+): (.+)"); //for getting the attributes
	private GoNodeFactory factory; //used to call functions to create nodes
	

	/*Constructor
	 * Gets the file we open, language...
	 * Opens factory and creates the hashmap
	 */
	public Parser(GoLanguage language, Source source) throws FileNotFoundException {
		this.file = source.getName();
		this.language = language;
		reader = new BufferedReader(new FileReader(this.file));
		factory = new GoNodeFactory(language,source);
	}
//	Constructor for testing
//	public Parser(String filename) throws FileNotFoundException {
//		file = filename;
//		reader = new BufferedReader(new FileReader(this.file));
//	}
	
	/* TODO what are we doing to the .File
	 * Purpose:
	 * Start the parse process. Also returns the hashmap with the nodes inside.
	 * We use this that map to execute. For now ignores the .file
	 * Input:
	 * Output:
	 * Hashmap of <string function name, Go node tree for that function>
	 */
	public Map<String, GoRootNode> beginParse() throws IOException{
		String type;
		GoBaseIRNode k = null;
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group();
				if(type.equals(".File")){
					//factory.startFunction();
					k = recParse(type.substring(1));
				}
			}
		}
		//dumpTree(k,0);
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
	private GoBaseIRNode recParse(String currNode) throws IOException {
		ArrayList<GoBaseIRNode> body = new ArrayList<>();
		Map<String, String> attrs = new HashMap<>();
		String nodeName = currNode;
		int bindex;//used to get start of the match of the reg ex.
		
		//while statement reading the file
		
		while((currentLine = reader.readLine()) != null) {
			if(currentLine.indexOf('}') != -1) {
				
	    		//creating itself, going up
				return getIRNode(nodeName,attrs,body);

	    	}
	    	else if(currentLine.indexOf('{') >= 0) {
	    		//going deeper into the tree creating children first
	    		
	    		matchedTerm = astPattern.matcher(currentLine);
	    		if(matchedTerm.find()){
	    			String nodeType = matchedTerm.group().substring(1);
	    			if(nodeType.equals("FuncDecl")){
	    				//factory.startBlock();
	    			}
	    			GoBaseIRNode par = recParse(nodeType);
	    			if(par != null)
	    				body.add(par);
	    		}
	    	}
	    	else {
	    		//adding attributes
	    		matchedTerm = attrPattern.matcher(currentLine);
	    		if(matchedTerm.find()){
	    			//TO-DO: Maybe shouldnt be hardcoded?????
	    			if(matchedTerm.group(stringAttr) == null){
	    				attrs.put(matchedTerm.group(regularAttr), matchedTerm.group(regularVal));
	    			}
	    			else{
	    				attrs.put(matchedTerm.group(stringAttr), matchedTerm.group(stringVal));
	    			}
	    		}
	    	}
		}
		
		return null;
	}
	
//	public void dumpTree(GoBaseIRNode node, int spacing) {
//		if(node != null) {
//			spaces(spacing);
//			System.out.println(node.toString());
//			ArrayList<GoBaseIRNode> l = node.getChildren();
//			for(int z =0; z < l.size(); z ++) {
//				dumpTree(l.get(z),spacing+1);
//			}
//		}
//	}
//	
//	public void spaces(int num) {
//		for(int x = 0; x < num; x++) {
//			System.out.print(". ");
//		}
//	}
	
	public GoBaseIRNode getIRNode(String nodeType, Map<String,String> attrs, ArrayList<GoBaseIRNode> body) {
		switch(nodeType) {
			case "AssignStmt":
				return new GoTempIRNode(nodeType,attrs,body);
			case "BasicLit":
				return new GoTempIRNode(nodeType,attrs,body);
			case "BinaryExpr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "UnaryExpr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "BlockStmt":
				return new GoTempIRNode(nodeType,attrs,body);
			case "CallExpr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Decl":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Expr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "ExprStmt":
				return new GoTempIRNode(nodeType,attrs,body);
			case "FieldList":
				return new GoTempIRNode(nodeType,attrs,body);
			case "File":
				return new GoTempIRNode(nodeType,attrs,body);
			case "FuncDecl":
				return new GoTempIRNode(nodeType,attrs,body);
			case "FuncType":
				return new GoTempIRNode(nodeType,attrs,body);
			case "GenDecl":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Ident":
				return new GoTempIRNode(nodeType,attrs,body);
			case "ImportSpec":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Object":
				return new GoTempIRNode(nodeType,attrs,body);
			case "ParenExpr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Scope":
				return new GoTempIRNode(nodeType,attrs,body);
			case "SelectorExpr":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Spec":
				return new GoTempIRNode(nodeType,attrs,body);
			case "Stmt":
				return new GoTempIRNode(nodeType,attrs,body);
			default:
				System.out.println("Error, in default: " + nodeType);
				
		}
		return new GoTempIRNode(nodeType,attrs,body);
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
 * may do GoBasicNode if no good mapping is available.
 */
	public GoStatementNode getNodeType(String nodeType, Map<String,String> attrs, ArrayList<GoStatementNode> body) throws IOException{

		switch(nodeType) {
			case "AssignStmt":
				return factory.createGoWriteLocalVariableNode(body);
			case "BasicLit":
				return factory.createBasicLit(attrs.get("Value"),attrs.get("Kind"));
			case "BinaryExpr":
				return factory.createBinaryExprNode(attrs.get("Op"),body);
			case "UnaryExpr":
				return factory.createUnaryExprNode(attrs.get("Op"),body);
			case "BlockStmt":
				//needs to return a block node
				return factory.createFunctionBlock(body);
			case "CallExpr":
				//Create invoke node
				return factory.createInvoke(body);
			case "Decl":
				//Start a new lexical scope for decls
				return factory.createDecl(body);
			case "Expr":
				return factory.createExpr(body);
			case "ExprStmt":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "FieldList":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "File":
				return factory.createFileNode(nodeType,body.toArray(new GoStatementNode[body.size()]));
			case "FuncDecl":
				//Start a new lexical scope
				//System.out.println(nodeType+" "+body);
				factory.createFunction(body);
				break;
			case "FuncType":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "GenDecl":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "Ident":
				//Should also cover cases of having an object attatched
				String fname =attrs.get("Name");
				return factory.createIdentNode(fname,body);
			case "ImportSpec":
				System.out.println(nodeType);
				break;
			case "Object":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "ParenExpr":
				return new GoExprNode((GoExpressionNode) body.get(0));
			case "Scope":
				return new GoBasicNode(nodeType, body.toArray(new GoExpressionNode[body.size()]));
			case "SelectorExpr":
				System.out.println(nodeType);
				break;
			case "Spec":
				System.out.println(nodeType);
				break;
			case "Stmt":
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
	
//	public static void main(String[] args) throws IOException {
//		Parser p = new Parser("Addition.ast");
//		p.beginParse();
//	}
}