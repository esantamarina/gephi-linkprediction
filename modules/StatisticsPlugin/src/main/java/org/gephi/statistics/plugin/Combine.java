/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.text.DecimalFormat;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.CombineBuilder;
import org.gephi.statistics.plugin.builder.HittingTimeBuilder;
import org.gephi.statistics.plugin.builder.ParameterDependentBuilder;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class Combine extends PredictorAbs{
    
    private PredictorAbs predictor1, predictor2;  
    private StatisticsBuilder[] statisticsBuilders;
    private Double similitud1,similitud2;
    public Combine(){
        super();
    }
    
    public PredictorAbs getPredictor1(){
        return predictor1;
    }
    public PredictorAbs getPredictor2(){
        return predictor2;
    }
    
     public void setPredictor1(PredictorAbs p1){
        predictor1 = p1;
    }
    
    public void setPredictor2(PredictorAbs p2){
        predictor2 = p2;
    }
    
    
    public void setPredictor1(String p1,String parametro){
        predictor1 = getPredictor(p1);
        if (p1.equals((NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name")))){
            HittingTime hitting = (HittingTime) predictor1;
            hitting.setIteraciones(Integer.parseInt(parametro));
        }
        if (p1.equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name"))){
            ParameterDependent parameter = (ParameterDependent) predictor1;
            parameter.setExponente(Double.parseDouble(parametro));
        }
        
    }
    
    public void setPredictor2(String p2,String parametro){
        predictor2 = getPredictor(p2);
        if (p2.equals((NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name")))){
            HittingTime hitting = (HittingTime) predictor2;
            hitting.setIteraciones(Integer.parseInt(parametro));
        }
        if (p2.equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name"))){
            ParameterDependent parameter = (ParameterDependent) predictor2;
            parameter.setExponente(Double.parseDouble(parametro));
        }
    }
    
    public PredictorAbs getPredictor(String p){
        statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {        
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics();                       
                      if (c.getTitle().equals(p))
                          return c;
               }              
        }
           return null;
    }
     
      
    @Override
     public Double getSimilitude(Graph graph, Node n1, Node n2) {           
        similitud1=predictor1.getSimilitude(graph, n1, n2);
        similitud2=predictor2.getSimilitude(graph, n1, n2);
        if ((similitud1!=null)&&(similitud2!=null)){
            return similitud1+ similitud2/2;
        }
        else
          return null;
    }
     
     
      @Override
    public String getReport(){          
                          
           String curvaRocImageFile = super.getRocImage();
        
         DecimalFormat df = new DecimalFormat("0.00");   
           DecimalFormat df2 = new DecimalFormat("0.0000");   
           String report = "<HTML><BODY> <h2>"+getTitle()+"</h2> "
                + "<hr> <br />"
                + "<h3> Eficiencia = " + df.format(eficienciaTotal*100) + "%"+ "<h3>" 
                + "<h3> Recall = " +  df.format(recallTotal*100) + "%"+  "<h3>" 
                + "<h3> Parámetros: </h3>"
                + "Cantidad de predicciones = " + cantPredictions + "<br>"   
                + "Porcentaje enlace a ocultar = " + porcentaje +" %"+ "<br>" 
                + "Métrica de predicción 1 = " + predictor1.getTitle() + "<br>"   
                + "Métrica de predicción 2 = " + predictor2.getTitle() + "<br>" 
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
        return NbBundle.getMessage(CombineBuilder.class, "Combine.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
}
