import core.application_management.ManageApp;
import core.screen_driver.DriverFactory;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.*;


public class BaseTest {

    protected IOSDriver<IOSElement> driver;
    protected Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    @Parameters({ "app_url","app_args","run_type" })
    public void beforeSuite(String app_url, String app_args, String run_type){
        DriverFactory.app_url = app_url;
        DriverFactory.run_type = run_type;
        DriverFactory.project_name = app_args.split(",")[1];
        DriverFactory.package_name = app_args.split(",")[0];
    }

    @BeforeClass(alwaysRun=true)
    public void beforeClass(){
        driver = DriverFactory.getDriver();;
    }

    @AfterClass(alwaysRun=true)
    public void afterClass(){
        ManageApp.resetApp(driver);
    }

    @AfterSuite(alwaysRun=true)
    public void afterSuite(){
        ManageApp.driverQuit(driver);
    }
}