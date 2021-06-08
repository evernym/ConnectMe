package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class MenuPageNew {
  AppiumDriver driver;

  public MenuPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Home\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Home\"]")
  public WebElement homeButton;

  @AndroidFindBy(xpath = "//*[@text=\"My Connections\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Connections\"]")
  public WebElement myConnectionsButton;

  @AndroidFindBy(xpath = "//*[@text=\"My Credentials\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Credentials\"]")
  public WebElement myCredentialsButton;

  @AndroidFindBy(xpath = "//*[@text=\"Settings\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Settings\"]")
  public WebElement settingsButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"menu-container\"]")
  @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name=\"menu-container\"])[2]")
  public WebElement menuContainer;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"menu-container\"]/android.view.ViewGroup[1]")
  @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name=\"user-avatar\"])[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther")
  public WebElement banner;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"connect-me-logo\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"connect-me-logo\"]")
  public WebElement logo;

  @AndroidFindBy(xpath = "//*[contains(@text, \"built by\")]")
  @iOSXCUITFindBy(xpath = "//*[contains(@name, \"built by\")]")
  public WebElement builtByFooter;

  @AndroidFindBy(xpath = "//*[contains(@text, \"Version\")]")
  @iOSXCUITFindBy(xpath = "//*[contains(@name, \"Version\")]")
  public WebElement versionFooter;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"user-avatar\"]")
  @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name=\"user-avatar\"])[2]")
  public WebElement userAvatar;

  @AndroidFindBy(xpath = "//*[@text=\"OK\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
  public WebElement okButton;

  @AndroidFindBy(xpath = "//*[@text=\"ALLOW\"]") // allow | ALLOW
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
  public WebElement menuAllowButton;

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Allow Access to All Photos\"]")
  public WebElement menuAllowButtonAlt;

  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Cancel\"]")
  public WebElement cancelButton;
}
