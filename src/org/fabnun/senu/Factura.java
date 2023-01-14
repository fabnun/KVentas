package org.fabnun.senu;

import com.sun.java.swing.plaf.windows.WindowsBorders;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.UndoManager;
import org.fabnun.senu.vista.AutoCompleter;
import org.fabnun.senu.vista.Escritorio;
import org.fabnun.senu.vista.FocusInterface;
import org.fabnun.senu.vista.TextFormater;

public class Factura extends javax.swing.JPanel implements FocusInterface {

    public final static int LINEAS = 27;
    private final Color FOCUS_COLOR = Color.yellow;
    private final Color NOFOCUS_COLOR = Color.white;
    private Escritorio escritorio;
    public JLabel[] codigos;
    private JTextField[] detalles;
    private JTextField[] cantidades;
    private JTextField[] precios;
    private JLabel[] totales;
    private int nfact = 1;
    boolean disableRutFormater = false;
    private TextFormater formaterRut;
    private TextFormater formaterDV = new TextFormater(1, true, "\\d|K");
    private TextFormater formaterNombre = new TextFormater(70, true);
    private TextFormater formaterFono = new TextFormater(15, true, "\\d| |-|\\(|\\)|\\.");
    private TextFormater formaterDireccion = new TextFormater(40, true);
    private TextFormater formaterOcompra = new TextFormater(22, true);
    private TextFormater formaterNguia = new TextFormater(9, true);
    private TextFormater formaterComuna = new TextFormater(25, true);
    private TextFormater formaterCiudad = new TextFormater(25, true);
    private TextFormater formaterGiro = new TextFormater(30, true);
    private TextFormater formaterVenc = new TextFormater(10, true, "\\d|-");
    private String literal = "";
    private boolean updateTotales = true;

    private void updateTotales(int idx) {
        if (updateTotales) {
            String c = cantidades[idx].getText();
            String p = precios[idx].getText();
            String t;
            if (p.length() > 0 && c.length() > 0) {
                try {
                    Double dc = Constantes.decimalFormat.parse(c).doubleValue();
                    Double dp = Constantes.decimalFormat.parse(p).doubleValue();
                    double newVal = Math.round(dp * dc);
                    totales[idx].setText(Constantes.decimalFormat.format(newVal));
                } catch (Exception e) {
                    literal = "";
                    e.printStackTrace();
                }
            } else {
                totales[idx].setText("");
            }
            updateTotal();;
        }
    }

    private synchronized void updateTotal() {
        double total = 0;
        String t;
        for (int i = 0; i < LINEAS; i++) {
            t = totales[i].getText().trim().replaceAll("\\.", "").replaceAll(",", ".");
            if (t.length() > 0) {
                try {
                    total = total + new Double(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        jTextField8.setText(Constantes.decimalFormat.format(total));
        double iva = Math.round(total * Constantes.iva);
        total = total + iva;
        jTextField14.setText(Constantes.decimalFormat.format(iva));
        jTextField15.setText(Constantes.decimalFormat.format(total));
        literal = NumberLiteral.N2L((int) total) + " pesos";
    }
    private DefaultTableModel dtm = new DefaultTableModel(new Object[]{"#", "Fecha", "Cliente"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private boolean abonar, anular;

    public Factura(Escritorio escritorio, boolean abonar, boolean anular) {
        this.abonar = abonar;
        this.anular = anular;
        try {
            nfact = Constantes.db.getUltimaFactura() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.escritorio = escritorio;
        initComponents();
        jDateChooser1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    if (updateDate) {
                        Constantes.actualDate = jDateChooser1.getDate();
                    }
                }
            }
        });
        jButton13.setEnabled(false);
        jButton7.setEnabled(false);
        jTable1.setModel(dtm);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(0).setMinWidth(30);
        jTable1.getColumnModel().getColumn(0).setWidth(30);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(70);
        jTable1.getColumnModel().getColumn(1).setMinWidth(70);
        jTable1.getColumnModel().getColumn(1).setWidth(70);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jDialog1.setIconImage(Constantes.IMG_PRINT);
        jDialog2.setIconImage(Constantes.IMG_MONITOR);
        formaterRut = new TextFormater(11, true, "\\d|\\.", Constantes.decimalFormat, jTextField1) {
            @Override
            public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException {
                if (!disableRutFormater) {
                    super.insertString(offs, str, attribute);
                    try {
                        String text = jTextField1.getText().trim().replaceAll("\\.", "");
                        jTextField2.setText(dv(Integer.parseInt(text)));
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                if (!disableRutFormater) {
                    super.remove(offs, len);
                    String text = jTextField1.getText().trim().replaceAll("\\.", "");
                    if (text.length() == 0) {
                        jTextField2.setText("");
                    } else {
                        jTextField2.setText(dv(Integer.parseInt(text)));
                    }
                }
            }
        };
        jTextField1.setDocument(formaterRut);
        jTextField2.setDocument(formaterDV);
        jTextField3.setDocument(formaterNombre);
        jTextField13.setDocument(formaterFono);
        jTextField7.setDocument(formaterDireccion);
        jTextField10.setDocument(formaterOcompra);
        jTextField12.setDocument(formaterNguia);
        jTextField4.setDocument(formaterComuna);
        jTextField5.setDocument(formaterCiudad);
        jTextField6.setDocument(formaterGiro);
        jTextField11.setDocument(formaterVenc);

        jTextField9.setText(Constantes.decimalFormat.format(nfact));
        jButton1.setIcon(Constantes.ICON_BUSCAR);
        jButton4.setIcon(Constantes.ICON_IMPRIMIR);
        jButton7.setIcon(Constantes.ICON_ANULAR);
        codigos = new JLabel[LINEAS];
        detalles = new JTextField[LINEAS];
        cantidades = new JTextField[LINEAS];
        precios = new JTextField[LINEAS];
        totales = new JLabel[LINEAS];
        jButton9.setIcon(Constantes.ICON_ANTERIOR);
        jButton10.setIcon(Constantes.ICON_SIGUIENTE);
        jButton8.setIcon(Constantes.ICON_ULTIMA);
        jPanel4.add(addRow(true));
        MouseAdapter mouseWhell = new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getUnitsToScroll() > 0) {
                    jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() + 20);
                } else {
                    jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue() - 20);
                }
            }
        };
        for (int i = 0; i < LINEAS; i++) {
            JPanel p = addRow(false);
            jPanel3.add(p);
            codigos[i] = (JLabel) p.getComponent(1);
            detalles[i] = (JTextField) p.getComponent(2);
            cantidades[i] = (JTextField) p.getComponent(3);
            precios[i] = (JTextField) p.getComponent(4);
            totales[i] = (JLabel) p.getComponent(5);
            //codigos[i].setDocument(new TextFormater(8, true, "\\d|\\w"));
            detalles[i].setDocument(new TextFormater(60, true));
            final int idx = i;
            cantidades[i].setDocument(new TextFormater(14, true, "\\d|,|\\.", Constantes.decimalFormat, cantidades[i]) {
                @Override
                public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException {
                    if (cantidades[idx].hasFocus()) {
                        str = str.replaceAll("\\.", "");
                    }
                    super.insertString(offs, str, attribute);
                    updateTotales(idx);
                }

                @Override
                public void remove(int offs, int len) throws BadLocationException {
                    super.remove(offs, len);
                    updateTotales(idx);
                }
            });
            precios[i].setDocument(new TextFormater(14, true, "\\d|,|\\.", Constantes.decimalFormat, precios[i]) {
                @Override
                public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException {
                    if (precios[idx].hasFocus()) {
                        str = str.replaceAll("\\.", "");
                    }
                    super.insertString(offs, str, attribute);
                    updateTotales(idx);
                }

                @Override
                public void remove(int offs, int len) throws BadLocationException {
                    super.remove(offs, len);
                    updateTotales(idx);
                }
            });
            totales[i].setFocusable(false);
            codigos[i].setFocusable(false);
            codigos[i].addMouseWheelListener(mouseWhell);
            detalles[i].addMouseWheelListener(mouseWhell);
            cantidades[i].addMouseWheelListener(mouseWhell);
            precios[i].addMouseWheelListener(mouseWhell);

            totales[i].addMouseWheelListener(mouseWhell);
            scrollRectToVisible(codigos[i]);
            scrollRectToVisible(detalles[i]);
            scrollRectToVisible(cantidades[i]);
            scrollRectToVisible(precios[i]);
            scrollRectToVisible(totales[i]);
            prodComplete = new ProductoAutoCompleter(detalles[i], false, this);
            //codComplete = new CodigoAutoCompleter(codigos[i]);
        }
        setWhiteCells();
        addFocusEvents(jTextField1);
        addFocusEvents(jTextField2);
        addFocusEvents(jTextField3);
        addFocusEvents(jTextField4);
        addFocusEvents(jTextField5);
        addFocusEvents(jTextField6);
        addFocusEvents(jTextField7);
        addFocusEvents(jTextField10);
        addFocusEvents(jTextField11);
        addFocusEvents(jTextField12);
        addFocusEvents(jComboBox1);
        addFocusEvents(jTextField13);
        new CiudadAutoCompleter(jTextField5);
        new ComunaAutoCompleter(jTextField4);
        new GiroAutoCompleter(jTextField6);
        new ClienteAutoCompleter(jTextField3, this);
        new RutAutoCompleter(jTextField1, this);
        if (Constantes.dvEditable) {
            jTextField2.setEditable(true);
            jTextField2.setFocusable(true);
        }
        jDateChooser1.setLocale(new Locale("es", "CL"));
        JComponent comp = (JComponent) jDateChooser1.getComponent(0);
        comp.setToolTipText("Fecha de la factura");
        jDateChooser1.getComponent(1).setFocusable(false);
        ((JComponent) (jDateChooser1.getComponent(1))).setToolTipText("Modificar fecha facturación");
        comp = (JComponent) comp.getComponent(2);
        comp = (JComponent) comp.getComponent(0);
        comp.setFocusable(false);
        ((JTextComponent) comp).setEditable(false);
        comp.setFont(jTextField9.getFont());
    }
    ProductoAutoCompleter prodComplete = null;

    private JPanel addRow(boolean titles) {
        JPanel panel = new JPanel();
        JLabel label;
        JTextField text;
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        LineBorder borde2 = new LineBorder(Color.black, 1);
        LineBorder borde = new WindowsBorders.DashedBorder(Color.black);
        if (titles) {
            label = new JLabel(" ", JLabel.CENTER);
            label.setMaximumSize(new Dimension(20, 20));
            label.setMinimumSize(new Dimension(20, 20));
            label.setPreferredSize(new Dimension(20, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel("Codigo", JLabel.CENTER);
            label.setMaximumSize(new Dimension(80, 20));
            label.setMinimumSize(new Dimension(80, 20));
            label.setPreferredSize(new Dimension(80, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel("Detalle", JLabel.CENTER);
            label.setMaximumSize(new Dimension(4096, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel("Cantidad", JLabel.CENTER);
            label.setMaximumSize(new Dimension(100, 20));
            label.setMinimumSize(new Dimension(100, 20));
            label.setPreferredSize(new Dimension(100, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel("Precio", JLabel.CENTER);
            label.setMaximumSize(new Dimension(100, 20));
            label.setMinimumSize(new Dimension(100, 20));
            label.setPreferredSize(new Dimension(100, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel("Total", JLabel.CENTER);
            label.setMaximumSize(new Dimension(100, 20));
            label.setMinimumSize(new Dimension(100, 20));
            label.setPreferredSize(new Dimension(100, 20));
            label.setBorder(borde2);
            panel.add(label);
            label = new JLabel(" ", JLabel.CENTER);
            label.setMaximumSize(new Dimension(17, 20));
            label.setMinimumSize(new Dimension(17, 20));
            label.setPreferredSize(new Dimension(17, 20));
            label.setBorder(borde2);
            panel.add(label);

        } else {
            KeyAdapter adapter = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                        e.consume();
                    }
                }
            };
            label = new JLabel("" + (jPanel3.getComponentCount() + 1), JLabel.CENTER);
            label.setMaximumSize(new Dimension(20, 20));
            label.setMinimumSize(new Dimension(20, 20));
            label.setPreferredSize(new Dimension(20, 20));
            label.setBorder(borde);
            panel.add(label);
            label = new JLabel();
            final JLabel codigoText = label;
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setMaximumSize(new Dimension(80, 20));
            label.setMinimumSize(new Dimension(80, 20));
            label.setPreferredSize(new Dimension(80, 20));
            label.setBorder(borde);
            panel.add(label);
            text = new JTextField();
            addFocusEvents(text);
            text.setMaximumSize(new Dimension(4096, 20));
            text.setBorder(borde);
            panel.add(text);
            text = new JTextField();
            text.setHorizontalAlignment(JTextField.RIGHT);
            addFocusEvents(text);
            text.setMaximumSize(new Dimension(100, 20));
            text.setMinimumSize(new Dimension(100, 20));
            text.setPreferredSize(new Dimension(100, 20));
            text.setBorder(borde);
            text.addKeyListener(adapter);
            panel.add(text);
            text = new JTextField();
            text.setHorizontalAlignment(JTextField.RIGHT);
            addFocusEvents(text);
            text.setMaximumSize(new Dimension(100, 20));
            text.setMinimumSize(new Dimension(100, 20));
            text.setPreferredSize(new Dimension(100, 20));
            text.setBorder(borde);
            text.addKeyListener(adapter);
            panel.add(text);
            label = new JLabel();
            label.setHorizontalAlignment(JTextField.RIGHT);
            label.setMaximumSize(new Dimension(100, 20));
            label.setMinimumSize(new Dimension(100, 20));
            label.setPreferredSize(new Dimension(100, 20));
            label.setBorder(borde);
            panel.add(label);
        }
        return panel;
    }

    public void checkDeuda(String rt) {
        final String rtt = rt.replaceAll("(\\s|\\.)*", "");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int rut = 0;
                try {
                    rut = Integer.parseInt(rtt);
                } catch (Exception e) {
                }
                try {
                    boolean exist = Constantes.db.existCliente("" + rut);
                    jButton14.setEnabled(exist);
                    double val = Constantes.db.alert(rut);
                    if (val > 0.06) {
                        jButton14.setBorder(alertBorder1);
                    } else if (val > 0.03) {
                        jButton14.setBorder(alertBorder2);
                    } else if (val > 0) {
                        jButton14.setBorder(alertBorder3);
                    } else {
                        jButton14.setBorder(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    jButton14.setBorder(null);
                }
            }
        });
    }
    private Border alertBorder1 = new LineBorder(Color.red, 2, true);
    private Border alertBorder2 = new LineBorder(Color.orange, 2, true);
    private Border alertBorder3 = new LineBorder(Color.yellow, 2, true);

    private void scrollRectToVisible(final Component component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Component c = e.getComponent().getParent();
                Rectangle vRect = jScrollPane1.getViewport().getViewRect();
                Rectangle cRect = c.getBounds();
                if (!vRect.contains(cRect)) {
                    if (cRect.y < vRect.y) {
                        jScrollPane1.getViewport().setViewPosition(new Point(0, cRect.y));
                    } else {
                        jScrollPane1.getViewport().setViewPosition(new Point(0, cRect.y + cRect.height - vRect.height));
                    }
                }
            }
        });
    }
    final UndoManager undo = new UndoManager();

    private void addFocusEvents(final Component component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBackground(FOCUS_COLOR);
            }

            @Override
            public void focusLost(FocusEvent e) {
                component.setBackground(NOFOCUS_COLOR);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jDialog2 = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jDialog3 = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jDialog4 = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jButton9 = new javax.swing.JButton();
        jTextField9 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jLabel14 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton5 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jButton14 = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        jButton13 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jTextField8 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();

        jDialog1.setTitle("Imprimir y Guardar");
        jDialog1.setAlwaysOnTop(true);
        jDialog1.setFocusable(false);
        jDialog1.setModal(true);
        jDialog1.setResizable(false);

        jCheckBox1.setText("Cotización");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Factura Cancelada");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jButton2.setText("Imprimir y Guardar Factura");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton2KeyPressed(evt);
            }
        });

        jPanel7.setFocusable(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(64, 64));
        jPanel7.setLayout(new java.awt.CardLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/pagado.png"))); // NOI18N
        jLabel9.setFocusable(false);
        jPanel7.add(jLabel9, "card2");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Imprimir");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jButton2))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog1Layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );

        jDialog2.setTitle("Cotizaciones");
        jDialog2.setAlwaysOnTop(true);
        jDialog2.setFocusable(false);
        jDialog2.setModal(true);

        jScrollPane2.setFocusable(false);

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
        jScrollPane2.setViewportView(jTable1);

        jButton11.setText("Elimina cotización seleccionadar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jButton11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton11KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jButton11)
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDialog3.setTitle("Buscar una factura");
        jDialog3.setAlwaysOnTop(true);
        jDialog3.setFocusable(false);
        jDialog3.setModal(true);
        jDialog3.setResizable(false);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/ask.png"))); // NOI18N
        jLabel11.setFocusable(false);

        jLabel12.setText("Ingrese el N° de la factura ");
        jLabel12.setFocusable(false);

        jTextField16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField16KeyPressed(evt);
            }
        });

        jButton12.setText("Buscar");
        jButton12.setFocusable(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addComponent(jTextField16, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 83, Short.MAX_VALUE)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12))
                .addContainerGap())
        );

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
        jScrollPane3.setViewportView(jTable2);

        jButton3.setText("Agregar");

        jButton15.setText("Cancelar");

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton15))
                .addContainerGap())
        );

        setDoubleBuffered(false);
        setFocusable(false);

        jLabel1.setText("Rut");
        jLabel1.setFocusable(false);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setFocusable(false);
        jToolBar1.setMaximumSize(new java.awt.Dimension(33209, 30));
        jToolBar1.setMinimumSize(new java.awt.Dimension(442, 30));
        jToolBar1.setPreferredSize(new java.awt.Dimension(720, 30));
        jToolBar1.add(jSeparator8);

        jButton9.setToolTipText("Factura anterior");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton9.setFocusable(false);
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jTextField9.setEditable(false);
        jTextField9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setToolTipText("Número de factura");
        jTextField9.setFocusable(false);
        jTextField9.setMaximumSize(new java.awt.Dimension(90, 25));
        jTextField9.setMinimumSize(new java.awt.Dimension(90, 25));
        jTextField9.setPreferredSize(new java.awt.Dimension(90, 25));
        jToolBar1.add(jTextField9);

        jButton10.setToolTipText("Factura siguiente");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton10.setFocusable(false);
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        jButton8.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton8.setText("F1");
        jButton8.setToolTipText("Factura nueva [F1]");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton8.setFocusable(false);
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton1.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton1.setText("F2");
        jButton1.setToolTipText("Carga una factura [F2]");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.setFocusable(false);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator6);

        jLabel14.setText("  Fecha ");
        jLabel14.setFocusable(false);
        jToolBar1.add(jLabel14);

        jDateChooser1.setToolTipText("Fecha de la factura");
        jDateChooser1.setDateFormatString("dd-MM-yyyy");
        jDateChooser1.setFocusable(false);
        jDateChooser1.setMaximumSize(new java.awt.Dimension(140, 25));
        jDateChooser1.setMinimumSize(new java.awt.Dimension(140, 25));
        jDateChooser1.setPreferredSize(new java.awt.Dimension(140, 25));
        jToolBar1.add(jDateChooser1);
        jToolBar1.add(jSeparator1);

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Factura Nueva");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.add(jPanel1);
        jToolBar1.add(jSeparator5);

        jButton4.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/printer.png"))); // NOI18N
        jButton4.setText("F3");
        jButton4.setToolTipText("Guardar/ Imprimir factura [F3]");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton4.setFocusable(false);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton6.setText("Abonos");
        jButton6.setToolTipText("Abonos de la factura");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton6.setFocusable(false);
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton6.setMaximumSize(new java.awt.Dimension(0, 0));
        jButton6.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton6.setPreferredSize(new java.awt.Dimension(0, 0));
        jButton6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton6);
        jToolBar1.add(jSeparator4);

        jButton5.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/accessories-text-editor.png"))); // NOI18N
        jButton5.setText("F4");
        jButton5.setToolTipText("Cotizaciones [F4]");
        jButton5.setFocusable(false);
        jButton5.setMaximumSize(new java.awt.Dimension(56, 32));
        jButton5.setMinimumSize(new java.awt.Dimension(56, 32));
        jButton5.setPreferredSize(new java.awt.Dimension(56, 32));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);
        jToolBar1.add(jSeparator7);

        jButton7.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/cancel.png"))); // NOI18N
        jButton7.setText("F5");
        jButton7.setToolTipText("Anular factura [F5]");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton7.setEnabled(false);
        jButton7.setFocusable(false);
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);
        jToolBar1.add(jSeparator12);

        jButton14.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/documento.png"))); // NOI18N
        jButton14.setText("F6");
        jButton14.setToolTipText("Facturas del cliente [F6]");
        jButton14.setEnabled(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton14.setMaximumSize(new java.awt.Dimension(56, 32));
        jButton14.setMinimumSize(new java.awt.Dimension(56, 32));
        jButton14.setPreferredSize(new java.awt.Dimension(56, 32));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton14);
        jToolBar1.add(jSeparator11);

        jButton13.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/recargar.png"))); // NOI18N
        jButton13.setText("F10");
        jButton13.setToolTipText("Copiar factura [F10]");
        jButton13.setEnabled(false);
        jButton13.setFocusable(false);
        jButton13.setMaximumSize(new java.awt.Dimension(62, 32));
        jButton13.setMinimumSize(new java.awt.Dimension(62, 32));
        jButton13.setPreferredSize(new java.awt.Dimension(62, 32));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton13);

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
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });

        jLabel3.setText("Comuna");
        jLabel3.setFocusable(false);

        jTextField4.setToolTipText("Comuna del cliente");

        jLabel4.setText("Ciudad");
        jLabel4.setFocusable(false);

        jLabel13.setText("Dirección");
        jLabel13.setFocusable(false);

        jTextField7.setToolTipText("Direccion del cliente");

        jLabel5.setText("Giro");
        jLabel5.setFocusable(false);

        jTextField6.setToolTipText("Giro del cliente");

        jLabel16.setText("Ord.Comp.");
        jLabel16.setFocusable(false);

        jTextField10.setToolTipText("Orden de compra");

        jLabel17.setText("Pago");
        jLabel17.setFocusable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CONTADO", "CHEQUE 30", "CHEQUE 60" }));
        jComboBox1.setToolTipText("Forma de pago");

        jLabel18.setText("Venc.");
        jLabel18.setFocusable(false);

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setToolTipText("Vencimiento del pago");
        jTextField11.setMaximumSize(new java.awt.Dimension(70, 20));
        jTextField11.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField11.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabel19.setText("Num.Guia");
        jLabel19.setFocusable(false);

        jTextField12.setToolTipText("Numero de guia");

        jTextField5.setToolTipText("Ciudad del cliente");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(4096, 14));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));
        jPanel2.add(jPanel4);

        jPanel5.setMaximumSize(new java.awt.Dimension(4096, 0));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 989, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setFocusable(false);
        jScrollPane1.setRequestFocusEnabled(false);
        jScrollPane1.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setWheelScrollingEnabled(false);
        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
        });

        jPanel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel3KeyPressed(evt);
            }
        });
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel3);

        jPanel2.add(jScrollPane1);

        jLabel20.setText("Fono");

        jTextField13.setToolTipText("Telefono del cliente");

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.PAGE_AXIS));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Subtotal ");
        jLabel7.setFocusable(false);
        jLabel7.setMaximumSize(new java.awt.Dimension(50, 20));
        jPanel11.add(jLabel7);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("I.V.A. ");
        jLabel8.setFocusable(false);
        jLabel8.setMaximumSize(new java.awt.Dimension(50, 20));
        jPanel11.add(jLabel8);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total ");
        jLabel10.setFocusable(false);
        jLabel10.setMaximumSize(new java.awt.Dimension(50, 20));
        jPanel11.add(jLabel10);

        jPanel6.add(jPanel11);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.PAGE_AXIS));

        jTextField8.setEditable(false);
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField8.setFocusable(false);
        jPanel12.add(jTextField8);

        jTextField14.setEditable(false);
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField14.setFocusable(false);
        jPanel12.add(jTextField14);

        jTextField15.setEditable(false);
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTextField15.setFocusable(false);
        jPanel12.add(jTextField15);

        jPanel6.add(jPanel12);

        jPanel13.setMaximumSize(new java.awt.Dimension(20, 32767));
        jPanel13.setPreferredSize(new java.awt.Dimension(18, 59));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel13);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/go-next.png"))); // NOI18N
        jButton16.setToolTipText("Insertar registro [Ctrl+PgDown]");
        jButton16.setFocusable(false);
        jButton16.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton16.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton16.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton16);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/emblem-unreadable.png"))); // NOI18N
        jButton17.setToolTipText("Borrar registro [Ctrl+PgUp]");
        jButton17.setFocusable(false);
        jButton17.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton17.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton17.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton17);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 1003, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1003, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 314, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 314, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 246, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 247, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateDV() {
        try {
            String text = jTextField1.getText().trim().replaceAll("\\.", "");
            final int rut = Integer.parseInt(text);
        } catch (Exception e) {
            e.printStackTrace();
            jButton14.setEnabled(false);
        }
        if (!Constantes.dvEditable) {
            try {
                jTextField2.setText(dv(Integer.parseInt(jTextField1.getText().replaceAll("\\.", ""))));
            } catch (Exception e) {
                jTextField2.setText("");
            }
        }
    }

    private void jTextField1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField1InputMethodTextChanged
        if (jTextField1.getText().trim().length() > 0) {
            updateDV();
        } else {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField1InputMethodTextChanged

    private void jTextField1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField1CaretPositionChanged
        if (jTextField1.getText().trim().length() > 0) {
            updateDV();
        } else {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField1CaretPositionChanged

    private void jTextField1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextField1CaretUpdate
        if (jTextField1.getText().trim().length() > 0) {
            updateDV();
        } else {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField1CaretUpdate
    FacturasDeCliente oldFacts = null;

    private void viewFacturas(boolean all) {
        String rut = jTextField1.getText().trim().replaceAll("\\.", "");
        FacturasDeCliente facts;
        if (rut.length() > 0) {
            facts = new FacturasDeCliente(rut, this, abonar, all);
        } else {
            facts = oldFacts;
        }
        if (facts != null) {
            facts.select(nfact);
            facts.clicked = false;
            escritorio.showDialog(facts, "Facturas de " + jTextField3.getText().trim(), null, true, true);
            try {
                if (!facts.clicked && Constantes.db.existFactura(nfact)) {
                    cargar(nfact);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            oldFacts = facts;
        }
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            if (nfact <= Constantes.db.getUltimaFactura()) {
                nfact++;
                cargar(nfact);
                checkDeuda(jTextField1.getText());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            if (nfact > 1) {
                cargar(nfact - 1);
                checkDeuda(jTextField1.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void openCargar() {
        recargar = false;
        jDialog3.setTitle("Cargar factura");
        jLabel12.setText("N° de la factura");
        jButton12.setText("Cargar");
        jTextField16.setText("");
        jDialog3.pack();
        jDialog3.setLocationRelativeTo(this);
        jDialog3.setVisible(true);
    }

    private synchronized void print() throws Exception {
        PrintModel model = null;
        try {
            model = (PrintModel) Constantes.db.getObject("print");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setValue("NFACT", jTextField9.getText());
        model.setValue("FECHA", Constantes.dateFormat.format(jDateChooser1.getDate()));
        model.setValue("RUT", jTextField1.getText() + "-" + jTextField2.getText());
        model.setValue("NOMBRE", jTextField3.getText());
        model.setValue("DIR", jTextField7.getText());
        model.setValue("COMUNA", jTextField4.getText());
        model.setValue("CIUDAD", jTextField5.getText());
        model.setValue("FONO", jTextField13.getText());
        model.setValue("GIRO", jTextField6.getText());
        model.setValue("OCOMP", jTextField10.getText());
        model.setValue("CVENT", jComboBox1.getSelectedItem().toString());
        model.setValue("VENCI", jTextField11.getText());
        model.setValue("NGUIA", jTextField12.getText());
        for (int i = 0; i < Factura.LINEAS; i++) {
            model.setValue("CAN" + (i + 1), cantidades[i].getText());
            model.setValue("DET" + (i + 1), detalles[i].getText());
            model.setValue("PRE" + (i + 1), precios[i].getText());
            model.setValue("TOT" + (i + 1), totales[i].getText());
        }
        model.setValue("SUBT", jTextField8.getText());
        model.setValue("IVA", jTextField14.getText());
        model.setValue("TOTAL", jTextField15.getText());

        String lit1 = literal;
        String lit2 = "";
        if (literal.length() > 62) {
            int idx = literal.lastIndexOf(" ", 62);
            lit1 = literal.substring(0, idx).trim();
            lit2 = literal.substring(idx).trim();
        }

        model.setValue("LIT1", lit1);
        model.setValue("LIT2", lit2);
        model.setValue("ESP", "");
        model.print();
    }

    private String validarFactura() {
        int fact = new Integer(jTextField9.getText().replaceAll("\\.", ""));
        Date fecha = jDateChooser1.getDate();
        if (jTextField1.getText().trim().length() == 0) {
            return "Debe ingresar el rut del cliente";
        }
        if (jTextField2.getText().trim().length() == 0) {
            return "El rut del cliente debe ser valido";
        }
        if (jTextField3.getText().trim().length() == 0) {
            return "Debe ingresar el nombre del cliente";
        }
        if (jTextField7.getText().trim().length() == 0) {
            return "Debe ingresar la dirección del cliente";
        }
        if (jTextField4.getText().trim().length() == 0) {
            return "Debe ingresar la comuna del cliente";
        }
        if (jTextField5.getText().trim().length() == 0) {
            return "Debe ingresar la ciudad del cliente";
        }
        if (jTextField6.getText().trim().length() == 0) {
            return "Debe ingresar el giro del cliente";
        }
        int tot;
        try {
            tot = Integer.parseInt(jTextField15.getText().trim().replaceAll("\\.", "").replaceAll(",", ""));
        } catch (Exception ex) {
            tot = 0;
        }
        if (tot == 0) {
            return "La factura no puede sumar cero";
        }
        return null;
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (nueva == 1) {
            String msg = validarFactura();
            if (msg == null) {
                jCheckBox1.setSelected(false);
                jCheckBox2.setSelected(true);
                jCheckBox3.setSelected(true);
                jCheckBox1.setEnabled(true);
                jCheckBox2.setEnabled(true);
                jCheckBox3.setEnabled(true);
                jLabel9.setIcon(getIcon(jCheckBox2.isSelected(), false, jCheckBox1.isSelected()));
                updateButtonText();
                jDialog1.pack();
                jDialog1.setLocationRelativeTo(this);
                jButton2.requestFocus();
                jDialog1.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, msg);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No puede guardar una factura ya guardada");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private Icon getIcon(boolean cancelada, boolean anulada, boolean cotizacion) {
        if (cotizacion) {
            return Constantes.ICON_COTIZACION;
        } else {
            if (anulada) {
                return Constantes.ICON_ANULADO;
            } else {
                if (cancelada) {
                    return Constantes.ICON_PAGADO;
                } else {
                    return Constantes.ICON_NOPAGADO;
                }
            }
        }
    }

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        jCheckBox2.setEnabled(!jCheckBox1.isSelected());
        jCheckBox3.setEnabled(!jCheckBox1.isSelected());
        jLabel9.setIcon(getIcon(jCheckBox2.isSelected(), false, jCheckBox1.isSelected()));
        updateButtonText();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        printAndSave();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (nueva == 1) {
            JOptionPane.showMessageDialog(this, "No puede anular/desanular una factura nueva");
            return;
        }
        String msg = "Desea Quitar la anulación de la factura " + Constantes.decimalFormat.format(nfact) + "?";
        if (anulada == 0) {
            msg = "Desea anular la factura " + Constantes.decimalFormat.format(nfact) + "?";
        }
        if (JOptionPane.showConfirmDialog(this, msg, "Anular/DesAnular", JOptionPane.YES_NO_OPTION) == 0) {
            try {
                Constantes.db.setAnular(nfact, anulada == 0);
                cargar(nfact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        jCheckBox2.setEnabled(!jCheckBox1.isSelected());
        jLabel9.setIcon(getIcon(jCheckBox2.isSelected(), false, jCheckBox1.isSelected()));
        updateButtonText();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void updateCotis() {
        int count = dtm.getRowCount();
        for (int i = 0; i < count; i++) {
            dtm.removeRow(0);
        }
        try {
            List<Object[]> list = Constantes.db.getCotizaciones();
            for (Object[] obs : list) {
                dtm.addRow(obs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        updateCotis();
        jDialog2.pack();
        jDialog2.setLocationRelativeTo(this);
        jDialog2.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void loadCotizacion() {
        try {
            int idx = jTable1.getSelectedRow();
            idx = jTable1.convertRowIndexToModel(idx);
            cargar(((Integer) dtm.getValueAt(idx, 0)).intValue() * -1);
            nfact = Integer.parseInt(jTextField9.getText().trim().replaceAll("\\.", ""));
            jDialog2.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            loadCotizacion();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int idx = jTable1.getSelectedRow();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione primero una cotización");
        } else {
            try {
                Constantes.db.eliminarCotizacion((Integer) dtm.getValueAt(idx, 0) * -1);
                updateCotis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        updateButtonText();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void printAndSave() {
        try {
            synchronized (this) {
                updateTotal();
                if (!jCheckBox1.isSelected() && jCheckBox3.isSelected()) {
                    print();
                }
                save();
            }
            cargar(nfact);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No pudo imprimir/guardar factura\n" + e.getMessage());
            e.printStackTrace();
        }
        jDialog1.setVisible(false);
    }

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jDialog1.setVisible(false);
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            printAndSave();
        }
    }//GEN-LAST:event_jButton2KeyPressed

    public int getNfact() {
        String text = jTextField16.getText().trim();
        int nf = 0;
        try {
            nf = Integer.parseInt(text);
        } catch (Exception e) {
        }
        return nf;
    }

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        cargarRegargar();
}//GEN-LAST:event_jTextField16ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        cargarRegargar();
}//GEN-LAST:event_jButton12ActionPerformed

    private void cargarRegargar() {
        jDialog3.setVisible(false);
        int nf = getNfact();
        if (nf > 0) {
            if (recargar) {
                try {
                    recargar(nf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    cargar(nf);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void jTextField16KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jDialog3.setVisible(false);
        }
    }//GEN-LAST:event_jTextField16KeyPressed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jDialog2.setVisible(false);
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jButton11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton11KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            jDialog2.setVisible(false);
        }
    }//GEN-LAST:event_jButton11KeyPressed

    private void jPanel3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN || evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            evt.consume();
        }
    }//GEN-LAST:event_jPanel3KeyPressed

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN || evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            evt.consume();
        }
    }//GEN-LAST:event_jScrollPane1KeyPressed
    private boolean updateDate = true;

    private void recargar(int nf) {
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
        jButton13.setEnabled(false);
        if (anular) {
            jButton7.setEnabled(false);
        }
        try {
            int newNfact = nf;
            if (newNfact != nfact) {
                nfact = newNfact;
            }
            updateDate = false;
            jDateChooser1.setDate(Constantes.actualDate);
            updateDate = true;
            jTextField9.setText(Constantes.decimalFormat.format(newNfact));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        nueva = 1;
        jLabel6.setForeground(Color.black);
        jLabel6.setText("Factura Nueva");
        setFacturaEditable(true);
        jButton13.setEnabled(false);
        jButton4.setEnabled(true);
    }
    boolean recargar = false;
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        openRecargar();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            int newNfact = Constantes.db.getUltimaFactura() + 1;
            if (newNfact != nfact) {
                nfact = newNfact;
            }
            cargar(nfact);
            checkDeuda(jTextField1.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openCargar();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        viewFacturas(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField3MouseClicked
        if (nueva == 0 && evt.getClickCount() == 2 && evt.getButton() == 1) {
            Clientes cl = new Clientes((SenuEscritorio) escritorio, true, jTextField3.getText());
            escritorio.addVentana("Clientes", Constantes.ICON_CLIENTE,
                    true, true, true, true,
                    cl);
        }
    }//GEN-LAST:event_jTextField3MouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        pgDown();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        pgUp();
    }//GEN-LAST:event_jButton17ActionPerformed

    public void updateButtonText() {
        if (!jCheckBox1.isSelected()) {
            if (jCheckBox3.isSelected()) {
                jButton2.setText("Imprimir y Guardar Factura");
            } else {
                jButton2.setText("Guardar Factura");
            }
        } else {
            jButton2.setText("Guardar Cotización");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    public javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private String dv(int valor) {
        int M = 0, S = 1, T = valor;
        for (; T != 0; T /= 10) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        }
        return "" + (char) (S != 0 ? S + 47 : 75);
    }

    private void pgDown() {
        int idx = -1;
        int idx2 = -1;
        for (int i = 0; i < LINEAS; i++) {
            if (codigos[i].hasFocus() || detalles[i].hasFocus()
                    || cantidades[i].hasFocus() || precios[i].hasFocus() || totales[i].hasFocus()) {
                idx = i;
                if (codigos[i].hasFocus()) {
                    idx2 = 0;
                } else if (detalles[i].hasFocus()) {
                    idx2 = 1;
                } else if (cantidades[i].hasFocus()) {
                    idx2 = 2;
                } else if (precios[i].hasFocus()) {
                    idx2 = 3;
                } else if (totales[i].hasFocus()) {
                    idx2 = 4;
                }
                break;
            }
        }
        if (idx >= 0) {
            prodComplete.setDisable(true);
            for (int i = LINEAS - 1; i > idx; i--) {
                totales[i].setText(totales[i - 1].getText());
                precios[i].setText(precios[i - 1].getText());
                cantidades[i].setText(cantidades[i - 1].getText());
                detalles[i].setText(detalles[i - 1].getText());
                codigos[i].setText(codigos[i - 1].getText());
            }
            totales[idx].setText("");
            precios[idx].setText("");
            cantidades[idx].setText("");
            detalles[idx].setText("");
            codigos[idx].setText("");
            prodComplete.setDisable(false);
            switch (idx2) {
                case 0:
                    codigos[idx].requestFocus();
                    break;
                case 1:
                    detalles[idx].requestFocus();
                    break;
                case 2:
                    cantidades[idx].requestFocus();
                    break;
                case 3:
                    precios[idx].requestFocus();
                    break;
                case 4:
                    totales[idx].requestFocus();
                    break;
            }
        }
    }

    private void pgUp() {
        int idx = -1;
        int idx2 = -1;
        for (int i = 0; i < LINEAS; i++) {
            if (codigos[i].hasFocus() || detalles[i].hasFocus()
                    || cantidades[i].hasFocus() || precios[i].hasFocus() || totales[i].hasFocus()) {
                idx = i;
                if (codigos[i].hasFocus()) {
                    idx2 = 0;
                } else if (detalles[i].hasFocus()) {
                    idx2 = 1;
                } else if (cantidades[i].hasFocus()) {
                    idx2 = 2;
                } else if (precios[i].hasFocus()) {
                    idx2 = 3;
                } else if (totales[i].hasFocus()) {
                    idx2 = 4;
                }
                break;
            }
        }
        if (idx >= 0) {
            prodComplete.setDisable(true);
            for (int i = idx; i < LINEAS - 1; i++) {
                totales[i].setText(totales[i + 1].getText());
                precios[i].setText(precios[i + 1].getText());
                cantidades[i].setText(cantidades[i + 1].getText());
                detalles[i].setText(detalles[i + 1].getText());
                codigos[i].setText(codigos[i + 1].getText());
            }
            totales[LINEAS - 1].setText("");
            precios[LINEAS - 1].setText("");
            cantidades[LINEAS - 1].setText("");
            detalles[LINEAS - 1].setText("");
            codigos[LINEAS - 1].setText("");
            prodComplete.setDisable(false);
            switch (idx2) {
                case 0:
                    codigos[idx].requestFocus();
                    break;
                case 1:
                    detalles[idx].requestFocus();
                    break;
                case 2:
                    cantidades[idx].requestFocus();
                    break;
                case 3:
                    precios[idx].requestFocus();
                    break;
                case 4:
                    totales[idx].requestFocus();
                    break;
            }
        }
    }

    public void KeyPressed(int keyCode, int modif) {
        if (keyCode == KeyEvent.VK_PAGE_DOWN && modif == KeyEvent.CTRL_MASK) {
            pgDown();
        } else if (keyCode == KeyEvent.VK_PAGE_UP && modif == KeyEvent.CTRL_MASK) {
            pgUp();
        } else if (keyCode == KeyEvent.VK_F1) {
            try {
                int newNfact = Constantes.db.getUltimaFactura() + 1;
                if (newNfact != nfact) {
                    nfact = newNfact;
                }
                cargar(nfact);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (keyCode == KeyEvent.VK_F2) {
            openCargar();
        } else if (keyCode == KeyEvent.VK_F3) {
            if (nueva == 1) {
                String msg = validarFactura();
                if (msg == null) {
                    jCheckBox1.setSelected(false);
                    jCheckBox2.setSelected(true);
                    jCheckBox3.setSelected(true);
                    jCheckBox1.setEnabled(true);
                    jCheckBox2.setEnabled(true);
                    jCheckBox3.setEnabled(true);
                    jLabel9.setIcon(getIcon(jCheckBox2.isSelected(), false, jCheckBox1.isSelected()));
                    updateButtonText();
                    jDialog1.pack();
                    jDialog1.setLocationRelativeTo(this);
                    jButton2.requestFocus();
                    jDialog1.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, msg);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No puede guardar una factura ya guardada");
            }
        } else if (keyCode == KeyEvent.VK_F5) {
            if (nueva == 1) {
                JOptionPane.showMessageDialog(this, "No puede anular/desanular una factura nueva");
                return;
            }
            String msg = "Desea Quitar la anulación de la factura " + Constantes.decimalFormat.format(nfact) + "?";
            if (anulada == 0) {
                msg = "Desea anular la factura " + Constantes.decimalFormat.format(nfact) + "?";
            }
            if (JOptionPane.showConfirmDialog(this, msg, "Anular/DesAnular", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    Constantes.db.setAnular(nfact, anulada == 0);
                    cargar(nfact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (keyCode == KeyEvent.VK_F6) {
            viewFacturas(true);
        } else if (keyCode == KeyEvent.VK_F10) {
            openRecargar();
        } else if (keyCode == KeyEvent.VK_F4 && modif == 0) {
            updateCotis();
            jDialog2.pack();
            jDialog2.setLocationRelativeTo(this);
            jDialog2.setVisible(true);
        }
    }

    private void openRecargar() {
        try {
            recargar = true;
            int newNfact = Constantes.db.getUltimaFactura() + 1;
            jDialog3.setTitle("Copiar factura");
            jLabel12.setText("N° de factura nueva");
            jButton12.setText("Copiar");
            jTextField16.setText("" + newNfact);
            jDialog3.pack();
            jDialog3.setLocationRelativeTo(this);
            jDialog3.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Component[][] getFocus() {
        if (!Constantes.dvEditable) {
            Component[][] compoenents = new Component[11 + LINEAS * 3][5];
            compoenents[0] = new Component[]{jTextField1, jTextField13, detalles[LINEAS - 1], jTextField3, jTextField7};
            compoenents[1] = new Component[]{jTextField3, jTextField1, detalles[LINEAS - 1], jTextField13, jTextField7};
            compoenents[2] = new Component[]{jTextField13, jTextField3, precios[LINEAS - 1], jTextField1, jTextField5};
            compoenents[3] = new Component[]{jTextField7, jTextField5, jTextField1, jTextField4, jTextField6};
            compoenents[4] = new Component[]{jTextField4, jTextField7, jTextField3, jTextField5, jTextField12};
            compoenents[5] = new Component[]{jTextField5, jTextField4, jTextField3, jTextField7, jTextField11};
            compoenents[6] = new Component[]{jTextField6, jTextField11, jTextField7, jTextField10, detalles[0]};
            compoenents[7] = new Component[]{jTextField10, jTextField6, jTextField7, jTextField12, detalles[0]};
            compoenents[8] = new Component[]{jTextField12, jTextField10, jTextField4, jComboBox1, cantidades[0]};
            compoenents[9] = new Component[]{jComboBox1, jTextField12, jTextField5, jTextField11, precios[0]};
            compoenents[10] = new Component[]{jTextField11, jComboBox1, jTextField5, jTextField6, precios[0]};
            for (int i = 0; i < LINEAS; i++) {
                if (i == 0) {
                    compoenents[11 + i * 3] = new Component[]{detalles[i], precios[i], jTextField10, cantidades[i], detalles[i + 1]};
                    compoenents[12 + i * 3] = new Component[]{cantidades[i], detalles[i], jTextField12, precios[i], cantidades[i + 1]};
                    compoenents[13 + i * 3] = new Component[]{precios[i], cantidades[i], jComboBox1, detalles[i], precios[i + 1]};
                } else if (i == LINEAS - 1) {
                    compoenents[11 + i * 3] = new Component[]{detalles[i], precios[i], detalles[i - 1], cantidades[i], jTextField3};
                    compoenents[12 + i * 3] = new Component[]{cantidades[i], detalles[i], cantidades[i - 1], precios[i], jTextField3};
                    compoenents[13 + i * 3] = new Component[]{precios[i], cantidades[i], precios[i - 1], detalles[i], jTextField13};
                } else {
                    compoenents[11 + i * 3] = new Component[]{detalles[i], precios[i], detalles[i - 1], cantidades[i], detalles[i + 1]};
                    compoenents[12 + i * 3] = new Component[]{cantidades[i], detalles[i], cantidades[i - 1], precios[i], cantidades[i + 1]};
                    compoenents[13 + i * 3] = new Component[]{precios[i], cantidades[i], precios[i - 1], detalles[i], precios[i + 1]};
                }
            }
            return compoenents;
        } else {
            Component[][] compoenents = new Component[12 + LINEAS * 3][5];
            compoenents[0] = new Component[]{jTextField1, jTextField13, detalles[LINEAS - 1], jTextField2, jTextField7};
            compoenents[1] = new Component[]{jTextField2, jTextField1, detalles[LINEAS - 1], jTextField3, jTextField7};
            compoenents[2] = new Component[]{jTextField3, jTextField2, detalles[LINEAS - 1], jTextField13, jTextField7};
            compoenents[3] = new Component[]{jTextField13, jTextField3, detalles[LINEAS - 1], jTextField1, jTextField7};
            compoenents[4] = new Component[]{jTextField7, jTextField5, jTextField1, jTextField4, jTextField6};
            compoenents[5] = new Component[]{jTextField4, jTextField7, jTextField3, jTextField5, jTextField12};
            compoenents[6] = new Component[]{jTextField5, jTextField4, jTextField13, jTextField7, jTextField11};
            compoenents[7] = new Component[]{jTextField6, jTextField11, jTextField7, jTextField10, detalles[0]};
            compoenents[8] = new Component[]{jTextField10, jTextField6, jTextField7, jTextField12, detalles[0]};
            compoenents[9] = new Component[]{jTextField12, jTextField10, jTextField4, jComboBox1, cantidades[0]};
            compoenents[10] = new Component[]{jComboBox1, jTextField12, jTextField5, jTextField11, precios[0]};
            compoenents[11] = new Component[]{jTextField11, jComboBox1, jTextField5, jTextField6, precios[0]};
            for (int i = 0; i < LINEAS; i++) {
                if (i == 0) {
                    compoenents[13 + i * 3] = new Component[]{detalles[i], precios[i], jTextField10, cantidades[i], detalles[i + 1]};
                    compoenents[14 + i * 3] = new Component[]{cantidades[i], detalles[i], jTextField12, precios[i], cantidades[i + 1]};
                    compoenents[15 + i * 3] = new Component[]{precios[i], cantidades[i], jComboBox1, detalles[i], precios[i + 1]};
                } else if (i == LINEAS - 1) {
                    compoenents[13 + i * 3] = new Component[]{detalles[i], precios[i], detalles[i - 1], cantidades[i], jTextField3};
                    compoenents[14 + i * 3] = new Component[]{cantidades[i], detalles[i], cantidades[i - 1], precios[i], jTextField3};
                    compoenents[15 + i * 3] = new Component[]{precios[i], cantidades[i], precios[i - 1], detalles[i], jTextField3};
                } else {
                    compoenents[13 + i * 3] = new Component[]{detalles[i], precios[i], detalles[i - 1], cantidades[i], detalles[i + 1]};
                    compoenents[14 + i * 3] = new Component[]{cantidades[i], detalles[i], cantidades[i - 1], precios[i], cantidades[i + 1]};
                    compoenents[15 + i * 3] = new Component[]{precios[i], cantidades[i], precios[i - 1], detalles[i], precios[i + 1]};
                }
            }
            return compoenents;
        }
    }

    public void nueva(int nfact) {
        jButton4.setEnabled(true);
        jButton5.setEnabled(true);
        jButton13.setEnabled(false);
        if (anular) {
            jButton7.setEnabled(false);
        }
        updateDate = false;
        jDateChooser1.setDate(Constantes.actualDate);
        updateDate = true;
        jTextField9.setText(Constantes.decimalFormat.format(nfact));
        jTextField1.setText("");//Rut
        jTextField2.setText("");//dv
        jTextField3.setText("");//nombre
        jTextField13.setText("");//fono
        jTextField7.setText("");//direc
        jTextField4.setText("");//comuna
        jTextField5.setText("");//ciudad
        jTextField6.setText("");//giro
        jTextField10.setText("");//ocomp
        jTextField12.setText("");//ngui
        jComboBox1.setSelectedIndex(0);//pago
        jTextField11.setText("");//venc
        for (int i = 0; i < LINEAS; i++) {
            codigos[i].setText("");
            detalles[i].setText("");
            cantidades[i].setText("");
            precios[i].setText("");
            totales[i].setText("");
        }
        jTextField8.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
    }
//select 0 a.nfact, 1 a.rut, 2 b.nombre,  3 b.telefono, 4 b.direccion,"
//                    + " 5 b.comuna_id, 6 b.ciudad_id, 7 b.giro_id, 8 a.fecha, 9 a.ocompra, 10 a.cventa, 11 a.nguia, "
//                    + "12 a.vencimiento, 13 a.subtotal, 14 a.iva, 15 a .total from factura a, cliente b where a.nfact=? and a.rut=b.rut
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int anulada = 0;
    private int cancelada = 0;
    private int nueva = 1;

    public void cargar(int nfact) throws SQLException {
        boolean coti = nfact < 0;
        AutoCompleter.blockOther(true);
        updateTotales = false;
        ObjectArray obj = Constantes.db.getFactura(nfact);
        this.nfact = nfact;
        if (obj != null) {
            Object[] o = obj.objects;
            anulada = (Integer) o[16];
            cancelada = (Integer) o[17];
            nueva = 0;
            String msg = "";
            if (!coti) {
                jButton4.setEnabled(false);
                jButton5.setEnabled(false);
                jButton13.setEnabled(true);
                if (anular) {
                    jButton7.setEnabled(true);
                }
                if (anulada == 1) {
                    msg = "Factura Anulada";
                    jLabel6.setForeground(Color.blue);
                } else {
                    if (cancelada == 1) {
                        msg = "Factura Cancelada";
                        jLabel6.setForeground(Color.green);
                    } else {
                        msg = "Factura No Cancelada";
                        jLabel6.setForeground(Color.red);
                    }
                }
                jLabel6.setText(msg);
            } else {
                jButton4.setEnabled(true);
                jButton5.setEnabled(true);
                jButton13.setEnabled(false);
                if (anular) {
                    jButton7.setEnabled(false);
                }
            }

            if (!coti) {
                jTextField9.setText(Constantes.decimalFormat.format(o[0]));//nfact
                try {
                    Date d = sdf.parse(o[8].toString());
                    updateDate = false;
                    jDateChooser1.setDate(d);
                    updateDate = true;
                } catch (Exception e) {
                    jDateChooser1.setDate(null);
                    e.printStackTrace();
                }
            }
            jTextField1.setText(Constantes.decimalFormat.format(o[1]));//Rut
            jTextField2.setText(dv((Integer) o[1]));//dv
            jTextField3.setText(o[2].toString());//nombre
            jTextField13.setText((o[3].toString()));//fono
            jTextField7.setText(o[4].toString());//direc
            jTextField4.setText(Constantes.db.getComuna((Integer) o[5]));//comuna
            jTextField5.setText(Constantes.db.getCiudad((Integer) o[6]));//ciudad
            jTextField6.setText(Constantes.db.getGiro((Integer) o[7]));//giro
            jTextField10.setText(o[9].toString());//ocomp
            jComboBox1.setSelectedIndex((Integer) o[10] - 1);//pago
            jTextField12.setText(o[11].toString());//ngui
            Date venc = (Date) o[12];
            String sVenc = "";
            if (venc != null) {
                sVenc = Constantes.dateFormat.format(venc);
            }
            jTextField11.setText(sVenc);//venc
            jTextField8.setText(Constantes.decimalFormat.format(o[13]));
            jTextField14.setText(Constantes.decimalFormat.format(o[14]));
            jTextField15.setText(Constantes.decimalFormat.format(o[15]));
            LinkedList<ObjectArray> objs = Constantes.db.getDetalle(nfact);
            for (int i = 0; i < LINEAS; i++) {
                codigos[i].setText("");
                detalles[i].setText("");
                cantidades[i].setText("");
                precios[i].setText("");
                totales[i].setText("");
            }
            for (ObjectArray det : objs) {
                int linea = (Integer) det.objects[1] - 1;
                codigos[linea].setText(det.objects[2].toString());
                detalles[linea].setText(det.objects[3].toString());
                BigDecimal[] values = new BigDecimal[]{
                    (BigDecimal) det.objects[4],
                    (BigDecimal) det.objects[5],
                    (BigDecimal) det.objects[6]
                };
                if (values[0].doubleValue() != 0 || values[1].doubleValue() != 0 || values[2].doubleValue() != 0) {
                    cantidades[linea].setText(Constantes.decimalFormat.format(det.objects[4]));
                    precios[linea].setText(Constantes.decimalFormat.format(det.objects[5]));
                    totales[linea].setText(Constantes.decimalFormat.format(det.objects[6]));
                }
            }
        } else {
            nueva = 1;
            jLabel6.setForeground(Color.black);
            jLabel6.setText("Factura Nueva");
            nueva(nfact);
            jButton13.setEnabled(false);
        }
        setFacturaEditable(coti || nueva == 1);
        JInternalFrame jif = (JInternalFrame) getParent().getParent().getParent().getParent();
        escritorio.jDesktopPane1.setSelectedFrame(jif);
        jTextField1.requestFocus();
        AutoCompleter.blockOther(false);
        if (coti) {
            nueva = 1;
            try {
                nfact = Integer.parseInt(jTextField9.getText().replaceAll("\\.", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateTotales = true;
    }

    private void setWhiteCells() {
        for (int i = 0; i < LINEAS; i++) {
            codigos[i].setBackground(Color.white);
            detalles[i].setBackground(Color.white);
            cantidades[i].setBackground(Color.white);
            precios[i].setBackground(Color.white);
        }
        jTextField1.setBackground(Color.white);
        jTextField2.setBackground(Color.white);
        jTextField3.setBackground(Color.white);
        jTextField13.setBackground(Color.white);
        jTextField4.setBackground(Color.white);
        jTextField5.setBackground(Color.white);
        jTextField7.setBackground(Color.white);
        jTextField6.setBackground(Color.white);
        jTextField10.setBackground(Color.white);
        jTextField12.setBackground(Color.white);
        jTextField11.setBackground(Color.white);
        jComboBox1.setBackground(Color.white);
        jDateChooser1.setBackground(Color.white);
    }

    private void setFacturaEditable(boolean editable) {
        for (int i = 0; i < LINEAS; i++) {
            detalles[i].setEditable(editable);
            cantidades[i].setEditable(editable);
            precios[i].setEditable(editable);
        }
        jButton5.setEnabled(editable);
        jTextField1.setEditable(editable);
        jTextField2.setEditable(editable);
        jTextField3.setEditable(editable);
        jTextField13.setEditable(editable);
        jTextField4.setEditable(editable);
        jTextField5.setEditable(editable);
        jTextField7.setEditable(editable);
        jTextField6.setEditable(editable);
        jTextField10.setEditable(editable);
        jTextField12.setEditable(editable);
        jTextField11.setEditable(editable);
        jComboBox1.setEnabled(editable);
        jDateChooser1.setEnabled(editable);
        JComponent comp = (JComponent) jDateChooser1.getComponent(0);
        comp.getComponent(0).setEnabled(editable);
    }

    private synchronized void save() throws SQLException, IOException, ClassNotFoundException {
        boolean imprimir = !jCheckBox1.isSelected();
        boolean cancelar = false;
        int fact = new Integer(jTextField9.getText().replaceAll("\\.", ""));
        Date fecha = jDateChooser1.getDate();
        int rut = new Integer(jTextField1.getText().replaceAll("\\.", ""));
        String nombre = jTextField3.getText().trim();
        String dir = jTextField7.getText().trim();
        String comuna = jTextField4.getText().trim();
        String ciudad = jTextField5.getText().trim();

        String fono = jTextField13.getText().trim();
        String giro = jTextField6.getText().trim();
        String ocompra = jTextField10.getText().trim();
        int cventa = jComboBox1.getSelectedIndex() + 1;
        String venc = jTextField11.getText().trim();
        Date vencimiento;
        try {
            vencimiento = Constantes.dateFormat.parse(venc);
        } catch (Exception e) {
            vencimiento = null;
        }
        String nguia = jTextField12.getText().trim();

        String[] codigo = new String[LINEAS];
        String[] detalle = new String[LINEAS];
        double[] cantidad = new double[LINEAS];
        double[] precio = new double[LINEAS];
        double[] total = new double[LINEAS];

        for (int i = 0; i < Factura.LINEAS; i++) {
            codigo[i] = codigos[i].getText().trim();
            detalle[i] = detalles[i].getText().trim();
            try {
                cantidad[i] = Double.parseDouble(cantidades[i].getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
            } catch (Exception e) {
                cantidad[i] = 0;
            }
            try {
                precio[i] = Double.parseDouble(precios[i].getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
            } catch (Exception e) {
                precio[i] = 0;
            }
            try {
                total[i] = Double.parseDouble(totales[i].getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
            } catch (Exception e) {
                total[i] = 0;
            }
        }
        int subtotal, iva, tot;

        try {
            subtotal = Integer.parseInt(jTextField8.getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
        } catch (Exception ex) {
            subtotal = 0;
        }
        try {
            iva = Integer.parseInt(jTextField14.getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
        } catch (Exception ex) {
            iva = 0;
        }
        try {
            tot = Integer.parseInt(jTextField15.getText().trim().replaceAll("\\.", "").replaceAll(",", "."));
        } catch (Exception ex) {
            tot = 0;
        }

        try {
            Constantes.db.updateCliente(rut, nombre, fono, dir, comuna, ciudad, giro);
            if (!imprimir) {
                fact = Constantes.db.getMinNFact();
                cancelar = true;
            } else {
                cancelar = jCheckBox2.isSelected();
            }
            Constantes.db.addFactura(fact, rut, fecha, ocompra, cventa, nguia, vencimiento, subtotal, iva, tot, cancelar, imprimir, false);
            for (int i = 0; i < Factura.LINEAS; i++) {
                if (codigo[i].length() > 0 || detalle[i].length() > 0
                        || cantidad[i] > 0 || precio[i] > 0 || total[i] > 0) {
                    Constantes.db.addDetalle(fact, i + 1, codigo[i], detalle[i], cantidad[i], precio[i], total[i]);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getCause());
            e.printStackTrace();
        }

    }
}
