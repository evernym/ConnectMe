package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.time.Duration;
import java.util.List;

public class MyCredentialsPageNew {
    AppiumDriver driver;

    public MyCredentialsPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
    }

    @AndroidFindBy(xpath = "//*[@text=\"My Credentials\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Credentials\"]")
    public WebElement myCredentialsHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"burger-menu\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"burger-menu\"]")
    public WebElement burgerMenuButton;

    public void expandCredentialBySchemeName(String credentialNameScheme) throws NoSuchElementException {
        // TODO: implement scroll-searching if needed
        List<WebElement> credentials = getCredentialsBySchemeName(credentialNameScheme);
        if (credentials.size() > 0) {
            credentials.get(0).click();
        } else {
            throw new NoSuchElementException("No credentials with scheme name " + credentialNameScheme + " found");
        }
        if (credentials.size() > 1) {
            credentials.get(0).click();
        }
    }

    public List<WebElement> getConnectionsBySchemeName(String schemeName) {
      String locator = Config.iOS_Devices.contains(Config.Device_Type)?
            "//*[@label='" + schemeName + "']" : "//*[@label='" + schemeName + "']";
      return (List<WebElement>)driver.findElementsByXPath(locator);
    }

    public List<WebElement> getCredentialsBySchemeName(String schemeName) {
        // TODO: implement scroll-search
        List<WebElement> credentials;
//        String credentialId = schemeName + "-title";
//        if (test.java.utility.Helpers.getPlatformType().equals(Platform.IOS)) {
//            credentials = driver.findElementsByAccessibilityId(credentialId);
//        } else {
//            String androidLocator = APP_PKG + ":id/" + credentialId;
//            credentials = driver.findElementsById(androidLocator);
//        }
        credentials = fetchCredentialsOnScreenBySchemeName(schemeName);
        if (credentials.size() == 0) {
            // test.java.appModules.AppUtils.pullScreenUp(driver);

            // ===== DIY scrolling
            System.out.println("Swipe up");
            Dimension windowSize = driver.manage().window().getSize();
            int anchor = windowSize.width/2;
            int startScrollHeight = windowSize.height / 2;
            int endScrollHeight = windowSize.height / 6;
            new TouchAction(driver)
                .longPress(PointOption.point(anchor, startScrollHeight))
                .moveTo(PointOption.point(anchor, endScrollHeight))
                .release()
                .perform();
            // ========

            credentials = fetchCredentialsOnScreenBySchemeName(schemeName);
        }
         return credentials;
    }

    private List<WebElement> fetchCredentialsOnScreenBySchemeName(String schemeName) {
        List<WebElement> credentials;
        String credentialId = schemeName + "-title";
        if (test.java.utility.Helpers.getPlatformType().equals(Platform.IOS)) {
            credentials = driver.findElementsByAccessibilityId(credentialId);
        } else {
//            String androidLocator = "me.connect" + ":id/" + credentialId; // I have no idea why it does not work
//            System.out.println(androidLocator);
//            credentials = driver.findElementsById(credentialId);
            credentials = driver.findElementsByXPath("//*[@text=\"" + schemeName + "\"]");
        }
        return credentials;
    }
}
