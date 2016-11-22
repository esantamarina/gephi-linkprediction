/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.gephi.algorithms.shortestpath.AbstractShortestPathAlgorithm;
import org.gephi.algorithms.shortestpath.BellmanFordShortestPathAlgorithm;
import org.gephi.algorithms.shortestpath.DijkstraShortestPathAlgorithm;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.ShortestPathBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class ShortestPath  extends PredictorAbs{

    public ShortestPath(){
        orden=1;
    }
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2){
       //SHORTEST PATH
        AbstractShortestPathAlgorithm algorithm;      
        GraphModel gm = graph.getModel();
        if (gm.isDirected()) {
            algorithm = new BellmanFordShortestPathAlgorithm(gm.getDirectedGraphVisible(), n1);
        } else {
            algorithm = new DijkstraShortestPathAlgorithm(gm.getGraphVisible(), n1);
        }
        algorithm.compute();
        double distance;

        if ((distance = algorithm.getDistances().get(n2)) != Double.POSITIVE_INFINITY) {           
            return distance;
        }
        else
            return null;
    }
    
    @Override
     public XYSeries getCurvaRoc(String name){
        ArrayList<Double> positivos = new ArrayList<Double>();
        ArrayList<Double> negativos = new ArrayList<Double>();
        Iterator it = valoresRoc.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            Prediction p = (Prediction) e.getKey();   
            if ((Integer)e.getValue()>0){                            
                positivos.add(p.getSimilitude());
            }
            else
            {
                negativos.add(p.getSimilitude());
            }
        }
        
        int mayores=0;
        int empates=0;
        int r = 0;
        for (Double p:positivos){
            for (Double n:negativos){
                r = p.compareTo(n);
                if (r<0)
                    mayores++;                
                else
                {
                    if (r==0)
                        empates++;
                }
            }
        }
        auc = (mayores + 0.5 * empates) / (positivos.size()*negativos.size());
        
        double acierto=0;
        ArrayList<Double> todos = new ArrayList<Double>();
        
        ArrayList<Double> aciertosPos = new ArrayList<Double>();
        ArrayList<Double> aciertosNeg = new ArrayList<Double>();
        
        todos.addAll(positivos);
        todos.addAll(negativos);
        Collections.sort(todos);
        Collections.reverse(todos);       
        
        for (Double valor:todos){
            for (Double valorPos:positivos){
                r = valorPos.compareTo(valor);
                if (r<=0)
                    acierto++;
            }
            aciertosPos.add(acierto);
            acierto=0;
            for (Double valorNeg:negativos){
                r = valorNeg.compareTo(valor);
                if (r<=0)
                    acierto++;
            }
            aciertosNeg.add(acierto);
            acierto=0;
            
        }
        double maxPos=0;
        double maxNeg=0;
        if (aciertosPos.size()>0){
             maxPos = aciertosPos.get(0);
        }
        if (aciertosNeg.size()>0){
             maxNeg = aciertosNeg.get(0);
        }
        if ((maxPos!=0)&&(maxNeg!=0)) {
            for (int i=0;i<aciertosPos.size();i++){
                double v = aciertosPos.get(i);
                v = v / maxPos; 
                aciertosPos.set(i, v);
             }
             for (int i=0;i<aciertosNeg.size();i++){
                double v = aciertosNeg.get(i);
                v = v / maxNeg; 
                aciertosNeg.set(i, v);
             }        
        }
            
            XYSeries series = new XYSeries(name);
            for (int j=0;j<aciertosPos.size();j++) {
                Number y = (Number) aciertosPos.get(j);                
                Number x = (Number) aciertosNeg.get(j);
                series.add(x, y);    
            }           
            series.add(0,0);
            return series; 
    }
      

    @Override
    public String getTitle() {
       return NbBundle.getMessage(ShortestPathBuilder.class, "ShortestPath.name");
    }
    @Override
    public String getAlgDescription() {
        return "";
    }
}
