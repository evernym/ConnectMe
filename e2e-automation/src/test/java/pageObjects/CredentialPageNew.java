package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

public class CredentialPageNew {
  AppiumDriver driver;

  public CredentialPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Accept Credential\"]")
  @iOSXCUITFindBy(accessibility = "Accept Credential")
  public WebElement acceptButton;

  @AndroidFindBy(xpath = "//*[@text=\"Reject\"]")
  @iOSXCUITFindBy(accessibility = "Reject")
  public WebElement rejectButton;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='sender-avatar-image']")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='sender-avatar']")
  public WebElement credentialSenderLogo;

  @AndroidFindBy(xpath = "//*[@text=\"Credential Offer\"]")
  @iOSXCUITFindBy(accessibility = "Credential Offer")
  public WebElement credentialOfferHeader;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"back-arrow\"]")
  @iOSXCUITFindBy(accessibility = "back-arrow")
  public WebElement backArrow;

  @AndroidFindBy(xpath = "//*[@text=\"Close\"]")
  @iOSXCUITFindBy(accessibility = "Close")
  public WebElement closeButton;

  @AndroidFindBy(xpath = "//*[@text=\"Delete Credential\"]")
  @iOSXCUITFindBy(accessibility = "Delete Credential")
  public WebElement deleteButton;

  @AndroidFindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/" +
    "android.view.ViewGroup[2]/android.widget.TextView")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[1]/XCUIElementTypeOther[3]/XCUIElementTypeStaticText")
  public WebElement credentialSchemeName;
}
