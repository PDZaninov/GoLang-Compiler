package com.oracle.app.nodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node.Children;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * A statement node that just executes a list of other statements.
 */
@NodeInfo(shortName = "Expr", description = "The node implementing a source code Expr")
public final class GoExprNode extends GoExpressionNode {

    /**
     * The array of child nodes. The annotation {@link com.oracle.truffle.api.nodes.Node.Children
     * Children} informs Truffle that the field contains multiple children. It is a Truffle
     * requirement that the field is {@code final} and an array of nodes.
     */
    @Child private GoExpressionNode bodyNode;

    public GoExprNode(GoExpressionNode bodyNode) {
        this.bodyNode = bodyNode;
    }

    /**
     * Execute all child statements. The annotation {@link ExplodeLoop} triggers full unrolling of
     * the loop during compilation. This allows the {@link GoStatementNode#executeVoid} method of
     * all children to be inlined.
     */
    
    @Override
    @ExplodeLoop
    public void executeVoid(VirtualFrame frame) {
        /*
         * This assertion illustrates that the array length is really a constant during compilation.
         */
        bodyNode.executeVoid(frame);
    }
    
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		
		return bodyNode.executeGeneric(frame);
	}
	
}