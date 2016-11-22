package org.gephi.statistics.plugin;


import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.SaltonCosineBuilder;
import org.openide.util.NbBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author esteban.santamarina
 */
public class SaltonCosine extends PredictorAbs{
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
       CommonNeighbors common = new CommonNeighbors();
       Double numerador = common.getSimilitude(graph, n1, n2);
       PreferentialAttachment preferential = new PreferentialAttachment();
       Double denominador = preferential.getSimilitude(graph, n1, n2);
       if ((numerador!=null) && (denominador!=null) && (denominador!=0)) {
           return numerador/Math.sqrt(denominador);
       }
       else
           return null;
    }

    @Override
    public String getTitle() {
        return NbBundle.getMessage(SaltonCosineBuilder.class, "SaltonCosine.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
}
