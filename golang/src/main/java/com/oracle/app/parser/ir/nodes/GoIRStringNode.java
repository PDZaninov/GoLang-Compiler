package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoIRVisitor;
import com.oracle.app.parser.ir.StringEscape;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public class GoIRStringNode extends GoIRBasicLitNode{
		private String value;
		public GoIRStringNode(String value, String source) {
			super(source);
			if(value.length() > 2){
				value = value.substring(2, value.length()-2);
				value = value.replace("\\\\", "\\");
			
				value = StringEscape.unescape_perl_string(value);
			}
			this.value = value;
			this.type = "STRING";
		}

		public String getValue(){
			return value;
		}
		
		@Override
		public Object accept(GoIRVisitor visitor) {
			return visitor.visitIRStringNode(this);
		}

		@Override
		public SourceSection getSource(Source section) {
			String[] split = source.split(":");
			int linenum = Integer.parseInt(split[1]);
			int charindex = Integer.parseInt(split[2]);
			return section.createSection(linenum,charindex,value.length());
		}

		@Override
		public String getValString() {
			// TODO Auto-generated method stub
			System.out.println("String val:" + value);
			return value;
		}
		
}