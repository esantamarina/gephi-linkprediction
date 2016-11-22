/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import javax.swing.JPanel;
import org.gephi.statistics.plugin.HittingTime;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class HittingTimeUI extends PredictorAbsUI  {
    
    private HittingTimePanel panel;
    private StatSettingsHitting settingsHitting = new StatSettingsHitting();
    private HittingTime hitting;
   
       
    @Override
    public JPanel getSettingsPanel() {
       panel = new HittingTimePanel();
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
        return HittingTime.class;
    }

    @Override
    public String getValue() {
           return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "HittingTimeUI.name");  
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
         hitting = (HittingTime) statistics;
         HittingTimePanel panelHitting = (HittingTimePanel) panel;
        if (this.getPanel() != null) {
            settingsHitting.load(hitting);    
            panelHitting.setCantidad(hitting.getCantidad());
            panelHitting.setPorcentaje(hitting.getPorcentaje());
            panelHitting.setIteraciones(hitting.getIteraciones());
        }
    }
    
    @Override
    public void unsetup() {    
       HittingTimePanel panelHitting = (HittingTimePanel) panel;
       if (panelHitting != null) {
            settingsHitting.save(hitting);
            hitting.setCantidad(panelHitting.getCantidad());
            hitting.setIteraciones(panelHitting.getIteraciones());
            hitting.setPorcentaje(panelHitting.getPorcentaje());
            if (panelHitting.getRadioButton1()) {
                hitting.setMinNode(null);
                hitting.setMaxNode(null);
            }
            else
            {
               if (panelHitting.getRadioButton2()) {
                    if ((panelHitting.getMinNode()!=null)&&(panelHitting.getMaxNode()!=null)){
                         hitting.setMinNode(Integer.parseInt(panelHitting.getMinNode()));
                         hitting.setMaxNode(Integer.parseInt(panelHitting.getMaxNode()));
                    }
               }
               else
                   if (panelHitting.getRadioButton3())
                       if (panelHitting.getUniqueNode()!=null){
                         hitting.setMinNode(Integer.parseInt(panelHitting.getUniqueNode()));
                         hitting.setMaxNode(Integer.parseInt(panelHitting.getUniqueNode()));  
                       }
            }
        }
        this.setPanelNull();
        predictor = null;
    }

    @Override
    public int getPosition() {
        return 700;
    }
    
    private class StatSettingsHitting  {
        
        private int cantidad=10;
        private int iteraciones=1;
        private int orden=1;

        private void save(HittingTime stat) {
            this.cantidad=stat.getCantidad();
            this.iteraciones=stat.getIteraciones();
            this.orden=orden;
        }

        private void load(HittingTime stat) {
            stat.setCantidad(cantidad);
            stat.setOrden(orden);
            stat.setIteraciones(iteraciones);
        }
    }
    
}
