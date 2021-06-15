package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

public class ConnectionHistoryPageNew {
  AppiumDriver driver;

  public ConnectionHistoryPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"connection-history-icon-close\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"connection-history-icon-close-touchable\"]")
  public WebElement closeButton;

  @AndroidFindBy(xpath = "//*[contains(@name, \"RECEIVED\")]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[contains(@name, \"RECEIVED\")]")
  public WebElement receivedStatus;

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"connection-history-icon-delete-touchable\"]")
  public WebElement deleteIcon; // do we need it?

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Delete\"]")
  public WebElement deleteButton; // do we need it?

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[contains(@name, \"SHARED ProofTestAuto\")]")
  public WebElement sharedButton; // do we need it?

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeText[@name=\"custom-list-data-0\"]")
  public WebElement attributeText; // do we need it?

  @AndroidFindBy(xpath = "//*[@text=\"Added Connection\"]")
  @iOSXCUITFindBy(accessibility = "Added Connection") // id?
  public WebElement connectedRecord;

  @AndroidFindBy(xpath = "//*[@text='You added out-of-band-invitation as a Connection']")
  @iOSXCUITFindBy(accessibility = "Added Connection") // id?
  public WebElement oobConnectedRecordDescription;

  @AndroidFindBy(xpath = "//*[@text=\"YOU ANSWERED\"]")
  @iOSXCUITFindBy(accessibility = "YOU ANSWERED") // id?
  public WebElement questionAnswerRecord;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"back-arrow\"]")
  @iOSXCUITFindBy(accessibility = "back-arrow-touchable") // id?
  public WebElement backButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"logo\"]")
  @iOSXCUITFindBy(accessibility = "logo") // id?
  public WebElement connectionLogo;

  @AndroidFindBy(xpath = "//*[@text=\"out-of-band-invitation\"]")
  @iOSXCUITFindBy(accessibility = "out-of-band-invitation")
  public WebElement oobConnectionName;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"three-dots\"]")
  @iOSXCUITFindBy(accessibility = "three-dots")
  public WebElement threeDotsButton;

  @AndroidFindBy(xpath = "//*[@text=\"YOU REJECTED\"]")
  @iOSXCUITFindBy(accessibility = "YOU REJECTED")
  public WebElement rejectedProofRequestRecord;

  @AndroidFindBy(xpath = "//*[@text=\"VIEW REQUEST DETAILS\"]")
  @iOSXCUITFindBy(accessibility = "VIEW REQUEST DETAILS")
  public WebElement viewProofRequestDetailsButton;

  @AndroidFindBy(xpath = "//*[@text=\"ACCEPTED CREDENTIAL\"]")
  @iOSXCUITFindBy(accessibility = "ACCEPTED CREDENTIAL")
  public WebElement acceptedCredentialRecord;

  @AndroidFindBy(xpath = "//*[@text=\"VIEW CREDENTIAL\"]")
  @iOSXCUITFindBy(accessibility = "VIEW CREDENTIAL")
  public WebElement acceptedCredentialViewButton;

  @AndroidFindBy(xpath = "//*[@text=\"YOU REJECTED\"]")
  @iOSXCUITFindBy(accessibility = "YOU REJECTED")
  public WebElement rejectedCredentialRecord;

  public WebElement findParameterizedElement(String expression) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId(expression);
    } else {
      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
    }
  }

  public WebElement questionAnswerRecordDescription(String answer) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("\"" + answer + "\"");
    } else {
      return driver.findElement(By.xpath("//*[@text='\"" + answer + "\"']"));
    }
  }
}
