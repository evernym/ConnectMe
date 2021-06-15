package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class QrScannerPageNew {
  AppiumDriver driver;

  public QrScannerPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
  @AndroidFindBy(xpath = "//*[@text=\"ALLOW\"]")
  @AndroidFindBy(xpath = "//*[@text=\"Allow\"]")
  @AndroidFindBy(xpath = "//*[@text=\"allow\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
  public WebElement scannerAllowButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"close-qr-scanner-icon\"]")
  @iOSXCUITFindBy(accessibility = "close-qr-scanner-icon") // id?
  public WebElement scannerCloseButton;
}
