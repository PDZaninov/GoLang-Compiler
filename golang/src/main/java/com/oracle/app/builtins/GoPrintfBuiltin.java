package com.oracle.app.builtins;

import java.math.BigInteger;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
 
@NodeInfo(shortName = "printf")
public abstract class GoPrintfBuiltin extends GoBuiltinNode {

    @Specialization
    public long printf(long value) {
        System.out.print(value);
        return value;
    }

    @Specialization
    public float printf(float value) {
        System.out.print(value);
        return value;
    }

    @Specialization
    public double printf(double value) {
        System.out.print(value);
        return value;
    }
    
    @Specialization
    public int printf(int value) {
        System.out.print(value);
        return value;
    }
    
    @Specialization
    public BigInteger printf(BigInteger value) {
        System.out.print(value);
        return value;
    }
    
    @Specialization
    public String printf(String value) {
        System.out.print(value);
        return value;
    }
    
    @Specialization
    public boolean printf(boolean value) {
        System.out.print(value);
        return value;
    }

    @Specialization
    public Object printf(Object value) {
        System.out.print(value);
        return value;
    }
}
