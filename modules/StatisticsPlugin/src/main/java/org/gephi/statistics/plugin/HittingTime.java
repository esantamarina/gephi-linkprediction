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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.HittingTimeBuilder;
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
public class HittingTime extends PredictorAbs{
    
    private double pasos=0;
    private int cantIteraciones=1;
    private Node destino;
    private final ArrayList<String> visitados = new ArrayList<String>();
    ArrayList<String> todocamino = new ArrayList<String>();

    public HittingTime(){
        super();
        cantIteraciones=1;
        orden=1;
    }
    
    public int getIteraciones(){
        return cantIteraciones;        
    }
    public void setIteraciones(int d){
        cantIteraciones=d;
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
     public Double getSimilitude(Graph graph, Node n1, Node n2) {        
        visitados.clear();
        visitados.add(n1.getId().toString());     
        this.destino=n2;
        int repeticion=0;        
        while (repeticion<cantIteraciones){
             pasos=0;
             Double result = getHittingTime(graph,n1);
             if (result !=null)
                 return result;
             else 
                 repeticion++;
        }
        return null;
    }
    
    public Double getHittingTime(Graph graph, Node actual){      
         if (destino.equals(actual))
             return pasos;
         else
         {            
            boolean visitado=false;
            Node[] aux = graph.getNeighbors(actual).toArray();
            List<Node> list = new ArrayList<Node>(Arrays.asList(aux));         
            while ((!visitado)&&(list.size()>0)){               
                Random r = new Random();
                int camino;
                camino =r.nextInt(list.size());
                if (!visitados.contains(list.get(camino).getId().toString())){                   
                    pasos++;
                    visitado=true;
                    visitados.add(list.get(camino).getId().toString());
                    return getHittingTime(graph,list.get(camino));                    
                }
                else
                {
                    list.remove(camino);
                }                    
            }
            return null;
         }         
    }
    
    @Override
    public String getReport(){          
            
            XYSeries dSeries = this.getCurvaRoc("Valores Roc");
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
                
                chart1.getXYPlot().setRenderer(new XYSplineRenderer());                              
                DecimalFormat df = new DecimalFormat("0.00");   
                 DecimalFormat df2 = new DecimalFormat("0.0000");
                XYSplineRenderer renderer = (XYSplineRenderer)  chart1.getXYPlot().getRenderer();                
                renderer.setShapesVisible(false);
                
                String curvaRocImageFile = ChartUtils.renderChart(chart1, "curva-roc-file.png");
             
                
           String report = "<HTML><BODY> <h2>"+getTitle()+"</h2> "
                + "<hr> <br />"
                + "<h3> Eficiencia = " + df.format(eficienciaTotal*100) + "%"+ "<h3>" 
                + "<h3> Recall = " +  df.format(recallTotal*100) + "%"+  "<h3>" 
                + "<h3> Parámetros: </h3>"
                + "Cantidad de predicciones = " + cantPredictions + "<br>"   
                + "Porcentaje enlace a ocultar = " + porcentaje +" %"+ "<br>" 
                + "Cantidad de iteraciones = " + cantIteraciones + "<br>" 
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

    @Override
    public String getTitle() {
        return NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
}
