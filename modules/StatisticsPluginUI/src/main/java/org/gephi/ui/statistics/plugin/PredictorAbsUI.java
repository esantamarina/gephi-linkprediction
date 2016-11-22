/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;


import org.gephi.statistics.plugin.PredictorAbs;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;

/**
 *
 * @author esteban.santamarina
 */
    public abstract class PredictorAbsUI  implements StatisticsUI {
    
    protected PredictorAbs predictor;
    protected final StatSettings settings = new StatSettings();
    
    public abstract PredictionPanelAbs getPanel();
    public abstract void setPanelNull();
    
    @Override
    public void setup(Statistics statistics) {
       this.predictor = (PredictorAbs) statistics;
        if (this.getPanel() != null) {
            settings.load(predictor);    
            this.getPanel().setCantidad(predictor.getCantidad());
            this.getPanel().setPorcentaje(predictor.getPorcentaje());
            this.getPanel().setTiempo(predictor.getTiempo());
        }
    }

    @Override
    public void unsetup() {
       if (this.getPanel() != null) {
            settings.save(predictor);
            predictor.setCantidad(this.getPanel().getCantidad());
            predictor.setPorcentaje(this.getPanel().getPorcentaje());
            predictor.setTiempo(this.getPanel().getTiempo());
            if (this.getPanel().getRadioButton1()) {
                predictor.setMinNode(null);
                predictor.setMaxNode(null);
            }
            else
            {
               if (this.getPanel().getRadioButton2()) {
                    if ((this.getPanel().getMinNode()!=null)&&(this.getPanel().getMaxNode()!=null)){
                         predictor.setMinNode(Integer.parseInt(this.getPanel().getMinNode()));
                         predictor.setMaxNode(Integer.parseInt(this.getPanel().getMaxNode()));
                    }
               }
               else
                   if (this.getPanel().getRadioButton3())
                       if (this.getPanel().getUniqueNode()!=null){
                         predictor.setMinNode(Integer.parseInt(this.getPanel().getUniqueNode()));
                         predictor.setMaxNode(Integer.parseInt(this.getPanel().getUniqueNode()));  
                       }
            }
        }
        this.setPanelNull();
        predictor = null;
    }
    
 
     private static class StatSettings {
        
        private int cantidad=10;

        private void save(PredictorAbs stat) {
            this.cantidad=stat.getCantidad();
        }

        private void load(PredictorAbs stat) {
            stat.setCantidad(cantidad);
        }
    }

    
}
