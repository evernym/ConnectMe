package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.appModules.AppUtils;
import test.java.utility.Config;
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
        List<WebElement> credentials = getCredentialsBySchemeName(credentialNameScheme);
        // TODO: make a better scroll-search implementation
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
        // TODO: implement a better way to scroll-search
        List<WebElement> credentials;
        credentials = fetchCredentialsOnScreenBySchemeName(schemeName);
        if (credentials.size() == 0) {
            // ===== AppUtils pullScreenUp method opens a credential in the middle of the screen instead of swiping
            AppUtils.swipeVerticallyByScreenHeightPercent(50, 15);
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
//            credentials = driver.findElementsById(androidLocator);
            credentials = driver.findElementsByXPath("//*[@text=\"" + schemeName + "\"]");
        }
        return credentials;
    }
}
