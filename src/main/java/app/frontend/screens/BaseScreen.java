package app.frontend.screens;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;


public abstract class BaseScreen {

    protected IOSDriver<IOSElement > driver;
    protected WebDriverWait driverWait;
    protected Logger logger = LogManager.getLogger(BaseScreen.class);

    public BaseScreen(IOSDriver<IOSElement > driver) {
        this.driver = driver;
        driverWait = new WebDriverWait(driver, 30);
    }

    public IOSElement getElement(String text){
        return driver.findElementByName(text);
    }

    public boolean isElementPresent(String text){
        try{
            driver.findElementByName(text);
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isElementVisible(String text){
        IOSElement element;
        try{
            element = driver.findElementByName(text);
            return element.isDisplayed();
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public IOSElement waitGetClickableElement(IOSElement element) throws TimeoutException {
        return (IOSElement) driverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public IOSElement waitGetVisibleElement(IOSElement element) throws TimeoutException {
        return (IOSElement) driverWait.until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitIsElementVisible(IOSElement element){
        try{
            driverWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        }catch (TimeoutException e){
            e.printStackTrace();
            return false;
        }
    }

    public void waitClickAndClearField(IOSElement element){
        element.click();
        element.clear();
    }

    public void waitClickEnterDataInField(IOSElement element, String data){
        element.click();
        element.sendKeys(data);
    }

    public void waitClickClearEnterDataInField(IOSElement element, String data){
        element.click();
        element.clear();
        element.sendKeys(data);
    }

    public void openScreen(){

    }

    public void waitScreenLoaded(){

    }

    public boolean waitIsScreenLoaded(){
        try {
            waitScreenLoaded ();
            return true;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void tapElement_iOS_XCTest(IOSElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, String> tapObject = new HashMap<String, String>();
        tapObject.put("x", String.valueOf(el.getSize().getWidth() / 2));
        tapObject.put("y", String.valueOf(el.getSize().getHeight() / 2));
        tapObject.put("element", el.getId());
        js.executeScript("mobile:tap", tapObject);
    }

    public void swipeToDirection_iOS_XCTest(IOSElement element, String direction) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, String> swipeObject = new HashMap<String, String>();
        switch (direction) {
            case "d":
                swipeObject.put("direction", "down");
                break;
            case "u":
                swipeObject.put("direction", "up");
                break;
            case "l":
                swipeObject.put("direction", "left");
                break;
            case "r":
                swipeObject.put("direction", "right");
                break;
        }
        swipeObject.put("element", element.getId());
        js.executeScript("mobile:swipe", swipeObject);
    }

    public void scrollToElementInDataPicker_iOS_XCTest(String text){
        driver.findElementByClassName("XCUIElementTypePickerWheel")
                .sendKeys(text);
    }

    public void swipeToElement_iOS_XCTest(IOSElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Map<String, Object> scrollObject = new HashMap<>();
        scrollObject.put("element", element.getId());
        js.executeScript("mobile: scroll", scrollObject);
    }

    public void touchAndHold(IOSElement element){
        Map<String, Object> args = new HashMap<>();
        args.put("element", element.getId());
        args.put("duration", 1);
        driver.executeScript("mobile: touchAndHold", args);
    }
}