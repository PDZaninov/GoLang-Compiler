package com.oracle.app;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.oracle.app.parser;
import com.oracle.app.parser.GoBasicNode;
import com.oracle.runtime.GoNull;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotEngine.Value;

/**
 * Hello world!
 *
 */
public class GoMain 
{
    public static void main(String[] args)
    {
      Source source;
        if(args.length == 0){
        	System.out.println("Don't know about standard input quite yet");
        	source = Source.newBuilder(new InputStreamReader(System.in)).
                    name("<stdin>").
                    mimeType(GoLanguage.MIME_TYPE).
                    build();
        }
        else{
        	source = Source.newBuilder(new File(args[0])).build();
        }
        
        executeSource(source, System.in, System.out);
      
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
  private static void executeSource(Source source, InputStream in, PrintStream out){
    	PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
    	//Don't know about this yet
    	//assert engine.getLanguages().containsKey(GoLanguage.MIME_TYPE);
    	
    	try {
    		Value result = engine.eval(source);
    		
    		if(result == null) {
    			//Throw error GoException
    		}
    		else if (result.get() != GoNull.SINGLETON ) {
    			out.println(result.get());
    		}
    	}
    	//A Parse error goes here
    	catch (Throwable ex){
    		//Other error catching stuff, refer to SLMain....
    	}
    	
    	engine.dispose();
    }
}
