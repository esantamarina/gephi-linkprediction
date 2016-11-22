/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;


import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.VectorNodeInformationBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class VectorNodeInformation extends PredictorAbs{

    String column;
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
        double result=0;
        if (!column.equals("")) {
        graph.getModel().getNodeTable().getColumn(column);
        if ((n1.getAttribute(column)!=null)&&(n2.getAttribute(column)!=null)){
            String a = n1.getAttribute(column).toString();
            String b = n2.getAttribute(column).toString();
            String[] wordsN1 = a.split("\\W+");
            String[] wordsN2 = b.split("\\W+");
            for (String s1:wordsN1){
                for (String s2: wordsN2){
                    if (s1.equals(s2)) {
                        result++;
                        break;
                    }
                }
            }
        }
        }
        if (result!=0)
            return result;
        else
            return null;
        
    }

    @Override
    public String getTitle() {
            return NbBundle.getMessage(VectorNodeInformationBuilder.class, "VectorNodeInformation.name");
    }

    @Override
    public String getAlgDescription() {
        return "";
    }
    
    public void setColumn(String c){
        this.column=c;
    }
    
    public String getColumn(){
        return column;
    }
    
}
