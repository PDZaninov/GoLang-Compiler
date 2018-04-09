package com.oracle.app;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotEngine.Value;


public class GoMain 
{
    public static void main(String[] args) throws IOException, InterruptedException {


//		tool.run(args[0]);
     	Source source;
        if(args.length == 0){
        	System.out.println("Don't know about standard input quite yet");
        	source = Source.newBuilder(new InputStreamReader(System.in)).
                    name("<stdin>").
                    mimeType(GoLanguage.MIME_TYPE).
                    build();
        }
        else{
        	
        	String goFile  = args[0];
        	ToolChain.executeCommands(goFile);
        	source = Source.newBuilder(new File(goFile)).build();
        }
        executeSource(source, System.in, System.out);

    }

  	private static void executeSource(Source source, InputStream in, PrintStream out){
  		out.println("== running on " + Truffle.getRuntime().getName());
    	PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
    	//Don't know about this yet
    	assert engine.getLanguages().containsKey(GoLanguage.MIME_TYPE);
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
    		//Other error catching stuff, refer to GoMain....

    		ex.printStackTrace(out);
    	}
    	
    	engine.dispose();
    }
}
