package org.flexible.order.section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public class Footer {

    private final WebDriver webDriver;

    public Footer(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void clickEnglishLanguage() {
        webDriver.findElement(By.id("language")).findElements(By.tagName("a")).get(0).click();
    }

    public void clickGermanLanguage() {
        webDriver.findElement(By.id("language")).findElements(By.tagName("a")).get(1).click();
    }
}
