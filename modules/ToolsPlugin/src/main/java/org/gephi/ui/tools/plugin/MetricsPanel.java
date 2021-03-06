/*
Copyright 2008-2010 Gephi
Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
Website : http://www.gephi.org

This file is part of Gephi.

DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

Copyright 2011 Gephi Consortium. All rights reserved.

The contents of this file are subject to the terms of either the GNU
General Public License Version 3 only ("GPL") or the Common
Development and Distribution License("CDDL") (collectively, the
"License"). You may not use this file except in compliance with the
License. You can obtain a copy of the License at
http://gephi.org/about/legal/license-notice/
or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
specific language governing permissions and limitations under the
License.  When distributing the software, include this License Header
Notice in each file and include the License files at
/cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
License Header, with the fields enclosed by brackets [] replaced by
your own identifying information:
"Portions Copyrighted [year] [name of copyright owner]"

If you wish your version of this file to be governed by only the CDDL
or only the GPL Version 3, indicate your decision by adding
"[Contributor] elects to include this software in this distribution
under the [CDDL or GPL Version 3] license." If you do not indicate a
single choice of license, a recipient has the option to distribute
your version of this file under either the CDDL, the GPL Version 3 or
to extend the choice of license to its licensees as provided above.
However, if you add GPL Version 3 code and therefore, elected the GPL
Version 3 license, then the option applies only if the new code is
made subject to such option by the copyright holder.

Contributor(s):

Portions Copyrighted 2011 Gephi Consortium.
*/
package org.gephi.ui.tools.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.statistics.plugin.HittingTime;
import org.gephi.statistics.plugin.ParameterDependent;

import org.gephi.statistics.plugin.PredictorAbs;
import org.gephi.statistics.plugin.builder.CombineBuilder;
import org.gephi.statistics.plugin.builder.HittingTimeBuilder;
import org.gephi.statistics.plugin.builder.ParameterDependentBuilder;

import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.gephi.statistics.spi.StatisticsBuilder;
/**
 *
 * @author Mathieu Bastian
 */
public class MetricsPanel extends javax.swing.JPanel {

   
    private StatisticsBuilder[] statisticsBuilders;
    private Graph graph;
    private double porcentaje;
    private ArrayList<Edge> ocultas = new ArrayList<Edge>();
    /** Creates new form HeatMapPanel */
    
    public MetricsPanel() {
        initComponents();
        loadCombo(jComboBox1);       
        jTextField1.setEnabled(false);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/gephi/tools/plugin/resources/filter.png")));
    }
    
      public void loadCombo(JComboBox combo){
     //   StatisticsController controller = Lookup.getDefault().lookup(StatisticsController.class);            
           statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics(); 
                      if ((!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "Combine.name")))&&
                           (!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "NodeInformation.name")))  &&
                              (!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "VectorNodeInformation.name"))))
                                   combo.addItem(c.getTitle());
               }              
        } 
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(654, 28));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(MetricsPanel.class, "MetricsPanel.jLabel1.text")); // NOI18N

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jButton1.setText(org.openide.util.NbBundle.getMessage(MetricsPanel.class, "MetricsPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(MetricsPanel.class, "MetricsPanel.jLabel2.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(MetricsPanel.class, "MetricsPanel.jTextField1.text")); // NOI18N
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(192, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1)
                .addComponent(jLabel2)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

     public PredictorAbs getPredictor(String p){
        statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {        
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics();                       
                      if (c.getTitle().equals(p))
                          return c;
               }              
        }
        return null;
    }
   
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jButton1.getText().equals("Ejecutar")){                                  
            boolean isDirected=false;        
            GraphModel graphModel=null;
            GraphController graphController = Lookup.getDefault().lookup(GraphController.class);       
            if (graphController != null && graphController.getGraphModel() != null) {
                graphModel = graphController.getGraphModel();
                isDirected = graphController.getGraphModel().isDirected();
            }
            if (isDirected) {
                graph = graphModel.getDirectedGraphVisible(); 
            } else {
                graph = graphModel.getUndirectedGraphVisible();            
            }          
            
            if (!graphModel.getEdgeTable().hasColumn(jComboBox1.getSelectedItem().toString())){
                graphModel.getEdgeTable().addColumn(jComboBox1.getSelectedItem().toString(), Double.class);
            }
                graph.readUnlockAll();                     
                Edge[] edges = graph.getEdges().toArray(); //Agarro todos los enlaces del grafo
                ArrayList<Edge> enlaces = new ArrayList<Edge>(Arrays.asList(edges));  

            PredictorAbs p = getPredictor(jComboBox1.getSelectedItem().toString()); //Predictor elegido
                
                //SI TIENEN PARAMETROS ADICIONALES
            if (jComboBox1.getSelectedItem().toString().equals((NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name")))){
                HittingTime hitting = (HittingTime) p;
                hitting.setIteraciones(Integer.parseInt(jTextField1.getText()));
            }
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name"))){
                ParameterDependent parameter = (ParameterDependent) p;
                parameter.setExponente(Double.parseDouble(jTextField1.getText()));
            }

            for (Edge e: enlaces){
                Double similitude = p.getSimilitude(graph, e.getSource(), e.getTarget());                    
                if (similitude!=null) {
                    e.setAttribute(jComboBox1.getSelectedItem().toString(), similitude);                        
                }
                else
                {
                    e.setAttribute(jComboBox1.getSelectedItem().toString(), 0.0);        
                }
            }
            JOptionPane.showMessageDialog(this, "Se ha agregado una columna dentro de la tabla de datos con los valores de la mética");      
         }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
      if (jComboBox1.getSelectedItem()!=null) {
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
               if (jTextField1.getText().equals("")){
                   jTextField1.setText("1");
               }
            }
           else
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
                if (jTextField1.getText().equals("")){
                   jTextField1.setText("0.5");
               }
         }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
         if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
             jTextField1.setEnabled(true);
             jTextField1.setText("1");
             jLabel2.setText("Iteraciones");  
            
        }
       else
        if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
            {
              jTextField1.setEnabled(true);
              jTextField1.setText("0.5");
              jLabel2.setText("Exponente");                 
           
            }
         else
            {
                jTextField1.setText("");
                jLabel2.setText("Parámetro Adicional");  
                jTextField1.setEnabled(false);
            }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
         char c = evt.getKeyChar();
        
        if (jComboBox1.getSelectedItem()!=null) {
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
               if (!Character.isDigit(c)){
                    evt.consume();
                }
            }
           else
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
               if (!jTextField1.getText().contains(".")&&(c=='.')&&(!jTextField1.getText().equals(""))){
                 evt.setKeyChar(c);
                }
                else
                {
                    if (!Character.isDigit(c))
                        evt.consume();
                } 
         }
    }//GEN-LAST:event_jTextField1KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
