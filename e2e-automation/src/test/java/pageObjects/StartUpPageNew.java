package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import test.java.utility.Config;

import java.time.Duration;

public class StartUpPageNew {
  AppiumDriver driver;

  public StartUpPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//*[@text=\"Set Up\"]")
  @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name=\"Set Up\"])[6]")
  public WebElement setUpButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"lock-selection-or-text-touchable\"]/android.widget.ImageView")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"lock-selection-or-text-touchable\"]/XCUIElementTypeOther/XCUIElementTypeImage")
  public WebElement orText;

  @AndroidFindBy(xpath = "//*[@text=\"OK\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"OK\"]")
  public WebElement okButton;

  @AndroidFindBy(xpath = "//*[@text=\"Save\"]")
  public WebElement saveButton;

  @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"switch-environment-devrc\"]/android.widget.TextView")
  public WebElement devrcEnvButton;

  @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"switch-environment-devteam1\"]/android.widget.TextView")
  public WebElement devteam1EnvButton;

  @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"switch-environment-staging\"]/android.widget.TextView")
  public WebElement stagingEnvButton;

  @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"switch-environment-demo\"]/android.widget.TextView")
  public WebElement demoEnvButton;

  @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name=\"Use Staging Net An alternative network for app developers\"])[2]//XCUIElementTypeSwitch")
  public WebElement stagingNetSlider;

  @AndroidFindBy(xpath = "//*[@text=\"No thanks\"]")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"No thanks\"]")
  public WebElement noThanksButton;

  @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"eula-accept\"]/android.widget.TextView")
  @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"eula-accept\"]")
  public WebElement eulaAcceptButton;

  public void switchEnv() throws Exception {
    if (Config.iOS_Devices.contains(Config.Device_Type)) { // iOS: cannot enter env switching screen so use staging net slider
      stagingNetSlider.click();
    }
    else { // Android: set env using env switching screen
      new TouchAction(driver)
        .longPress(
          new LongPressOptions()
            .withElement(ElementOption.element(orText))
            .withDuration(Duration.ofMillis(1000))
        )
        .release()
        .perform();

      for (int i = 0; i < 10; i++) {
        Thread.sleep(100);
        orText.click();
      }

      okButton.click();
      switch (Config.VERITY_ENV) {
        case "demo":
          demoEnvButton.click();
          break;
        case "dev-rc":
          devrcEnvButton.click();
          break;
        case "team1":
          devteam1EnvButton.click();
          break;
        case "staging":
          stagingEnvButton.click();
          break;
      }
      Thread.sleep(2000);
      saveButton.click();
    }
    // steps for both platforms
    noThanksButton.click();
    eulaAcceptButton.click();
    Thread.sleep(10000); // FIXME - probably it's a bug in CM
  }
}
