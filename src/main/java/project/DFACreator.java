package project;

import java.util.*;

public class DFACreator {

    private final Set<Integer>                            positionsForStartState;
    private final SortedMap<Integer, FollowPosTableEntry> followPosTable;  //bei uns Klasse FollowPos mit großem P
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable; //= new ThreadLocal<Map<DFAState, Map<Character, DFAState>>>();
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


        //Algorithmus durchlaufen
        while(!qStates.isEmpty())
        {
            //aktuellen Zustand aus qStates entnehmen
            DFAState currentState=qStates.get(counter);
            //aktuellen Zustand aus qStates entfernen
            qStates.remove(counter);


            //neue Zeile in stateTransitionTable hinzufuegen mit currentState als Schluessel
            stateTransitionTable.put(currentState, new HashMap<>());

            for(String symbol:alphabet)
            {
                //Folgezustand ermitteln + eifnügen in neue Zeile
                Character Ctest;

                Ctest = Character.valueOf(symbol.charAt(0));
                Map<Character, DFAState> testung = new HashMap<>(); //wo geht es von current state weiter mit dem aktuellen symbol?
                FollowPosTableEntry fpte = followPosTable.get(counter);

                testung.put(Ctest, fpte);
                stateTransitionTable.put(currentState, testung);
                //Überprüfen, ob Folgezustand schone rfasst bzw. verarbeitet wurde
                //fals nicht, am Ende von qStates hinzufügen
            }
        }

    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable()
    {
        return stateTransitionTable;
    }



}

