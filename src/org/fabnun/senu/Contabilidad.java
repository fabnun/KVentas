/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Compras.java
 *
 * Created on Mar 14, 2011, 12:33:36 AM
 */
package org.fabnun.senu;

import java.awt.event.KeyEvent;
import java.io.File;
import jxl.*;
import jxl.write.*;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.fabnun.senu.vista.MyCellEditor;
import org.fabnun.senu.vista.MyCellRender;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.TextFormater;

/**
 *
 * @author fabnun
 */
public class Contabilidad extends javax.swing.JPanel implements FocusInterface {

    public void KeyPressed(int keyCode, int modif) {
    }
    ArrayList<Object[]> oldData = new ArrayList<Object[]>();
    private static Class[] classes2 = new Class[]{Integer.class, Integer.class, Date.class, String.class, String.class, String.class, Double.class, Double.class, Double.class};
    DefaultTableModel dtm2 = new DefaultTableModel(new Object[]{"Periodo", "#", "$Facturado", "$Cancelado", "$Abonado", "$Deuda", "$Egresos", "$Suma"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private static Class[] classes = new Class[]{Integer.class, Date.class, Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class};
    DefaultTableModel dtm = new DefaultTableModel(new Object[]{"#", "Fecha", "N°Docto.", "Razón Social", "Detalle", "Otros", "NETO", "IVA", "TOTAL"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return column > 0;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex];
        }
    };
    private int nextSaved = 0;
    private int next = 0;

    private void loadData() {
        String s0 = "%" + jTextField1.getText().trim() + "%";
        String s1 = "%" + jTextField2.getText().trim() + "%";
        String s2 = "%" + jTextField3.getText().trim() + "%";
        try {
            List<Object[]> data = Constantes.db.getContabilidad(s0, s1, s2);
            int size = dtm.getRowCount();
            oldData = new ArrayList<Object[]>();
            for (int i = 0; i < size; i++) {
                dtm.removeRow(0);
            }
            for (Object[] o : data) {
                o[2] = ((BigDecimal) o[2]).intValue();
                o[6] = ((BigDecimal) o[6]).intValue();
                o[7] = ((BigDecimal) o[7]).intValue();
                o[8] = ((BigDecimal) o[8]).intValue();
                oldData.add(o);
                dtm.addRow(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateNexts();
    }

    private void updateNexts() {
        try {
            next = Constantes.db.getNextIDCompra();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nextSaved = next;
    }

    private void recalc() {
        int row = jTable1.getSelectedRow();
        int neto = ((Integer) dtm.getValueAt(row, 6)).intValue();
        int iva = (int) (neto * Constantes.iva);
        int tt = iva + neto;
        dtm.setValueAt(iva, row, 7);
        dtm.setValueAt(tt, row, 8);
    }

    /** Creates new form Compras */
    public Contabilidad(boolean editarEgresos) {
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Archivo Excel .xls";
            }
        });
        updateNexts();
        initComponents();
        if (!editarEgresos) {
            jTabbedPane1.remove(1);
        }
        jTable1.setModel(dtm);
        jTable2.setModel(dtm2);
        jTable3.setModel(dtm3);
        jTable4.setModel(dtm4);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable2.getTableHeader().setReorderingAllowed(false);
        TableColumnModel tcm = jTable1.getColumnModel();
        TableColumnModel tcm3 = jTable3.getColumnModel();
        TableColumnModel tcm4 = jTable4.getColumnModel();
        tcm3.getColumn(0).setMaxWidth(65);
        tcm3.getColumn(0).setMaxWidth(65);
        tcm3.getColumn(1).setMaxWidth(65);
        tcm3.getColumn(1).setMaxWidth(65);
        tcm3.getColumn(2).setMaxWidth(75);
        tcm3.getColumn(2).setMaxWidth(75);
        tcm3.getColumn(4).setMaxWidth(70);
        tcm3.getColumn(4).setMaxWidth(70);
        tcm3.getColumn(5).setMaxWidth(70);
        tcm3.getColumn(5).setMaxWidth(70);
        tcm3.getColumn(6).setMaxWidth(70);
        tcm3.getColumn(6).setMaxWidth(70);
        tcm3.getColumn(0).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm3.getColumn(1).setCellRenderer(new MyCellRender(sdf));
        tcm3.getColumn(2).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm3.getColumn(3).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm3.getColumn(4).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm3.getColumn(5).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm3.getColumn(6).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));

        tcm4.getColumn(0).setMaxWidth(65);
        tcm4.getColumn(0).setMaxWidth(65);
        tcm4.getColumn(1).setMaxWidth(65);
        tcm4.getColumn(1).setMaxWidth(65);
        tcm4.getColumn(4).setMaxWidth(70);
        tcm4.getColumn(4).setMaxWidth(70);
        tcm4.getColumn(5).setMaxWidth(70);
        tcm4.getColumn(5).setMaxWidth(70);
        tcm4.getColumn(0).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm4.getColumn(1).setCellRenderer(new MyCellRender(sdf));
        tcm4.getColumn(2).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm4.getColumn(3).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm4.getColumn(4).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm4.getColumn(5).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));


        tcm.getColumn(0).setMaxWidth(40);
        tcm.getColumn(0).setMinWidth(40);
        tcm.getColumn(1).setMaxWidth(110);
        tcm.getColumn(1).setMinWidth(110);
        tcm.getColumn(2).setMaxWidth(80);
        tcm.getColumn(2).setMinWidth(80);
        tcm.getColumn(6).setMaxWidth(80);
        tcm.getColumn(6).setMinWidth(80);
        tcm.getColumn(7).setMaxWidth(80);
        tcm.getColumn(7).setMinWidth(80);
        tcm.getColumn(8).setMaxWidth(80);
        tcm.getColumn(8).setMinWidth(80);
        tcm.getColumn(0).setCellRenderer(new MyCellRender(MyCellRender.Type.INT));
        tcm.getColumn(1).setCellRenderer(new MyCellRender(sdf));
        tcm.getColumn(1).setCellEditor(new MyCellEditor(sdf));
        tcm.getColumn(3).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm.getColumn(2).setCellRenderer(new MyCellRender(MyCellRender.Type.INT));
        tcm.getColumn(4).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm.getColumn(5).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm.getColumn(6).setCellRenderer(new MyCellRender(MyCellRender.Type.INT));
        tcm.getColumn(7).setCellRenderer(new MyCellRender(MyCellRender.Type.INT));
        tcm.getColumn(8).setCellRenderer(new MyCellRender(MyCellRender.Type.INT));
        /////
        JTextField field = new JTextField();
        field.setDocument(new TextFormater(64, true));
        tcm.getColumn(3).setCellEditor(new MyCellEditor(field));
        new EgresoAutoCompleter(field, 1);

        KeyListener notDotListener = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '.') {
                    e.consume();
                }
            }
        };

        field = new JTextField();
        field.setDocument(new TextFormater(14,true,"\\d|\\."));
        field.addKeyListener(notDotListener);
        tcm.getColumn(2).setCellEditor(new MyCellEditor(field, MyCellEditor.Type.INT));

        field = new JTextField();
        field.setDocument(new TextFormater(64, true));
        tcm.getColumn(4).setCellEditor(new MyCellEditor(field));
        new EgresoAutoCompleter(field, 4);

        field = new JTextField();
        field.setDocument(new TextFormater(64, true));
        tcm.getColumn(5).setCellEditor(new MyCellEditor(field));
        new EgresoAutoCompleter(field, 5);

        field = new JTextField();
        field.setDocument(new TextFormater(14,true,"\\d|\\."));
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                recalc();
            }
        });
        tcm.getColumn(6).setCellEditor(new MyCellEditor(field, MyCellEditor.Type.INT));

        field = new JTextField();
        field.setDocument(new TextFormater(14,true,"\\d|\\."));
        tcm.getColumn(7).setCellEditor(new MyCellEditor(field, MyCellEditor.Type.INT));

        field = new JTextField();
        field.setDocument(new TextFormater(14,true,"\\d|\\."));
        tcm.getColumn(8).setCellEditor(new MyCellEditor(field, MyCellEditor.Type.INT));

        jTextField1.setDocument(new TextFormater(64, true));
        jTextField2.setDocument(new TextFormater(64, true));
        jTextField3.setDocument(new TextFormater(64, true));

        //////
        jDateChooser1.setLocale(new Locale("es", "cl"));
        jDateChooser1.setDateFormatString("dd-MM-yyyy");
        jDateChooser1.setDate(new Date());
        TableColumnModel tcm2 = jTable2.getColumnModel();

        tcm2.getColumn(0).setCellRenderer(new MyCellRender(MyCellRender.Type.STRING));
        tcm2.getColumn(1).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(2).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(3).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(4).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(5).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(6).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(7).setCellRenderer(new MyCellRender(MyCellRender.Type.DOUBLE));
        tcm2.getColumn(0).setMaxWidth(70);
        tcm2.getColumn(0).setMinWidth(70);
        tcm2.getColumn(1).setMaxWidth(50);
        tcm2.getColumn(1).setMinWidth(50);
        jComboBox2.setModel(model);
        loadData();
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
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jDialog1.setTitle("Exportar Contabilidad");
        jDialog1.setAlwaysOnTop(true);
        jDialog1.setModal(true);

        jLabel6.setText("año/mes");

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel7.setText("IVA:");

        jTextField4.setEditable(false);
        jTextField4.setFocusable(false);

        jTextField5.setEditable(false);
        jTextField5.setFocusable(false);

        jLabel8.setText("+ PPM:");

        jTextField6.setEditable(false);
        jTextField6.setFocusable(false);

        jLabel9.setText("=");

        jButton7.setText("Exportar a Excel");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingresos"));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

        jSplitPane1.setLeftComponent(jScrollPane3);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Egresos"));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTable4);

        jSplitPane1.setRightComponent(jScrollPane4);

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                        .addComponent(jButton7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        jButton4.setText("Exportar Contabilidad");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(jTable2);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Diario (ultimos 3 meses)", "Semanal  (ultimos 6 meses)", "Mensual (ultimos 12 meses)" }));

        jLabel1.setText("Periodo");

        jButton5.setText("Cargar Datos");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setText("Hasta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Contabilidad", jPanel1);

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

        jButton2.setText("Nuevo Egreso");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Eliminar Egreso");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Filtrar razón social");

        jTextField1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField1CaretUpdate(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jTextField2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField2CaretUpdate(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel4.setText("Filtrar detalle");

        jTextField3.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextField3CaretUpdate(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        jLabel5.setText("Filtrar otros");

        jButton3.setText("Guardar Cambios");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 418, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Egresos", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Date now = new Date();
        Object[] o = new Object[]{next, now, 0, "", "", "", 0, 0, 0};
        oldData.add(0, o);
        dtm.insertRow(0, o);
        jTable1.setRowSelectionInterval(0, 0);
        jTable1.setEditingRow(0);
        jTable1.setEditingColumn(2);
        jTable1.requestFocus();
        next++;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void updateContas() {
        try {
            List<Object[]> list = Constantes.db.getContabilidad(jComboBox1.getSelectedIndex() + 1, jDateChooser1.getDate());
            oldData = new ArrayList<Object[]>();
            int size = dtm2.getRowCount();
            for (int i = 0; i < size; i++) {
                dtm2.removeRow(0);
            }
            for (Object[] o : list) {
                oldData.add(o);
                dtm2.addRow(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        updateContas();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int[] selected = jTable1.getSelectedRows();
        if (selected.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selected.length; i++) {
                sb.append(dtm.getValueAt(selected[i], 0));
                sb.append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            try {
                Constantes.db.executeUpdate("delete from compra where id in (" + sb.toString() + ");");
                jTable1.editingStopped(null);
                for (int i = selected.length - 1; i >= 0; i--) {
                    dtm.removeRow(selected[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateNexts();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTable1.editingStopped(null);
        int size = oldData.size();
        for (int i = 0; i < size; i++) {
            Object[] o = oldData.get(i);

            int id = 0;
            if (o[0] instanceof String) {
                id = Integer.parseInt((String) o[0]);
            } else {
                id = (Integer) o[0];
            }
            try {
                if (id > nextSaved - 1) {
                    Constantes.db.insertEgreso(new Object[]{dtm.getValueAt(i, 0),
                                new Timestamp(((Date) dtm.getValueAt(i, 1)).getTime()),
                                dtm.getValueAt(i, 3),
                                dtm.getValueAt(i, 4),
                                dtm.getValueAt(i, 5),
                                dtm.getValueAt(i, 2),
                                dtm.getValueAt(i, 6),
                                dtm.getValueAt(i, 7),
                                dtm.getValueAt(i, 8)});
                } else {
                    if (!o[1].equals(dtm.getValueAt(i, 1))
                            || !o[2].equals(dtm.getValueAt(i, 2))
                            || !o[3].equals(dtm.getValueAt(i, 3))
                            || !o[4].equals(dtm.getValueAt(i, 4))
                            || !o[5].equals(dtm.getValueAt(i, 5))
                            || !o[6].equals(dtm.getValueAt(i, 6))
                            || !o[7].equals(dtm.getValueAt(i, 7))
                            || !o[8].equals(dtm.getValueAt(i, 8))) {
                        Constantes.db.updateEgreso(new Object[]{new Timestamp(((Date) dtm.getValueAt(i, 1)).getTime()),
                                    dtm.getValueAt(i, 3),
                                    dtm.getValueAt(i, 4),
                                    dtm.getValueAt(i, 5),
                                    dtm.getValueAt(i, 2),
                                    dtm.getValueAt(i, 6),
                                    dtm.getValueAt(i, 7),
                                    dtm.getValueAt(i, 8),
                                    dtm.getValueAt(i, 0)});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
        loadData();
    }//GEN-LAST:event_jTextField1CaretUpdate

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        loadData();
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        loadData();
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        loadData();
    }//GEN-LAST:event_jTextField3KeyTyped

    private void jTextField3CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField3CaretUpdate
        loadData();
    }//GEN-LAST:event_jTextField3CaretUpdate

    private void jTextField2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField2CaretUpdate
        loadData();
    }//GEN-LAST:event_jTextField2CaretUpdate
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    boolean updateConta = true;
    DefaultTableModel dtm3 = new DefaultTableModel(new Object[]{"N°", "Fecha", "Rut", "Cliente", "SubTotal", "IVA", "Total"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel dtm4 = new DefaultTableModel(new Object[]{"N°", "Fecha", "Proveedor", "Producto", "IVA", "Total"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private void updateConta() {
        String s = (String) jComboBox2.getSelectedItem();
        int idx = s.indexOf("/");
        int year = Integer.parseInt(s.substring(0, idx));
        int month = Integer.parseInt(s.substring(idx + 1));
        try {
            int ivaIngreso = 0, ivaEgreso = 0, neto = 0;
            List<Object[]> lista1 = Constantes.db.getIngresos(year, month);
            int size = dtm3.getRowCount();
            for (int i = 0; i < size; i++) {
                dtm3.removeRow(0);
            }
            for (Object[] o : lista1) {
                dtm3.addRow(o);
                ivaIngreso = ivaIngreso + ((BigDecimal) o[5]).intValue();
                neto = neto + ((BigDecimal) o[6]).intValue();
            }
            lista1 = Constantes.db.getEgresos(year, month);
            size = dtm4.getRowCount();
            for (int i = 0; i < size; i++) {
                dtm4.removeRow(0);
            }
            for (Object[] o : lista1) {
                dtm4.addRow(o);
                ivaEgreso = ivaEgreso + ((BigDecimal) o[4]).intValue();
            }
            int iva = ivaIngreso - ivaEgreso;
            int ppm = (int) Math.round(neto * Constantes.PPM);
            jTextField4.setText(Constantes.decimalFormat.format(iva));
            jTextField5.setText(Constantes.decimalFormat.format(ppm));
            jTextField6.setText(Constantes.decimalFormat.format(ppm + iva));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        updateConta = false;
        model.removeAllElements();
        Date d = new Date();
        int month = d.getMonth() + 1;
        int year = d.getYear() + 1900;
        for (int i = 2001; i <= year; i++) {
            if (i != year) {
                for (int j = 1; j <= 12; j++) {
                    model.addElement(i + "/" + j);
                }
            } else {
                for (int j = 1; j <= month; j++) {
                    model.addElement(i + "/" + j);
                }
            }
        }
        jComboBox2.setSelectedIndex(model.getSize() - 1);
        updateConta();
        updateConta = true;
        jDialog1.pack();
        jDialog1.setLocationRelativeTo(this);
        jDialog1.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        if (updateConta) {
            updateConta();
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed
    JFileChooser jfc = new JFileChooser();
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            File select = File.createTempFile("contabilidad", ".xls");
            select.deleteOnExit();
            if (!select.getName().toLowerCase().endsWith(".xls")) {
                select = new File(select.getParent(), select.getName() + ".xls");
            }
            WritableWorkbook workbook = Workbook.createWorkbook(select);
            WritableSheet ingresos = workbook.createSheet("Ingresos", 0);
            WritableSheet egresos = workbook.createSheet("Egresos", 1);
            //"N°", "Fecha", "Proveedor", "Producto", "IVA", "Total"
            ingresos.addCell(new Label(0, 0, "N°"));
            ingresos.addCell(new Label(1, 0, "Fecha"));
            ingresos.addCell(new Label(2, 0, "Rut"));
            ingresos.addCell(new Label(3, 0, "Cliente"));
            ingresos.addCell(new Label(4, 0, "SubTotal"));
            ingresos.addCell(new Label(5, 0, "IVA"));
            ingresos.addCell(new Label(6, 0, "Total"));

            egresos.addCell(new Label(0, 0, "N°"));
            egresos.addCell(new Label(1, 0, "Fecha"));
            egresos.addCell(new Label(2, 0, "Proveedor"));
            egresos.addCell(new Label(3, 0, "Producto"));
            egresos.addCell(new Label(4, 0, "IVA"));
            egresos.addCell(new Label(5, 0, "Total"));
            int size = dtm3.getRowCount();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 7; j++) {
                    if (j == 0 || j == 2 || j > 3) {
                        try {
                            ingresos.addCell(new jxl.write.Number(j, i + 1, ((BigDecimal) dtm3.getValueAt(i, j)).doubleValue()));
                        } catch (Exception e) {
                            ingresos.addCell(new jxl.write.Number(j, i + 1, ((Integer) dtm3.getValueAt(i, j)).doubleValue()));
                        }

                    } else {
                        ingresos.addCell(new Label(j, i + 1, dtm3.getValueAt(i, j).toString()));
                    }
                }
            }
            size = dtm4.getRowCount();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 6; j++) {
                    if (j == 0 || j > 3) {
                        try {
                            egresos.addCell(new jxl.write.Number(j, i + 1, ((BigDecimal) dtm4.getValueAt(i, j)).doubleValue()));
                        } catch (Exception e) {
                            egresos.addCell(new jxl.write.Number(j, i + 1, ((Integer) dtm4.getValueAt(i, j)).doubleValue()));
                        }
                    } else {
                        egresos.addCell(new Label(j, i + 1, dtm4.getValueAt(i, j).toString()));
                    }
                }
            }
            workbook.write();
            workbook.close();
            try {
                String fileName = select.getAbsolutePath();
                String[] commands = {"cmd", "/c", "start", "\"Contabilidad\"", fileName};
                Runtime.getRuntime().exec(commands);
                jDialog1.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
        jDialog1.setAlwaysOnTop(true);
    }//GEN-LAST:event_jButton7ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables

    public Component[][] getFocus() {
        return null;
    }
}
