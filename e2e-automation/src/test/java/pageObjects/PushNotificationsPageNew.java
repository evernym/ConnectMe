package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
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

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Allow\"]")
  public WebElement okButton;
}
