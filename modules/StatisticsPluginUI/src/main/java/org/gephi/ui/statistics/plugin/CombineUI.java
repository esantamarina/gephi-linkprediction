/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import javax.swing.JPanel;
import org.gephi.statistics.plugin.Combine;
import org.gephi.statistics.plugin.PredictorAbs;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class CombineUI extends PredictorAbsUI  {
    
    private CombinePanel panel;
    private StatSettingsCombine settingsCombine = new StatSettingsCombine();
    private Combine combine;
    
     @Override
    public JPanel getSettingsPanel() {
       panel = new CombinePanel();
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
        return Combine.class;
    }

    @Override
    public String getValue() {
           return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "CombineUI.name");  
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
    public void setup(Statistics statistics) {
         combine = (Combine) statistics;
         CombinePanel panelCombine = (CombinePanel) panel;
        if (this.getPanel() != null) {
            settingsCombine.load(combine);    
            panelCombine.setCantidad(combine.getCantidad());
            panelCombine.setPorcentaje(combine.getPorcentaje());
        }
    }
    
      @Override
    public void unsetup() {    
       CombinePanel panelCombine = (CombinePanel) panel;
       if (panelCombine != null) {
            settingsCombine.save(combine);
            combine.setCantidad(panelCombine.getCantidad());
            combine.setPorcentaje(panelCombine.getPorcentaje());
            combine.setPredictor1(panelCombine.getCombo1().getSelectedItem().toString(),panelCombine.getParam1().getText());
            combine.setPredictor2(panelCombine.getCombo2().getSelectedItem().toString(),panelCombine.getParam2().getText());
            if (panelCombine.getRadioButton1()) {
                combine.setMinNode(null);
                combine.setMaxNode(null);
            }
            else
            {
               if (panelCombine.getRadioButton2()) {
                    if ((panelCombine.getMinNode()!=null)&&(panelCombine.getMaxNode()!=null)){
                         combine.setMinNode(Integer.parseInt(panelCombine.getMinNode()));
                         combine.setMaxNode(Integer.parseInt(panelCombine.getMaxNode()));
                    }
               }
               else
                   if (panelCombine.getRadioButton3())
                       if (panelCombine.getUniqueNode()!=null){
                         combine.setMinNode(Integer.parseInt(panelCombine.getUniqueNode()));
                         combine.setMaxNode(Integer.parseInt(panelCombine.getUniqueNode()));  
                       }
            }
        }
        this.setPanelNull();
        predictor = null;
    }
    
    
    @Override
    public int getPosition() {
        return 970;
    }
    
    private class StatSettingsCombine  {
        
        private int cantidad=10;
        private PredictorAbs p1, p2;

        private void save(Combine stat) {
            this.cantidad=stat.getCantidad();
            this.p1=stat.getPredictor1();
            this.p2=stat.getPredictor2();
        }

        private void load(Combine stat) {
            stat.setCantidad(cantidad);
            combine.setPredictor1(p1);
            combine.setPredictor2(p2);
        }
    }
}
