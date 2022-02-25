package pageObjects;

import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.StaleElementReferenceException;
import test.java.appModules.AppUtils;

import io.appium.java_client.AppiumDriver;
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
    private WebElement burgerMenuButton;

    @AndroidFindBy(xpath = "//*[@text=\"Scan\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"Scan\"]")
    public WebElement scanButton;

    @AndroidFindBy(xpath = "//*[@text=\"Recent\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Recent\"]")
    public WebElement recentEventsSection;

//    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE, iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
//    @AndroidFindBy(xpath = "//*[@text=\"Making secure connection...\"]")
//    @AndroidFindBy(xpath = "//*[@text=\'Making secure connection...\']")

//    @AndroidFindBy(xpath = "//*[@text='Making secure connection...']")
//    @AndroidFindBy(xpath = "//*[@text=Making secure connection...]")

//    @AndroidFindBy(xpath = "//*[@text=\"Making secure connection...]")
//    @AndroidFindBy(xpath = "//*[@text=Making secure connection...\"]")


//    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Making secure connection...\"]")


//    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\'Making secure connection...\']")
//    @AndroidFindBy(xpath = "//android.widget.TextView[@text=Making secure connection..]")

    //""Making secure connection..." Other"

//    @AndroidFindBy(xpath = "//android.widget.TextView[contains(text(), \"Making secure connection\")]")
//    @AndroidFindBy(xpath = "//android.widget.TextView[contains(text(), 'Making secure connection')]")


//android.view.ViewGroup[@content-desc="home-container"]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView[1]

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"home-container\"]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView[1]")

//    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"home-container\"]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView[1]")
//    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"home-container\"]//android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView[1]")
//    @AndroidFindBy(xpath = "//*[@text='Making secure connection...']")
    @iOSXCUITFindBy(accessibility = "Making secure connection...")
    //@iOSXCUITFindBy(xpath = "//*[@text=Making secure connection...]")
    public WebElement makingConnectionEvent;

    //AndroidFindBy(x[ath = android.view.ViewGroup[@content-desc="home-container"]/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView[1]);
    @AndroidFindBy(xpath = "//*[@text='You connected with \"connection-invitation\".']")
    @iOSXCUITFindBy(accessibility = "You connected with \"connection-invitation\".") // id?
    public WebElement commonConnectedEvent;

    @AndroidFindBy(xpath = "//*[@text='You connected with \"out-of-band-invitation\".']")
    @iOSXCUITFindBy(accessibility = "You connected with \"out-of-band-invitation\".") // id?
    public WebElement oobConnectedEvent;

    @AndroidFindBy(xpath = "//*[@text='You connected with \"push-connection-invitation\".']")
    @iOSXCUITFindBy(accessibility = "You connected with \"push-connection-invitation\".") // id?
    public WebElement pushConnectedEvent;

    @AndroidFindBy(xpath = "//*[@text=\"connection-invitation sent you a Credential Offer\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"connection-invitation sent you a Credential Offer\"]")
    public WebElement credentialOfferNotification;

    @AndroidFindBy(xpath = "//*[@text=\"connection-invitation sent you a Proof Request\"]")
    @iOSXCUITFindBy(xpath = "//*[@text=\"connection-invitation sent you a Proof Request\"]")
    public WebElement proofRequestNotification;

    @AndroidFindBy(xpath = "//*[@text=\"connection-invitation sent you a Question\"]")
    @iOSXCUITFindBy(xpath = "//*[@text=\"connection-invitation sent you a Question\"]")
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

    public WebElement namedConnectionEvent(String connectionName) {
        if (Config.iOS_Devices.contains(Config.Device_Type)) {
            return driver.findElementByAccessibilityId("You connected with \"" + connectionName + "\".");
        } else {
            return driver.findElement(By.xpath("//*[@text='You connected with \"" + connectionName + "\".']"));
        }
    }

    public void tapOnBurgerMenu() {
        try {
            burgerMenuButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
        catch (StaleElementReferenceException e) {}
        if (!AppUtils.isElementAbsent(driver, burgerMenuButton)) {
            System.out.println("Failed to open side menu, retrying");
            try { Thread.sleep(1000); }
            catch (InterruptedException e) {}
            burgerMenuButton.click();
        }
        System.out.println("Side menu should be visible now");
    }

    public void checkHome() {
        homeHeader.isDisplayed();
        burgerMenuButton.isDisplayed();
        scanButton.isDisplayed();
    }
}
