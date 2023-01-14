package org.fabnun.senu;

import org.fabnun.senu.vista.QueryPanel;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import org.fabnun.senu.base.DBSenu.Permiso;
import org.fabnun.senu.vista.Configuracion;
import org.fabnun.senu.vista.Escritorio;

public class SenuEscritorio extends Escritorio {

    public String usuario = null;
    public boolean permisoFacturar = false;
    public boolean permisoAnular = false;
    public boolean permisoAbonar = false;
    public boolean permisoVerFacturas = false;
    public boolean permisoVerContabilidad = false;
    public boolean permisoEditarPrecios = false;
    public boolean permisoEditarEgresos = false;
    public boolean permisoEditarNotas = false;
    public boolean permisoEditarUsuarios = false;
    public boolean permisoEditarConfiguracionGrafica = false;
    public boolean permisoEditarConfiguracionImpr = false;
    public boolean permisoEditarClientes = false;

    public SenuEscritorio() {
        super("Senu", Constantes.IMG_SENU);
        final SenuEscritorio este = this;
    }

    public void login() {
        Login login = new Login(this);
        login.limpiar();
        showDialog(login, "Ingrese su usuario y clave", Constantes.ICON_LOGIN, false, false);
        updatePermisos();
    }

    public void updatePermisos() {
        try {
            permisoFacturar = Constantes.db.tienePermiso(usuario, Permiso.permisoFacturar);
            permisoAnular = Constantes.db.tienePermiso(usuario, Permiso.permisoAnular);
            permisoAbonar = Constantes.db.tienePermiso(usuario, Permiso.permisoAbonar);
            System.out.println(permisoAbonar);
            permisoVerFacturas = Constantes.db.tienePermiso(usuario, Permiso.permisoVerFacturas);
            permisoVerContabilidad = Constantes.db.tienePermiso(usuario, Permiso.permisoVerContabilidad);
            permisoEditarPrecios = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarPrecios);
            permisoEditarEgresos = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarEgresos);
            permisoEditarNotas = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarNotas);
            permisoEditarUsuarios = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarUsuarios);
            permisoEditarConfiguracionGrafica = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarConfiguracionGrafica);
            permisoEditarConfiguracionImpr = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarConfiguracionImpr);
            permisoEditarClientes = Constantes.db.tienePermiso(usuario, Permiso.permisoEditarClientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("awt.toolkit", "org.fabnun.senu.vista.MyToolkit");

        final SenuEscritorio escritorio = new SenuEscritorio();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                escritorio.showInJFrame(escritorio.config.fullscreen, escritorio.config.siempreVisible);
                Configuracion configuracion = new Configuracion(escritorio);
                escritorio.login();
                SenuMenu menu = new SenuMenu(escritorio, configuracion);
                escritorio.setVisibleTime(escritorio.config.showDateTime);
                escritorio.setVisibleMem(escritorio.config.showMem);
                escritorio.setMenu(menu);
                escritorio.setSkin(escritorio.config.laf);
            }
        });

        //escritorio.login();

    }

    @Override
    public void windowClossing(WindowEvent e) {
        QueryPanel exitPanel = new QueryPanel(280, 100, "¿Realemente desea salir?", "Si", "No") {

            @Override
            public void buttonPressed(int idxButton) {
                if (idxButton == 0) {
                    exitAndSave(0);
                }
            }
        };
        showDialog(exitPanel, "Salir", null, false, false);
    }
}
