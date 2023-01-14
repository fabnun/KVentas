package org.fabnun.senu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import org.fabnun.senu.vista.FocusInterface;

public final class PrintConfig extends javax.swing.JPanel implements FocusInterface {

    public void KeyPressed(int keyCode, int modif) {
    }
    LinkedList<String> words = new LinkedList<String>();
    LinkedList<String> words2 = new LinkedList<String>();
    PrintModel model = new PrintModel();

    public void loadModel() throws SQLException, IOException, ClassNotFoundException {
        model = (PrintModel) Constantes.db.getObject("print");
        if (model == null) {
            model = new PrintModel();
        }
        jTextField1.setText(model.url);
        jTextField2.setText(model.url2);
        jTextArea1.setText(model.code);
    }

    public void saveModel() throws SQLException, IOException {
        model.url = jTextField1.getText().trim();
        model.url2 = jTextField2.getText().trim();
        model.code = jTextArea1.getText();
        Constantes.db.setObject("print", model);
    }

    /** Creates new form PrintConfig */
    public PrintConfig() {
        initComponents();
        words.add("NFACT");
        words.add("FECHA");
        words.add("RUT");
        words.add("NOMBRE");
        words.add("DIR");
        words.add("COMUNA");
        words.add("CIUDAD");
        words.add("FONO");
        words.add("GIRO");
        words.add("OCOMP");
        words.add("CVENT");
        words.add("VENCI");
        words.add("NGUIA");
        for (int i = 0; i < Factura.LINEAS; i++) {
            words.add("CAN" + (i + 1) + "");
            words.add("DET" + (i + 1) + "");
            words.add("PRE" + (i + 1) + "");
            words.add("TOT" + (i + 1) + "");
        }
        words.add("SUBT");
        words.add("IVA");
        words.add("TOTAL");
        words.add("LIT1");
        words.add("LIT2");
        words.add("ESP");

        words2.add("Número de Factura (12)");
        words2.add("Fecha de Factura (10)");
        words2.add("Rut del cliente (13)");
        words2.add("Nombre del cliente (60)");
        words2.add("Dirección del cliente (60)");
        words2.add("Comuna del cliente (30)");
        words2.add("Ciudad del cliente (30)");
        words2.add("Telefono del cliente (13)");
        words2.add("Giro del cliente (30)");
        words2.add("Orden de Compra (20)");
        words2.add("Condiciones de Venta (10)");
        words2.add("Fecha de vencimiento (10)");
        words2.add("Número de guia (16)");
        for (int i = 0; i < Factura.LINEAS; i++) {
            words2.add("Cantidad linea " + (i + 1) + " (10)");
            words2.add("Detalle linea " + (i + 1) + " (10)");
            words2.add("Precio linea " + (i + 1) + " (10)");
            words2.add("Total linea " + (i + 1) + " (10)");
        }
        words2.add("Subtotal (10)");
        words2.add("IVA (10)");
        words2.add("Total (10)");
        words2.add("Literal linea 1 (40)");
        words2.add("Literal linea 2 (40)");
        words2.add("Espacios en blanco");

        // Collections.sort(words);
        JButton item;
        String w;
        Font f =new Font("new courier", 0, 8);
        for (int i = 0; i < words.size(); i++) {
            w = words.get(i);
            final String wo = w;
            item = new JButton(w);
            item.setFont(f);
            item.setToolTipText(words2.get(i));
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jTextArea1.append(wo + "(?),");
                    jTextArea1.requestFocus();
                }
            });
            jPanel1.add(item);
        }
        
        final JTextArea lines = new JTextArea("1");

        lines.setBackground(Color.darkGray);
        lines.setEditable(false);

        jTextArea1.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = jTextArea1.getDocument().getLength();
                Element root = jTextArea1.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
        });

        jScrollPane1.setRowHeaderView(lines);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        try {
            loadModel();
        } catch (Exception e) {
            e.printStackTrace();
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        jLabel1.setText("impresora de facturación");

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Lucida Console", 0, 13));
        jTextArea1.setRows(5);
        jTextArea1.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextArea1CaretUpdate(evt);
            }
        });
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(10, 1));

        jButton2.setText("Test");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("impresora de reportes");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_CONTROL) {
            int pos = jTextArea1.getCaretPosition();
            String text = jTextArea1.getText();
            int idxPrev = 0;
            idxPrev = text.lastIndexOf(",", pos);
        }
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            saveModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        model.setValue("NFACT", "304.304");
        model.setValue("FECHA", "17-05-2011");
        model.setValue("RUT", "12.404.430-8");
        model.setValue("NOMBRE", "Fabian Nuñez Perez");
        model.setValue("DIR", "Sargento Aldea 648-R");
        model.setValue("COMUNA", "El Bosque");
        model.setValue("CIUDAD", "Santiago");
        model.setValue("FONO", "9872172");
        model.setValue("GIRO", "Desarrollo Software");
        model.setValue("OCOMP", "1232");
        model.setValue("CVENT", "Contado");
        model.setValue("VENCI", "17-08-2011");
        model.setValue("NGUIA", "123");
        for (int i = 0; i < Factura.LINEAS; i++) {
            model.setValue("CAN" + (i + 1) + "", "10");
            model.setValue("DET" + (i + 1) + "", "la la al");
            model.setValue("PRE" + (i + 1) + "", "100");
            model.setValue("TOT" + (i + 1) + "", "1.000");
        }
        model.setValue("SUBT", "27.000");
        model.setValue("IVA", "5.130");
        model.setValue("TOTAL", "32.130");
        model.setValue("LIT1", "treinta y dos mil ciento");
        model.setValue("LIT2", "treinta pesos");
        model.setValue("ESP", "");
        try {
            model.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextArea1CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTextArea1CaretUpdate
        int caret = jTextArea1.getCaretPosition();
        String text = jTextArea1.getText();
        int x = 1, y = 1;
        char c;
        for (int i = 0; i < caret; i++) {
            c = text.charAt(i);
            if (c == '\n') {
                y++;
                x = 1;
            } else if (c == '\r') {
            } else {
                x++;
            }
        }
        jLabel2.setText(x + ":" + y);
    }//GEN-LAST:event_jTextArea1CaretUpdate
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

    public Component[][] getFocus() {
        return null;
    }
}