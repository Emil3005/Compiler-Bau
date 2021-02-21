import org.junit.jupiter.api.Test;
import project.*;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecondVisTest {

        @Test
        public void test () {
            IVisitable syntaxTree = createSyntaxTree();
            SortedMap<Integer, FollowPosTableEntry> followPosTable = createFollowPosTable();
            SecondVisitor visitor = new SecondVisitor();
            DepthFirstIterator.traverse(syntaxTree, visitor);
            assertEquals(followPosTable, visitor.getFollowPosTableEntries());
        }

        private IVisitable createSyntaxTree() {
            OperandNode left1 = new OperandNode("a");
            left1.firstpos.add(1);
            left1.lastpos.add(1);
            left1.position = 1;

            OperandNode right = new OperandNode("b");
            right.firstpos.add(2);
            right.lastpos.add(2);
            right.position = 2;

            SyntaxNode left2 = new BinOpNode("|", left1, right);
            left2.firstpos.addAll(Arrays.asList(1, 2));
            left2.lastpos.addAll(Arrays.asList(1, 2));

            left2 = new UnaryOpNode("*", (IVisitable) left2);
            left2.firstpos.addAll(Arrays.asList(1, 2));
            left2.lastpos.addAll(Arrays.asList(1, 2));

            right = new OperandNode("a");
            right.firstpos.add(3);
            right.lastpos.add(3);
            right.position = 3;

            left2 = new BinOpNode("°", (IVisitable) left2, right);
            left2.firstpos.addAll(Arrays.asList(1, 2, 3));
            left2.lastpos.add(3);

            right = new OperandNode("b");
            right.firstpos.add(4);
            right.lastpos.add(4);
            right.position = 4;

            left2 = new BinOpNode("°", (IVisitable) left2, right);
            left2.firstpos.addAll(Arrays.asList(1, 2, 3));
            left2.lastpos.add(4);

            right = new OperandNode("b");
            right.firstpos.add(5);
            right.lastpos.add(5);
            right.position = 5;

            left2 = new BinOpNode("°", (IVisitable) left2, right);
            left2.firstpos.addAll(Arrays.asList(1, 2, 3));
            left2.lastpos.add(5);

            right = new OperandNode("#");
            right.firstpos.add(6);
            right.lastpos.add(6);
            right.position = 6;

            left2 = new BinOpNode("°", (IVisitable) left2, right);
            left2.firstpos.addAll(Arrays.asList(1, 2, 3));
            left2.lastpos.add(6);

            return (IVisitable) left2;
        }

        private SortedMap<Integer, FollowPosTableEntry> createFollowPosTable() {
            SortedMap<Integer, FollowPosTableEntry> followPosTable = new TreeMap<>();
            FollowPosTableEntry entry = new FollowPosTableEntry(1, "a");
            entry.followpos.addAll(Arrays.asList(1, 2, 3));
            followPosTable.put(1, entry);
            entry = new FollowPosTableEntry(2, "b");
            entry.followpos.addAll(Arrays.asList(1, 2, 3));
            followPosTable.put(2, entry);
            entry = new FollowPosTableEntry(3, "a");
            entry.followpos.add(4);
            followPosTable.put(3, entry);
            entry = new FollowPosTableEntry(4, "b");
            entry.followpos.add(5);
            followPosTable.put(4, entry);
            entry = new FollowPosTableEntry(5, "b");
            entry.followpos.add(6);
            followPosTable.put(5, entry);
            entry = new FollowPosTableEntry(6, "#");
            followPosTable.put(6, entry);
            return followPosTable;
        }
}
