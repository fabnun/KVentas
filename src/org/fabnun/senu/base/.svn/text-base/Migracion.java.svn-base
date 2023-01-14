/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu.base;

import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Migracion {

    public static void main(String[] args) throws SQLException, ParseException {
        //checksum();
        migracion(false);
    }

    public static void checksum() throws SQLException {
        double d1, d2;
        ODBCSenu odbc = new ODBCSenu();
        DBSenu senu = new DBSenu();
        System.out.println("count.productos: "
                + odbc.executeFirstQuery("select count(*) from codigos")[0] + " "
                + senu.executeFirstQuery("select count(*) from precio")[0]);

        System.out.println("ruts.clientes: "
                + odbc.executeFirstQuery("select sum(rut) from clientes")[0] + " "
                + senu.executeFirstQuery("select sum(rut) from cliente where nombre<>'SIN DATOS'")[0]);


        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(precio) from codigos")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(precio) from precio")[0]).doubleValue();
        System.out.println("precios.productos: " + d1 + " " + d2);

        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(subtotal) from facturas")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(subtotal) from factura")[0]).doubleValue();
        System.out.println("subtotales.factura: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(iva) from facturas")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(iva) from factura")[0]).doubleValue();
        System.out.println("ivas: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(iva+subtotal) from facturas")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(total) from factura")[0]).doubleValue();
        System.out.println("total.factuas: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((Double) odbc.executeFirstQuery("select sum(cantidad) from detalle")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(cantidad) from detalle")[0]).doubleValue();
        System.out.println("cantidad.detalle: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(precio) from detalle")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(precio) from detalle")[0]).doubleValue();
        System.out.println("precio.detalle: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((BigDecimal) odbc.executeFirstQuery("select sum(total) from detalle")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(total) from detalle")[0]).doubleValue();
        System.out.println("total.detalle: " + d1 + " " + d2 + " " + (d1 == d2));

        d1 = ((Double) odbc.executeFirstQuery("select sum(rutcliente) from facturas")[0]).doubleValue();
        d2 = ((BigDecimal) senu.executeFirstQuery("select sum(rut) from factura")[0]).doubleValue();
        System.out.println("rut.facturas: " + d1 + " " + d2 + " " + (d1 == d2));

        ResultSet rs = odbc.executeQuery("select fecha from facturas");
        Date d;
        int i0 = 0, i1 = 0;
        while (rs.next()) {
            d = rs.getDate(1);
            i0 = i0 + d.getDay() + d.getMonth() + d.getYear();
        }
        odbc.closeResultSetAndStatement(rs);

        rs = senu.executeQuery("select fecha from factura");
        while (rs.next()) {
            d = rs.getDate(1);
            i1 = i1 + d.getDay() + d.getMonth() + d.getYear();
        }
        System.out.println("fecha.facturas: " + i0 + " " + i1 + " " + (i0 == i1));
        Date dt;
        i0 = 0;
        i1 = 0;
        GregorianCalendar cal0 = new GregorianCalendar();
        GregorianCalendar cal1 = new GregorianCalendar();

        rs = odbc.executeQuery("select fecha, hora from abonos");
        while (rs.next()) {
            dt = rs.getDate(1);
            d = rs.getDate(2);
            cal0.setTime(dt);
            cal1.setTime(d);
            i0 = i0 + cal0.get(GregorianCalendar.DATE) + cal0.get(GregorianCalendar.MONTH) + cal0.get(GregorianCalendar.YEAR)
                    + cal1.get(GregorianCalendar.HOUR_OF_DAY) + cal1.get(GregorianCalendar.MINUTE) + cal1.get(GregorianCalendar.SECOND);
        }
        odbc.closeResultSetAndStatement(rs);

        rs = senu.executeQuery("select fecha from abono");
        while (rs.next()) {
            d = rs.getDate(1);
            cal0.setTime(d);
            i1 = i1 + cal0.get(GregorianCalendar.DATE) + cal0.get(GregorianCalendar.MONTH) + cal0.get(GregorianCalendar.YEAR)
                    + cal0.get(GregorianCalendar.HOUR_OF_DAY) + cal0.get(GregorianCalendar.MINUTE) + cal0.get(GregorianCalendar.SECOND);
        }
        System.out.println("fecha.abonos: " + i0 + " " + i1 + " " + (i0 == i1));


        d1 = ((Integer) odbc.executeFirstQuery("select count(*) from abonos")[0]).intValue();
        d2 = ((Long) senu.executeFirstQuery("select count(*) from abono")[0]).intValue();
        System.out.println("rut.facturas: " + d1 + " " + d2 + " " + (d1 == d2));




    }

    public static void migracion(boolean checkExist) throws SQLException, ParseException {

        JFrame frame = new JFrame("Migracion");
        frame.setLayout(new GridLayout(3, 1));
        JLabel carga = new JLabel();
        JLabel vel = new JLabel();
        JProgressBar bar = new JProgressBar();
        frame.add(carga, "1");
        frame.add(bar, "2");
        frame.add(vel, "3");
        frame.setSize(800, 80);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        long t0 = System.currentTimeMillis();
        String s0, s1, s2, s3, s4, s5;
        int i0, i1, i2, i3, i4, i5, count;
        BigDecimal d0, d1, d2, d3, d4, d5;
        Double dd0, dd1, dd2;
        Date dt0, dt1;
        boolean b0, b1, b2;
        Object[] o1, o2;
        ODBCSenu odbc = new ODBCSenu();
        DBSenu db = new DBSenu();
        ResultSet rs;

        carga.setText("Cargando ciudades (1/7)");///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getCiudadesCount());
        bar.setValue(0);
        count = 0;

        rs = odbc.getCiudades();
        while (rs.next()) {
            s0 = rs.getString(1);
            i0 = db.getIdCiudad(s0);
            if (i0 == -1) {
                db.insertCiudad(s0);
            }
            count++;
            bar.setValue(count);
        }

        carga.setText("Cargando comunas (2/7)");///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getComunasCount());
        bar.setValue(0);
        count = 0;

        rs = odbc.getComunas();
        while (rs.next()) {
            s0 = rs.getString(1);
            i0 = db.getIdComuna(s0);
            if (i0 == -1) {
                db.insertComuna(s0);
            }
            count++;
            bar.setValue(count);
        }

        carga.setText("Cargando clientes (3/7)");///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getClientesCount());
        bar.setValue(0);
        count = 0;

        rs = odbc.getClientes();
        while (rs.next()) {
            i0 = rs.getInt(1);//rut
            s0 = rs.getString(2);//nombre
            s1 = rs.getString(3);//dir
            s2 = rs.getString(4);//comuna
            s3 = rs.getString(5);//ciudad
            s4 = rs.getString(6);//giro
            s5 = rs.getString(7);//otros
            if (checkExist) {
                if (db.getCliente(i0) == null) {
                    db.insertCliente(i0, s0, s1, s2, s3, s4, "", s5);
                }
            } else {
                db.insertCliente(i0, s0, s1, s2, s3, s4, "", s5);
            }
            count++;
            bar.setValue(count);
        }

        carga.setText("Cargando productos (4/7)");///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getProductosCount());
        bar.setValue(0);
        count = 0;

        rs = odbc.getProductos();
        Timestamp now = new Timestamp(new Date().getTime());
        while (rs.next()) {
            s0 = rs.getString(1);//codigo
            s1 = rs.getString(2);//articulo
            d0 = rs.getBigDecimal(3);//precio
            if (checkExist) {
                if (db.getProducto(s0) == null) {
                    db.insertProducto(s0, s1, d0, now);
                }
            } else {
                db.insertProducto(s0, s1, d0, now);
            }
            count++;
            bar.setValue(count);
        }

        HashSet<Integer> ruts = new HashSet<Integer>();
        rs = db.executeQuery("select distinct rut from cliente");
        while (rs.next()) {
            ruts.add(rs.getInt(1));
        }

        carga.setText("Cargando facturas (5/7)");///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getFacturasCount());
        bar.setValue(0);
        count = 0;

        rs = odbc.getFacturas();
        int except = 0;
        while (rs.next()) {
            i0 = rs.getInt(1);//nfact
            if (!(i0 == 269700 && except == 0)) {
                i1 = rs.getInt(2);//rut
                dt0 = rs.getDate(3);//fecha
                s0 = rs.getString(4);//ocompra
                s1 = rs.getString(5);//cventa
                s2 = rs.getString(6);//nguia
                s3 = rs.getString(7);//venc
                dd0 = rs.getDouble(8);//subtotal
                dd1 = rs.getDouble(9);//iva
                dd2 = dd0 + dd1;//total
                b0 = rs.getInt(10) == 1;//cancelada
                b1 = rs.getInt(11) == 1;//impresa
                b2 = rs.getInt(12) == 1;//anulada
                if (!ruts.contains(i1) && db.getCliente(i1) == null) {
                    db.insertCliente(i1, "SIN DATOS", "SIN DATOS", "SIN DATOS", "SIN DATOS", "SIN DATOS", "", "");
                }
                if (checkExist) {
                    if (db.getFactura(i0) == null) {
                        db.insertFactura(i0, i1, dt0, s0, s1, s2, s3, dd0, dd1, dd2, b0, b1, b2);
                    }
                } else {
                    db.insertFactura(i0, i1, dt0, s0, s1, s2, s3, dd0, dd1, dd2, b0, b1, b2);
                }
                count++;
                bar.setValue(count);
            } else {
                except++;
                System.out.println("Except");
            }
        }



        HashSet<Integer> facturas = new HashSet<Integer>();
        rs = db.executeQuery("select distinct nfact from factura");
        while (rs.next()) {
            facturas.add(rs.getInt(1));
        }

        carga.setText("Cargando detalle facturas (6/7)");///////////////////////////////////////////////////////////////////////////////////
        int total = odbc.getDetalleFactCount();
        bar.setMaximum(total);
        bar.setValue(0);
        count = 0;


        rs = odbc.getDetalleFact();
        long tt = System.currentTimeMillis();
        while (rs.next()) {
            i0 = rs.getInt(1);//nfact
            i1 = rs.getInt(2);//nlinea
            s0 = rs.getString(3);//codigo
            s1 = rs.getString(4);//detalle
            d0 = rs.getBigDecimal(5);//cantidad
            dd1 = rs.getDouble(6);//precio
            dd2 = rs.getDouble(7);//total
            try {
                db.insertDetalle(i0, i1, s0, s1, d0, dd1, dd2);
            } catch (Exception e) {
                System.err.print("nfact " + i0);
                e.printStackTrace();
            }
            count++;
            if (count % 1000 == 0) {
                long ttt = System.currentTimeMillis();
                vel.setText("faltan " + ((double) (total - count) / 1000d) * ((double) (ttt - tt) / 1000d) + "  segundos");
                tt = ttt;
                bar.setValue(count);
            }
        }

        carga.setText("Cargando abonos (7/7)");///////////////////////////////////////////////////////////////////////////////////
        bar.setMaximum(odbc.getAbonosCount());
        bar.setValue(0);
        count = 0;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        rs = odbc.getAbonos();
        while (rs.next()) {
            i0 = rs.getInt(1);//nfact
            s0 = rs.getString(2);
            dt0 = sdf2.parse(s0);//fecha
            s0 = rs.getString(3);
            dt1 = sdf2.parse(s0);//hora
            d0 = rs.getBigDecimal(4);//abono
            db.insertAbono(i0, dt0, dt1, d0);
            count++;
            bar.setValue(count);
        }

        long t1 = System.currentTimeMillis();
        System.out.println(((t1 - t0) / 1000) + " segundos");
    }
}
