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
        System.out.println(value);
        return value;
    }

    @Specialization
    public float println(float value) {
        System.out.println(value);
        return value;
    }
    
    @Specialization
    public int println(int value) {
        System.out.println(value);
        return value;
    }
    
    @Specialization
    public BigInteger println(BigInteger value) {
        System.out.println(value);
        return value;
    }
    
    @Specialization
    public String println(String value) {
        System.out.println(value);
        return value;
    }
    
    @Specialization
    public boolean println(boolean value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public Object println(Object value) {
        System.out.println(value);
        return value;
    }
}