package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ConnectionDetailPageNew {
  AppiumDriver driver;

  public ConnectionDetailPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Cancel\"]")
  @iOSXCUITFindBy(accessibility = "Cancel")
  public WebElement closeButton;

  @AndroidFindBy(xpath = "//*[@text=\"Delete Connection\"]")
  @iOSXCUITFindBy(accessibility = "Delete Connection")
  public WebElement deleteButton;
}
