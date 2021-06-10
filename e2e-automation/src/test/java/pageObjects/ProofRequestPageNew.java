package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

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

  public WebElement findParameterizedElement(String expression) {
    if (test.java.utility.Config.iOS_Devices.contains(test.java.utility.Config.Device_Type)) {
      return driver.findElementByAccessibilityId(expression);
    } else {
      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
    }
  }
}
