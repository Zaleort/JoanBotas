package com.a4l.joanbot;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Main {
    
    public static final int NAV = 2;
    
    public static void main(String[] args) throws InterruptedException{
        WebDriver driver;
        
        if (NAV == 0){
            System.setProperty(
                "phantomjs.binary.path", 
                "phantomjs.exe");
            
            driver = new PhantomJSDriver();
            driver.manage().window().maximize();
            
        }
        
        else if (NAV == 1){
            System.setProperty(
                "webdriver.gecko.driver", 
                "geckodriver.exe");
       
            driver = new FirefoxDriver();
        }
        
        else if (NAV == 2){
            System.setProperty(
                "webdriver.chrome.driver", 
                "chromedriver.exe");
            
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
			options.addArguments("disablegpu");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(capabilities);
            driver.manage().window().setSize(new Dimension(800, 600));
        }      

        MainFX.driver = driver;
        MainFX.launch(new MainFX().getClass());
    }
}