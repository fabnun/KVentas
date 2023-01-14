/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu.vista;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import org.fabnun.senu.Constantes;

/**
 *
 * @author fabnun
 */
public class MyCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    public enum Type {

        DATE, INT, STRING, DOUBLE
    };
    private Type type = Type.STRING;
    private JComponent component = null;
    private JDateChooser dateChooser;
    private JTextField textField;

    public MyCellEditor(SimpleDateFormat sdf) {
        type = Type.DATE;
        dateChooser = new JDateChooser(sdf.toPattern(), false);
        dateChooser.setLocale(new Locale("es", "cl"));
        component = dateChooser;
    }

    public MyCellEditor(JTextField textField) {
        this.textField = textField;
        type = Type.STRING;
        textField.setHorizontalAlignment(JTextField.RIGHT);
        component = textField;
    }

    public MyCellEditor(JTextField textField, Type type) {
        this.textField = textField;
        this.type = type;
        if (type == Type.DOUBLE || type == Type.INT) {
            textField.setHorizontalAlignment(JTextField.RIGHT);
        }
        component = textField;
    }

    public Object getCellEditorValue() {
        Object tmp = null;
        switch (type) {
            case DATE:
                tmp = dateChooser.getDate();
                break;
            case STRING:
                tmp = textField.getText();
                break;
            case DOUBLE:
                try {
                    String s = textField.getText().replaceAll("\\.", ",");
                    tmp = Constantes.decimalFormat.parse(s).doubleValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case INT:
                try {
                    tmp = Constantes.decimalFormat.parse(textField.getText().replaceAll("\\.", ",")).intValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return tmp;
    }

    public static void main(String[] args) {
        System.out.println(Constantes.decimalFormat.format(0.4));
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String s;
        switch (type) {
            case DATE:
                dateChooser.setDate((Date) value);
                break;
            case STRING:
                textField.setText((String) value);
                textField.setSelectionStart(column);
                break;
            case DOUBLE:
                s = Constantes.decimalFormat.format(value);
                s = s.replaceAll("\\.", "").replaceAll(",", ".");
                textField.setText(s);
                break;
            case INT:
                s = Constantes.decimalFormat.format(value);
                s = s.replaceAll("\\.", "").replaceAll(",", ".");
                textField.setText(s);
                break;
        }
        return component;
    }

    public void actionPerformed(ActionEvent e) {
    }
}
