package org.fabnun.senu;

import org.fabnun.senu.vista.QueryPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.fabnun.senu.vista.Configuracion;

public class SenuMenu extends JPopupMenu {

    JMenuItem senuItem = new JMenuItem("Senu", Constantes.ICON_SENU);
    JMenuItem contaItem = new JMenuItem("Contabilidad", Constantes.ICON_CONTA);
    JMenuItem printItem = new JMenuItem("Configurar Impresión", Constantes.ICON_IMPRIMIR);
    JMenuItem facturasItem = new JMenuItem("Facturas y Abonos", Constantes.ICON_FACTURAS);
    JMenuItem productosItem = new JMenuItem("Precios", Constantes.ICON_PRODUCTOS);
    JMenuItem notasItem = new JMenuItem("Notas", Constantes.ICON_NOTAS);
    JMenuItem usuariosItem = new JMenuItem("Usuarios", Constantes.ICON_USUARIO);
    JMenuItem clientesItem = new JMenuItem("Clientes", Constantes.ICON_CLIENTE);
    JMenuItem configuracionItem = new JMenuItem("Configuracion Gráfica", Constantes.ICON_CONFIG);
    JMenuItem exitItem = new JMenuItem("Salir", Constantes.ICON_EXIT);
    JMenuItem backupItem = new JMenuItem("Respaldar", Constantes.ICON_BACKUP);
    SenuEscritorio escritorio;
    Configuracion configuracion;
    QueryPanel exitPanel = new QueryPanel(280, 100, "¿Realemente desea salir?", "Si", "No") {

        @Override
        public void buttonPressed(int idxButton) {
            if (idxButton == 0) {
                escritorio.exitAndSave(0);
            }
        }
    };

    public SenuMenu(final SenuEscritorio escritorio,
            final Configuracion configuracion) {
        super();
        this.escritorio = escritorio;
        this.configuracion = configuracion;
        if (escritorio.permisoFacturar) {
            add(senuItem);
        }
        if (escritorio.permisoVerFacturas) {
            add(facturasItem);
        }
        if (escritorio.permisoEditarPrecios) {
            add(productosItem);
        }
        if (escritorio.permisoEditarClientes) {
            add(clientesItem);
        }
        if (escritorio.permisoVerContabilidad) {
            add(contaItem);
        }
        add(backupItem);
        add(notasItem);
        if (escritorio.permisoEditarUsuarios) {
            add(usuariosItem);
        }
        if (escritorio.permisoEditarConfiguracionGrafica) {
            add(configuracionItem);
        }
        if (escritorio.permisoEditarConfiguracionImpr) {
            add(printItem);
        }
        add(exitItem);

        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.showDialog(exitPanel, "Salir", null, false, true);
            }
        });

        configuracionItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarConfiguracionGrafica) {
                    escritorio.addVentana("Configuración Gráfica", Constantes.ICON_CONFIG,
                            false, true, false, true, configuracion);
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar la configuración grafica");
                }
            }
        });

        contaItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoVerContabilidad) {
                    escritorio.addVentana("Contabilidad", Constantes.ICON_CONTA,
                            true, true, true, true, new Contabilidad(escritorio.permisoEditarEgresos));
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para ver la contabilidad");
                }
            }
        });

        notasItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarNotas) {
                    Notas notas = new Notas(escritorio.permisoEditarNotas);
                    notas.setUsuario(escritorio.usuario);
                    escritorio.addVentana("Notas", Constantes.ICON_NOTAS,
                            true, true, true, true, notas);
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar notas");
                }
            }
        });

        backupItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Runtime rt = Runtime.getRuntime();
                Properties props = new Properties();
                String pg = "C:\\Program Files\\PostgreSQL\\9.0\\bin\\pg_dump.exe";
                int port = 5432;
                String user = "postgres";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.hhmmss");
                File file = new File("C:\\Documents and Settings\\fabnun\\Desktop\\senu"
                        + sdf.format(new Date()) + ".backup");

                try {
                    props.load(new FileInputStream("backup.properties"));
                    pg = props.getProperty("pgpath");
                    file = new File(props.getProperty("path")+"."
                            + sdf.format(new Date()) + ".backup");
                    FileOutputStream fos = new FileOutputStream("backup.bat");
                    String script = "\"" + pg + "\" --host localhost --port 5432 --username \"postgres\" "
                            + "--format custom --blobs --verbose --file \"" + file.getAbsolutePath() + "\" senu\n@exit";
                    PrintWriter pw = new PrintWriter(fos);
                    pw.write(script);
                    pw.close();
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    if (JOptionPane.showConfirmDialog(escritorio, "Desea respaldar\nen " + file.getAbsolutePath() + "?", "", JOptionPane.YES_NO_OPTION) == 0) {
                        String exec = "cmd /c start \"Respaldo\" backup.bat";
                        System.out.println(exec);
                        Process p = rt.exec(exec);
                        p.waitFor();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(escritorio, "No se pudo respaldar:\n" + ex.getMessage());
                }

            }
        });

        usuariosItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarUsuarios) {
                    escritorio.addVentana("Usuarios", Constantes.ICON_USUARIO,
                            false, true, false, true, new UserAdmin());
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar usuarios");
                }
            }
        });
        printItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarConfiguracionImpr) {
                    escritorio.addVentana("Configurar Impresión", Constantes.ICON_IMPRIMIR,
                            true, true, true, true, new PrintConfig());
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar la configuracion de impresión");
                }
            }
        });
        senuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoFacturar) {
                    escritorio.addVentana("Senu", Constantes.ICON_SENU,
                            true, true, true, true, new Factura(escritorio, escritorio.permisoAbonar, escritorio.permisoAnular));
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para facturar");
                }
            }
        });

        facturasItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoVerFacturas) {
                    escritorio.addVentana("Facturas y Abonos", Constantes.ICON_FACTURAS,
                            true, true, true, true, new Facturas(escritorio));
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para ver facturas");
                }
            }
        });

        productosItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarPrecios) {
                    escritorio.addVentana("Precios", Constantes.ICON_PRODUCTOS,
                            true, true, true, true, new Producto());
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar precios");
                }
            }
        });

        clientesItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escritorio.updatePermisos();
                if (escritorio.permisoEditarClientes) {
                    escritorio.addVentana("Clientes", Constantes.ICON_CLIENTE,
                            true, true, true, true, new Clientes(escritorio, escritorio.permisoAbonar, null));
                } else {
                    JOptionPane.showMessageDialog(escritorio, "Ya no tiene permiso para editar clientes");
                }
            }
        });


    }
}
