package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIntNode extends GoIRBasicLitNode{
		
		private int value;
		
		public GoIRIntNode(String value, String source) {
			super(source);
			this.value = Integer.parseInt(value);
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
		
	}