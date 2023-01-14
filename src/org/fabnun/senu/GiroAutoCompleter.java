/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.util.LinkedList;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

/**
 *
 * @author fabnun
 */
public class GiroAutoCompleter extends AutoCompleter {

    public GiroAutoCompleter(JTextComponent comp) {
        super(comp);
    }

    @Override
    protected boolean updateListData() {
        String value = textComp.getText().trim();
        LinkedList<String> lista = null;
        try {
            lista = Constantes.db.buscaGiro(value);
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
        if (selected != null) {
            textComp.setText(selected.toString());
        }
    }
}
