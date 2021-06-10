package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class QuestionPageNew {
  AppiumDriver driver;

  public QuestionPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Question\"]")
  @iOSXCUITFindBy(accessibility = "Question")
  public WebElement header;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"question-sender-logo\"]")
  @iOSXCUITFindBy(accessibility = "question-sender-logo")
  public WebElement senderLogo;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"default-logo\"]")
  @iOSXCUITFindBy(accessibility = "default-logo")
  public WebElement senderDefaultLogo;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"question-action-submit\"]")
  @iOSXCUITFindBy(accessibility = "question-action-submit")
  public WebElement submitButton;

  public WebElement findParameterizedElement(String expression) {
    if (test.java.utility.Config.iOS_Devices.contains(test.java.utility.Config.Device_Type)) {
      return driver.findElementByAccessibilityId(expression);
    } else {
      return driver.findElement(By.xpath("//*[@text=\"" + expression + "\"]"));
    }
  }
}
