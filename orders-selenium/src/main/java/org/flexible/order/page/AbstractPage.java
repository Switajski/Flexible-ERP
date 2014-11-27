package org.flexible.order.page;

import org.flexible.order.section.Footer;
import org.flexible.order.section.Header;
import org.flexible.order.section.Menu;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public abstract class AbstractPage {
    private final WebDriver webDriver;
    private final Footer footer;
    private final Header header = new Header();
    private final Menu menu;

    public AbstractPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.menu = new Menu(webDriver);
        this.footer = new Footer(webDriver);
    }

    public abstract void open();

    protected WebDriver getWebDriver() {
        return webDriver;
    }

    public Footer getFooter() {
        return footer;
    }

    public Header getHeader() {
        return header;
    }

    public Menu getMenu() {
        return menu;
    }
}
