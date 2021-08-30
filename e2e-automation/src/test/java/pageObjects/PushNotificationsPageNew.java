package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class PushNotificationsPageNew {
  AppiumDriver driver;

  public PushNotificationsPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @iOSXCUITFindBy(accessibility = "allow-notifications-button") // id?
  public WebElement allowButton;

  @iOSXCUITFindBy(accessibility = "not-now-notifications-button") // id?
  public WebElement notNowButton;

  @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Allow\"]")
  @AndroidFindBy(xpath = "//*[@text='While using the app']")
  public WebElement okButton;
}
