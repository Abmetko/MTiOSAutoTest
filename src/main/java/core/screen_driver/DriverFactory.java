package core.screen_driver;

import core.utils.APIClient;
import core.utils.PropertyLoader;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class DriverFactory {

    private static AppiumDriverLocalService SERVICE = null;

    protected static IOSDriver<IOSElement> driver = null;

    private static final String appiumInstallationDir = "C:/Program Files";

    private static final String appiumNode = appiumInstallationDir + File.separator + "nodejs" + File.separator + "node.exe";

    private static final String appiumNodeModule = "C:/Users/abmetko/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";

    private static final String APP_DIR = "src/main/resources";

    private static final String appName = PropertyLoader.getProperty("file.name");

    private static final String APP_FILE_ABSOLUTE_PATH = getAppFilePath();

    public static String APP_URL;

    public static String APP_ARGS;

    public static String RUN_TYPE;

    private DriverFactory(){

    }

    public static IOSDriver<IOSElement> getDriver() {
        if (driver != null) {
            return driver;
        }
        configureSessionForRealDevice();
        return driver;
    }

    private static void configureSessionForRealDevice() {
        if(RUN_TYPE.equals("r")){
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
            networkLogsOptions.put("captureContent", true);
            capabilities.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
            capabilities.setCapability("app", APP_URL);
            capabilities.setCapability("browserstack.user", PropertyLoader.getProperty("browserstack.login"));
            capabilities.setCapability("browserstack.key", PropertyLoader.getProperty("browserstack.password"));
            capabilities.setCapability("device", PropertyLoader.getProperty("device.name"));
            capabilities.setCapability("os_version", PropertyLoader.getProperty("device.os"));
            capabilities.setCapability("project", APP_ARGS.split(",")[1]);
            capabilities.setCapability("build", "Build: " + APIClient.getApplicationData(APP_URL));
            capabilities.setCapability("name", "smoke test");
            capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
            capabilities.setCapability("newCommandTimeout", 120000);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("browserstack.networkLogs","true");
            capabilities.setCapability("browserstack.appium_version", "1.19.1");
            capabilities.setCapability("browserstack.idleTimeout", 60);//0 to 300 seconds (Default: 90 seconds)
            capabilities.setCapability("disableAnimations", "true");
            capabilities.setCapability("browserstack.debug","true");
            capabilities.setCapability("browserstack.networkLogs", "true");
            try {
                driver = new IOSDriver<IOSElement>(new URL("http://hub-cloud.browserstack.com/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }else if(RUN_TYPE.equals("l")){
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 10");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.7");
            capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, PropertyLoader.getProperty("app.package"));
            capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
            capabilities.setCapability("udid", "eeb9f5518160cb4b53d439119c39bd5a16f2b262");
            capabilities.setCapability("xcodeOrgId", "3NQ62UFLS8");
            capabilities.setCapability("xcodeSigningId", "iPhone Developer");
            capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
            capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
            capabilities.setCapability("newCommandTimeout", 100000);
            capabilities.setCapability("isHeadless", false);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            capabilities.setCapability("automationName", "XCUITest");
            capabilities.setCapability("autoAcceptAlerts", true);
            capabilities.setCapability("settings[waitForIdleTimeout]", 0.5);
            try {
                driver = new IOSDriver<IOSElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }

    public static void runAppiumServer() {
        SERVICE = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File(appiumNode))
                .withAppiumJS(new File(appiumNodeModule))
                .withIPAddress("127.0.0.1").usingPort(4723));
        SERVICE.start();
    }

    public static void stopAppiumServer() {
        SERVICE.stop();
    }

    private static String getAppFilePath() {
        File appDir = new File(APP_DIR);
        return new File(appDir, appName).getAbsolutePath();
    }
}