package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SettingsPageNew {
  AppiumDriver driver;

  public SettingsPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"settings-container\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"settings-container\"]")
  public WebElement settingsContainer;

  @AndroidFindBy(xpath = "//*[@text=\"Settings\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Settings\"]")
  public WebElement settingsHeader;

  @AndroidFindBy(xpath = "//*[@text=\"Biometrics\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Biometrics Use your finger or face to secure app\"]")
  public WebElement biometricsButton;

  @AndroidFindBy(xpath = "//*[@text=\"Passcode\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Passcode Change your Connect.Me passcode\"]")
  public WebElement passCodeButton;

  @AndroidFindBy(xpath = "//*[@text=\"Give app feedback\"]/parent::*")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Give app feedback Tell us what you think of Connect.Me\"]")
  public WebElement chatButton;

  @AndroidFindBy(xpath = "//*[@text=\"About\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"About Legal, Version, and Network Information\"]")
  public WebElement aboutButton;
}
