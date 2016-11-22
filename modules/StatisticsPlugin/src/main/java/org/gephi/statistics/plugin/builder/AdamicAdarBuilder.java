/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.statistics.plugin.builder;

import org.gephi.statistics.plugin.AdamicAdar;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */

@ServiceProvider(service = StatisticsBuilder.class)

public class AdamicAdarBuilder implements StatisticsBuilder  {

    @Override
    public String getName() {
       return NbBundle.getMessage(AdamicAdarBuilder.class, "AdamicAdar.name");
    }

    @Override
    public Statistics getStatistics() {
       return new AdamicAdar();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
       return AdamicAdar.class;
    }
    
}
