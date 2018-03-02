package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RepeatingNode;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.source.SourceSection;

public class GoForBreakNode extends Node implements RepeatingNode {
	
    @Child private GoStatementNode bodyNode;


    private final BranchProfile continueTaken = BranchProfile.create();
    private final BranchProfile breakTaken = BranchProfile.create();

    private SourceSection sourceSection;

    public GoForBreakNode(GoStatementNode bodyNode) {
        this.bodyNode = bodyNode;
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    public void setSourceSection(SourceSection section) {
        assert this.sourceSection == null : "overwriting existing SourceSection";
        this.sourceSection = section;
    }

    @Override
    public boolean executeRepeating(VirtualFrame frame) {

        try {
            bodyNode.executeVoid(frame);
            return true;

        } catch (GoContinueException ex) {
            continueTaken.enter();
            
            return true;

        } catch (GoBreakException ex) {
            breakTaken.enter();
            
            return false;
        }
    }

    @Override
    public String toString() {
        return GoStatementNode.formatSourceSection(this);
    }
}
