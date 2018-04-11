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
	String lbrack;
	String rbrack;
	
	
	public GoIRWriteIndexNode(GoIRIdentNode name, GoBaseIRNode index, String lbrack,String rbrack) {
		super("Write Index");
		this.name = name;
		this.index = index;
		this.lbrack = lbrack;
		this.rbrack = rbrack;
	}

	public static GoIRWriteIndexNode createIRWriteIndex(GoIRIndexNode node){
		return new GoIRWriteIndexNode(node.name,node.index,node.lbrack,node.rbrack);
	}
	
	public GoIRIdentNode getName(){
		return name;
	}
	
	public int getLineNumber(){
		return Integer.parseInt(rbrack.split(":")[1]);
	}
	
	public int getRBrack(){
		return Integer.parseInt(rbrack.split(":")[2]);
	}
	
	public int getLBrack(){
		return Integer.parseInt(lbrack.split(":")[2]);
	}
	
	public int getSourceSize(){
		int start = Integer.parseInt(lbrack.split(":")[2]);
		int end = Integer.parseInt(rbrack.split(":")[2]);
		return end - start;
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
