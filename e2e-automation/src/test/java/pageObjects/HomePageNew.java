package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomePageNew {
  AppiumDriver driver;

  public HomePageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Home\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Home\"]")
  public WebElement homeHeader;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"burger-menu\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"burger-menu\"]")
  public WebElement burgerMenuButton;

  @AndroidFindBy(xpath = "//*[@text=\"Scan\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Scan\"]")
  public WebElement scanButton;
}
