package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CustomValuesPageNew {
  AppiumDriver driver;

  public CustomValuesPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text='Custom Values']")
  @iOSXCUITFindBy(accessibility = "Custom Values")
  public WebElement title;

  @AndroidFindBy(xpath = "//*[@text='Please provide values for the following attributes']")
  @iOSXCUITFindBy(accessibility = "Please provide values for the following attributes")
  public WebElement description;

  @AndroidFindBy(xpath = "//*[@content-desc=\"custom-value-input\"]")
  @iOSXCUITFindBy(accessibility = "custom-value-input")
  public WebElement customValueInput;

  @AndroidFindBy(xpath = "//*[@text='Cancel']")
  @iOSXCUITFindBy(accessibility = "Cancel")
  public WebElement cancelButton;

  @AndroidFindBy(xpath = "//*[@text='Done']")
  @iOSXCUITFindBy(accessibility = "Done")
  public WebElement doneButton;

  public WebElement attributeNameLabel(String attribute) {
    if (test.java.utility.Config.iOS_Devices.contains(test.java.utility.Config.Device_Type)) {
      return driver.findElementByAccessibilityId(attribute);
    } else {
      return driver.findElement(By.xpath("//*[@text='" + attribute + "']"));
    }
  }
}
