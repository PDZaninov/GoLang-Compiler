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
import com.oracle.app.nodes.GoFileNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

public class Parser {
	private final String file;
	private static GoLanguage lang;
	private static BufferedReader reader;
	private static String currentLine;
	private static Matcher matchedTerm; 
	private static Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+");
	private static GoNodeFactory factory;
	private static Map<String, GoRootNode> allFunctions;

	public Parser(GoLanguage lang, Source source) throws FileNotFoundException {
		this.file = source.getName();
		this.lang = lang;
		reader = new BufferedReader(new FileReader(this.file));
		factory = new GoNodeFactory(lang,source);
		allFunctions = new HashMap<>();
	}

	public Map<String, GoRootNode> beginParse() throws IOException{
		String type;
		
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group();
				getNodeType(type.substring(1));
			}
		}
		return allFunctions;
		
	}
		
	
	//written by Petar, we need this owrking asap, im not sorry.
	//TO-DO: ADD A FACTORY INSTEAD
	public static Node getNodeType(String nodeType) throws IOException{
		String type, num;
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
				System.out.println(nodeType);
				break;
				
			case "Decl":
				System.out.println(nodeType);
				//Start a new lexical scope for decls
				return decl();
				
			case "Spec":
				System.out.println(nodeType);
				break;
				
			case "ImportSpec":
				System.out.println(nodeType);
				break;
				
			case "BasicLit":
				System.out.println(nodeType);
				break;
				
			case "FuncDecl":
				//Start a new lexical scope
				createFunction();
				System.out.println(nodeType);
				break;
				
			case "Object":
				System.out.println(nodeType);
				break;
				
			case "FuncType":
				System.out.println(nodeType);
				break;
				
			case "BlockStmt":
				//needs to return a block node
				return createBlock();
				
			case "Stmt":
				System.out.println("Skip " + nodeType);
				break;
				
			case "ExprStmt":
				System.out.println(nodeType);
				break;
				
			case "CallExpr":
				while((currentLine = reader.readLine()) != null){
					matchedTerm = astPattern.matcher(currentLine);
					if(matchedTerm.find()){
						type = matchedTerm.group();
						getNodeType(type);
					}
				}
				System.out.println(nodeType);
				break;
				
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
	
	/*
	 * Create a block of statements. Currently only specific to function blocks
	 */
	static GoBlockNode createBlock() throws IOException{
		String type;
		List<GoStatementNode> bodyNodes = new ArrayList<>();
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
				type = matchedTerm.group();
				if(!type.equals(".Stmt")){
					bodyNodes.add((GoStatementNode) getNodeType(type)); 
				}
			}
		}
		//Something about flattening nodes
		//Experimental flattening tactics
		List<GoStatementNode> flatNodes = new ArrayList<>(bodyNodes.size());
		flattenBlocks(bodyNodes, flatNodes);
		//Loop around to give source section to each node
		GoBlockNode blockNode = new GoBlockNode(flatNodes.toArray(new GoStatementNode[flatNodes.size()]));
		//Blocknode source section
		return blockNode;
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
					GoBlockNode bodyNode = (GoBlockNode) getNodeType(type.substring(1));
					GoRootNode root = new GoRootNode(lang,null,bodyNode,null,name);
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
				bodyNodes.add((GoStatementNode) getNodeType(type.substring(1)));
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