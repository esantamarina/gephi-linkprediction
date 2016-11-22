/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;

/**
 *
 * @author esteban.santamarina
 */
public class Prediction implements Comparable  {

    
    //PROBAR DESPUES SI SE PUEDE RECIBIR UN EDGE DIRECTAMENTE
    private double similitude;
    private int factor;
 //   private Edge edge;
    private Node n1, n2;
    private boolean exito;
    
//    public Prediction(Edge edge, float similitude){
//        this.edge=edge;
//        this.similitude=similitude;
//    }
    
     public Prediction(Node n1, Node n2, double similitude, int orden){
       this.n1=n1;
       this.n2=n2;
       this.similitude=similitude;
       this.factor=orden;
       this.exito=false;
    }
     
    public void setExito(boolean a){
        exito=a;
    }
    
    public boolean getExito(){
        return exito;
    }
    
    public Prediction(Node n1, Node n2){
       this.n1=n1;
       this.n2=n2;
    } 
    public Node getNodeSource(){
         return n1;
    }
     
    public Node getNodeTarget(){
         return n2;
    }
    
    public double getSimilitude(){
        return similitude;
    }

    @Override
    public int compareTo(Object o) {
     Prediction aux = (Prediction) o;
     return Double.compare(similitude, aux.getSimilitude())*factor;
    }
     
    @Override
    public boolean equals(Object o){
        Prediction p2 = (Prediction) o;
        if ((n1.getId()==p2.getNodeTarget().getId()) && (n2.getId()==p2.getNodeSource().getId()))
            return true;
        else
            return false;
    }
     
}
