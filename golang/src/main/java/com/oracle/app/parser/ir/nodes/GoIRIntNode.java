package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRIntNode extends GoIRBasicLitNode{
		
		private int value;
		private int valuelen;
		
		public GoIRIntNode(String value, String source) {
			super(source);
			this.value = Integer.parseInt(value);
			valuelen = value.length();
			this.type = "INT";
		}
		
		public GoIRIntNode(int value,String source){
			super(source);
			this.value = value;
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
			int linenum = Integer.parseInt(split[0]);
			int charindex = Integer.parseInt(split[1]);
			return section.createSection(linenum,charindex,valuelen);
		}
		
	}