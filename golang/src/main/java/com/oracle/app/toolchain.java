package com.oracle.app;

import java.io.*;

public class toolchain {

    public static void executeCommands(String FILE_NAME, String GO_DIRECTORY, String AST_DIRECTORY) throws IOException, InterruptedException {

        File tempScript = createTempScript(FILE_NAME,GO_DIRECTORY,AST_DIRECTORY);

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();
        } finally {
            tempScript.delete();
        }
    }

    public static File createTempScript(String FILE_NAME, String GO_DIRECTORY, String AST_DIRECTORY) throws IOException {
        String currentDir = System.getProperty("user.dir");
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("go build " + currentDir + "/" + "printAST.go");
        printWriter.println("./printAST " + GO_DIRECTORY + FILE_NAME + ".go" + " > " + AST_DIRECTORY + FILE_NAME + ".ast");
//        printWriter.println("rm printAST");
//        printWriter.println("javac "+ currentDir + "/src/main/java/com/oracle/app/toolchain.java");
//        System.out.println("Run this");
//        System.out.println("java -classpath " + currentDir + "/src/main/java" + " com.oracle.app.toolchain " + FILE_NAME +".go" );
        System.out.println("chmod +x "+ AST_DIRECTORY + "gt");
        printWriter.println("chmod +x "+ AST_DIRECTORY + "gt");
        printWriter.println("sudo -S true");
        System.out.println("sudo "+ "."+ AST_DIRECTORY + "gt " + AST_DIRECTORY + FILE_NAME + ".ast");
        printWriter.println("sudo "+ "."+ AST_DIRECTORY + "gt " + AST_DIRECTORY + FILE_NAME + ".ast");
        printWriter.close();

        return tempScript;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);
        String GO_DIRECTORY = currentDir + "/gofiles/"; //TODO: fix the directory
        String AST_DIRECTORY = currentDir + "/astfiles/"; //TODO: fix the directory
//		File folder = new File("your/path");
//		File[] listOfFiles = folder.listFiles();

        String filename = args[0];
        filename=filename.substring(0, filename.lastIndexOf('.'));
//        String filename = "HelloGo";
        executeCommands(filename, GO_DIRECTORY, AST_DIRECTORY);
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				String filename = listOfFiles[i].getName();
//				System.out.println("File " + filename);
//				createTempScript(filename,GO_DIRECTORY,AST_DIRECTORY);
//			} else if (listOfFiles[i].isDirectory()) {
//				System.out.println("There shouldnt be a directory in here");
//				System.exit(0);
//			}
//		}

    }
}
