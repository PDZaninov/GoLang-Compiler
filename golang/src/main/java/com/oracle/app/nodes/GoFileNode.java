package com.oracle.app.nodes;

import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.SpecDecl.GoImportSpec;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;

/*
 * File node is at the top of every Go program and contains the info for
 * the entire code in that file
 */
@NodeInfo(language = "Go", description = "The base node for every Go file")
public class GoFileNode extends RootNode{
	@Child private GoArrayExprNode bodyNode; 
	@Child private GoImportSpec imports;
	private GoLanguage language;
	private Map<String,GoRootNode> allfunctions;
	private GoRootNode mainFunction;
	private String name;
	private final ContextReference<GoContext> reference;
	
	public GoFileNode(GoLanguage language, FrameDescriptor frameDescriptor, GoArrayExprNode bodyNode,
			GoImportSpec imports,GoRootNode mainFunction, Map<String,GoRootNode> allfunctions, String name){
		super(language,frameDescriptor);
		this.bodyNode = bodyNode;
		this.language = language;
		this.imports = imports;
		this.allfunctions = allfunctions;
		this.name = name;
		this.mainFunction = mainFunction;
		this.reference = language.getContextReference();
	}
	
	public String getFileName(){
		return name;
	}
	
	public void executeVoid(VirtualFrame frame){
		bodyNode.executeVoid(frame);
	}

	@Override
	public Object execute(VirtualFrame frame) {
		//Register functions and global variables
		//Then execute global declarations
		CompilerDirectives.transferToInterpreterAndInvalidate();
        reference.get().getFunctionRegistry().register(allfunctions);
        FrameDescriptor f = getFrameDescriptor();
        FrameSlot slot;
        slot = f.findFrameSlot("int");
        slot.setKind(FrameSlotKind.Int);
        frame.setInt(slot, 0);
        slot = f.findFrameSlot("float64");
        frame.setDouble(slot, 0);
        slot = f.findFrameSlot("float32");
        frame.setFloat(slot, 0);
        slot = f.findFrameSlot("string");
        frame.setObject(slot, "");
        slot = f.findFrameSlot("bool");
        frame.setBoolean(slot, false);
        slot = f.findFrameSlot("true");
        frame.setBoolean(slot,true);
        slot = f.findFrameSlot("false");
        frame.setBoolean(slot, false);
        
        for(GoExpressionNode decl : bodyNode.getArguments()){
        	if(decl != null){
        		decl.executeGeneric(frame);
        	}
        }
        
		mainFunction = new GoEvalRootNode(language, mainFunction.getFrameDescriptor(), mainFunction.getBodyNode(), mainFunction.getSourceSection(), mainFunction.getName());
		return mainFunction.execute(frame);
	}
	
}
