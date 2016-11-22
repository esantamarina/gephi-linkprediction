/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.ResourceAllocationBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class ResourceAllocation extends PredictorAbs   {
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
       double result=0;
        for (Node source : graph.getNeighbors(n1)) {
            for (Node target : graph.getNeighbors(n2)) {
                if (source.equals(target)){
                    int cant = graph.getNeighbors(source).toArray().length;
                    if (cant!=0){
                        double a = 1/(double)cant;
                        result = result + a;
                    }                        
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
        return NbBundle.getMessage(ResourceAllocationBuilder.class, "ResourceAllocation.name");
    }
    
    @Override
    public String getAlgDescription() {
       return "";
    }
    
}
