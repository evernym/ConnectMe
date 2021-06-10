package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

public class PassCodePageNew {
  AppiumDriver driver;

  public PassCodePageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE, iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc=\"Enter passcode\"]")
  @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc=\"Enter your passcode\"]")
  @iOSXCUITFindBy(accessibility = "Enter passcode") // id?
  @iOSXCUITFindBy(accessibility = "Enter your passcode") // id?
  public WebElement passCodeTitle;

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"pin-code-digit-0-touchable pin-code-digit-1-touchable pin-code-digit-2-touchable pin-code-digit-3-touchable pin-code-digit-4-touchable pin-code-digit-5-touchable\"]")
  public WebElement passCodeTextBox;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"left-icon\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"left-icon\"]")
  public WebElement backArrow;

  public void enterPassCode() throws Exception {
    Thread.sleep(2000);
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      passCodeTextBox.sendKeys("000000");
    } else {
      AndroidDriver androidDriver = (AndroidDriver) driver;
      for (int i = 0; i < 6; i++) {
        androidDriver.pressKey(new KeyEvent(AndroidKey.DIGIT_0));
      }
    }
  }

  public void openApp() throws Exception {
    driver.launchApp();
    driver.context("NATIVE_APP"); // make sure that main app is active
    passCodeTitle.isDisplayed();
    enterPassCode();
  }
}
