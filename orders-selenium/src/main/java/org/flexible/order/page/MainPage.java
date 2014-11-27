package org.flexible.order.page;

import org.flexible.order.UrlNameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public class MainPage extends AbstractPage {

    public MainPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void open() {
        getWebDriver().get(UrlNameUtils.MAIN_PAGE_URL);
    }

    public String findWelcomeText() {
        return getWebDriver().findElement(By.id("_title_title_id")).findElement(By.tagName("p")).getText();
        // WebDriver webDriver = getWebDriver();
        // By byDivId = By.id("_title_title_id");
        // WebElement divElement = webDriver.findElement(byDivId);
        // By byPTagName = By.tagName("p");
        // WebElement pElement = divElement.findElement(byPTagName);
        // String welcomeText = pElement.getText();
        // return welcomeText;
    }

    public String findImageSource() {
        return getWebDriver().findElement(By.id("_title_title_id")).findElement(By.tagName("img")).getAttribute("src");
    }
}
