package project;


import java.util.SortedMap;
import java.util.TreeMap;

//8622410
public class SecondVisitor implements IVisitor {
    private final SortedMap<Integer, FollowPosTableEntry> followPosTableEntries = new TreeMap<>();

    @Override
    public void visit(OperandNode node) {
        FollowPosTableEntry followPosTableEntry = new FollowPosTableEntry(node.position, node.symbol);
        followPosTableEntries.put(node.position, followPosTableEntry);
    }

    @Override
    public void visit(BinOpNode node) {
        switch (node.operator) {
            case "°" -> {
                SyntaxNode syntaxNodeLeft = (SyntaxNode) node.left;
                syntaxNodeLeft.lastpos.forEach(i -> {
                    SyntaxNode syntaxNodeRight = (SyntaxNode) node.right;
                    followPosTableEntries.get(i).followpos.addAll(syntaxNodeRight.firstpos);
                });
            }

        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        switch (node.operator) {
            case "*", "+" -> {
                SyntaxNode syntaxNode = (SyntaxNode) node.subNode;
                syntaxNode.lastpos.forEach(i -> followPosTableEntries.get(i).followpos.addAll(syntaxNode.firstpos));
            }

        }
    }

    public SortedMap<Integer, FollowPosTableEntry> getFollowPosTableEntries() {
        return this.followPosTableEntries;
    }
}
