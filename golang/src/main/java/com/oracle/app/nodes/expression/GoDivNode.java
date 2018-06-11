package com.oracle.app.nodes.expression;

import java.math.BigInteger;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//Should throw a runtime GoException when dividing by 0
/*
 * package main
import "fmt"

func main() {
	x := 0
    fmt.Println(3/x)
}
 */
@NodeInfo(shortName = "/")
public abstract class GoDivNode extends GoBinaryNode {
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected int div(int left, int right) throws ArithmeticException {
        int result = left / right;

        if ((left & right & result) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return result;
    }
    
    @Specialization(rewriteOn = ArithmeticException.class)
    protected float div(float left, float right) throws ArithmeticException {
        float result = left / right;
        
        /*//TODO
        if ((left & right & result) < 0) {
            throw new ArithmeticException("float overflow");
        }*/
        return result;
    }

    @Specialization(rewriteOn = ArithmeticException.class)
    protected double div(double left, double right) throws ArithmeticException {
        double result = left / right;

        /*//TODO
        if ((left & right & result) < 0) {
            throw new ArithmeticException("float overflow");
        }*/
        return result;
    }

    @Specialization
    @TruffleBoundary
    protected BigInteger div(BigInteger left, BigInteger right) {
        return left.divide(right);
    }
}