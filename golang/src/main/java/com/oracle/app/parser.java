package com.oracle.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parser {

	public static class Bnode{
		private String name;
		private String[] attr = new String[25];
		private Bnode parent;
		private Bnode[] children = new Bnode[7];
		
		public Bnode(String named) {
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
		public Bnode setParent(Bnode someNode) {
			parent = someNode;
			return parent;
		}

		public int addChildren(Bnode theChosenOne) {
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
		
		public void printTree(Bnode root, int spacing) {
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
		
		public Bnode parseFile(String fileName) throws FileNotFoundException, IOException {
			Bnode root = new Bnode("root");
			Bnode tracker = root;
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
			    		Bnode child = new Bnode(line.substring(bindex, line.length()-1));
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


	
	public static void main(String[] args) {
		System.out.println("---------------------------------------------");
		try {
			Bnode root = new Bnode("root");
			root = root.parseFile("HelloGo.ast");
			root.printSelf(0);
			root.children[0].printSelf(1);
			root.children[0].children[0].printSelf(2);
			root.children[0].children[1].printSelf(2);
			root.children[0].children[1].children[0].printSelf(3);
			root.children[0].children[1].children[1].printSelf(3);
			root.children[0].children[2].printSelf(2);
			root.children[0].children[3].printSelf(2);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			root.printTree(root, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
