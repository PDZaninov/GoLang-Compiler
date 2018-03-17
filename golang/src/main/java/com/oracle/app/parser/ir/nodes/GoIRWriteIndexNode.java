package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

/**
 * Class only created when an Index node appears on the left hand side of an assignment.
 * @author Trevor
 *
 */
public class GoIRWriteIndexNode extends GoBaseIRNode {

	GoIRIdentNode name;
	GoBaseIRNode index;
	
	
	public GoIRWriteIndexNode(GoIRIdentNode name, GoBaseIRNode index) {
		super("Write Index");
		this.name = name;
		this.index = index;
	}

	public static GoIRWriteIndexNode createIRWriteIndex(GoIRIndexNode node){
		return new GoIRWriteIndexNode(node.getName(),node.getIndex());
	}
	
	public GoIRIdentNode getName(){
		return name;
	}
	
	@Override
	public String getIdentifier(){
		return name.getIdentifier();
	}
	
	public GoBaseIRNode getIndex(){
		return index;
	}	

	@Override
	public Object accept(GoIRVisitor visitor) {
		// TODO Auto-generated method stub
		return visitor.visitWriteIndex(this);
	}

}
