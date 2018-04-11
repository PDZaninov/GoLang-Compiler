package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFloat64Node extends GoIRBasicLitNode{

    private double value;

    public GoIRFloat64Node(String value) {
        this.value = Double.valueOf(value);
        this.type = "FLOAT64";
    }

    public GoIRFloat64Node(double value){
        this.value = value;
    }

    public double getValue(){
        return value;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIRFloat64Node(this);
    }

}