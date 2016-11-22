/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Interval;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.Subgraph;
import org.gephi.graph.api.TimeIndex;
import org.gephi.statistics.plugin.builder.RandomPredictionBuilder;
import org.gephi.utils.progress.Progress;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class RandomPrediction extends PredictorAbs {
    
    @Override
    public Double getSimilitude(Graph graph,Node n1, Node n2) {     
        return 1.0;
    }
    
    @Override
    public String getTitle() {
       return NbBundle.getMessage(RandomPredictionBuilder.class, "RandomPrediction.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
     @Override
    public void execute(GraphModel graphModel) {
        Graph graph;                       
        boolean isDirected=false;
        Progress.start(progressTicket);
        isCanceled = false;
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getGraphModel() != null) {
            isDirected = graphController.getGraphModel().isDirected();
        }
        if (isDirected) {
            graph = graphModel.getDirectedGraphVisible();
        } else {
            graph = graphModel.getUndirectedGraphVisible();
        }
        graph.readLock();
        if (graphModel.isDynamic()){
            super.executeDinamico(graphModel,graph);
        }
        else
        {
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        graph.readUnlockAll();        
        filterNodes(graph); //Filtra nodos para quedarse con los que quiero (Todos, Intervalo, Unico)
        Random r = new Random();
        double eficienciaNode=0;   
        double recallNode=0;
        for (Node n: validos){
            Edge[] edges = graph.getEdges(n).toArray(); //Agarro todos los enlaces del nodo que estoy tratando
            ArrayList<Edge> enlaces = new ArrayList<Edge>(Arrays.asList(edges));    
            ArrayList<Edge> oculNode = new ArrayList<Edge>();    
            int cantHide = (int) Math.round(enlaces.size()*porcentaje/100);
            int i =0;
                while (i<cantHide){
                    Edge a = enlaces.remove(r.nextInt(enlaces.size()));
                    graph.removeEdge(a);              
                    ocultadas.add(a);
                    oculNode.add(a);
                    i++;
                }
            ArrayList<Prediction> predNode = this.getNodePrediction(graph, n);
            ArrayList<Prediction> predNodeFinal = new ArrayList<Prediction>();         
            cargarValoresRoc(oculNode,predNode);
            int j=0;         
            while ((j<cantPredictions)&&(predNode.size()>0)){
               Prediction p = predNode.remove(r.nextInt(predNode.size()));
               predNodeFinal.add(p);
               j++;
            }
            eficienciaNode=getEficiencia(oculNode,predNodeFinal);
            recallNode=getRecall(oculNode,predNodeFinal);
            predictions.addAll(predNodeFinal);
            graph.readUnlockAll();
            for (Edge e:oculNode){
                graph.addEdge(e);
            }
            eficienciaTotal+=eficienciaNode;
            recallTotal+=recallNode;
            if (isCanceled) {
                graph.readUnlockAll();
                break;
            }
        }
       
       Collections.sort(predictions);            
       result=predictions;
       if ((validos.size()>0)&&(validos.size()-descarteEficiencia>0))
               eficienciaTotal=eficienciaTotal/(validos.size()-descarteEficiencia);
       if ((validos.size()>0)&&(validos.size()-descarteRecall>0))
            recallTotal=recallTotal/(validos.size()-descarteRecall);
       graph.readUnlockAll();     
    }
 }
           
    @Override
    public boolean cancel() {
        isCanceled = true;
        return true;
    }
    
    public void executeDinamico(GraphModel graphModel, Graph graph){
        Interval intervalo = graphModel.getTimeBounds();
        GraphView currentView = graphModel.getVisibleView();
        GraphView view = graphModel.createView();
        Subgraph g = graphModel.getGraph(view);
        graph.writeLock();
        double t0=tiempo*(intervalo.getHigh()-intervalo.getLow())/100;
        t0 = t0 + intervalo.getLow();
        
        //AGREGO NODOS
        TimeIndex<Node> nodeIndex = graphModel.getNodeTimeIndex(currentView);
        if (Double.isInfinite(nodeIndex.getMinTimestamp()) && Double.isInfinite(nodeIndex.getMaxTimestamp())) {
            for (Node node : graph.getNodes()) {
                g.addNode(node);
            }
        } else {
            for (Node node : nodeIndex.get(new Interval(intervalo.getLow(), intervalo.getHigh()))) {
                g.addNode(node);
            }
        }
        //AGREGO ARISTAS
        TimeIndex<Edge> edgeIndex = graphModel.getEdgeTimeIndex(currentView);
         if (Double.isInfinite(edgeIndex.getMinTimestamp()) && Double.isInfinite(edgeIndex.getMaxTimestamp())) {
            for (Edge edge : graph.getEdges()) {
                if (g.contains(edge.getSource()) && g.contains(edge.getTarget())) {
                    g.addEdge(edge);
                }
            }
        } else {
            for (Edge edge : edgeIndex.get(new Interval(t0, t0))) {
                if (g.contains(edge.getSource()) && g.contains(edge.getTarget())) {
                    g.addEdge(edge);
                }
            }
        }
        graph.writeUnlock();
        System.out.println("Tiempo minimo: " + t0 + "Tiempo maximo: " + intervalo.getHigh());   
        
        //PREDECIR
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        graph.readUnlockAll();        
        filterNodes(graph); //Filtra nodos para quedarse con los que quiero (Todos, Intervalo, Unico)        
        double eficienciaNode=0;   
        double recallNode=0;
        boolean pertenece=false;
        for (Node n: validos){
            Edge[] edges = graph.getEdges(n).toArray(); //Agarro todos los enlaces del nodo que estoy tratando
            ArrayList<Edge> enlaces = new ArrayList<Edge>(Arrays.asList(edges));    
            ArrayList<Edge> oculNode = new ArrayList<Edge>();    
            for (Edge aux:enlaces)
            {
                for (Edge e:g.getEdges()){           
                    if ((e.getSource().equals(aux.getSource()))&&(e.getTarget().equals(aux.getTarget()))){
                        pertenece=true;
                        break;
                    }
                }
                if (!pertenece){
                    oculNode.add(aux);
                    ocultadas.add(aux);               
                }
                 pertenece=false;
            }
            
           Random r = new Random();
            ArrayList<Prediction> predNode = this.getNodePrediction(g, n);
            ArrayList<Prediction> predNodeFinal = new ArrayList<Prediction>();
            Collections.sort(predNode);   
            int j=0;         
            while ((j<cantPredictions)&&(predNode.size()>0)){
               Prediction p = predNode.remove(r.nextInt(predNode.size()));
               predNodeFinal.add(p);
               j++;
            }
            eficienciaNode=getEficiencia(oculNode,predNodeFinal);
            recallNode=getRecall(oculNode,predNodeFinal);
            predictions.addAll(predNodeFinal);
            graph.readUnlockAll();
            for (Edge e:oculNode){
                graph.addEdge(e);
            }
            eficienciaTotal+=eficienciaNode;
            recallTotal+=recallNode;
            System.out.println("Nodo numero: "+n.getId().toString() + " Eficiencia: " + eficienciaNode + " Recall: " + recallNode);
              if (isCanceled) {
                graph.readUnlockAll();
                break;
            }
        }
       
       Collections.sort(predictions);            
       result=predictions;
       if ((validos.size()>0)&&(validos.size()-descarteEficiencia>0))
               eficienciaTotal=eficienciaTotal/(validos.size()-descarteEficiencia);
       if ((validos.size()>0)&&(validos.size()-descarteRecall>0))
            recallTotal=recallTotal/(validos.size()-descarteRecall);
       graph.readUnlockAll();     
    }
}
