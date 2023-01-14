/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.util.LinkedList;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

/**
 *
 * @author fabnun
 */
public class ClienteAutoCompleter extends AutoCompleter {

    Factura factuParent;

    public ClienteAutoCompleter(JTextComponent comp, Factura factuParent) {
        super(comp);
        this.factuParent = factuParent;
    }

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
            if (lista.size() == 1) {
                this.list.setSelectedIndex(0);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void acceptedListItem(Object selected) {
        ObjectArray objects = (ObjectArray) selected;
        if (selected != null) {
//            for (int i = 0; i < textComp.getParent().getComponentCount(); i++) {
//                System.out.println(i + ": " + textComp.getParent().getComponent(i));
//            }
            textComp.setText(objects.objects[0].toString());//nombre
            ((JTextField) textComp.getParent().getComponent(8)).setText(Constantes.decimalFormat.format(Integer.parseInt(objects.objects[1].toString())));//rut
            ((JTextField) textComp.getParent().getComponent(14)).setText(objects.objects[2].toString());//direccion
            ((JTextField) textComp.getParent().getComponent(13)).setText(objects.objects[3].toString());//telefono
            //((JTextField) textComp.getParent().getComponent(20)).setText(objects.objects[4].toString());//otros
            int ciudad_id = Integer.parseInt(objects.objects[5].toString());//ciudad_id
            int comuna_id = Integer.parseInt(objects.objects[6].toString());//comuna_id
            int giro_id = Integer.parseInt(objects.objects[7].toString());//giro_id
            try {
                ((JTextField) textComp.getParent().getComponent(18)).setText(Constantes.db.getCiudad(ciudad_id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ((JTextField) textComp.getParent().getComponent(16)).setText(Constantes.db.getComuna(comuna_id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ((JTextField) textComp.getParent().getComponent(19)).setText(Constantes.db.getGiro(giro_id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            textComp.requestFocus();
            try {
                factuParent.checkDeuda("" + objects.objects[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
