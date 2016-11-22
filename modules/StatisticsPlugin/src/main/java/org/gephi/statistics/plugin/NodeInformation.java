
package org.gephi.statistics.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.gephi.statistics.plugin.builder.NodeInformationBuilder;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */
public class NodeInformation extends PredictorAbs {

    private HashMap<String,Double> parametros;
    
    public NodeInformation(){
        parametros = new HashMap<String,Double>();
        porcentaje=20.0;
        cantPredictions=10;
    }
    
    public HashMap<String,Double> getParametros(){
        return parametros;
    }
    
    public void setParametros(HashMap<String,Double> parametros){
        this.parametros=parametros;
    }
    
    @Override
    public Double getSimilitude(Graph graph, Node n1, Node n2) {
        double result=0;  
        Iterator it = parametros.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            if (graph.getModel().getNodeTable().hasColumn(e.getKey().toString())) {
            Object o1= n1.getAttribute(e.getKey().toString());
            Object o2= n2.getAttribute(e.getKey().toString());
            String clave1="";
            String clave2="";
            if ((o1!=null)&&(o2!=null)){
                clave1 = o1.toString(); 
                clave2 = o2.toString();  
                double tmp = (double)e.getValue();
                if (clave1.equals(clave2)){
                  result = result + tmp;
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
          return NbBundle.getMessage(NodeInformationBuilder.class, "NodeInformation.name");
    }

    @Override
    public String getAlgDescription() {
       return "";
    }
}
    
    
