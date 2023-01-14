/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabnun.senu.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import org.fabnun.senu.Constantes;

public class GraficoHistorialCliente implements GraficoInterface {

    Font f0 = new Font("aril", 0, 10);
    Font f1 = new Font("aril", 0, 9);

    public Image paint() {
        int sep = 60;
        int width = facturado.size() * sep + 110;
        int height = 220;
        BufferedImage img = new BufferedImage(width + 30, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();
        g.setFont(f0);
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.darkGray);
        g.drawLine(50, 200, width - 50, 200);
        int total = 0;
        for (int per : facturado.keySet()) {
            total = total + facturado.get(per);
        }
        int sumP = 0, sumT = 0;
        Integer sumP0, sumT0;
        int j = 0, val;

        Color blue = new Color(0, 0, 255, 128);
        Color red = new Color(255, 0, 0, 128);
        for (int i = minPer; i <= maxPer; i++) {
            sumT0 = facturado.get(i);
            sumP0 = cancelado.get(i);
            if (sumP0 != null && sumT0 != null) {
                sumT = sumT + sumT0;
                sumP = sumP + sumP0;
                g.setColor(Color.darkGray);
                g.drawLine(50 + j * sep, 190, 50 + j * sep, 210);
                g.setColor(Color.yellow);
                g.drawString("" + i, 55 + j * sep, 213);
                g.setColor(Color.blue);
                val = (int) (180d * (double) sumT / (double) total);
                g.fillRect(52 + j * sep, 200 - val, 20, val);
                g.setColor(blue);
                g.fillRect(53 + j * sep, 199 - val, 20, val);
                if (sumT > sumP) {
                    g.setFont(f1);
                    g.setColor(Color.yellow);
                    g.drawString("(" + Constantes.decimalFormat.format(100d * (sumT - sumP) / sumT) + "%)", 53 + j * sep, 197 - val);
                    g.drawString(Constantes.decimalFormat.format(sumT - sumP), 53 + j * sep, 189 - val);
                    g.setFont(f0);
                }
                g.setColor(Color.red);
                val = (int) (180d * (double) sumP / (double) total);
                g.fillRect(75 + j * sep, 200 - val, 20, val);
                g.setColor(red);
                g.fillRect(76 + j * sep, 199 - val, 20, val);

                j++;
            }
        }
        Color c = new Color(128, 128, 128, 128);
        for (int i = 0; i <= 180; i = i + 18) {
            g.setColor(c);
            g.drawLine(50, 200 - i, width - 60, 200 - i);
            g.setColor(Color.white);
            val = (int) ((double) total * (double) i / 180d);
            g.drawString("$" + Constantes.decimalFormat.format(val), width - 55, 205 - i);
        }
        return img;
    }
    HashMap<Integer, Integer> facturado = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> cancelado = new HashMap<Integer, Integer>();
    int minPer, maxPer;

    public void setData(List<Object[]> data) {
        Object[] o, on = null;
        facturado.clear();
        cancelado.clear();
        minPer = Integer.MAX_VALUE;
        maxPer = Integer.MIN_VALUE;
        for (int i = 0; i < data.size(); i++) {
            int sumT = 0, sumP = 0;
            o = data.get(i);
            on = (i < data.size() - 1) ? data.get(i + 1) : null;
            if (o[3].equals(0) || o[3].equals(1)) {//es una factura cancelada o sin cancelar
                int oldT = ((BigDecimal) o[1]).intValue();
                sumT = (int) (sumT + oldT);//suma el total al total general
                if (on == null || (on != null && !on[0].equals(o[0]))) {//Si no existe siguiente o
                    //existe pero son distanta factura
                    if (o[3].equals(1)) {//si la factura fue cancelada
                        sumP = (int) (sumP + ((BigDecimal) o[1]).intValue());//suma el total al total pagado
                    }
                }
            } else {
                int ab = ((BigDecimal) o[1]).intValue();
                sumP = (int) (sumP + ab);//suma el abono al total pagado
            }
            Integer old, oldF;
            int periodo = ((Double) o[2]).intValue();
            if (periodo > maxPer) {
                maxPer = periodo;
            }
            if (periodo < minPer) {
                minPer = periodo;
            }
            //sumP=Math.min(sumP, sumT);
            oldF = facturado.get(periodo);
            oldF = (oldF == null) ? 0 : oldF;
            old = cancelado.get(periodo);
            old = (old == null) ? 0 : old;
            sumT = oldF + sumT;
            sumP = old + sumP;
            facturado.put(periodo, sumT);
            cancelado.put(periodo, sumP);
        }
    }
}
