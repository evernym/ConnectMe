package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AboutPageNew {
  AppiumDriver driver;

  public AboutPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"left-icon\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"left-icon\"]")
  public WebElement backArrow;
}
