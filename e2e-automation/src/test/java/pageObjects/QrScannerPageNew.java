package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class QrScannerPageNew {
  AppiumDriver driver;

  public QrScannerPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"ALLOW\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
  public WebElement scannerAllowButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"close-qr-scanner-icon\"]")
  @iOSXCUITFindBy(accessibility = "close-qr-scanner-icon") // id?
  public WebElement scannerCloseButton;
}
