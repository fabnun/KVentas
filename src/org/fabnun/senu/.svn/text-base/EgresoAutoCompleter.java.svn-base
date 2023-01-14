/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.util.LinkedList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

/**
 *
 * @author fabnun
 */
public class EgresoAutoCompleter extends AutoCompleter {

    private int index;

    public EgresoAutoCompleter(JTextComponent text, int index) {
        super(text);
        this.index = index;
    }

    @Override
    protected boolean updateListData() {
        String value = textComp.getText().trim();

        List<Object[]> lista = null;
        List<String> strings = new LinkedList<String>();
        try {
            lista = Constantes.db.buscaEgreso("%" + value + "%", index);
            for(Object[] o:lista){
                strings.add((String) o[0]);
            }
        } catch (Exception ex) {
        }
        if (strings != null && strings.size() > 0) {
            this.list.setListData(strings.toArray());
            this.list.requestFocus();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void acceptedListItem(Object selected) {
        String s = (String) selected;
        if (selected != null) {
            textComp.setText(s);
            textComp.requestFocus();
        }
    }
}
