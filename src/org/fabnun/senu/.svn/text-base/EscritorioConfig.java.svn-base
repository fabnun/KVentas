/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fabnun.senu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.fabnun.senu.vista.Escritorio.FondoTipo;

/**
 *
 * @author fabnun
 */
public class EscritorioConfig implements Serializable {

    private static final long serialVersionUID = 1;

    public String fondo=null;
    public FondoTipo tipo=FondoTipo.CENTRADO;
    public int color=0;
    public int x=0;
    public int y=0;
    public int width=800;
    public int height=600;
    public int state=0;
    public boolean fullscreen=false;
    public boolean showDateTime=true;
    public String laf=null;
    public boolean siempreVisible=false;

    public static void save(EscritorioConfig config) throws FileNotFoundException, IOException{
        FileOutputStream fos=new FileOutputStream("config.dat");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(config);
        oos.close();
        fos.close();
    }

    public static EscritorioConfig load() throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis=new FileInputStream("config.dat");
        ObjectInputStream ois=new ObjectInputStream(fis);
        EscritorioConfig model=(EscritorioConfig) ois.readObject();
        ois.close();
        fis.close();
        return model;
    }
    public boolean showMem;


}
