package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRFloat64Node extends GoIRBasicLitNode{

    private double value;

    public GoIRFloat64Node(String value) {
    	super("Float64 Node", "FLOAT64", value);
        this.value = Double.valueOf(value);
        this.type = "FLOAT64";
    }

    public GoIRFloat64Node(double value){
    	super("Float64 Node", "FLOAT64", Double.toString(value));
        this.value = value;
    }

    public double getValue(){
        return value;
    }

    @Override
    public Object accept(GoIRVisitor visitor) {
        return visitor.visitIRFloat64Node(this);
    }

	@Override
	public SourceSection getSource(Source section) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValString() {
		// TODO Auto-generated method stub
		return Double.toString(value);
	}

}