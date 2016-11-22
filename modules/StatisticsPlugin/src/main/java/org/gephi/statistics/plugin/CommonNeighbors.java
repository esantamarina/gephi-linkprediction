/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.CommonNeighborsBuilder;
import org.openide.util.NbBundle;
/**
 *
 * @author esteban.santamarina
 */
public class CommonNeighbors  extends PredictorAbs {

    @Override
    public Double getSimilitude(Graph graph,Node n1, Node n2) {
        double result=0;
        for (Node source : graph.getNeighbors(n1)) {
            for (Node target : graph.getNeighbors(n2)) {
                if (source.equals(target)){
                    result++;
                }
            }        
        }
        if (result>0)
            return result;
        else
            return null;
    }

    @Override
    public String getTitle() {
       return NbBundle.getMessage(CommonNeighborsBuilder.class, "CommonNeighbors.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
}
