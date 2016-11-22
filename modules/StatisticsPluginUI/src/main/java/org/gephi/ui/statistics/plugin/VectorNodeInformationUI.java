/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;


import javax.swing.JPanel;
import org.gephi.statistics.plugin.VectorNodeInformation;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class VectorNodeInformationUI extends PredictorAbsUI {
    
    private VectorNodeInformationPanel panel;
    private VectorNodeInformation node;
    private StatSettingsParameter settingsHitting = new StatSettingsParameter();
    
    @Override
    public JPanel getSettingsPanel() {
       panel = new VectorNodeInformationPanel();
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
        return VectorNodeInformation.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "VectorNodeInformationUI.name");  
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
      return 999;
    }

    
    @Override
    public void setup(Statistics statistics) {
         node = (VectorNodeInformation) statistics;    
        if (this.getPanel() != null) {     
            settingsHitting.load(node);    
            panel.setCantidad(node.getCantidad());
            panel.setPorcentaje(node.getPorcentaje());
          //  panel.setColumn(node.getColumn());
        }
    }
    
    @Override
    public void unsetup() {    
    //   ParameterDependentPanel panelParameter = (ParameterDependentPanel) panel;
       if (panel != null) {         
            settingsHitting.save(node);
            node.setColumn(panel.getColumn());
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
    
         private String column;
         

        private void save(VectorNodeInformation stat) {
            this.column=stat.getColumn();        
        }

        private void load(VectorNodeInformation stat) {
            stat.setColumn(column);            
        }
    }
    
}
