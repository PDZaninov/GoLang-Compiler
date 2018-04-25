package com.oracle.app.builtins.fmt;

import java.math.BigInteger;

import com.oracle.app.builtins.GoBuiltinNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
 
@NodeInfo(shortName = "Println")
public abstract class GoFmtPrintln extends GoBuiltinNode {

    @Specialization
    public long Println(long value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public float Println(float value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public int Println(int value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public BigInteger Println(BigInteger value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public String Println(String value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public boolean Println(boolean value) {
        System.out.println(value);
        return value;
    }

    @Specialization
    public Object Println(Object value) {
        System.out.println(value);
        return value;
    }


    @Override
    public String toString() {
        return "Fmt Println";
    }

}