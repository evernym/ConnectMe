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

  @AndroidFindBy(xpath = "//*[@text=\"Proof Request\"]")
  @iOSXCUITFindBy(accessibility = "Proof Request")
  public WebElement proofRequestHeader;

  @AndroidFindBy(xpath = "//*[@text=\"Close\"]")
  @iOSXCUITFindBy(accessibility = "Close")
  public WebElement closeButton;

  @AndroidFindBy(xpath = "//*[@text='Missing - Tap to fix']")
  @iOSXCUITFindBy(accessibility = "Missing - Tap to fix")
  public WebElement missingAttributePlaceholder;

  @AndroidFindBy(xpath = "//*[@text=\"Missing Credentials\"]")
  @iOSXCUITFindBy(accessibility = "Missing Credentials")
  public WebElement missingCredentialsError;

  @AndroidFindBy(xpath = "//*[@content-desc=\"selected-credential-icon\"]")
  @iOSXCUITFindBy(accessibility = "selected-credential-icon")
  public WebElement selectedCredentialIcon;

  @AndroidFindBy(xpath = "//*[@content-desc=\"arrow-forward-icon\"]")
  @iOSXCUITFindBy(accessibility = "arrow-forward-icon")
  public WebElement arrowForwardIcon;

  @AndroidFindBy(xpath = "//*[@text=\"OK\"]")
  @iOSXCUITFindBy(accessibility = "OK")
  public WebElement okButton;

  @AndroidFindBy(xpath = "//*[@text=\"Not found\"]")
  @iOSXCUITFindBy(accessibility = "Not found")
  public WebElement notFoundError;

  @AndroidFindBy(xpath = "//*[@content-desc=\"alert-icon\"]")
  @iOSXCUITFindBy(accessibility = "alert-icon")
  public WebElement notFoundIcon;

//  public WebElement findParameterizedElement(String expression) {
//    if (Config.iOS_Devices.contains(Config.Device_Type)) {
//      return driver.findElementByAccessibilityId(expression);
//    } else {
//      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
//    }
//  }

  public WebElement unresolvedPredicateError(String text) {
    if (test.java.utility.Config.iOS_Devices.contains(test.java.utility.Config.Device_Type)) {
      return driver.findElementByAccessibilityId(text);
    } else {
      return driver.findElement(By.xpath("//*[@text='" + text + "']"));
    }
  }
}
