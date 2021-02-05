package project;

import java.util.SortedMap;
import java.util.TreeMap;

public class SecondVisitor implements Visitor {
    private final SortedMap<Integer, FollowPosTableEntry> followPosTableEntries = new TreeMap<>();

    @Override
    public void visit(OperandNode node) {
        FollowPosTableEntry followPosTableEntry = new FollowPosTableEntry(node.position, node.symbol);
        followPosTableEntries.put(node.position, followPosTableEntry);
    }

    @Override
    public void visit(BinOpNode node) {
        switch (node.operator) {
            case "°":
                SyntaxNode syntaxNodeLeft = (SyntaxNode) node.left;
                syntaxNodeLeft.lastpos.forEach(i -> {
                    SyntaxNode syntaxNodeRight = (SyntaxNode) node.right;
                    followPosTableEntries.get(i).followpos.addAll(syntaxNodeRight.firstpos);
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + node.operator);
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        switch (node.operator) {
            case "*":
            case "+":
                SyntaxNode syntaxNode = (SyntaxNode) node.subNode;
                syntaxNode.lastpos.forEach(i -> followPosTableEntries.get(i).followpos.addAll(syntaxNode.firstpos));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + node.operator);
        }
    }

    public SortedMap<Integer, FollowPosTableEntry> getFollowPosTableEntries() {
        return this.followPosTableEntries;
    }
}
