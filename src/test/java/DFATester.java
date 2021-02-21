import org.junit.jupiter.api.Test;
import project.DFACreator;
import project.DFAState;
import project.FollowPosTableEntry;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DFATester {
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

        Map<DFAState, Map<Character, DFAState>> controlMap = new HashMap<>();

        DFAState state1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2)));
        DFAState state2 = new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4,5)));
        DFAState state3 = new DFAState(3, false, new HashSet<>(Arrays.asList(3,4,5)));
        DFAState state4 = new DFAState(4, true, new HashSet<>(Collections.singletonList(6)));

        Map<Character, DFAState> innerMap = new HashMap<>();
        innerMap.put('a', state2);
        innerMap.put('b', state2);
        innerMap.put('c', null);
        innerMap.put('d', null);
        controlMap.put(state1, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', state2);
        innerMap.put('b', state2);
        innerMap.put('c', state3);
        innerMap.put('d', state4);
        controlMap.put(state2, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', null);
        innerMap.put('b', state3);
        innerMap.put('c', state3);
        innerMap.put('d', state4);
        controlMap.put(state3, innerMap);

        innerMap = new HashMap<>();
        innerMap.put('a', null);
        innerMap.put('b', null);
        innerMap.put('c', null);
        innerMap.put('d', null);
        controlMap.put(state4, innerMap);

        assertEquals(controlMap, creator.getStateTransitionTable());

    }
}
