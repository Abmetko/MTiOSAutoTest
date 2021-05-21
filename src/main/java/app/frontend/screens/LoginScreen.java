package app.frontend.screens;

import core.screen_driver.DriverFactory;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.Arrays;
import static core.mt.ProjectPackages.*;
import static core.mt.utils.PropertyLoader.getProperty;


public class LoginScreen extends BaseScreen {

    public @iOSXCUITFindBy(id = "Login Page")
    IOSElement Login_Page;

    public @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[./preceding-sibling::* | ./following-sibling::*[@id='Email']]")
    IOSElement Email;

    public @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[./preceding-sibling::* | ./following-sibling::*[@id='Password']]")
    IOSElement Password;

    public @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Login']")
    IOSElement Login;

    public @iOSXCUITFindBy(id = "Forgot password?")
    IOSElement Forgot_password;


    public LoginScreen(IOSDriver<IOSElement> driver){
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver),this);
    }

    @Override
    public void waitScreenLoaded(){
        try {
            driverWait.until(ExpectedConditions.visibilityOf(Forgot_password));
        }catch (TimeoutException e)  {
            throw new TimeoutException();
        }
    }

    @Override
    public void openScreen(){
        StartScreen startScreen = new StartScreen(driver);
        startScreen.waitGetClickableElement(startScreen.Login).click();
        waitScreenLoaded();
    }

    public boolean makeLogin(){
        try{
            String packageName = DriverFactory.package_name;
            String userEmail = getProperty("user.email");
            String userPassword = getProperty("user.password");
            if(HFT_TRADING_AU.getValueAsList().contains(packageName)){
                userPassword = getProperty("user.password.hft");
            }
            else if(HFT_TRADING.getValueAsList().contains(packageName)){
                userEmail = getProperty("user.email.hft");
                userPassword = getProperty("user.password.hft");
            }
            waitClickEnterDataInField(waitGetClickableElement(Email), userEmail);
            logger.debug("Input email: " + userEmail);
            waitClickEnterDataInField(waitGetClickableElement(Password), userPassword);
            logger.debug("Input password: " + userPassword);
            waitGetClickableElement(Login).click();
            return true;
        }catch (WebDriverException e){
            return false;
        }
    }
}