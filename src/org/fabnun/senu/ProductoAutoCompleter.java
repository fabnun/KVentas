/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.util.Date;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

public class ProductoAutoCompleter extends AutoCompleter {

    private static boolean disable = false;

    public void setDisable(boolean disable) {
        ProductoAutoCompleter.disable = disable;
    }
    public boolean ordenado = false;
    private Factura factura;

    public ProductoAutoCompleter(JTextComponent comp, boolean ordenado, Factura factura) {
        super(comp);
        this.ordenado = ordenado;
        this.factura = factura;
    }

    @Override
    protected boolean updateListData() {
        if (disable) {
            return false;
        }
        String value = textComp.getText().trim();
        LinkedList<ObjectArray> lista = null;
        try {
            if (value.length() == 0) {
                LinkedList<String> codigos = new LinkedList<String>();
                for (JLabel jt : factura.codigos) {
                    String text = jt.getText().trim();
                    if (text.length() > 0) {
                        codigos.add(text);
                    }
                }
                lista = Constantes.db.buscaSugerencia(codigos);
            } else {

                lista = Constantes.db.buscaProducto(value, new Date(), ordenado);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.list.removeAll();
        if (lista != null && lista.size() > 0) {
            this.list.setListData(lista.toArray());
            if (lista.size() == 1) {
                if (((String) lista.get(0).objects[0]).trim().equals(value)) {
                    this.list.removeAll();
                    return false;
                } else {
                    this.list.setSelectedIndex(0);
                }
            }
            this.list.requestFocus();
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
            ((JLabel) textComp.getParent().getComponent(zOrder - 1)).setText(objects.objects[1].toString());//codigo
            ((JTextField) textComp.getParent().getComponent(zOrder + 2)).setText(Constantes.decimalFormat.format(Double.parseDouble(objects.objects[2].toString())));//precio
            textComp.requestFocus();
        }
    }
}
