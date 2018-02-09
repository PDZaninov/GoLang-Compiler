package com.oracle.app.builtins;

import java.io.PrintWriter;
import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "println")
public abstract class GoPrintlnBuiltin extends GoBuiltinNode {

	@Specialization
    public long println(long value) {
        doPrint(getContext().getOutput(), value);
        return value;
    }
	
	@TruffleBoundary
    private static void doPrint(PrintWriter out, long value) {
        out.println(value);
    }

	@Specialization
	public float println(float value) {
	    doPrint(getContext().getOutput(), value);
	    return value;
	}
	
	@TruffleBoundary
	private static void doPrint(PrintWriter out, float value){
		out.println(value);
	}
	    
	@Specialization
	public int println(int value) {
	    doPrint(getContext().getOutput(), value);
	    return value;
	}

	@TruffleBoundary
	private static void doPrint(PrintWriter out, int value){
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
}