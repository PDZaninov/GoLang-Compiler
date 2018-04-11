package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRFloat32Node extends GoIRBasicLitNode{

    private float value;

    public GoIRFloat32Node(String value) {
    	super("Float32 Node");
        this.value = Float.valueOf(value);
        this.type = "FLOAT32";
    }

    public GoIRFloat32Node(float value){
    	super("Float32 Node");
        this.value = value;
    }

    public float getValue(){
        return value;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIRFloat32Node(this);
    }

	@Override
	public SourceSection getSource(Source section) {
		// TODO Auto-generated method stub
		return null;
	}

}