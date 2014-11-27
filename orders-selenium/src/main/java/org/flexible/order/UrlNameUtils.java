package org.flexible.order;

/**
 * 
 * @author Mariusz Malinowski
 * @since 2014.11.09
 */
public final class UrlNameUtils {
    private UrlNameUtils() {
        // No instance allowed
    }

    public final static String MAIN_PAGE_URL = "http://localhost:8080/FlexibleOrders/?lang=de";

    public final static String LIST_ITEMS_URL = "http://localhost:8080/FlexibleOrders/customers/listitems";

    public final static String CREDIT_NOTES_URL = "http://localhost:8080/FlexibleOrders/creditnotes/createissue";
}
