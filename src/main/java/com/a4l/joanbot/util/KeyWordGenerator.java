package com.a4l.joanbot.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class KeyWordGenerator {
    private final String titulo, subtitulo, noticia, etiqueta;
    private String keyPrimaria = "", keySecundaria = "";

    public String getKeyPrimaria() {
        return keyPrimaria;
    }

    public void setKeyPrimaria(String keyPrimaria) {
        this.keyPrimaria = keyPrimaria;
    }

    public String getKeySecundaria() {
        return keySecundaria;
    }

    public void setKeySecundaria(String keySecundaria) {
        this.keySecundaria = keySecundaria;
    }
    
    Set<String> ignoradas = new HashSet<>();
    
    public KeyWordGenerator (String titulo, String subtitulo, String noticia, String etiqueta){
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.etiqueta = etiqueta;
        
        loadFilter();
    }
    
    public void calcularKeyWord(){
        String[] etiquetas = etiqueta.toLowerCase().replaceAll(" ", "").split(",");
        String tNoticia = noticia.toLowerCase();
        for (String str : etiquetas){
            if (tNoticia.contains(str)){
                if (keyPrimaria.isEmpty())
                    keyPrimaria = str;
                
                else if (keySecundaria.isEmpty()){
                    keySecundaria = str;
                    break;
                }
            }     
        }
        
        if (keyPrimaria.isEmpty() || keySecundaria.isEmpty()){
            tNoticia = tNoticia.replaceAll("[^A-Za-zÁ-ú\\s]", "");
            tNoticia = tNoticia.replaceAll("\n", " ");
            tNoticia = tNoticia.trim();
            
            String tTitulo = titulo.replaceAll("[^A-Za-zÁ-ú\\s]", "");
            tTitulo = tTitulo.toLowerCase();
            
            String tSubtitulo = subtitulo.replaceAll("[^A-Za-zÁ-ú\\s]", "");
            tSubtitulo = tSubtitulo.toLowerCase();
            
            String[] palabras = tNoticia.split(" ");
            String[] tituloPalabras = tTitulo.split(" ");
            String[] subtituloPalabras = tSubtitulo.split(" ");
            int[] count = new int[palabras.length];
            List<String> yaAnalizado = new ArrayList<>();
            
            int i = 0;
            for (; i < palabras.length; i++){
                if (yaAnalizado.contains(palabras[i]) || ignoradas.contains(palabras[i]))
                    continue;
                
                int j = 0;
                for (; j < palabras.length; j++){
                    if (palabras[i].equals(palabras[j])){
                        count[i] += 1;
                    }
                }
                
                j = 0;
                for (; j < tituloPalabras.length; j++){
                    if (palabras[i].equals(tituloPalabras[j])){
                        count[i] += 5;
                    }
                }
                
                j = 0;
                for (; j < subtituloPalabras.length; j++){
                    if (palabras[i].equals(subtituloPalabras[j])){
                        count[i] += 3;
                    }
                }

                yaAnalizado.add(palabras[i]);
                System.out.println(palabras[i] + ": " + count[i]);
            }
        
        
            i = 0;
            int[] key1 = {0,0};
            int[] key2 = {0,0};
            for (; i < count.length; i++){
                if (count[i] > key1[0]){
                    key1[0] = count[i];
                    key1[1] = i;
                }

                else if (count[i] > key2[0] && key1[1] != i){
                    key2[0] = count[i];
                    key2[1] = i;   
                }
            }
            
            if (keyPrimaria.isEmpty() && keySecundaria.isEmpty()){
                keyPrimaria = palabras[key1[1]];
                keySecundaria = palabras[key2[1]];
            }
            
            else if (keyPrimaria.isEmpty() && keySecundaria.isEmpty()){
                keySecundaria = palabras[key1[1]];
            }
        }
        
        System.out.println(keyPrimaria);
        System.out.println(keySecundaria);
    }
    
    private void loadFilter(){
        Path path = Paths.get(".\\src\\main\\resources\\filter.txt");
        try  (Stream<String> lines = Files.lines(path, Charset.forName("ISO-8859-1"))) {
            lines.forEach(s -> ignoradas.add(s));
        
        } catch (IOException ex) {
            
        }
    }
}
