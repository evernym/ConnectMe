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

//  @HowToUseLocators(iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
//  @iOSXCUITFindBy(accessibility = "allow-notifications-button") // label
  @iOSXCUITFindBy(id = "allow-notifications-button") // id
//  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Allow Push Notifications\"]") // debug
//  @iOSXCUITFindBy(xpath = "//*[@text=\"Allow Push Notifications\"]") // debug
  public WebElement allowButton;

  @iOSXCUITFindBy(accessibility = "not-now-notifications-button") // id?
  public WebElement notNowButton;

  @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Allow\"]")
  @AndroidFindBy(xpath = "//*[@text='While using the app']")
  public WebElement okButton;
}
