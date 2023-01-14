/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.util.Date;
import java.util.LinkedList;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

public class CodigoAutoCompleter extends AutoCompleter {

    private static boolean disable = false;

    public void setDisable(boolean disable) {
        CodigoAutoCompleter.disable = disable;
    }

    public CodigoAutoCompleter(JTextComponent comp) {
        super(comp);
    }

    @Override
    protected boolean updateListData() {
        if (disable) {
            return false;
        }
        String value = textComp.getText().trim();
        LinkedList<ObjectArray> lista = null;
        try {
            lista = Constantes.db.buscaCodigo(value, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.list.removeAll();
        if (lista != null && lista.size() > 0) {
            this.list.setListData(lista.toArray());
            this.list.requestFocus();
            this.list.setSelectedIndex(0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void acceptedListItem(Object selected) {
        ObjectArray objects = (ObjectArray) selected;
        if (selected != null) {
            int zOrder = textComp.getParent().getComponentZOrder(textComp);
            textComp.setText(objects.objects[0].toString());//detalle
            ((JTextField) textComp.getParent().getComponent(zOrder + 1)).setText(objects.objects[1].toString());//codigo
            ((JTextField) textComp.getParent().getComponent(zOrder + 3)).setText(Constantes.decimalFormat.format(Double.parseDouble(objects.objects[2].toString())));//precio
            textComp.requestFocus();
        }
    }
}
