/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Fasturas.java
 *
 * Created on Jan 31, 2011, 9:03:10 AM
 */
package org.fabnun.senu;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.fabnun.senu.vista.AutoCompleter;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.MyCellRender;
import org.fabnun.senu.vista.TextFormater;

/**
 *
 * @author fabnun
 */
public class Facturas extends javax.swing.JPanel implements FocusInterface {

    SenuEscritorio escritorio;
    int facts = 0;
    LineBorder lf = new LineBorder(Color.blue);
    LineBorder la = new LineBorder(Color.green);
    DefaultTableCellRenderer montoRender = new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, Constantes.decimalFormat.format(value), isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.RIGHT);
            row = jTable1.convertRowIndexToModel(row);
            setBorder(row >= facts ? la : lf);
            return this;
        }
    };
    DefaultTableCellRenderer dateRender = new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                setText(sdf.format(value));
            } else {
                setText("");
            }
            setHorizontalAlignment(JLabel.CENTER);
            row = jTable1.convertRowIndexToModel(row);
            setBorder(row >= facts ? la : lf);
            return this;
        }
    };
    DefaultTableCellRenderer stringRender = new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                setText(value.toString());
            } else {
                setText("");
            }
            setHorizontalAlignment(JLabel.CENTER);
            row = jTable1.convertRowIndexToModel(row);
            setBorder(row >= facts ? la : lf);
            return this;
        }
    };

    public void KeyPressed(int keyCode, int modif) {
    }

    /** Creates new form Fasturas */
    public Facturas(SenuEscritorio escritorio) {
        this.escritorio = escritorio;
        initComponents();
        jComboBox5.setSelectedIndex(new Date().getMonth());
        jTextField10.setBorder(lf);
        jTextField11.setBorder(la);
        TableColumnModel tcm2 = jTable2.getColumnModel();
        MyCellRender intRender = new MyCellRender(MyCellRender.Type.INT);
        tcm2.getColumn(0).setCellRenderer(intRender);
        tcm2.getColumn(1).setCellRenderer(intRender);
        tcm2.getColumn(2).setCellRenderer(intRender);
        tcm2.getColumn(3).setCellRenderer(intRender);
        tcm2.getColumn(4).setCellRenderer(intRender);
        tcm2.getColumn(5).setCellRenderer(intRender);
        tcm2.getColumn(6).setCellRenderer(intRender);
        jDateChooser1.setLocale(new Locale("es", "CL"));
        jDateChooser2.setLocale(new Locale("es", "CL"));
        jDateChooser3.setLocale(new Locale("es", "CL"));
        Date now = new Date();
        jDateChooser1.setDate(now);
        jDateChooser2.setDate(now);
        jDateChooser3.setDate(now);
        int year = new Date().getYear() + 1900;
        for (int i = 0; i < 20; i++) {
            jComboBox6.addItem(year - i);
            jComboBox7.addItem(year - i);
        }
        jTable1.setModel(dtm);
        TableColumnModel tcm = jTable1.getColumnModel();
        tcm.getColumn(0).setCellRenderer(montoRender);
        tcm.getColumn(1).setCellRenderer(dateRender);
        tcm.getColumn(2).setCellRenderer(montoRender);
        tcm.getColumn(3).setCellRenderer(stringRender);
        tcm.getColumn(4).setCellRenderer(stringRender);
        tcm.getColumn(5).setCellRenderer(dateRender);
        tcm.getColumn(6).setCellRenderer(montoRender);
        tcm.getColumn(9).setCellRenderer(montoRender);
        tcm.getColumn(10).setCellRenderer(dateRender);
        tcm.getColumn(0).setMinWidth(60);
        tcm.getColumn(0).setMaxWidth(60);
        tcm.getColumn(1).setMinWidth(75);
        tcm.getColumn(1).setMaxWidth(75);
        tcm.getColumn(2).setMinWidth(70);
        tcm.getColumn(2).setMaxWidth(70);
        tcm.getColumn(4).setMinWidth(75);
        tcm.getColumn(4).setMaxWidth(75);
        tcm.getColumn(5).setMaxWidth(75);
        tcm.getColumn(5).setMinWidth(75);
        tcm.getColumn(6).setMaxWidth(75);
        tcm.getColumn(6).setMinWidth(75);
        tcm.getColumn(7).setMinWidth(75);
        tcm.getColumn(7).setMaxWidth(75);
        tcm.getColumn(8).setMinWidth(75);
        tcm.getColumn(8).setMaxWidth(75);
        tcm.getColumn(9).setMinWidth(75);
        tcm.getColumn(9).setMaxWidth(75);
        tcm.getColumn(10).setMinWidth(75);
        tcm.getColumn(10).setMaxWidth(75);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtm);
        jTable1.setRowSorter(sorter);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTextField1.setDocument(new TextFormater(8, true, "\\d"));
        jTextField8.setDocument(new TextFormater(8, true, "\\d"));
        jTextField2.setDocument(new TextFormater(9, true, "\\d"));
        TextFormater formaterNombre = new TextFormater(70, true);
        TextFormater formaterComuna = new TextFormater(25, true);
        TextFormater formaterCiudad = new TextFormater(25, true);
        TextFormater formaterGiro = new TextFormater(30, true);
        jTextField3.setDocument(formaterNombre);
        jTextField4.setDocument(formaterComuna);
        jTextField5.setDocument(formaterCiudad);
        jTextField6.setDocument(formaterGiro);
        jTextField9.setDocument(new TextFormater(7, false, "-|\\d"));
        new AutoCompleter(jTextField3) {

            @Override
            protected boolean updateListData() {
                String value = textComp.getText().trim();
                LinkedList<ObjectArray> lista = null;
                try {
                    lista = Constantes.db.buscaCliente(value);
                } catch (Exception e) {
                }
                this.list.removeAll();
                if (lista != null && lista.size() > 0) {
                    this.list.setListData(lista.toArray());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void acceptedListItem(Object selected) {

                ObjectArray objects = (ObjectArray) selected;
                if (selected != null) {
                    textComp.setText(objects.objects[0].toString());//nombre
                }


            }
        };
        new AutoCompleter(jTextField2) {

            @Override
            protected boolean updateListData() {
                String value = textComp.getText().trim().replaceAll("\\.", "");
                LinkedList<ObjectArray> lista = null;
                try {
                    lista = Constantes.db.buscaRut(value);
                } catch (Exception e) {
                }
                this.list.removeAll();
                if (lista != null && lista.size() > 0) {
                    list.setListData(lista.toArray());
                    list.setSelectedIndex(0);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void acceptedListItem(Object selected) {
            }
        };

        new AutoCompleter(jTextField4) {

            @Override
            protected boolean updateListData() {
                String value = textComp.getText().trim();
                LinkedList<String> lista = null;
                try {
                    lista = Constantes.db.buscaComuna(value);
                } catch (Exception e) {
                }
                this.list.removeAll();
                if (lista != null && lista.size() > 0) {
                    this.list.setListData(lista.toArray());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void acceptedListItem(Object selected) {
                if (selected != null) {
                    textComp.setText(selected.toString());
                }
            }
        };

        new AutoCompleter(jTextField5) {

            @Override
            protected boolean updateListData() {
                String value = textComp.getText().trim();
                LinkedList<String> lista = null;
                try {
                    lista = Constantes.db.buscaCiudad(value);
                } catch (Exception e) {
                }
                this.list.removeAll();
                if (lista != null && lista.size() > 0) {
                    this.list.setListData(lista.toArray());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void acceptedListItem(Object selected) {
                if (selected != null) {
                    textComp.setText(selected.toString());
                }
            }
        };

        new AutoCompleter(jTextField6) {

            @Override
            protected boolean updateListData() {
                String value = textComp.getText().trim();
                LinkedList<String> lista = null;
                try {
                    lista = Constantes.db.buscaGiro(value);
                } catch (Exception e) {
                }
                this.list.removeAll();
                if (lista != null && lista.size() > 0) {
                    this.list.setListData(lista.toArray());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void acceptedListItem(Object selected) {
                if (selected != null) {
                    textComp.setText(selected.toString());
                }
            }
        };
        jDateChooser1.setDateFormatString("dd-MM-yyyy");
        jDateChooser2.setDateFormatString("dd-MM-yyyy");
        jDateChooser3.setDateFormatString("dd-MM-yyyy");
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
        jLabel3 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();
        jComboBox7 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jCheckBox7 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jCheckBox9 = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jCheckBox11 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jCheckBox12 = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();

        jDialog1.setAlwaysOnTop(true);
        jDialog1.setModal(true);
        jDialog1.setResizable(false);

        jLabel3.setText("Cuanto desea sumar a el número de las facturas seleccionadas?");

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jButton11.setText("Modificar Numeros");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setRequestFocusEnabled(false);

        jDateChooser1.setDate(new java.util.Date(1296711986000L));
        jDateChooser1.setDateFormatString("dd-MM-yyyy");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Dia");
        jCheckBox1.setFocusable(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 627, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Diario", jPanel2);

        jDateChooser2.setDateFormatString("dd-MM-yyyy");

        jLabel2.setText("hasta");

        jDateChooser3.setDateFormatString("dd-MM-yyyy");

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Desde");
        jCheckBox2.setFocusable(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 412, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Entre Fechas", jPanel1);

        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Mes");
        jCheckBox3.setFocusable(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        jLabel17.setText("Año");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 603, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mensual", jPanel3);

        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Año");
        jCheckBox5.setFocusable(false);
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 715, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox5)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Anual", jPanel4);

        jButton5.setText("Buscar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("Desde N°");
        jCheckBox6.setFocusable(false);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jLabel1.setText("hasta N°");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 495, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox6)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("N° Factura", jPanel5);

        jButton6.setText("Buscar");
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jCheckBox7.setText("Rut");
        jCheckBox7.setFocusable(false);
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 642, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Rut", jPanel6);

        jButton7.setText("Buscar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jCheckBox8.setText("Nombre Cliente");
        jCheckBox8.setFocusable(false);
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jCheckBox8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox8)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Nombre Cliente", jPanel7);

        jButton8.setText("Buscar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jCheckBox9.setText("Comuna");
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jCheckBox9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 548, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox9)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Comuna", jPanel8);

        jButton9.setText("Buscar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jCheckBox10.setText("Ciudad");
        jCheckBox10.setFocusable(false);
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jCheckBox10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 548, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox10)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ciudad", jPanel9);

        jButton10.setText("Buscar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jCheckBox11.setText("Giro");
        jCheckBox11.setFocusable(false);
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jCheckBox11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 548, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox11)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Giro", jPanel10);

        jButton14.setText("Buscar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jCheckBox12.setText("Consulta");
        jCheckBox12.setFocusable(false);
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jCheckBox12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addComponent(jButton14)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton14))
                    .addComponent(jCheckBox12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consulta Sql", jPanel11);

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
        jTable1.setFocusable(false);
        jScrollPane1.setViewportView(jTable1);

        jButton12.setText("Cambiar numeros de facturas");
        jButton12.setFocusable(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Eliminar facturas y abonos");
        jButton13.setFocusable(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTable2.setModel(new DefaultTableModel(new Object[]{"#Facturas", "#Canceladas", "#Anuladas", "$Facturado", "$Cancelado", "$Abonado", "$Abonado Total", "$Deuda"},1){

            public boolean isCellEditable(int row, int col){
                return false;
            }

        });
        jTable2.setFocusable(false);
        jScrollPane2.setViewportView(jTable2);

        jLabel13.setText("Canceladas");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Si", "No" }));
        jComboBox1.setFocusable(false);

        jLabel16.setText("Anuladas");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "Si", "No" }));
        jComboBox4.setFocusable(false);

        jTextField10.setEditable(false);
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("Facturas");
        jTextField10.setEnabled(false);
        jTextField10.setFocusable(false);

        jTextField11.setEditable(false);
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setText("Abonos");
        jTextField11.setEnabled(false);
        jTextField11.setFocusable(false);

        jButton15.setText("Imprimir");

        jButton16.setText("Buscar Errores");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(659, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addComponent(jButton16)
                .addGap(18, 18, 18)
                .addComponent(jButton15)
                .addGap(18, 18, 18)
                .addComponent(jButton12)
                .addGap(18, 18, 18)
                .addComponent(jButton13)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15)
                    .addComponent(jButton16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            updateList(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    boolean[] join = new boolean[11];

    private void updateCheck(int idx, boolean value) {
        join[idx] = value;
        jTabbedPane1.setIconAt(idx, value ? Constantes.ICON_CHECK : null);
        jTabbedPane1.repaint();
    }

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        updateCheck(10, jCheckBox12.isSelected());
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        updateCheck(0, jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        updateCheck(1, jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        updateCheck(2, jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        updateCheck(3, jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        updateCheck(4, jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        updateCheck(5, jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed
        updateCheck(6, jCheckBox8.isSelected());
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jCheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9ActionPerformed
        updateCheck(7, jCheckBox9.isSelected());
    }//GEN-LAST:event_jCheckBox9ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        updateCheck(8, jCheckBox10.isSelected());
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        updateCheck(9, jCheckBox11.isSelected());
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            
            updateList(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            updateList(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            updateList(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            updateList(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            updateList(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            updateList(6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            updateList(7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            updateList(8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            updateList(9);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        try {
            updateList(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Esta seguro que desea eliminar facturas/abonos?", "Eliminar Facturas", JOptionPane.YES_NO_OPTION) == 0) {
            int[] idx;
            try {
                idx = jTable1.getSelectedRows();
                for (int i = idx.length - 1; i >= 0; i--) {
                    int id = jTable1.convertRowIndexToModel(idx[i]);
                    if (id < facts) {
                        int nf = (Integer) dtm.getValueAt(id, 0);
                        Constantes.db.eliminarFactura(nf);
                        dtm.removeRow(id);
                        facts--;
                    } else {
                        int nf = (Integer) dtm.getValueAt(id, 0);
                        Date dt = (Date) dtm.getValueAt(id, 10);
                        double ab = (Double) dtm.getValueAt(id, 9);
                        Constantes.db.eliminarAbono(nf, dt, ab);
                        dtm.removeRow(id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        jDialog1.pack();
        jDialog1.setLocationRelativeTo(this);
        jDialog1.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int sum = 0;
        try {
            sum = Integer.parseInt(jTextField9.getText());
        } catch (Exception e) {
        }
        if (sum == 0) {
            JOptionPane.showMessageDialog(this, "El número tiene que ser distinto de cero");
            return;
        }
        if (jTable1.getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero las facturas");
            return;
        }
        jDialog1.setVisible(false);
        StringBuilder sb = new StringBuilder();
        int[] idx;
        try {
            idx = jTable1.getSelectedRows();
            for (int i = idx.length - 1; i >= 0; i--) {
                int id = jTable1.convertRowIndexToModel(idx[i]);
                if (id < facts) {
                    int nf = (Integer) dtm.getValueAt(id, 0);
                    sb.append("insert into factura ( nfact, rut, fecha, ocompra, cventa, nguia, vencimiento, subtotal, iva, total, cancelada, impresa, anulada) select ");
                    sb.append(nf + sum);
                    sb.append(", rut, fecha, ocompra, cventa, nguia, vencimiento, subtotal, iva, total, cancelada, impresa, anulada from factura where nfact = ");
                    sb.append(nf).append(";\n");
                    sb.append("update abono set nfact=");
                    sb.append(nf + sum);
                    sb.append(" where nfact=");
                    sb.append(nf).append(";\n");
                    sb.append("update detalle set nfact=");
                    sb.append(nf + sum);
                    sb.append(" where nfact=");
                    sb.append(nf).append(";\n");
                    sb.append("delete from factura");
                    sb.append(" where nfact=");
                    sb.append(nf).append(";\n");
                }
            }
            Constantes.db.executeUpdate(sb.toString());
            for (int i = idx.length - 1; i >= 0; i--) {
                int id = jTable1.convertRowIndexToModel(idx[i]);
                if (id < facts) {
                    int nf = (Integer) dtm.getValueAt(id, 0);
                    dtm.setValueAt(nf + sum, id, 0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo realizar la modificacion.\nCausa:" + e.getMessage());
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if (dtm.getRowCount() > 1) {
            int nf = (Integer) dtm.getValueAt(0, 0), nf0;
            Date d0, d = (Date) dtm.getValueAt(0, 1);
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < facts; i++) {
                nf0 = nf;
                d0 = d;
                d = (Date) dtm.getValueAt(i, 1);
                nf = (Integer) dtm.getValueAt(i, 0);
                if (nf0 != 0 && nf - nf0 != 1) {
                    sb.append("Salto de número entre factura ").append(nf0).append(" y ").append(nf).append("\n");
                } else if (d.before(d0)) {
                    sb.append("Salto de a fecha anterior en factura ").append(nf).append("\n");
                } else if (d.getTime() - d0.getTime() > 259200000) {
                    sb.append("Salto de a fecha posterior en factura ").append(nf).append("\n");
                }
            }
            if (sb.length()>0){
                JOptionPane.showMessageDialog(this, sb.toString());
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed
    int nfact = 0;
    DefaultTableModel dtm = new DefaultTableModel(
            new String[]{"N°", "Fecha", "Rut", "Nombre", "Venta", "Vencimiento", "Total", "Cancelada", "Anulada", "Abono", "Fecha(Ab)"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        private Class[] clases = new Class[]{Integer.class, Date.class, Integer.class, String.class, String.class, Date.class, Integer.class, Boolean.class, Boolean.class, Integer.class, Date.class};

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return clases[columnIndex];
        }
    };

    private String getWhere(int idx) {
        String where = "";
        switch (idx) {
            case 0:
                where = "a.fecha='" + sdf.format(jDateChooser1.getDate()) + "'";
                break;
            case 1:
                where = "a.fecha>='" + sdf.format(jDateChooser2.getDate()) + "' and a.fecha<='" + sdf.format(jDateChooser3.getDate()) + "'";
                break;
            case 2:
                where = "date_part('month', a.fecha)=" + (jComboBox5.getSelectedIndex() + 1) + " and date_part('year', a.fecha)=" + jComboBox6.getSelectedItem();
                break;
            case 3:
                where = "date_part('year', a.fecha)=" + jComboBox7.getSelectedItem();
                break;
            case 4:
                where = "a.nfact>=" + jTextField1.getText().trim() + " and a.nfact<=" + jTextField8.getText().trim();
                break;
            case 5:
                where = "a.rut=" + jTextField2.getText().trim();
                break;
            case 6:
                where = "b.nombre='" + jTextField3.getText().trim() + "'";
                break;
            case 7:
                where = "b.comuna_id=(select id from comuna where comuna='" + jTextField4.getText().trim().toUpperCase() + "')";
                break;
            case 8:
                where = "b.ciudad_id=(select id from ciudad where ciudad='" + jTextField5.getText().trim().toUpperCase() + "')";
                break;
            case 9:
                where = "b.giro_id=(select id from giro where giro='" + jTextField6.getText().trim().toUpperCase() + "')";
                break;
            case 10:
                where = jTextField7.getText().trim();
                break;
        }
        return where + " and a.nfact>0 ";
    }

    private void updateList(int idx) throws SQLException {

        String where = getWhere(idx);
        for (int i = 0; i < 11; i++) {
            if (idx != i && join[i]) {
                where = where + " and " + getWhere(i);
            }
        }
        String and = "";
        if (jComboBox1.getSelectedIndex() > 0) {
            and = and + " and a.cancelada=" + (jComboBox1.getSelectedIndex()) % 2;
        }
        if (jComboBox4.getSelectedIndex() > 0) {
            and = and + " and a.anulada=" + (jComboBox4.getSelectedIndex()) % 2;
        }
        List<Object[]> lista = Constantes.db.getFacturas(where + and + " order by a.nfact");
        facts = lista.size();
        HashSet<Integer> fcs = new HashSet<Integer>();
        for (Object[] o : lista) {
            fcs.add((Integer) o[0]);
        }
        if (idx < 4) {
            List<ObjectArray> lista2 = Constantes.db.getAbonos(where);
            for (ObjectArray o : lista2) {
                if (!fcs.contains(o.objects[0])) {
                    lista.add(o.objects);
                }
            }
        }
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        Integer uno = 1;
        String[] cvent = new String[]{"CONTADO", "CHEQUE 30", "CHEQUE 60"};
        double cancel = 0, total = 0, t, abono, totalAbono = 0, totalAbono2=0;
        int canceladas = 0, anuladas = 0, facturado = 0;
        boolean cb, ab;

        int count = 0;
        for (Object[] obj : lista) {
            String cv = cvent[Byte.valueOf(obj[4].toString()) - 1];
            cb = uno.equals(obj[7]);
            ab = uno.equals(obj[9]);
            t = ((BigDecimal) obj[6]).intValue();
            if (obj[10] != null) {
                abono = ((BigDecimal) obj[10]).doubleValue();
                if ((!cb && !ab) || (count >= facts && !ab)) {
                    totalAbono = totalAbono + abono;
                    if (count<facts)
                        totalAbono2 = totalAbono2 + abono;
                }
            } else {
                abono = 0;
            }

            if (!ab && count < facts) {
                total = total + t;
                if (cb) {
                    cancel = cancel + t;
                }
            }
            if (cb && count < facts) {
                canceladas++;
            }
            if (ab && count < facts) {
                anuladas++;
            }
            if (obj.length == 12) {
                dtm.addRow(new Object[]{obj[0], obj[1], obj[2], obj[3], cv, obj[5], t, cb, ab, abono, obj[11]});
            } else {
                dtm.addRow(new Object[]{obj[0], obj[1], obj[2], obj[3], cv, obj[5], t, cb, ab, abono, null});
            }
            count++;
        }
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setValueAt(facts, 0, 0);
        model.setValueAt(canceladas, 0, 1);
        model.setValueAt(anuladas, 0, 2);
        model.setValueAt(((Double) total).intValue(), 0, 3);
        model.setValueAt(((Double) cancel).intValue(), 0, 4);
        model.setValueAt(((Double) totalAbono2).intValue(), 0, 5);
        model.setValueAt(((Double) totalAbono).intValue(), 0, 6);
        model.setValueAt(((Double) (total - cancel - totalAbono)).intValue(), 0, 7);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
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
}
