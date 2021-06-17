package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ChatPageNew {
  AppiumDriver driver;

  public ChatPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Back\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Close\"]")
  public WebElement backArrow;

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Cancel\"]")
  public WebElement backArrowAlt;
}
