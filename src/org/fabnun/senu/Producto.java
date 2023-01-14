/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Productos.java
 *
 * Created on Jan 28, 2011, 5:13:30 PM
 */
package org.fabnun.senu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.TextFormater;

/**
 *
 * @author fabnun
 */
public class Producto extends javax.swing.JPanel implements FocusInterface {

    public void KeyPressed(int keyCode, int modif) {
    }
    DefaultTableModel dtm = new DefaultTableModel(new String[]{"Codigo", "Detalle", "Precio"}, 0) {

        Class[] classes = new Class[]{String.class, String.class, String.class};

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex];
        }
    };
    private JTextField text0 = new JTextField();
    private JTextField text1 = new JTextField();
    private JTextField text2 = new JTextField();

    /** Creates new form Productos */
    public Producto() {
        initComponents();
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setModel(dtm);
        TableColumnModel tcm = jTable1.getColumnModel();
        tcm.getColumn(0).setMinWidth(80);
        tcm.getColumn(0).setMaxWidth(80);
        tcm.getColumn(0).setWidth(80);
        tcm.getColumn(2).setMinWidth(80);
        tcm.getColumn(2).setMaxWidth(80);
        tcm.getColumn(2).setWidth(80);
        updateProductos();
        jTextField2.setDocument(new TextFormater(80, true));

        final Font font = jTable1.getFont();
        text0.setFont(font);
        text1.setFont(font);
        text2.setFont(font);
        text2.setHorizontalAlignment(JTextField.RIGHT);
        text0.setDocument(new TextFormater(8, true));
        text1.setDocument(new TextFormater(60, true));
        text2.setDocument(new TextFormater(12, true, "\\d|\\."));
        jTable1.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(text0));
        jTable1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(text1));
        jTable1.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(text2));
        final Producto este = this;
        TableCellRenderer render = new TableCellRenderer() {

            private JTextField text = new JTextField();
            private boolean set = false;

            private void set() {
                text.setFont(font);
                set = true;
            }
            private final Color changeColor = new Color(0, 255, 0);
            private final Color newColor = new Color(0, 0, 255);

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                synchronized (este) {
                    if (!set) {
                        set();
                    }
                    if (column == 2) {
                        text.setHorizontalAlignment(JTextField.RIGHT);
                    } else {
                        text.setHorizontalAlignment(JTextField.LEFT);
                    }
                    if (isSelected) {
                        text.setBackground(Color.lightGray);
                    } else {
                        text.setBackground(Color.white);
                    }
                    Color col = null;
                    if (row >= codigos.length) {
                        try {
                            if (column == 0 && Constantes.db.existProducto(value.toString().trim())) {
                                col = Color.red;
                            } else {
                                col = newColor;
                            }
                        } catch (Exception e) {
                            col = newColor;
                        }
                    } else {

                        String val = value.toString().trim();
                        try {
                            switch (column) {
                                case 0:
                                    try {
                                        if (!val.equals(codigos[row]) && Constantes.db.existProducto(val)) {
                                            col = Color.red;
                                        } else {
                                            col = (val.equals(codigos[row]) ? Color.white : changeColor);
                                        }
                                    } catch (Exception ex) {
                                        col = (val.equals(codigos[row]) ? Color.white : changeColor);
                                    }
                                    break;
                                case 1:
                                    col = (val.equals(detalles[row]) ? Color.white : changeColor);
                                    break;
                                case 2:
                                    col = (val.equals(precios[row]) ? Color.white : changeColor);
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (hasFocus) {
                        col = Color.black;
                    }
                    if (col != null) {
                        text.setBorder(new LineBorder(col, 2));
                    }
                    text.setText(value.toString());
                    return text;
                }
            }
        };

        jTable1.getColumnModel().getColumn(0).setCellRenderer(render);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(render);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(render);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jDialog1.setAlwaysOnTop(true);
        jDialog1.setModal(true);
        jDialog1.setResizable(false);

        jLabel2.setText("Aplicar factor a precios de");

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setFocusable(false);

        jLabel3.setText("%");

        jSlider1.setMinimum(-100);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jButton6.setText("Aceptar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Lucida Console", 0, 12)); // NOI18N
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel1.setText(" Filtrar por detalle ");
        jToolBar1.add(jLabel1);

        jTextField2.setToolTipText("Filtro");
        jTextField2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField2CaretUpdate(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });
        jToolBar1.add(jTextField2);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/reload.png"))); // NOI18N
        jButton7.setToolTipText("Aplicar Filtro");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/productos.png"))); // NOI18N
        jButton1.setText("Nuevo");
        jButton1.setToolTipText("Nuevo producto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/percent.png"))); // NOI18N
        jButton5.setText("Precios");
        jButton5.setToolTipText("Aplica un % a varios productos");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/drive-optical.png"))); // NOI18N
        jButton4.setText("Guardar");
        jButton4.setToolTipText("Guardar las modificaciones");
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/emblem-unreadable.png"))); // NOI18N
        jButton2.setText("Eliminar");
        jButton2.setToolTipText("Elimina varios productos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/reportes.png"))); // NOI18N
        jButton3.setText("Historial");
        jButton3.setEnabled(false);
        jToolBar1.add(jButton3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    String[] codigos = new String[0];
    String[] detalles = new String[0];
    String[] precios = new String[0];

    private void updateProductos() {
        try {
            LinkedList<ObjectArray> lista = Constantes.db.buscaProducto(jTextField2.getText().trim(), new Date(), false);
            codigos = new String[lista.size()];
            detalles = new String[lista.size()];
            precios = new String[lista.size()];
            while (dtm.getRowCount() > 0) {
                dtm.removeRow(0);
            }
            int i = 0;
            for (ObjectArray obj : lista) {
                codigos[i] = (String) obj.objects[1];
                detalles[i] = (String) obj.objects[0];
                precios[i] = obj.objects[2].toString();
                dtm.addRow(new Object[]{obj.objects[1], obj.objects[0], obj.objects[2]});
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String old = "";
    private void jTextField2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField2CaretUpdate
        String nw = jTextField2.getText().trim();
        if (!nw.equals(old)) {
            updateProductos();
        }
        old = nw;
    }//GEN-LAST:event_jTextField2CaretUpdate

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable1.requestFocus();
            jTable1.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private static void ensureRowIsVisible(JTable table, int row) {
        // get the parent viewport
        Component parent = table.getParent();
        while (parent != null && !(parent instanceof JViewport)) {
            parent = parent.getParent();
        }
        Rectangle rec = null;
        int height = table.getRowHeight();
        if (parent == null) {
            // no parent so use 0 and 1 as X and width
            rec = new Rectangle(0, row * height, 1, height);
        } else {
            // use the X pos and width of the current viewing rectangle
            rec = ((JViewport) parent).getViewRect();
            rec.y = row * height;
            rec.height = height;
        }
        table.scrollRectToVisible(rec);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        synchronized (this) {
            jTable1.setAutoscrolls(true);
            dtm.addRow(new Object[]{"", "", "0.0"});
            int size = dtm.getRowCount();
            ensureRowIsVisible(jTable1, size - 1);
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTable1.editingStopped(null);
        if (JOptionPane.showConfirmDialog(this, "Desea guardar los cambios?", "Guardar", JOptionPane.YES_NO_OPTION) == 0) {
            synchronized (this) {
                String codigo = "", detalle;
                String precio;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < dtm.getRowCount(); i++) {
                    codigo = (String) dtm.getValueAt(i, 0);
                    detalle = (String) dtm.getValueAt(i, 1);
                    precio = dtm.getValueAt(i, 2).toString();
                    String fecha = sdf.format(new Date());
                    if (i >= codigos.length) {
                        sb.append("insert into producto (codigo, detalle) values ('").append(codigo).append("','").append(detalle).append("');\n");
                        sb.append("insert into precio (codigo, fecha, precio) values ('").append(codigo).append("','").append(fecha).append("',").append(precio).append(");\n");
                        //Constantes.db.insertProducto(codigo, detalle, new BigDecimal(precio), new Timestamp(new Date().getTime()));
                    } else {
                        if (!codigo.equals(codigos[i])) {
                            sb.append("insert into producto (codigo, detalle) values ('").append(codigo).append("','").append(detalle).append("');\n");
                            sb.append("update precio set codigo='").append(codigo).append("' where codigo='").append(codigos[i]).append("';\n");
                            sb.append("delete from producto where codigo='").append(codigos[i]).append("';\n");
                            //Constantes.db.updateCodigoProducto(codigo, codigos[i]);
                        }
                        if (!detalle.equals(detalles[i])) {
                            sb.append("update producto set detalle='").append(detalle).append("' where codigo='").append(codigo).append("';\n");
                            //Constantes.db.updateDetalleProducto(detalle, codigo);
                        }
                        if (!precio.equals(precios[i])) {
                            sb.append("insert into precio (codigo, fecha, precio) values ('").append(codigo).append("','").append(fecha).append("',").append(precio).append(");\n");
                            //Constantes.db.addPrecioProducto(new Double(precio), codigo);
                        }
                    }

                }
                try {
                    Constantes.db.executeUpdate(sb.toString());
                    int size = dtm.getRowCount();
                    codigos = new String[size];
                    detalles = new String[size];
                    precios = new String[size];
                    for (int i = 0; i < size; i++) {
                        codigos[i] = (String) dtm.getValueAt(i, 0);
                        if (codigos[i] == null) {
                            codigos[i] = "";
                        }
                        detalles[i] = (String) dtm.getValueAt(i, 1);
                        if (detalles[i] == null) {
                            detalles[i] = "";
                        }
                        precios[i] = dtm.getValueAt(i, 2).toString();
                        if (precios[i] == null) {
                            precios[i] = "0.0";
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al realizar cambios.\n" + e.getMessage());
                }
                jTable1.repaint();
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Eliminar productos seleccionados?", "Eliminar Productos", JOptionPane.YES_NO_OPTION) == 0) {
            synchronized (this) {
                int[] idxs = jTable1.getSelectedRows();
                int count = 0;
                for (int i = idxs.length - 1; i >= 0; i--) {
                    try {
                        if (idxs[i] < codigos.length) {
                            count++;
                            String codigo = (String) dtm.getValueAt(idxs[i], 0);
                            Constantes.db.eliminarProducto(codigo);
                            dtm.removeRow(idxs[i]);
                            for (int j = idxs[i]; j < codigos.length - 1; j++) {
                                codigos[j] = codigos[j + 1];
                                detalles[j] = detalles[j + 1];
                                precios[j] = precios[j + 1];
                            }

                        } else {
                            dtm.removeRow(idxs[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                codigos = Arrays.copyOf(codigos, codigos.length - count);
                detalles = Arrays.copyOf(detalles, detalles.length - count);
                precios = Arrays.copyOf(precios, precios.length - count);
                jTable1.repaint();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (jTable1.getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero los productos");
        } else {
            jSlider1.setValue(0);
            jDialog1.pack();
            jDialog1.setLocationRelativeTo(this);
            jDialog1.setVisible(true);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        double porcent = ((double) jSlider1.getValue() / 2d);
        jTextField1.setText("" + porcent);
    }//GEN-LAST:event_jSlider1StateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        double porcent = ((double) jSlider1.getValue() / 2d);
        for (int idx : jTable1.getSelectedRows()) {
            String value = dtm.getValueAt(idx, 2).toString();
            try {
                double val = new Double(value);
                val = val + val * porcent / 100d;
                dtm.setValueAt(val, idx, 2);
            } catch (Exception e) {
            }
        }
        jDialog1.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String nw = jTextField2.getText().trim();
        updateProductos();
        old = nw;
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        String nw = jTextField2.getText().trim();
        updateProductos();
        old = nw;
    }//GEN-LAST:event_jTextField2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    public Component[][] getFocus() {
        return null;
    }
}
