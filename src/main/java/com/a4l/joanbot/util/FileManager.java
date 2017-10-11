package com.a4l.joanbot.util;

import com.a4l.joanbot.Noticias;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {
    public static void writeFile(File file, Object obj){
        ObjectOutputStream flujoSalida = null;

        try {
            flujoSalida = new ObjectOutputStream(new FileOutputStream(file));
            flujoSalida.writeObject(obj);
            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                if (flujoSalida != null){
                    flujoSalida.flush();
                    flujoSalida.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void modifyFile(File file, Object obj){
        File newF = new File("tmp_" + file.getName());
        ObjectOutputStream flujoSalida = null;

        try {
            flujoSalida = new ObjectOutputStream(new FileOutputStream(newF));
            flujoSalida.writeObject(obj);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }  finally {
            try {
                if (flujoSalida != null){
                    flujoSalida.flush();
                    flujoSalida.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        file.delete();
        newF.renameTo(file);
    }
    
    public static Noticias readNoticia(File file){
        
        ObjectInputStream miFlujoEntrada = null;
        Noticias noticia = null;
        
        try {
            miFlujoEntrada = new ObjectInputStream(new FileInputStream(file));
            try {
                noticia = (Noticias) miFlujoEntrada.readObject();
                
            } catch (ClassNotFoundException ex) {
                System.err.println(Arrays.toString(ex.getStackTrace()));
            } catch (EOFException ex){
                System.out.println("Puto Alberto como la lía");
            }
        } catch (FileNotFoundException ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (miFlujoEntrada != null)
                    miFlujoEntrada.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return noticia; 
    }
}
