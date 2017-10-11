package com.a4l.joanbot;

import com.a4l.joanbot.util.DriverHandler;
import com.a4l.joanbot.util.HTMLUtil;
import com.a4l.joanbot.util.KeyWordGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class SendNoticia extends Task<Integer> {
    private String categoria, titulo, subtitulo, noticia, fuentes;
    private final String[] etiquetas;
    private boolean publish;
    
    private final WebDriver driver;
    
    public SendNoticia(String categoria, String titulo, String subtitulo, String noticia, String[] etiquetas, String fuentes, 
            WebDriver driver, boolean publish){
        this.categoria = categoria;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.etiquetas = etiquetas;
        this.fuentes = fuentes;   
        this.driver = driver;
        this.publish = publish;
    }
    
    @Override
    public Integer call(){
        try {
            noticia = Noticias.curateHTML(noticia);
            KeyWordGenerator kwg = new KeyWordGenerator(titulo, subtitulo, noticia, etiquetas);
            kwg.calcularKeyWord();

            String urlP, urlS;
            urlP = DriverHandler.search(kwg.getKeyPrimaria(), driver);
            
            // Si la URL es, no se han encontrado resultados
            if (urlP == null){
                urlP = DriverHandler.search(kwg.getKeyTerciaria(), driver);
            }
            
            if (this.isCancelled()) return 0;
            
            
            urlS = DriverHandler.search(kwg.getKeySecundaria(), driver);
            
            if (urlS == null){
                urlS = DriverHandler.search(kwg.getKeyTerciaria(), driver);
            }
            
            if (urlP.equals(urlS)){
                System.out.println("URL iguales");
                urlS = DriverHandler.searchGetSecond(kwg.getKeySecundaria(), driver);
            }
            
            System.out.println(urlP);
            System.out.println(urlS);

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
                
                // Etiqueta no permitida si el resultado es false
                if(!DriverHandler.setEtiquetas(driver, etiquetas)){
                    return 3;
                }
                DriverHandler.writeFuentes(driver, fuentes);

                if (!isCorrect){
                    DriverHandler.saveNews(driver);
                    return 1;
                }

                else if (publish) {
                    DriverHandler.saveNews(driver);
                    DriverHandler.sendNews(driver);
                } else {
                    DriverHandler.saveNews(driver);
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
        int indexKP, indexKS;
        int kpLength = keyPrimaria.length();
        int ksLength = keySecundaria.length();
        String noticiaLower = noticia.toLowerCase();
        
        indexKP = noticiaLower.indexOf(keyPrimaria + " ");
        if (indexKP == -1)
            indexKP = noticiaLower.indexOf(keyPrimaria + ".");
        if (indexKP == -1)
            indexKP = noticiaLower.indexOf(keyPrimaria);
        
        StringBuilder builder = new StringBuilder(noticia);
        builder = builder.insert(indexKP + kpLength, "</a>");
        builder = builder.insert(indexKP, "<a href='" + urlP + "' data-mce-href='" + urlP + "'>");
        
        noticiaLower = builder.toString().toLowerCase();
        
        indexKS = noticiaLower.indexOf(keySecundaria + " ");
        if (indexKS == -1)
            indexKS = noticiaLower.indexOf(keySecundaria + ".");
        if (indexKS == -1)
            indexKS = noticiaLower.indexOf(keySecundaria);
        
        builder = builder.insert(indexKS + ksLength, "</a>");
        builder = builder.insert(indexKS, "<a href='" + urlS + "' data-mce-href='" + urlS + "'>");
        
        System.out.println("Index 1: " + indexKP);
        System.out.println("Index 2: " + indexKS);
        System.out.println("Primaria length: " + kpLength);
        System.out.println("Secundaria length: " + ksLength);

        noticia = HTMLUtil.curateHTML(builder.toString());
        DriverHandler.writeNews(driver, noticia);  
        
        System.out.println(noticia);
    }
}