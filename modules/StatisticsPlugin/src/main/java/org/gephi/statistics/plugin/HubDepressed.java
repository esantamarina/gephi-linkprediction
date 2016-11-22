/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.HubDepressedBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class HubDepressed extends PredictorAbs {
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
       CommonNeighbors common = new CommonNeighbors();
       Double numerador = common.getSimilitude(graph, n1, n2);
       double denominador=0;
       int vecinos1 = graph.getNeighbors(n1).toArray().length;
       int vecinos2 = graph.getNeighbors(n2).toArray().length;
       if (vecinos1>vecinos2)
           denominador=vecinos1;
       else
           denominador=vecinos2;
       if ((numerador!=null) && (denominador!=0)) {
           return numerador/denominador;
       }
       else
           return null;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(HubDepressedBuilder.class, "HubDepressed.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
}
