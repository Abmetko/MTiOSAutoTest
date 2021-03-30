package app.frontend.screens;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class StartScreen extends BaseScreen {

    @iOSXCUITFindBy(id = "Login")
    public IOSElement Login;

    @iOSXCUITFindBy(xpath = "//*[@class='UIAImage' and ./parent::*[(./preceding-sibling::* | ./following-sibling::*)[@text='loginBg']]]")
    public IOSElement TradeATF;

    @iOSXCUITFindBy(id = "Sign up")
    public IOSElement Sign_up;

    @iOSXCUITFindBy(xpath = "//*[@text='WE DO NOT PROVIDE SERVICES IN YOUR COUNTRY' and @class='UIAStaticText']")
    public IOSElement WE_DO_NOT_PROVIDE;

    public StartScreen(IOSDriver<IOSElement> driver){
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver),this);
    }

    @Override
    public void waitScreenLoaded(){
        try {
            driverWait.until(ExpectedConditions.elementToBeClickable(Login));
        }catch (TimeoutException e)  {
            throw new TimeoutException();
        }
    }

    public boolean clickLoginButton(){
        try {
            waitGetClickableElement(Login).click();
            return true;
        }catch (WebDriverException e){
            return false;
        }
    }
}