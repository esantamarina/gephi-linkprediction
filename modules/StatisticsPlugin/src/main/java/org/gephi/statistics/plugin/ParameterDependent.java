/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import java.text.DecimalFormat;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.ParameterDependentBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class ParameterDependent extends PredictorAbs {

    private double exponente=0.5;
    
    public ParameterDependent(){
        super();
        exponente=0.5;
    }
    
    public double getExponente(){
        return exponente;
    }
    
    public void setExponente(double d){
        exponente=d;
    }
            
    @Override
    public Double getSimilitude(Graph graph,Node n1, Node n2) {             
        CommonNeighbors common = new CommonNeighbors();
        Double numerador = common.getSimilitude(graph, n1, n2);
        PreferentialAttachment preferential = new PreferentialAttachment();
        Double denominador = preferential.getSimilitude(graph, n1, n2);
        if ((numerador!=null)&&(denominador!=null)&&(denominador!=0))
            return numerador/Math.pow(denominador, exponente);
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
                + "Exponente aplicado = " + exponente + "<br>" 
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
       return NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
}
