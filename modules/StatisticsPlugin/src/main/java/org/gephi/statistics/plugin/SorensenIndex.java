/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.util.ArrayList;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.JaccardCoefficientBuilder;
import org.gephi.statistics.plugin.builder.SorensenIndexBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class SorensenIndex extends PredictorAbs{
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
       CommonNeighbors common = new CommonNeighbors();
       Double numerador = common.getSimilitude(graph, n1, n2);
       if (numerador!=null){
          Node[] vecinosSource = graph.getNeighbors(n1).toArray();
          Node[] vecinosTarget = graph.getNeighbors(n2).toArray();
          double denominador = vecinosSource.length+vecinosTarget.length;
          if (denominador!=0)
              return numerador/denominador;
          else
             return null;
       }
       else
           return null;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(SorensenIndexBuilder.class, "SorensenIndex.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
}
