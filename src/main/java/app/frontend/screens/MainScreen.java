package app.frontend.screens;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static core.utils.PropertyLoader.getProperty;
import static org.testng.AssertJUnit.assertTrue;


public class MainScreen extends BaseScreen {

    public @iOSXCUITFindBy(accessibility = "selected_group_name")
    IOSElement selected_group_name;

    public @iOSXCUITFindBy(id = "Trade")
    IOSElement Trade;

    public @iOSXCUITFindBy(id = "Portfolio")
    IOSElement Portfolio;

    public @iOSXCUITFindBy(accessibility = "selected_asset_name")
    IOSElement default_instrument;

    public @iOSXCUITFindBy(iOSNsPredicate = "value == \"ADAUSD\"")
    IOSElement instrument_set;

    public @iOSXCUITFindBy(accessibility = "order_name")
    IOSElement order_name;

    public @iOSXCUITFindBy(accessibility = "order_open_date")
    IOSElement order_open_date;

    public @iOSXCUITFindBy(id = "BUY")
    IOSElement BUY_trade;

    public @iOSXCUITFindBy(accessibility = "action_button")
    IOSElement BUY_order;

    public @iOSXCUITFindBy(accessibility = "tp_text_field")
    IOSElement tp_create;

    public @iOSXCUITFindBy(accessibility = "tp_text_field")
    IOSElement tp_open;

    public @iOSXCUITFindBy(accessibility = "sl_text_field")
    IOSElement sl_create;

    public @iOSXCUITFindBy(accessibility = "sl_text_field")
    IOSElement sl_open;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[./preceding-sibling::*[@name='Order #'] and ./following-sibling::*[@name='Swap']]")
    IOSElement order_number_position_opening;

    public @iOSXCUITFindBy(accessibility = "volume_text_field")
    IOSElement volume_lots;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[contains(@name,'.') and ./preceding-sibling::*[@name='Volume, Units:']]")
    IOSElement volume_units;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[./preceding-sibling::*[@name='Asset Leverage:']]")
    IOSElement asset_leverage;

    public @iOSXCUITFindBy(accessibility = "tp_switch")
    IOSElement Close_at_Profit;

    public @iOSXCUITFindBy(accessibility = "sl_switch")
    IOSElement Close_at_Loss;

    public @iOSXCUITFindBy(xpath = "//UIAButton[1][1]")
    IOSElement close_dialog;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[contains(@name,'#') and ./preceding-sibling::*[@name='The order is closed']]")
    IOSElement notification_order_closed;

    public @iOSXCUITFindBy(id = "Open")
    IOSElement Open;

    public @iOSXCUITFindBy(id = "History")
    IOSElement History;

    public @iOSXCUITFindBy(accessibility = "cancel_button")
    IOSElement CLOSE_POSITION;

    public @iOSXCUITFindBy(id = "BUY")
    IOSElement operation_open_buy;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[contains(@name,'.') and ./preceding-sibling::*[@name='BUY']][1]")
    IOSElement volume_open;

    public @iOSXCUITFindBy(iOSNsPredicate = "value == \"ADAUSD\"")
    IOSElement instrument_order_open;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[./preceding-sibling::*[@name='@']]")
    IOSElement open_price_notification;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[@width<100 and contains(@name,'$ ') or contains(@name,'€ ') and ./preceding-sibling::*[@name='Profit']]")
    IOSElement profit_notification;

    public @iOSXCUITFindBy(xpath = "//UIAButton[@name='viewSettings']")
    IOSElement history_settings;

    public @iOSXCUITFindBy(xpath = "//UIAStaticText[contains(@name,'From period:')]")
    IOSElement From_period;

    public @iOSXCUITFindBy(accessibility = "history_profit_text")
    List<IOSElement> history_profit_text;

    public @iOSXCUITFindBy(accessibility = "history_info_tex")
    IOSElement history_info_text;

    public @iOSXCUITFindBy(id = "No quotes")
    IOSElement No_quotes;

    public @iOSXCUITFindBy(id = "OK")
    IOSElement OK;

    public @iOSXCUITFindBy(id = "Nothing yet.\nChange the filter settings or choose an instrument to trade.")
    IOSElement Nothing_yet;

    public MainScreen(IOSDriver<IOSElement> driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Override
    public void waitScreenLoaded() {
        try {
            driverWait.until(ExpectedConditions.visibilityOf(Trade));
        } catch (TimeoutException e) {
            throw new TimeoutException();
        }
    }

    @Override
    public void openScreen() {
        LoginScreen loginScreen = new LoginScreen(driver);
        loginScreen.openScreen();
        loginScreen.waitClickClearEnterDataInField(loginScreen.waitGetClickableElement(loginScreen.Email), getProperty("user.email"));
        loginScreen.waitClickClearEnterDataInField(loginScreen.waitGetClickableElement(loginScreen.Password), getProperty("user.password"));
        loginScreen.waitGetClickableElement(loginScreen.Login).click();
        waitScreenLoaded();
    }

    public boolean tapExistedOrderInPortfolio(String text) {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        for (int i = 1; i < 5; i++) {
            System.out.println("[DEBUG] Attempt to tap on the order in Portfolio - " + i + " time");
            assertTrue(tapEnabledElement(text));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                driver.findElementByAccessibilityId("cancel_button");
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                return true;
            } catch (NoSuchElementException e) {
                System.out.println("[WARNING] Try again...");
            }
        }
        System.out.println("[ERROR] Order is not extended...");
        return false;
    }

    public void tapClosePositionInOrderDialog() {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        for (int i = 1; i < 5; i++) {
            System.out.println("[DEBUG] Attempt to tap on CLOSE POSITION - " + i + " time");
            CLOSE_POSITION.click();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                driver.findElementByAccessibilityId("cancel_button");
                System.out.println("[WARNING] Try again...");
            } catch (NoSuchElementException e) {
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                return;
            }
        }
        System.out.println("[ERROR] Order is not closed...");
    }

    public boolean isOpenPriceValidInOrderDialog(String value) {
        try {
            driver.findElementByIosNsPredicate("value CONTAINS \"" + value + "\"");
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresent(String text) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.findElementByIosNsPredicate("value == \"" + text + "\"");
                result = true;
                break;
            } catch (StaleElementReferenceException ignored) {
            }
            attempts++;
        }
        return result;
    }

    public boolean tapEnabledElement(String text) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.findElementByIosNsPredicate("value == \"" + text + "\"").click();
                result = true;
                break;
            } catch (StaleElementReferenceException ignored) {
            }
            attempts++;
        }
        return result;
    }
}