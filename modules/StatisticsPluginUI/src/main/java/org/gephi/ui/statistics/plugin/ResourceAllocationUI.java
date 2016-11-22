/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.ui.statistics.plugin;

import javax.swing.JPanel;
import org.gephi.statistics.plugin.ResourceAllocation;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = StatisticsUI.class)
public class ResourceAllocationUI extends PredictorAbsUI {

    
    private ResourceAllocationPanel panel;
    
    @Override
    public JPanel getSettingsPanel() {
       panel = new ResourceAllocationPanel();
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
        return ResourceAllocation.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(getClass(), "ResourceAllocationUI.name");  
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
      return 400;
    }

}
