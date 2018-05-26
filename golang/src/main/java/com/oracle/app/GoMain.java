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
    	PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(in).setOut(out).build();
    	assert engine.getLanguages().containsKey(GoLanguage.MIME_TYPE);
    	try {
    		Value result = engine.eval(source);
    		
    		if(result == null) {
    			//Throw error GoException
    		}
    		else if (result.get() != GoNull.SINGLETON ) {
    			//This thing here is actually the reason why we get that last null LOL
    			//out.println(result.get());
    		}
    	}
    	//All errors end up here
    	catch(GoException e){
    		
    	}
    	catch (Throwable ex){

    		ex.printStackTrace(out);
    	}
    	
    	engine.dispose();
    }
}
