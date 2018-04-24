package com.oracle.app.builtins;

import java.io.PrintWriter;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "println")
public abstract class GoPrintlnBuiltin extends GoBuiltinNode {

	@Specialization
    public int println(int value) {
		System.out.println("lol");
        System.out.println(value);
        return value;
    }
	
	@TruffleBoundary
	private static void doPrint(PrintWriter out, int value){
		out.println(value);
	}
	
	@Specialization
    public long println(long value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }

    @Specialization
    public double println(double value) {
		System.out.println("double");
        System.out.println(value);
        return value;
    }
    
    @Specialization
    public float println(float value) {
		System.out.println("float");
    	
    	System.out.printf("%e\n", value);
        return value;
    }
    
    
    @TruffleBoundary
    private static void doPrint(PrintWriter out, long value) {
        out.println(value);
    }

    @Specialization
    public boolean println(boolean value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(PrintWriter out, boolean value) {
        out.println(value);
    }

    @Specialization
    public String println(String value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(PrintWriter out, String value) {
        out.println(value);
    }

    @Specialization
    public Object println(Object value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }

    @TruffleBoundary
    private static void doPrint(PrintWriter out, Object value) {
        out.println(value);
    }
   
	@Override
	public String toString() {
		return "Println Builtin";
	} 
	
}
