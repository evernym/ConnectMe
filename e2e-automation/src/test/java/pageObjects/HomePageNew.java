package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

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

  @AndroidFindBy(xpath = "//*[@text=\"Recent\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Recent\"]")
  public WebElement recentEventsSection;

  @AndroidFindBy(xpath = "//*[@text='You connected with \"connection-invitation\".']")
  @iOSXCUITFindBy(accessibility = "You connected with \"connection-invitation\".") // id?
  public WebElement commonConnectedEvent;

  @AndroidFindBy(xpath = "//*[@text='You connected with \"out-of-band-invitation\".']")
  @iOSXCUITFindBy(accessibility = "You connected with \"out-of-band-invitation\".") // id?
  public WebElement oobConnectedEvent;

  @AndroidFindBy(xpath = "//*[@text='You connected with \"push-connection-invitation\".']")
  @iOSXCUITFindBy(accessibility = "You connected with \"push-connection-invitation\".") // id?
  public WebElement pushConnectedEvent;

  @AndroidFindBy(xpath = "//*[@text=\"Remote connection sent you a Credential Offer\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Remote connection sent you a Credential Offer\"]")
  public WebElement credentialOfferNotification;

  @AndroidFindBy(xpath = "//*[@text=\"Remote connection sent you a Proof Request\"]")
  @iOSXCUITFindBy(xpath = "//*[@text=\"Remote connection sent you a Proof Request\"]")
  public WebElement proofRequestNotification;

  @AndroidFindBy(xpath = "//*[@text=\"Remote connection sent you a Question\"]")
  @iOSXCUITFindBy(xpath = "//*[@text=\"Remote connection sent you a Question\"]")
  public WebElement questionNotification;

  @AndroidFindBy(xpath = "//*[@text=\"NEW MESSAGE - TAP TO OPEN\"]")
  @iOSXCUITFindBy(accessibility = "new-message")
  public WebElement newMessage;

  public WebElement credentialIssuedEvent(String credentialName) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You have been issued a \"" + credentialName + "\".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You have been issued a \"" + credentialName + "\".']"));
    }
  }

  public WebElement credentialRejectedEvent(String credentialName) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You rejected \"" + credentialName + "\".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You rejected \"" + credentialName + "\".']"));
    }
  }

  public WebElement proofSharedEvent(String proofName) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You shared \"" + proofName + "\".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You shared \"" + proofName + "\".']"));
    }
  }

  public WebElement proofRequestRejectedEvent(String proofName) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You rejected \"" + proofName + "\".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You rejected \"" + proofName + "\".']"));
    }
  }

  public WebElement questionRespondedEvent(String answer) {
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You responded with: " + answer + ".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You responded with: " + answer + ".']"));
    }
  }

  public WebElement namedConnectionEvent(String connectionName){
    if (Config.iOS_Devices.contains(Config.Device_Type)) {
      return driver.findElementByAccessibilityId("You connected with \"" + connectionName + "\".");
    } else {
      return driver.findElement(By.xpath("//*[@text='You connected with \"" + connectionName + "\".']"));
    }
  }
}
