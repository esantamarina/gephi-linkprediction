/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gephi.tools.plugin;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.gephi.graph.api.Node;
import org.gephi.tools.spi.NodeClickEventListener;
import org.gephi.tools.spi.Tool;
import org.gephi.tools.spi.ToolEventListener;
import org.gephi.tools.spi.ToolSelectionType;
import org.gephi.tools.spi.ToolUI;
import org.gephi.ui.tools.plugin.ClusteringPanel;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author esteban.santamarina
 */
@ServiceProvider(service = Tool.class)
public class Clustering implements Tool {
    
    
    private ToolEventListener[] listeners;
    private ClusteringPanel clusteringPanel;


    public Clustering() {
        //Default settings
      
    }

    @Override
    public void select() {
    }

    @Override
    public void unselect() {
        clusteringPanel.undoChanges();
        listeners = null;
        clusteringPanel = null;
    }

    @Override
    public ToolEventListener[] getListeners() {
            listeners = new ToolEventListener[1];
        listeners[0] = new NodeClickEventListener() {
            @Override
            public void clickNodes(Node[] nodes) {
               
            }
        };
        return listeners;
    }

    @Override
    public ToolUI getUI() {
        return new ToolUI() {
            @Override
            public JPanel getPropertiesBar(Tool tool) {
                clusteringPanel = new ClusteringPanel();
                return clusteringPanel;
            }

            @Override
            public String getName() {
                return NbBundle.getMessage(HeatMap.class, "Clustering.name");
            }

            @Override
            public Icon getIcon() {
                return new ImageIcon(getClass().getResource("/org/gephi/tools/plugin/resources/cluster.png"));
            }

            @Override
            public String getDescription() {
                return NbBundle.getMessage(Clustering.class, "Clustering.description");
            }

            @Override
            public int getPosition() {
                return 950;
            }
        };
    }

    @Override
    public ToolSelectionType getSelectionType() {
        return ToolSelectionType.NONE;
    }
    
}
