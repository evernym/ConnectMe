package pageObjects;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class InvitationPageNew {
  AppiumDriver driver;

  public InvitationPageNew(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    this.driver = driver;
  }

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"invitation-text-container-message-title\"]")
  @iOSXCUITFindBy(accessibility = "invitation-text-container-message-title")
  public WebElement title;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"invitation-avatars-invitee-image\"]")
  @iOSXCUITFindBy(accessibility = "invitation-avatars-invitee")
  public WebElement inviteeAvatar;

  @AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"invitation-avatars-inviter-image\"]")
  @iOSXCUITFindBy(accessibility = "invitation-avatars-inviter")
  public WebElement inviterAvatar;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"invitation-deny\"]")
  @iOSXCUITFindBy(accessibility = "invitation-deny")
  public WebElement denyButton;

  @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"invitation-accept\"]")
  @iOSXCUITFindBy(accessibility = "invitation-accept")
  public WebElement connectButton;
}
