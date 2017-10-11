package com.a4l.joanbot;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    
    public static void main(String[] args){
        WebDriver driver = null;
        System.setProperty(
            "webdriver.chrome.driver", 
            "chromedriver.exe");

        if (args.length > 0){
            if (args[0].equals("-dev")){
                driver = new ChromeDriver();
                driver.manage().window().setSize(new Dimension(1024, 768));
            }
        }
        
        else {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            options.addArguments("window-size=1024,768");
            options.addArguments("disable-gpu");
            driver = new ChromeDriver(options);
        }
        
        MainFX.driver = driver;
        MainFX.launch(new MainFX().getClass());
    }
}