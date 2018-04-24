package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRIntNode extends GoIRBasicLitNode{
		
		int value;
		int valuelen;
		String source;
		
		public GoIRIntNode(String value, String source) {
			super("IR Int Node");
			this.value = Integer.parseInt(value);
			valuelen = value.length();
			this.type = "INT";
			this.source = source;
		}
		
		public GoIRIntNode(int value,String source){
			super("IR Int Node");
			this.value = value;
			valuelen = 1;
			this.source = source;
		}
		
		public int getValue(){
			return value;
		}

		@Override
		public Object accept(GoIRVisitor visitor) {
			return visitor.visitIRIntNode(this);
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
			return Integer.toString(value);
		}
		
	}