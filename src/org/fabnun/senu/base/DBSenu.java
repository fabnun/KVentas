package org.fabnun.senu.base;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.fabnun.senu.Constantes;
import org.fabnun.senu.ObjectArray;

public class DBSenu extends DBManager {

    public enum Permiso {

        permisoFacturar(5),
        permisoAnular(6),
        permisoAbonar(7),
        permisoVerFacturas(8),
        permisoVerContabilidad(9),
        permisoEditarPrecios(10),
        permisoEditarEgresos(11),
        permisoEditarNotas(12),
        permisoEditarUsuarios(13),
        permisoEditarConfiguracionGrafica(14),
        permisoEditarConfiguracionImpr(15),
        permisoEditarClientes(16);
        /////////////////
        int id;

        Permiso(int id) {
            this.id = id;
        }
    };

    private enum SQL {

        CREAR_SESSION("insert into session (id, usuario, inicio, termino, maquina) values (?,?,?,?,?)"),
        TIENE_PERMISO("select permiso_id from permisos where permiso_id=? and usuario=?"),
        TIENE_SESSION_ACTIVA("select id from session where id=? and usuario=? and termino=null and inicio<?"),
        BUSCA_CIUDAD("select ciudad from ciudad where ciudad like ? order by ciudad "),
        BUSCA_COMUNA("select comuna from comuna where comuna like ? order by comuna "),
        BUSCA_GIRO("select giro from giro where giro like ? order by giro"),
        GET_CIUDAD("select ciudad from ciudad where id=?"),
        GET_COMUNA("select comuna from comuna where id=?"),
        GET_GIRO("select giro from giro where id=? "),
        INSERTA_GIRO("insert into giro (giro) values (?)"),
        INSERTA_CIUDAD("insert into ciudad (ciudad) values (?)"),
        INSERTA_COMUNA("insert into comuna (comuna) values (?)"),
        GETID_COMUNA("select id from comuna where comuna=?"),
        GETID_CIUDAD("select id from ciudad where ciudad=?"),
        GETID_GIRO("select id from giro where giro=?"),
        INSERTA_CLIENTE("insert into cliente (rut, nombre, direccion, telefono, otros, ciudad_id, comuna_id, giro_id) "
        + "values (?,?,?,?,?,?,?,?)"),
        BUSCA_RUT("select nombre, rut, direccion, telefono, otros, ciudad_id, comuna_id, giro_id from cliente where rut=? limit 1"),
        BUSCA_CLIENTE("select nombre, rut, direccion, telefono, otros, ciudad_id, comuna_id, giro_id from cliente "
        + "where nombre like ? order by nombre"),
        GET_CLIENTE("select nombre, rut, direccion, telefono, otros, ciudad_id, comuna_id, giro_id from cliente where rut=?"),
        INSERTA_PRODUCTO("insert into producto (codigo, detalle) values (?,?)"),
        UPDATE_PRODUCTO_DETALLE("update producto set detalle=? where codigo=?"),
        UPDATE_PRODUCTO_CODIGO("update producto set codigo=? where codigo=?"),
        ADD_PRODUCTO_PRECIO("insert into precio (codigo, fecha, precio) values (?,?,?)"),
        GET_PRODUCTO("select codigo, detalle  from producto  where codigo=?"),
        INSERTA_PRECIO("insert into precio (codigo, fecha, precio) values (?,?,?)"),
        DEL_PRODUCTO("delete from producto where codigo=?"),
        EXIST_PRODUCTO("SELECT codigo from producto where codigo=?"),
        EXIST_RUT("SELECT rut from cliente where rut=?"),
        EXIST_FACT("SELECT nfact from factura where nfact=?"),
        DEL_PRECIO("delete from precio where codigo=?"),
        GET_FACTURA("select a.nfact, a.rut, b.nombre, b.telefono,  b.direccion, b.comuna_id, b.ciudad_id, b.giro_id, a.fecha, "
        + "a.ocompra, a.cventa, a.nguia, a.vencimiento, a.subtotal, a.iva, a.total, a.anulada, a.cancelada from factura a, cliente b "
        + "where a.rut=b.rut and a.nfact=?"),
        INSERTA_FACTURA("INSERT INTO factura (nfact, rut, fecha, ocompra, cventa, nguia, vencimiento, subtotal, iva, total, "
        + "cancelada,impresa,anulada) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"),
        GET_PRECIO("select precio from precio where codigo=? and fecha<=? order by fecha desc limit 1"),
        BUSCA_POR_PRODUCTO("select a.codigo, a.detalle, (select precio from precio where codigo=a.codigo and fecha<=? order by fecha desc limit 1) as precio from producto a where a.detalle like ? order by a.detalle"),
        BUSCA_POR_COD("select a.codigo, a.detalle, (select precio from precio where codigo=a.codigo and fecha<=? order by fecha desc limit 1) as precio from producto a where a.codigo=?"),
        GET_DETALLE("select nfact, linea, codigo, detalle, cantidad, precio, total from detalle where nfact=?"
        + "order by linea "),
        INSERTA_DETALLE("INSERT INTO detalle (nfact, linea, codigo, detalle, cantidad, precio, total) "
        + " VALUES (?,?,?,?,?,?,?)"),
        BUSCA_POR_CODIGO("select a.codigo, a.detalle, b.precio, b.fecha from producto a, precio b "
        + "where a.codigo=b.codigo and b.fecha<=? and a.codigo=? limit 1"),
        INSERTA_ABONO("INSERT INTO abono (nfact, fecha, abono) values (?,?,?)"),
        GET_FACTURAS_CLIENTE("select a.nfact, a.fecha, a.total, 0, "
        + " a.cancelada, a.impresa, a.anulada, a.cventa from factura a where a.rut=? and nfact>0 "
        + "order by a.cancelada+a.anulada>0, a.nfact desc"),
        GET_FACTURAS_CLIENTE2("select a.nfact, a.fecha, a.total, (select sum(abono) from abono b where a.nfact=b.nfact), "
        + " a.cancelada, a.impresa, a.anulada, a.cventa from factura a where a.rut=? and nfact>0 and anulada=0 and cancelada=0 "
        + "order by a.cancelada, a.fecha desc, a.nfact desc"),
        GET_ULTIMA_FACTURA("select max(nfact) from factura"),
        GET_SIGUIENTE_FACTURA("select min(nfact) from factura where nfact>?"),
        GET_ANTERIOR_FACTURA("select max(nfact) from factura where nfact<?"),
        AUTORIZAR("select usuario from usuario where usuario=? and clave=?"),
        GET_ABONOS("select id ,fecha, abono from abono where nfact=? order by fecha"),
        GET_FACTURA_CLIENTE("select  a.total, (select sum(abono) from abono b where a.nfact=b.nfact) from factura a "
        + "where a.nfact=?"),
        SUM_ABONO("select sum(abono) from abono where nfact=?"),
        TOTAL_FACTURA("select sum(total) from factura where nfact=?"),
        SET_FACTURA_CANCELADA("update factura set cancelada=? where nfact=?"),
        GET_NOTAS("SELECT titulo, usuario, texto, cfondo, ctexto, inicio, repetir, patron, destinatarios from nota where usuario=?"),
        GET_USUARIOS("SELECT usuario from usuario order by usuario"),
        NUEVA_NOTA("INSERT INTO nota(titulo, usuario, texto, cfondo, ctexto, inicio, repetir, patron, destinatarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"), //
        SET_NOTA("UPDATE nota SET texto=?, cfondo=?, ctexto=?, inicio=?, repetir=?, "
        + "patron=?, destinatarios=? WHERE titulo=? and usuario=?"),
        DEL_NOTA("DELETE FROM nota WHERE titulo=? and usuario=?"),
        GET_USUARIO("SELECT usuario, clave, nombre, direccion, telefono, correo FROM usuario where usuario=?"),
        NUEVO_USUARIO("INSERT INTO usuario(usuario, clave, nombre, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?, ?)"), //
        DEL_USUARIO("DELETE FROM usuario WHERE usuario=?"),
        UPDATE_USUARIO("UPDATE usuario SET nombre=?, direccion=?, telefono=?, correo=? WHERE usuario=?"),
        SET_CLAVE("UPDATE usuario SET clave=? WHERE usuario=?"),
        GET_PERMISOS("select a.id, a.permiso, (select count(*) from permisos where permiso_id=a.id and usuario=?) as activo from permiso a"),
        SET_PERMISO("insert into permisos (permiso_id, usuario) values (?,?)"),
        UNSET_PERMISO("delete from permisos where permiso_id=? and usuario=?"),
        ANULAR_FACTURA("UPDATE factura set anulada=? where nfact=?"),
        ADD_FACTURA("INSERT INTO factura(nfact, rut, fecha, ocompra, cventa, nguia, "
        + "vencimiento, subtotal, iva, total, cancelada, impresa, anulada)"
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"),
        ADD_DETALLE("INSERT INTO detalle(nfact, linea, codigo, detalle, cantidad, precio, total) VALUES (?, ?, ?, ?, ?, ?, ?)"),
        UPDATE_CLIENTE("UPDATE cliente SET rut=?, nombre=?, direccion=?, telefono=?, ciudad_id=?, comuna_id=?, giro_id=? WHERE rut=?"),
        ADD_CLIENTE("INSERT INTO cliente(rut, nombre, direccion, telefono, ciudad_id, comuna_id, giro_id) VALUES (?, ?, ?, ?, ?, ?, ?)"),
        MIN_FACT("select min(nfact) from factura"),
        GET_COTI("select -a.nfact, a.fecha, b.nombre from factura a, cliente b where a.rut=b.rut and a.nfact<0 and impresa=0 order by nfact desc"),
        DEL_COTIDETA("delete from detalle where nfact=?"),
        DEL_COTIABONO("delete from abono where nfact=?"),
        DEL_COTI("delete from factura where nfact=?"),
        GET_CONTA1("select  a.per, sum(a.cuantas), sum(a.facturado), sum(a.cancelado), sum(a.abono), \n"
        + "	sum(a.cancelado+a.abono-a.facturado) as deuda, sum(a.pagos), sum(a.cancelado+a.abono-a.pagos) as suma \n"
        + "from \n"
        + "	(select a.per, a.cuantas, a.facturado, a.cancelado, (case when a.abono is null then 0 else a.abono end) as abono , \n"
        + "	(case when a.pagos is null then 0 else a.pagos end) as pagos \n"
        + "from\n"
        + "	(select to_char(EXTRACT(YEAR FROM a.fecha),'9999') || to_char(EXTRACT(MONTH FROM a.fecha),'00') || to_char(EXTRACT(DAY FROM a.fecha),'00') as per, count(a.*) as cuantas, sum(a.total) as facturado, \n"
        + "	sum(case when a.cancelada=1 then a.total else 0 end) as cancelado,\n"
        + "	(select sum(b.abono) from abono b, factura c\n"
        + "	where b.nfact=c.nfact and b.fecha=a.fecha and c.anulada=0 and c.cancelada=0) as abono,\n"
        + "	(select sum(total) from compra where fecha=a.fecha) as pagos	\n"
        + "from \n"
        + "	factura a\n"
        + "where \n"
        + "	a.anulada=0 and a.nfact>0 \n"
        + "	and a.fecha>=date(?) - interval '3 month'\n"
        + "	and a.fecha<=date(?)\n"
        + "group by\n"
        + "	a.fecha) a) a \n"
        + "group by per order by per desc"),
        GET_CONTA2(
        "select  a.per, sum(a.cuantas), sum(a.facturado), sum(a.cancelado), sum(a.abono), \n"
        + "	sum(a.cancelado+a.abono-a.facturado) as deuda, sum(a.pagos), sum(a.cancelado+a.abono-a.pagos) as suma \n"
        + "from \n"
        + "	(select a.per, a.cuantas, a.facturado, a.cancelado, (case when a.abono is null then 0 else a.abono end) as abono , \n"
        + "	(case when a.pagos is null then 0 else a.pagos end) as pagos \n"
        + "from\n"
        + "	(select to_char(EXTRACT(YEAR FROM a.fecha),'9999') || to_char(EXTRACT(WEEK FROM a.fecha),'00') as per, count(a.*) as cuantas, sum(a.total) as facturado, \n"
        + "	sum(case when a.cancelada=1 then a.total else 0 end) as cancelado,\n"
        + "	(select sum(b.abono) from abono b, factura c\n"
        + "	where b.nfact=c.nfact and b.fecha=a.fecha and c.anulada=0 and c.cancelada=0) as abono,\n"
        + "	(select sum(total) from compra where fecha=a.fecha) as pagos	\n"
        + "from \n"
        + "	factura a\n"
        + "where \n"
        + "	a.anulada=0 and a.nfact>0 \n"
        + "	and a.fecha>=date(?) - interval '6 month'\n"
        + "	and a.fecha<=date(?)\n"
        + "group by\n"
        + "	a.fecha) a) a \n"
        + "group by per order by per desc"),
        GET_CONTA3("select  a.per, sum(a.cuantas), sum(a.facturado), sum(a.cancelado), sum(a.abono), \n"
        + "	sum(a.cancelado+a.abono-a.facturado) as deuda, sum(a.pagos), sum(a.cancelado+a.abono-a.pagos) as suma \n"
        + "from \n"
        + "	(select a.per, a.cuantas, a.facturado, a.cancelado, (case when a.abono is null then 0 else a.abono end) as abono , \n"
        + "	(case when a.pagos is null then 0 else a.pagos end) as pagos \n"
        + "from\n"
        + "	(select to_char(EXTRACT(YEAR FROM a.fecha),'9999') || to_char(EXTRACT(MONTH FROM a.fecha),'00') as per, count(a.*) as cuantas, sum(a.total) as facturado, \n"
        + "	sum(case when a.cancelada=1 then a.total else 0 end) as cancelado,\n"
        + "	(select sum(b.abono) from abono b, factura c\n"
        + "	where b.nfact=c.nfact and b.fecha=a.fecha and c.anulada=0 and c.cancelada=0) as abono,\n"
        + "	(select sum(total) from compra where fecha=a.fecha) as pagos	\n"
        + "from \n"
        + "	factura a\n"
        + "where \n"
        + "	a.anulada=0 and a.nfact>0 \n"
        + "	and a.fecha>=date(?) - interval '12 month'\n"
        + "	and a.fecha<=date(?)\n"
        + "group by\n"
        + "	a.fecha) a) a \n"
        + "group by per order by per desc"),
        BUSCA_EGRESO1("SELECT proveedor from compra where proveedor like ?"),
        BUSCA_EGRESO2("SELECT producto from compra where producto like ?"),
        BUSCA_EGRESO3("SELECT otros from compra where otros like ?"),
        GET_NEXT_COMPRA("SELECT max(id) from compra"),
        GET_EGRESO("select id, fecha, cantidad, proveedor, producto, otros, precio, total, iva from compra where "
        + "proveedor like ? and  producto like ? and otros like ? order by fecha, id desc"),
        INSERTA_EGRESO("INSERT INTO compra(id, fecha, proveedor, producto, otros, cantidad, precio, total, iva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"),
        UPDATE_EGRESO("UPDATE compra SET fecha=?, proveedor=?, producto=?, otros=?, cantidad=?, precio=?, total=?, iva=? WHERE id=?;"),
        GET_INGRESOS("select a.nfact, a.fecha, a.rut, b.nombre, (case when anulada=1 then 0 else a.subtotal end) as subtotal, (case when anulada=1 then 0 else a.iva end) as iva, (case when anulada=1 then 0 else a.total end) as total "
        + "from factura a, cliente b where a.rut=b.rut and nfact>0 "
        + "and EXTRACT(YEAR FROM a.fecha)=? and EXTRACT(MONTH FROM a.fecha)=? "
        + "order by fecha, nfact"),
        GET_EGRESOS("select id, fecha, proveedor, producto, iva, total from compra "
        + "where EXTRACT(YEAR FROM fecha)=? and EXTRACT(MONTH FROM fecha)=? "
        + "order by fecha, id"),
        DEL_ABONOS("delete from abono where nfact=?"),
        SET_SIN_CANCELAR("update factura set cancelada=0 where nfact=?"),
        UPDATE_RUT("INSERT INTO cliente SELECT ?, nombre, direccion, telefono, "
        + "otros, ciudad_id, comuna_id, giro_id FROM cliente where rut=?;\n"
        + "UPDATE factura SET rut=? WHERE rut=?;"
        + "DELETE FROM cliente WHERE rut=?"),
        ELIMINA_ABONO("delete from abono where id in (select id from abono where nfact=? and fecha=? and abono=? limit 1);"
        + "update factura set cancelada=0 where cancelada=1 and nfact=?;"),
        ULTIMO_ABONO("select date_part('days',now()-max(fecha)) from abono where nfact in (select nfact from factura where rut=? and anulada=0)"),
        GET_HISTORIA("select nfact, total, month, cancelada from"
        + "(select a.nfact, a.abono as total, date_part('year',a.fecha)*100+date_part('month',a.fecha) as month, 2 as cancelada\n"
        + "from abono a where a.nfact in \n"
        + "(select nfact from factura where rut=? and anulada=0) \n"
        + "union select nfact,  total, date_part('year',fecha)*100+date_part('month',fecha) as month, cancelada\n"
        + "from factura where rut=? and anulada=0) a order by nfact, cancelada"),
        GET_ALERTA("select nfact, total from factura where cancelada=0 and anulada=0 and rut=?");
        private String query;
        private PreparedStatement pstmt;

        private void setParams(Object... params) throws SQLException {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        }

        public boolean execute(Object... params) throws SQLException {
            setParams(params);
            //System.out.println(pstmt);
            return pstmt.execute();
        }

        public int executeUpdate(Object... params) throws SQLException {
            setParams(params);
            //System.out.println(pstmt);
            return pstmt.executeUpdate();
        }

        public List<Object[]> executeQuery(Object... params) throws SQLException {
            LinkedList<Object[]> result = new LinkedList<Object[]>();
            setParams(params);
            //System.out.println(pstmt);
            ResultSet rs = pstmt.executeQuery();
            int cols = rs.getMetaData().getColumnCount();
            Object[] obj;
            while (rs.next()) {
                obj = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    obj[i] = rs.getObject(i + 1);
                }
                result.add(obj);
            }
            rs.close();
            return result;
        }

        private SQL(String query) {
            this.query = query;
        }

        private void loadPrepareStatement(Connection conn) throws SQLException {
            pstmt = conn.prepareStatement(query);
        }

        public PreparedStatement get() {
            return pstmt;
        }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DBSenu() {
        super("org.postgresql.Driver", "postgres", "root",
                new String[]{"jdbc:postgresql://127.0.0.1:5432/senu"
                });

        try {
            Connection[] conn = new Connection[4];
            for (int i = 0; i < 4; i++) {
                conn[i] = getConnection();
            }
            int idx = 0;
            for (SQL sql : SQL.values()) {
                try {
                    sql.loadPrepareStatement(conn[idx]);
                    idx = (idx + 1) % 4;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double alert(int rut) throws SQLException {
        List<Object[]> result = SQL.GET_ALERTA.executeQuery(rut);
        StringBuilder sb = new StringBuilder();
        double dd = 0;
        for (Object[] o : result) {
            sb.append(o[0]).append(",");
            dd = dd + ((BigDecimal) o[1]).intValue();
        }
        double ab = 0;
        double tt = 0;
        ResultSet rs = executeQuery("select sum(total) from factura where rut=" + rut);
        if (rs.next()) {
            tt = rs.getInt(1);
        }
        closeResultSetAndStatement(rs);
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            rs = executeQuery("select sum(abono) from abono where nfact in (" + sb.toString() + ")");
            if (rs.next()) {
                ab = rs.getInt(1);
            }
            closeResultSetAndStatement(rs);
        }
        dd = dd - ab;
        double factor = (double) dd / (double) tt;
        return factor;
    }

    public List<Object[]> getHistoria(int rut) throws SQLException {
        return SQL.GET_HISTORIA.executeQuery(rut, rut);
    }

    public void eliminarAbono(int nf, Date dt, double ab) throws SQLException {
        SQL.ELIMINA_ABONO.execute(nf, dt, ab, nf);
    }

    public List<ObjectArray> getAbonos(String where) throws SQLException {
        return executeQueryList(
                "select b.nfact, b.fecha, b.rut, c.nombre, b.cventa, b.vencimiento, "
                + "b.total, b.cancelada, b.impresa, b.anulada, a.abono, a.fecha as fecab "
                + "from factura b, abono a, cliente c where a.nfact=b.nfact and b.rut=c.rut "
                + "and " + where);
    }

    public void cambiarRut(int oldRut, int newRut) throws SQLException {
        SQL.UPDATE_RUT.execute(newRut, oldRut, newRut, oldRut, oldRut);
    }

    public List<Object[]> getEgresos(int year, int month) throws SQLException {
        return SQL.GET_EGRESOS.executeQuery(year, month);
    }

    public List<Object[]> getIngresos(int year, int month) throws SQLException {
        return SQL.GET_INGRESOS.executeQuery(year, month);
    }

    public void insertEgreso(Object[] object) throws SQLException {
        SQL.INSERTA_EGRESO.executeUpdate(object);
    }

    public void updateEgreso(Object[] object) throws SQLException {
        SQL.UPDATE_EGRESO.executeUpdate(object);
    }

    public List<Object[]> getContabilidad(String proveedor, String producto, String otros) throws SQLException {
        return SQL.GET_EGRESO.executeQuery(proveedor, producto, otros);
    }

    public List<Object[]> getContabilidad(int tipo, Date inicio) throws SQLException {
        Timestamp time = new Timestamp(inicio.getTime());
        switch (tipo) {
            case 1:
                return SQL.GET_CONTA1.executeQuery(time, time);
            case 2:
                return SQL.GET_CONTA2.executeQuery(time, time);
            case 3:
                return SQL.GET_CONTA3.executeQuery(time, time);
        }
        return null;
    }

    public int getNextIDCompra() throws SQLException {
        List<Object[]> lista = SQL.GET_NEXT_COMPRA.executeQuery();
        if (lista != null && lista.size() > 0 && lista.get(0)[0] != null) {
            return (Integer) (lista.get(0)[0]) + 1;
        } else {
            return 1;
        }
    }

    public List<Object[]> buscaEgreso(String value, int index) throws SQLException {
        switch (index) {
            case 1:
                return SQL.BUSCA_EGRESO1.executeQuery(value);
            case 2:
                return SQL.BUSCA_EGRESO2.executeQuery(value);
            case 3:
                return SQL.BUSCA_EGRESO3.executeQuery(value);
        }
        return null;
    }

    public void eliminarFactura(int nf) throws SQLException {
        SQL.DEL_COTIABONO.executeUpdate(nf);
        SQL.DEL_COTIDETA.executeUpdate(nf);
        SQL.DEL_COTI.executeUpdate(nf);
    }

    public void eliminarCotizacion(int ncoti) throws SQLException {
        SQL.DEL_COTIDETA.executeUpdate(ncoti);
        SQL.DEL_COTI.executeUpdate(ncoti);
    }

    public List<Object[]> getCotizaciones() throws SQLException {
        return SQL.GET_COTI.executeQuery();
    }

    public int getMinNFact() throws SQLException {
        List<Object[]> list = SQL.MIN_FACT.executeQuery();
        if (list.isEmpty()) {
            return -1;
        }
        return Math.min((Integer) list.get(0)[0], 0) - 1;
    }

    public void setAnular(int nfact, boolean b) throws SQLException {
        SQL.ANULAR_FACTURA.executeUpdate(b ? 1 : 0, nfact);
        if (!b) {
            SQL.DEL_ABONOS.executeUpdate(nfact);
            SQL.SET_SIN_CANCELAR.executeUpdate(nfact);
        }
    }

    public boolean existFactura(int nfact) throws SQLException {
        return SQL.EXIST_FACT.executeQuery(nfact).size() == 1;
    }

    public void updateCliente(int rut, String nombre, String fono, String dir, String comuna, String ciudad, String giro) throws SQLException {
        int idComuna = getIdComuna(comuna);
        int idCiudad = getIdCiudad(ciudad);
        int idGiro = getIdGiro(giro);
        if (idComuna == -1) {
            idComuna = insertComuna(comuna);
        }
        if (idCiudad == -1) {
            idCiudad = insertCiudad(ciudad);
        }
        if (idGiro == -1) {
            idGiro = insertGiro(giro);
        }
        if (existCliente("" + rut)) {
            SQL.UPDATE_CLIENTE.executeUpdate(rut, nombre, dir, fono, idCiudad, idComuna, idGiro, rut);
        } else {
            SQL.ADD_CLIENTE.executeUpdate(rut, nombre, dir, fono, idCiudad, idComuna, idGiro);
        }
    }

    public void addFactura(int fact, int rut, Date fecha, String ocompra, int cventa, String nguia, Date vencimiento, int subtotal, int iva, int tot, boolean cancelar, boolean imprimir, boolean anular) throws SQLException {
        java.sql.Date d1 = new java.sql.Date(fecha.getTime());
        java.sql.Date d2 = (vencimiento == null ? null : (new java.sql.Date(vencimiento.getTime())));
        SQL.ADD_FACTURA.executeUpdate(fact, rut, d1, ocompra, cventa, nguia, d2, subtotal, iva, tot, cancelar ? 1 : 0, imprimir ? 1 : 0, anular ? 1 : 0);
    }

    public void addDetalle(int fact, int linea, String codigo, String detalle, double cantidad, double precio, double total) throws SQLException {
        SQL.ADD_DETALLE.executeUpdate(fact, linea, codigo, detalle, cantidad, precio, total);
    }

    public boolean existCliente(String rut) throws SQLException {
        rut = rut.replaceAll("\\.", "");
        return SQL.EXIST_RUT.executeQuery(new Integer(rut)).size() == 1;
    }

    public boolean existProducto(String codigo) throws SQLException {
        return SQL.EXIST_PRODUCTO.executeQuery(codigo).size() == 1;
    }

    public void eliminarProducto(String codigo) throws SQLException {
        SQL.DEL_PRECIO.executeUpdate(codigo);
        SQL.DEL_PRODUCTO.executeUpdate(codigo);
    }

    //UPDATE_PRODUCTO_DETALLE("update producto set detalle=? where codigo=?"),
    //UPDATE_PRODUCTO_CODIGO("update producto set codigo=? where codigo=?"),
    //ADD_PRODUCTO_PRECIO("insert into precio (codigo, fecha, precio) values (?,?,?)"),
    public void updateDetalleProducto(String detalle, String codigo) throws SQLException {
        SQL.UPDATE_PRODUCTO_DETALLE.executeUpdate(detalle, codigo);
    }

    public void updateCodigoProducto(String codigoNuevo, String codigo) throws SQLException {
        SQL.UPDATE_PRODUCTO_CODIGO.executeUpdate(codigoNuevo, codigo);
    }

    public void addPrecioProducto(double precio, String codigo) throws SQLException {
        Timestamp time = new Timestamp(new Date().getTime());
        SQL.ADD_PRODUCTO_PRECIO.executeUpdate(codigo, time, new BigDecimal(precio));
    }

    public void setPermiso(String usuario, int permiso_id, boolean selected) throws SQLException {
        if (selected) {
            SQL.SET_PERMISO.executeUpdate(permiso_id, usuario);
        } else {
            SQL.UNSET_PERMISO.executeUpdate(permiso_id, usuario);
        }
    }

    public List<Object[]> getPermisos(String usuario) throws SQLException {
        return SQL.GET_PERMISOS.executeQuery(usuario);
    }

    public void setClave(String usuario, String claveNu1) throws SQLException {
        SQL.SET_CLAVE.executeUpdate(claveNu1, usuario);
    }

    public void updateUsuario(String usuario, String nombre, String direccion, String telefono, String correo) throws SQLException {
        SQL.UPDATE_USUARIO.executeUpdate(nombre, direccion, telefono, correo, usuario);
    }

    public void eliminarUsuario(String usuario) throws SQLException {
        SQL.DEL_USUARIO.executeUpdate(usuario);
    }

    //usuario, clave, nombre, telefono, correo, direccion, ciudad_id, comuna_id
    public void nuevoUsuario(String usuario, String clave, String nombre, String telefono, String correo, String direccion) throws SQLException {
        SQL.NUEVO_USUARIO.executeUpdate(usuario, clave, nombre, telefono, correo, direccion);
    }

    public List<Object[]> getUsuario(String usuario) throws SQLException {
        return SQL.GET_USUARIO.executeQuery(usuario);
    }

    public void eliminarNota(String titulo, String usuario) throws SQLException {
        SQL.DEL_NOTA.executeUpdate(titulo, usuario);
    }

    public void setNota(String titulo, String usuario, String texto, int cfondo, int ctexto, Timestamp inicio, int repetir, String patron, String destino) throws SQLException {
        SQL.SET_NOTA.executeUpdate(texto, cfondo, ctexto, inicio, repetir, patron, destino, titulo, usuario);
    }

    public List<Object[]> getNotas(String usuario) throws SQLException {
        return SQL.GET_NOTAS.executeQuery(usuario);
    }

    public List<Object[]> getUsuarios() throws SQLException {
        return SQL.GET_USUARIOS.executeQuery();
    }

    //NUEVA_NOTA("INSERT INTO nota(titulo, usuario, texto, cfondo, ctexto, inicio, repetir, patron, destinatarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"), //
    public void nuevaNota(String titulo, String usuario, String texto, int cfondo, int ctexto, Timestamp inicio, int repetir, String patron, String destinatarios) throws SQLException {
        SQL.NUEVA_NOTA.executeUpdate(titulo, usuario, texto, cfondo, ctexto, inicio, repetir, patron, destinatarios);
    }

    public Object[] getFacturaCliente(int nfact) throws SQLException {
        return executeFirstPreparedStatementQuery(SQL.GET_FACTURA_CLIENTE.get(), nfact);
    }

    public boolean abonar(int nfact, int monto) throws SQLException {
        if (monto > 0) {
            executePreparedStatementUpdate(SQL.INSERTA_ABONO.get(), nfact, new Timestamp(new Date().getTime()), monto);
        }
        BigDecimal bdd = (BigDecimal) SQL.SUM_ABONO.executeQuery(nfact).get(0)[0];
        double d = (bdd == null) ? 0 : bdd.doubleValue();
        BigDecimal bdt = (BigDecimal) SQL.TOTAL_FACTURA.executeQuery(nfact).get(0)[0];
        double t = (bdt == null) ? 0 : bdt.doubleValue();
        if (t <= d) {
            SQL.SET_FACTURA_CANCELADA.executeUpdate(1, nfact);
        }
        return t <= d;
    }

    public LinkedList<ObjectArray> getAbonos(int nfact) throws SQLException {
        return executePreparedStatementQueryList(SQL.GET_ABONOS.get(), nfact);
    }

    public boolean autorizar(String usuario, String clave) throws SQLException {
        return executeFirstPreparedStatementQuery(SQL.AUTORIZAR.get(), usuario, clave) != null;
    }

    public int getUltimaFactura() throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_ULTIMA_FACTURA.get());
        Integer val = (Integer) obj[0];
        val = (val == null) ? 0 : val;
        return val;
    }

    public LinkedList<ObjectArray> getFacturasCliente(String rut, boolean all) throws SQLException {
        if (rut != null && rut.trim().length() > 0) {
            if (all) {
                return executePreparedStatementQueryList(SQL.GET_FACTURAS_CLIENTE.get(), Integer.parseInt(rut));
            } else {
                return executePreparedStatementQueryList(SQL.GET_FACTURAS_CLIENTE2.get(), Integer.parseInt(rut));
            }
        } else {
            return new LinkedList<ObjectArray>();
        }
    }

    public LinkedList<ObjectArray> getDetalle(int nfact) throws SQLException {
        return executePreparedStatementQueryList(SQL.GET_DETALLE.get(), nfact);
    }

    public void insertDetalle(int nfact, int linea, String codigo, String detalle, BigDecimal cantidad, double precio, double total) throws SQLException {
        executePreparedStatementUpdate(SQL.INSERTA_DETALLE.get(), nfact, linea, codigo, detalle, cantidad, precio, total);
    }
    private GregorianCalendar cal0 = new GregorianCalendar();
    private GregorianCalendar cal1 = new GregorianCalendar();

    public void insertAbono(int nfact, Date fecha, Date hora, BigDecimal abono) throws SQLException {
        cal0.setTime(fecha);
        cal1.setTime(hora);
        cal0.set(GregorianCalendar.HOUR_OF_DAY, cal1.get(GregorianCalendar.HOUR_OF_DAY));
        cal0.set(GregorianCalendar.MINUTE, cal1.get(GregorianCalendar.MINUTE));
        cal0.set(GregorianCalendar.SECOND, cal1.get(GregorianCalendar.SECOND));
        Timestamp s = new Timestamp(cal0.getTimeInMillis());
        executePreparedStatementUpdate(SQL.INSERTA_ABONO.get(), nfact, s, abono);
    }

    public void insertProducto(String codigo, String detalle, BigDecimal precio, Timestamp inicio) throws SQLException {
        executePreparedStatementUpdate(SQL.INSERTA_PRODUCTO.get(), codigo, detalle);
        executePreparedStatementUpdate(SQL.INSERTA_PRECIO.get(), codigo, inicio, precio);
    }

    public ObjectArray getProducto(String codigo) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_PRODUCTO.get(), codigo);
        if (obj != null) {
            return new ObjectArray(obj);
        } else {
            return null;
        }
    }

    public ObjectArray getFactura(int nfact) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_FACTURA.get(), nfact);
        if (obj != null) {
            return new ObjectArray(obj);
        } else {
            return null;
        }
    }

    public void insertCliente(int rut, String nombre, String direccion, String comuna, String ciudad, String giro, String telefono, String otros) throws SQLException {
        int ciudad_id = getIdCiudad(ciudad);
        int comuna_id = getIdComuna(comuna);
        int giro_id = getIdGiro(giro);
        ciudad_id = (ciudad_id == -1 ? insertCiudad(ciudad) : ciudad_id);
        comuna_id = (comuna_id == -1 ? insertComuna(comuna) : comuna_id);
        giro_id = (giro_id == -1 ? insertGiro(giro) : giro_id);
        executePreparedStatementUpdate(SQL.INSERTA_CLIENTE.get(), rut, nombre, direccion, telefono, otros, ciudad_id, comuna_id, giro_id);
    }
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yy");

    public void insertFactura(int nfact, int rut, Date fecha, String ocompra, String cventa, String nguia, String vencimiento, double subtotal, double iva, double total, boolean cancelada, boolean impresa, boolean anulada) throws SQLException, ParseException {
        int idxCventa = 0;
        if (cventa.equals("CONTADO")) {
            idxCventa = 1;
        } else if (cventa.equals("CHEQUE 30")) {
            idxCventa = 2;
        } else {
            idxCventa = 3;
        }
        int idxCancelada = cancelada ? 1 : 0;
        int idxImpresa = impresa ? 1 : 0;
        int idxAnulada = anulada ? 1 : 0;
        vencimiento = vencimiento.trim();
        Date venc = ((vencimiento.length() == 0) ? null : sdf2.parse(vencimiento));
        java.sql.Date vencDate = null;
        if (venc != null) {
            vencDate = new java.sql.Date(venc.getTime());
        }
        executePreparedStatementUpdate(SQL.INSERTA_FACTURA.get(), nfact, rut, new java.sql.Date(fecha.getTime()), ocompra, idxCventa, nguia, vencDate, subtotal, iva, total, idxCancelada, idxImpresa, idxAnulada);
    }

    public boolean tienePermiso(String usuario, Permiso permiso) throws SQLException {
        int idPermiso = permiso.id;
        ResultSet rs;
        rs = executePreparedStatementQuery(SQL.TIENE_PERMISO.get(), idPermiso, usuario);
        if (rs.next()) {
            rs.close();
            return true;
        } else {
            rs.close();
            return false;
        }
    }

    public LinkedList<String> buscaCiudad(String texto) throws SQLException {
        texto = texto.replaceAll("\\s+", "% ") + "%";
        ResultSet rs = executePreparedStatementQuery(SQL.BUSCA_CIUDAD.get(), texto);
        LinkedList<String> list = new LinkedList<String>();


        while (rs.next()) {
            list.add(rs.getString(1));


        }
        rs.close();


        return list;


    }

    public LinkedList<String> buscaGiro(String texto) throws SQLException {
        texto = texto.replaceAll("\\s+", "% ") + "%";
        ResultSet rs = executePreparedStatementQuery(SQL.BUSCA_GIRO.get(), texto);
        LinkedList<String> list = new LinkedList<String>();


        while (rs.next()) {
            list.add(rs.getString(1));


        }
        rs.close();


        return list;


    }

    public LinkedList<String> buscaComuna(String texto) throws SQLException {
        texto = texto.replaceAll("\\s+", "% ") + "%";
        ResultSet rs = executePreparedStatementQuery(SQL.BUSCA_COMUNA.get(), texto);
        LinkedList<String> list = new LinkedList<String>();


        while (rs.next()) {
            list.add(rs.getString("comuna"));


        }
        rs.close();


        return list;


    }

    //nombre, rut, direccion, telefono, otros, ciudad_id, comuna_id, giro_id
    public LinkedList<ObjectArray> buscaCliente(String texto) throws SQLException {
        synchronized (this) {
//            texto = "%" + texto.replaceAll("\\s+", "% %") + "%";
//            ResultSet rs = executePreparedStatementQuery(SQL.BUSCA_CLIENTE.get(), texto);
//            LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();
//            while (rs.next()) {
//                list.add(new ObjectArray(new Object[]{rs.getString("nombre"), rs.getString("rut"), rs.getString("direccion"), rs.getString("telefono"),
//                            rs.getString("otros"), rs.getString("ciudad_id"), rs.getString("comuna_id"), rs.getString("giro_id")}));
//            }
//            rs.close();
//            return list;



            String[] textos = texto.trim().split("\\s");
            StringBuilder sql = new StringBuilder("select nombre, rut, direccion, telefono, otros, ciudad_id, comuna_id, giro_id from cliente "
                    + "where true ");


            for (String s : textos) {
                sql.append(" and nombre like '%");
                sql.append(s);
                sql.append("%' ");


            }
            sql.append("order by nombre");
            ResultSet rs = executeQuery(sql.toString());
            LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();

            while (rs.next()) {
                list.add(new ObjectArray(new Object[]{rs.getString("nombre"), rs.getString("rut"), rs.getString("direccion"), rs.getString("telefono"),
                            rs.getString("otros"), rs.getString("ciudad_id"), rs.getString("comuna_id"), rs.getString("giro_id")}));

            }
            closeResultSetAndStatement(rs);
            return list;


        }
    }

    public LinkedList<ObjectArray> buscaRut(String rut) throws SQLException {
        LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();
        rut = rut.trim();
        if (rut.length() > 0) {
            rut = rut.replaceAll("\\.", "");
            Object[] objects = executeFirstPreparedStatementQuery(SQL.BUSCA_RUT.get(), new Integer(rut));
            if (objects != null) {
                list.add(new ObjectArray(objects));
            }
        }
        return list;
    }

    private String addSpaces(String text, int spaces, boolean after) {
        spaces = spaces - text.length();


        char[] chars = new char[spaces];
        Arrays.fill(chars, ' ');


        if (after) {
            return text + new String(chars);


        } else {
            return new String(chars) + text;


        }
    }

    public LinkedList<ObjectArray> buscaCodigo(String codigo, Date date) throws SQLException {
        LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();
        codigo = codigo.trim();


        if (codigo.length() > 0) {
            Object[] objects = executeFirstPreparedStatementQuery(SQL.BUSCA_POR_CODIGO.get(), new Timestamp(date.getTime()), codigo);


            if (objects != null) {
                list.add(new ObjectArray(objects) {
                    @Override
                    public String toString() {
                        return addSpaces(objects[1].toString(), 60, true) + addSpaces(Constantes.decimalFormat.format(Double.parseDouble(objects[2].toString())), 16, false);


                    }
                });


            }
        }
        return list;


    }
    Map<String, Map<String, Integer>> mapSug = null;

    public LinkedList<ObjectArray> buscaSugerencia(List<String> codigos) throws SQLException {
        if (mapSug == null) {
            mapSug = miner1(20, 0.4, 100000);
        }
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        for (String cod : codigos) {
            Map<String, Integer> map = mapSug.get(cod);
            if (map != null && map.size() > 2) {
                for (String key : map.keySet()) {
                    if (!key.equals(cod)) {
                        Integer val = resultMap.get(key);
                        if (val == null) {
                            val = map.get(key);
                        } else {
                            val = val + map.get(key);
                        }
                        resultMap.put(key, val);
                    }
                }
            }
        }

        if (resultMap.isEmpty()) {
            return new LinkedList<ObjectArray>();
        }

        LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();

        for (String key : resultMap.keySet()) {
            if (!codigos.contains(key)) {
                ResultSet rs =
                        executePreparedStatementQuery(SQL.BUSCA_POR_COD.get(),
                        new Timestamp(new Date().getTime()), key);
                while (rs.next()) {
                    list.add(new ObjectArray(
                            new Object[]{rs.getString("detalle"),
                                rs.getString("codigo"),
                                rs.getDouble("precio"), resultMap.get(key)}) {
                        @Override
                        public String toString() {
                            return addSpaces(objects[0].toString(), 60, true) + addSpaces(Constantes.decimalFormat.format(Double.parseDouble(objects[2].toString())), 16, false);
                        }
                    });
                }
                rs.close();
            }
        }
        for (int i = 1; i < list.size(); i++) {
            for (int j = 0; j < i; j++) {
                ObjectArray oi = list.get(i);
                ObjectArray oj = list.get(j);
                if ((Integer) oj.objects[3] < (Integer) oi.objects[3]) {
                    list.set(i, oj);
                    list.set(j, oi);
                }
            }
        }

        return list;
    }

    public LinkedList<ObjectArray> buscaProducto(String texto, Date now, boolean ordenado) throws SQLException {
        LinkedList<ObjectArray> list = new LinkedList<ObjectArray>();
        ResultSet rs;


        if (ordenado) {
            texto = texto.replaceAll("\\s+", "% ") + "%";
            rs = executePreparedStatementQuery(SQL.BUSCA_POR_PRODUCTO.get(), new Timestamp(now.getTime()), texto);


            while (rs.next()) {
                list.add(new ObjectArray(new Object[]{rs.getString("detalle"), rs.getString("codigo"), rs.getDouble("precio")}) {
                    @Override
                    public String toString() {
                        return addSpaces(objects[0].toString(), 60, true) + addSpaces(Constantes.decimalFormat.format(Double.parseDouble(objects[2].toString())), 16, false);


                    }
                });


            }
            rs.close();


            return list;


        } else {
            String[] textos = texto.trim().split("\\s");
            StringBuilder sql = new StringBuilder("select a.codigo, a.detalle, "
                    + "(select precio from precio where codigo=a.codigo and fecha<=' "
                    + sdf.format(now) + "' order by fecha desc limit 1) as precio "
                    + "from producto a where true ");


            for (String s : textos) {
                sql.append(" and a.detalle like '%");
                sql.append(s);
                sql.append("%' ");


            }
            sql.append("order by a.detalle");
            rs = executeQuery(sql.toString());


            while (rs.next()) {
                list.add(new ObjectArray(new Object[]{rs.getString("detalle"), rs.getString("codigo"), rs.getDouble("precio")}) {
                    @Override
                    public String toString() {
                        return addSpaces(objects[0].toString(), 60, true) + addSpaces(Constantes.decimalFormat.format(Double.parseDouble(objects[2].toString())), 16, false);


                    }
                });


            }
            closeResultSetAndStatement(rs);


            return list;


        }
    }

    public List<Object[]> getFacturas(String where) throws SQLException {
        String sql = "select a.nfact, a.fecha, a.rut, b.nombre, a.cventa, a.vencimiento, a.total, a.cancelada, a.impresa, a.anulada, "
                + "(select sum(c.abono) from abono c where a.cancelada=0 and a.anulada=0 and a.nfact=c.nfact) abono from factura a, cliente b where a.rut=b.rut and ";
        ResultSet rs = executeQuery(sql + where);


        int cols = rs.getMetaData().getColumnCount();
        Object[] obj;
        LinkedList<Object[]> list = new LinkedList<Object[]>();


        while (rs.next()) {
            obj = new Object[cols];


            for (int i = 0; i
                    < cols; i++) {
                obj[i] = rs.getObject(i + 1);


            }
            list.add(obj);


        }
        closeResultSetAndStatement(rs);


        return list;


    }

    public int getIdCiudad(String ciudad) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GETID_CIUDAD.get(), ciudad);


        if (obj != null) {
            return ((Integer) obj[0]).intValue();


        } else {
            return -1;


        }
    }

    public int insertCiudad(String ciudad) throws SQLException {
        SQL.INSERTA_CIUDAD.executeUpdate(ciudad);


        return getIdCiudad(ciudad);


    }

    public int getIdComuna(String comuna) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GETID_COMUNA.get(), comuna);


        if (obj != null) {
            return ((Integer) obj[0]).intValue();


        } else {
            return -1;


        }
    }

    public int insertComuna(String comuna) throws SQLException {
        SQL.INSERTA_COMUNA.executeUpdate(comuna);


        return getIdComuna(comuna);


    }

    public int getIdGiro(String giro) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GETID_GIRO.get(), giro);


        if (obj != null) {
            return ((Integer) obj[0]).intValue();


        } else {
            return -1;


        }
    }

    public int insertGiro(String giro) throws SQLException {
        SQL.INSERTA_GIRO.executeUpdate(giro);


        return getIdGiro(giro);


    }

    public String getCiudad(int id) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_CIUDAD.get(), id);


        if (obj != null) {
            return (String) obj[0];


        } else {
            return "";


        }
    }

    public ObjectArray getCliente(int rut) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_CLIENTE.get(), rut);


        if (obj != null) {
            return new ObjectArray(obj);


        } else {
            return null;


        }
    }

    public String getComuna(int id) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_COMUNA.get(), id);


        if (obj != null) {
            return (String) obj[0];


        } else {
            return "";


        }
    }

    public String getGiro(int id) throws SQLException {
        Object[] obj = executeFirstPreparedStatementQuery(SQL.GET_GIRO.get(), id);


        if (obj != null) {
            return (String) obj[0];


        } else {
            return "";


        }
    }

    public Map<String, Map<String, Integer>> miner1(int minCant, double minPorcentaje, int registros) throws SQLException {
        long t0 = System.currentTimeMillis();
        ResultSet rs = executeQuery("select codigo, nfact from detalle order by nfact desc limit " + registros);
        String codigo;
        long nfact;
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
        long oldfact = 0;
        LinkedList<String> list = new LinkedList<String>();
        while (rs.next()) {
            codigo = rs.getString(1);
            nfact = rs.getLong(2);
            Integer count2 = map2.get(codigo);
            if (count2 == null) {
                count2 = 1;
            } else {
                count2++;
            }
            map2.put(codigo, count2);


            if (oldfact != nfact && list.size() > 0) {
                for (String l : list) {
                    Map<String, Integer> countMap = map.get(l);
                    if (countMap == null) {
                        countMap = new HashMap<String, Integer>();
                    }
                    map.put(l, countMap);
                    for (String l1 : list) {
                        Integer count = countMap.get(l1);
                        if (count == null) {
                            count = 1;
                        } else {
                            count++;
                        }
                        countMap.put(l1, count);
                    }
                    if (countMap.size() > 0) {
                        // countMap.put(l, count2);
                    }
                }

                list.clear();
            }
            list.add(codigo);
            oldfact = nfact;
        }
        closeResultSetAndStatement(rs);
        for (String s : map.keySet()) {
            int total = map2.get(s);
            LinkedList<String> del = new LinkedList<String>();
            for (String key : map.get(s).keySet()) {
                int ocurrencias = map.get(s).get(key);
                if (ocurrencias < total * minPorcentaje) {
                    del.add(key);
                }
            }
            for (String k : del) {
                map.get(s).remove(k);
            }
        }
        return map;
    }

    public static void main1(String[] args) throws SQLException {
        DBSenu db = new DBSenu();
        ResultSet rs = db.executeQuery("select nfact, sum from (select a.nfact, a.total-sum(b.abono) as sum "
                + "from factura a, abono b where a.nfact=b.nfact group by a.nfact, a.total order by 2) a "
                + "where sum<0");
        long nfact, sum, ab, idab, oldab;
        while (rs.next()) {
            nfact = rs.getLong(1);
            sum = -rs.getLong(2);
            System.out.println("factura " + nfact + ", abono de mas = " + sum);
            ResultSet rs2 = db.executeQuery("select id, abono from abono where nfact=" + nfact + " order by fecha desc");
            while (rs2.next()) {
                idab = rs2.getLong(1);
                ab = rs2.getLong(2);
                oldab = ab;
                if (ab >= sum) {
                    ab = ab - sum;
                    sum = 0;
                } else {
                    sum = sum - ab;
                    ab = 0;
                }
                if (ab != oldab) {
                    if (ab == 0) {
                        db.executeUpdate("delete from abono where id=" + idab);
                    } else {
                        db.executeUpdate("update abono set abono=" + ab + " where id=" + idab);
                    }
                }
            }
            db.closeResultSetAndStatement(rs2);
        }
        db.closeResultSetAndStatement(rs);
    }

    @Override
    public String creationScript() {
        return "CREATE SCHEMA \"senu\" ;\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"ciudad\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"ciudad\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"ciudad\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"ciudad\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") ,\n"
                + " UNIQUE INDEX \"ciudad_UNIQUE\" (\"ciudad\" ASC) );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"comuna\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"comuna\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"comuna\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"comuna\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") ,\n"
                + " UNIQUE INDEX \"comuna_UNIQUE\" (\"comuna\" ASC) );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"usuario\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"usuario\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"usuario\" (\n"
                + " \"usuario\" VARCHAR(32) NOT NULL ,\n"
                + " \"clave\" VARCHAR(64) NULL ,\n"
                + " \"nombre\" VARCHAR(64) NULL ,\n"
                + " \"telefono\" VARCHAR(16) NULL ,\n"
                + " \"correo\" VARCHAR(128) NULL ,\n"
                + " \"direccion\" VARCHAR(128) NULL ,\n"
                + " \"ciudad_id\" INT NOT NULL ,\n"
                + " \"comuna_id\" INT NOT NULL ,\n"
                + " PRIMARY KEY (\"usuario\") ,\n"
                + " INDEX \"fk_usuario_ciudad1\" (\"ciudad_id\" ASC) ,\n"
                + " INDEX \"fk_usuario_comuna1\" (\"comuna_id\" ASC) ,\n"
                + " CONSTRAINT \"fk_usuario_ciudad1\"\n"
                + " FOREIGN KEY (\"ciudad_id\" )\n"
                + " REFERENCES \"senu\".\"ciudad\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION,\n"
                + " CONSTRAINT \"fk_usuario_comuna1\"\n"
                + " FOREIGN KEY (\"comuna_id\" )\n"
                + " REFERENCES \"senu\".\"comuna\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"permiso\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"permiso\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"permiso\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"permiso\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"permisos\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"permisos\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"permisos\" (\n"
                + " \"permiso_id\" INT NOT NULL ,\n"
                + " \"usuario\" VARCHAR(32) NOT NULL ,\n"
                + " PRIMARY KEY (\"permiso_id\", \"usuario\") ,\n"
                + " INDEX \"fk_permisos_usuario1\" (\"usuario\" ASC) ,\n"
                + " CONSTRAINT \"fk_permisos_permiso1\"\n"
                + " FOREIGN KEY (\"permiso_id\" )\n"
                + " REFERENCES \"senu\".\"permiso\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION,\n"
                + " CONSTRAINT \"fk_permisos_usuario1\"\n"
                + " FOREIGN KEY (\"usuario\" )\n"
                + " REFERENCES \"senu\".\"usuario\" (\"usuario\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"constantes\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"constantes\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"constantes\" (\n"
                + " \"constante\" VARCHAR(32) NOT NULL ,\n"
                + " \"id\" INT NOT NULL ,\n"
                + " \"valor_int\" INT NULL ,\n"
                + " \"valor_long\" BIGINT NULL ,\n"
                + " \"valor_fecha\" DATETIME NULL ,\n"
                + " \"valor_string\" VARCHAR(128) NULL ,\n"
                + " \"valor_decimal\" DECIMAL(19,4) NULL ,\n"
                + " PRIMARY KEY (\"constante\", \"id\") );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"session\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"session\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"session\" (\n"
                + " \"id\" INT NOT NULL ,\n"
                + " \"usuario\" VARCHAR(32) NOT NULL ,\n"
                + " \"inicio\" DATETIME NULL ,\n"
                + " \"termino\" DATETIME NULL ,\n"
                + " \"maquina\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") ,\n"
                + " INDEX \"fk_session_usuario1\" (\"usuario\" ASC) ,\n"
                + " CONSTRAINT \"fk_session_usuario1\"\n"
                + " FOREIGN KEY (\"usuario\" )\n"
                + " REFERENCES \"senu\".\"usuario\" (\"usuario\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"giro\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"giro\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"giro\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"giro\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") ,\n"
                + " UNIQUE INDEX \"giro_UNIQUE\" (\"giro\" ASC) );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"cliente\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"cliente\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"cliente\" (\n"
                + " \"rut\" INT NOT NULL ,\n"
                + " \"nombre\" VARCHAR(128) NULL ,\n"
                + " \"direccion\" VARCHAR(64) NULL ,\n"
                + " \"telefono\" VARCHAR(32) NULL ,\n"
                + " \"otros\" VARCHAR(256) NULL ,\n"
                + " \"ciudad_id\" INT NOT NULL ,\n"
                + " \"comuna_id\" INT NOT NULL ,\n"
                + " \"giro_id\" INT NOT NULL ,\n"
                + " PRIMARY KEY (\"rut\") ,\n"
                + " INDEX \"fk_cliente_ciudad1\" (\"ciudad_id\" ASC) ,\n"
                + " INDEX \"fk_cliente_comuna1\" (\"comuna_id\" ASC) ,\n"
                + " INDEX \"fk_cliente_giro1\" (\"giro_id\" ASC) ,\n"
                + " INDEX \"nombre_idx\" (\"nombre\" ASC) ,\n"
                + " CONSTRAINT \"fk_cliente_ciudad1\"\n"
                + " FOREIGN KEY (\"ciudad_id\" )\n"
                + " REFERENCES \"senu\".\"ciudad\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION,\n"
                + " CONSTRAINT \"fk_cliente_comuna1\"\n"
                + " FOREIGN KEY (\"comuna_id\" )\n"
                + " REFERENCES \"senu\".\"comuna\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION,\n"
                + " CONSTRAINT \"fk_cliente_giro1\"\n"
                + " FOREIGN KEY (\"giro_id\" )\n"
                + " REFERENCES \"senu\".\"giro\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"categoria\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"categoria\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"categoria\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"categoria\" VARCHAR(64) NULL ,\n"
                + " PRIMARY KEY (\"id\") );\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"producto\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"producto\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"producto\" (\n"
                + " \"codigo\" VARCHAR(16) NOT NULL ,\n"
                + " \"categoria_id\" INT NOT NULL ,\n"
                + " \"detalle\" VARCHAR(64) NOT NULL ,\n"
                + " PRIMARY KEY (\"codigo\") ,\n"
                + " INDEX \"fk_producto_categoria1\" (\"categoria_id\" ASC) ,\n"
                + " CONSTRAINT \"fk_producto_categoria1\"\n"
                + " FOREIGN KEY (\"categoria_id\" )\n"
                + " REFERENCES \"senu\".\"categoria\" (\"id\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"factura\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"factura\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"factura\" (\n"
                + " \"nfact\" INT NOT NULL ,\n"
                + " \"rut\" INT NOT NULL ,\n"
                + " \"fecha\" DATE NOT NULL ,\n"
                + " \"ocompra\" VARCHAR(32) NULL ,\n"
                + " \"cventa\" TINYINT NULL ,\n"
                + " \"nguia\" VARCHAR(32) NULL ,\n"
                + " \"vencimiento\" DATE NULL ,\n"
                + " \"subtotal\" DECIMAL(19,4) NULL ,\n"
                + " \"iva\" DECIMAL(19,4) NULL ,\n"
                + " \"total\" DECIMAL(19,4) NULL ,\n"
                + " \"cancelada\" TINYINT NULL ,\n"
                + " \"impresa\" TINYINT NULL ,\n"
                + " \"anulada\" TINYINT NULL ,\n"
                + " \"cotizacion\" TINYINT NOT NULL DEFAULT 0 ,\n"
                + " PRIMARY KEY (\"nfact\", \"cotizacion\") ,\n"
                + " INDEX \"fk_factura_cliente1\" (\"rut\" ASC) ,\n"
                + " CONSTRAINT \"fk_factura_cliente1\"\n"
                + " FOREIGN KEY (\"rut\" )\n"
                + " REFERENCES \"senu\".\"cliente\" (\"rut\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"detalle\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"detalle\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"detalle\" (\n"
                + " \"nfact\" INT NOT NULL ,\n"
                + " \"linea\" TINYINT NOT NULL ,\n"
                + " \"codigo\" VARCHAR(16) NULL ,\n"
                + " \"detalle\" VARCHAR(64) NULL ,\n"
                + " \"cantidad\" DECIMAL(19,4) NULL ,\n"
                + " \"precio\" DECIMAL(19,4) NULL ,\n"
                + " \"total\" DECIMAL(19,4) NULL ,\n"
                + " \"cotizacion\" TINYINT NOT NULL ,\n"
                + " PRIMARY KEY (\"nfact\", \"linea\", \"cotizacion\") ,\n"
                + " INDEX \"fk_detalle_factura1\" (\"nfact\" ASC, \"cotizacion\" ASC) ,\n"
                + " CONSTRAINT \"fk_detalle_factura1\"\n"
                + " FOREIGN KEY (\"nfact\" , \"cotizacion\" )\n"
                + " REFERENCES \"senu\".\"factura\" (\"nfact\" , \"cotizacion\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"precio\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"precio\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"precio\" (\n"
                + " \"codigo\" VARCHAR(16) NOT NULL ,\n"
                + " \"fecha\" DATETIME NOT NULL ,\n"
                + " \"precio\" DECIMAL(19,4) NULL ,\n"
                + " PRIMARY KEY (\"codigo\", \"fecha\") ,\n"
                + " INDEX \"fk_precio_producto1\" (\"codigo\" ASC) ,\n"
                + " CONSTRAINT \"fk_precio_producto1\"\n"
                + " FOREIGN KEY (\"codigo\" )\n"
                + " REFERENCES \"senu\".\"producto\" (\"codigo\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);\n"
                + "-- -----------------------------------------------------\n"
                + "-- Table \"senu\".\"abono\"\n"
                + "-- -----------------------------------------------------\n"
                + "DROP TABLE IF EXISTS \"senu\".\"abono\" ;\n"
                + "CREATE TABLE IF NOT EXISTS \"senu\".\"abono\" (\n"
                + " \"id\" INT NOT NULL AUTO_INCREMENT ,\n"
                + " \"nfact\" INT NOT NULL ,\n"
                + " \"fecha\" DATETIME NULL ,\n"
                + " \"abono\" DECIMAL(19,4) NULL ,\n"
                + " PRIMARY KEY (\"id\") ,\n"
                + " CONSTRAINT \"fk_abono_factura1\"\n"
                + " FOREIGN KEY (\"nfact\" )\n"
                + " REFERENCES \"senu\".\"factura\" (\"nfact\" )\n"
                + " ON DELETE NO ACTION\n"
                + " ON UPDATE NO ACTION);";

    }
}
