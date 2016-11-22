/*
Copyright 2008-2010 Gephi
Authors : Patick J. McSweeney <pjmcswee@syr.edu>
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
package org.gephi.ui.statistics.plugin;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.gephi.statistics.plugin.PredictorAbs;
import org.gephi.statistics.plugin.builder.CombineBuilder;
import org.gephi.statistics.plugin.builder.HittingTimeBuilder;
import org.gephi.statistics.plugin.builder.ParameterDependentBuilder;
import org.gephi.statistics.plugin.builder.ShortestPathBuilder;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author esteban.santamarina
 */

public class CombinePanel extends PredictionPanelAbs{

    private StatisticsBuilder[] statisticsBuilders;
    
    public CombinePanel() {
        super();
        initComponents(); 
        loadCombo(jComboBox1);
        loadCombo(jComboBox2);
        jRadioButton1.setSelected(true);
        int a = graph.getModel().getMaxNodeStoreId()-1;
       jLabel1.setText("# Nodo Máx: "+a);
        if (graph.getModel().isDynamic()){
            jCheckBox1.setEnabled(false);
            jCheckBox1.setSelected(true);
            jTextField5.setEnabled(false); 
            jScrollBar1.setEnabled(true);
            jScrollBar1.setValue(0);   
            jLabel2.setEnabled(true);
            jLabel3.setEnabled(true);
        }
        else
        {
            jCheckBox1.setEnabled(false);
            jScrollBar1.setEnabled(false);
            jLabel2.setEnabled(false);
            jLabel3.setEnabled(false);
        }
        jTextField6.setEnabled(false);
        jTextField7.setEnabled(false);
    }
    
     @Override
    public int getTiempo(){
        return jScrollBar1.getValue();
    }
    
    @Override
    public void setTiempo(int t) { 
        jScrollBar1.setValue(t);    
    }
    
    public JTextField getParam1(){
        return jTextField6;
    }
    
    public JTextField getParam2(){
        return jTextField7;
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
    
    public void loadComboHitting(JComboBox combo){
           statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics();                       
                      if (c.getTitle().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name")))
                            combo.addItem(c.getTitle());
               }              
         }  
    
    }
    
    public void loadComboShortest(JComboBox combo){
           statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics();                       
                      if (c.getTitle().equals(NbBundle.getMessage(ShortestPathBuilder.class, "ShortestPath.name")))
                            combo.addItem(c.getTitle());
               }              
         }
    }
    
    public void loadComboNormalizados(JComboBox combo){
           statisticsBuilders = Lookup.getDefault().lookupAll(StatisticsBuilder.class).toArray(new StatisticsBuilder[0]);
           for (StatisticsBuilder b : statisticsBuilders) {
               if (b.getStatistics() instanceof PredictorAbs)   {
                      PredictorAbs c = (PredictorAbs) b.getStatistics();                       
                      if ((!c.getTitle().equals(NbBundle.getMessage(ShortestPathBuilder.class, "ShortestPath.name"))) &&
                         (!c.getTitle().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name")))&&
                         (!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "Combine.name")))&&                          
                           (!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "NodeInformation.name")))  &&
                              (!c.getTitle().equals(NbBundle.getMessage(CombineBuilder.class, "VectorNodeInformation.name"))))
                                   combo.addItem(c.getTitle());                           
               }              
         }
    }
    
    @Override
    public void setCantidad(int c){
        this.jTextField1.setText(Integer.toString(c));
    }
    
    @Override
    public boolean getRadioButton1(){
        return jRadioButton1.isSelected();
    }
    
    @Override
    public boolean getRadioButton2(){
        return jRadioButton2.isSelected();
    }
    
    @Override
    public boolean getRadioButton3(){
        return jRadioButton3.isSelected();
    }
    
    @Override
    public String getUniqueNode(){
        if (jTextField4.getText().isEmpty())
            return null;
        else
            return jTextField4.getText();
    }
    
    @Override
    public String getMinNode(){
        if (jTextField2.getText().isEmpty())
            return null;
        else            
            return jTextField2.getText();
    }
    @Override
    public String getMaxNode(){
        if (jTextField3.getText().isEmpty())
            return null;
        else            
            return jTextField3.getText();
    }

    @Override
    public void setPorcentaje(Double c){
         this.jTextField5.setText(Double.toString(c));
    }
    
    @Override
    public Double getPorcentaje(){
        return Double.parseDouble(this.jTextField5.getText());
    }
 
    @Override
    public int getCantidad(){
        return Integer.parseInt(this.jTextField1.getText());
    }
    
    public JComboBox getCombo1 (){
        return jComboBox1;
    }
    
    public JComboBox getCombo2 (){
        return jComboBox2;
    }
    
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        directedButtonGroup = new javax.swing.ButtonGroup();
        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        labelCant = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JFormattedTextField();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabelPredicto1 = new javax.swing.JLabel();
        jLabelPredictor2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollBar1 = new javax.swing.JScrollBar();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();

        jXHeader1.setDescription(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jXHeader1.description")); // NOI18N
        jXHeader1.setTitle(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jXHeader1.title")); // NOI18N

        jTextField4.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField4.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel5.text")); // NOI18N

        jTextField5.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField5.text")); // NOI18N
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jRadioButton1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jRadioButton1.text")); // NOI18N
        jRadioButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton1StateChanged(evt);
            }
        });
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jRadioButton2.text")); // NOI18N
        jRadioButton2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton2StateChanged(evt);
            }
        });

        labelCant.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.labelCant.text")); // NOI18N

        jRadioButton3.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jRadioButton3.text")); // NOI18N
        jRadioButton3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton3StateChanged(evt);
            }
        });

        jXLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jXLabel1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jXLabel1.text")); // NOI18N
        jXLabel1.setFont(jXLabel1.getFont().deriveFont(jXLabel1.getFont().getSize()-1f));
        jXLabel1.setLineWrap(true);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel1.text")); // NOI18N

        jTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        jTextField1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField1.text")); // NOI18N
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jXLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jXLabel2.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jXLabel2.text")); // NOI18N
        jXLabel2.setFont(jXLabel2.getFont().deriveFont(jXLabel2.getFont().getSize()-1f));
        jXLabel2.setLineWrap(true);

        jTextField2.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField2.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel4.text")); // NOI18N

        jTextField3.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField3.text")); // NOI18N

        jLabelPredicto1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabelPredicto1.text")); // NOI18N

        jLabelPredictor2.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabelPredictor2.text")); // NOI18N

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel3.text")); // NOI18N

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jCheckBox1.text")); // NOI18N

        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.setVisibleAmount(0);
        jScrollBar1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jScrollBar1PropertyChange(evt);
            }
        });
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel2.text")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel6.text")); // NOI18N

        jLabel7.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jLabel7.text")); // NOI18N

        jTextField6.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField6.text")); // NOI18N
        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jTextField7.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jTextField7.text")); // NOI18N
        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jXLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jXLabel3.setText(org.openide.util.NbBundle.getMessage(CombinePanel.class, "CombinePanel.jXLabel3.text")); // NOI18N
        jXLabel3.setFont(jXLabel3.getFont().deriveFont(jXLabel3.getFont().getSize()-1f));
        jXLabel3.setLineWrap(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jXHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelCant)
                                .addGap(35, 35, 35)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jRadioButton1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton2)
                                    .addComponent(jRadioButton3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelPredicto1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabelPredictor2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXHeader1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(labelCant)
                    .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)))
                            .addComponent(jXLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelPredictor2)
                                .addComponent(jLabelPredicto1)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jXLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jRadioButton3)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton1StateChanged
        // TODO add your handling code here:
        if (jRadioButton1.isSelected()){
            jRadioButton2.setSelected(false);
            jRadioButton3.setSelected(false);
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(false);
            jTextField4.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton1StateChanged

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton2StateChanged
        // TODO add your handling code here:
        if (jRadioButton2.isSelected()){
            jRadioButton1.setSelected(false);
            jRadioButton3.setSelected(false);
            jTextField2.setEnabled(true);
            jTextField3.setEnabled(true);
            jTextField4.setEnabled(false);
        }
    }//GEN-LAST:event_jRadioButton2StateChanged

    private void jRadioButton3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton3StateChanged
        // TODO add your handling code here:
        if (jRadioButton3.isSelected()){
            jRadioButton1.setSelected(false);
            jRadioButton2.setSelected(false);
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(false);
            jTextField4.setEnabled(true);
        }
    }//GEN-LAST:event_jRadioButton3StateChanged

    private void jScrollBar1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jScrollBar1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollBar1PropertyChange

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        // TODO add your handling code here:
        this.jLabel2.setText(String.valueOf(jScrollBar1.getValue())+" %");
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
      //  jComboBox2.removeAllItems();
        if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
             jTextField6.setEnabled(true);
             jLabel6.setText("Cantidad de iteraciones");
             jTextField6.setText("1");
         //    loadComboShortest(jComboBox2);
        }
       else
        if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
            {
              jTextField6.setEnabled(true);
              jLabel6.setText("Parámetro del exponente");  
               jTextField6.setText("0.5");
         //     loadComboNormalizados(jComboBox2);
            }
        else
        {
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ShortestPathBuilder.class, "ShortestPath.name"))) {
                jLabel6.setText("Parámetro adicional N° 1");  
                jTextField6.setEnabled(false);
                 jTextField6.setText("");
           //     loadComboHitting(jComboBox2);
            }                      
            else
            {
                jLabel6.setText("Parámetro adicional N° 1");  
                jTextField6.setEnabled(false);
                 jTextField6.setText("");
             //   loadComboNormalizados(jComboBox2);
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox2.getSelectedItem()!=null) {
            if (jComboBox2.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
             jTextField7.setEnabled(true);
             jTextField7.setText("1");
             jLabel7.setText("Cantidad de iteraciones");
            }
           else
            if (jComboBox2.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
                {
                  jTextField7.setEnabled(true);
                  jTextField7.setText("0.5");
                  jLabel7.setText("Parámetro del exponente");      
                }
            else
            {
                jLabel7.setText("Parámetro adicional N° 2"); 
                jTextField7.setText("");
                jTextField7.setEnabled(false);
            }
        } 
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
         char c = evt.getKeyChar();
        if (!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
  
        char c = evt.getKeyChar();
        if (!jTextField5.getText().contains(".")&&(c=='.')&&(!jTextField5.getText().equals(""))){
                 evt.setKeyChar(c);
             }
        else
        {
            if (!Character.isDigit(c))
                evt.consume();
        }
       
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        if (jTextField1.getText().equals("")){
            jTextField1.setText("10");
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        // TODO add your handling code here:
         if (jTextField5.getText().equals("")){
            jTextField5.setText("20.0");
        }
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
        // TODO add your handling code here:
         if (jComboBox1.getSelectedItem()!=null) {
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
               if (jTextField6.getText().equals("")){
                   jTextField6.setText("1");
               }
            }
           else
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
                if (jTextField6.getText().equals("")){
                   jTextField6.setText("0.5");
               }
         }
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
        // TODO add your handling code here:
          if (jComboBox2.getSelectedItem()!=null) {
            if (jComboBox1.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
               if (jTextField7.getText().equals("")){
                   jTextField7.setText("1");
               }
            }
           else
            if (jComboBox2.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
                if (jTextField7.getText().equals("")){
                   jTextField7.setText("0.5");
               }
         }
    }//GEN-LAST:event_jTextField7FocusLost

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
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
               if (!jTextField6.getText().contains(".")&&(c=='.')&&(!jTextField6.getText().equals(""))){
                 evt.setKeyChar(c);
                }
                else
                {
                    if (!Character.isDigit(c))
                        evt.consume();
                } 
         }
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (jComboBox2.getSelectedItem()!=null) {
            if (jComboBox2.getSelectedItem().toString().equals(NbBundle.getMessage(HittingTimeBuilder.class, "HittingTime.name"))){
               if (!Character.isDigit(c)){
                    evt.consume();
                }
            }
           else
            if (jComboBox2.getSelectedItem().toString().equals(NbBundle.getMessage(ParameterDependentBuilder.class, "ParameterDependent.name")))    
               if (!jTextField7.getText().contains(".")&&(c=='.')&&(!jTextField7.getText().equals(""))){
                 evt.setKeyChar(c);
                }
                else
                {
                    if (!Character.isDigit(c))
                        evt.consume();
                } 
         }
    }//GEN-LAST:event_jTextField7KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup directedButtonGroup;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelPredicto1;
    private javax.swing.JLabel jLabelPredictor2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JFormattedTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private javax.swing.JLabel labelCant;
    // End of variables declaration//GEN-END:variables
}
