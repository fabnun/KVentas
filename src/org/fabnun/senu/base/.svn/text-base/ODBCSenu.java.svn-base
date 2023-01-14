/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu.base;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * * @author fabnun
 */
public class ODBCSenu extends DBManager {

    public ODBCSenu() {
        super("sun.jdbc.odbc.JdbcOdbcDriver", " ", " ", new String[]{"jdbc:odbc:senu2"}, "charSet=iso-8859-1");
    }

    public static void main(String[] args) throws SQLException {
        ODBCSenu db=new ODBCSenu();
        System.out.println(db.executeQueryList("select max(nfact) from facturas"));
    }

    public ResultSet getClientes() throws SQLException {
        return executeQuery("select rut, nombre, direc, comuna, ciudad, giro, otros  from clientes");
    }

    public ResultSet getCiudades() throws SQLException {
        return executeQuery("select distinct ciudad from clientes");
    }

    public ResultSet getComunas() throws SQLException {
        return executeQuery("select distinct comuna from clientes");
    }

    public ResultSet getProductos() throws SQLException {
        return executeQuery("select codigo, articulo, precio from codigos");
    }

    public ResultSet getDetalleFact() throws SQLException {
        return executeQuery("select nfact, nlinea, codigo, detalle, cantidad, precio, total from detalle order by nfact");
    }

    public ResultSet getDetalleCoti() throws SQLException {
        return executeQuery("select ncot, nlinea, codigo, detalle, cantidad, precio, total from datacoti");
    }

    public ResultSet getFacturas() throws SQLException {
        return executeQuery("select nfact, rutcliente, fecha, ocompra, cventa, nguia, vencimiento, subtotal, iva, cancelada, impresa, anulada from facturas order by nfact");
    }

    public ResultSet getCotizaciones() throws SQLException {
        return executeQuery("select ncot, rutcliente, total  from coti order by ncot");
    }

    public ResultSet getAbonos() throws SQLException {
        return executeQuery("select nfact, fecha, hora, abono from abonos order by fecha, hora");
    }

    public int getAbonosCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*) from abonos;")[0]).intValue();
    }

    public int getDetalleFactCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*) from detalle")[0]).intValue();
    }

    public int getDetalleCotiCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*) from datacoti")[0]).intValue();
    }

    public int getCotizacionCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*)  from coti")[0]).intValue();
    }

    public int getClientesCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*)  from clientes")[0]).intValue();
    }

    public int getCiudadesCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*) from (select distinct ciudad  from clientes) a")[0]).intValue();
    }

    public int getComunasCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*) from (select distinct comuna  from clientes) a")[0]).intValue();
    }

    public int getProductosCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*)   from codigos")[0]).intValue();
    }

    public int getFacturasCount() throws SQLException {
        return ((Integer) executeFirstQuery("select count(*)   from facturas")[0]).intValue();
    }

    public String checkCharacterCodification() throws SQLException {
        return (String) executeFirstQuery("select articulo from codigos where articulo like 'perla%16%18%'")[0];
    }

    @Override
    public String creationScript() {
        return "";
    }

    
}
