package com.a4l.joanbot;

import com.a4l.joanbot.util.DriverHandler;
import com.a4l.joanbot.util.KeyWordGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class SendNoticia extends Task<Integer> {
    private final String categoria, titulo, subtitulo, noticia, fuentes;
    private final String[] etiquetas;
    
    private WebDriver driver;
    
    public SendNoticia(String categoria, String titulo, String subtitulo, String noticia, String[] etiquetas, String fuentes, 
            WebDriver driver){
        this.categoria = categoria;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.etiquetas = etiquetas;
        this.fuentes = fuentes;   
        this.driver = driver;
    }
    
    @Override
    public Integer call(){
        try {  
            KeyWordGenerator kwg = new KeyWordGenerator(titulo, subtitulo, noticia, etiquetas);
            kwg.calcularKeyWord();

            String urlP, urlS;
            urlP = DriverHandler.fastSearch(kwg.getKeyPrimaria(), driver);
            
            if (this.isCancelled()){
                return 0;
            }
            
            urlS = DriverHandler.fastSearch(kwg.getKeySecundaria(), driver);

            driver.get("http://blast.blastingnews.com/news/edit/");
            
            if (this.isCancelled()){
                return 0;
            }

            DriverHandler.setCategory(driver, categoria); 

            if (DriverHandler.writeTitle(driver, titulo)){
                DriverHandler.writeSubtitle(driver, subtitulo);
                
                if (this.isCancelled()){
                    return 0;
                }
                
                boolean isCorrect = DriverHandler.setPhoto(driver, etiquetas);
                DriverHandler.setNoBlasterHelp(driver);
                
                if (this.isCancelled()){
                    return 0;
                }
                
                buildNoticia(noticia, kwg.getKeyPrimaria(), kwg.getKeySecundaria(), urlP, urlS);
                
                if (this.isCancelled()){
                    return 0;
                }

                DriverHandler.setEtiquetas(driver, etiquetas);
                DriverHandler.writeFuentes(driver, fuentes);

                if (!isCorrect){
                    DriverHandler.saveNews(driver);
                    return 1;
                }

                else {
                    DriverHandler.saveNews(driver);
                    DriverHandler.sendNews(driver);
                }
            }
            
            else return 2;

        } catch (InterruptedException ex) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, ex);
 
        } catch (WebDriverException we){
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, we);
            return -1;
        }
       
       return 0;
    }
    
    private void buildNoticia(String noticia, String keyPrimaria, String keySecundaria, String urlP, String urlS) {
        int indexKP, indexKS, index1, index2, lIndex1, lIndex2;
        int kpLength = keyPrimaria.length();
        int ksLength = keySecundaria.length();
        boolean orden;
        String builder = noticia.toLowerCase();
        
        indexKP = builder.indexOf(keyPrimaria + " ");
        if (indexKP == -1)
            indexKP = builder.indexOf(keyPrimaria + ".");
        if (indexKP == -1)
            indexKP = builder.indexOf(keyPrimaria);
        
        indexKS = builder.indexOf(keySecundaria + " ");
        if (indexKS == -1)
            indexKS = builder.indexOf(keySecundaria + ".");
        if (indexKS == -1)
            indexKS = builder.indexOf(keySecundaria);
        
        if (indexKP < indexKS){
            orden = true;
            index1 = indexKP;
            lIndex1 = kpLength;
            index2 = indexKS;
            lIndex2 = ksLength;
        }
        
        else {
            orden = false;
            index1 = indexKS;
            lIndex1 = ksLength;
            index2 = indexKP;
            lIndex2 = kpLength;
        }
        
        System.out.println("Index 1: " + indexKP);
        System.out.println("Index 2: " + indexKS);
        System.out.println("Primaria length: " + kpLength);
        System.out.println("Secundaria length: " + ksLength);
        
        String subNoticia, subNoticia2, subNoticia3;
        
        // Normalización de mayúsculas
        StringBuilder key = new StringBuilder();
        for (int i = indexKP; i < indexKP + kpLength; i++){
            key.append(noticia.charAt(i));
        }
        keyPrimaria = key.toString();
        
        key = new StringBuilder();
        for (int i = indexKS; i < indexKS + ksLength; i++){
            key.append(noticia.charAt(i));
        }
        keySecundaria = key.toString();
        
        subNoticia = noticia.substring(0, index1);
        subNoticia2 = noticia.substring(index1 + lIndex1, index2);
        subNoticia3 = noticia.substring(index2 + lIndex2);

        if (orden){
            DriverHandler.writeNews(driver, subNoticia);       
            DriverHandler.addLink(driver, urlP, keyPrimaria);
            DriverHandler.writeNews(driver, subNoticia2);
            DriverHandler.addLink(driver, urlS, keySecundaria);
            DriverHandler.writeNews(driver, subNoticia3);
        }
        
        else {
            DriverHandler.writeNews(driver, subNoticia);
            DriverHandler.addLink(driver, urlS, keySecundaria);
            DriverHandler.writeNews(driver, subNoticia2);
            DriverHandler.addLink(driver, urlP, keyPrimaria);
            DriverHandler.writeNews(driver, subNoticia3);
        }
    }
}