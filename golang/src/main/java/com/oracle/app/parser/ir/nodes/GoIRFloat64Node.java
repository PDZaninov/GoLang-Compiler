package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRFloat64Node extends GoIRBasicLitNode{

    private double value;
    private int valuelen;
    
    public GoIRFloat64Node(String value, String source) {
    	super(source, "FLOAT64", value);
    	this.valuelen = value.length();
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
		String[] split = source.split(":");
		int linenum = Integer.parseInt(split[1]);
		int charindex = Integer.parseInt(split[2]);
		return section.createSection(linenum,charindex,valuelen);
	}
	
	@Override
	public String getValString() {
		// TODO Auto-generated method stub
		return Double.toString(value);
	}

}