package com.oracle.app.nodes.expression;
import java.math.BigInteger;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.GoTypes;


@NodeInfo(shortName = "+")
public abstract class GoAddNode extends GoBinaryNode {


    @Specialization(rewriteOn = ArithmeticException.class)
    protected int add(int left, int right) {
        return Math.addExact(left, right);
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected long add(long left, long right) {
        return Math.addExact(left, right);
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float add(float left, float right) {
        return left + right;
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Specialization(guards = "isString(left, right)")
    @TruffleBoundary
    protected String add(Object left, Object right) {
        return left.toString() + right.toString();
    }

    protected boolean isString(Object a, Object b) {
        return a instanceof String || b instanceof String;
    }
}