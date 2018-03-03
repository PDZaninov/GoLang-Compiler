package com.oracle.app.nodes.controlflow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node.Children;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * A statement node that just executes a list of other statements.
 */
@NodeInfo(shortName = "block", description = "The node implementing a source code block")
public final class GoBlockNode extends GoStatementNode {

    /**
     * The array of child nodes. The annotation {@link com.oracle.truffle.api.nodes.Node.Children
     * Children} informs Truffle that the field contains multiple children. It is a Truffle
     * requirement that the field is {@code final} and an array of nodes.
     */
    @Children private final GoStatementNode[] bodyNodes;

    public GoBlockNode(GoStatementNode[] bodyNodes) {
        this.bodyNodes = bodyNodes;
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
        CompilerAsserts.compilationConstant(bodyNodes.length);

        for (GoStatementNode statement : bodyNodes) {
            statement.executeVoid(frame);
        }
    }

    /**
     * This executeVoid function is called by switch statment.
     * Goes through each GoCaseClauseNode by executing its generic function.
     * If the case returns true it means that it has already exectued its body function and we break.
     *
     * @param frame: Virtual Frame
     * @param value: Value of the tag passed in from Switch statement.
     */
    public void executVoid(VirtualFrame frame, Object value) {
        for (GoStatementNode case : bodyNodes){
            if (case.executeVoid(frame, value)){
                break;
            }
        }
    }

    public List<GoStatementNode> getStatements() {
        return Collections.unmodifiableList(Arrays.asList(bodyNodes));
    }

    /*
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		for (GoStatementNode statement : bodyNodes) {
			if(statement != null){
				statement.executeVoid(frame);
			}
        }
		return null;
	}
	*/
}