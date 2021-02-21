import org.junit.jupiter.api.Test;
import project.BinOpNode;
import project.OperandNode;
import project.UnaryOpNode;
import project.Visitable;

import java.util.*;

import project.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCases {

    //Vergleich zweier Syntaxbäume
    @Test
    private static boolean equals(Visitable v1, Visitable v2) {
        if (v1 == v2)
            return true;
        if (v1 == null)
            return false;
        if (v2 == null)
            return false;
        if (v1.getClass() != v2.getClass())
            return false;
        if (v1.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }
        if (v1.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && equals(op1.subNode, op2.subNode);
        }
        if (v1.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        throw new IllegalStateException("Ungueltiger Knotentyp");
    }


    //Überprüfen des ersten Visitors (Kapitel 4.3)
    @Test
    private boolean equals(Visitable expected, Visitable visited) {
        if (expected == null && visited == null) return true;
        if (expected == null || visited == null) return false;
        if (expected.getClass() != visited.getClass()) return false;
        if (expected.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) expected;
            BinOpNode op2 = (BinOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.left, op2.left) && equals(op1.right, op2.right);
        }
        if (expected.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) expected;
            UnaryOpNode op2 = (UnaryOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.subNode, op2.subNode);
        }
        if (expected.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) expected;
            OperandNode op2 = (OperandNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos);
        }
        throw new IllegalStateException(
                "Beide Wurzelknoten sind Instanzen der Klasse %1$s !" + " Dies ist nicht erlaubt!" +
                        expected.getClass().getSimpleName());
    }

    //Test DFA
    @Test
    public void test()
    {
        Set<Integer> firstpostRoot = new HashSet<>(Arrays.asList(1,2));

        SortedMap<Integer, FollowPosTableEntry> followPosTable = new TreeMap<>();
        FollowPosTableEntry fPTEntry = new FollowPosTableEntry(1, "a");
        fPTEntry.followpos.addAll(Arrays.asList(1, 2, 3, 4, 5));
        followPosTable.put(1, fPTEntry);
        fPTEntry = new FollowPosTableEntry(2, "b");
        fPTEntry.followpos.addAll(Arrays.asList(1, 2, 3, 4, 5));
        followPosTable.put(2, fPTEntry);
        fPTEntry = new FollowPosTableEntry(3, "b");
        fPTEntry.followpos.addAll(Arrays.asList(3, 4, 5));
        followPosTable.put(3, fPTEntry);
        fPTEntry = new FollowPosTableEntry(4, "c");
        fPTEntry.followpos.addAll(Arrays.asList(3, 4, 5));
        followPosTable.put(4, fPTEntry);
        fPTEntry = new FollowPosTableEntry(5, "d");
        fPTEntry.followpos.add(6);
        followPosTable.put(5, fPTEntry);
        fPTEntry = new FollowPosTableEntry(6, "#");
        followPosTable.put(6, fPTEntry);

        DFACreator creator = new DFACreator(firstpostRoot, followPosTable);

        creator.populateStateTransitionTable();

        Map<DFAState, Map<Character, DFAState>> controllMap = new HashMap<>();

        DFAState state1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2)));
        DFAState state2 = new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4,5)));
        DFAState state3 = new DFAState(3, false, new HashSet<>(Arrays.asList(3,4,5)));
        DFAState state4 = new DFAState(4, true, new HashSet<>(Collections.singletonList(6)));

        Map<Character, DFAState> innerMap = new HashMap<>();
        innerMap.put('a', state2);
        innerMap.put('b', state2);
        innerMap.put('c', null);
        innerMap.put('d', null);
        controllMap.put(state1, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', state2);
        innerMap.put('b', state2);
        innerMap.put('c', state3);
        innerMap.put('d', state4);
        controllMap.put(state2, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', null);
        innerMap.put('b', state3);
        innerMap.put('c', state3);
        innerMap.put('d', state4);
        controllMap.put(state3, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', null);
        innerMap.put('b', null);
        innerMap.put('c', null);
        innerMap.put('d', null);
        controllMap.put(state4, innerMap);

        assertEquals(controllMap, creator.getStateTransitionTable());

    }




}
