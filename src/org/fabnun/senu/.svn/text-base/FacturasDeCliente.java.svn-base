package org.fabnun.senu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.fabnun.senu.graficos.GraficoHistorialCliente;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.TextFormater;

public class FacturasDeCliente extends javax.swing.JPanel implements FocusInterface {

    Factura factura;
    double totales = 0, deudas = 0, abonos = 0;
    private int deuda = 0;
    private int nfact = -1;
    int countCanceladas = 0;
    int countAnuladas = 0;
    int countFacturas = 0;
    int rut = 0;

    public void KeyPressed(int keyCode, int modif) {
    }

    private void updateSelectedTotales() {
        int[] rows = jTable1.getSelectedRows();
        int idx;
        double totalDeuda = 0;
        double totalFacturado = 0;
        double totalAbono = 0;
        for (int i : rows) {
            idx = jTable1.convertRowIndexToModel(i);
            totalDeuda = totalDeuda + (Double) model.getValueAt(idx, 4);
            try {
                totalAbono = totalAbono + (Integer) model.getValueAt(idx, 3);
            } catch (Exception e) {
                totalAbono = totalAbono + (Double) model.getValueAt(idx, 3);
            }
            totalFacturado = totalFacturado + (Double) model.getValueAt(idx, 2);
        }
        jLabel9.setText("Selección ( Total: $" + Constantes.decimalFormat.format(totalFacturado)
                + ", Abono: $" + Constantes.decimalFormat.format(totalAbono)
                + ", Deuda: $" + Constantes.decimalFormat.format(totalDeuda) + " )");

    }

    public FacturasDeCliente(String rut, Factura factura, boolean abonar, boolean todas) {
        this.rut = Integer.parseInt(rut);
        this.factura = factura;
        initComponents();
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updateSelectedTotales();
            }
        });


        jButton1.setEnabled(abonar);
        jTextField1.setEnabled(abonar);
        jTable1.setModel(model);
        jTable2.setModel(model2);
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updateAbonos();
            }
        });
        int fact;
        Date fecha;
        double total, abono, dd;
        boolean cancel, anul;
        String cpago;
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        try {

            LinkedList<ObjectArray> list = Constantes.db.getFacturasCliente(rut, todas);
            countFacturas = list.size();
            Object[] objs;
            StringBuilder sb = new StringBuilder();
            for (ObjectArray o : list) {
                objs = o.objects;
                fact = ((Integer) objs[0]).intValue();
                sb.append(fact).append(",");
                fecha = ((Date) objs[1]);
                total = ((BigDecimal) objs[2]).doubleValue();
                if (objs[3] != null) {
                    try {
                        abono = ((Integer) objs[3]).doubleValue();
                    } catch (Exception e) {
                        abono = ((BigDecimal) objs[3]).doubleValue();
                    }

                } else {
                    abono = 0;
                }
                dd = total - abono;
                cancel = ((Integer) objs[4]).intValue() == 1;
                anul = ((Integer) objs[6]).intValue() == 1;
                if (cancel) {
                    countCanceladas++;
                }
                if (anul) {
                    countAnuladas++;
                }
                int idxCPago = ((Integer) objs[7]).intValue();
                if (idxCPago == 1) {
                    cpago = "CONTADO";
                } else if (idxCPago == 2) {
                    cpago = "CHEQUE 30";
                } else {
                    cpago = "CHEQUE 60";
                }
                if (cancel || anul) {
                    dd = 0;
                }
                model.addRow(new Object[]{fact, fecha, total, abono, dd, cancel, anul, cpago});
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
                ResultSet rs = Constantes.db.executeQuery(
                        "select nfact, sum(abono) from abono where nfact in ("
                        + sb.toString() + ") group by nfact");
                HashMap<Integer, Long> map = new HashMap<Integer, Long>();
                while (rs.next()) {
                    map.put(rs.getInt(1), rs.getLong(2));
                }
                Constantes.db.closeResultSetAndStatement(rs);
                Long value;
                for (int i = 0; i < model.getRowCount(); i++) {
                    fact = (Integer) model.getValueAt(i, 0);
                    value = map.get(fact);
                    if (value != null) {
                        model.setValueAt(((Long) value).doubleValue(), i, 3);
                    }
                    total = (Double) model.getValueAt(i, 2);
                    abono = (Double) model.getValueAt(i, 3);
                    anul = (Boolean) model.getValueAt(i, 6);
                    cancel = (Boolean) model.getValueAt(i, 5);
                    if (anul || cancel) {
                        dd = 0;
                    } else {
                        dd = total - abono;
                    }
                    model.setValueAt(dd, i, 4);

                    if (!anul) {
                        totales = totales + total;
                        deudas = deudas + dd;
                        abonos = abonos + abono;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        abonos = totales - deudas;
        double factor = 100d * deudas / totales;
        jLabel2.setText("Facturado: $" + Constantes.decimalFormat.format(totales)
                + ", Deuda: $" + Constantes.decimalFormat.format(deudas)
                + " (" + Constantes.decimalFormat.format(factor) + "%)");
        jTable1.getTableHeader().setReorderingAllowed(false);
        TableColumnModel tcm = jTable1.getColumnModel();
        TableColumnModel tcm2 = jTable2.getColumnModel();
        DefaultTableCellRenderer montoRender = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, Constantes.decimalFormat.format(value), isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                return this;
            }
        };

        DefaultTableCellRenderer dateRender = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(sdf.format(value));
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };
        DefaultTableCellRenderer dateRender2 = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(sdf.format(value));
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };
        jTextField1.setDocument(new TextFormater(14, true, "\\d|\\.|,", Constantes.decimalFormat));

        tcm.getColumn(0).setCellRenderer(montoRender);
        tcm.getColumn(1).setCellRenderer(dateRender);
        tcm.getColumn(2).setCellRenderer(montoRender);
        tcm.getColumn(3).setCellRenderer(montoRender);
        tcm2.getColumn(0).setCellRenderer(dateRender2);
        tcm2.getColumn(1).setCellRenderer(montoRender);
        tcm.getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, Constantes.decimalFormat.format(value), isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                if (((Double) value).doubleValue() > 0) {
                    setForeground(Color.red);
                } else {
                    setForeground(Color.black);
                }
                return this;
            }
        });

        tcm2.getColumn(0).setMinWidth(120);
        tcm2.getColumn(0).setMaxWidth(120);
        tcm.getColumn(0).setMinWidth(75);
        tcm.getColumn(0).setMaxWidth(75);
        tcm.getColumn(1).setMinWidth(65);
        tcm.getColumn(1).setMaxWidth(65);
        tcm.getColumn(2).setMinWidth(75);
        tcm.getColumn(2).setMaxWidth(75);
        tcm.getColumn(3).setMinWidth(75);
        tcm.getColumn(3).setMaxWidth(75);
        tcm.getColumn(4).setMinWidth(75);
        tcm.getColumn(4).setMaxWidth(75);
        tcm.getColumn(5).setMinWidth(85);
        tcm.getColumn(5).setMaxWidth(85);
        tcm.getColumn(6).setMinWidth(75);
        tcm.getColumn(6).setMaxWidth(75);

        try {
            repaintGrafico();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    DefaultTableModel model2 = new DefaultTableModel(new String[]{"Fecha", "Monto"}, 0) {

        Class[] classes = new Class[]{Date.class, Double.class};

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex];
        }
    };
    DefaultTableModel model = new DefaultTableModel(new String[]{" Factura", "Fecha", "   Total", "   Abono", "   Deuda", "Cancelada", "Anulada", "Pago"}, 0) {

        Class[] classes = new Class[]{Integer.class, Date.class, Double.class, Double.class, Double.class, Boolean.class, Boolean.class, String.class};

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex];
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setFocusable(false);

        jLabel2.setText(" ");
        jLabel2.setFocusable(false);

        jSplitPane1.setDividerLocation(145);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane1.setFocusable(false);

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
        jTable1.setNextFocusableComponent(jTextField1);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSplitPane1.setTopComponent(jScrollPane1);

        jPanel1.setFocusable(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(670, 50));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Abonos"));
        jScrollPane2.setFocusable(false);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setFocusable(false);
        jScrollPane2.setViewportView(jTable2);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jLabel8.setText("Monto");
        jLabel8.setFocusable(false);

        jButton1.setText("Abonar");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Historial del Cliente"));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jScrollPane3.setViewportView(jLabel6);

        jLabel9.setMaximumSize(new java.awt.Dimension(300, 16));
        jLabel9.setMinimumSize(new java.awt.Dimension(300, 16));
        jLabel9.setPreferredSize(new java.awt.Dimension(300, 16));

        jButton2.setText("Abonar selección");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jLabel8))
                    .addComponent(jButton2)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    public boolean clicked = false;
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2 && factura != null) {
            try {
                int idx = jTable1.getSelectedRow();
                System.out.println(idx);
                idx = jTable1.convertRowIndexToModel(idx);
                setVisible(false);
                factura.cargar(((Integer) model.getValueAt(idx, 0)).intValue());
                clicked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked
    private boolean cancelada = false;
    private boolean anulada = false;

    private void updateAbonos() {
        try {
            int idx = jTable1.getSelectedRow();
            if (idx > -1) {
                while (model2.getRowCount() > 0) {
                    model2.removeRow(0);
                }
                idx = jTable1.convertRowIndexToModel(idx);
                if (idx > -1) {
                    deuda = ((Double) model.getValueAt(idx, 4)).intValue();
                    nfact = ((Integer) model.getValueAt(idx, 0)).intValue();
                    cancelada = (Boolean) model.getValueAt(idx, 5);
                    anulada = (Boolean) model.getValueAt(idx, 6);
                    for (ObjectArray o : Constantes.db.getAbonos(nfact)) {
                        model2.addRow(new Object[]{o.objects[1], o.objects[2]});
                    }
                    jTextField1.setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String text = jTextField1.getText().trim();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Ingrese el monto a abonar");
            return;
        }
        try {
            abonar(nfact, Constantes.decimalFormat.parse(text).intValue(), cancelada, anulada);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String text = jTextField1.getText().trim();
        if (text.length() == 0) {
            JOptionPane.showMessageDialog(this, "Ingrese el monto a abonar");
            return;
        }
        try {
            abonar(nfact, Constantes.decimalFormat.parse(text).intValue(), cancelada, anulada);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        int keycode = evt.getKeyCode();
        if (keycode == KeyEvent.VK_TAB) {
            evt.consume();
            jTextField1.requestFocus();
        } else if (keycode == KeyEvent.VK_ENTER && factura != null) {
            evt.consume();
            try {
                int idx = jTable1.getSelectedRow();
                idx = jTable1.convertRowIndexToModel(idx);
                setVisible(false);
                factura.cargar(((Integer) model.getValueAt(idx, 0)).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (keycode == KeyEvent.VK_ESCAPE) {
            setVisible(false);
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setVisible(false);
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Desea cancelar las facturas seleccionadas?", "Abonar Facturas", JOptionPane.YES_NO_OPTION) == 0) {
            int[] rows = jTable1.getSelectedRows();
            int row;
            for (int i : rows) {
                jTable1.setRowSelectionInterval(i, i);
                row = jTable1.convertRowIndexToModel(i);
                deuda = ((Double) model.getValueAt(row, 4)).intValue();
                nfact = ((Integer) model.getValueAt(row, 0)).intValue();
                cancelada = (Boolean) model.getValueAt(row, 5);
                anulada = (Boolean) model.getValueAt(row, 6);
                if (!anulada && !cancelada && deuda > 0 && nfact > 0) {
                    abonar(nfact, deuda, cancelada, anulada);
                }
            }
            updateSelectedTotales();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    public Component[][] getFocus() {
        return null;
    }

    private void abonar(int nfact, int monto, boolean cancelado, boolean anulada) {
        int idx = jTable1.getSelectedRow();
        if (anulada) {
            JOptionPane.showMessageDialog(this, "No puede abonar una factura anulada");
        } else if (cancelado) {
            JOptionPane.showMessageDialog(this, "No puede abonar una factura cancelada");
        } else if (nfact == -1 || idx == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la factura que desea abonar");
        } else if (monto > deuda) {
            JOptionPane.showMessageDialog(this, "El monto no puede superior a la deuda de $" + Constantes.decimalFormat.format(deuda));
        } else {
            try {
                if (model.getValueAt(idx, 6) == Boolean.FALSE) {
                    boolean cancel = Constantes.db.abonar(nfact, monto);
                    if (cancel) {
                        countCanceladas++;
                    }
                    updateAbonos();
                    Object[] result = Constantes.db.getFacturaCliente(nfact);
                    double total = ((BigDecimal) result[0]).doubleValue();
                    double abs = ((BigDecimal) result[1]).doubleValue();
                    model.setValueAt(total - abs, idx, 4);
                    model.setValueAt(abs, idx, 3);
                    if (cancel) {
                        model.setValueAt(true, idx, 5);
                    }
                    abonos = abonos + monto;
                    deudas = deudas - monto;
                    abonos = totales - deudas;
                    double factor = 100d * deudas / totales;
                    jLabel2.setText("Facturado: $" + Constantes.decimalFormat.format(totales)
                            + ", Deuda: $" + Constantes.decimalFormat.format(deudas)
                            + " (" + Constantes.decimalFormat.format(factor) + "%)");
                    int[] selected = jTable1.getSelectedRows();
                    if (selected.length > 1) {
                        jTable1.setRowSelectionInterval(selected[1], selected[selected.length - 1]);
                    } else {
                        jTable1.getSelectionModel().clearSelection();
                    }
                    jTextField1.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(this, "No puede abonar una factura anulada");
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        updateSelectedTotales();
        try {
            repaintGrafico();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void repaintGrafico() throws SQLException {
        final List<Object[]> data = Constantes.db.getHistoria(rut);
        if (data.size() > 0) {

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    GraficoHistorialCliente gr = new GraficoHistorialCliente();
                    gr.setData(data);
                    jLabel6.setIcon(new ImageIcon(gr.paint()));
                    Point p = new Point(Integer.MAX_VALUE, 0);
                    jScrollPane3.getViewport().setViewPosition(p);
                    jPanel1.repaint();
                    jPanel1.updateUI();
                }
            });
        }
    }

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

    public void select(int nfact) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (nfact == (Integer) model.getValueAt(i, 0)) {
                int idx = jTable1.convertRowIndexToView(i);
                jTable1.setRowSelectionInterval(idx, idx);
                ensureRowIsVisible(jTable1, idx);
                break;
            }
        }

    }
}
