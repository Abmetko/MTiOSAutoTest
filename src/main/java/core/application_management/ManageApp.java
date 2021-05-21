package core.application_management;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import java.time.Duration;
import static core.mt.utils.PropertyLoader.getProperty;


public class ManageApp {

    public static void resetApp(AppiumDriver<IOSElement> driver){
        driver.resetApp();
    }

    public static void closeApp(AppiumDriver<IOSElement> driver){
        driver.closeApp();
    }

    public static void launchApp(AppiumDriver<IOSElement> driver){
        driver.launchApp();
    }

    public static void driverQuit(AppiumDriver<IOSElement> driver){
        driver.quit();
    }

    public static void removeApp(AppiumDriver<IOSElement> driver){
        driver.removeApp(getProperty("app.package"));
    }

    public static void runAppInBackgroundAndRelaunch(AppiumDriver<IOSElement> driver){
        driver.runAppInBackground(Duration.ofSeconds(1));
    }
}