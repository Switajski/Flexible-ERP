package org.flexible.order.section;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public class Menu {

    private final WebDriver webDriver;

    public Menu(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void clickFirstMenuItem() {
        webDriver.findElement(By.id("i_customer_itemlist")).click();
    }

    public void clickSecondMenuItem() {
        webDriver.findElement(By.id("i_creditnotes_createissue")).click();
    }
}
