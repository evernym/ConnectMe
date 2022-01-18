package test.java.appModules;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import test.java.utility.Config;
import test.java.utility.AppDriver;
import test.java.utility.IntSetup;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.testng.SkipException;

/**
 * The AppUtlis class is to implement ConnectMe app utility methods
 */
public class AppUtils extends IntSetup {

    public static boolean Success = false;

    public void openApp(AppiumDriver driver) throws Exception {
        driver.launchApp();
        driver.context("NATIVE_APP"); // DEBUG
        passCodePageNew.passCodeTitle.isDisplayed();
        enterPincode(driver);
    }

    public void unlockApp(AppiumDriver driver) throws Exception {
        passCodePageNew.passCodeTitle.isDisplayed();
        enterPincode(driver);
    }

    /**
     * enters the pincode on pincode page
     *
     * @param driver - appium driver available for session
     * @return void
     */
    public void enterPincode(AppiumDriver driver) throws Exception {
        Thread.sleep(3000);  //  sync issue
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            passCodePageNew.passCodeTextBox.sendKeys("000000");
        } else {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            for (int i = 0; i < 6; i++) {
                androidDriver.pressKey(new KeyEvent(AndroidKey.DIGIT_0));
            }
        }
    }

    /**
     * To Check previous test case passed or not
     */
    public void checkSkip() {
        if (!AppUtils.Success) {
            throw new SkipException("Skipping this exception");  //  if previous test case ,skipping current test case
        }
        AppUtils.Success = false;
    }

    public interface Func {
        void call() throws Exception;
    }

    public static void DoSomethingEventually(Func... fns) {
        for (Func fn : fns) {
            try {
                fn.call();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    fn.call();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public static void DoSomethingEventuallyNew(Func... fns) {
        try {
            for (Func fn : fns) {
                fn.call();
            }
        } catch (Exception e) {
            System.out.println(">>> Exception is thrown 1st time:");
            System.out.println(e.getMessage());
            System.out.println(">>> Retrying...");
            try {
                for (Func fn : fns) {
                    fn.call();
                }
            } catch (Exception ex) {
                System.out.println(">>> Exception is thrown 2nd time:");
                System.out.println(ex.getMessage());
                System.out.println(">>> Retrying...");
                try {
                    for (Func fn : fns) {
                        fn.call();
                    }
                } catch (Exception exc) {
                    System.out.println(">>> Exception is thrown 3rd time:");
                    throw new RuntimeException(exc);
                }
            }
        }
    }

    // Alternative eventuality helper for more/less frequent polling
    public static void DoSomethingEventuallyNew(int timeout, Func... fns) {
        driverApp.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        try {
            for (Func fn : fns) {
                fn.call();
            }
        } catch (Exception e) {
            System.out.println(">>> Exception is thrown 1st time:");
            System.out.println(e.getMessage());
            System.out.println(">>> Retrying...");
            try {
                for (Func fn : fns) {
                    fn.call();
                }
            } catch (Exception ex) {
                System.out.println(">>> Exception is thrown 2nd time:");
                System.out.println(ex.getMessage());
                System.out.println(">>> Retrying...");
                try {
                    for (Func fn : fns) {
                        fn.call();
                    }
                } catch (Exception exc) {
                    System.out.println(">>> Exception is thrown 3rd time:");
                    throw new RuntimeException(exc);
                }
            }
        }
        driverApp.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
    }


    public static boolean isElementAbsent(AppiumDriver driver, Func getterFunction) {
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            getterFunction.call();
        } catch (Exception ex) {
            return true;
        } finally {
            driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
        }
        return false;
    }

    public static boolean isElementAbsent(AppiumDriver driver, WebElement el) {
        try {
            System.out.print("Contexts 5 >>> " + driverApp.getContextHandles());
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            el.isDisplayed();
        } catch (Exception ex) {
            System.out.print("Contexts 6 >>> " + driverApp.getContextHandles());
            return true;
        } finally {
            driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
        }
        return false;
    }

    public interface ElementChecker {
        WebElement check() throws Exception;
    }

    public static WebElement waitForElement(AppiumDriver driver, ElementChecker checker) {
        System.out.println("Wait for element to be available");
        driver.manage().timeouts().implicitlyWait(AppDriver.SMALL_TIMEOUT, TimeUnit.SECONDS);
        for (int i = 0; i < 5; i++) {
            try {
                return checker.check();
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Retry...");
                AppUtils.pullScreenDown(driver);
            } finally {
                driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
            }
        }
        throw new ElementNotFoundException("Expected element not found", "", "");
    }

    public static void waitForElementNew(AppiumDriver driver, WebElement element) throws Exception {
        System.out.println("Wait for element to be available");
        for (int i = 1; i < 6; i++) {
            driver.manage().timeouts().implicitlyWait(AppDriver.SMALL_TIMEOUT, TimeUnit.SECONDS);
            try {
                element.isDisplayed();
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Retry #" + i);
                pullScreenDown(driver);
            }
            finally {
                driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
            }
        }
        throw new RuntimeException("Expected element not found!");
    }

    public static void waitForElementNew(AppiumDriver driver, WebElement element, int timeout) throws Exception {
        System.out.println("Wait for element to be available");
        for (int i = 1; i < 4; i++) {
            driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
            try {
                element.isDisplayed();
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Retry #" + i);
                pullScreenDown(driver);
            }
            finally {
                driver.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
            }
        }
        throw new RuntimeException("Expected element not found!");
    }

    public static void pullScreenDown(AppiumDriver driver) {
        System.out.println("Pull screen down to refresh");
        Dimension dims = driver.manage().window().getSize();
        try {
            new TouchAction(driver)
                .press(new PointOption().withCoordinates(dims.width / 2 - 50, dims.height / 4))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(200)))
                .moveTo(new PointOption().withCoordinates(dims.width / 2 - 50, dims.height - 50))
                .release().perform();
        } catch (Exception e) {
            System.err.println("Pull screen down to refresh FAILED with Error:\n" + e.getMessage());
        }
    }

    public static void pullScreenUp(AppiumDriver driver) {
        System.out.println("Pull screen up");
        Dimension dims = driver.manage().window().getSize();
        try {
            new TouchAction(driver)
                .press(new PointOption().withCoordinates(dims.width / 2 - 50, dims.height / 2))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(200)))
                .moveTo(new PointOption().withCoordinates(dims.width / 2 - 50, dims.height / 6))
                .release().perform();
        } catch (Exception e) {
            System.err.println("Pull screen up FAILED with Error:\n" + e.getMessage());
        }
    }

//  public static void swipe(AppiumDriver driver, String direction) {
//    Dimension dims = driver.manage().window().getSize();
//    int[] centerPoint = {dims.width / 2 - 50, dims.height / 2};
//    int[] topPoint = {dims.width / 2 - 50, dims.height / 10};
//    int[] bottomPoint = {dims.width / 2 - 50, dims.height - 20};
//    int[] rightPoint = {dims.width - 20, dims.height / 2};
//    int[] leftPoint = {dims.width / 10, dims.height / 2};
//  }

    public void acceptCredential() throws Exception {
        credentialPageNew.acceptButton.click();
        authForAction();
    }

    public void rejectCredential() throws Exception {
        credentialPageNew.rejectButton.click();
        authForAction();
    }

    public void shareProof() throws Exception {
        proofRequestPageNew.shareButton.click();
        authForAction();
    }

    public void rejectProof() throws Exception {
        proofRequestPageNew.rejectButton.click();
        authForAction();
    }

    public void authForAction() throws Exception {
        try {
            driverApp.manage().timeouts().implicitlyWait(AppDriver.SMALL_TIMEOUT, TimeUnit.SECONDS);
            passCodePageNew.passCodeTitle.isDisplayed();
            passCodePageNew.enterPassCode();
        } catch (NoSuchElementException e) {
            System.out.println("No authentication is required");
        } finally {
            driverApp.manage().timeouts().implicitlyWait(AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    public WebElement findParameterizedElement(String expression) {
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            return driverApp.findElementByAccessibilityId(expression);
        } else {
            return driverApp.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
        }
    }

    // Additional method to handle some iOS updates - it's not safe to update everywhere
    public WebElement findParameterizedElementAlt(String expression) {
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            return driverApp.findElementByXPath("//*[@label='" + expression + "']");
        } else {
            return driverApp.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
        }
    }
}
