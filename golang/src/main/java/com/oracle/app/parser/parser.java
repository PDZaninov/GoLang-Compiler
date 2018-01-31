package com.oracle.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parser {

	public static class GoBasicNode{
		String name;
		String[] attr = new String[25];
		GoBasicNode parent;
		GoBasicNode[] children = new GoBasicNode[7];
		
		public GoBasicNode(String named) {
			name = named;
			
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
			Pattern pattern = Pattern.compile("[a-zA-Z][.]*");
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
			    	else if(line.indexOf('{') != -1) {
			    		matched = pattern.matcher(line);
			    		matched.find();
			    		bindex = matched.start();
			    		GoBasicNode child = new GoBasicNode(line.substring(bindex, line.length()-1));
			    		cindex = tracker.addChildren(child);
			    		//System.out.println("******"+tracker.name + " ||| " + child.name);
			    		tracker = child;
			    	}else {
			    		matched = pattern.matcher(line);
			    		matched.find();
			    		bindex = matched.start();
			    		tracker.addData(line.substring(bindex,line.length()));
			    		//.out.println("attrs1: " + line.substring(bindex,line.length()));
			    		
			    	}
			    	
			    }
			}
			
			
		     
			return root;
		}
		
		
	}

}
