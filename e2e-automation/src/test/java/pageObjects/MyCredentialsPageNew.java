package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

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

  @AndroidFindBy(xpath = "//*[@text=\"\"]") // TODO
  @iOSXCUITFindBy(accessibility = "") // id? // TODO
  public WebElement testCredential;

}
