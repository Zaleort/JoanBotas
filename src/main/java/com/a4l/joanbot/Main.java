package com.a4l.joanbot;

import com.a4l.joanbot.util.DriverHandler;
import org.openqa.selenium.WebDriver;

public class Main {
    
    public static void main(String[] args){
        WebDriver driver;
        driver = DriverHandler.launchDriver(args);

        MainFX.driver = driver;
        MainFX.args = args;
        MainFX.launch(new MainFX().getClass());
    }
}