/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Clientes.java
 *
 * Created on Apr 4, 2011, 12:45:03 PM
 */
package org.fabnun.senu;

import java.awt.Component;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.fabnun.senu.vista.AutoCompleter;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.TextFormater;

/**
 *
 * @author fabnun
 */
public class Clientes extends javax.swing.JPanel implements FocusInterface {

    private TextFormater formaterComuna = new TextFormater(25, true);
    private TextFormater formaterCiudad = new TextFormater(25, true);
    private TextFormater formaterGiro = new TextFormater(30, true);
    private TextFormater formaterNombre = new TextFormater(70, true);
    private TextFormater formaterFono = new TextFormater(15, true, "\\d| |-|\\(|\\)|\\.");
    private TextFormater formaterDireccion = new TextFormater(40, true);
    private SenuEscritorio escritorio;
    private boolean abonar;

    /** Creates new form Clientes */
    public Clientes(SenuEscritorio escritorio, boolean abonar, String nombre) {
        this.escritorio = escritorio;
        this.abonar = abonar;
        initComponents();
        jTable1.setModel(dtm);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(0).setMinWidth(100);
        jTable1.getColumnModel().getColumn(0).setWidth(100);
        jTextField9.setDocument(new TextFormater(40, true));
        jTextField8.setDocument(new TextFormater(9, true, "\\d"));
        jTextField3.setDocument(formaterNombre);
        jTextField13.setDocument(formaterFono);
        jTextField7.setDocument(formaterDireccion);
        jTextField4.setDocument(formaterComuna);
        jTextField5.setDocument(formaterCiudad);
        jTextField6.setDocument(formaterGiro);
        new CiudadAutoCompleter(jTextField5);
        new ComunaAutoCompleter(jTextField4);
        new GiroAutoCompleter(jTextField6);
        updateList();
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                AutoCompleter.blockOther(true);
                try {
                    if (lista != null) {
                        int idx = jTable1.getSelectedRow();
                        ObjectArray oa = lista.get(idx);
                        jTextField1.setText("" + oa.objects[1]);
                        jTextField2.setText("" + dv(Integer.parseInt("" + oa.objects[1])));
                        jTextField3.setText("" + oa.objects[0]);
                        jTextField13.setText("" + oa.objects[3]);
                        jTextField7.setText("" + oa.objects[2]);
                        try {
                            jTextField4.setText(Constantes.db.getComuna(Integer.parseInt((String) oa.objects[6])));
                            jTextField5.setText(Constantes.db.getCiudad(Integer.parseInt((String) oa.objects[5])));
                            jTextField6.setText(Constantes.db.getGiro(Integer.parseInt((String) oa.objects[7])));
                        } catch (Exception ex) {
                            jTextField4.setText(Constantes.db.getComuna((Integer) oa.objects[6]));
                            jTextField5.setText(Constantes.db.getCiudad((Integer) oa.objects[5]));
                            jTextField6.setText(Constantes.db.getGiro((Integer) oa.objects[7]));
                        }
                        jButton1.setEnabled(true);
                        jButton2.setEnabled(true);
                        jButton3.setEnabled(true);
                        jButton4.setEnabled(true);
                    }
                } catch (Exception ex) {
                }
                AutoCompleter.blockOther(false);
            }
        });
        if (nombre != null) {
            {
                jTextField9.setText(nombre);
                jTable1.setRowSelectionInterval(0, 0);
            }
        }
    }
    DefaultTableModel dtm = new DefaultTableModel(new Object[]{"RUT", "Nombre"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    LinkedList<ObjectArray> lista = null;
    private String oldRut = "";

    private void updateList2() {
        AutoCompleter.blockOther(true);
        String rutfilter = jTextField8.getText().trim();
        if (!oldRut.equals(rutfilter)) {
            try {
                lista = Constantes.db.buscaRut(rutfilter);
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                for (ObjectArray o : lista) {
                    dtm.addRow(new Object[]{o.objects[1], o.objects[0]});
                }
                jTable1.getSelectionModel().clearSelection();
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");
                jTextField6.setText("");
                jTextField7.setText("");
                jTextField13.setText("");
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jButton3.setEnabled(false);
                jButton4.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oldRut = rutfilter;
        AutoCompleter.blockOther(false);
        jTextField8.requestFocus();
    }
    private String oldNom = "";

    private void updateList() {
        AutoCompleter.blockOther(true);
        String nomfilter = jTextField9.getText().trim();
        if (!nomfilter.equals(oldNom)) {
            try {
                lista = Constantes.db.buscaCliente(nomfilter);
                while (dtm.getRowCount() > 0) {
                    dtm.removeRow(0);
                }
                for (ObjectArray o : lista) {
                    dtm.addRow(new Object[]{o.objects[1], o.objects[0]});
                }
                jTable1.getSelectionModel().clearSelection();
                jTextField1.setText("");
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jTextField5.setText("");
                jTextField6.setText("");
                jTextField7.setText("");
                jTextField13.setText("");
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jButton3.setEnabled(false);
                jButton4.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oldNom = nomfilter;
        AutoCompleter.blockOther(false);
        jTextField9.requestFocus();
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
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();

        jLabel1.setText("Rut");
        jLabel1.setFocusable(false);

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setToolTipText("Rut del cliente");
        jTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField1CaretUpdate(evt);
            }
        });
        jTextField1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextField1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField1InputMethodTextChanged(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText(" ");
        jTextField2.setToolTipText("Digito verificador");
        jTextField2.setFocusable(false);

        jLabel2.setText("Nombre");
        jLabel2.setFocusable(false);

        jTextField3.setToolTipText("Nombre del cliente");

        jLabel20.setText("Fono");

        jTextField13.setToolTipText("Telefono del cliente");

        jTextField5.setToolTipText("Ciudad del cliente");

        jLabel4.setText("Ciudad");
        jLabel4.setFocusable(false);

        jTextField4.setToolTipText("Comuna del cliente");

        jLabel3.setText("Comuna");
        jLabel3.setFocusable(false);

        jTextField7.setToolTipText("Direccion del cliente");

        jLabel13.setText("Dirección");
        jLabel13.setFocusable(false);

        jLabel5.setText("Giro");
        jLabel5.setFocusable(false);

        jTextField6.setToolTipText("Giro del cliente");

        jButton1.setText("Modificar datos");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Ver todas las facturas");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Ver facturas sin cancelar");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );

        jButton4.setText("Modificar rut");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Filtrar por rut");
        jLabel6.setFocusable(false);

        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.setToolTipText("Rut del cliente");
        jTextField8.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField8CaretUpdate(evt);
            }
        });
        jTextField8.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextField8CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField8InputMethodTextChanged(evt);
            }
        });

        jLabel7.setText("Filtrar por nombre");
        jLabel7.setFocusable(false);

        jTextField9.setToolTipText("Nombre del cliente");
        jTextField9.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField9CaretUpdate(evt);
            }
        });
        jTextField9.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextField9CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField9InputMethodTextChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                                .addComponent(jButton1)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private String dv(int valor) {
        int M = 0, S = 1, T = valor;
        for (; T != 0; T /= 10) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        }
        return "" + (char) (S != 0 ? S + 47 : 75);
    }

    private void updateDV() {
        try {
            jTextField2.setText(dv(Integer.parseInt(jTextField1.getText().replaceAll("\\.", ""))));
        } catch (Exception e) {
            jTextField2.setText("");
        }
    }

    private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
        updateDV();
}//GEN-LAST:event_jTextField1CaretUpdate

    private void jTextField1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField1CaretPositionChanged
        updateDV();
}//GEN-LAST:event_jTextField1CaretPositionChanged

    private void jTextField1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField1InputMethodTextChanged
        updateDV();
}//GEN-LAST:event_jTextField1InputMethodTextChanged

    private void jTextField8CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField8CaretUpdate
        updateList2();
    }//GEN-LAST:event_jTextField8CaretUpdate

    private void jTextField8CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField8CaretPositionChanged
        updateList2();
    }//GEN-LAST:event_jTextField8CaretPositionChanged

    private void jTextField8InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField8InputMethodTextChanged
        updateList2();
    }//GEN-LAST:event_jTextField8InputMethodTextChanged

    private void jTextField9CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField9CaretUpdate
        updateList();
    }//GEN-LAST:event_jTextField9CaretUpdate

    private void jTextField9CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField9CaretPositionChanged
        updateList();
    }//GEN-LAST:event_jTextField9CaretPositionChanged

    private void jTextField9InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField9InputMethodTextChanged
        updateList();
    }//GEN-LAST:event_jTextField9InputMethodTextChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int rut = new Integer(jTextField1.getText().replaceAll("\\.", ""));
        String nombre = jTextField3.getText().trim();
        String dir = jTextField7.getText().trim();
        String comuna = jTextField4.getText().trim();
        String ciudad = jTextField5.getText().trim();
        String fono = jTextField13.getText().trim();
        String giro = jTextField6.getText().trim();
        try {
            Constantes.db.updateCliente(rut, nombre, fono, dir, comuna, ciudad, giro);
            int idx = jTable1.getSelectedRow();
            lista.set(idx, Constantes.db.getCliente(rut));
            dtm.setValueAt(nombre, idx, 1);
            JOptionPane.showMessageDialog(this, "Se han modificado los datos del cliente " + rut);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al modificar los datos del cliente " + rut, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String rut = jTextField1.getText();
        if (rut.length() > 0) {
            String r = JOptionPane.showInputDialog(this, "Ingrese el Rut que remplazara a " + rut + "?");
            if (r != null) {
                int nr = 0;
                try {
                    nr = Integer.parseInt(r);
                    if (nr < 1) {
                        JOptionPane.showMessageDialog(this, "Error: el rut debe ser mayor que cero", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (nr == Integer.parseInt(rut)) {
                        JOptionPane.showMessageDialog(this, "Error: el rut ser distinto al que se modificara", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (Constantes.db.getCliente(nr) == null) {
                            try {
                                int idx = jTable1.getSelectedRow();
                                Constantes.db.cambiarRut(Integer.parseInt(rut), nr);
                                dtm.setValueAt(nr, idx, 0);
                                lista.get(idx).objects[1] = nr;
                                jTextField1.setText("" + nr);
                                jTextField2.setText(dv(nr));
                                JOptionPane.showMessageDialog(this, "El rut " + rut + " ha sido reemplazado por " + nr);
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(this, "Error: al modificar rut", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: el rut ingresado ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: formato incorrecto del numero de rut", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void viewFacturas(boolean all) {
        String rut = jTextField1.getText().trim().replaceAll("\\.", "");
        FacturasDeCliente facts;
        if (rut.length() > 0) {
            facts = new FacturasDeCliente(rut, null, abonar, all);
            facts.clicked = false;
            escritorio.showDialog(facts, "facturas del rut " + jTextField1.getText() + "-" + jTextField2.getText(), Constantes.ICON_FACTURA, true, true);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        viewFacturas(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        viewFacturas(false);
    }//GEN-LAST:event_jButton3ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    public Component[][] getFocus() {
        return null;
    }

    public void KeyPressed(int keyCode, int modif) {
    }
}
