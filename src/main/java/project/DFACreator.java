package project;


import java.util.*;
//9899545
public class DFACreator {

    private final Set<Integer>                            positionsForStartState;
    private final SortedMap<Integer, FollowPosTableEntry> followPosTable;
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;
    private int counter = 1;
    /**    * Man beachte ! Parameter <code>positionsForStartState</code> muss vom Aufrufer
     * * mit der firstpos-Menge des Wurzelknotens des Syntaxbaums initialisiert werden !    */
    public DFACreator(Set<Integer> positionsForStartState, SortedMap<Integer, FollowPosTableEntry> followPosTable)
    {
        this.positionsForStartState = positionsForStartState;
        this.followPosTable = followPosTable;
        this.stateTransitionTable   = new HashMap<>();
    }

    // befuellt die Uebergangsmatrix
    public void populateStateTransitionTable()
    {
        //Alphabet ermitteln
        Set<String> alphabet = new HashSet<>();
        for(FollowPosTableEntry entry: followPosTable.values())
        {
            if(!"#".equals(entry.symbol))
            {
                alphabet.add(entry.symbol);
            }
        }

        //eine Liste mit Startzustand initialisieren
        List<DFAState> qStates = new ArrayList<>();
        int posOfTerminatingSymbol = followPosTable.lastKey(); //Schluessel des letzten Eintrags
        DFAState startState = new DFAState
                (
                counter++,
                positionsForStartState.contains(posOfTerminatingSymbol),
                positionsForStartState
                );

        qStates.add(startState);
        DFAState currentState;
        Map<Character, DFAState> innerMap;
        DFAState nextState;

        //Algorithmus durchlaufen
        while(!qStates.isEmpty())
        {
            //aktuellen Zustand aus qStates entnehmen
            currentState=qStates.get(0);
            //aktuellen Zustand aus qStates entfernen
            qStates.remove(0);
            innerMap= new HashMap<>();

            //neue Zeile in stateTransitionTable hinzufuegen mit currentState als Schluessel
            stateTransitionTable.put(currentState, innerMap);

            for(String symbol:alphabet)
            {
                //Folgezustand ermitteln + eifnügen in neue Zeile


                Set<Integer> nextPositionsSet = new HashSet<>();

                for(FollowPosTableEntry entry: followPosTable.values())
                {
                    if(currentState.positionsSet.contains(entry.position) && entry.symbol.equals(symbol))
                    {
                        nextPositionsSet.addAll(entry.followpos);
                    }
                }


                if(nextPositionsSet.isEmpty())
                {
                    nextState = null;
                }
                else
                {
                    boolean isAcceptingState = nextPositionsSet.contains(posOfTerminatingSymbol);

                    nextState = new DFAState(counter++, isAcceptingState, nextPositionsSet);
                }

                innerMap.put(Character.valueOf(symbol.charAt(0)), nextState);
                //Überprüfen, ob Folgezustand schone rfasst bzw. verarbeitet wurde
                //fals nicht, am Ende von qStates hinzufügen
                if(nextState != null && !stateTransitionTable.containsKey(nextState) && !qStates.contains(nextState))
                {
                    qStates.add(nextState);
                }



            }
        }

    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable()
    {
        return stateTransitionTable;
    }



}

