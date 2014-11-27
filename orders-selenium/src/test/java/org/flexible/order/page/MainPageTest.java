package org.flexible.order.page;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.flexible.order.UrlNameUtils;
import org.flexible.order.section.Footer;
import org.flexible.order.section.Menu;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public class MainPageTest {
    private WebDriver webDriver;

    @Before
    public void setUp() {
        // System.setProperty("webdriver.chrome.driver", "C:\\workspace\\chromedriver.exe");
        // WebDriver webDriver = new ChromeDriver();
        // TODO @Mariusz implement a way to exchange driver for different browsers (at least Firefox, Chrome, Safari)
        webDriver = new FirefoxDriver();
    }

    // @After
    // public void tearDown() {
    // webDriver.quit();
    // }

    @Test
    public void shouldOpenMainPage() {
        // GIVEN
        MainPage testedPage = new MainPage(webDriver);

        // WHEN
        testedPage.open();

        // THEN
        assertThat(webDriver.getCurrentUrl(), is(equalTo(UrlNameUtils.MAIN_PAGE_URL)));
        assertThat(testedPage.findWelcomeText(), is(startsWith("Flexible Orders unterstützt Startup Unternehmen")));
        assertThat(testedPage.findImageSource(), is(containsString("/FlexibleOrders/resources/images/bestellprozess.png")));
    }

    @Test
    public void shouldOpenListItemsPage() {
        // GIVEN
        MainPage mainPage = new MainPage(webDriver);
        Menu testedMenu = new Menu(webDriver);

        // WHEN
        mainPage.open();
        testedMenu.clickFirstMenuItem();

        // THEN
        assertThat(webDriver.getCurrentUrl(), is(startsWith(UrlNameUtils.LIST_ITEMS_URL)));
    }

    @Test
    public void shouldOpenCreditNotes() {
        // GIVEN
        MainPage mainPage = new MainPage(webDriver);
        Menu testedMenu = new Menu(webDriver);

        // WHEN
        mainPage.open();
        testedMenu.clickSecondMenuItem();

        // THEN
        assertThat(webDriver.getCurrentUrl(), is(startsWith(UrlNameUtils.CREDIT_NOTES_URL)));
    }

    @Test
    public void shouldOpenEnglishMainPage() {
        // GIVEN
        MainPage mainPage = new MainPage(webDriver);
        Footer testedFooter = new Footer(webDriver);

        // WHEN
        mainPage.open();
        testedFooter.clickEnglishLanguage();

        // THEN
        assertThat(mainPage.findWelcomeText(), is(containsString("is a java web application")));

    }

    @Test
    public void shouldOpenGermanMainPage() {
        // GIVEN
        MainPage mainPage = new MainPage(webDriver);
        Footer testedFooter = new Footer(webDriver);

        // WHEN
        mainPage.open();
        testedFooter.clickEnglishLanguage();
        testedFooter.clickGermanLanguage();

        // THEN
        assertThat(mainPage.findWelcomeText(), is(containsString("Unternehmen")));

    }
}
