package core.application_management;

import app.frontend.screens.BaseScreen;
import app.frontend.screens.LoginScreen;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static core.utils.PropertyLoader.getProperty;
import static org.testng.AssertJUnit.assertTrue;


public class ManageApp {

    private IOSDriver<IOSElement> driver;

    public ManageApp(IOSDriver<IOSElement> driver){
        this.driver = driver;
    }

    public void resetApp(){
        driver.resetApp();
    }

    public void closeApp(){
        driver.closeApp();
    }

    public void launchApp(){
        driver.launchApp();
    }

    public void driverQuit(){
        driver.quit();
    }

    public void removeApp(){
        driver.removeApp(getProperty("app.package"));
    }

    public void runAppInBackgroundAndRelaunch(){
        driver.runAppInBackground(Duration.ofSeconds(1));
    }

}