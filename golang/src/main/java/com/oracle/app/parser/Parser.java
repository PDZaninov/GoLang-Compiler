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
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.Source;

public class Parser {

	public static class GoBasicNode extends GoExpressionNode{
		String name;
		String[] attr = new String[25];
		GoBasicNode parent;
		public GoBasicNode[] children = new GoBasicNode[7];
		
		public GoBasicNode(String named) {
			name = named;
			
		}
		
		
		public void executeVoid(VirtualFrame frame){
			printSelf(0);
			System.out.println("Void\n");
		}
		
		public void addData(String someData) {
			for(int i = 0; i < attr.length; i++) {
				if(attr[i] == null) {
					attr[i] = someData;
					break;
				}
			}
		}
		public GoBasicNode setParent(GoBasicNode someNode) {
			parent = someNode;
			return parent;
		}

		public int addChildren(GoBasicNode theChosenOne) {
			int i;
			for(i = 0; i < children.length;i++) {
				if(children[i]== null) {
					children[i] = theChosenOne;
					break;
				}

			}
			theChosenOne.setParent(this);
			return i;
		}
		
		public void printSelf(int spacing) {
			//System.out.print(spacing);
			Spacing(spacing);
			
			System.out.println("Name: " + name);
			if(name != "root") {
				Spacing(spacing);
				System.out.println("Parent: " + parent.name);
			}
/*			for(int x = 0; x < children.length; x++) {
				if(children[x] == null)
					break;
				Spacing(spacing);
				System.out.println("Children: " + children[x].name);
			}*/
			for(int x = 0; x < attr.length; x++) {
				if(attr[x] == null)
					break;
				Spacing(spacing);
				System.out.println("Attr: " + attr[x]);
			}
		}
		public void Spacing(int spacing) {
			for(int x = 0; x < spacing; x++) {
				System.out.print(" . ");
			}
		}
		
		public void printTree(GoBasicNode root, int spacing) {
			root.printSelf(spacing);
			spacing += 1;
			for(int x = 0; x < root.children.length; x++) {
				if(root.children[x] != null) {
					root.printTree(root.children[x], spacing);
					
				}else {
					break;
				}
			}
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
			    		GoBasicNode child = new GoBasicNode(line.substring(bindex, line.length()-1));
			    		
			    		
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


		@Override
		public Object executeGeneric(VirtualFrame frame) {
			// TODO Auto-generated method stub
			printSelf(0);
			System.out.println("Generic\n");
			return null;
		}
		
		
	}
	//written by Petar, we need this owrking asap, im not sorry.
	//TO-DO: ADD A FACTORY INSTEAD
	public static void getNodeType(String nodeType) {
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

	}
	
	public static Map<String, GoRootNode> parseGo(GoLanguage language, Source source){
		Map<String, GoRootNode> function = new HashMap<>();
		function.put("main", new GoRootNode(language,null,null,null,"main"));
		return function;
	}
	

}
