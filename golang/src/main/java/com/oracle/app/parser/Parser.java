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
	private static GoLanguage language;
	private static BufferedReader reader;
	private static String currentLine;
	private static Matcher matchedTerm; 
	private static Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+");
	private static GoNodeFactory factory;
	private static Map<String, GoRootNode> allFunctions;

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
					getNodeType(type.substring(1));
				}
			}
		}
		return allFunctions;
		
	}
		
	
	//written by Petar, we need this owrking asap, im not sorry.
	//TO-DO: ADD A FACTORY INSTEAD
	public static Node getNodeType(String nodeType) throws IOException{
		String type;
		switch(nodeType) {
			case "File":
				while((currentLine = reader.readLine()) != null){
					matchedTerm = astPattern.matcher(currentLine);
					if(matchedTerm.find()){
						type = matchedTerm.group();
						if(type.equals(".Decl")){
							GoDeclNode bodyNode = (GoDeclNode) getNodeType(type.substring(1));
							return new GoFileNode(bodyNode);
						}
					}
				}
				System.out.println(nodeType);
				break;
			case "Ident":
				//Definitely will need changing but is going to be used for call expr for now
				//without chained calls
				//TEMPORARY PLS CHANGE
				reader.readLine();
				currentLine = reader.readLine();
				return new GoFunctionLiteralNode(language, currentLine.split("\"")[1]);
				
			case "Decl":
				//Start a new lexical scope for decls
				return decl();
				
			case "Spec":
				System.out.println(nodeType);
				break;
				
			case "ImportSpec":
				System.out.println(nodeType);
				break;
				
			case "BasicLit":
				return basicLit();
				
			case "FuncDecl":
				//Start a new lexical scope
				createFunction();
				break;
				
			case "Object":
				System.out.println(nodeType);
				break;
				
			case "FuncType":
				System.out.println(nodeType);
				break;
				
			case "BlockStmt":
				//needs to return a block node
				return createFunctionBlock();
				
			case "Stmt":
				System.out.println("Skip " + nodeType);
				break;
				
			case "ExprStmt":
				//Needs changing
				currentLine = reader.readLine();
				matchedTerm = astPattern.matcher(currentLine);
				if(matchedTerm.find()){
					type = matchedTerm.group();
					return getNodeType(type.substring(1));
				}
				
			case "CallExpr":
				//Create invoke node
				return createInvoke();
				
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
	
	static GoExpressionNode basicLit() throws IOException{
		reader.readLine();
		reader.readLine(); //This one hold the kind of basic lit the node is
		currentLine = reader.readLine();
		return new GoStringNode(currentLine.split("\"")[2]);
	}
	
	static GoInvokeNode createInvoke() throws IOException{
		String type;
		GoFunctionLiteralNode function = null;
		List<GoExpressionNode> argumentNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				//Temporary way to get the function call
				if(type.equals(".Ident")){
					function = (GoFunctionLiteralNode) getNodeType(type.substring(1));
				}
				else if(!type.equals(".Expr")){
					//Need to track how many arguments were reading in instead of breaking on the first one
					argumentNodes.add((GoExpressionNode) getNodeType(type.substring(1)));
					break;
				}
			}
		}
		GoInvokeNode invokeNode = new GoInvokeNode(function,argumentNodes.toArray(new GoExpressionNode[argumentNodes.size()]));
		return invokeNode;
	}
	
	/*
	 * Create a block of statements. Currently only specific to function blocks
	 */
	static GoFunctionBodyNode createFunctionBlock() throws IOException{
		String type;
		List<GoStatementNode> bodyNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				if(!type.equals(".Stmt")){
					//Also need to keep track of how many statmeents to process
					bodyNodes.add((GoStatementNode) getNodeType(type.substring(1))); 
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
	}
	
	private static void flattenBlocks(Iterable<? extends GoStatementNode> bodyNodes, List<GoStatementNode> flatNodes){
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
	static void createFunction() throws IOException{
		String name = "", type;
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
					GoFunctionBodyNode bodyNode = (GoFunctionBodyNode) getNodeType(type.substring(1));
					GoRootNode root = new GoRootNode(language,null,bodyNode,null,name);
					allFunctions.put(name,root);
					return;
				}
			}
			//Create function node out here and add it to the allFunctions hashmap
			//and maybe break out of this loop somewhere
		}
	}
	
	/*
	 * Currently only used by createFunction to get the function name
	 * Working only off of the assumption of a HelloWorld main function
	 * A BUNCH OF ASSUMPTIONS BUT EH..... jk should be fixed
	 */
	static String ident() throws IOException{
		reader.readLine();
		currentLine = reader.readLine();
		return currentLine.split("\"")[1];
	}
	
	static GoStatementNode[] genDecl() throws IOException{
		return null;
	}
	
	static GoDeclNode decl() throws IOException{
		String type;
		ArrayList<GoStatementNode> bodyNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				//Keep track of how many statements to process also
				bodyNodes.add((GoStatementNode) getNodeType(type.substring(1)));
				break;
			}
		}
		GoDeclNode node = new GoDeclNode(bodyNodes.toArray(new GoStatementNode[bodyNodes.size()]));
		return node;
	}
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source){
		Map<String, GoRootNode> function = new HashMap<>();
		function.put("main", new GoRootNode(language,null,null,null,"main"));
		return function;
	}
	

}