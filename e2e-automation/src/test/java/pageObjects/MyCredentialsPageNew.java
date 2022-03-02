package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
        String locator = schemeName + "-title";
        if (test.java.utility.Helpers.getPlatformType().equals(Platform.IOS)) {
            credentials = driver.findElementsByAccessibilityId(locator);
        } else {
            credentials = driver.findElementsById(locator);
        }
         return credentials;
    }
}
