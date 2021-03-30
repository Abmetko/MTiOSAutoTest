package core.utils;

import core.screen_driver.DriverFactory;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Listener implements ITestListener {

    private static String SESSION_STATUS = "UNMARKED";

    @Override
    public void onFinish(ITestContext Result) {

    }

    @Override
    public void onStart(ITestContext Result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult Result) {

    }

    @Override
    public void onTestFailure(ITestResult Result) {
        IOSDriver<IOSElement> driver = DriverFactory.getDriver();
        File file  = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File fileDir = new File("src/main/resources/");
        File fileAbsolutePath = new File(new File(fileDir, Result.getName() + "-" + Result.getEndMillis() + ".jpg").getAbsolutePath());
        try {
            FileUtils.copyFile(file, fileAbsolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String errorTrace = "error";
        if (null != Result.getThrowable()) {
            errorTrace = Result.getThrowable().getMessage();
        }
        if(!DriverFactory.RUN_TYPE.equals("l")){
            JavascriptExecutor jse = (JavascriptExecutor)DriverFactory.getDriver();
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Test FAILED: " + errorTrace + "\"}}");
            SESSION_STATUS = "FAILED";
        }
    }

    @Override
    public void onTestSkipped(ITestResult Result) {

    }

    @Override
    public void onTestStart(ITestResult Result) {

    }

    @Override
    public void onTestSuccess(ITestResult Result) {
        if(SESSION_STATUS.equals("UNMARKED")&!DriverFactory.RUN_TYPE.equals("l")){
            JavascriptExecutor jse = (JavascriptExecutor)DriverFactory.getDriver();
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Test PASSED\"}}");
            SESSION_STATUS = "PASSED";
        }
    }

    public static class DateClass{
        public static String transformTime(long millis){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(millis);
            return new SimpleDateFormat("HH:mm:ss:SSS").format(cal.getTime());
        }
    }
}