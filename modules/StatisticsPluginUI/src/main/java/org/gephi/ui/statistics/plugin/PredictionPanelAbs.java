/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.openide.util.Lookup;

/**
 *
 * @author esteban.santamarina
 */
public abstract class PredictionPanelAbs extends javax.swing.JPanel{
    
    protected boolean isDirected;
    protected final Graph graph;
    
    public PredictionPanelAbs() {
       
      
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getGraphModel() != null) {
            isDirected = graphController.getGraphModel().isDirected();
        }
        if (isDirected) {
            graph = graphController.getGraphModel().getDirectedGraphVisible();
        } else {
            graph = graphController.getGraphModel().getUndirectedGraphVisible();
        }
        
    }
    
    public abstract void setCantidad(int d);
    public abstract boolean getRadioButton1();
    public abstract boolean getRadioButton2();
    public abstract boolean getRadioButton3();
    public abstract String getUniqueNode();
    public abstract String getMinNode();
    public abstract String getMaxNode();
    public abstract void setPorcentaje(Double c);
    public abstract Double getPorcentaje();
    public abstract int getCantidad();
    public abstract void setTiempo(int t);
    public abstract int getTiempo();
 
}
