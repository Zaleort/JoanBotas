package com.a4l.joanbot.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class KeyWordGenerator {
    private final String titulo, subtitulo, noticia;
    private final String[] etiquetas;
    private String keyPrimaria = "", keySecundaria = "", keyTerciaria = "";

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
    
    public String getKeyTerciaria(){
        return keyTerciaria;
    }
    
    public void setKeyTerciaria(String keyTerciaria){
        this.keyTerciaria = keyTerciaria;
    }
    
    Set<String> ignoradas = new HashSet<>();
    
    public KeyWordGenerator (String titulo, String subtitulo, String noticia, String[] etiquetas){
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.etiquetas = etiquetas;
        
        loadFilter();
    }
    
    public void calcularKeyWord(){
        String[] tEtiquetas = etiquetas;
        
        for (int i = 0; i < tEtiquetas.length; i++){
            tEtiquetas[i] = tEtiquetas[i].toLowerCase();
        }
        
        String tNoticia = noticia;
        tNoticia = HTMLUtil.deleteHTML(tNoticia);
        tNoticia = tNoticia.toLowerCase();
        for (String str : tEtiquetas){
            if (tNoticia.contains(str)){
                if (keyPrimaria.isEmpty()){
                    keyPrimaria = str;
                }

                else if (keySecundaria.isEmpty()){
                    keySecundaria = str;
                    break;
                }
                
                String[] aStr = str.split(" ");
                ignoradas.addAll(Arrays.asList(aStr));
            } 
        }

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
                    count[i] += 3;
                }
            }

            j = 0;
            for (; j < subtituloPalabras.length; j++){
                if (palabras[i].equals(subtituloPalabras[j])){
                    count[i] += 3;
                }
            }

            yaAnalizado.add(palabras[i]);
            if (count[i] > 1){
                System.out.println(palabras[i] + ": " + count[i]);
            }
        }

        i = 0;

        int key1Valor = 0, key1Index = 0;
        int key2Valor = 0, key2Index = 0;
        int key3Valor = 0, key3Index = 0;

        for (; i < count.length; i++){
            if (count[i] > key1Valor){
                key3Valor = key2Valor;
                key3Index = key2Index;
                
                key2Valor = key1Valor;
                key2Index = key1Index;
                
                key1Valor = count[i];
                key1Index = i;
            }
            
            else if (count[i] == key1Valor){
                if (count[i] > key2Valor){
                    key3Valor = key2Valor;
                    key3Index = key2Index;
                
                    key2Valor = count[i];
                    key2Index = i;
                }
                
                else if (count[i] > key3Valor){
                    key3Valor = count[i];
                    key3Index = i;
                }
            }
        }

        if (keyPrimaria.isEmpty() && keySecundaria.isEmpty()){
            keyPrimaria = palabras[key1Index];
            keySecundaria = palabras[key2Index];
            keyTerciaria = palabras[key3Index];
        }

        else if (!keyPrimaria.isEmpty() && keySecundaria.isEmpty()){
            keySecundaria = palabras[key1Index];
            keyTerciaria = palabras[key2Index];
        }

        else {
            keyTerciaria = palabras[key1Index];
        }
        
        System.out.println("Puntuación key 1: " + key1Valor);
        System.out.println("Puntuación key 2: " + key2Valor);
        System.out.println("Puntuación key 3: " + key3Valor);
        
        System.out.println("Palabra clave primaria: " + keyPrimaria);
        System.out.println("Palabra clave secundaria: " + keySecundaria);
        System.out.println("Palabra clave terciaria: " + keyTerciaria);
    }
    
    private void loadFilter(){
        Path path = Paths.get("filter.txt");
        try  (Stream<String> lines = Files.lines(path, Charset.forName("ISO-8859-1"))) {
            lines.forEach(s -> ignoradas.add(s));
        
        } catch (IOException ex) {
            System.out.println("Archivo Filtro no encontrado");
            ignoradas.add("que");
            ignoradas.add("qué");
            ignoradas.add("a");
            ignoradas.add("y");
            ignoradas.add("como");
            ignoradas.add("aun");
            ignoradas.add("de");
            ignoradas.add("el");
            ignoradas.add("la");
            ignoradas.add("es");
        }
    }
}