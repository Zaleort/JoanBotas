package com.a4l.joanbot;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Main {
    
    public static void main(String[] args) throws InterruptedException{
        WebDriver driver = null;
        System.setProperty(
            "webdriver.chrome.driver", 
            "chromedriver.exe");

        if (args.length == 0){
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("disable-gpu");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(capabilities);
        }
        
        else if (args[0].equals("-dev")){
            driver = new ChromeDriver();
            driver.manage().window().setSize(new Dimension(800, 600));
        }

        MainFX.driver = driver;
        MainFX.launch(new MainFX().getClass());
    }
}