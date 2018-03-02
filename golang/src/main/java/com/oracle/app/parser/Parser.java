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
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;
import com.oracle.app.parser.ir.nodes.GoIRArrayListExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode;
import com.oracle.app.parser.ir.nodes.GoIRBinaryExprNode;
import com.oracle.app.parser.ir.nodes.GoIRBlockStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRDeclStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRExprNode;
import com.oracle.app.parser.ir.nodes.GoIRExprStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRFuncDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRGenDeclNode;
import com.oracle.app.parser.ir.nodes.GoIRIdentNode;
import com.oracle.app.parser.ir.nodes.GoIRInvokeNode;
import com.oracle.app.parser.ir.nodes.GoIRStmtNode;
import com.oracle.app.parser.ir.nodes.GoIRUnaryNode;
import com.oracle.app.parser.ir.nodes.GoIRValueSpecNode;
import com.oracle.app.parser.ir.nodes.GoTempIRNode;
import com.oracle.truffle.api.source.Source;

/**
 * 
 * Parses an ast file generated by the GoLang and creates
 * a 1-1 mapping of Go source code into our own IR representation.
 *
 */
public class Parser {
	//Magic number eraser
	private final int stringAttr  = 1;
	private final int stringVal   = 2;
	private final int regularAttr = 3;
	private final int regularVal  = 4;
	
	private final String file; // the file we open
	private GoLanguage language; // language we are passed
	private Source source;
	private BufferedReader reader; // used to read file
	private String currentLine; // String of the current line we are on
	private Matcher matchedTerm; // used for regex/parsing of file
	private Pattern astPattern = Pattern.compile("\\[\\]\\*ast\\.\\w+|\\.\\w+"); //for getting the type of node
	private Pattern nodeTypePattern = Pattern.compile("\\w+:|\"\\w+\"");
	private Pattern attrPattern= Pattern.compile("(\\w+): \"(.+)\"|(\\w+): (.+)"); //for getting the attributes
	

	/**
	 * 
	 * @param language
	 * @param source
	 * @throws FileNotFoundException
	 */
	public Parser(GoLanguage language, Source source) throws FileNotFoundException {
		this.file = source.getName();
		this.language = language;
		this.source = source;
		reader = new BufferedReader(new FileReader(this.file));
	}

	
	/**
	 * The starting point for the parse function. Initiates the call
	 * for the {@link GoTruffle.class} visitor.
	 * @return A Hashmap containing all function definitions
	 * @throws IOException
	 */
	public Map<String, GoRootNode> beginParse() throws IOException{
		String type;
		GoBaseIRNode k = null;
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group().substring(1);
				k = recParse(type);
			}
		}
		
		//Tree visitor for printing out the tree
		//GoVisitor visitor = new GoVisitor();
		//k.accept(visitor);
		
		GoTruffle truffleVisitor = new GoTruffle(language, source);
		k.accept(truffleVisitor);
		
		return truffleVisitor.getAllFunctions();
	}
	
	/**
	 * Depth first traversal of the ast file to recursively create the IRTree from the ast file. 
	 * Every occurence of an opening brace indicates a new node in the GoLang, so call recParse on the new node
	 * Every occurence of a closing brace indicates the ending of the current node so call getIRNode to generate the IRNode
	 * If no braces occur then fill in the attribute Map for  the current node
	 * @param currNode The current node of interest
	 * @return The new IRNode after gathering all the type information on it
	 * @throws IOException
	 */
	private GoBaseIRNode recParse(String currNode) throws IOException {
		Map<String, GoBaseIRNode> body = new HashMap<>();
		Map<String, String> attrs = new HashMap<>();
		String nodeName = currNode;
		
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
	    			matchedTerm = nodeTypePattern.matcher(currentLine);
	    			matchedTerm.find();
	    			String type = matchedTerm.group();
	    			type = type.substring(0,type.length()-1);
	    			body.put(type, recParse(nodeType));
	    			
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
	
	public GoIRValueSpecNode assignToValueSpec(GoIRArrayListExprNode l, GoIRArrayListExprNode r){
		GoBaseIRNode temp = new GoIRBinaryExprNode("+",l.getChildren().get(0),r.getChildren().get(0));
		r.getChildren().set(0, temp);
		return new GoIRValueSpecNode(l,null,r);
	}
	
	/**
	 * Create the IRNode.
	 * Match the nodetype to a switch case to create the specific IRNode that corresponds to
	 * the name and pass in the necessary attributes and children nodes in the constructor.
	 * @param nodeType The name of the node to make
	 * @param attrs The attributes associated with the node
	 * @param body The body nodes to insert for the new node
	 * @return The new IRNode
	 */
	public GoBaseIRNode getIRNode(String nodeType, Map<String,String> attrs, Map<String,GoBaseIRNode> body) {
		switch(nodeType) {
			case "AssignStmt":
				GoIRArrayListExprNode lhs = (GoIRArrayListExprNode) body.get("Lhs");
				GoIRArrayListExprNode rhs = (GoIRArrayListExprNode) body.get("Rhs");
				String assigntype = attrs.get("Tok");
				switch(assigntype){
				case "=":
					return new GoIRValueSpecNode(lhs,null,rhs);
				case "+=":
					return assignToValueSpec(lhs,rhs);
				default:
					System.out.println("Missing Assignment case " + assigntype);
				}
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "BasicLit":
				return new GoIRBasicLitNode(attrs.get("Kind"),attrs.get("Value"));
				
			case "BinaryExpr":
				return new GoIRBinaryExprNode(attrs.get("Op"),body.get("X"),body.get("Y"));
				
			case "UnaryExpr":
				return new GoIRUnaryNode(attrs.get("Op"),body.get("X"));
				
			case "BlockStmt":
				return new GoIRBlockStmtNode((GoIRStmtNode) body.get("List"));
				
			case "CallExpr":
				GoBaseIRNode functionNode = body.get("Fun");
				GoIRArrayListExprNode args = (GoIRArrayListExprNode) body.get("Args");
				return new GoIRInvokeNode(functionNode,args);
				
			case "DeclStmt":
				return new GoIRDeclStmtNode(body.get("Decl"));
				
			case "Decl":
				ArrayList<GoBaseIRNode> list = new ArrayList<>();
				for(GoBaseIRNode child : body.values()){
					list.add(child);
				}
				return new GoIRDeclNode(list);
				
			case "Expr":
				
				ArrayList<GoBaseIRNode> list1 = new ArrayList<>();
				for(GoBaseIRNode child : body.values()){
					list1.add(child);
				}
				
				return new GoIRArrayListExprNode(list1);
				
			case "ExprStmt":
				return new GoIRExprStmtNode(body.get("X"));
				
			case "FieldList":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "File":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "FuncDecl"://(GoBaseIRNode receiver, GoBaseIRNode name, GoBaseIRNode type, GoBaseIRNode body)
				GoBaseIRNode recv = body.get("Recv");
				GoBaseIRNode name = body.get("Name");
				GoBaseIRNode type = body.get("Type");
				GoBaseIRNode func_body = body.get("Body");
				return new GoIRFuncDeclNode(recv,name,type,func_body);
				
			case "FuncType":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "GenDecl":
				return new GoIRGenDeclNode(attrs.get("Tok"),(GoIRArrayListExprNode) body.get("Specs"));
				
			case "]*ast.Ident":
				ArrayList<GoBaseIRNode> identlist = new ArrayList<>();
				for(GoBaseIRNode child : body.values()){
					identlist.add(child);
				}
				
				return new GoIRArrayListExprNode(identlist);
				
			case "Ident":
				GoBaseIRNode obj = body.get("Obj");
				return new GoIRIdentNode(attrs.get("Name"),obj);
				
			case "ImportSpec":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "Object":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "ParenExpr":
				return new GoIRExprStmtNode(body.get("X"));
				
			case "Scope":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "SelectorExpr":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "Spec":
				ArrayList<GoBaseIRNode> speclist = new ArrayList<>();
				for(GoBaseIRNode child : body.values()){
					speclist.add(child);
				}
				
				return new GoIRArrayListExprNode(speclist);
				
			case "Stmt":
				ArrayList<GoBaseIRNode> stmtlist = new ArrayList<>();
				for(GoBaseIRNode children : body.values()){
					stmtlist.add(children);
				}
				return new GoIRStmtNode(stmtlist);
				
			case "ValueSpec":
				GoIRArrayListExprNode names = (GoIRArrayListExprNode) body.get("Names");
				GoBaseIRNode valuetype = body.get("Type");
				GoIRArrayListExprNode values = (GoIRArrayListExprNode) body.get("Values");
				return new GoIRValueSpecNode(names,valuetype,values);
				
			default:
				System.out.println("Error, in default: " + nodeType);
				
		}
		return new GoTempIRNode(nodeType,attrs,body);
	}

	/**
	 * The starting point of the parse function called from GoLanguage
	 * @param language
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source) throws IOException{
		Parser parser = new Parser(language, source);
		return parser.beginParse();
	}
	
}