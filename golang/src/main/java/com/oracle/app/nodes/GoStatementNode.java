package com.oracle.app.nodes;

import java.io.File;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.Instrumentable;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(language = "Go", description = "The abstract base node for all SL statements")
@Instrumentable(factory = GoStatementNodeWrapper.class)
public abstract class GoStatementNode extends Node {

    private SourceSection sourceSection;

    private boolean hasStatementTag;
    private boolean hasRootTag;

    @Override
    public final SourceSection getSourceSection() {
        return sourceSection;
    }

    public void setSourceSection(SourceSection section) {
        //assert this.sourceSection == null : "overwriting existing SourceSection";
        this.sourceSection = section;
    }

    /**
     * Execute this node as as statement, where no return value is necessary.
     */
    public abstract void executeVoid(VirtualFrame frame);

    /**
     * Marks this node as being a {@link StandardTags.StatementTag} for instrumentation purposes.
     */
    public final void addStatementTag() {
        hasStatementTag = true;
    }

    /**
     * Marks this node as being a {@link StandardTags.RootTag} for instrumentation purposes.
     */
    public final void addRootTag() {
        hasRootTag = true;
    }

    @Override
    protected boolean isTaggedWith(Class<?> tag) {
        if (tag == StandardTags.StatementTag.class) {
            return hasStatementTag;
        } else if (tag == StandardTags.RootTag.class) {
            return hasRootTag;
        }
        return false;
    }

    @Override
    public String toString() {
        return formatSourceSection(this);
    }

    /**
     * Formats a source section of a node in human readable form. If no source section could be
     * found it looks up the parent hierarchy until it finds a source section. Nodes where this was
     * required append a <code>'~'</code> at the end.
     *
     * @param node the node to format.
     * @return a formatted source section string
     */
    public static String formatSourceSection(Node node) {
        if (node == null) {
            return "<unknown>";
        }
        SourceSection section = node.getSourceSection();
        boolean estimated = false;
        if (section == null) {
            section = node.getEncapsulatingSourceSection();
            estimated = true;
        }

        if (section == null || section.getSource() == null) {
            return "<unknown source>";
        } else {
            String sourceName = new File(section.getSource().getName()).getName();
            int startLine = section.getStartLine();
            return String.format("%s:%d%s", sourceName, startLine, estimated ? "~" : "");
        }
    }

}