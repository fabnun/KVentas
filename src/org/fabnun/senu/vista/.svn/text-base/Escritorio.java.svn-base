package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.fabnun.senu.Constantes;
import org.fabnun.senu.EscritorioConfig;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SkinInfo;

public abstract class Escritorio extends javax.swing.JPanel {

    public void setSkin(final String name) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    SubstanceLookAndFeel.setSkin(lafMap.get(name).getClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**Tipos de organizacion del fondo*/
    public enum FondoTipo {

        CENTRADO, MOSAICO, ESTIRADO
    };
    /**Titulo de la aplicacion*/
    public String titulo;
    /**Icono de la aplicacion*/
    public Image icono;
    /**Frame que contiene a la aplicacion*/
    public JFrame actualFrame = null;
    /**Tread que actualiza el tiempo*/
    private Thread timeThread = null;
    /**Tread que actualiza la memoria libre*/
    private Thread memThread = null;
    /**Imagen de fondo redimensionada*/
    private Image fondoCargado = null, fondoProcesado = null;
    /**Lista de ventanas abiertas*/
    private ArrayList<InternalFrame> ventanas = new ArrayList<InternalFrame>();
    /**Menu de inicio*/
    private JPopupMenu menu = null;
    /**Configuracion del escritorio*/
    public EscritorioConfig config;
    /**Formato de la fecha*/
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss  ");
    public Map<String, SkinInfo> lafMap = SubstanceLookAndFeel.getAllSkins();

    /**Mantiene la informacion de una ventana*/
    public class InternalFrame {

        /**Icono del frame*/
        Icon icono;
        /**Titulo del frame*/
        String titulo;
        /**InternalFrame asociado*/
        public JInternalFrame frame;
        /**Boton de la barra de tareas asociado*/
        JButton button;

        /**Instancia el frame*/
        public InternalFrame(String titulo, Icon icono, final JInternalFrame frame) {
            this.titulo = titulo;
            this.icono = icono;
            this.frame = frame;
            button = new JButton(titulo);
            button.setIcon(icono);
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    frame.setVisible(true);
                    try {
                        frame.setIcon(false);
                    } catch (Exception ex) {
                    }
                    frame.toFront();
                }
            });
        }

        public void setVisible(boolean visible) {
            frame.setVisible(visible);
            button.setVisible(visible);
            jToolBar1.remove(button);
            jToolBar1.add(button, 2);
        }
    }

    public void setVisibleMem(boolean visible) {
        jPanel3.setVisible(visible);
        if (!visible) {
            if (memThread != null) {
                memThread = null;
            }
        } else {
            if (memThread == null) {
                memThread = new Thread(new Runnable() {

                    public void run() {
                        Runtime rt = Runtime.getRuntime();
                        int max = (int) rt.maxMemory() / 1024 / 1024;
                        while (timeThread != null) {
                            try {
                                int actual = (int) (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                                jLabel2.setText(" " + actual + "/" + max + " ");
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                memThread.start();
            }
        }
    }

    /**Muestra u oculta el reloj*/
    public void setVisibleTime(boolean visible) {
        if (!visible) {
            if (timeThread != null) {
                timeThread = null;
            }
        } else {
            if (timeThread == null) {
                timeThread = new Thread(new Runnable() {

                    public void run() {
                        GregorianCalendar cal = new GregorianCalendar();
                        String[] dias = new String[]{"Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"};
                        while (timeThread != null) {
                            Date d = new Date();
                            cal.setTime(d);
                            jLabel1.setText(dias[cal.get(GregorianCalendar.DAY_OF_WEEK) - 1] + " " + sdf.format(d));
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                        }
                        jLabel1.setText("");
                    }
                });
                timeThread.start();
            }
        }
    }

    /**Pinta el fondo del escritorio*/
    private void paintFondo(Graphics g) {
        g.setColor(new Color(config.color));
        g.fillRect(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
        if (fondoCargado != null && config.tipo != null) {
            int x, y;
            switch (config.tipo) {
                case CENTRADO:
                    x = jDesktopPane1.getWidth() / 2 - fondoCargado.getWidth(null) / 2;
                    y = jDesktopPane1.getHeight() / 2 - fondoCargado.getHeight(null) / 2;
                    g.drawImage(fondoCargado, x, y, null);
                    break;
                case ESTIRADO:
                    if (fondoProcesado == null || fondoProcesado.getWidth(null) != jDesktopPane1.getWidth()
                            || fondoProcesado.getHeight(null) != jDesktopPane1.getHeight()) {
                        fondoProcesado = fondoCargado.getScaledInstance(jDesktopPane1.getWidth(),
                                jDesktopPane1.getHeight(), Image.SCALE_FAST);
                    }
                    g.drawImage(fondoProcesado, 0, 0, null);
                    break;
                case MOSAICO:
                    for (x = 0; x < jDesktopPane1.getWidth(); x = x + fondoCargado.getWidth(null)) {
                        for (y = 0; y < jDesktopPane1.getHeight(); y = y + fondoCargado.getHeight(null)) {
                            g.drawImage(fondoCargado, x, y, null);
                        }
                    }
                    break;
            }
        }
    }

    public void updateFondo(Image img) {
        if (config.fondo != null) {
            try {
                fondoProcesado = null;
                fondoCargado = img;
            } catch (Exception e) {
                fondoCargado = null;
            }
        } else {
            fondoCargado = null;
        }
    }
    private HashMap<String, InternalFrame> mapaFrames = new HashMap<String, InternalFrame>();

    public InternalFrame getInternalFrame(String titulo) {
        return mapaFrames.get(titulo);
    }

    public InternalFrame addVentana(final String titulo, Icon icono, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, Component contenido) {
        final JInternalFrame jiframe;
        final InternalFrame iframe;
        if (!mapaFrames.containsKey(titulo)) {
            jiframe = new JInternalFrame(titulo, resizable, closable, maximizable, iconifiable);
            if (contenido != null) {
                jiframe.add(contenido);
            }
            jiframe.pack();

            jiframe.setLocation(Math.max(0, jDesktopPane1.getWidth() / 2 - jiframe.getWidth() / 2),
                    Math.max(0, jDesktopPane1.getHeight() / 2 - jiframe.getHeight() / 2));
            if (icono != null) {
                jiframe.setFrameIcon(icono);
            }
            iframe = new InternalFrame(titulo, icono, jiframe);
            jiframe.addInternalFrameListener(new InternalFrameAdapter() {

                @Override
                public void internalFrameIconified(InternalFrameEvent e) {
                    jiframe.setVisible(false);
                }

                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    jToolBar1.remove(iframe.button);
                    ventanas.remove(iframe);
                    mapaFrames.remove(titulo);
                    jToolBar1.repaint();
                }
            });
            if (contenido != null) {
                final FocusInterface inter = (FocusInterface) contenido;
                final Component[][] focus = inter.getFocus();
                if (focus != null) {
                    focus[0][0].requestFocus();
                    Component comp;
                    for (int f = 0; f < focus.length; f++) {
                        comp = focus[f][0];
                        final int ff = f;
                        comp.addKeyListener(new KeyAdapter() {

                            @Override
                            public void keyPressed(KeyEvent e) {
                                int code = e.getKeyCode();
                                int modif = e.getModifiers();
                                inter.KeyPressed(code, modif);
                                if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                                    if (code == KeyEvent.VK_LEFT) {
                                        focus[ff][1].requestFocus();
                                    } else if (code == KeyEvent.VK_UP) {
                                        focus[ff][2].requestFocus();
                                    } else if (code == KeyEvent.VK_RIGHT) {
                                        focus[ff][3].requestFocus();
                                    } else if (code == KeyEvent.VK_DOWN) {
                                        focus[ff][4].requestFocus();
                                    } else if (code == KeyEvent.VK_TAB) {
                                        //ACA el tab con control
                                    }
                                }
                            }
                        });
                    }
                }
            }
            jDesktopPane1.add(jiframe);
            jToolBar1.add(iframe.button, 2);
            jiframe.setVisible(true);
            mapaFrames.put(titulo, iframe);
        } else {
            iframe = mapaFrames.get(titulo);
            iframe.frame.setVisible(true);
            iframe.frame.toFront();
        }
        return iframe;
    }

    public void setMenu(JPopupMenu menu) {
        this.menu = menu;
        menu.setVisible(true);
        menu.setVisible(false);
        jDesktopPane1.requestFocus();
    }

    public JPopupMenu getMenu() {
        return menu;
    }

    public Escritorio(String titulo, Image icono) {
        try {
            config = EscritorioConfig.load();
        } catch (Exception e) {
            config = new EscritorioConfig();
        }
        if (config.laf == null) {
            config.laf = "Business";
        }
        setSkin(config.laf);
        this.titulo = titulo;
        this.icono = icono;
        initComponents();
        jButton2.setIcon(Constantes.ICON_START);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane(){

            public void paintComponent(Graphics g){
                paintFondo(g);
            }

        }
        ;
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDoubleBuffered(false);
        setFocusable(false);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jDesktopPane1.setBackground(new java.awt.Color(0, 0, 51));
        jDesktopPane1.setDoubleBuffered(true);
        jDesktopPane1.setFocusable(false);
        add(jDesktopPane1);

        jToolBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setFocusable(false);
        jToolBar1.setMaximumSize(new java.awt.Dimension(2048, 35));
        jToolBar1.setMinimumSize(new java.awt.Dimension(39, 35));
        jToolBar1.setPreferredSize(new java.awt.Dimension(319, 35));

        jButton2.setText("Inicio");
        jButton2.setFocusable(false);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setMaximumSize(new java.awt.Dimension(72, 30));
        jButton2.setMinimumSize(new java.awt.Dimension(72, 21));
        jButton2.setPreferredSize(new java.awt.Dimension(72, 21));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator1);

        jPanel1.setFocusable(false);
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 0));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.PAGE_AXIS));
        jToolBar1.add(jPanel1);
        jToolBar1.add(jSeparator2);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel1.setText("  ");
        jLabel1.setFocusable(false);
        jToolBar1.add(jLabel1);
        jToolBar1.add(jSeparator3);

        jPanel3.setMaximumSize(new java.awt.Dimension(70, 19));
        jPanel3.setMinimumSize(new java.awt.Dimension(70, 19));
        jPanel3.setPreferredSize(new java.awt.Dimension(70, 19));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel2.setText(" ");
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 13));
        jPanel3.add(jLabel2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/fabnun/senu/iconos/clear.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(20, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(20, 20));
        jButton1.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jToolBar1.add(jPanel3);

        add(jToolBar1);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (menu != null) {
            menu.show(jDesktopPane1, 1, jDesktopPane1.getHeight() - menu.getHeight() - 1);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Runtime.getRuntime().gc();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    public void showInJFrame(final boolean fullscreen, boolean siempreVisible) {
        config.fullscreen = fullscreen;
        if (actualFrame != null) {
            if (fullscreen) {
                Rectangle r = actualFrame.getBounds();
                config.x = r.x;
                config.y = r.y;
                config.width = r.width;
                config.height = r.height;
                config.state = actualFrame.getExtendedState();
                try {
                    EscritorioConfig.save(config);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            actualFrame.setVisible(false);
            actualFrame.remove(this);
            actualFrame.dispose();
        }
        actualFrame = new JFrame(titulo);
        actualFrame.setAlwaysOnTop(siempreVisible);
        actualFrame.setIconImage(icono);
        actualFrame.add(this);
        actualFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if (fullscreen) {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            actualFrame.setPreferredSize(d);
            actualFrame.setSize(d);
            actualFrame.setUndecorated(true);

            actualFrame.setLocation(0, 0);
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            GraphicsDevice gd = ge.getDefaultScreenDevice();
//            DisplayMode[] modes = gd.getDisplayModes();
//            DisplayMode selectMode = null;
//            int bpp = 0;
//            for (DisplayMode mode : modes) {
//                if (mode.getWidth() == d.getWidth() && mode.getHeight() == d.getHeight() && bpp < mode.getBitDepth()) {
//                    bpp = mode.getBitDepth();
//                    selectMode = mode;
//                }
//            }
//            actualFrame.setUndecorated(true);
//            gd.setFullScreenWindow(actualFrame);
//            gd.setDisplayMode(selectMode);
        } else {
            actualFrame.setBounds(config.x, config.y, config.width, config.height);
            actualFrame.setExtendedState(config.state);
        }
        actualFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                windowClossing(e);
            }
        });
        actualFrame.setIconImage(icono);
        actualFrame.setVisible(true);
    }

    public void exitAndSave(int status) {
        if (!config.fullscreen) {
            Rectangle r = actualFrame.getBounds();
            config.x = r.x;
            config.y = r.y;
            config.width = r.width;
            config.height = r.height;
            config.state = actualFrame.getExtendedState();
        }
        try {
            EscritorioConfig.save(config);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(status);
    }

    public abstract void windowClossing(WindowEvent e);

    public void showDialog(final JPanel panel, final String titulo, Icon icono, final boolean resizable, final boolean closeable) {

        if (panel != null) {
            panel.setVisible(true);
            final JDialog jdialog = new JDialog(actualFrame, titulo, true);
            jdialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            jdialog.add(panel);
            jdialog.setResizable(resizable);
            jdialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    if (closeable) {
                        jdialog.setVisible(false);
                    }
                }
            });
            jdialog.pack();
            jdialog.setLocationRelativeTo(actualFrame);

            panel.addComponentListener(new ComponentAdapter() {

                @Override
                public void componentHidden(ComponentEvent e) {
                    jdialog.dispose();
                    jdialog.setVisible(false);
                }
            });
            jdialog.setVisible(true);
        }
    }
}
