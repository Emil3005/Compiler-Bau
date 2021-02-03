package project;

import java.util.Set;

public class DFAState {
    public final int index;
    public final Boolean isAcceptingState;
    public final Set<Integer> positionsSet;
    public DFAState(int index,
                    Boolean isAcceptingState,
                    Set<Integer> positionsSet)
    {
        this.index = index;
        this.isAcceptingState = isAcceptingState;
        this.positionsSet = positionsSet;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        DFAState other = (DFAState)obj;
        return equals(this.positionsSet, other.positionsSet);
    }
    @Override
    public int hashCode()
    {
        return this.positionsSet == null ? 0 : this.positionsSet.hashCode();
    }
    private static boolean equals(Object o1, Object o2)
    {
        if (o1 == o2) return true;
        if (o1 == null) return false;
        if (o2 == null) return false;
        return o1.equals(o2);
    }
}

