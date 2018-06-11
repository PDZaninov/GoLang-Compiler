package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRFloat32Node extends GoIRBasicLitNode{

    private float value;
    private int valuelen;
    
    public GoIRFloat32Node(String value, String source) {
    	super(source, "FLOAT32", value);
    	this.valuelen = value.length();
        this.value = Float.valueOf(value);
        this.type = "FLOAT32";
    }

    public GoIRFloat32Node(float value){
    	super("Float32 Node", "FLOAT32", Float.toString(value));
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
		String[] split = source.split(":");
		int linenum = Integer.parseInt(split[1]);
		int charindex = Integer.parseInt(split[2]);
		return section.createSection(linenum,charindex,valuelen);
	}

	@Override
	public String getValString() {
		// TODO Auto-generated method stub
		return Float.toString(value);
	}

}