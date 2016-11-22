/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import java.util.HashMap;
import javax.swing.JPanel;
import org.gephi.statistics.plugin.NodeInformation;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class NodeInformationUI extends PredictorAbsUI {
    
    private NodeInformationPanel panel;
    private NodeInformation node;
    private StatSettingsParameter settingsHitting = new StatSettingsParameter();
    
    @Override
    public JPanel getSettingsPanel() {
       panel = new NodeInformationPanel();
       return panel;
    }

     @Override
    public PredictionPanelAbs getPanel(){
        return panel;
    }
    
    @Override
    public void setPanelNull(){
        panel=null;
    }


    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return NodeInformation.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "NodeInformationUI.name");  
    }

    @Override
    public String getShortDescription() {
        return "";
    }

    @Override
    public String getCategory() {
        return StatisticsUI.LINK_PREDICTION;
    }

    @Override
    public int getPosition() {
      return 990;
    }

    
      @Override
    public void setup(Statistics statistics) {
         node = (NodeInformation) statistics;
      //   NodeInformationPanel panel = (NodeInformationPanel) panel;
        if (this.getPanel() != null) {     
            settingsHitting.load(node);    
            panel.setCantidad(node.getCantidad());
            panel.setPorcentaje(node.getPorcentaje());
            panel.setParametros(node.getParametros());
        }
    }
    
    @Override
    public void unsetup() {    
    //   ParameterDependentPanel panelParameter = (ParameterDependentPanel) panel;
       if (panel != null) {         
            settingsHitting.save(node);
            node.setParametros(panel.getParametros());
            node.setCantidad(panel.getCantidad());         
            node.setPorcentaje(panel.getPorcentaje());
            if (panel.getRadioButton1()) {
                node.setMinNode(null);
                node.setMaxNode(null);
            }
            else
            {
               if (panel.getRadioButton2()) {
                    if ((panel.getMinNode()!=null)&&(panel.getMaxNode()!=null)){
                         node.setMinNode(Integer.parseInt(panel.getMinNode()));
                         node.setMaxNode(Integer.parseInt(panel.getMaxNode()));
                    }
               }
               else
                   if (panel.getRadioButton3())
                       if (panel.getUniqueNode()!=null){
                         node.setMinNode(Integer.parseInt(panel.getUniqueNode()));
                         node.setMaxNode(Integer.parseInt(panel.getUniqueNode()));  
                       }
            }
        }
        this.setPanelNull();
        predictor = null;
    }
    
     private class StatSettingsParameter  {
    
         private HashMap<String,Double> parametros;
         

        private void save(NodeInformation stat) {
            this.parametros=stat.getParametros();        
        }

        private void load(NodeInformation stat) {
            stat.setParametros(parametros);            
        }
    }
    
}
