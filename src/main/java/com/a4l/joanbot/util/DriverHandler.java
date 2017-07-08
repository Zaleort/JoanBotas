package com.a4l.joanbot.util;

import com.a4l.joanbot.Categorias;
import com.google.common.base.Function;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverHandler {
    public static boolean login(String user, String passwd, WebDriver driver) throws InterruptedException{
        if (!driver.getCurrentUrl().equals("http://blaster.blastingnews.com/"))
            driver.get("http://blaster.blastingnews.com/");

        WebElement userForm, passwdForm, loginBtn;

        userForm = driver.findElement(By.id("edtInputLogin"));
        passwdForm = driver.findElement(By.id("edtInputPassword"));
        loginBtn = driver.findElement(By.id("btnLogin"));

        userForm.clear();
        passwdForm.clear();
        userForm.sendKeys(user);
        passwdForm.sendKeys(passwd);

        loginBtn.click();
        
        Thread.sleep(1000);
        
        if (isLogged(driver)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Sesión iniciada correctamente");

            alert.showAndWait();
            return true;
        }
        
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al iniciar sesión. Comprueba tu usuario y contraseña");

            alert.showAndWait();
            return false;
        }
    }
    
    public static boolean writeTitle(WebDriver driver, String title) throws InterruptedException{
        if (!driver.getCurrentUrl().equals("http://blast.blastingnews.com/news/edit/"))
            driver.get("http://blast.blastingnews.com/news/edit/");
        
        WebElement eTitle = driver.findElement(By.id("news-title"));
        eTitle.sendKeys(title);
        WebElement eSubTitle = driver.findElement(By.id("news-subtitle"));
        eSubTitle.click();
        
        Thread.sleep(500);
        
        try {
            WebElement error = driver.findElement(By.id("news-title-error"));
            if (error.isDisplayed()){
                System.out.println("Título demasiado parecido");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Título demasiado parecido a uno ya existente");
                alert.setHeaderText(null);
                alert.showAndWait();
                return false;
            }
            
        } catch (NoSuchElementException e){
            
        }
        
        System.out.println("Título correcto");
        return true;
    }
    
    public static void writeSubtitle (WebDriver driver, String subTitle){
        WebElement eTitle = driver.findElement(By.id("news-subtitle"));
        eTitle.sendKeys(subTitle);
    }
    
    public static void writeNews(WebDriver driver, String news){
        WebElement iframe = driver.findElement(By.id("news-body_ifr"));
        iframe.sendKeys(Keys.RETURN);
        iframe.sendKeys(Keys.BACK_SPACE);
        iframe.sendKeys(news);
    }
    
    public static void writeFuentes(WebDriver driver, String fuentes){
        WebElement sourceBtn = driver.findElement(By.id("link-source"));
        sourceBtn.click();
        
        WebElement sources = driver.findElement(By.id("source-insert"));
        sources.sendKeys(fuentes);
    }
    
    public static void addLink(WebDriver driver, String url, String name) throws InterruptedException, IOException{
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(5, TimeUnit.SECONDS)
                .pollingEvery(250, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
        
        WebElement linkBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("mceu_0")));
        
        try {
            linkBtn.click();
        } catch (WebDriverException e){
           JavascriptExecutor jse = (JavascriptExecutor)driver;
           jse.executeScript("window.scrollBy(0,-200)", "");
           linkBtn.click();
        }

        WebElement urlText = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver){
                return driver.findElement(By.className("mce-placeholder"));
            }
        });

        urlText.sendKeys(url);
        
        String id = urlText.getAttribute("id");
        int nId = calcularId(id);
        
        WebElement nameText = driver.findElement(By.id("mceu_" + (nId + 1)));
        nameText.clear();
        nameText.sendKeys(name);
        
        WebElement sendBtn = driver.findElement(By.id("mceu_" + (nId + 4)));
        sendBtn.click();
    }
    
    public static void sendNews(WebDriver driver) {
        WebElement sendBtn = driver.findElement(By.id("button-send"));
        sendBtn.click();
        
        WebDriverWait wait = new WebDriverWait(driver, 3);
        
        WebElement siguiente = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-ok")));
        siguiente.click();
        
        siguiente = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-ok")));
        //siguiente.click();
    }
    
    public static void saveNews(WebDriver driver){
        WebElement saveBtn = driver.findElement(By.id("button-proof"));
        saveBtn.click();
    }
    
    public static String search(String search, WebDriver driver) throws InterruptedException{
        String url = driver.getCurrentUrl();
        
        if (!url.startsWith("http://es.blastingnews.com/"))
            driver.get("http://es.blastingnews.com/");
        
        WebElement searchBox, searchBtn;
        searchBox = driver.findElement(By.id("search"));
        searchBtn = driver.findElement(By.className("search-button"));     
        
        searchBox.sendKeys(search); 
        searchBox.sendKeys(" Queso");
        searchBtn.click();
        
        //Thread.sleep(2500);
        
        WebElement noticia = driver.findElement(By.className("search-item-title"));
        noticia.click();

        ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        
        //Thread.sleep(250);
        
        url = driver.getCurrentUrl();
        driver.close();
        
        driver.switchTo().window(tabs2.get(0));
        return url;
    }
    
    public static String fastSearch(String search, WebDriver driver){
        String url;
        driver.get("http://es.blastingnews.com/search?q=" + search);
        
        WebElement noticia = driver.findElement(By.className("search-item-title"));
        noticia.click();

        ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        
        url = driver.getCurrentUrl();
        driver.close();
        
        driver.switchTo().window(tabs2.get(0));
        return url;
    }
    
    public static boolean setPhoto(WebDriver driver, String[] etiquetas) throws InterruptedException, IOException{
        WebElement photoBtn = driver.findElement(By.id("upload-photo-default"));
        photoBtn.click();
        
        WebElement eSearch = driver.findElement(By.id("input-search"));
        eSearch.clear();
        for (int i = 0; i < etiquetas.length; i++){
            if ((i+1 >= etiquetas.length))
                eSearch.sendKeys(etiquetas[i]);
            else
                eSearch.sendKeys(etiquetas[i] + " ");
        }
 
        eSearch.sendKeys(Keys.RETURN);
        
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement photo;
        
        try{
            photo = wait.until(ExpectedConditions.elementToBeClickable((By.className("box-img"))));
            photo.click();
        } catch (TimeoutException e){
            System.out.println("No hay imágenes");
            WebElement close = driver.findElement(By.className("close-search-image"));
            close.click();
            WebElement container = driver.findElement(By.id("divGalleryBackground"));
            wait.until(ExpectedConditions.invisibilityOf(container));
            return false;
        }
        
        
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnDoneAndNext")));
        button.click();
        
        boolean success = true;
        
        try{
            button = driver.findElement(By.id("btnSaveAndClose"));
            button = wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            
        } catch (TimeoutException | NoSuchElementException nse){
            System.out.println("Imagen inválida");
            button = wait.until(ExpectedConditions.elementToBeClickable(By.className("upload-ok")));
            button.click();
            boolean pelo = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal-photo-upload")));
            photo.click();
            
            List<WebElement> photos = driver.findElements(By.className("box-img"));
            success = trySetPhoto(driver, photos);
        }

        if (!success) {
            WebElement close = driver.findElement(By.className("close-search-image"));
            close.click();
        }
        
        WebElement container = driver.findElement(By.id("divGalleryBackground"));
        wait.until(ExpectedConditions.invisibilityOf(container));
        
        return success;
    }
    
    private static boolean trySetPhoto(WebDriver driver, List<WebElement> photos){
        WebElement button;
        WebDriverWait wait = new WebDriverWait(driver, 5);
        
        for (int i = 1; i < photos.size(); i++){
            photos.get(i).click();
            button = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnDoneAndNext")));
            button.click();
            
            try{
                button = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSaveAndClose")));
                button.click();
            } catch (TimeoutException e){
                button = wait.until(ExpectedConditions.elementToBeClickable(By.className("upload-ok")));
                button.click();
                boolean pelo = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal-photo-upload")));
                photos.get(i).click();
                
                continue;
            }
            
            return true;
        }
        
        System.out.println("No se ha logrado encontrar una imágen válida");
        return false;
    }
    
    public static void setCategory(WebDriver driver, String categoria){
        WebElement categoriasBtn = driver.findElement(By.id("news-category"));
        categoriasBtn.click();
        
        WebElement categorias = driver.findElement(By.id("news-category-list"));
        List<WebElement> categoriasList = new ArrayList<>();
        
        String _categoria = categoria.replaceAll(" ", "");
        String id = Categorias.valueOf(_categoria).getID();
        
        categoriasList = categorias.findElements(By.tagName("li"));
        for (WebElement cat : categoriasList){
            if (cat.getAttribute("class").equals(id)){
                
                Coordinates c = ((Locatable)cat).getCoordinates();
                c.inViewPort();
                
                cat.click();
                
                break;
            } 
        }
    }
    
    public static void setEtiquetas(WebDriver driver, String[] etiquetas) throws InterruptedException, IOException{
        WebElement etiqueta = driver.findElement(By.id("input-dei-tag"));
        etiqueta.click();
        
        for (String str : etiquetas) {
            etiqueta.sendKeys(str);
            etiqueta.sendKeys(Keys.RETURN);
            Thread.sleep(150);
        }
    }
    
    public static void setNoBlasterHelp(WebDriver driver){
        WebElement slider = driver.findElement(By.className("slider-horizontal"));
        int width=slider.getSize().getWidth();
        Actions move = new Actions(driver);
        move.moveToElement(slider, ((width)/10), 0).click();
        move.build().perform();
    }
    
    private static int calcularId(String id){
        String[] split = id.split("");
        String result = "";
        boolean finCadena = false;
                
        for(int i = 0; i < split.length; i++){
            if (isNumber(split[i])){
                result += split[i];
                finCadena = true;
            }  
            
            else if (!isNumber(split[i]) && finCadena)
                break;
        }
        
        return Integer.parseInt(result);
    }
    
    private static boolean isNumber(String str){
        char c = str.charAt(0);
        if (c < '0' || c > '9') {
            return false;
        }

        return true;
    }
    
    public static boolean isLogged(WebDriver driver) {
        if (!driver.getCurrentUrl().contains("blastingnews"))
                driver.get("http://blaster.blastingnews.com/");

            try {
                WebElement logged = driver.findElement(By.id("logged"));
            } catch (NoSuchElementException e){
                return false;
            }
        
        return true;
    }
    
    public static void takeScreenshot(WebDriver driver) throws IOException{
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("D:\\screenshot.png"));
    }
    
    public static void takeScreenshot(WebDriver driver, String file) throws IOException{
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("D:\\" + file));
    }
    
    public static void closePopUp(WebDriver driver){
        try{
            WebElement closeBtn = driver.findElement(By.id("welcome-bar-close"));
            closeBtn.click();
        } catch (Exception e){
            System.out.println("Ya no se encuentra el Pop-Up");
        }
    }
}