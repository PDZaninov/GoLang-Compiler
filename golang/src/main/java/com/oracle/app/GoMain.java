package com.oracle.app;

import com.oracle.app.parser;
import com.oracle.app.parser.GoBasicNode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class GoMain 
{
    public static void main(String[] args)
    {
        System.out.println( "Hello World!" );
        System.out.println("---------------------------------------------");
		try {
			GoBasicNode root = new GoBasicNode("root");
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
