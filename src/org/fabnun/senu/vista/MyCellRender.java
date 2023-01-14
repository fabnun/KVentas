/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import org.fabnun.senu.Constantes;

/**
 *
 * @author fabnun
 */
public class MyCellRender extends DefaultTableCellRenderer {

    public enum Type {

        STRING, DATE, INT, DOUBLE
    }
    private SimpleDateFormat sdf = null;
    private Type type = Type.STRING;
    private JTextField field = new JTextField();

    public MyCellRender(Type type) {
        this.type = type;
        if (type == Type.DOUBLE || type == Type.INT) {
            field.setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    public MyCellRender(SimpleDateFormat sdf) {
        this.sdf = sdf;
        type = Type.DATE;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color color = Color.white;
        if (isSelected) {
            color = Color.lightGray;
        }
        if (hasFocus) {
            color = Color.cyan;
        }
        field.setBackground(color);
        String text = "";
        if (value != null) {
            switch (type) {
                case DATE:
                    text = sdf.format(value);
                    break;
                case INT:
                    try {
                        text = Constantes.decimalFormat.format(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DOUBLE:
                    text = Constantes.decimalFormat.format(value);
                    break;
                case STRING:
                    text = value.toString();
                    break;
            }
        }
        field.setText(text);
        return field;
    }
}
