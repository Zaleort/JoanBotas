package com.a4l.joanbot;

import java.io.Serializable;

public class Noticias implements Serializable {
    private String titulo, subtitulo, noticia, categoria, etiquetas, fuentes;
    
    public Noticias (String titulo, String subtitulo, String noticia, String categoria, String etiquetas, String fuentes){
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.categoria = categoria;
        this.etiquetas = etiquetas;
        this.fuentes = fuentes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getNoticia() {
        return noticia;
    }

    public void setNoticia(String noticia) {
        this.noticia = noticia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getFuentes() {
        return fuentes;
    }

    public void setFuentes(String fuentes) {
        this.fuentes = fuentes;
    }
    
    public static String curateHTML(String noticia){
        String curatedNoticia;
        
        curatedNoticia = noticia;
        curatedNoticia = curatedNoticia.replaceAll("<div>", "<p>");
        curatedNoticia = curatedNoticia.replaceAll("</div>", "</p>");
        curatedNoticia = curatedNoticia.replaceAll("<b>", "<strong>");
        curatedNoticia = curatedNoticia.replaceAll("</b>", "</strong>");
        curatedNoticia = curatedNoticia.replaceAll("<i>", "<em>");
        curatedNoticia = curatedNoticia.replaceAll("</i>", "</em>");
        curatedNoticia = curatedNoticia.replaceAll("(<\\/?font)[\\s\\S]*?(>)", "");
        
        return curatedNoticia;
    }
}