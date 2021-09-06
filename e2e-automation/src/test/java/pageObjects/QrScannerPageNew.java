package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.appModules.AppUtils;

import java.util.concurrent.TimeUnit;

public class QrScannerPageNew {
    AppiumDriver driver;

    public QrScannerPageNew(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(xpath = "//*[@text=\"ALLOW\"]")
    @AndroidFindBy(xpath = "//*[@text=\"Allow\"]")
    @AndroidFindBy(xpath = "//*[@text=\"allow\"]")
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_foreground_only_button")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
    public WebElement scannerAllowButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"close-qr-scanner-icon\"]")
    @iOSXCUITFindBy(accessibility = "close-qr-scanner-icon") // id?
    public WebElement scannerCloseButton;

    public void getCameraPermissions() {
        try {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            if (!AppUtils.isElementAbsent(driver, scannerAllowButton)) {
                scannerAllowButton.click();
            } else {
                System.out.println("Permissions already have been granted!");
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(test.java.utility.AppDriver.LARGE_TIMEOUT, TimeUnit.SECONDS);
        }
    }
}
