package org.fabnun.senu.graficos;

import java.awt.Image;
import java.util.List;

public interface GraficoInterface {

    public Image paint();

    public void setData(List<Object[]> data);
}
