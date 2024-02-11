package com.qaexamples.util;

import org.openqa.selenium.By;

public class CustomBy {

    private CustomBy(){
    }

   public static By imageSource(String imageSrcVal) {
        return By.xpath(String.format("//img[@src='" + imageSrcVal + "']"));
    }
}
