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
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class JaccardCoefficient  extends PredictorAbs{
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
       CommonNeighbors common = new CommonNeighbors();
       Double numerador = common.getSimilitude(graph, n1, n2);
       if (numerador!=null){
       ArrayList<String> todos = new ArrayList<String>();
       for (Node a:graph.getNeighbors(n1)){
           todos.add(a.getId().toString());
       }
       for (Node b: graph.getNeighbors(n2))
       {
           if (!todos.contains(b.getId().toString()))
               todos.add(b.getId().toString());
       }
       if (todos.size()>0)
            return numerador/todos.size();
       else
           return null;
       }
       else
           return null;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(JaccardCoefficientBuilder.class, "JaccardCoefficient.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
}