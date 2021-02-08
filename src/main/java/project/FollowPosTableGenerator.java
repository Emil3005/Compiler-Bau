package project;

import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGenerator implements Visitor {
    private final SortedMap<Integer,FollowposTableEntry> followPosTableEntries = new TreeMap<>();

    public void visit(OperandNode node){
        followPosTableEntries.put(
                node.position, new FollowposTableEntry(node.position, node.symbol)
        );
    }

     public void visit(BinOpNode node){
      //Nur ° oder | sind möglich
        if ("°".equals(node.operator)){
          //lastpos-Menge von Unterknoten left holen
          //firstpos-Menge von Unterknoten right holen
          //Algorithmus siehe Vorlesung
      }
        //Alternative spielt keine Rolle
    }

    public void visit(UnaryOpNode node){
        //Nur * , + oder ? möglich
        //lastpos-Menge von Unterknoten left holen
        //firstpos-Menge von Unterknoten right holen
        if ("*".equals(node.operator) || "+".equals(node.operator)){
            // ...
        }
    }
}
