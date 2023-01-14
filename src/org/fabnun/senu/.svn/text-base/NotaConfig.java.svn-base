/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NotaConfig.java
 *
 * Created on Mar 3, 2011, 12:38:37 PM
 */
package org.fabnun.senu;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author fabnun
 */
public class NotaConfig extends javax.swing.JPanel {

    private boolean updateActive = false;

    private void updateProgramar() {
        boolean selected = jCheckBox1.isSelected();
        jDateChooser1.setVisible(selected);
        jComboBox1.setVisible(selected);
        jTextField2.setVisible(selected);
        jFormattedTextField1.setVisible(selected);
        jLabel3.setVisible(selected);
        jLabel6.setVisible(selected);
        jLabel4.setVisible(selected);
    }

    private synchronized void updateDB() {
        if (editable && updateActive) {
            try {
                StringBuilder sb = new StringBuilder();
                int idx[] = jList1.getSelectedIndices();
                for (int id : idx) {
                    sb.append(dlm.get(id)).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                Timestamp time = null;
                if (jCheckBox1.isSelected()) {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(jDateChooser1.getDate());
                    String s = jFormattedTextField1.getText();
                    int index = s.indexOf(":");
                    int hour = 0;
                    int minute = 0;
                    if (index > -1) {
                        hour = Integer.parseInt(s.substring(0, index));
                        minute = Integer.parseInt(s.substring(index + 1));
                    }
                    cal.set(GregorianCalendar.HOUR_OF_DAY, hour);
                    cal.set(GregorianCalendar.MINUTE, minute);
                    cal.set(GregorianCalendar.SECOND, 0);
                    time = new Timestamp(cal.getTimeInMillis());
                }
                Constantes.db.setNota(
                        titulo,
                        usuario,
                        jTextArea1.getText(),
                        buttonColor1.getColor().getRGB(),
                        buttonColor2.getColor().getRGB(),
                        time,
                        jComboBox1.getSelectedIndex(),
                        jTextField2.getText().replaceAll("//s+", ""),
                        sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private String titulo, usuario;
    private DefaultListModel dlm;
    private boolean editable;

    /** Creates new form NotaConfig */
    public NotaConfig(Object[] datos, boolean configVisible, boolean editable) {
        initComponents();
        this.editable = editable;
        jTextArea1.setEditable(editable);
        setConfigVisible(configVisible);
        titulo = datos[0].toString();
        usuario = datos[1].toString();
        jTextArea1.setText(datos[2].toString());
        buttonColor1.setColor(new Color(((Integer) datos[3]).intValue()));
        buttonColor2.setColor(new Color(((Integer) datos[4]).intValue()));
        jTextArea1.setBackground(new Color(((Integer) datos[3]).intValue()));
        jTextArea1.setForeground(new Color(((Integer) datos[4]).intValue()));
        setName(datos[0].toString());
        if (datos[5] != null) {
            Date time = new Date(((Timestamp) datos[5]).getTime());
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(time);
            jDateChooser1.setDate(time);
            String s0 = "" + cal.get(GregorianCalendar.HOUR_OF_DAY);
            String s1 = "" + cal.get(GregorianCalendar.MINUTE);
            if (s0.length() == 1) {
                s0 = "0" + s0;
            }
            if (s1.length() == 1) {
                s1 = "0" + s1;
            }
            jFormattedTextField1.setText(s0 + ":" + s1);
        }
        jComboBox1.setSelectedIndex(((Integer) datos[6]).intValue());
        jTextField2.setText(datos[7].toString());
        dlm = new DefaultListModel();
        try {
            List<Object[]> users = Constantes.db.getUsuarios();
            for (Object[] o : users) {
                dlm.addElement(o[0].toString());
            }
        } catch (Exception e) {
        }
        String[] dest = datos[8].toString().split(",");
        int[] idx = new int[dest.length];
        int i = 0;
        for (String dst : dest) {
            idx[i] = dlm.indexOf(dst);
            i++;
        }
        jList1.setModel(dlm);
        jList1.setSelectedIndices(idx);
        buttonColor1.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("foreground")) {
                    jTextArea1.setBackground(buttonColor1.getColor());
                    updateDB();
                }
            }
        });

        buttonColor2.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("foreground")) {
                    jTextArea1.setForeground(buttonColor2.getColor());
                    updateDB();
                }
            }
        });
        jCheckBox1.setSelected(datos[5] != null);
        updateProgramar();
        updateActive = true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        buttonColor1 = new org.fabnun.senu.vista.ButtonColor();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonColor2 = new org.fabnun.senu.vista.ButtonColor();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Comic Sans MS", 0, 18));
        jTextArea1.setRows(5);
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea1FocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea1);

        add(jScrollPane2);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buttonColor1.setText("buttonColor1");

        jLabel1.setText("Color de Fondo");

        jLabel2.setText("Color del texto");

        buttonColor2.setText("buttonColor1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "nunca", "diario", "semanal", "mensual", "anual", "cada 12 horas", "cada 6 horas", "cada 3 horas", "cada 2 horas", "cada 1 horas", " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText("  Repetir");

        jDateChooser1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jDateChooser1FocusLost(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Destinatarios"));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel6.setText("Patron de repeticion");

        jTextField2.setText("1");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });

        jCheckBox1.setText("Programar");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField1.setText("00:00");
        jFormattedTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jFormattedTextField1CaretUpdate(evt);
            }
        });

        jLabel4.setText("Inicio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(buttonColor2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonColor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2)
                    .addComponent(jComboBox1)
                    .addComponent(jFormattedTextField1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel6))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusLost
        updateDB();
    }//GEN-LAST:event_jTextArea1FocusLost

    private void jDateChooser1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jDateChooser1FocusLost
        updateDB();
    }//GEN-LAST:event_jDateChooser1FocusLost

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        updateDB();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        updateDB();
    }//GEN-LAST:event_jTextField2FocusLost

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        updateDB();
    }//GEN-LAST:event_jList1ValueChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        updateProgramar();
        updateDB();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jFormattedTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jFormattedTextField1CaretUpdate
        updateDB();
    }//GEN-LAST:event_jFormattedTextField1CaretUpdate

    public void setConfigVisible(boolean visible) {
        jPanel1.setVisible(visible);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.fabnun.senu.vista.ButtonColor buttonColor1;
    private org.fabnun.senu.vista.ButtonColor buttonColor2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
