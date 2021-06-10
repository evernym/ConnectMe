package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class MyConnectionsPageNew {
  AppiumDriver driver;

  public MyConnectionsPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"My Connections\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Connections\"]")
  public WebElement myConnectionsHeader;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"burger-menu\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"burger-menu\"]")
  public WebElement burgerMenuButton;

  @AndroidFindBy(xpath = "//*[@text=\"connection-invitation\"]")
  @iOSXCUITFindBy(accessibility = "connection-invitation-title") // id?
  public WebElement commonTestConnection;

  @AndroidFindBy(xpath = "//*[@text=\"out-of-band-invitation\"]")
  @iOSXCUITFindBy(accessibility = "out-of-band-invitation-title") // id?
  public WebElement oobTestConnection;
}
