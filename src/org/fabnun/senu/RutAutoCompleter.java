/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu;

import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.vista.AutoCompleter;

/**
 *
 * @author fabnun
 */
public class RutAutoCompleter extends AutoCompleter {
    
    Factura factuParent;
    
    public RutAutoCompleter(JTextComponent comp, Factura factuParent) {
        super(comp);
        this.factuParent = factuParent;
    }
    
    @Override
    protected boolean updateListData() {
        String value = textComp.getText().trim().replaceAll("\\.", "");
        LinkedList<ObjectArray> lista = null;
        try {
            lista = Constantes.db.buscaRut(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.list.removeAll();
        
        if (lista != null && lista.size() > 0) {
            list.setListData(lista.toArray());
            list.setSelectedIndex(0);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    protected void acceptedListItem(Object selected) {
        
        
        
        final ObjectArray objects = (ObjectArray) selected;
        if (selected != null) {
//            for (int i = 0; i < textComp.getParent().getComponentCount(); i++) {
//                System.out.println(i + ": " + textComp.getParent().getComponent(i));
//            }
            textComp.setText(Constantes.decimalFormat.format(objects.objects[1]));//rut
            ((JTextField) textComp.getParent().getComponent(11)).setText(objects.objects[0].toString());//nombre
            ((JTextField) textComp.getParent().getComponent(14)).setText(objects.objects[2].toString());//direccion
            ((JTextField) textComp.getParent().getComponent(13)).setText(objects.objects[3].toString());//telefono
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
                factuParent.checkDeuda(""+(Integer) objects.objects[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
}
