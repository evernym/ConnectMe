package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

public class MyCredentialsPageNew {
  AppiumDriver driver;

  public MyCredentialsPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"My Credentials\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"My Credentials\"]")
  public WebElement myCredentialsHeader;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"burger-menu\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"burger-menu\"]")
  public WebElement burgerMenuButton;

//  public WebElement findParameterizedElement(String expression) {
//    if (Config.iOS_Devices.contains(Config.Device_Type)) {
//      return driver.findElementByAccessibilityId(expression);
//    } else {
//      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
//    }
//  }
}
