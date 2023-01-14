/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;

/**
 *
 * @author fabnun
 */
public class ButtonColor extends JButton {

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public ButtonColor() {
        super();
        final ButtonColor este=this;
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color c=JColorChooser.showDialog(este, este.getToolTipText(), este.color);
                if (c!=null){
                    este.color=c;
                    este.setForeground(c);
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(2,2,getWidth()-4, getHeight()-4);
    }


}
