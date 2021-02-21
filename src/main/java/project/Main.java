package project;

import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;

public class Main {
    public static void main(String... args){
        Scanner scanner = new Scanner(System.in);
        String txtInput = scanner.next();
        IVisitable syntaxTree = null;

        //Parser

        Parser parser = new Parser(txtInput);
        syntaxTree = parser.parse();

        //firstVis

        FirstVisitor firstVis = new FirstVisitor();
        DepthFirstIterator.traverse(syntaxTree,firstVis);

        //secondVis

        SecondVisitor secondVisitor = new SecondVisitor();
        DepthFirstIterator.traverse(syntaxTree,secondVisitor);
        SortedMap<Integer, FollowPosTableEntry> followPosTableEntrySortedMap = secondVisitor.getFollowPosTableEntries();

        //DFA

        DFACreator dfaCreator = new DFACreator( ((SyntaxNode) syntaxTree).firstpos, followPosTableEntrySortedMap);
        dfaCreator.populateStateTransitionTable();
        Map<DFAState, Map<Character,DFAState>> transTable = dfaCreator.getStateTransitionTable();
        System.out.println(transTable);



    }
}
