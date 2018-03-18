package com.oracle.app;

import java.io.*;

/**
 * Runs the GoLang compiler frontend and spits out the ast tree for use on the backend
 *
 */
public class ToolChain {

    public static void executeCommands(String goFile) throws IOException, InterruptedException {

    	String astFile = goFile.substring(0, goFile.lastIndexOf('.')) + ".ast";
        File tempScript = createTempScript(goFile, astFile);

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } finally {
            tempScript.delete();
        }
        
    }

    public static File createTempScript(String goFile, String astFile) throws IOException {

        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);
        //Assumed to have the printast file
        printWriter.println("#!/bin/bash");
        //System.out.println("go build " + "printAST.go");
        printWriter.println("go build " + "printast.go");
        //System.out.println("./printast "+ goFile + " > " + astFile);
        printWriter.println("./printast "+ goFile + " > " + astFile);
        //printWriter.println("rm printast");

        printWriter.close();

        return tempScript;
    }

}
