package com.oracle.app.runtime;

public class GoNull {

	public static final GoNull SINGLETON = new GoNull();
	
	private GoNull() {
	}
	
	public String toString(){
		return "nil";
	}

}
