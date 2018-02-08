package com.oracle.app.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoFileNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.SpecDecl.GoDeclNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

public class Parser {
	private final String file;
	private static BufferedReader reader;
	private static String currentLine;
	private static Matcher matchedTerm; 
	private static Pattern astPattern = Pattern.compile("\\.[a-zA-Z]+");

	public Parser(String file) throws FileNotFoundException {
		this.file = file;
		reader = new BufferedReader(new FileReader(this.file));
	}

	public void beginParse() throws IOException{
		String type;
		
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group();
				getNodeType(type.substring(1));
			}
		}
		
	}
	
/*
	public GoBasicNode parseFile(String fileName) throws FileNotFoundException, IOException {
		GoBasicNode root = new GoBasicNode("root");
		GoBasicNode tracker = root;
		// has to begin with a letter or number, can't end with { 
		Pattern pattern = Pattern.compile("[\\.][a-zA-Z]+");
		Pattern attr    = Pattern.compile("[a-zA-Z][.]*");
		Matcher matched;
		int bindex;
		int cindex;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	//System.out.println(line);
		    	if(line.indexOf('}') != -1) {
		    		tracker = tracker.parent;
		    		//System.out.println("...." + tracker.name);
		    	}
		    	else if(line.indexOf('{') >= 0) {
		    		matched = pattern.matcher(line);
		    		matched.find();
		    		bindex = matched.start();
		    		
		    		//It works
		    		String nodeType = line.substring(bindex+1, line.length()-2);
		    		if(nodeType.contains("(len")) {
		    			nodeType = nodeType.substring(0, nodeType.indexOf("(") - 1);
		    		}
		    		
		    		//getNodeType(nodeType);
		    		GoBasicNode child = new GoBasicNode(nodeType);
		    		
		    		tracker.trufflechildren(child);
		    		cindex = tracker.addChildren(child);
		    		//System.out.println("******"+tracker.name + " ||| " + child.name);
		    		tracker = child;
		    		
		    	}
		    	else {
		    		matched = attr.matcher(line);
		    		matched.find();
		    		bindex = matched.start();
		    		tracker.addData(line.substring(bindex,line.length()));
		    		//System.out.println("attrs1: " + line.substring(bindex,line.length()));
		    		
		    	}
		    }
		}
		     
			return root;
		}
*/
		
		
	
	//written by Petar, we need this owrking asap, im not sorry.
	//TO-DO: ADD A FACTORY INSTEAD
	public static Node getNodeType(String nodeType) throws IOException{
		String type;
		switch(nodeType) {
			case "File":
				while((currentLine = reader.readLine()) != null){
					matchedTerm = astPattern.matcher(currentLine);
					type = matchedTerm.group();
					if(type.equals(".Decl")){
						GoDeclNode bodyNode = (GoDeclNode) getNodeType(type.substring(1));
						return new GoFileNode(bodyNode);
					}
				}
				System.out.println(nodeType);
				break;
			case "Ident":
				System.out.println(nodeType);
				break;
			case "Decl":
				while((currentLine = reader.readLine()) != null){
					matchedTerm = astPattern.matcher(currentLine);
					type = matchedTerm.group();
					//Find out how to add multiple children when you dont know them yet...
					GoDeclNode node = new GoDeclNode();
					getNodeType(type);
				}
				System.out.println(nodeType);
				break;
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
				System.out.println(nodeType);
				break;
			case "Object":
				System.out.println(nodeType);
				break;
			case "FuncType":
				System.out.println(nodeType);
				break;
			case "BlockStmt":
				System.out.println(nodeType);
				break;
			case "Stmt":
				System.out.println(nodeType);
				break;
			case "ExprStmt":
				System.out.println(nodeType);
				break;
			case "CallExpr":
				System.out.println(nodeType);
				break;
			case "SelectorExpr":
				System.out.println(nodeType);
				break;
			case "Expr":
				System.out.println(nodeType);
				break;
			case "GenDecl":
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
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source){
		Map<String, GoRootNode> function = new HashMap<>();
		function.put("main", new GoRootNode(language,null,null,null,"main"));
		return function;
	}
	

}
