package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.fabnun.senu.Constantes;

public class Configuracion extends javax.swing.JPanel implements FocusInterface {

    DefaultListModel dlm = new DefaultListModel();
    DefaultComboBoxModel themeModel = new DefaultComboBoxModel();
    File path = new File("fondos");
    Escritorio escritorio;
    ButtonColor bColor=new ButtonColor();

    private void updateThemes() {
        for (String laf : escritorio.lafMap.keySet()) {
            themeModel.addElement(laf);
        }
    }

    private void updateImages() {
        File[] files = path.listFiles();
        for (File f : files) {
            if (!f.getName().equals(".svn") && !dlm.contains(f.getName())) {
                dlm.addElement(f.getName());
            }
        }
        String s;
        boolean found;
        LinkedList<String> del = new LinkedList<String>();
        for (int i = 0; i < dlm.getSize(); i++) {
            s = (String) dlm.get(i);
            found = false;
            for (File f : files) {
                if (s.equals(f.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                del.add(s);
            }
        }
        if (escritorio.config.fondo != null && dlm.contains(escritorio.config.fondo)) {
            jComboBox1.setSelectedItem(escritorio.config.fondo);
        } else if (dlm.getSize() > 0) {
            jComboBox1.setSelectedIndex(0);
        }
    }

    /** Creates new form Configuracion */
    public Configuracion(final Escritorio escritorio) {
        this.escritorio = escritorio;
        initComponents();
        bColor.setToolTipText("Color de fondo");
        jPanel1.add(bColor,"bcol");
        bColor.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("foreground")){
                    escritorio.config.color=bColor.getColor().getRGB();
                    escritorio.repaint();
                }
            }
        });
        jList1.setModel(dlm);
        updateThemes();
        jComboBox3.setModel(themeModel);
      jComboBox3.setSelectedItem(escritorio.config.laf);
        updateImages();
        if (escritorio.config.fondo != null) {
            jList1.setSelectedValue(escritorio.config.fondo, true);
            if (escritorio.config.tipo == Escritorio.FondoTipo.CENTRADO) {
                jComboBox1.setSelectedIndex(0);
            } else if (escritorio.config.tipo == Escritorio.FondoTipo.ESTIRADO) {
                jComboBox1.setSelectedIndex(1);
            } else {
                jComboBox1.setSelectedIndex(2);
            }
        }
        jCheckBox1.setSelected(escritorio.config.showDateTime);
        jCheckBox2.setSelected(escritorio.config.showMem);
        jCheckBox3.setSelected(escritorio.config.siempreVisible);
        jComboBox2.setSelectedIndex(escritorio.config.fullscreen ? 1 : 0);
        Color c = new Color(escritorio.config.color);
        bColor.setColor(c);
    }

    private void paintFondo(Graphics g) {
        g.setColor(jPanel7.getBackground());
        g.fillRect(0, 0, 210, 180);
        g.drawImage(Constantes.IMG_MONITOR, 15, 10, null);
        g.setColor(new Color(escritorio.config.color));
        g.fillRect(28, 25, 154, 115);
        if (img != null && (imgProcesada == null || imgProcesada.getWidth(null) != 154
                || imgProcesada.getHeight(null) != 115)) {
            imgProcesada = img.getScaledInstance(154, 115, Image.SCALE_FAST);
        }
        if (imgProcesada != null) {
            g.drawImage(imgProcesada, 28, 25, null);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel(){

            public void paint(Graphics g){
                paintFondo(g);
            }

        };
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();

        jPanel3.setFocusable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fondos"));

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Agregar Fondo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Quitar Fondo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Posición:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Centrado", "Estirado", "Mosaico" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel2.setText("Color:");

        jLabel3.setText("Estilo:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ventana", "FullScreen" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Modo:");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Ver Fecha / Hora");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Ver Memoria libre");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Siempre Visible");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jPanel1.setMaximumSize(new java.awt.Dimension(201, 25));
        jPanel1.setMinimumSize(new java.awt.Dimension(201, 25));
        jPanel1.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jCheckBox3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, 0, 201, Short.MAX_VALUE)
                    .addComponent(jComboBox2, 0, 201, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 201, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox2))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(5, 5, 5)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    Image img = null;
    Image imgProcesada = null;

    private Escritorio.FondoTipo getTipo() {
        int idx = jComboBox1.getSelectedIndex();
        if (idx == 0) {
            return Escritorio.FondoTipo.CENTRADO;
        } else if (idx == 1) {
            return Escritorio.FondoTipo.ESTIRADO;
        } else {
            return Escritorio.FondoTipo.MOSAICO;
        }
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        escritorio.config.fondo = (String) jList1.getSelectedValue();
        if (escritorio.config.fondo != null) {
            File f = new File(path, escritorio.config.fondo);
            try {
                imgProcesada = null;
                img = ImageIO.read(f);
                escritorio.updateFondo(img);
                escritorio.repaint();
            } catch (Exception e) {
                imgProcesada = null;
                img = null;
                escritorio.updateFondo(img);
                escritorio.repaint();
            }
        } else {
            imgProcesada = null;
            img = null;
            escritorio.updateFondo(img);
            escritorio.repaint();
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        try {
            escritorio.config.tipo = getTipo();
            escritorio.repaint();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        escritorio.config.fullscreen = jComboBox2.getSelectedIndex() == 1;
        escritorio.showInJFrame(escritorio.config.fullscreen, escritorio.config.siempreVisible);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        escritorio.config.showDateTime = jCheckBox1.isSelected();
        escritorio.setVisibleTime(escritorio.config.showDateTime);
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    JFileChooser jfc = new JFileChooser(new File(System.getProperty("user.home")));
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jfc.showDialog(this, "Agregar imagen de fondo") == JFileChooser.APPROVE_OPTION) {
            File fi = jfc.getSelectedFile();
            try {
                ImageIO.read(fi);
            } catch (Exception e) {
                fi = null;
            }
            if (fi != null) {
                try {
                    byte[] buffer = new byte[4096];
                    FileInputStream fis = new FileInputStream(fi);
                    String name = fi.getName();
                    File fo = new File("fondos", name);
                    int count = 0;
                    int idx = name.lastIndexOf(".");
                    if (idx < 0) {
                        idx = name.length();
                    }
                    while (fo.exists()) {
                        count++;
                        String name0 = name.substring(0, idx - 1) + count + name.substring(idx);
                        fo = new File("fondos", name0);
                    }
                    FileOutputStream fos = new FileOutputStream(fo);
                    int readed;
                    while ((readed = fis.read(buffer)) > -1) {
                        fos.write(buffer, 0, readed);
                    }
                    fos.close();
                    fis.close();
                    dlm.addElement(fo.getName());
                    jList1.setSelectedValue(fo.getName(), true);
                } catch (Exception e) {
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String selected = (String) jList1.getSelectedValue();
        if (selected != null) {
            if (selected.equals("_Sin fondo_")) {
                JOptionPane.showMessageDialog(this, "No puede eliminar este item");
            } else if (JOptionPane.showConfirmDialog(this, "Esta seguro que desea eliminar esta imagen de la lista", "Eliminar imagen", JOptionPane.YES_NO_OPTION) == 0) {
                File fo = new File("fondos", selected);
                fo.delete();
                dlm.removeElement(selected);
                jList1.setSelectedValue("_Sin fondo_", true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una imagen de la lista");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        escritorio.config.showMem = jCheckBox2.isSelected();
        escritorio.setVisibleMem(escritorio.config.showMem);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        String name=(String) jComboBox3.getSelectedItem();
        escritorio.config.laf=name;
        escritorio.setSkin(name);
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        escritorio.config.siempreVisible = jCheckBox3.isSelected();
        escritorio.showInJFrame(escritorio.config.fullscreen, escritorio.config.siempreVisible);
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public Component getFirstFocus() {
        return getFocus()[0][0];
    }

    public Component[][] getFocus() {
        return new Component[][]{{jPanel3}};
    }

    public void KeyPressed(int keyCode, int modif) {
    }
}