/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openide.util.Lookup;

/**
 *
 * @author esteban.santamarina
 */
public abstract class PredictorAbs implements Statistics,LongTask {
    
    protected int cantPredictions;
    protected ArrayList<Prediction> result = new ArrayList<Prediction>();
    protected boolean isCanceled = false;
    protected Integer minNode, maxNode;
    protected ProgressTicket progressTicket;
    protected Double porcentaje;
    protected double eficienciaTotal=0;
    protected double recallTotal=0;
    protected int descarteEficiencia=0;
    protected int descarteRecall=0;
    protected int tiempo;
    protected ArrayList<Edge> ocultadas = new ArrayList<Edge>();
    protected ArrayList<Node> validos;
    protected int orden;
    protected HashMap<Prediction,Integer> valoresRoc;
    protected double auc;
    
    public PredictorAbs(){
        cantPredictions=10;
        porcentaje=20.0;
        tiempo=0;
        orden=-1;
        auc=0;
        valoresRoc = new HashMap<Prediction,Integer>();
    }
    
    public abstract Double getSimilitude(Graph graph,Node n1, Node n2);
    
    public void setPorcentaje(Double d){
        porcentaje=d;        
    }
    
    public Double getPorcentaje(){
        return porcentaje;
    }
    
    public void setTiempo(int t){
        this.tiempo=t;
    }
    
    public int getTiempo(){
        return tiempo;
    }
    
    public int getMinNode(){
        return minNode;       
    }  
    
    public int getMaxNode(){
        return maxNode;
    }
    
    public void setMinNode(Integer n){
        minNode=n;
    }
    public void setMaxNode(Integer n){
        maxNode=n;
    }             
    
    @Override
    public boolean cancel() {
        isCanceled = true;
        return true;
    }

    @Override
    public void setProgressTicket(ProgressTicket progressTicket) {
        this.progressTicket = progressTicket;
    }
    
    public HashMap<Node, Integer> createIndiciesMap(Graph hgraph) {
        HashMap<Node, Integer> newIndicies = new HashMap<>();
        int index = 0;
        for (Node s : hgraph.getNodes()) {
            newIndicies.put(s, index);
            index++;
        }
        return newIndicies;
    }  
    
    public void filterNodes(Graph graph){
        Node[] nodes = graph.getNodes().toArray();
        if ((minNode==null)&&(maxNode==null)){       
            validos = new ArrayList<Node>(Arrays.asList(nodes));
        }
        else
        {
          validos = new ArrayList<Node>();
          for (Node e: nodes){
              if ((Float.parseFloat(e.getId().toString())>=minNode)&&(Float.parseFloat(e.getId().toString())<=maxNode)){
                  validos.add(e);                  
              }
          }  
        }
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
                           
            ArrayList<Prediction> predNode = this.getNodePrediction(g, n);
            ArrayList<Prediction> predNodeFinal = new ArrayList<Prediction>();            
            Collections.sort(predNode);   
            cargarValoresRoc(oculNode,predNode);
            int j=0;
            for (Prediction p:predNode){
                if (j<cantPredictions)
                   predNodeFinal.add(p); 
              j++; 
            }       
            eficienciaNode=getEficiencia(oculNode,predNodeFinal);
            recallNode=getRecall(oculNode,predNodeFinal);
            predictions.addAll(predNodeFinal);
            if (isCanceled) {
                graph.readUnlockAll();
                break;
            }
            graph.readUnlockAll();

            
            eficienciaTotal+=eficienciaNode;
            recallTotal+=recallNode;          
        }
            Collections.sort(predictions);            

            result=predictions;
            if ((validos.size()>0)&&(validos.size()-descarteEficiencia>0))
                 eficienciaTotal=eficienciaTotal/(validos.size()-descarteEficiencia);
            if ((validos.size()>0)&&(validos.size()-descarteRecall>0))
                 recallTotal=recallTotal/(validos.size()-descarteRecall);
            graph.readUnlockAll();     
    }

    @Override
    public void execute(GraphModel graphModel) {
        Graph graph;       
        Progress.start(progressTicket);
        isCanceled = false;
     
        boolean isDirected=false;
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getGraphModel() != null) {
            isDirected = graphController.getGraphModel().isDirected();
        }
        if (isDirected) {
            graph = graphModel.getDirectedGraphVisible();
            
        } else {
            graph = graphModel.getUndirectedGraphVisible();
        }
     
        if (graphModel.isDynamic()){
            executeDinamico(graphModel,graph);
        }
        else
        {
        graph.readLock();
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
            int cantHide = (int) Math.round(enlaces.size()*porcentaje/100); //Cantidad de enlaces a ocultar
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
            Collections.sort(predNode);   
            this.cargarValoresRoc(oculNode,predNode);
            
            int j=0;
            for (Prediction p:predNode){
                if (j<cantPredictions)
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
        
        //SACAR
//        Iterator it = valoresRoc.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry e = (Map.Entry)it.next();
//            Prediction p = (Prediction) e.getKey();            
//            System.out.println(p.getNodeSource().getLabel().toString()+ "-" +p.getNodeTarget().getLabel().toString()+ " " +p.getSimilitude() + " Cant: " + e.getValue());
//        }
        
       Collections.sort(predictions);                  
       result=predictions;
        if ((validos.size()>0)&&(validos.size()-descarteEficiencia>0))
               eficienciaTotal=eficienciaTotal/(validos.size()-descarteEficiencia);
       if ((validos.size()>0)&&(validos.size()-descarteRecall>0))
            recallTotal=recallTotal/(validos.size()-descarteRecall);
       graph.readUnlockAll();     
        }
    }
    
    public XYSeries getCurvaRoc(String name){
        ArrayList<Double> positivos = new ArrayList<Double>();
        ArrayList<Double> negativos = new ArrayList<Double>();
        Iterator it = valoresRoc.entrySet().iterator();
        //Dentro de la hash estan todas las predicciones realizadas, las divido en aciertos o no
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
        
        double acierto=0;
        ArrayList<Double> todos = new ArrayList<Double>();
        
        ArrayList<Double> aciertosPos = new ArrayList<Double>();
        ArrayList<Double> aciertosNeg = new ArrayList<Double>();
        
        
        //Armo todas las posibles combinaciones de predicciones, para calcular el auc
        int mayores=0;
        int empates=0;
        int r = 0;
        for (Double p:positivos){
            for (Double n:negativos){
                r = p.compareTo(n);
                if (r>0)
                    mayores++;                
                else
                {
                    if (r==0)
                        empates++;
                }
            }
        }
        auc = (mayores + 0.5 * empates) / (positivos.size()*negativos.size());
        
        //Guardo todas las predicciones en una lista ordenada crecientemente
        todos.addAll(positivos);
        todos.addAll(negativos);
        Collections.sort(todos);
        
        //Cuento cuantas veces cada valor de prediccion es superado por los positivos o negativos
        for (Double valor:todos){
            for (Double valorPos:positivos){
                r = valorPos.compareTo(valor);
                if (r>=0)
                    acierto++;
            }
            aciertosPos.add(acierto);
            acierto=0;
            for (Double valorNeg:negativos){
                r = valorNeg.compareTo(valor);
                if (r>=0)
                    acierto++;
            }
            aciertosNeg.add(acierto);
            acierto=0;
            
        }
        
        //Divido a cada valor de positivos y negativos por el numero mas alto para normalizar
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
        
            //Armo el conjunto con coordenadas X, Y
            XYSeries series = new XYSeries(name,false,true);
            for (int j=0;j<aciertosPos.size();j++) {                
                Number y = (Number) aciertosPos.get(j);               
                Number x = (Number) aciertosNeg.get(j);                             
                series.add(x, y);                  
            }         
            series.add(0,0);              
            return series; 
    }
    
    public Double getEficiencia (ArrayList<Edge> ocultadas,ArrayList<Prediction> predicciones){
        if (predicciones.size()>0){
            double aciertos=0;
            if (ocultadas.size()>0) {
            for (Edge e: ocultadas){
                for (Prediction p: predicciones){         
                    if (((p.getNodeSource().getId().equals(e.getSource().getId()))&&((p.getNodeTarget().getId().equals(e.getTarget().getId()))))
                        || ((p.getNodeSource().getId().equals(e.getTarget().getId()))&&((p.getNodeTarget().getId().equals(e.getSource().getId())))))
                    {
                        aciertos++;
                        p.setExito(true);
                    }
                }
            }          
            return aciertos/predicciones.size();
            }        
            else
            {
               descarteEficiencia++;
               return 0.0;
            }
        }
        else
        {
            return 0.0;
        }
    }
    
    
    public Double getRecall(ArrayList<Edge> ocultadas,ArrayList<Prediction> predicciones){
        if (ocultadas.size()>0){
            double aciertos=0;
            for (Edge e: ocultadas){
                for (Prediction p: predicciones){         
                    if (((p.getNodeSource().getId().equals(e.getSource().getId()))&&((p.getNodeTarget().getId().equals(e.getTarget().getId()))))
                        || ((p.getNodeSource().getId().equals(e.getTarget().getId()))&&((p.getNodeTarget().getId().equals(e.getSource().getId())))))
                        aciertos++;
                }
            }
            return aciertos/ocultadas.size();
        }
        else
        {
            descarteRecall++;
            return 0.0;
        }
    }
    
     public void cargarValoresRoc (ArrayList<Edge> ocultas,ArrayList<Prediction> predicciones) {        
         for (Prediction p: predicciones){
             if (!valoresRoc.containsKey(p)){
                 valoresRoc.put(p, 0);
             }
             for (Edge e:ocultas){
                 if (((p.getNodeSource().getId().equals(e.getSource().getId()))&&((p.getNodeTarget().getId().equals(e.getTarget().getId()))))
                    || ((p.getNodeSource().getId().equals(e.getTarget().getId()))&&((p.getNodeTarget().getId().equals(e.getSource().getId())))))
                    {                       
                       valoresRoc.put(p,valoresRoc.get(p)+1);
                    }
            }                                     
        }
    }
    
    public void setOrden(int orden){
        this.orden=orden;
    }
    
    public int getOrden(){
        return orden;
    }
        
    public int getCantidad(){
        return cantPredictions;
    }
    
    public void setCantidad(int c){
        cantPredictions=c;
    }

    public abstract String getTitle();
    
    public abstract String getAlgDescription();         
    
    public String getRocImage(){
        XYSeries dSeries = getCurvaRoc("Valores Roc");
        XYSeriesCollection dataset1 = new XYSeriesCollection();       
        dataset1.addSeries(dSeries);
        JFreeChart chart1 = ChartFactory.createXYLineChart(
            "Curva Roc",
            "FPR",
            "VPR",
            dataset1,
            PlotOrientation.VERTICAL,
            true,
            false,
            false);
            ChartUtils.decorateChart(chart1);
            ChartUtils.scaleChart(chart1, dSeries, true);

            chart1.getXYPlot().setRenderer(new XYLineAndShapeRenderer());
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart1.getXYPlot().getRenderer();
            //renderer.
            //renderer.set(true);
            //renderer.setDefaultShapesFilled(true);
           // renderer.setDefaultShapesVisible(true);

//            chart1.getXYPlot().setRenderer(new XYSplineRenderer());
//            XYSplineRenderer renderer = (XYSplineRenderer)  chart1.getXYPlot().getRenderer();
            renderer.setShapesVisible(false);

            String curvaRocImageFile = ChartUtils.renderChart(chart1, "curva-roc-file.png");
            return curvaRocImageFile;
    }
            
    @Override
    public String getReport(){                               

           String curvaRocImageFile = getRocImage();         
           DecimalFormat df = new DecimalFormat("0.00");   
           DecimalFormat df2 = new DecimalFormat("0.0000");   
           String report = "<HTML><BODY> <h2>"+getTitle()+"</h2> "
                + "<hr> <br />"
                + "<h3> Eficiencia = " + df.format(eficienciaTotal*100) + "%"+ "<h3>" 
                + "<h3> Recall = " +  df.format(recallTotal*100) + "%"+  "<h3>" 
                + "<h3> Parámetros: </h3>"
                + "Cantidad de predicciones = " + cantPredictions + "<br>"   
                + "Porcentaje enlace a ocultar = " + porcentaje +" %"+ "<br>" 
                + curvaRocImageFile + "<br>" 
                + "<h3> Area bajo la curva = " + df2.format(auc) + "<h3>" 
                + "<h3> Predicciones: </h3><br/>"
                + "<table> <tr>"
                + "<th> ID Origen </th>"
                + "<th> Nombre Origen </th>"
                + "<th> ID Destino </th>"
                + "<th> Nombre Destino </th>"
                + "<th> Similitud </th>"
                + "<th> Predicción exitosa </th> </tr>";
               
                
             for (Prediction p:result){
                 report=report+"<tr> <td align=\"center\">"+p.getNodeSource().getId().toString()+"</td> <td align=\"center\">" +p.getNodeSource().getLabel() +"</td><td align=\"center\">" + p.getNodeTarget().getId().toString()+"</td><td align=\"center\">"+p.getNodeTarget().getLabel()+"</td><td align=\"center\">"+ p.getSimilitude()+"</td><td align=\"center\">"+ p.getExito() +"</td></tr><br>";
             }   
                
//                           
//            report= report+ "</table><br/><br/>"
//                + "<h2>Enlaces ocultos:</h2>"
//                + "<table> <tr>"
//                + "<th> Nodo 1 </th>"
//                + "<th> Nodo 2 </th> </tr>";
//            for (Edge e:ocultadas){
//                report=report+"<tr> <td align=\"center\">"+ e.getSource().getId().toString() + "</td> <td align=\"center\">" + e.getTarget().getId().toString() +"</td></tr><br>";;
//            }
            report=report+ "</BODY> </HTML>";
              
        return report;
    }
    
    public ArrayList<Prediction> getNodePrediction(Graph graph, Node source){
        isCanceled = false;
        graph.readLock();
        Progress.start(progressTicket);
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        HashMap<Node, Integer> indicies = createIndiciesMap(graph);
        Double similitude;
        while (true) {
            boolean done=false;
            for (Node target : graph.getNodes()) {
                  if (!source.equals(target)){
                  boolean isNeighbor = false;            
                   for (Node neighbor:graph.getNeighbors(source)){
                    if(indicies.get(target).equals(indicies.get(neighbor)))
                       isNeighbor = true;
                    if (isCanceled) {
                        graph.readUnlockAll();
                        break;
                 }
                }
                if (!isNeighbor){
                    similitude = getSimilitude(graph,source,target);
                    if (similitude!=null){
                        Prediction newPrediction = new Prediction(source,target,similitude,orden);
                        boolean hasPrediction = false;
                        for (Prediction aux:predictions){
                            if (aux.equals(newPrediction)) {
                                hasPrediction=true;
                                break; 
                            }
                        }                    
                    if (!hasPrediction)
                        predictions.add(newPrediction);
                    }
                }
                if (isCanceled) {
                      graph.readUnlockAll();
                      break;
                   }
                }
            if (isCanceled) {
               graph.readUnlockAll();
               break;
                 }
            }
            done=true;
            if ((done) || (isCanceled)) {
               break;
            }
         }
        return predictions;
    }
    

}
