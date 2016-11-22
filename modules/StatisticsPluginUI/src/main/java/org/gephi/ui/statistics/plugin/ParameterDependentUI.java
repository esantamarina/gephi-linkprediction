/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import javax.swing.JPanel;
import org.gephi.statistics.plugin.HittingTime;
import org.gephi.statistics.plugin.ParameterDependent;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class ParameterDependentUI extends PredictorAbsUI  {
    
    private ParameterDependentPanel panel;
    private StatSettingsParameter settingsHitting = new StatSettingsParameter();
    private ParameterDependent parameter;
    
       @Override
    public JPanel getSettingsPanel() {
       panel = new ParameterDependentPanel();
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
        return ParameterDependent.class;
    }

    @Override
    public String getValue() {
           return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "ParameterDependentUI.name");  
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
         parameter = (ParameterDependent) statistics;
         ParameterDependentPanel panelParameter = (ParameterDependentPanel) panel;
        if (this.getPanel() != null) {
            settingsHitting.load(parameter);    
            panelParameter.setCantidad(parameter.getCantidad());
            panelParameter.setPorcentaje(parameter.getPorcentaje());
            panelParameter.setExponente(parameter.getExponente());
        }
    }
    
    @Override
    public void unsetup() {    
       ParameterDependentPanel panelParameter = (ParameterDependentPanel) panel;
       if (panelParameter != null) {
            settingsHitting.save(parameter);
            parameter.setCantidad(panelParameter.getCantidad());
            parameter.setExponente(panelParameter.getExponente());
            parameter.setPorcentaje(panelParameter.getPorcentaje());
            if (panelParameter.getRadioButton1()) {
                parameter.setMinNode(null);
                parameter.setMaxNode(null);
            }
            else
            {
               if (panelParameter.getRadioButton2()) {
                    if ((panelParameter.getMinNode()!=null)&&(panelParameter.getMaxNode()!=null)){
                         parameter.setMinNode(Integer.parseInt(panelParameter.getMinNode()));
                         parameter.setMaxNode(Integer.parseInt(panelParameter.getMaxNode()));
                    }
               }
               else
                   if (panelParameter.getRadioButton3())
                       if (panelParameter.getUniqueNode()!=null){
                         parameter.setMinNode(Integer.parseInt(panelParameter.getUniqueNode()));
                         parameter.setMaxNode(Integer.parseInt(panelParameter.getUniqueNode()));  
                       }
            }
        }
        this.setPanelNull();
        predictor = null;
    }

    @Override
    public int getPosition() {
        return 940;
    }
    
    private class StatSettingsParameter  {
        
        private int cantidad=10;
        private double exponente=0.5;

        private void save(ParameterDependent stat) {
            this.cantidad=stat.getCantidad();
            this.exponente=stat.getExponente();
        }

        private void load(ParameterDependent stat) {
            stat.setCantidad(cantidad);
            stat.setExponente(exponente);
        }
    }
    
   
       
    
}
