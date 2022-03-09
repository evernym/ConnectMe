package pageObjects;

import org.openqa.selenium.StaleElementReferenceException;
import test.java.appModules.AppUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;
import java.util.concurrent.TimeUnit;

public class MyConnectionsPageNew {
    AppiumDriver driver;

    public MyConnectionsPageNew(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    @AndroidFindBy(xpath = "//*[@text=\"My Connections\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Connections\"]")
    public WebElement myConnectionsHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"burger-menu\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"burger-menu\"]")
    public WebElement burgerMenuButton;

    public WebElement getConnectionByName(String name) {
        try {
            // TODO: decrease time parameter
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            if (Config.iOS_Devices.contains(Config.Device_Type)) {
                return driver.findElementByAccessibilityId(name + "-title");
            } else {
                return driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"" + name + "-title\"]"));
            }
        } catch (NoSuchElementException e) {
            System.out.println("There is no connection with the name: " + name);
            return null;
        }
        finally {
            driver.manage().timeouts().implicitlyWait(test.java.utility.AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
        }
    }

    public void drillDownConnection(String name) {
        try {
            getConnectionByName(name).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
        catch (StaleElementReferenceException e) {}
        if (!AppUtils.isElementAbsent(driver, myConnectionsHeader)) {
            System.out.println("Failed to drill down connection, retrying");
            try { Thread.sleep(1000); }
            catch (InterruptedException e) {}
            getConnectionByName(name).click();
        }
        System.out.println("Connection history should be visible now");
    }
}
