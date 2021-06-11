package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

public class ProofRequestPageNew {
  AppiumDriver driver;

  public ProofRequestPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Share Attributes\"]")
  @iOSXCUITFindBy(accessibility = "Share Attributes")
  public WebElement shareButton;

  @AndroidFindBy(xpath = "//*[@text=\"Reject\"]")
  @iOSXCUITFindBy(accessibility = "Reject")
  public WebElement rejectButton;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"sender-avatar-image\"]")
  @iOSXCUITFindBy(accessibility = "sender-avatar")
  public WebElement proofRequestSenderLogo;

  @AndroidFindBy(xpath = "//*[@text=\"Close\"]")
  @iOSXCUITFindBy(accessibility = "Close")
  public WebElement closeButton;

  public WebElement findParameterizedElement(String expression) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId(expression);
    } else {
      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
    }
  }
}
