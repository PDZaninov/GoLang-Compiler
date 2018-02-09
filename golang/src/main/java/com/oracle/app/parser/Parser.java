package com.oracle.app.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoBasicNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.Source;

public class Parser {
	
	private final GoNodeFactory factory;
	
	private String[] StringFile;

	Pattern pattern = Pattern.compile("[\\.][a-zA-Z]+");
	Pattern attr    = Pattern.compile("[a-zA-Z][.]*");
	Matcher matched;
	
	
	public Parser(GoLanguage language, Source source) {
		this.factory = new GoNodeFactory(language, source);
	}




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

		
		
	
	//written by Petar, we need this owrking asap, im not sorry.
	//TO-DO: ADD A FACTORY INSTEAD
	public GoStatementNode getNodeType(String nodeType,List<String> attrs ,List<GoStatementNode> body) {
		GoStatementNode result = null;
		switch(nodeType) {
			case "File":
				System.out.println(nodeType);
				break;
			case "Ident":
				System.out.println(nodeType);
				break;
			case "Decl":
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
		return result;

	}
	
	private void function() {
		
	}
	
	private GoStatementNode block() {
		
		return null;
		
	}
	
	private GoStatementNode getGoType() {
		return null;
		
	}
	
	private void Parse(String filename) {
		
		// has to begin with a letter or number, can't end with { 
		
		//GoRootNode evalMain = new GoRootNode(this,null,man,null,"main");
		
		
		Scanner sc = new Scanner(new File(filename));
		List<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
		  lines.add(sc.nextLine());
		}

		StringFile = lines.toArray(new String[0]);
		

		this.factory.put()
		
		
		
	}
	
	private GoStatementNode recParse(int lineNumber,String currNode) {
		List<GoStatementNode> body = new ArrayList<>();
		List<String> attrs = new ArrayList<>();
		String nodeName = currNode;
		int bindex;
		int cindex;
		
		if(StringFile[lineNumber].indexOf('}') != -1) {
    		//System.out.println("...." + tracker.name);
    		
    		
    		//create Node here
			return getNodeType(nodeName,attrs,body);
    	}
    	else if(StringFile[lineNumber].indexOf('{') >= 0) {
    		matched = pattern.matcher(StringFile[lineNumber]);
    		matched.find();
    		bindex = matched.start();
    		
    		//It works
    		String nodeType = StringFile[lineNumber].substring(bindex+1, StringFile[lineNumber].length()-2);
    		if(nodeType.contains("(len")) {
    			nodeType = nodeType.substring(0, nodeType.indexOf("(") - 1);
    		}
    		
    		
    		body.add(recParse(lineNumber+1,nodeType));
    		
    		
    	}
    	else {
    		matched = attr.matcher(StringFile[lineNumber]);
    		matched.find();
    		bindex = matched.start();

    		attrs.add(StringFile[lineNumber].substring(bindex));
    		
    	}

		return null;
	}
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source){
		/*Map<String, GoRootNode> function = new HashMap<>();
		function.put("main", new GoRootNode(language,null,null,null,"main"));
		return function;*/
		
		Parser parser = new Parser(language, source);
        parser.Parse(source.getName());
        return parser.factory.getAllFunctions();
	}

}
