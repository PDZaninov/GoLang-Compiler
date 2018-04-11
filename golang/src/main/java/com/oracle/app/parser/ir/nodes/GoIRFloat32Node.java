package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRFloat32Node extends GoIRBasicLitNode{

    private float value;

    public GoIRFloat32Node(String value) {
        this.value = Float.valueOf(value);
        this.type = "FLOAT32";
    }

    public GoIRFloat32Node(float value){
        this.value = value;
    }

    public float getValue(){
        return value;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIRFloat32Node(this);
    }

}