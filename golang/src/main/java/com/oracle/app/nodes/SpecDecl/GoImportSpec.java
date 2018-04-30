package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.GoLanguage;
import com.oracle.app.builtins.fmt.FmtFunctionList;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Load the built in library when executed
 * Will currently not handle user defined file imports
 * @author Trevor
 *
 */
public class GoImportSpec extends GoExpressionNode {
	
	@Child private GoStringNode child;
	private final GoLanguage language;
	private final FrameSlot slot;
	
	public GoImportSpec(GoStringNode child, GoLanguage language, FrameSlot slot) {
		this.child = child;
		this.language = language;
		this.slot = slot;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		String name = (String) child.executeGeneric(frame);
		switch(name) {
        case "fmt":
        	frame.setObject(slot, new FmtFunctionList(language));
            break;
        default:
            System.out.println("Package not yet done");
		}
		return null;
	}
}