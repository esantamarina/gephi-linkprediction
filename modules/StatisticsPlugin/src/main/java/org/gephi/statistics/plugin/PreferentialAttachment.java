/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.PreferentialAttachmentBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class PreferentialAttachment  extends PredictorAbs  {

    @Override
    public Double getSimilitude(Graph graph,Node n1, Node n2) {
        Node[] list1 = graph.getNeighbors(n1).toArray();
        Node[] list2 = graph.getNeighbors(n2).toArray();
        double result = list1.length*list2.length;
        if (result>0)
            return result;
        else
            return null;
    }

    @Override
    public String getTitle() {
           return NbBundle.getMessage(PreferentialAttachmentBuilder.class, "PreferentialAttachment.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }

}

